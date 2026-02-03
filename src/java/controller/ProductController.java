package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import model.Product;
import model.User;
import service.ProductService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@WebServlet(name = "ProductController", urlPatterns = {"/admin/products", "/admin/products/*"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,    // 1 MB
    maxFileSize = 1024 * 1024 * 5,      // 5 MB
    maxRequestSize = 1024 * 1024 * 25   // 25 MB
)
public class ProductController extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() throws ServletException {
        super.init();
        productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String currentUserRole = (String) session.getAttribute("roleName");
        
        // Check if user has admin permission
        if (!"Admin".equals(currentUserRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập khu vực này");
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            pathInfo = "/list";
        }

        try {
            switch (pathInfo) {
                case "/list":
                
                    showProductManagement(request, response);
                    break;
                case "/view":
                    showProductView(request, response);
                    break;
                case "/new":
                    showProductForm(request, response, null);
                    break;
                case "/edit":
                    try {
                        String idParam = request.getParameter("id");
                        if (idParam == null || idParam.isEmpty()) {
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu ID sản phẩm");
                            return;
                        }
                        int editProductId = Integer.parseInt(idParam);
                        Product editProduct = productService.getProductById(editProductId);
                        if (editProduct == null) {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sản phẩm");
                            return;
                        }
                        showProductForm(request, response, editProduct);
                    } catch (NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID sản phẩm không hợp lệ");
                        return;
                    }
                    break;
                case "/export":
                    exportProducts(request, response);
                    break;
                case "/template":
                    downloadTemplate(request, response);
                    break;
                default:
                    showProductManagement(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/common/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String currentUserRole = (String) session.getAttribute("roleName");
        
        // Check if user has admin permission
        if (!"Admin".equals(currentUserRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập");
            return;
        }

        String action = request.getParameter("action");
        
        try {
            switch (action) {
                case "create":
                    createProduct(request, response);
                    break;
                case "update":
                    updateProduct(request, response);
                    break;
                case "delete":
                    deleteProduct(request, response);
                    break;
                case "toggle-status":
                    toggleProductStatus(request, response);
                    break;
                case "import":
                    importProducts(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/admin/products");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/products");
        }
    }

    /**
     * Show product management list
     */
    private void showProductManagement(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Get parameters
            int page = 1;
            int pageSize = 10;
            String searchTerm = request.getParameter("search");
            String categoryParam = request.getParameter("category");
            String statusParam = request.getParameter("status");
            
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }
            
            Integer categoryId = null;
            if (categoryParam != null && !categoryParam.isEmpty()) {
                categoryId = Integer.parseInt(categoryParam);
            }
            
            Boolean isActive = null;
            if ("active".equals(statusParam)) {
                isActive = true;
            } else if ("inactive".equals(statusParam)) {
                isActive = false;
            }
            
            // Get sort parameters
            String sortBy = request.getParameter("sortBy");
            String sortOrder = request.getParameter("sortOrder");
            
            // Get data with sorting
            List<Product> products = productService.getAllProducts(page, pageSize, searchTerm, categoryId, isActive, sortBy, sortOrder);
            int totalCount = productService.getTotalProductsCount(searchTerm, categoryId, isActive);
            int totalPages = productService.getTotalPages(totalCount, pageSize);
            
            // Get dropdown data
            List<Object[]> categories = productService.getCategories();
            
            // Set attributes
            request.setAttribute("products", products);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("searchTerm", searchTerm);
            request.setAttribute("selectedCategory", categoryId);
            request.setAttribute("selectedStatus", statusParam);
            request.setAttribute("categories", categories);
            
            request.getRequestDispatcher("/WEB-INF/views/admin/product-list.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải danh sách sản phẩm: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/common/error.jsp").forward(request, response);
        }
    }
    
    /**
     * Show product view
     */
    private void showProductView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int productId = Integer.parseInt(request.getParameter("id"));
            Product product = productService.getProductById(productId);
            
            if (product == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sản phẩm");
                return;
            }
            
            // Get BOM items for this product
            dao.ProductBOMDAO bomDAO = new dao.ProductBOMDAO();
            List<model.BOMItem> bomItems = bomDAO.getBOMItemsByProductId(productId);
            
            // Format recipe text (format: "18 g Cà phê Espresso xay + 180 ml Sữa tươi nguyên kem + 5 g Đường trắng")
            String formattedRecipe = productService.formatRecipe(bomItems);
            
            // Check for recipe success message from session
            HttpSession session = request.getSession(false);
            if (session != null) {
                String recipeSuccessMessage = (String) session.getAttribute("recipeSuccessMessage");
                if (recipeSuccessMessage != null) {
                    request.setAttribute("recipeSuccessMessage", recipeSuccessMessage);
                    session.removeAttribute("recipeSuccessMessage"); // Remove after getting
                }
            }
            
            request.setAttribute("product", product);
            request.setAttribute("bomItems", bomItems);
            request.setAttribute("formattedRecipe", formattedRecipe);
            request.getRequestDispatcher("/WEB-INF/views/admin/product-view.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải chi tiết sản phẩm: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/common/error.jsp").forward(request, response);
        }
    }
    
    /**
     * Show product form (for create or edit)
     */
    private void showProductForm(HttpServletRequest request, HttpServletResponse response, Product product)
            throws ServletException, IOException {
        try {
            // Get dropdown data
            List<Object[]> categories = productService.getCategories();
            
            // Check if data is loaded
            if (categories == null || categories.isEmpty()) {
                throw new Exception("Không thể tải danh mục sản phẩm");
            }
            
            request.setAttribute("categories", categories);
            request.setAttribute("product", product);
            
            request.getRequestDispatcher("/WEB-INF/views/admin/product-form.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", "Lỗi khi tải form sản phẩm: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/products");
        }
    }
    
    /**
     * Create new product
     */
    private void createProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            // Get form data
            String productName = request.getParameter("productName");
            String description = request.getParameter("description");
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            BigDecimal price = new BigDecimal(request.getParameter("price"));
            boolean isActive = "on".equals(request.getParameter("isActive"));
            
            // Handle image upload
            String imageUrl = handleImageUpload(request);
            
            // Create product (without ID and timestamp)
            Product newProduct = new Product(productName, description, imageUrl, categoryId, price, isActive);
            boolean success = productService.createProduct(newProduct);
            
            if (success) {
                session.setAttribute("successMessage", "Tạo sản phẩm thành công!");
                response.sendRedirect(request.getContextPath() + "/admin/products");
            } else {
                session.setAttribute("errorMessage", "Không thể tạo sản phẩm. Vui lòng thử lại.");
                response.sendRedirect(request.getContextPath() + "/admin/products/new");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/products/new");
        }
    }
    
    /**
     * Update existing product
     */
    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            // Get form data
            int productId = Integer.parseInt(request.getParameter("productId"));
            String productName = request.getParameter("productName");
            String description = request.getParameter("description");
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            BigDecimal price = new BigDecimal(request.getParameter("price"));
            boolean isActive = "on".equals(request.getParameter("isActive"));
            
            // Get existing product to check for old image
            Product existingProduct = productService.getProductById(productId);
            
            // Handle image upload
            String imageUrl = handleImageUpload(request);
            if (imageUrl == null || imageUrl.isEmpty()) {
                // No new image, keep old one
                imageUrl = existingProduct.getImageUrl();
            } else {
                // New image uploaded, delete old one
                deleteOldImage(existingProduct.getImageUrl());
            }
            
            // Update product (set ID manually)
            Product product = new Product(productName, description, imageUrl, categoryId, price, isActive);
            product.setProductID(productId);
            boolean success = productService.updateProduct(product);
            
            if (success) {
                session.setAttribute("successMessage", "Cập nhật sản phẩm thành công!");
                response.sendRedirect(request.getContextPath() + "/admin/products/view?id=" + productId);
            } else {
                session.setAttribute("errorMessage", "Không thể cập nhật sản phẩm. Vui lòng thử lại.");
                response.sendRedirect(request.getContextPath() + "/admin/products/edit?id=" + productId);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/products");
        }
    }
    
    /**
     * Delete product
     */
    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            int productId = Integer.parseInt(request.getParameter("id"));
            
            // Get product to delete image
            Product product = productService.getProductById(productId);
            
            boolean success = productService.deleteProduct(productId);
            
            if (success) {
                // Delete image file
                deleteOldImage(product.getImageUrl());
                session.setAttribute("successMessage", "Xóa sản phẩm thành công!");
            } else {
                session.setAttribute("errorMessage", "Không thể xóa sản phẩm. Vui lòng thử lại.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/products");
    }
    
    /**
     * Toggle product status (active/inactive)
     */
    private void toggleProductStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            int productId = Integer.parseInt(request.getParameter("id"));
            boolean success = productService.toggleProductStatus(productId);
            
            if (success) {
                session.setAttribute("successMessage", "Cập nhật trạng thái sản phẩm thành công!");
            } else {
                session.setAttribute("errorMessage", "Không thể cập nhật trạng thái sản phẩm.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/products");
    }
    
    /**
     * Handle image upload and return the image URL
     */
    private String handleImageUpload(HttpServletRequest request) throws Exception {
        Part imagePart = request.getPart("imageFile");
        
        if (imagePart == null || imagePart.getSize() == 0) {
            return null;
        }
        
        // Validate file type
        String contentType = imagePart.getContentType();
        if (!contentType.startsWith("image/")) {
            throw new Exception("File phải là hình ảnh");
        }
        
        // Get file extension
        String originalFileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        
        // Validate extension
        if (!fileExtension.matches("\\.(jpg|jpeg|png|gif)$")) {
            throw new Exception("Chỉ hỗ trợ định dạng: jpg, jpeg, png, gif");
        }
        
        // Generate unique filename
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
        
        // Get upload directory - use web folder instead of build folder
        String realPath = request.getServletContext().getRealPath("");
        String webPath;
        
        if (realPath.contains("build")) {
            webPath = realPath.substring(0, realPath.indexOf("build")) + "web";
        } else {
            webPath = realPath;
        }
        
        String uploadPath = webPath + File.separator + "uploads" + File.separator + "products";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        // Save file to web/uploads/products
        String filePath = uploadPath + File.separator + uniqueFileName;
        imagePart.write(filePath);
        
        // Also copy to build folder for immediate display (if different from web)
        if (realPath.contains("build")) {
            String buildUploadPath = realPath + File.separator + "uploads" + File.separator + "products";
            File buildUploadDir = new File(buildUploadPath);
            if (!buildUploadDir.exists()) {
                buildUploadDir.mkdirs();
            }
            String buildFilePath = buildUploadPath + File.separator + uniqueFileName;
            java.nio.file.Files.copy(
                new File(filePath).toPath(),
                new File(buildFilePath).toPath(),
                java.nio.file.StandardCopyOption.REPLACE_EXISTING
            );
        }
        
        // Return relative URL
        return "/uploads/products/" + uniqueFileName;
    }
    
    /**
     * Delete old image file from server
     */
    private void deleteOldImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return;
        }
        
        try {
            String realPath = getServletContext().getRealPath("") + imageUrl.replace("/", File.separator);
            File file = new File(realPath);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            System.err.println("Error deleting old image: " + e.getMessage());
        }
    }
    
    /**
     * Export all products to Excel file
     */
    private void exportProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Get all products
            List<Product> products = productService.getAllProducts(1, Integer.MAX_VALUE, "", null, null);
            
            // Create workbook and sheet
            XSSFWorkbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Products");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Tên sản phẩm", "Mô tả", "Danh mục ID", "Giá", "Trạng thái", "URL ảnh"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            
            // Fill data rows
            int rowNum = 1;
            for (Product product : products) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(product.getProductID());
                row.createCell(1).setCellValue(product.getProductName());
                row.createCell(2).setCellValue(product.getDescription());
                row.createCell(3).setCellValue(product.getCategoryID());
                row.createCell(4).setCellValue(product.getPrice().doubleValue());
                row.createCell(5).setCellValue(product.isActive());
                row.createCell(6).setCellValue(product.getImageUrl());
            }
            
            // Set response headers
            String filename = "products_" + System.currentTimeMillis() + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            
            // Write to output stream
            workbook.write(response.getOutputStream());
            workbook.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", "Lỗi khi export: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/products");
        }
    }
    
    /**
     * Download Excel template for product import
     */
    private void downloadTemplate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Create empty workbook with headers only
            XSSFWorkbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Product Template");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Tên sản phẩm", "Mô tả", "Danh mục ID", "Giá", "Trạng thái", "URL ảnh"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            
            // Optional: Add example row
            Row exampleRow = sheet.createRow(1);
            exampleRow.createCell(0).setCellValue("Cà phê Arabica");
            exampleRow.createCell(1).setCellValue("Cà phê hạt rang nguyên chất");
            exampleRow.createCell(2).setCellValue(1);
            exampleRow.createCell(3).setCellValue(150000);
            exampleRow.createCell(4).setCellValue(true);
            exampleRow.createCell(5).setCellValue("/uploads/products/coffee.jpg");
            
            // Set response headers
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"product_template.xlsx\"");
            
            // Write to output stream
            workbook.write(response.getOutputStream());
            workbook.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", "Lỗi khi tạo template: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/products");
        }
    }
    
    /**
     * Import products from Excel file
     */
    private void importProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            // Get uploaded file
            Part filePart = request.getPart("importFile");
            
            if (filePart == null || filePart.getSize() == 0) {
                session.setAttribute("errorMessage", "Vui lòng chọn file để import");
                response.sendRedirect(request.getContextPath() + "/admin/products");
                return;
            }
            
            // Validate file extension
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
                session.setAttribute("errorMessage", "Chỉ chấp nhận file Excel (.xlsx hoặc .xls)");
                response.sendRedirect(request.getContextPath() + "/admin/products");
                return;
            }
            
            // Open workbook
            Workbook workbook = WorkbookFactory.create(filePart.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            
            // Track results
            int successCount = 0;
            int errorCount = 0;
            StringBuilder errorMessages = new StringBuilder();
            
            // Skip header row, iterate data rows
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                try {
                    // Extract data
                    String productName = getCellValueAsString(row.getCell(0));
                    String description = getCellValueAsString(row.getCell(1));
                    int categoryId = (int) row.getCell(2).getNumericCellValue();
                    BigDecimal price = new BigDecimal(row.getCell(3).getNumericCellValue());
                    boolean isActive = row.getCell(4).getBooleanCellValue();
                    String imageUrl = getCellValueAsString(row.getCell(5));
                    
                    // Validate required fields
                    if (productName == null || productName.trim().isEmpty()) {
                        errorMessages.append("Dòng ").append(i + 1).append(": Tên sản phẩm không được để trống<br>");
                        errorCount++;
                        continue;
                    }
                    
                    // Create product
                    Product product = new Product();
                    product.setProductName(productName.trim());
                    product.setDescription(description);
                    product.setCategoryID(categoryId);
                    product.setPrice(price);
                    product.setActive(isActive);
                    product.setImageUrl(imageUrl);
                    
                    // Save product (will check for duplicates)
                    productService.createProduct(product);
                    successCount++;
                    
                } catch (Exception e) {
                    errorMessages.append("Dòng ").append(i + 1).append(": ").append(e.getMessage()).append("<br>");
                    errorCount++;
                }
            }
            
            workbook.close();
            
            // Set result message
            if (successCount > 0) {
                String message = "Import thành công " + successCount + " sản phẩm";
                if (errorCount > 0) {
                    message += ". Có " + errorCount + " lỗi:<br>" + errorMessages.toString();
                }
                session.setAttribute(errorCount > 0 ? "errorMessage" : "successMessage", message);
            } else {
                session.setAttribute("errorMessage", "Không có sản phẩm nào được import:<br>" + errorMessages.toString());
            }
            
            response.sendRedirect(request.getContextPath() + "/admin/products");
            
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi khi import: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/products");
        }
    }
    
    // Helper method for Excel cell reading
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
}

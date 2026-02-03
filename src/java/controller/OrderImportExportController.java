package controller;

import model.Order;
import model.OrderDetail;
import model.Product;
import model.Setting;
import service.OrderService;
import service.SettingService;
import dao.OrderDAO;
import dao.ProductDAO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

/**
 * Controller for Order Import/Export functionality with OrderDetails
 */
@WebServlet(name = "OrderImportExportController", urlPatterns = {"/barista/orders/export", "/barista/orders/import", "/barista/orders/template"})
@MultipartConfig(maxFileSize = 16177215) // 15MB max file size
public class OrderImportExportController extends HttpServlet {

    private OrderService orderService;
    private SettingService settingService;
    private OrderDAO orderDAO;
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        orderService = new OrderService();
        settingService = new SettingService();
        orderDAO = new OrderDAO();
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        switch (path) {
            case "/barista/orders/export":
                exportOrders(request, response);
                break;
            case "/barista/orders/template":
                downloadTemplate(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/barista/orders/import".equals(path)) {
            importOrders(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Export orders with their order details to Excel
     */
    private void exportOrders(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // Get all orders
        List<Order> orders = orderService.getAllOrders();

        // Create workbook
        Workbook workbook = new XSSFWorkbook();
        
        // Create Orders sheet
        Sheet ordersSheet = workbook.createSheet("Orders");
        createOrdersSheet(workbook, ordersSheet, orders);
        
        // Create OrderDetails sheet
        Sheet detailsSheet = workbook.createSheet("OrderDetails");
        createOrderDetailsSheet(workbook, detailsSheet, orders);

        // Set response headers
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=orders_export_" + System.currentTimeMillis() + ".xlsx");

        // Write to output stream
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    /**
     * Create Orders sheet with styling
     */
    private void createOrdersSheet(Workbook workbook, Sheet sheet, List<Order> orders) {
        // Create header style
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Order ID", "Shop ID", "Shop Name", "Created By ID", "Created By Name",
                "Status ID", "Status Name", "Created At"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Create data rows
        int rowNum = 1;
        for (Order order : orders) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(order.getOrderID());
            row.createCell(1).setCellValue(order.getShopID());
            row.createCell(2).setCellValue(order.getShopName() != null ? order.getShopName() : "");
            row.createCell(3).setCellValue(order.getCreatedBy());
            row.createCell(4).setCellValue(order.getCreatedByName() != null ? order.getCreatedByName() : "");
            row.createCell(5).setCellValue(order.getStatusID());
            row.createCell(6).setCellValue(order.getStatusName() != null ? order.getStatusName() : "");
            row.createCell(7).setCellValue(order.getCreatedAt() != null ? order.getCreatedAt().toString() : "");
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    /**
     * Create OrderDetails sheet with all order details
     */
    private void createOrderDetailsSheet(Workbook workbook, Sheet sheet, List<Order> orders) {
        // Create header style
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Order ID", "Product ID", "Product Name", "Quantity", "Price"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Create data rows
        int rowNum = 1;
        for (Order order : orders) {
            List<OrderDetail> details = orderDAO.getOrderDetails(order.getOrderID());
            for (OrderDetail detail : details) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(order.getOrderID());
                row.createCell(1).setCellValue(detail.getProductID());
                
                // Get product name
                Product product = productDAO.getProductById(detail.getProductID());
                row.createCell(2).setCellValue(product != null ? product.getProductName() : "");
                
                row.createCell(3).setCellValue(detail.getQuantity());
                row.createCell(4).setCellValue(detail.getPrice().doubleValue());
            }
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    /**
     * Download order import template with Orders and OrderDetails sheets
     */
    private void downloadTemplate(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // Get order statuses for reference
        List<Setting> statuses = settingService.getSettingsByType("OrderStatus");
        
        // Get all products for reference
        List<Product> products = productDAO.getAllProducts(1, 1000, null, null, true, null, null);

        // Create workbook
        Workbook workbook = new XSSFWorkbook();
        Sheet ordersSheet = workbook.createSheet("Orders");
        Sheet detailsSheet = workbook.createSheet("OrderDetails");
        Sheet referenceSheet = workbook.createSheet("Reference");

        // === Create Orders Sheet ===
        CellStyle headerStyle = createHeaderStyle(workbook, IndexedColors.LIGHT_BLUE);
        CellStyle instructionStyle = createInstructionStyle(workbook);

        // Orders header row
        Row ordersHeaderRow = ordersSheet.createRow(0);
        String[] ordersHeaders = {"Order ID (auto)*", "Shop ID*", "Created By ID*", "Status ID (optional)"};

        for (int i = 0; i < ordersHeaders.length; i++) {
            Cell cell = ordersHeaderRow.createCell(i);
            cell.setCellValue(ordersHeaders[i]);
            cell.setCellStyle(headerStyle);
        }

        // Orders instruction row
        Row ordersInstructionRow = ordersSheet.createRow(1);
        ordersInstructionRow.createCell(0).setCellValue("1");
        ordersInstructionRow.createCell(1).setCellValue("1");
        ordersInstructionRow.createCell(2).setCellValue("7");
        ordersInstructionRow.createCell(3).setCellValue("29");
        
        Cell instructionCell = ordersInstructionRow.createCell(5);
        instructionCell.setCellValue("Order ID sẽ tự động tạo. Status ID mặc định là 29 (New)");
        instructionCell.setCellStyle(instructionStyle);

        // Example data row
        Row exampleOrderRow = ordersSheet.createRow(2);
        exampleOrderRow.createCell(0).setCellValue("2");
        exampleOrderRow.createCell(1).setCellValue("1");
        exampleOrderRow.createCell(2).setCellValue("7");
        exampleOrderRow.createCell(3).setCellValue("");

        // Auto-size orders sheet columns
        for (int i = 0; i < ordersHeaders.length; i++) {
            ordersSheet.autoSizeColumn(i);
        }

        // === Create OrderDetails Sheet ===
        CellStyle detailsHeaderStyle = createHeaderStyle(workbook, IndexedColors.LIGHT_CORNFLOWER_BLUE);

        // OrderDetails header row
        Row detailsHeaderRow = detailsSheet.createRow(0);
        String[] detailsHeaders = {"Order ID*", "Product ID*", "Product Name (reference)", "Quantity*", "Price*"};

        for (int i = 0; i < detailsHeaders.length; i++) {
            Cell cell = detailsHeaderRow.createCell(i);
            cell.setCellValue(detailsHeaders[i]);
            cell.setCellStyle(detailsHeaderStyle);
        }

        // OrderDetails instruction row
        Row detailsInstructionRow = detailsSheet.createRow(1);
        detailsInstructionRow.createCell(0).setCellValue("1");
        detailsInstructionRow.createCell(1).setCellValue("1");
        detailsInstructionRow.createCell(2).setCellValue("Espresso");
        detailsInstructionRow.createCell(3).setCellValue("2");
        detailsInstructionRow.createCell(4).setCellValue("50000");
        
        Cell detailsInstructionCell = detailsInstructionRow.createCell(6);
        detailsInstructionCell.setCellValue("Order ID phải khớp với Orders sheet");
        detailsInstructionCell.setCellStyle(instructionStyle);

        // Example data rows
        Row exampleDetailRow1 = detailsSheet.createRow(2);
        exampleDetailRow1.createCell(0).setCellValue("1");
        exampleDetailRow1.createCell(1).setCellValue("2");
        exampleDetailRow1.createCell(2).setCellValue("Latte");
        exampleDetailRow1.createCell(3).setCellValue("1");
        exampleDetailRow1.createCell(4).setCellValue("45000");

        Row exampleDetailRow2 = detailsSheet.createRow(3);
        exampleDetailRow2.createCell(0).setCellValue("2");
        exampleDetailRow2.createCell(1).setCellValue("1");
        exampleDetailRow2.createCell(2).setCellValue("Espresso");
        exampleDetailRow2.createCell(3).setCellValue("3");
        exampleDetailRow2.createCell(4).setCellValue("50000");

        // Auto-size details sheet columns
        for (int i = 0; i < detailsHeaders.length; i++) {
            detailsSheet.autoSizeColumn(i);
        }

        // === Create Reference Sheet ===
        CellStyle referenceHeaderStyle = createHeaderStyle(workbook, IndexedColors.GREY_25_PERCENT);

        // Order Statuses reference
        Row statusHeaderRow = referenceSheet.createRow(0);
        statusHeaderRow.createCell(0).setCellValue("Order Status ID");
        statusHeaderRow.createCell(1).setCellValue("Status Name");
        statusHeaderRow.getCell(0).setCellStyle(referenceHeaderStyle);
        statusHeaderRow.getCell(1).setCellStyle(referenceHeaderStyle);

        int rowNum = 1;
        for (Setting status : statuses) {
            Row row = referenceSheet.createRow(rowNum++);
            row.createCell(0).setCellValue(status.getSettingID());
            row.createCell(1).setCellValue(status.getValue());
        }

        // Products reference
        rowNum += 2; // Skip 2 rows
        Row productHeaderRow = referenceSheet.createRow(rowNum++);
        productHeaderRow.createCell(0).setCellValue("Product ID");
        productHeaderRow.createCell(1).setCellValue("Product Name");
        productHeaderRow.createCell(2).setCellValue("Price");
        productHeaderRow.getCell(0).setCellStyle(referenceHeaderStyle);
        productHeaderRow.getCell(1).setCellStyle(referenceHeaderStyle);
        productHeaderRow.getCell(2).setCellStyle(referenceHeaderStyle);

        for (Product product : products) {
            Row row = referenceSheet.createRow(rowNum++);
            row.createCell(0).setCellValue(product.getProductID());
            row.createCell(1).setCellValue(product.getProductName());
            row.createCell(2).setCellValue(product.getPrice().doubleValue());
        }

        // Auto-size reference sheet columns
        for (int i = 0; i < 3; i++) {
            referenceSheet.autoSizeColumn(i);
        }

        // Set response headers
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=order_import_template.xlsx");

        // Write to output stream
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    /**
     * Import orders with order details from Excel file
     */
    private void importOrders(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get uploaded file
        Part filePart = request.getPart("file");

        if (filePart == null) {
            request.getSession().setAttribute("errorMessage", "Vui lòng chọn file để import");
            response.sendRedirect(request.getContextPath() + "/barista/orders");
            return;
        }

        // Get "New" status ID (default status for imported orders)
        List<Setting> statuses = settingService.getSettingsByType("OrderStatus");
        Integer newStatusID = null;
        for (Setting status : statuses) {
            if ("New".equals(status.getValue())) {
                newStatusID = status.getSettingID();
                break;
            }
        }

        if (newStatusID == null) {
            newStatusID = 29; // Fallback to hardcoded ID if not found
        }

        InputStream fileContent = filePart.getInputStream();
        Workbook workbook = new XSSFWorkbook(fileContent);

        // Read Orders sheet
        Sheet ordersSheet = workbook.getSheetAt(0);
        Map<Integer, Order> ordersMap = readOrdersSheet(ordersSheet, newStatusID);

        // Read OrderDetails sheet
        Sheet detailsSheet = workbook.getSheetAt(1);
        Map<Integer, List<OrderDetail>> detailsMap = readOrderDetailsSheet(detailsSheet);

        workbook.close();
        fileContent.close();

        // Validate and save
        List<String> errors = new ArrayList<>();
        int savedOrderCount = 0;
        int savedDetailCount = 0;

        for (Map.Entry<Integer, Order> entry : ordersMap.entrySet()) {
            Integer tempOrderID = entry.getKey();
            Order order = entry.getValue();

            // Create order
            String error = orderService.createOrder(order);
            if (error != null) {
                errors.add("Lỗi khi tạo order #" + tempOrderID + ": " + error);
                continue;
            }

            savedOrderCount++;
            int newOrderID = order.getOrderID();

            // Create order details
            List<OrderDetail> details = detailsMap.get(tempOrderID);
            if (details != null) {
                for (OrderDetail detail : details) {
                    detail.setOrderID(newOrderID);
                    boolean success = orderDAO.insertOrderDetail(detail);
                    if (success) {
                        savedDetailCount++;
                    } else {
                        errors.add("Lỗi khi tạo order detail cho order #" + tempOrderID + ", product #" + detail.getProductID());
                    }
                }
            }
        }

        // Set success/error messages
        if (savedOrderCount > 0) {
            request.getSession().setAttribute("successMessage",
                    "Import thành công " + savedOrderCount + " đơn hàng và " + savedDetailCount + " chi tiết đơn hàng");
        }

        if (!errors.isEmpty()) {
            StringBuilder errorMsg = new StringBuilder("Có " + errors.size() + " lỗi:<br>");
            for (int i = 0; i < Math.min(errors.size(), 10); i++) {
                errorMsg.append("- ").append(errors.get(i)).append("<br>");
            }
            if (errors.size() > 10) {
                errorMsg.append("... và ").append(errors.size() - 10).append(" lỗi khác");
            }
            request.getSession().setAttribute("errorMessage", errorMsg.toString());
        }

        response.sendRedirect(request.getContextPath() + "/barista/orders");
    }

    /**
     * Read Orders sheet and return map of temporary ID to Order
     */
    private Map<Integer, Order> readOrdersSheet(Sheet sheet, Integer defaultStatusID) {
        Map<Integer, Order> ordersMap = new HashMap<>();
        Iterator<Row> rowIterator = sheet.iterator();

        // Skip header row
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        // Skip instruction row
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            if (isRowEmpty(row)) {
                continue;
            }

            try {
                int tempOrderID = (int) getNumericCellValue(row.getCell(0));
                int shopID = (int) getNumericCellValue(row.getCell(1));
                int createdBy = (int) getNumericCellValue(row.getCell(2));

                // Status ID is optional, default to "New"
                Integer statusID = defaultStatusID;
                Cell statusCell = row.getCell(3);
                if (statusCell != null && statusCell.getCellType() != CellType.BLANK) {
                    statusID = (int) getNumericCellValue(statusCell);
                }

                // Validate
                if (tempOrderID <= 0 || shopID <= 0 || createdBy <= 0) {
                    continue;
                }

                // Create order
                Order order = new Order();
                order.setShopID(shopID);
                order.setCreatedBy(createdBy);
                order.setStatusID(statusID != null && statusID > 0 ? statusID : defaultStatusID);

                ordersMap.put(tempOrderID, order);

            } catch (Exception e) {
                // Skip invalid rows
            }
        }

        return ordersMap;
    }

    /**
     * Read OrderDetails sheet and return map of Order ID to list of OrderDetails
     */
    private Map<Integer, List<OrderDetail>> readOrderDetailsSheet(Sheet sheet) {
        Map<Integer, List<OrderDetail>> detailsMap = new HashMap<>();
        Iterator<Row> rowIterator = sheet.iterator();

        // Skip header row
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        // Skip instruction row
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            if (isRowEmpty(row)) {
                continue;
            }

            try {
                int orderID = (int) getNumericCellValue(row.getCell(0));
                int productID = (int) getNumericCellValue(row.getCell(1));
                int quantity = (int) getNumericCellValue(row.getCell(3));
                double price = getNumericCellValue(row.getCell(4));

                // Validate
                if (orderID <= 0 || productID <= 0 || quantity <= 0 || price <= 0) {
                    continue;
                }

                // Create order detail
                OrderDetail detail = new OrderDetail();
                detail.setProductID(productID);
                detail.setQuantity(quantity);
                detail.setPrice(BigDecimal.valueOf(price));

                // Add to map
                detailsMap.computeIfAbsent(orderID, k -> new ArrayList<>()).add(detail);

            } catch (Exception e) {
                // Skip invalid rows
            }
        }

        return detailsMap;
    }

    /**
     * Create header style with specified color
     */
    private CellStyle createHeaderStyle(Workbook workbook, IndexedColors color) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setFillForegroundColor(color.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    /**
     * Create instruction style
     */
    private CellStyle createInstructionStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setItalic(true);
        font.setColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFont(font);
        return style;
    }

    /**
     * Get numeric value from cell
     */
    private double getNumericCellValue(Cell cell) {
        if (cell == null) {
            return 0;
        }

        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return Double.parseDouble(cell.getStringCellValue().trim());
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        return 0;
    }

    /**
     * Check if row is empty
     */
    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }

        for (int i = 0; i < 4; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }

        return true;
    }
}

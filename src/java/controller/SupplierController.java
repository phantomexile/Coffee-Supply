/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.Supplier;
import model.User;
import dao.SupplierDAO;
import dao.IngredientDAO;
import service.SupplierService;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Map;


@WebServlet(name = "SupplierController", urlPatterns = {"/admin/supplier"})
public class SupplierController extends HttpServlet {

    private SupplierService supplierService;
    private IngredientDAO ingredientDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        supplierService = new SupplierService();
        ingredientDAO = new IngredientDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String currentUserRole = (String) session.getAttribute("roleName");
        
        // Check if user has admin permission
        if (!"Admin".equals(currentUserRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập khu vực này");
            return;
        }

        String action = request.getParameter("action");
        if (action == null || action.trim().isEmpty()) {
            action = "list";
        }

        try {
            switch (action) {
                case "list":
                    showSupplierList(request, response);
                    break;
                case "details":
                    showSupplierDetails(request, response);
                    break;
                case "new":
                    showNewSupplierForm(request, response);
                    break;
                case "edit":
                    showEditSupplierForm(request, response);
                    break;
                case "toggle-status":
                    toggleSupplierStatus(request, response);
                    break;
                default:
                    showSupplierList(request, response);
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
        
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String currentUserRole = (String) session.getAttribute("roleName");
        
        // Check if user has admin permission
        if (!"Admin".equals(currentUserRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập khu vực này");
            return;
        }

        String action = request.getParameter("action");
        if (action == null || action.trim().isEmpty()) {
            action = "list";
        }

        try {
            switch (action) {
                case "new":
                    createSupplier(request, response);
                    break;
                case "edit":
                    updateSupplier(request, response);
                    break;
                default:
                    showSupplierList(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/common/error.jsp").forward(request, response);
        }
    }

    /**
     * Display supplier list
     */
    private void showSupplierList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String searchKeyword = request.getParameter("search");
        if (searchKeyword != null && searchKeyword.trim().isEmpty()) {
            searchKeyword = null;
        }

        String statusFilter = request.getParameter("status");
        if (statusFilter != null && statusFilter.trim().isEmpty()) {
            statusFilter = null;
        }
        
        
        int page = 1;
        int pageSize = 5; // Set page size to 5 as requested
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        
        
        List<Supplier> suppliers = supplierService.getSuppliers(page, pageSize, statusFilter, searchKeyword);
        
        // Calculate pagination info
        int totalCount = supplierService.getTotalSuppliersCount(statusFilter, searchKeyword);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        
        request.setAttribute("searchKeyword", searchKeyword);
        request.setAttribute("statusFilter", statusFilter);
        
        request.setAttribute("suppliers", suppliers);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", pageSize);
        request.getRequestDispatcher("/WEB-INF/views/admin/supplier-list.jsp").forward(request, response);
    }

    /**
     * Display supplier details
     */
    private void showSupplierDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/supplier?action=list");
            return;
        }
        
        try {
            int supplierId = Integer.parseInt(idParam);
            Supplier supplier = supplierService.getSupplierById(supplierId);
            
            if (supplier == null) {
                request.setAttribute("error", "Không tìm thấy nhà cung cấp");
                showSupplierList(request, response);
                return;
            }
            
            // Get ingredients from this supplier
            List<model.Ingredient> ingredients = ingredientDAO.getIngredientsBySupplier(supplierId);
            
            request.setAttribute("supplier", supplier);
            request.setAttribute("ingredients", ingredients);
            request.getRequestDispatcher("/WEB-INF/views/admin/supplier-details.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/supplier?action=list");
        }
    }

    /**
     * Show form to create new supplier
     */
    private void showNewSupplierForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setAttribute("action", "new");
        request.getRequestDispatcher("/WEB-INF/views/admin/supplier-form.jsp").forward(request, response);
    }

    /**
     * Show form to edit existing supplier
     */
    private void showEditSupplierForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/supplier?action=list");
            return;
        }
        
        try {
            int supplierId = Integer.parseInt(idParam);
            Supplier supplier = supplierService.getSupplierById(supplierId);
            
            if (supplier == null) {
                request.setAttribute("error", "Không tìm thấy nhà cung cấp");
                showSupplierList(request, response);
                return;
            }
            
            request.setAttribute("supplier", supplier);
            request.setAttribute("action", "edit");
            request.getRequestDispatcher("/WEB-INF/views/admin/supplier-form.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/supplier?action=list");
        }
    }

    /**
     * Create new supplier
     */
    private void createSupplier(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        String supplierName = request.getParameter("supplierName");
        String contactName = request.getParameter("contactName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String isActiveParam = request.getParameter("isActive");
        
        // Handle null values
        if (supplierName == null) supplierName = "";
        if (contactName == null) contactName = "";
        if (email == null) email = "";
        if (phone == null) phone = "";
        if (address == null) address = "";
        
        boolean isActive = "true".equals(isActiveParam);
        
        Supplier supplier = new Supplier();
        supplier.setSupplierName(supplierName);
        supplier.setContactName(contactName);
        supplier.setEmail(email);
        supplier.setPhone(phone);
        supplier.setAddress(address);
        supplier.setActive(isActive);
        
        Map<String, String> errors = supplierService.createSupplier(supplier);
        if (errors.isEmpty()) {
            // Success
            request.getSession().setAttribute("successMessage", "Thêm nhà cung cấp thành công");
            response.sendRedirect(request.getContextPath() + "/admin/supplier?action=list");
        } else {
            // Error - show validation errors
            request.setAttribute("validationErrors", errors);
            request.setAttribute("supplier", supplier);
            request.setAttribute("action", "new");
            request.getRequestDispatcher("/WEB-INF/views/admin/supplier-form.jsp").forward(request, response);
        }
    }

    /**
     * Update existing supplier
     */
    private void updateSupplier(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        String idParam = request.getParameter("supplierId");
        String supplierName = request.getParameter("supplierName");
        String contactName = request.getParameter("contactName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String isActiveParam = request.getParameter("isActive");
        
        // Handle null values
        if (supplierName == null) supplierName = "";
        if (contactName == null) contactName = "";
        if (email == null) email = "";
        if (phone == null) phone = "";
        if (address == null) address = "";
        
        // Validation
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/supplier?action=list");
            return;
        }
        
        try {
            int supplierId = Integer.parseInt(idParam);
            boolean isActive = "true".equals(isActiveParam);
            
            Supplier supplier = new Supplier();
            supplier.setSupplierID(supplierId);
            supplier.setSupplierName(supplierName);
            supplier.setContactName(contactName);
            supplier.setEmail(email);
            supplier.setPhone(phone);
            supplier.setAddress(address);
            supplier.setActive(isActive);
            
            Map<String, String> errors = supplierService.updateSupplier(supplier);
            
            if (errors.isEmpty()) {
                // Success
                request.getSession().setAttribute("successMessage", "Cập nhật nhà cung cấp thành công");
                response.sendRedirect(request.getContextPath() + "/admin/supplier?action=list");
            } else {
                // Error - show validation errors
                request.setAttribute("validationErrors", errors);
                request.setAttribute("supplier", supplier);
                request.setAttribute("action", "edit");
                request.getRequestDispatcher("/WEB-INF/views/admin/supplier-form.jsp").forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/supplier?action=list");
        }
    }

    /**
     * Toggle supplier active status
     */
    private void toggleSupplierStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/supplier?action=list");
            return;
        }
        
        try {
            int supplierId = Integer.parseInt(idParam);
            String errorMessage = supplierService.toggleSupplierStatus(supplierId);
            
            if (errorMessage == null) {
                // Success
                request.getSession().setAttribute("successMessage", "Đã cập nhật trạng thái nhà cung cấp thành công");
            } else {
                // Error
                request.getSession().setAttribute("errorMessage", errorMessage);
            }
            
            // Redirect back to the list with current filters
            String redirectUrl = request.getContextPath() + "/admin/supplier?action=list";
            String page = request.getParameter("page");
            String status = request.getParameter("status");
            String search = request.getParameter("search");
            
            StringBuilder params = new StringBuilder();
            if (page != null && !page.trim().isEmpty()) {
                params.append("page=").append(page);
            }
            if (status != null && !status.trim().isEmpty()) {
                if (params.length() > 0) params.append("&");
                params.append("status=").append(status);
            }
            if (search != null && !search.trim().isEmpty()) {
                if (params.length() > 0) params.append("&");
                params.append("search=").append(search);
            }
            
            if (params.length() > 0) {
                redirectUrl += "&" + params.toString();
            }
            
            response.sendRedirect(redirectUrl);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/supplier?action=list");
        }
    }

}

package controller;

import model.User;
import model.Issue;
import model.Ingredient;
import model.Setting;
import service.IssueService;
import service.IssueService.IssueResult;
import service.IngredientService;
import service.SettingService;
import service.UserService;
import java.io.IOException;
import java.util.List;
import java.math.BigDecimal;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Controller for Issue management
 */
@WebServlet(name = "IssueController", urlPatterns = {"/issue"})
public class IssueController extends HttpServlet {
    
    private IssueService issueService;
    private IngredientService ingredientService;
    private SettingService settingService;
    private UserService userService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        issueService = new IssueService();
        ingredientService = new IngredientService();
        settingService = new SettingService();
        userService = new UserService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check authentication and authorization
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User currentUser = (User) session.getAttribute("user");
        String currentUserRole = (String) session.getAttribute("roleName");
        
        // Check if user has permission to access issue management
        if (!"Inventory".equals(currentUserRole) && !"Admin".equals(currentUserRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập chức năng này");
            return;
        }
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        
        try {
            switch (action) {
                case "list":
                    showIssueList(request, response);
                    break;
                case "view":
                    showIssueDetails(request, response);
                    break;
                // Inventory Staff is NOT allowed to create or edit issues
                // Only Admin can create/edit issues
                case "create":
                case "edit":
                    if ("Admin".equals(currentUserRole)) {
                        if ("create".equals(action)) {
                            showCreateForm(request, response);
                        } else {
                            showEditForm(request, response);
                        }
                    } else {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, 
                            "Bạn không có quyền tạo/chỉnh sửa issue. Chỉ Admin mới có quyền này.");
                    }
                    break;
                default:
                    showIssueList(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            showIssueList(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check authentication and authorization
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User currentUser = (User) session.getAttribute("user");
        String currentUserRole = (String) session.getAttribute("roleName");
        
        if (!"Inventory".equals(currentUserRole) && !"Admin".equals(currentUserRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập chức năng này");
            return;
        }
        
        String action = request.getParameter("action");
        System.out.println("DEBUG doPost: action = " + action);
        
        try {
            switch (action) {
                // Only Admin can create/update issues
                case "create":
                    if ("Admin".equals(currentUserRole)) {
                        System.out.println("DEBUG: Calling handleCreateIssue");
                        handleCreateIssue(request, response, currentUser);
                    } else {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Chỉ Admin mới có quyền tạo issue");
                    }
                    break;
                case "update":
                    if ("Admin".equals(currentUserRole)) {
                        System.out.println("DEBUG: Calling handleUpdateIssue");
                        handleUpdateIssue(request, response, currentUser);
                    } else {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Chỉ Admin mới có quyền chỉnh sửa issue");
                    }
                    break;
                // Inventory Staff can approve/reject issues
                case "approve":
                    System.out.println("DEBUG: Calling handleApproveIssue");
                    handleApproveIssue(request, response);
                    break;
                case "reject":
                    System.out.println("DEBUG: Calling handleRejectIssue");
                    handleRejectIssue(request, response);
                    break;
                default:
                    System.out.println("DEBUG: Showing issue list (default)");
                    showIssueList(request, response);
                    break;
            }
        } catch (Exception e) {
            System.out.println("DEBUG: Exception in doPost: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + 
                "/issue?action=list&error=3");
        }
    }
    
    /**
     * Show issue list with pagination and filtering
     */
    private void showIssueList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get pagination parameters
        int page = 1;
        int pageSize = 10;
        
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        
        String pageSizeParam = request.getParameter("pageSize");
        if (pageSizeParam != null && !pageSizeParam.isEmpty()) {
            try {
                pageSize = Integer.parseInt(pageSizeParam);
            } catch (NumberFormatException e) {
                pageSize = 10;
            }
        }
        
        // Get filter parameters
        Integer statusFilter = null;
        String statusParam = request.getParameter("statusFilter");
        if (statusParam != null && !statusParam.isEmpty() && !statusParam.equals("all")) {
            try {
                statusFilter = Integer.parseInt(statusParam);
            } catch (NumberFormatException e) {
                // Ignore invalid filter
            }
        }
        
        Integer ingredientFilter = null;
        String ingredientParam = request.getParameter("ingredientFilter");
        if (ingredientParam != null && !ingredientParam.isEmpty() && !ingredientParam.equals("all")) {
            try {
                ingredientFilter = Integer.parseInt(ingredientParam);
            } catch (NumberFormatException e) {
                // Ignore invalid filter
            }
        }
        
        // Get sort parameters
        String sortBy = request.getParameter("sortBy");
        if (sortBy != null && sortBy.trim().isEmpty()) {
            sortBy = null;
        }
        
        String sortOrder = request.getParameter("sortOrder");
        if (sortOrder != null && sortOrder.trim().isEmpty()) {
            sortOrder = null;
        }
        
        // Get issues with sorting
        IssueResult result = issueService.getAllIssues(page, pageSize, statusFilter, ingredientFilter, null, sortBy, sortOrder);
        
        // Get filter data
        List<Setting> statusList = settingService.getSettingsByType("IssueStatus");
        List<Ingredient> ingredientList = ingredientService.getAllIngredients(1, 1000, null, null, true);
        
        // Set attributes
        request.setAttribute("issues", result.getIssues());
        request.setAttribute("currentPage", result.getCurrentPage());
        request.setAttribute("totalPages", result.getTotalPages());
        request.setAttribute("totalCount", result.getTotalCount());
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("statusList", statusList);
        request.setAttribute("ingredientList", ingredientList);
        request.setAttribute("selectedStatus", statusFilter);
        request.setAttribute("selectedIngredient", ingredientFilter);
        
        // Forward to JSP
        request.getRequestDispatcher("/WEB-INF/views/inventory/issue-list.jsp").forward(request, response);
    }
    
    /**
     * Show create issue form
     */
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get ingredients and status settings
        List<Ingredient> ingredients = ingredientService.getAllIngredients(1, 1000, null, null, true);
        List<Setting> statusList = settingService.getSettingsByType("IssueStatus");
        
        // Set default status to "Reported" (first status in list)
        Integer defaultStatusId = null;
        if (!statusList.isEmpty()) {
            // Find "Reported" status, or use first status
            for (Setting status : statusList) {
                if ("Reported".equals(status.getValue())) {
                    defaultStatusId = status.getSettingID();
                    break;
                }
            }
            if (defaultStatusId == null) {
                defaultStatusId = statusList.get(0).getSettingID();
            }
        }
        
        request.setAttribute("ingredients", ingredients);
        request.setAttribute("statusList", statusList);
        request.setAttribute("defaultStatusId", defaultStatusId);
        
        request.getRequestDispatcher("/WEB-INF/views/inventory/issue-form.jsp").forward(request, response);
    }
    
    /**
     * Show edit issue form
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String issueIdParam = request.getParameter("id");
        if (issueIdParam == null || issueIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/issue?action=list&error=Invalid issue ID");
            return;
        }
        
        try {
            int issueId = Integer.parseInt(issueIdParam);
            Issue issue = issueService.getIssueById(issueId);
            
            if (issue == null) {
                response.sendRedirect(request.getContextPath() + "/issue?action=list&error=Issue not found");
                return;
            }
            
            // Get ingredients and users for edit form
            List<Ingredient> ingredients = ingredientService.getAllIngredients(1, 1000, null, null, true);
            List<User> userList = userService.getUser(1, 1000, null, null,null); // Get all users for confirmedBy dropdown
            
            request.setAttribute("issue", issue);
            request.setAttribute("ingredients", ingredients);
            request.setAttribute("userList", userList);
            request.setAttribute("isEdit", true);
            
            request.getRequestDispatcher("/WEB-INF/views/inventory/issue-form.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/issue?action=list&error=Invalid issue ID format");
        }
    }
    
    /**
     * Show issue details
     */
    private void showIssueDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String issueIdParam = request.getParameter("id");
        if (issueIdParam == null || issueIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/issue?action=list&error=Invalid issue ID");
            return;
        }
        
        try {
            int issueId = Integer.parseInt(issueIdParam);
            Issue issue = issueService.getIssueById(issueId);
            
            if (issue == null) {
                response.sendRedirect(request.getContextPath() + "/issue?action=list&error=Issue not found");
                return;
            }
            
            request.setAttribute("issue", issue);
            request.getRequestDispatcher("/WEB-INF/views/inventory/issue-details.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/issue?action=list&error=Invalid issue ID format");
        }
    }
    
    /**
     * Handle create issue
     */
    private void handleCreateIssue(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws ServletException, IOException {
        
        try {
            // Get form parameters
            int ingredientId = Integer.parseInt(request.getParameter("ingredientId"));
            String description = request.getParameter("description");
            BigDecimal quantity = new BigDecimal(request.getParameter("quantity"));
            int statusId = Integer.parseInt(request.getParameter("statusId"));
            
            // Validate
            if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
                request.setAttribute("error", "Số lượng phải lớn hơn 0");
                showCreateForm(request, response);
                return;
            }
            
            // Create issue object
            Issue issue = new Issue();
            issue.setIngredientID(ingredientId);
            issue.setDescription(description);
            issue.setQuantity(quantity);
            issue.setStatusID(statusId);
            issue.setCreatedBy(currentUser.getUserID());
            
            // Save to database
            int issueId = issueService.createIssue(issue);
            
            System.out.println("DEBUG: Created issue ID = " + issueId);
            
            if (issueId > 0) {
                System.out.println("DEBUG: Redirecting to list page...");
                response.sendRedirect(request.getContextPath() + 
                    "/issue?action=list&success=1");
            } else {
                System.out.println("DEBUG: Failed to create issue, showing form again");
                response.sendRedirect(request.getContextPath() + 
                    "/issue?action=list&error=1");
            }
            
        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("error", "Dữ liệu không hợp lệ: " + e.getMessage());
            showCreateForm(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tạo vấn đề: " + e.getMessage());
            showCreateForm(request, response);
        }
    }
    
    /**
     * Handle update issue
     */
    private void handleUpdateIssue(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws ServletException, IOException {
        
        try {
            // Get form parameters
            int issueId = Integer.parseInt(request.getParameter("issueId"));
            int ingredientId = Integer.parseInt(request.getParameter("ingredientId"));
            String description = request.getParameter("description");
            BigDecimal quantity = new BigDecimal(request.getParameter("quantity"));
            // Validate
            if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
                request.setAttribute("error", "Số lượng phải lớn hơn 0");
                showEditForm(request, response);
                return;
            }
            
            // Get existing issue
            Issue issue = issueService.getIssueById(issueId);
            if (issue == null) {
                response.sendRedirect(request.getContextPath() + "/issue?action=list&error=Issue not found");
                return;
            }
            
            // Update issue object (chỉ update ingredientID, description, quantity)
            // Không update statusID và confirmedBy - giữ nguyên giá trị cũ
            issue.setIngredientID(ingredientId);
            issue.setDescription(description);
            issue.setQuantity(quantity);
            // Keep original status and confirmedBy - không thay đổi
            // issue.setStatusID(statusId); // REMOVED - status không được thay đổi
            // issue.setConfirmedBy(confirmedBy); // REMOVED - confirmedBy không được thay đổi
            
            // Update in database
            boolean success = issueService.updateIssue(issue);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + 
                    "/issue?action=view&id=" + issueId + "&success=2");
            } else {
                response.sendRedirect(request.getContextPath() + 
                    "/issue?action=list&error=2");
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dữ liệu không hợp lệ: " + e.getMessage());
            showEditForm(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi cập nhật vấn đề: " + e.getMessage());
            showEditForm(request, response);
        }
    }
    
    
    /**
     * Handle approve issue request from Inventory Staff
     */
    private void handleApproveIssue(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String issueIdParam = request.getParameter("id");
        if (issueIdParam == null || issueIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/issue?action=list&error=Invalid issue ID");
            return;
        }
        
        try {
            int issueID = Integer.parseInt(issueIdParam);
            
            // Approve the issue
            String error = issueService.approveIssue(issueID);
            
            if (error == null) {
                response.sendRedirect(request.getContextPath() + 
                    "/issue?action=view&id=" + issueID + "&success=Issue approved successfully");
            } else {
                response.sendRedirect(request.getContextPath() + 
                    "/issue?action=view&id=" + issueID + "&error=" + error);
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/issue?action=list&error=Invalid issue ID format");
        }
    }
    
    /**
     * Handle reject issue request from Inventory Staff
     */
    private void handleRejectIssue(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String issueIdParam = request.getParameter("id");
        String rejectionReason = request.getParameter("rejectionReason");
        
        if (issueIdParam == null || issueIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/issue?action=list&error=Invalid issue ID");
            return;
        }
        
        if (rejectionReason == null || rejectionReason.trim().isEmpty()) {
            try {
                int issueID = Integer.parseInt(issueIdParam);
                response.sendRedirect(request.getContextPath() + 
                    "/issue?action=view&id=" + issueID + "&error=Rejection reason is required");
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/issue?action=list&error=Invalid issue ID");
            }
            return;
        }
        
        try {
            int issueID = Integer.parseInt(issueIdParam);
            
            // Reject the issue
            String error = issueService.rejectIssue(issueID, rejectionReason.trim());
            
            if (error == null) {
                response.sendRedirect(request.getContextPath() + 
                    "/issue?action=view&id=" + issueID + "&success=Issue rejected successfully");
            } else {
                response.sendRedirect(request.getContextPath() + 
                    "/issue?action=view&id=" + issueID + "&error=" + error);
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/issue?action=list&error=Invalid issue ID format");
        }
    }
}

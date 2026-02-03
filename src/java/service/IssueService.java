package service;

import dao.IssueDAO;
import dao.SettingDAO;
import dao.PurchaseOrderDAO;
import model.Issue;
import model.IssueStatistics;
import model.Setting;
import model.PurchaseOrder;
import model.PurchaseOrderDetail;
import java.util.List;

/**
 * Service layer for Issue management
 */
public class IssueService {
    
    private IssueDAO issueDAO;
    private SettingDAO settingDAO;
    private PurchaseOrderDAO purchaseOrderDAO;
    
    public IssueService() {
        this.issueDAO = new IssueDAO();
        this.settingDAO = new SettingDAO();
        this.purchaseOrderDAO = new PurchaseOrderDAO();
    }
    
    /**
     * Result class to hold issues with pagination info
     */
    public static class IssueResult {
        private List<Issue> issues;
        private int totalPages;
        private int currentPage;
        private int totalCount;
        
        public IssueResult(List<Issue> issues, int totalPages, int currentPage, int totalCount) {
            this.issues = issues;
            this.totalPages = totalPages;
            this.currentPage = currentPage;
            this.totalCount = totalCount;
        }
        
        public List<Issue> getIssues() {
            return issues;
        }
        
        public int getTotalPages() {
            return totalPages;
        }
        
        public int getCurrentPage() {
            return currentPage;
        }
        
        public int getTotalCount() {
            return totalCount;
        }
    }
    
    /**
     * Get all issues with pagination and filtering
     */
    public IssueResult getAllIssues(int page, int pageSize, Integer statusFilter, 
                                    Integer ingredientFilter, Integer createdByFilter) {
        List<Issue> issues = issueDAO.getAllIssues(page, pageSize, statusFilter, ingredientFilter, createdByFilter);
        int totalCount = issueDAO.getTotalIssueCount(statusFilter, ingredientFilter, createdByFilter);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        
        return new IssueResult(issues, totalPages, page, totalCount);
    }
    
    /**
     * Get all issues with pagination, filtering and sorting
     */
    public IssueResult getAllIssues(int page, int pageSize, Integer statusFilter, 
                                    Integer ingredientFilter, Integer createdByFilter,
                                    String sortBy, String sortOrder) {
        List<Issue> issues = issueDAO.getAllIssues(page, pageSize, statusFilter, ingredientFilter, createdByFilter, sortBy, sortOrder);
        int totalCount = issueDAO.getTotalIssueCount(statusFilter, ingredientFilter, createdByFilter);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        
        return new IssueResult(issues, totalPages, page, totalCount);
    }
    
    /**
     * Get issue by ID
     */
    public Issue getIssueById(int issueID) {
        return issueDAO.getIssueById(issueID);
    }

    /**
     * Get all issues that belong to the same request (same description + creator)
     */
    public List<Issue> getIssuesByDescriptionAndCreator(String description, int createdBy) {
        return issueDAO.getIssuesByDescriptionAndCreator(description, createdBy);
    }

    /**
     * Create new issue
     */
    public int createIssue(Issue issue) {
        return issueDAO.createIssue(issue);
    }
    
    /**
     * Update issue
     */
    public boolean updateIssue(Issue issue) {
        return issueDAO.updateIssue(issue);
    }
    
    /**
     * Delete issue
     */
    public boolean deleteIssue(int issueID) {
        return issueDAO.deleteIssue(issueID);
    }
    
    /**
     * Get issue statistics
     */
    public int[] getIssueStatsByStatus() {
        return issueDAO.getIssueStatsByStatus();
    }
    
    /**
     * Update issue status
     * @param issueID Issue ID
     * @param statusID New status ID
     * @return Error message if failed, null if successful
     */
    public String updateIssueStatus(int issueID, int statusID) {
        // Check if issue exists
        Issue existingIssue = issueDAO.getIssueById(issueID);
        if (existingIssue == null) {
            return "Không tìm thấy sự cố";
        }
        
        // Update status
        if (issueDAO.updateIssueStatus(issueID, statusID)) {
            return null; // Success
        } else {
            return "Lỗi hệ thống khi cập nhật trạng thái";
        }
    }
    
    /**
     * Resolve issue (set status to Resolved)
     * @param issueID Issue ID
     * @return Error message if failed, null if successful
     */
    public String resolveIssue(int issueID) {
        // Check if issue exists
        Issue existingIssue = issueDAO.getIssueById(issueID);
        if (existingIssue == null) {
            return "Không tìm thấy sự cố";
        }
        
        // Resolve the issue
        if (issueDAO.resolveIssue(issueID)) {
            return null; // Success
        } else {
            return "Lỗi hệ thống khi giải quyết sự cố";
        }
    }
    
    /**
     * Reject issue with reason
     * @param issueID Issue ID
     * @param rejectionReason Reason for rejection
     * @return Error message if failed, null if successful
     */
    public String rejectIssue(int issueID, String rejectionReason) {
        // Validate rejection reason
        if (rejectionReason == null || rejectionReason.trim().isEmpty()) {
            return "Vui lòng nhập lý do từ chối";
        }
        
        // Check if issue exists
        Issue existingIssue = issueDAO.getIssueById(issueID);
        if (existingIssue == null) {
            return "Không tìm thấy sự cố";
        }
        
        // Reject the issue
        if (issueDAO.rejectIssue(issueID, rejectionReason.trim())) {
            return null; // Success
        } else {
            return "Lỗi hệ thống khi từ chối sự cố";
        }
    }
    
    /**
     * Approve issue (set status to In Progress - StatusID = 26)
     * Used by Inventory Staff to approve Barista's issue request
     * If issueType is RESTOCK_REQUEST, automatically create a PO
     * @param issueID Issue ID
     * @return Error message if failed, null if successful
     */
    public String approveIssue(int issueID) {
        // Check if issue exists
        Issue existingIssue = issueDAO.getIssueById(issueID);
        if (existingIssue == null) {
            return "Không tìm thấy sự cố";
        }
        
        // Check if issue is in Pending status (StatusID = 25)
        if (existingIssue.getStatusID() != 25) {
            return "Chỉ có thể phê duyệt sự cố đang ở trạng thái Chờ xử lý";
        }
        
        // Approve the issue - change status to "In Progress" (StatusID = 26)
        if (!issueDAO.updateIssueStatus(issueID, 26)) {
            return "Lỗi hệ thống khi phê duyệt sự cố";
        }
        
        // If issueType is RESTOCK_REQUEST, automatically create a Purchase Order
        if ("RESTOCK_REQUEST".equals(existingIssue.getIssueType())) {
            try {
                // Get the user's shop ID (assuming the user who created issue is Barista)
                // For now, we'll use a default shop. You may need to fetch from User table
                int shopID = 1; // Default shop - you should get this from the user's shop assignment
                
                // Get the supplier for the ingredient (you may need to add logic to determine supplier)
                int supplierID = 1; // Default supplier - should be determined by ingredient or shop preference
                
                // Create Purchase Order with status = Pending (PO StatusID = 20)
                PurchaseOrder po = new PurchaseOrder();
                po.setShopID(shopID);
                po.setSupplierID(supplierID);
                po.setCreatedBy(existingIssue.getCreatedBy());
                po.setStatusID(20); // Pending status
                
                int poID = purchaseOrderDAO.insertPurchaseOrder(po);
                
                if (poID > 0) {
                    // Create PO Detail with the ingredient and quantity from issue
                    PurchaseOrderDetail detail = new PurchaseOrderDetail();
                    detail.setPoID(poID);
                    detail.setIngredientID(existingIssue.getIngredientID());
                    detail.setQuantity(existingIssue.getQuantity());
                    detail.setReceivedQuantity(java.math.BigDecimal.ZERO);
                    
                    if (!purchaseOrderDAO.insertPurchaseOrderDetail(detail)) {
                        return "Lỗi khi tạo chi tiết đơn hàng tự động";
                    }
                    
                    System.out.println("Auto-created Purchase Order ID: " + poID + " for Restock Request Issue ID: " + issueID);
                } else {
                    return "Lỗi khi tạo đơn hàng tự động";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Lỗi khi tạo đơn hàng tự động: " + e.getMessage();
            }
        }
        
        return null; // Success
    }
    
    /**
     * Get issue statistics (counts by status)
     * @return IssueStatistics object
     */
    public IssueStatistics getIssueStatistics() {
        IssueStatistics stats = new IssueStatistics();
        
        // Get all status settings for IssueStatus
        List<Setting> statuses = settingDAO.getSettingsByType("IssueStatus");
        
        // Get all issues to count
        IssueResult result = getAllIssues(1, Integer.MAX_VALUE, null, null, null);
        List<Issue> allIssues = result.getIssues();
        
        // Set total
        stats.setTotalIssues(allIssues.size());
        
        // Count by each status
        int reportedCount = 0;
        int underInvestigationCount = 0;
        int resolvedCount = 0;
        int rejectedCount = 0;
        
        for (Issue issue : allIssues) {
            String statusName = issue.getStatusName();
            if (statusName != null) {
                switch (statusName) {
                    case "Reported":
                        reportedCount++;
                        break;
                    case "Under Investigation":
                        underInvestigationCount++;
                        break;
                    case "Resolved":
                        resolvedCount++;
                        break;
                    case "Rejected":
                        rejectedCount++;
                        break;
                }
            }
        }
        
        stats.setReportedCount(reportedCount);
        stats.setUnderInvestigationCount(underInvestigationCount);
        stats.setResolvedCount(resolvedCount);
        stats.setRejectedCount(rejectedCount);
        
        return stats;
    }
}

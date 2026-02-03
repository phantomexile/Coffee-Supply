package model;

/**
 * Model class to hold issue statistics
 */
public class IssueStatistics {
    private int totalIssues;
    private int reportedCount;           // Reported status
    private int underInvestigationCount; // Under Investigation status
    private int resolvedCount;           // Resolved status
    private int rejectedCount;           // Rejected status
    
    public IssueStatistics() {
    }
    
    public int getTotalIssues() {
        return totalIssues;
    }
    
    public void setTotalIssues(int totalIssues) {
        this.totalIssues = totalIssues;
    }
    
    public int getReportedCount() {
        return reportedCount;
    }
    
    public void setReportedCount(int reportedCount) {
        this.reportedCount = reportedCount;
    }
    
    public int getUnderInvestigationCount() {
        return underInvestigationCount;
    }
    
    public void setUnderInvestigationCount(int underInvestigationCount) {
        this.underInvestigationCount = underInvestigationCount;
    }
    
    public int getResolvedCount() {
        return resolvedCount;
    }
    
    public void setResolvedCount(int resolvedCount) {
        this.resolvedCount = resolvedCount;
    }
    
    public int getRejectedCount() {
        return rejectedCount;
    }
    
    public void setRejectedCount(int rejectedCount) {
        this.rejectedCount = rejectedCount;
    }
}

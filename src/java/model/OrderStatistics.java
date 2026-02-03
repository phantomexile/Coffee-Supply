package model;

/**
 * Model class to hold order statistics
 * @author DrDYNew
 */
public class OrderStatistics {
    private int totalOrders;
    private int pendingOrders;      // New status
    private int processingOrders;   // Preparing status
    private int readyOrders;        // Ready status
    private int completedOrders;    // Completed status
    private int cancelledOrders;    // Cancelled status
    
    public OrderStatistics() {
    }
    
    public int getTotalOrders() {
        return totalOrders;
    }
    
    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }
    
    public int getPendingOrders() {
        return pendingOrders;
    }
    
    public void setPendingOrders(int pendingOrders) {
        this.pendingOrders = pendingOrders;
    }
    
    public int getProcessingOrders() {
        return processingOrders;
    }
    
    public void setProcessingOrders(int processingOrders) {
        this.processingOrders = processingOrders;
    }
    
    public int getReadyOrders() {
        return readyOrders;
    }
    
    public void setReadyOrders(int readyOrders) {
        this.readyOrders = readyOrders;
    }
    
    public int getCompletedOrders() {
        return completedOrders;
    }
    
    public void setCompletedOrders(int completedOrders) {
        this.completedOrders = completedOrders;
    }
    
    public int getCancelledOrders() {
        return cancelledOrders;
    }
    
    public void setCancelledOrders(int cancelledOrders) {
        this.cancelledOrders = cancelledOrders;
    }
}

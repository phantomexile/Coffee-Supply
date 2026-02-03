package service;

import dao.OrderDAO;
import dao.SettingDAO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import model.Order;
import model.OrderDetail;
import model.OrderStatistics;
import model.Setting;

/**
 * Service layer for Order business logic
 */
public class OrderService {
    
    private OrderDAO orderDAO;
    private SettingDAO settingDAO;
    
    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.settingDAO = new SettingDAO();
    }
    
    /**
     * Get all orders
     * @return List of orders
     */
    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }
    
    /**
     * Get orders by status
     * @param statusID Status ID to filter
     * @return List of orders with the given status
     */
    public List<Order> getOrdersByStatus(int statusID) {
        return orderDAO.getOrdersByStatus(statusID);
    }
    
    /**
     * Get orders with pagination
     * @param page Page number
     * @param pageSize Page size
     * @return List of orders
     */
    public List<Order> getOrders(int page, int pageSize) {
        return orderDAO.getAllOrders(page, pageSize);
    }
    
    /**
     * Get orders with pagination and status filter
     * @param page Page number (starting from 1)
     * @param pageSize Page size
     * @param statusFilter Status filter (null for all)
     * @return List of orders
     */
    public List<Order> getOrdersWithPagination(int page, int pageSize, Integer statusFilter) {
        return orderDAO.getOrdersWithPagination(page, pageSize, statusFilter);
    }
    
    /**
     * Get orders with detailed information
     * @param page Page number
     * @param pageSize Page size
     * @param statusFilter Status filter (null for all)
     * @param shopFilter Shop filter (null for all)
     * @return List of orders with details
     */
    public List<Map<String, Object>> getOrdersWithDetails(int page, int pageSize,
                                                           Integer statusFilter, Integer shopFilter) {
        return orderDAO.getOrdersWithDetails(page, pageSize, statusFilter, shopFilter);
    }
    
    /**
     * Get total order count
     * @param statusFilter Status filter
     * @param shopFilter Shop filter
     * @return Total count
     */
    public int getTotalOrderCount(Integer statusFilter, Integer shopFilter) {
        return orderDAO.getTotalOrderCount(statusFilter, shopFilter);
    }
    
    /**
     * Get order by ID
     * @param orderID Order ID
     * @return Order object
     */
    public Order getOrderById(int orderID) {
        return orderDAO.getOrderById(orderID);
    }
    
    /**
     * Get order with details by ID
     * @param orderID Order ID
     * @return Map with order details
     */
    public Map<String, Object> getOrderWithDetailsById(int orderID) {
        return orderDAO.getOrderWithDetailsById(orderID);
    }
    
    /**
     * Get order details with product info
     * @param orderID Order ID
     * @return List of order details
     */
    public List<Map<String, Object>> getOrderDetailsWithProduct(int orderID) {
        return orderDAO.getOrderDetailsWithProduct(orderID);
    }
    
    /**
     * Get order details
     * @param orderID Order ID
     * @return List of order details
     */
    public List<OrderDetail> getOrderDetails(int orderID) {
        return orderDAO.getOrderDetails(orderID);
    }
    
    /**
     * Calculate order total
     * @param orderID Order ID
     * @return Total amount
     */
    public BigDecimal calculateOrderTotal(int orderID) {
        return orderDAO.calculateOrderTotal(orderID);
    }
    
    /**
     * Create new order
     * @param order Order object
     * @return Error message if failed, null if successful
     */
    public String createOrder(Order order) {
        // Validate order data
        if (order.getShopID() <= 0) {
            return "Shop không hợp lệ";
        }
        if (order.getCreatedBy() <= 0) {
            return "Người tạo không hợp lệ";
        }
        if (order.getStatusID() <= 0) {
            return "Trạng thái không hợp lệ";
        }
        
        // Insert order
        int orderID = orderDAO.insertOrder(order);
        if (orderID > 0) {
            order.setOrderID(orderID);
            return null; // Success
        } else {
            return "Lỗi hệ thống khi tạo đơn hàng";
        }
    }
    
    /**
     * Update order
     * @param order Order object
     * @return Error message if failed, null if successful
     */
    public String updateOrder(Order order) {
        // Check if order exists
        Order existingOrder = orderDAO.getOrderById(order.getOrderID());
        if (existingOrder == null) {
            return "Không tìm thấy đơn hàng";
        }
        
        // Validate order data
        if (order.getShopID() <= 0) {
            return "Shop không hợp lệ";
        }
        if (order.getStatusID() <= 0) {
            return "Trạng thái không hợp lệ";
        }
        
        // Update order
        if (orderDAO.updateOrder(order)) {
            return null; // Success
        } else {
            return "Lỗi hệ thống khi cập nhật đơn hàng";
        }
    }
    
    /**
     * Update order status
     * @param orderID Order ID
     * @param statusID New status ID
     * @return Error message if failed, null if successful
     */
    public String updateOrderStatus(int orderID, int statusID) {
        // Check if order exists
        Order existingOrder = orderDAO.getOrderById(orderID);
        if (existingOrder == null) {
            return "Không tìm thấy đơn hàng";
        }
        
        // Validate status transition
        String validationError = validateStatusTransition(existingOrder.getStatusID(), statusID);
        if (validationError != null) {
            return validationError;
        }
        
        // Update status
        if (orderDAO.updateOrderStatus(orderID, statusID)) {
            return null; // Success
        } else {
            return "Lỗi hệ thống khi cập nhật trạng thái đơn hàng";
        }
    }
    
    /**
     * Cancel order (update status to Cancelled)
     * @param orderID Order ID
     * @return Error message if failed, null if successful
     */
    public String cancelOrder(int orderID, String cancellationReason) {
        // Validate cancellation reason
        if (cancellationReason == null || cancellationReason.trim().isEmpty()) {
            return "Vui lòng nhập lý do hủy đơn";
        }
        
        // Get Cancelled status ID
        List<Setting> statuses = settingDAO.getSettingsByType("OrderStatus");
        Integer cancelledStatusID = null;
        for (Setting status : statuses) {
            if ("Cancelled".equals(status.getValue())) {
                cancelledStatusID = status.getSettingID();
                break;
            }
        }
        
        if (cancelledStatusID == null) {
            return "Không tìm thấy trạng thái Cancelled";
        }
        
        // Check if order exists and can be cancelled
        Order existingOrder = orderDAO.getOrderById(orderID);
        if (existingOrder == null) {
            return "Không tìm thấy đơn hàng";
        }
        
        // Get Completed status ID
        Integer completedStatusID = null;
        for (Setting status : statuses) {
            if ("Completed".equals(status.getValue())) {
                completedStatusID = status.getSettingID();
                break;
            }
        }
        
        // Don't allow cancelling completed or already cancelled orders
        if (completedStatusID != null && existingOrder.getStatusID() == completedStatusID) {
            return "Không thể hủy đơn hàng đã hoàn thành";
        }
        if (existingOrder.getStatusID() == cancelledStatusID) {
            return "Đơn hàng đã bị hủy trước đó";
        }
        
        // Update status to Cancelled with reason
        if (orderDAO.cancelOrder(orderID, cancelledStatusID, cancellationReason.trim())) {
            return null; // Success
        } else {
            return "Lỗi hệ thống khi hủy đơn hàng";
        }
    }
    
    /**
     * Delete order
     * @param orderID Order ID
     * @return Error message if failed, null if successful
     */
    public String deleteOrder(int orderID) {
        // Check if order exists
        Order existingOrder = orderDAO.getOrderById(orderID);
        if (existingOrder == null) {
            return "Không tìm thấy đơn hàng";
        }
        
        // Delete order
        if (orderDAO.deleteOrder(orderID)) {
            return null; // Success
        } else {
            return "Lỗi hệ thống khi xóa đơn hàng";
        }
    }
    
    /**
     * Add order detail
     * @param detail OrderDetail object
     * @return Error message if failed, null if successful
     */
    public String addOrderDetail(OrderDetail detail) {
        // Validate detail data
        if (detail.getOrderID() <= 0) {
            return "Đơn hàng không hợp lệ";
        }
        if (detail.getProductID() <= 0) {
            return "Sản phẩm không hợp lệ";
        }
        if (detail.getQuantity() <= 0) {
            return "Số lượng phải lớn hơn 0";
        }
        if (detail.getPrice() == null || detail.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            return "Giá không hợp lệ";
        }
        
        // Insert detail
        if (orderDAO.insertOrderDetail(detail)) {
            return null; // Success
        } else {
            return "Lỗi hệ thống khi thêm chi tiết đơn hàng";
        }
    }
    
    /**
     * Get all order statuses
     * @return List of order statuses
     */
    public List<Setting> getOrderStatuses() {
        return settingDAO.getSettingsByType("OrderStatus");
    }
    
    /**
     * Validate status transition
     * @param currentStatusID Current status ID
     * @param newStatusID New status ID
     * @return Error message if invalid, null if valid
     */
    private String validateStatusTransition(int currentStatusID, int newStatusID) {
        // Get status names
        Setting currentStatus = settingDAO.getSettingById(currentStatusID);
        Setting newStatus = settingDAO.getSettingById(newStatusID);
        
        if (currentStatus == null || newStatus == null) {
            return "Trạng thái không hợp lệ";
        }
        
        String currentStatusName = currentStatus.getValue();
        String newStatusName = newStatus.getValue();
        
        // Define valid transitions
        // New -> Preparing, Cancelled
        // Preparing -> Ready, Cancelled
        // Ready -> Completed, Cancelled
        // Completed -> (no transition allowed)
        // Cancelled -> (no transition allowed)
        
        if ("Completed".equals(currentStatusName) || "Cancelled".equals(currentStatusName)) {
            return "Không thể thay đổi trạng thái của đơn hàng đã hoàn thành hoặc đã hủy";
        }
        
        if ("New".equals(currentStatusName)) {
            if (!"Preparing".equals(newStatusName) && !"Cancelled".equals(newStatusName)) {
                return "Đơn hàng mới chỉ có thể chuyển sang trạng thái Đang chuẩn bị hoặc Đã hủy";
            }
        } else if ("Preparing".equals(currentStatusName)) {
            if (!"Ready".equals(newStatusName) && !"Cancelled".equals(newStatusName)) {
                return "Đơn hàng đang chuẩn bị chỉ có thể chuyển sang trạng thái Sẵn sàng hoặc Đã hủy";
            }
        } else if ("Ready".equals(currentStatusName)) {
            if (!"Completed".equals(newStatusName) && !"Cancelled".equals(newStatusName)) {
                return "Đơn hàng sẵn sàng chỉ có thể chuyển sang trạng thái Đã hoàn thành hoặc Đã hủy";
            }
        }
        
        return null; // Valid transition
    }
    
    /**
     * Get order statistics (counts by status)
     * @return OrderStatistics object
     */
    public OrderStatistics getOrderStatistics() {
        OrderStatistics stats = new OrderStatistics();
        
        // Get all status settings
        List<Setting> statuses = settingDAO.getSettingsByType("OrderStatus");
        
        // Count total orders
        stats.setTotalOrders(orderDAO.countAllOrders());
        
        // Count by each status
        for (Setting status : statuses) {
            int count = orderDAO.countOrdersByStatus(status.getSettingID());
            String statusValue = status.getValue();
            
            switch (statusValue) {
                case "New":
                    stats.setPendingOrders(count);
                    break;
                case "Preparing":
                    stats.setProcessingOrders(count);
                    break;
                case "Ready":
                    stats.setReadyOrders(count);
                    break;
                case "Completed":
                    stats.setCompletedOrders(count);
                    break;
                case "Cancelled":
                    stats.setCancelledOrders(count);
                    break;
            }
        }
        
        return stats;
    }
}

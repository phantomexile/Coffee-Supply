package dao;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Order;
import model.OrderDetail;

/**
 * DAO for Order operations

 */
public class OrderDAO extends BaseDAO {

    /**
     * Get all orders with pagination
     * @param page Page number (starting from 1)
     * @param pageSize Number of records per page
     * @return List of orders
     */
    public List<Order> getAllOrders(int page, int pageSize) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM \"Order\" ORDER BY CreatedAt DESC LIMIT ? OFFSET ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, pageSize);
            ps.setInt(2, (page - 1) * pageSize);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Order order = new Order();
                order.setOrderID(rs.getInt("OrderID"));
                order.setShopID(rs.getInt("ShopID"));
                order.setCreatedBy(rs.getInt("CreatedBy"));
                order.setStatusID(rs.getInt("StatusID"));
                order.setCreatedAt(rs.getTimestamp("CreatedAt"));
                list.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Get all orders without pagination
     * @return List of all orders with shop name, status name, and created by name
     */
    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.orderid, o.shopid, o.createdby, o.statusid, o.createdat, " +
                    "s.shopname, st.value as statusname, u.fullname as createdbyname " +
                    "FROM \"Order\" o " +
                    "LEFT JOIN shop s ON o.shopid = s.shopid " +
                    "LEFT JOIN setting st ON o.statusid = st.settingid " +
                    "LEFT JOIN \"User\" u ON o.createdby = u.userid " +
                    "ORDER BY o.createdat DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Order order = new Order();
                order.setOrderID(rs.getInt("orderid"));
                order.setShopID(rs.getInt("shopid"));
                order.setCreatedBy(rs.getInt("createdby"));
                order.setStatusID(rs.getInt("statusid"));
                
                Timestamp timestamp = rs.getTimestamp("createdat");
                if (timestamp != null) {
                    order.setCreatedAt(timestamp);
                    order.setCreatedAt(timestamp.toLocalDateTime());
                }
                
                order.setShopName(rs.getString("shopname"));
                order.setStatusName(rs.getString("statusname"));
                order.setCreatedByName(rs.getString("createdbyname"));
                
                list.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * Get orders by status
     * @param statusID Status ID to filter
     * @return List of orders with the given status
     */
    public List<Order> getOrdersByStatus(int statusID) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.orderid, o.shopid, o.createdby, o.statusid, o.createdat, " +
                    "s.shopname, st.value as statusname, u.fullname as createdbyname " +
                    "FROM \"Order\" o " +
                    "LEFT JOIN shop s ON o.shopid = s.shopid " +
                    "LEFT JOIN setting st ON o.statusid = st.settingid " +
                    "LEFT JOIN \"User\" u ON o.createdby = u.userid " +
                    "WHERE o.statusid = ? " +
                    "ORDER BY o.createdat DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, statusID);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setOrderID(rs.getInt("orderid"));
                    order.setShopID(rs.getInt("shopid"));
                    order.setCreatedBy(rs.getInt("createdby"));
                    order.setStatusID(rs.getInt("statusid"));
                    
                    Timestamp timestamp = rs.getTimestamp("createdat");
                    if (timestamp != null) {
                        order.setCreatedAt(timestamp);
                        order.setCreatedAt(timestamp.toLocalDateTime());
                    }
                    
                    order.setShopName(rs.getString("shopname"));
                    order.setStatusName(rs.getString("statusname"));
                    order.setCreatedByName(rs.getString("createdbyname"));
                    
                    list.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * Get orders with pagination and optional status filter
     * @param page Page number (starting from 1)
     * @param pageSize Number of records per page
     * @param statusFilter Status filter (null for all)
     * @return List of orders with shop name, status name, and created by name
     */
    public List<Order> getOrdersWithPagination(int page, int pageSize, Integer statusFilter) {
        List<Order> list = new ArrayList<>();
        
        // Validate page and pageSize
        if (page < 1) {
            page = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT o.orderid, o.shopid, o.createdby, o.statusid, o.createdat, ");
        sql.append("s.shopname, st.value as statusname, u.fullname as createdbyname ");
        sql.append("FROM \"Order\" o ");
        sql.append("LEFT JOIN shop s ON o.shopid = s.shopid ");
        sql.append("LEFT JOIN setting st ON o.statusid = st.settingid ");
        sql.append("LEFT JOIN \"User\" u ON o.createdby = u.userid ");
        
        if (statusFilter != null) {
            sql.append("WHERE o.statusid = ? ");
        }
        
        sql.append("ORDER BY o.createdat DESC LIMIT ? OFFSET ?");
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            if (statusFilter != null) {
                ps.setInt(paramIndex++, statusFilter);
            }
            ps.setInt(paramIndex++, pageSize);
            int offset = (page - 1) * pageSize;
            ps.setInt(paramIndex++, offset);
            
            System.out.println("=== DEBUG getOrdersWithPagination ===");
            System.out.println("Page: " + page + ", PageSize: " + pageSize + ", Offset: " + offset);
            System.out.println("StatusFilter: " + statusFilter);
            System.out.println("SQL: " + sql.toString());
            
            try (ResultSet rs = ps.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    count++;
                    Order order = new Order();
                    order.setOrderID(rs.getInt("orderid"));
                    order.setShopID(rs.getInt("shopid"));
                    order.setCreatedBy(rs.getInt("createdby"));
                    order.setStatusID(rs.getInt("statusid"));
                    
                    Timestamp timestamp = rs.getTimestamp("createdat");
                    if (timestamp != null) {
                        order.setCreatedAt(timestamp);
                        order.setCreatedAt(timestamp.toLocalDateTime());
                    }
                    
                    order.setShopName(rs.getString("shopname"));
                    order.setStatusName(rs.getString("statusname"));
                    order.setCreatedByName(rs.getString("createdbyname"));
                    
                    list.add(order);
                }
                System.out.println("Rows returned: " + count);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getOrdersWithPagination: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error in getOrdersWithPagination: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * Get orders with detailed information (joined with Setting, Shop, User)
     * @param page Page number
     * @param pageSize Page size
     * @param statusFilter Status filter (null for all)
     * @param shopFilter Shop filter (null for all)
     * @return List of orders with detailed info
     */
    public List<Map<String, Object>> getOrdersWithDetails(int page, int pageSize, 
                                                           Integer statusFilter, Integer shopFilter) {
        List<Map<String, Object>> orders = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT o.OrderID, o.ShopID, o.CreatedBy, o.StatusID, o.CreatedAt, ");
        sql.append("sh.ShopName, u.FullName as CreatedByName, st.Value as StatusName ");
        sql.append("FROM \"Order\" o ");
        sql.append("LEFT JOIN Shop sh ON o.ShopID = sh.ShopID ");
        sql.append("LEFT JOIN \"User\" u ON o.CreatedBy = u.UserID ");
        sql.append("LEFT JOIN Setting st ON o.StatusID = st.SettingID ");
        sql.append("WHERE 1=1 ");
        
        List<Object> params = new ArrayList<>();
        if (statusFilter != null) {
            sql.append("AND o.StatusID = ? ");
            params.add(statusFilter);
        }
        if (shopFilter != null) {
            sql.append("AND o.ShopID = ? ");
            params.add(shopFilter);
        }
        
        sql.append("ORDER BY o.CreatedAt DESC LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> order = new HashMap<>();
                order.put("orderID", rs.getInt("OrderID"));
                order.put("shopID", rs.getInt("ShopID"));
                order.put("createdBy", rs.getInt("CreatedBy"));
                order.put("statusID", rs.getInt("StatusID"));
                order.put("createdAt", rs.getTimestamp("CreatedAt"));
                order.put("shopName", rs.getString("ShopName"));
                order.put("createdByName", rs.getString("CreatedByName"));
                order.put("statusName", rs.getString("StatusName"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    /**
     * Get total count of orders
     * @param statusFilter Status filter
     * @param shopFilter Shop filter
     * @return Total count
     */
    public int getTotalOrderCount(Integer statusFilter, Integer shopFilter) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM \"Order\" o WHERE 1=1 ");
        
        List<Object> params = new ArrayList<>();
        if (statusFilter != null) {
            sql.append("AND o.statusid = ? ");
            params.add(statusFilter);
        }
        if (shopFilter != null) {
            sql.append("AND o.shopid = ? ");
            params.add(shopFilter);
        }
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get order by ID
     * @param orderID Order ID
     * @return Order object or null
     */
    public Order getOrderById(int orderID) {
        String sql = "SELECT o.OrderID, o.ShopID, o.CreatedBy, o.StatusID, o.CreatedAt, " +
                    "s.ShopName, st.Value as StatusName, u.FullName as CreatedByName " +
                    "FROM \"Order\" o " +
                    "LEFT JOIN Shop s ON o.ShopID = s.ShopID " +
                    "LEFT JOIN Setting st ON o.StatusID = st.SettingID " +
                    "LEFT JOIN \"User\" u ON o.CreatedBy = u.UserID " +
                    "WHERE o.OrderID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Order order = new Order();
                order.setOrderID(rs.getInt("OrderID"));
                order.setShopID(rs.getInt("ShopID"));
                order.setCreatedBy(rs.getInt("CreatedBy"));
                order.setStatusID(rs.getInt("StatusID"));
                order.setCreatedAt(rs.getTimestamp("CreatedAt"));
                
                // Set additional fields from JOINs
                order.setShopName(rs.getString("ShopName"));
                order.setStatusName(rs.getString("StatusName"));
                order.setCreatedByName(rs.getString("CreatedByName"));
                
                return order;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get order with detailed information by ID
     * @param orderID Order ID
     * @return Map with order details
     */
    public Map<String, Object> getOrderWithDetailsById(int orderID) {
        String sql = "SELECT o.OrderID, o.ShopID, o.CreatedBy, o.StatusID, o.CreatedAt, " +
                    "sh.ShopName, u.FullName as CreatedByName, st.Value as StatusName " +
                    "FROM \"Order\" o " +
                    "LEFT JOIN Shop sh ON o.ShopID = sh.ShopID " +
                    "LEFT JOIN \"User\" u ON o.CreatedBy = u.UserID " +
                    "LEFT JOIN Setting st ON o.StatusID = st.SettingID " +
                    "WHERE o.OrderID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Map<String, Object> order = new HashMap<>();
                order.put("orderID", rs.getInt("OrderID"));
                order.put("shopID", rs.getInt("ShopID"));
                order.put("createdBy", rs.getInt("CreatedBy"));
                order.put("statusID", rs.getInt("StatusID"));
                order.put("createdAt", rs.getTimestamp("CreatedAt"));
                order.put("shopName", rs.getString("ShopName"));
                order.put("createdByName", rs.getString("CreatedByName"));
                order.put("statusName", rs.getString("StatusName"));
                return order;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Insert new order
     * @param order Order object
     * @return Generated order ID or -1 if failed
     */
    public int insertOrder(Order order) {
        String sql = "INSERT INTO \"Order\" (ShopID, CreatedBy, StatusID) VALUES (?, ?, ?) RETURNING OrderID";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, order.getShopID());
            ps.setInt(2, order.getCreatedBy());
            ps.setInt(3, order.getStatusID());
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Update order
     * @param order Order object
     * @return true if successful
     */
    public boolean updateOrder(Order order) {
        String sql = "UPDATE \"Order\" SET ShopID = ?, StatusID = ? WHERE OrderID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, order.getShopID());
            ps.setInt(2, order.getStatusID());
            ps.setInt(3, order.getOrderID());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Update order status only
     * @param orderID Order ID
     * @param statusID Status ID
     * @return true if successful
     */
    public boolean updateOrderStatus(int orderID, int statusID) {
        String sql = "UPDATE \"Order\" SET StatusID = ? WHERE OrderID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, statusID);
            ps.setInt(2, orderID);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cancel order with reason
     * @param orderID Order ID
     * @param statusID Cancelled status ID
     * @param cancellationReason Reason for cancellation
     * @return true if successful
     */
    public boolean cancelOrder(int orderID, int statusID, String cancellationReason) {
        String sql = "UPDATE \"Order\" SET StatusID = ?, CancellationReason = ? WHERE OrderID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, statusID);
            ps.setString(2, cancellationReason);
            ps.setInt(3, orderID);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete order (and its details via cascade)
     * @param orderID Order ID
     * @return true if successful
     */
    public boolean deleteOrder(int orderID) {
        String sql = "DELETE FROM \"Order\" WHERE OrderID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ============== Order Details Methods ==============

    /**
     * Get all details of an order
     * @param orderID Order ID
     * @return List of order details
     */
    public List<OrderDetail> getOrderDetails(int orderID) {
        List<OrderDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM OrderDetail WHERE OrderID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                OrderDetail detail = new OrderDetail();
                detail.setOrderDetailID(rs.getInt("OrderDetailID"));
                detail.setOrderID(rs.getInt("OrderID"));
                detail.setProductID(rs.getInt("ProductID"));
                detail.setQuantity(rs.getInt("Quantity"));
                detail.setPrice(rs.getBigDecimal("Price"));
                list.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * Get order details with product information
     * @param orderID Order ID
     * @return List of maps with order detail and product info
     */
    public List<Map<String, Object>> getOrderDetailsWithProduct(int orderID) {
        List<Map<String, Object>> details = new ArrayList<>();
        String sql = "SELECT od.OrderDetailID, od.OrderID, od.ProductID, od.Quantity, od.Price, " +
                    "p.ProductName, p.Description, c.Value as CategoryName " +
                    "FROM OrderDetail od " +
                    "LEFT JOIN Product p ON od.ProductID = p.ProductID " +
                    "LEFT JOIN Setting c ON p.CategoryID = c.SettingID " +
                    "WHERE od.OrderID = ?";
        
        System.out.println("=== DEBUG getOrderDetailsWithProduct ===");
        System.out.println("Order ID: " + orderID);
        System.out.println("SQL: " + sql);
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderID);
            ResultSet rs = ps.executeQuery();
            
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
                Map<String, Object> detail = new HashMap<>();
                detail.put("orderDetailID", rs.getInt("OrderDetailID"));
                detail.put("orderID", rs.getInt("OrderID"));
                detail.put("productID", rs.getInt("ProductID"));
                detail.put("quantity", rs.getInt("Quantity"));
                detail.put("price", rs.getBigDecimal("Price"));
                detail.put("productName", rs.getString("ProductName"));
                detail.put("description", rs.getString("Description"));
                detail.put("categoryName", rs.getString("CategoryName"));
                
                // Calculate subtotal
                BigDecimal subtotal = rs.getBigDecimal("Price")
                                       .multiply(BigDecimal.valueOf(rs.getInt("Quantity")));
                detail.put("subtotal", subtotal);
                
                details.add(detail);
                System.out.println("Row " + rowCount + ": " + detail.get("productName") + " x" + detail.get("quantity"));
            }
            System.out.println("Total rows fetched: " + rowCount);
        } catch (SQLException e) {
            System.out.println("ERROR in getOrderDetailsWithProduct: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Returning " + details.size() + " details");
        System.out.println("=== END DEBUG ===");
        return details;
    }

    /**
     * Insert order detail
     * @param detail OrderDetail object
     * @return true if successful
     */
    public boolean insertOrderDetail(OrderDetail detail) {
        String sql = "INSERT INTO OrderDetail (OrderID, ProductID, Quantity, Price) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, detail.getOrderID());
            ps.setInt(2, detail.getProductID());
            ps.setInt(3, detail.getQuantity());
            ps.setBigDecimal(4, detail.getPrice());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Update order detail
     * @param detail OrderDetail object
     * @return true if successful
     */
    public boolean updateOrderDetail(OrderDetail detail) {
        String sql = "UPDATE OrderDetail SET ProductID = ?, Quantity = ?, Price = ? WHERE OrderDetailID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, detail.getProductID());
            ps.setInt(2, detail.getQuantity());
            ps.setBigDecimal(3, detail.getPrice());
            ps.setInt(4, detail.getOrderDetailID());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete order detail
     * @param orderDetailID Order Detail ID
     * @return true if successful
     */
    public boolean deleteOrderDetail(int orderDetailID) {
        String sql = "DELETE FROM OrderDetail WHERE OrderDetailID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderDetailID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Calculate total amount for an order
     * @param orderID Order ID
     * @return Total amount
     */
    public BigDecimal calculateOrderTotal(int orderID) {
        String sql = "SELECT SUM(Quantity * Price) as Total FROM OrderDetail WHERE OrderID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("Total");
                return total != null ? total : BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * Count all orders
     * @return Total count of orders
     */
    public int countAllOrders() {
        String sql = "SELECT COUNT(*) FROM \"Order\"";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Count orders by status ID
     * @param statusID Status ID to filter
     * @return Count of orders with the given status
     */
    public int countOrdersByStatus(int statusID) {
        String sql = "SELECT COUNT(*) FROM \"Order\" WHERE statusid = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, statusID);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
}

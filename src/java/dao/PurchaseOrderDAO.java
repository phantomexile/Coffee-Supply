/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import model.PurchaseOrder;
import model.PurchaseOrderDetail;
import model.PurchaseOrderView;

/**
 *
 * @author Hòa
 */
public class PurchaseOrderDAO extends BaseDAO {

    /**
     * Get all purchase orders with pagination
     */
    public List<PurchaseOrder> getAllPurchaseOrders(int page, int pageSize) {
        List<PurchaseOrder> list = new ArrayList<>();
        String sql = "SELECT * FROM PurchaseOrder ORDER BY CreatedAt DESC LIMIT ? OFFSET ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pageSize);
            ps.setInt(2, (page - 1) * pageSize);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PurchaseOrder po = new PurchaseOrder();
                po.setPoID(rs.getInt("POID"));
                po.setShopID(rs.getInt("ShopID"));
                po.setSupplierID(rs.getInt("SupplierID"));
                po.setCreatedBy(rs.getInt("CreatedBy"));
                po.setStatusID(rs.getInt("StatusID"));
                po.setCreatedAt(rs.getTimestamp("CreatedAt"));
                list.add(po);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Get all purchase orders with joined information (View)
     */
    public List<PurchaseOrderView> getAllPurchaseOrdersView() {
        return getAllPurchaseOrdersView(null, null, null);
    }

    /**
     * Get all purchase orders without pagination
     */
    
    public List<PurchaseOrder> getAllPurchaseOrders() {
        List<PurchaseOrder> list = new ArrayList<>();
        String sql = "SELECT * FROM PurchaseOrder ORDER BY CreatedAt DESC";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PurchaseOrder po = new PurchaseOrder();
                po.setPoID(rs.getInt("POID"));
                po.setShopID(rs.getInt("ShopID"));
                po.setSupplierID(rs.getInt("SupplierID"));
                po.setCreatedBy(rs.getInt("CreatedBy"));
                po.setStatusID(rs.getInt("StatusID"));
                po.setRejectReason(rs.getString("RejectReason"));
                po.setCreatedAt(rs.getTimestamp("CreatedAt"));
                list.add(po);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * Get purchase orders by status with joined information
     */
    public List<PurchaseOrderView> getPurchaseOrdersByStatus(int statusID) {
        List<PurchaseOrderView> list = new ArrayList<>();
        String sql = "SELECT po.POID, po.ShopID, po.SupplierID, po.CreatedBy, po.StatusID, "
                + "po.RejectReason, po.CreatedAt, "
                + "sh.ShopName, sup.SupplierName, u.FullName as CreatedByName, s.Value as StatusName "
                + "FROM PurchaseOrder po "
                + "LEFT JOIN Shop sh ON po.ShopID = sh.ShopID "
                + "LEFT JOIN Supplier sup ON po.SupplierID = sup.SupplierID "
                + "LEFT JOIN \"User\" u ON po.CreatedBy = u.UserID "
                + "LEFT JOIN Setting s ON po.StatusID = s.SettingID AND s.Type = 'POStatus' "
                + "WHERE po.StatusID = ? "
                + "ORDER BY po.CreatedAt DESC";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, statusID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PurchaseOrderView pov = new PurchaseOrderView();
                pov.setPoID(rs.getInt("POID"));
                pov.setShopID(rs.getInt("ShopID"));
                pov.setSupplierID(rs.getInt("SupplierID"));
                pov.setCreatedBy(rs.getInt("CreatedBy"));
                pov.setStatusID(rs.getInt("StatusID"));
                pov.setRejectReason(rs.getString("RejectReason"));
                pov.setCreatedAt(rs.getTimestamp("CreatedAt"));
                pov.setShopName(rs.getString("ShopName"));
                pov.setSupplierName(rs.getString("SupplierName"));
                pov.setCreatedByName(rs.getString("CreatedByName"));
                pov.setStatusName(rs.getString("StatusName"));
                list.add(pov);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Get total count of purchase orders
     */
    public int getTotalPurchaseOrderCount() {
        String sql = "SELECT COUNT(*) FROM PurchaseOrders";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
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
     * Get purchase order by ID
     */
    public PurchaseOrder getPurchaseOrderById(int poID) {
        String sql = "SELECT po.*, " +
                     "s.ShopName, " +
                     "sup.SupplierName, " +
                     "u.FullName as CreatedByName " +
                     "FROM PurchaseOrder po " +
                     "LEFT JOIN Shop s ON po.ShopID = s.ShopID " +
                     "LEFT JOIN Supplier sup ON po.SupplierID = sup.SupplierID " +
                     "LEFT JOIN \"User\" u ON po.CreatedBy = u.UserID " +
                     "WHERE po.POID = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, poID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                PurchaseOrder po = new PurchaseOrder();
                po.setPoID(rs.getInt("POID"));
                po.setShopID(rs.getInt("ShopID"));
                po.setSupplierID(rs.getInt("SupplierID"));
                po.setCreatedBy(rs.getInt("CreatedBy"));
                po.setStatusID(rs.getInt("StatusID"));
                po.setRejectReason(rs.getString("RejectReason"));
                po.setCreatedAt(rs.getTimestamp("CreatedAt"));
                po.setShopName(rs.getString("ShopName"));
                po.setSupplierName(rs.getString("SupplierName"));
                po.setCreatedByName(rs.getString("CreatedByName"));
                return po;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Insert new purchase order
     */
    public int insertPurchaseOrder(PurchaseOrder po) {
        String sql = "INSERT INTO PurchaseOrder (ShopID, SupplierID, CreatedBy, StatusID) VALUES (?, ?, ?, ?) RETURNING POID";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, po.getShopID());
            ps.setInt(2, po.getSupplierID());
            ps.setInt(3, po.getCreatedBy());
            ps.setInt(4, po.getStatusID());

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
     * Update purchase order
     */
    public boolean updatePurchaseOrder(PurchaseOrder po) {
        String sql = "UPDATE PurchaseOrder SET ShopID = ?, SupplierID = ?, StatusID = ? WHERE POID = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, po.getShopID());
            ps.setInt(2, po.getSupplierID());
            ps.setInt(3, po.getStatusID());
            ps.setInt(4, po.getPoID());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Update purchase order status only
     */
    public boolean updatePurchaseOrderStatus(int poID, int statusID) {
        String sql = "UPDATE PurchaseOrder SET StatusID = ? WHERE POID = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, statusID);
            ps.setInt(2, poID);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Update purchase order status with reject/cancel reason
     */
    public boolean updatePurchaseOrderStatusWithReason(int poID, int statusID, String reason) {
        String sql = "UPDATE PurchaseOrder SET StatusID = ?, RejectReason = ? WHERE POID = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, statusID);
            ps.setString(2, reason);
            ps.setInt(3, poID);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete purchase order (and its details via cascade)
     */
    public boolean deletePurchaseOrder(int poID) {
        String sql = "DELETE FROM PurchaseOrder WHERE POID = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, poID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ============== Purchase Order Details Methods ==============
    /**
     * Get all details of a purchase order
     */
    public List<PurchaseOrderDetail> getPurchaseOrderDetails(int poID) {
        List<PurchaseOrderDetail> list = new ArrayList<>();
        String sql = "SELECT pod.*, i.Name as IngredientName, s.Value as UnitName " +
                     "FROM PurchaseOrderDetail pod " +
                     "LEFT JOIN Ingredient i ON pod.IngredientID = i.IngredientID " +
                     "LEFT JOIN Setting s ON pod.UnitID = s.SettingID " +
                     "WHERE pod.POID = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, poID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PurchaseOrderDetail detail = new PurchaseOrderDetail();
                detail.setPoDetailID(rs.getInt("PODetailID"));
                detail.setPoID(rs.getInt("POID"));
                detail.setIngredientID(rs.getInt("IngredientID"));
                detail.setQuantity(rs.getBigDecimal("Quantity"));
                detail.setUnitID(rs.getInt("UnitID"));
                detail.setReceivedQuantity(rs.getBigDecimal("ReceivedQuantity"));
                detail.setIngredientName(rs.getString("IngredientName"));
                detail.setUnitName(rs.getString("UnitName"));
                list.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Insert purchase order detail
     */
    public boolean insertPurchaseOrderDetail(PurchaseOrderDetail detail) {
        String sql = "INSERT INTO PurchaseOrderDetail (POID, IngredientID, Quantity, UnitID, ReceivedQuantity) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, detail.getPoID());
            ps.setInt(2, detail.getIngredientID());
            ps.setBigDecimal(3, detail.getQuantity());
            ps.setInt(4, detail.getUnitID());
            ps.setBigDecimal(5, detail.getReceivedQuantity());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Update purchase order detail
     */
    public boolean updatePurchaseOrderDetail(PurchaseOrderDetail detail) {
        String sql = "UPDATE PurchaseOrderDetail SET IngredientID = ?, Quantity = ?, UnitID = ?, ReceivedQuantity = ? WHERE PODetailID = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, detail.getIngredientID());
            ps.setBigDecimal(2, detail.getQuantity());
            ps.setInt(3, detail.getUnitID());
            ps.setBigDecimal(4, detail.getReceivedQuantity());
            ps.setInt(5, detail.getPoDetailID());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete purchase order detail
     */
    public boolean deletePurchaseOrderDetail(int poDetailID) {
        String sql = "DELETE FROM PurchaseOrderDetail WHERE PODetailID = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, poDetailID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete all details of a purchase order
     */
    public boolean deletePurchaseOrderDetailsByPOID(int poID) {
        String sql = "DELETE FROM PurchaseOrderDetail WHERE POID = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, poID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get purchase order detail by ID
     */
    public PurchaseOrderDetail getPurchaseOrderDetailById(int poDetailID) {
        String sql = "SELECT * FROM PurchaseOrderDetail WHERE PODetailID = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, poDetailID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                PurchaseOrderDetail detail = new PurchaseOrderDetail();
                detail.setPoDetailID(rs.getInt("PODetailID"));
                detail.setPoID(rs.getInt("POID"));
                detail.setIngredientID(rs.getInt("IngredientID"));
                detail.setQuantity(rs.getBigDecimal("Quantity"));
                detail.setReceivedQuantity(rs.getBigDecimal("ReceivedQuantity"));
                return detail;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Confirm PO - Update status to Approved and update received quantities
     */
    public boolean confirmPurchaseOrder(int poID) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // Get the "Approved" status ID
            int approvedStatusID = getStatusIDByValue("Approved");
            if (approvedStatusID == -1) {
                conn.rollback();
                return false;
            }

            // Update PO status to Approved
            String updatePOSql = "UPDATE PurchaseOrder SET StatusID = ? WHERE POID = ?";
            PreparedStatement ps1 = conn.prepareStatement(updatePOSql);
            ps1.setInt(1, approvedStatusID);
            ps1.setInt(2, poID);
            ps1.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * Get status ID by value
     */
    private int getStatusIDByValue(String statusValue) {
        String sql = "SELECT SettingID FROM Setting WHERE Type = 'POStatus' AND Value = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statusValue);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("SettingID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Get purchase order view by ID
     */
    public PurchaseOrderView getPurchaseOrderViewById(int poID) {
        String sql = "SELECT po.POID, po.ShopID, po.SupplierID, po.CreatedBy, po.StatusID, "
                + "po.RejectReason, po.CreatedAt, "
                + "sh.ShopName, sup.SupplierName, u.FullName as CreatedByName, s.Value as StatusName "
                + "FROM PurchaseOrder po "
                + "LEFT JOIN Shop sh ON po.ShopID = sh.ShopID "
                + "LEFT JOIN Supplier sup ON po.SupplierID = sup.SupplierID "
                + "LEFT JOIN \"User\" u ON po.CreatedBy = u.UserID "
                + "LEFT JOIN Setting s ON po.StatusID = s.SettingID AND s.Type = 'POStatus' "
                + "WHERE po.POID = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, poID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                PurchaseOrderView pov = new PurchaseOrderView();
                pov.setPoID(rs.getInt("POID"));
                pov.setShopID(rs.getInt("ShopID"));
                pov.setSupplierID(rs.getInt("SupplierID"));
                pov.setCreatedBy(rs.getInt("CreatedBy"));
                pov.setStatusID(rs.getInt("StatusID"));
                pov.setRejectReason(rs.getString("RejectReason"));
                pov.setCreatedAt(rs.getTimestamp("CreatedAt"));
                pov.setShopName(rs.getString("ShopName"));
                pov.setSupplierName(rs.getString("SupplierName"));
                pov.setCreatedByName(rs.getString("CreatedByName"));
                pov.setStatusName(rs.getString("StatusName"));
                return pov;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get all purchase orders with joined information, filtering and sorting
     */
    public List<PurchaseOrderView> getAllPurchaseOrdersView(Integer statusFilter, String sortBy, String sortOrder) {
        List<PurchaseOrderView> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT po.POID, po.ShopID, po.SupplierID, po.CreatedBy, po.StatusID, ");
        sql.append("po.RejectReason, po.CreatedAt, ");
        sql.append("sh.ShopName, sup.SupplierName, u.FullName as CreatedByName, s.Value as StatusName ");
        sql.append("FROM PurchaseOrder po ");
        sql.append("LEFT JOIN Shop sh ON po.ShopID = sh.ShopID ");
        sql.append("LEFT JOIN Supplier sup ON po.SupplierID = sup.SupplierID ");
        sql.append("LEFT JOIN \"User\" u ON po.CreatedBy = u.UserID ");
        sql.append("LEFT JOIN Setting s ON po.StatusID = s.SettingID AND s.Type = 'POStatus' ");
        sql.append("WHERE 1=1 ");
        
        List<Object> params = new ArrayList<>();
        if (statusFilter != null) {
            sql.append("AND po.StatusID = ? ");
            params.add(statusFilter);
        }
        
        // Add sorting
        String orderByClause = "ORDER BY ";
        if (sortBy != null && !sortBy.trim().isEmpty()) {
            String columnName;
            switch (sortBy.toLowerCase()) {
                case "id":
                    columnName = "po.POID";
                    break;
                case "supplier":
                    columnName = "sup.SupplierName";
                    break;
                case "created":
                    columnName = "po.CreatedAt";
                    break;
                case "status":
                    columnName = "s.Value";
                    break;
                case "creator":
                    columnName = "u.FullName";
                    break;
                default:
                    columnName = "po.CreatedAt";
            }
            
            String order = "ASC";
            if (sortOrder != null && sortOrder.trim().equalsIgnoreCase("desc")) {
                order = "DESC";
            }
            
            orderByClause += columnName + " " + order;
        } else {
            orderByClause += "po.CreatedAt DESC";
        }
        
        sql.append(orderByClause);
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                PurchaseOrderView pov = new PurchaseOrderView();
                pov.setPoID(rs.getInt("POID"));
                pov.setShopID(rs.getInt("ShopID"));
                pov.setSupplierID(rs.getInt("SupplierID"));
                pov.setCreatedBy(rs.getInt("CreatedBy"));
                pov.setStatusID(rs.getInt("StatusID"));
                pov.setRejectReason(rs.getString("RejectReason"));
                pov.setCreatedAt(rs.getTimestamp("CreatedAt"));
                pov.setShopName(rs.getString("ShopName"));
                pov.setSupplierName(rs.getString("SupplierName"));
                pov.setCreatedByName(rs.getString("CreatedByName"));
                pov.setStatusName(rs.getString("StatusName"));
                list.add(pov);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

package service;

import dao.PurchaseOrderDAO;
import dao.SettingDAO;
import dao.IngredientDAO;
import dao.SupplierDAO;
import dao.ShopDAO;
import model.PurchaseOrder;
import model.PurchaseOrderDetail;
import model.PurchaseOrderView;
import model.Setting;
import model.Ingredient;
import model.Supplier;
import model.Shop;
import java.util.List;

/**
 * Service layer for Purchase Order business logic
 */
public class PurchaseOrderService {

    private PurchaseOrderDAO poDAO;
    private SettingDAO settingDAO;
    private IngredientDAO ingredientDAO;
    private SupplierDAO supplierDAO;
    private ShopDAO shopDAO;

    public PurchaseOrderService() {
        this.poDAO = new PurchaseOrderDAO();
        this.settingDAO = new SettingDAO();
        this.ingredientDAO = new IngredientDAO();
        this.supplierDAO = new SupplierDAO();
        this.shopDAO = new ShopDAO();
    }

    /**
     * Get all purchase orders
     */
    public List<PurchaseOrder> getAllPurchaseOrders() {
        return poDAO.getAllPurchaseOrders();
    }

    /**
     * Get all purchase orders with joined information
     */
    public List<PurchaseOrderView> getAllPurchaseOrdersView() {
        return poDAO.getAllPurchaseOrdersView();
    }

    /**
     * Get purchase orders by status
     */
    public List<PurchaseOrderView> getPurchaseOrdersByStatus(int statusID) {
        return poDAO.getPurchaseOrdersByStatus(statusID);
    }

    /**
     * Get purchase orders with pagination
     */
    public List<PurchaseOrder> getPurchaseOrders(int page, int pageSize) {
        return poDAO.getAllPurchaseOrders(page, pageSize);
    }

    /**
     * Get total count
     */
    public int getTotalCount() {
        return poDAO.getTotalPurchaseOrderCount();
    }

    /**
     * Get purchase order by ID
     */
    public PurchaseOrder getPurchaseOrderById(int poID) {
        return poDAO.getPurchaseOrderById(poID);
    }

    /**
     * Get purchase order details
     */
    public List<PurchaseOrderDetail> getPurchaseOrderDetails(int poID) {
        return poDAO.getPurchaseOrderDetails(poID);
    }

    /**
     * Create new purchase order with details
     */
    public boolean createPurchaseOrder(PurchaseOrder po, List<PurchaseOrderDetail> details) {
        // Insert PO
        int poID = poDAO.insertPurchaseOrder(po);
        if (poID <= 0) {
            return false;
        }

        // Insert details
        for (PurchaseOrderDetail detail : details) {
            detail.setPoID(poID);
            if (!poDAO.insertPurchaseOrderDetail(detail)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Update purchase order
     */
    public boolean updatePurchaseOrder(PurchaseOrder po) {
        return poDAO.updatePurchaseOrder(po);
    }
    
    /**
     * Update purchase order with details
     * This will delete all existing details and add new ones
     */
    public boolean updatePurchaseOrderWithDetails(PurchaseOrder po, List<PurchaseOrderDetail> details) {
        // Update PO info
        if (!poDAO.updatePurchaseOrder(po)) {
            return false;
        }
        
        // Delete all existing details
        poDAO.deletePurchaseOrderDetailsByPOID(po.getPoID());
        
        // Insert new details
        for (PurchaseOrderDetail detail : details) {
            detail.setPoID(po.getPoID());
            if (!poDAO.insertPurchaseOrderDetail(detail)) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * Update purchase order detail
     */
    public boolean updatePurchaseOrderDetail(PurchaseOrderDetail detail) {
        return poDAO.updatePurchaseOrderDetail(detail);
    }

    /**
     * Delete purchase order detail
     */
    public boolean deletePurchaseOrderDetail(int poDetailID) {
        return poDAO.deletePurchaseOrderDetail(poDetailID);
    }

    /**
     * Add new detail to existing PO
     */
    public boolean addPurchaseOrderDetail(PurchaseOrderDetail detail) {
        return poDAO.insertPurchaseOrderDetail(detail);
    }

    /**
     * Confirm purchase order (change status to Approved)
     */
    public boolean confirmPurchaseOrder(int poID) {
        return poDAO.confirmPurchaseOrder(poID);
    }

    /**
     * Get all PO statuses
     */
    public List<Setting> getAllPOStatuses() {
        return settingDAO.getSettingsByType("POStatus");
    }

    /**
     * Get all units
     */
    public List<Setting> getAllUnits() {
        return settingDAO.getSettingsByType("Unit");
    }

    /**
     * Get all ingredients (including inactive for PO dropdown)
     */
    public List<Ingredient> getAllIngredients() {
        return ingredientDAO.getAllIngredients(1, 1000, null, null, null); // null = lấy tất cả
    }

    /**
     * Get status name by ID
     */
    public String getStatusName(int statusID) {
        Setting setting = settingDAO.getSettingById(statusID);
        return setting != null ? setting.getValue() : "Unknown";
    }

    /**
     * Get ingredient name by ID
     */
    public String getIngredientName(int ingredientID) {
        Ingredient ingredient = ingredientDAO.getIngredientById(ingredientID);
        return ingredient != null ? ingredient.getName() : "Unknown";
    }

    /**
     * Delete purchase order
     */
    public boolean deletePurchaseOrder(int poID) {
        // First delete all details
        poDAO.deletePurchaseOrderDetailsByPOID(poID);
        // Then delete the PO
        return poDAO.deletePurchaseOrder(poID);
    }

    /**
     * Get all suppliers
     */
    public List<Supplier> getAllSuppliers() {
        return supplierDAO.getAllSuppliers();
    }

    /**
     * Get supplier name by ID
     */
    public String getSupplierName(int supplierID) {
        Supplier supplier = supplierDAO.getSupplierById(supplierID);
        return supplier != null ? supplier.getSupplierName() : "Unknown";
    }

    /**
     * Update purchase order status
     */
    public boolean updatePurchaseOrderStatus(int poID, int statusID) {
        return poDAO.updatePurchaseOrderStatus(poID, statusID);
    }

    /**
     * Update purchase order status with reason (for reject/cancel)
     */
    public boolean updatePurchaseOrderStatusWithReason(int poID, int statusID, String reason) {
        return poDAO.updatePurchaseOrderStatusWithReason(poID, statusID, reason);
    }

    /**
     * Get purchase order view by ID
     */
    public PurchaseOrderView getPurchaseOrderViewById(int poID) {
        return poDAO.getPurchaseOrderViewById(poID);
    }

    /**
     * Get all shops
     */
    public List<Shop> getAllShops() {
        return shopDAO.getAllShops();
    }

    public List<PurchaseOrderView> getAllPurchaseOrdersView(Integer statusFilter, String sortBy, String sortOrder) {
        return poDAO.getAllPurchaseOrdersView(statusFilter, sortBy, sortOrder);
    }
    
    /**
     * Check if there's already a Purchase Order created for an issue
     * This is done by checking PO notes field for "Created from Issue #X"
     */
    public boolean hasPurchaseOrderForIssue(int issueId) {
        // Get all POs and check if any has note containing this issue ID
        List<PurchaseOrder> allPOs = poDAO.getAllPurchaseOrders();
        String issueMarker = "Issue #" + issueId;
        
        for (PurchaseOrder po : allPOs) {
            // Check if notes contain reference to this issue
            // Since PO doesn't have notes field in current schema, we'll need another approach
            // For now, return false - will implement proper tracking later
            // TODO: Add IssueID field to PurchaseOrder table for proper tracking
        }
        return false;
    }
}

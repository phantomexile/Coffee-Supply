package controller;

import service.PurchaseOrderService;
import service.IngredientService;
import service.SettingService;
import service.ShopService;
import service.SupplierService;
import model.PurchaseOrder;
import model.PurchaseOrderDetail;
import model.PurchaseOrderView;
import model.Ingredient;
import model.Setting;
import model.User;
import model.Shop;
import model.Supplier;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import model.Supplier;

/**
 * Controller for Purchase Order operations
 */
@WebServlet(name = "PurchaseOrderController", urlPatterns = {"/purchase-order"})
public class PurchaseOrderController extends HttpServlet {

    private PurchaseOrderService poService;
    private IngredientService ingredientService;
    private SettingService settingService;
    private ShopService shopService;
    private SupplierService supplierService;

    @Override
    public void init() throws ServletException {
        poService = new PurchaseOrderService();
        ingredientService = new IngredientService();
        settingService = new SettingService();
        shopService = new ShopService();
        supplierService = new SupplierService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Check if user is logged in and has appropriate role
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "list":
                    showPOList(request, response);
                    break;
                case "view":
                    viewPODetails(request, response);
                    break;
                case "new":
                    showNewPOForm(request, response);
                    break;
                case "edit":
                    showEditPOForm(request, response);
                    break;
                case "confirm":
                    confirmPO(request, response);
                    break;
                default:
                    showPOList(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/common/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        try {
            switch (action) {
                case "create":
                    createPO(request, response, user);
                    break;
                case "update":
                    updatePO(request, response);
                    break;
                case "add-detail":
                    addPODetail(request, response);
                    break;
                case "update-detail":
                    updatePODetail(request, response);
                    break;
                case "delete-detail":
                    deletePODetail(request, response);
                    break;
                case "update-status":
                    updatePOStatus(request, response);
                    break;
                case "approve":
                    approvePO(request, response);
                    break;
                case "reject":
                    rejectPO(request, response);
                    break;
                case "cancel":
                    cancelPO(request, response);
                    break;
                case "delete":
                    deletePO(request, response);
                    break;
                default:
                    showPOList(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/common/error.jsp").forward(request, response);
        }
    }

    /**
     * Show list of all purchase orders
     * Support filter by status via query parameter: ?status=20
     */
   private void showPOList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get status filter from request parameter
        String statusParam = request.getParameter("status");
        Integer statusFilter = null;
        if (statusParam != null && !statusParam.isEmpty()) {
            try {
                statusFilter = Integer.parseInt(statusParam);
            } catch (NumberFormatException e) {
                // Invalid status, ignore filter
            }
        }
        
        // Get supplier filter
        String supplierParam = request.getParameter("supplier");
        Integer supplierFilter = null;
        if (supplierParam != null && !supplierParam.isEmpty()) {
            try {
                supplierFilter = Integer.parseInt(supplierParam);
            } catch (NumberFormatException e) {
                // Invalid supplier, ignore filter
            }
        }
        
        // Get search query
        String searchQuery = request.getParameter("search");
        if (searchQuery != null && searchQuery.trim().isEmpty()) {
            searchQuery = null;
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
        
        // Get all POs with sorting
        List<PurchaseOrderView> poList = poService.getAllPurchaseOrdersView(statusFilter, sortBy, sortOrder);
        
        // Apply client-side filters (search and supplier)
        if (searchQuery != null || supplierFilter != null) {
            List<PurchaseOrderView> filteredList = new java.util.ArrayList<>();
            for (PurchaseOrderView po : poList) {
                boolean match = true;
                
                // Search filter (ID or Shop name)
                if (searchQuery != null) {
                    String query = searchQuery.toLowerCase();
                    boolean searchMatch = String.valueOf(po.getPoID()).contains(query) ||
                                        (po.getShopName() != null && po.getShopName().toLowerCase().contains(query));
                    if (!searchMatch) {
                        match = false;
                    }
                }
                
                // Supplier filter
                if (supplierFilter != null && po.getSupplierID() != supplierFilter) {
                    match = false;
                }
                
                if (match) {
                    filteredList.add(po);
                }
            }
            poList = filteredList;
        }
        
        // Pagination logic
        int itemsPerPage = 5;
        int totalItems = poList.size();
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);
        
        // Get current page from request
        String pageParam = request.getParameter("page");
        int currentPage = 1;
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) currentPage = 1;
                if (currentPage > totalPages && totalPages > 0) currentPage = totalPages;
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }
        
        // Calculate start and end index
        int startIndex = (currentPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, totalItems);
        
        // Get sublist for current page
        List<PurchaseOrderView> paginatedList = new ArrayList<>();
        if (totalItems > 0) {
            paginatedList = poList.subList(startIndex, endIndex);
        }
        
        List<Setting> statuses = poService.getAllPOStatuses();
        List<Supplier> suppliers = poService.getAllSuppliers();
        
        request.setAttribute("poList", paginatedList);
        request.setAttribute("statuses", statuses);
        request.setAttribute("suppliers", suppliers);
        request.setAttribute("currentStatus", statusFilter);
        request.setAttribute("currentSupplier", supplierFilter);
        request.setAttribute("currentSearch", searchQuery);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalItems", totalItems);
        request.getRequestDispatcher("/WEB-INF/views/inventory/po-list.jsp").forward(request, response);
    }

    /**
     * View purchase order details
     */
    private void viewPODetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int poID = Integer.parseInt(request.getParameter("id"));
        
        PurchaseOrder po = poService.getPurchaseOrderById(poID);
        List<PurchaseOrderDetail> details = poService.getPurchaseOrderDetails(poID);
        List<Ingredient> ingredients = poService.getAllIngredients();
        List<Setting> statuses = poService.getAllPOStatuses();
        List<Setting> units = poService.getAllUnits();
        
        request.setAttribute("po", po);
        request.setAttribute("details", details);
        request.setAttribute("ingredients", ingredients);
        request.setAttribute("statuses", statuses);
        request.setAttribute("units", units);
        request.getRequestDispatcher("/WEB-INF/views/inventory/po-details.jsp").forward(request, response);
    }

    /**
     * Show form to create new purchase order
     */
    private void showNewPOForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Ingredient> ingredients = poService.getAllIngredients();
        List<Setting> statuses = poService.getAllPOStatuses();
        List<Setting> units = settingService.getSettingsByType("Unit");
        List<Shop> shops = shopService.getAllShops();
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        
        request.setAttribute("ingredients", ingredients);
        request.setAttribute("statuses", statuses);
        request.setAttribute("units", units);
        request.setAttribute("shops", shops);
        request.setAttribute("suppliers", suppliers);
        request.getRequestDispatcher("/WEB-INF/views/inventory/po-form.jsp").forward(request, response);
    }

    /**
     * Show form to edit purchase order
     */
    private void showEditPOForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int poID = Integer.parseInt(request.getParameter("id"));
        
        PurchaseOrder po = poService.getPurchaseOrderById(poID);
        List<PurchaseOrderDetail> details = poService.getPurchaseOrderDetails(poID);
        List<Ingredient> ingredients = poService.getAllIngredients();
        List<Setting> statuses = poService.getAllPOStatuses();
        List<Setting> units = settingService.getSettingsByType("Unit");
        List<Shop> shops = shopService.getAllShops();
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        
        request.setAttribute("po", po);
        request.setAttribute("details", details);
        request.setAttribute("ingredients", ingredients);
        request.setAttribute("statuses", statuses);
        request.setAttribute("units", units);
        request.setAttribute("shops", shops);
        request.setAttribute("suppliers", suppliers);
        request.getRequestDispatcher("/WEB-INF/views/inventory/po-form.jsp").forward(request, response);
    }

    /**
     * Create new purchase order
     */
    private void createPO(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String userRole = (String) session.getAttribute("roleName");
        
        int shopID = Integer.parseInt(request.getParameter("shopID"));
        int supplierID = Integer.parseInt(request.getParameter("supplierID"));
        
        // Admin tạo PO tự động được Approved (21), Staff tạo PO cần chờ Pending (20)
        int statusID;
        if ("Admin".equalsIgnoreCase(userRole)) {
            statusID = 21; // Approved - Admin không cần phê duyệt
        } else {
            statusID = 20; // Pending - Staff cần chờ Admin phê duyệt
        }
        
        PurchaseOrder po = new PurchaseOrder();
        po.setShopID(shopID);
        po.setSupplierID(supplierID);
        po.setCreatedBy(user.getUserID());
        po.setStatusID(statusID);
        
        // Get details
        List<PurchaseOrderDetail> details = new ArrayList<>();
        String[] ingredientIDs = request.getParameterValues("ingredientID[]");
        String[] quantities = request.getParameterValues("quantity[]");
        String[] unitIDs = request.getParameterValues("unitID[]");
        
        if (ingredientIDs != null && quantities != null) {
            for (int i = 0; i < ingredientIDs.length; i++) {
                int ingredientID = Integer.parseInt(ingredientIDs[i]);
                BigDecimal quantity = new BigDecimal(quantities[i]);
                
                // Tự động lấy unitID từ ingredient nếu unitID không hợp lệ hoặc không có
                int unitID = 0;
                if (unitIDs != null && i < unitIDs.length && unitIDs[i] != null && !unitIDs[i].isEmpty()) {
                    try {
                        unitID = Integer.parseInt(unitIDs[i]);
                    } catch (NumberFormatException e) {
                        unitID = 0;
                    }
                }
                
                // Nếu unitID <= 0, tự động lấy từ ingredient
                if (unitID <= 0) {
                    Ingredient ingredient = ingredientService.getIngredientById(ingredientID);
                    if (ingredient != null && ingredient.getUnitID() > 0) {
                        unitID = ingredient.getUnitID();
                        System.out.println("Auto-set UnitID from Ingredient: " + unitID + " for IngredientID: " + ingredientID);
                    } else {
                        session.setAttribute("errorMessage", "Không tìm thấy nguyên liệu hoặc nguyên liệu không có đơn vị hợp lệ!");
                        showNewPOForm(request, response);
                        return;
                    }
                }
                
                PurchaseOrderDetail detail = new PurchaseOrderDetail();
                detail.setIngredientID(ingredientID);
                detail.setQuantity(quantity);
                detail.setUnitID(unitID);
                detail.setReceivedQuantity(BigDecimal.ZERO);
                details.add(detail);
            }
        }
        
        boolean success = poService.createPurchaseOrder(po, details);
        
        if (success) {
            String successMsg;
            if ("Admin".equalsIgnoreCase(userRole)) {
                successMsg = "Tạo đơn hàng thành công! Đơn hàng đã được tự động phê duyệt.";
            } else {
                successMsg = "Tạo đơn hàng thành công! Đơn hàng đang chờ Admin phê duyệt.";
            }
            request.getSession().setAttribute("successMessage", successMsg);
            response.sendRedirect(request.getContextPath() + "/purchase-order?action=list");
        } else {
            request.setAttribute("errorMessage", "Không thể tạo đơn hàng. Vui lòng thử lại.");
            showNewPOForm(request, response);
        }
    }

    /**
     * Update purchase order
     * Note: Status cannot be changed here - only Admin can approve/reject
     */
    private void updatePO(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int poID = Integer.parseInt(request.getParameter("poID"));
        int shopID = Integer.parseInt(request.getParameter("shopID"));
        int supplierID = Integer.parseInt(request.getParameter("supplierID"));
        
        // Get existing PO to preserve status
        PurchaseOrder existingPO = poService.getPurchaseOrderById(poID);
        if (existingPO == null) {
            request.getSession().setAttribute("errorMessage", "Không tìm thấy đơn hàng");
            response.sendRedirect(request.getContextPath() + "/purchase-order?action=list");
            return;
        }
        
        // Only allow update if status is Pending (20)
        if (existingPO.getStatusID() != 20) {
            request.getSession().setAttribute("errorMessage", 
                "Chỉ có thể chỉnh sửa đơn hàng ở trạng thái Chờ xử lý (Pending)");
            response.sendRedirect(request.getContextPath() + "/purchase-order?action=view&id=" + poID);
            return;
        }
        
        PurchaseOrder po = new PurchaseOrder();
        po.setPoID(poID);
        po.setShopID(shopID);
        po.setSupplierID(supplierID);
        po.setStatusID(existingPO.getStatusID()); // Keep existing status
        
        // Get details from form
        List<PurchaseOrderDetail> details = new ArrayList<>();
        String[] ingredientIDs = request.getParameterValues("ingredientID[]");
        String[] quantities = request.getParameterValues("quantity[]");
        String[] unitIDs = request.getParameterValues("unitID[]");
        
        if (ingredientIDs != null && quantities != null) {
            for (int i = 0; i < ingredientIDs.length; i++) {
                int ingredientID = Integer.parseInt(ingredientIDs[i]);
                BigDecimal quantity = new BigDecimal(quantities[i]);
                
                // Tự động lấy unitID từ ingredient nếu unitID không hợp lệ hoặc không có
                int unitID = 0;
                if (unitIDs != null && i < unitIDs.length && unitIDs[i] != null && !unitIDs[i].isEmpty()) {
                    try {
                        unitID = Integer.parseInt(unitIDs[i]);
                    } catch (NumberFormatException e) {
                        unitID = 0;
                    }
                }
                
                // Nếu unitID <= 0, tự động lấy từ ingredient
                if (unitID <= 0) {
                    Ingredient ingredient = ingredientService.getIngredientById(ingredientID);
                    if (ingredient != null && ingredient.getUnitID() > 0) {
                        unitID = ingredient.getUnitID();
                        System.out.println("Auto-set UnitID from Ingredient: " + unitID + " for IngredientID: " + ingredientID);
                    } else {
                        request.getSession().setAttribute("errorMessage", "Không tìm thấy nguyên liệu hoặc nguyên liệu không có đơn vị hợp lệ!");
                        response.sendRedirect(request.getContextPath() + "/purchase-order?action=edit&id=" + poID);
                        return;
                    }
                }
                
                PurchaseOrderDetail detail = new PurchaseOrderDetail();
                detail.setIngredientID(ingredientID);
                detail.setQuantity(quantity);
                detail.setUnitID(unitID);
                detail.setReceivedQuantity(BigDecimal.ZERO);
                details.add(detail);
            }
        }
        
        // Update PO with details
        boolean success = poService.updatePurchaseOrderWithDetails(po, details);
        
        if (success) {
            request.getSession().setAttribute("successMessage", "Cập nhật đơn hàng thành công!");
            response.sendRedirect(request.getContextPath() + "/purchase-order?action=view&id=" + poID);
        } else {
            request.setAttribute("errorMessage", "Không thể cập nhật đơn hàng. Vui lòng thử lại.");
            viewPODetails(request, response);
        }
    }

    /**
     * Add detail to purchase order
     */
    private void addPODetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            int poID = Integer.parseInt(request.getParameter("poID"));
            int ingredientID = Integer.parseInt(request.getParameter("ingredientID"));
            BigDecimal quantity = new BigDecimal(request.getParameter("quantity"));
            int unitID = Integer.parseInt(request.getParameter("unitID"));
            
            // Check if PO exists and is in Pending status (only allow add detail when Pending)
            PurchaseOrder po = poService.getPurchaseOrderById(poID);
            if (po == null) {
                session.setAttribute("errorMessage", "Không tìm thấy đơn hàng!");
                response.sendRedirect(request.getContextPath() + "/purchase-order?action=list");
                return;
            }
            
            // Only allow add detail when status is Pending (20)
            if (po.getStatusID() != 20) {
                session.setAttribute("errorMessage", 
                    "Chỉ có thể thêm nguyên liệu khi đơn hàng ở trạng thái Pending!");
                response.sendRedirect(request.getContextPath() + "/purchase-order?action=view&id=" + poID);
                return;
            }
            
            // Validate quantity
            if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
                session.setAttribute("errorMessage", "Số lượng phải lớn hơn 0!");
                response.sendRedirect(request.getContextPath() + "/purchase-order?action=view&id=" + poID);
                return;
            }
            
            // Validate và tự động lấy UnitID từ Ingredient nếu unitID = 0 hoặc không hợp lệ
            if (unitID <= 0) {
                Ingredient ingredient = ingredientService.getIngredientById(ingredientID);
                if (ingredient != null && ingredient.getUnitID() > 0) {
                    unitID = ingredient.getUnitID();
                    System.out.println("Auto-set UnitID from Ingredient: " + unitID);
                } else {
                    session.setAttribute("errorMessage", "Đơn vị không hợp lệ hoặc nguyên liệu không tồn tại!");
                    response.sendRedirect(request.getContextPath() + "/purchase-order?action=view&id=" + poID);
                    return;
                }
            }
            
            PurchaseOrderDetail detail = new PurchaseOrderDetail();
            detail.setPoID(poID);
            detail.setIngredientID(ingredientID);
            detail.setQuantity(quantity);
            detail.setUnitID(unitID);
            detail.setReceivedQuantity(BigDecimal.ZERO);
            
            System.out.println("Adding PO Detail: POID=" + poID + ", IngredientID=" + ingredientID + 
                ", Quantity=" + quantity + ", UnitID=" + unitID);
            
            boolean success = poService.addPurchaseOrderDetail(detail);
            
            if (success) {
                session.setAttribute("successMessage", "Thêm nguyên liệu thành công!");
            } else {
                session.setAttribute("errorMessage", "Không thể thêm nguyên liệu. Có thể nguyên liệu đã tồn tại trong đơn hàng hoặc có lỗi hệ thống.");
            }
            
        } catch (NumberFormatException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Dữ liệu không hợp lệ: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
        }
        
        String poIDParam = request.getParameter("poID");
        if (poIDParam != null && !poIDParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/purchase-order?action=view&id=" + poIDParam);
        } else {
            response.sendRedirect(request.getContextPath() + "/purchase-order?action=list");
        }
    }

    /**
     * Update purchase order detail
     */
    private void updatePODetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int poDetailID = Integer.parseInt(request.getParameter("poDetailID"));
        int poID = Integer.parseInt(request.getParameter("poID"));
        int ingredientID = Integer.parseInt(request.getParameter("ingredientID"));
        BigDecimal quantity = new BigDecimal(request.getParameter("quantity"));
        int unitID = Integer.parseInt(request.getParameter("unitID"));
        BigDecimal receivedQuantity = new BigDecimal(request.getParameter("receivedQuantity"));
        
        PurchaseOrderDetail detail = new PurchaseOrderDetail();
        detail.setPoDetailID(poDetailID);
        detail.setPoID(poID);
        detail.setIngredientID(ingredientID);
        detail.setQuantity(quantity);
        detail.setUnitID(unitID);
        detail.setReceivedQuantity(receivedQuantity);
        
        boolean success = poService.updatePurchaseOrderDetail(detail);
        
        if (success) {
            request.getSession().setAttribute("successMessage", "Cập nhật chi tiết thành công!");
        } else {
            request.getSession().setAttribute("errorMessage", "Không thể cập nhật chi tiết. Vui lòng thử lại.");
        }
        
        response.sendRedirect(request.getContextPath() + "/purchase-order?action=view&id=" + poID);
    }

    /**
     * Delete purchase order detail
     */
    private void deletePODetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int poDetailID = Integer.parseInt(request.getParameter("poDetailID"));
        int poID = Integer.parseInt(request.getParameter("poID"));
        
        boolean success = poService.deletePurchaseOrderDetail(poDetailID);
        
        if (success) {
            request.getSession().setAttribute("successMessage", "Xóa chi tiết thành công!");
        } else {
            request.getSession().setAttribute("errorMessage", "Không thể xóa chi tiết. Vui lòng thử lại.");
        }
        
        response.sendRedirect(request.getContextPath() + "/purchase-order?action=view&id=" + poID);
    }

    /**
     * Confirm purchase order
     */
    private void confirmPO(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int poID = Integer.parseInt(request.getParameter("id"));
        
        boolean success = poService.confirmPurchaseOrder(poID);
        
        if (success) {
            request.getSession().setAttribute("successMessage", "Xác nhận đơn hàng thành công!");
        } else {
            request.getSession().setAttribute("errorMessage", "Không thể xác nhận đơn hàng. Vui lòng thử lại.");
        }
        
        response.sendRedirect(request.getContextPath() + "/purchase-order?action=view&id=" + poID);
    }
    
    /**
     * Update purchase order status (following workflow: Pending -> Approved -> Shipping -> Received)
     */
    private void updatePOStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int poID = Integer.parseInt(request.getParameter("id"));
        int newStatusID = Integer.parseInt(request.getParameter("statusID"));
        
        // Get current PO to verify workflow
        PurchaseOrder po = poService.getPurchaseOrderById(poID);
        
        // Validate correct workflow transitions
        // Status flow: 20 (Pending) -> 21 (Approved) -> 22 (Shipping) -> 23 (Received) OR 24 (Cancelled)
        boolean validTransition = false;
        
        if (po.getStatusID() == 20 && newStatusID == 21) validTransition = true;  // Pending -> Approved
        if (po.getStatusID() == 21 && newStatusID == 22) validTransition = true;  // Approved -> Shipping
        if (po.getStatusID() == 22 && newStatusID == 23) validTransition = true;  // Shipping -> Received
        if (po.getStatusID() == 22 && newStatusID == 24) validTransition = true;  // Shipping -> Cancelled
        
        if (!validTransition) {
            request.getSession().setAttribute("errorMessage", 
                "Chuyển trạng thái không hợp lệ! Vui lòng tuân thủ luồng: Pending → Approved → Shipping → Received/Cancelled");
            response.sendRedirect(request.getContextPath() + "/purchase-order?action=view&id=" + poID);
            return;
        }
        
        boolean success = poService.updatePurchaseOrderStatus(poID, newStatusID);
        
        // If transitioning to Received (status 23), update ingredient inventory
        if (success && newStatusID == 23) {
            List<PurchaseOrderDetail> details = poService.getPurchaseOrderDetails(poID);
            
            for (PurchaseOrderDetail detail : details) {
                // Only add received quantity to inventory (not the ordered quantity)
                if (detail.getReceivedQuantity() != null && detail.getReceivedQuantity().compareTo(BigDecimal.ZERO) > 0) {
                    ingredientService.increaseStockQuantity(detail.getIngredientID(), detail.getReceivedQuantity());
                }
            }
        }
        
        if (success) {
            String message = "Cập nhật trạng thái thành công!";
            if (newStatusID == 23) {
                message += " Đã cập nhật tồn kho nguyên liệu.";
            }
            request.getSession().setAttribute("successMessage", message);
        } else {
            request.getSession().setAttribute("errorMessage", "Không thể cập nhật trạng thái. Vui lòng thử lại.");
        }
        
        response.sendRedirect(request.getContextPath() + "/purchase-order?action=view&id=" + poID);
    }
    
    /**
     * Approve purchase order (Admin only, from Pending to Approved)
     */
    private void approvePO(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String roleName = (String) session.getAttribute("roleName");
        
        // Check if user is Admin
        if (!"Admin".equals(roleName)) {
            session.setAttribute("errorMessage", "Chỉ Admin mới có quyền duyệt đơn hàng!");
            response.sendRedirect(request.getContextPath() + "/purchase-order?action=list");
            return;
        }
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            idParam = request.getParameter("poID"); // Fallback to poID
        }
        
        if (idParam == null || idParam.isEmpty()) {
            session.setAttribute("errorMessage", "Thiếu ID đơn hàng!");
            response.sendRedirect(request.getContextPath() + "/purchase-order?action=list");
            return;
        }
        
        int poID = Integer.parseInt(idParam);
        
        // Get current PO to verify can approve
        PurchaseOrder po = poService.getPurchaseOrderById(poID);
        
        if (po == null) {
            session.setAttribute("errorMessage", "Không tìm thấy đơn hàng!");
            response.sendRedirect(request.getContextPath() + "/purchase-order?action=list");
            return;
        }
        
        // Only allow approve from Pending status (20)
        if (po.getStatusID() != 20) {
            session.setAttribute("errorMessage", 
                "Chỉ có thể phê duyệt đơn hàng ở trạng thái Chờ xử lý (Pending)!");
            response.sendRedirect(request.getContextPath() + "/purchase-order?action=view&id=" + poID);
            return;
        }
        
        // Status 21 = Approved
        boolean success = poService.updatePurchaseOrderStatus(poID, 21);
        
        if (success) {
            session.setAttribute("successMessage", "Phê duyệt đơn hàng thành công!");
        } else {
            session.setAttribute("errorMessage", "Không thể phê duyệt đơn hàng. Vui lòng thử lại.");
        }
        
        response.sendRedirect(request.getContextPath() + "/purchase-order?action=view&id=" + poID);
    }
    
    /**
     * Reject purchase order with reason (Admin only, from Pending status)
     */
    private void rejectPO(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String roleName = (String) session.getAttribute("roleName");
        
        // Check if user is Admin
        if (!"Admin".equals(roleName)) {
            session.setAttribute("errorMessage", "Chỉ Admin mới có quyền từ chối đơn hàng!");
            response.sendRedirect(request.getContextPath() + "/purchase-order?action=list");
            return;
        }
        
        try {
            // JSP gửi "id" và "reason"
            String poIDParam = request.getParameter("id");
            
            if (poIDParam == null || poIDParam.isEmpty()) {
                session.setAttribute("errorMessage", "Thiếu thông tin đơn hàng!");
                response.sendRedirect(request.getContextPath() + "/purchase-order?action=list");
                return;
            }
            
            int poID = Integer.parseInt(poIDParam);
            String rejectReason = request.getParameter("reason");
            
            // Get current PO to verify can reject
            PurchaseOrder po = poService.getPurchaseOrderById(poID);
            
            if (po == null) {
                session.setAttribute("errorMessage", "Không tìm thấy đơn hàng!");
                response.sendRedirect(request.getContextPath() + "/purchase-order?action=list");
                return;
            }
            
            // Only allow reject from Pending status (20)
            if (po.getStatusID() != 20) {
                session.setAttribute("errorMessage", 
                    "Chỉ có thể từ chối đơn hàng ở trạng thái Chờ xử lý (Pending)!");
                response.sendRedirect(request.getContextPath() + "/purchase-order?action=view&id=" + poID);
                return;
            }
            
            if (rejectReason == null || rejectReason.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Vui lòng nhập lý do từ chối!");
                response.sendRedirect(request.getContextPath() + "/purchase-order?action=view&id=" + poID);
                return;
            }
            
            // Status 24 = Cancelled
            boolean success = poService.updatePurchaseOrderStatusWithReason(poID, 24, rejectReason);
            
            if (success) {
                session.setAttribute("successMessage", "Từ chối đơn hàng thành công!");
            } else {
                session.setAttribute("errorMessage", "Không thể từ chối đơn hàng. Vui lòng thử lại.");
            }
            
            response.sendRedirect(request.getContextPath() + "/purchase-order?action=view&id=" + poID);
            
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Mã đơn hàng không hợp lệ!");
            response.sendRedirect(request.getContextPath() + "/purchase-order?action=list");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/purchase-order?action=list");
        }
    }
    
    /**
     * Cancel purchase order with reason (only from Shipping status)
     */
    private void cancelPO(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int poID = Integer.parseInt(request.getParameter("id"));
        String cancelReason = request.getParameter("cancelReason");
        
        // Get current PO to verify can cancel
        PurchaseOrder po = poService.getPurchaseOrderById(poID);
        
        // Can only cancel from Shipping (22) status
        if (po.getStatusID() != 22) {
            request.getSession().setAttribute("errorMessage", "Chỉ có thể hủy đơn hàng ở trạng thái Shipping!");
            response.sendRedirect(request.getContextPath() + "/purchase-order?action=view&id=" + poID);
            return;
        }
        
        if (cancelReason == null || cancelReason.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Vui lòng nhập lý do hủy đơn!");
            response.sendRedirect(request.getContextPath() + "/purchase-order?action=view&id=" + poID);
            return;
        }
        
        // Status 24 = Cancelled
        boolean success = poService.updatePurchaseOrderStatusWithReason(poID, 24, cancelReason);
        
        if (success) {
            request.getSession().setAttribute("successMessage", "Hủy đơn hàng thành công!");
        } else {
            request.getSession().setAttribute("errorMessage", "Không thể hủy đơn hàng. Vui lòng thử lại.");
        }
        
        response.sendRedirect(request.getContextPath() + "/purchase-order?action=view&id=" + poID);
    }
    
    /**
     * Delete purchase order (only Pending status and only by Inventory Staff or Admin)
     */
    private void deletePO(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String roleName = (String) session.getAttribute("roleName");
        
        // Check if user is Inventory Staff or Admin
        if (!"Inventory".equals(roleName) && !"Admin".equals(roleName)) {
            session.setAttribute("errorMessage", "Chỉ Inventory Staff hoặc Admin mới có quyền xóa đơn hàng!");
            response.sendRedirect(request.getContextPath() + "/purchase-order");
            return;
        }
        
        try {
            int poID = Integer.parseInt(request.getParameter("id"));
            
            // Get current PO to verify status
            PurchaseOrder po = poService.getPurchaseOrderById(poID);
            
            if (po == null) {
                session.setAttribute("errorMessage", "Không tìm thấy đơn hàng!");
                response.sendRedirect(request.getContextPath() + "/purchase-order");
                return;
            }
            
            // Can only delete Pending (20) status
            if (po.getStatusID() != 20) {
                session.setAttribute("errorMessage", "Chỉ có thể xóa đơn hàng ở trạng thái Pending!");
                response.sendRedirect(request.getContextPath() + "/purchase-order");
                return;
            }
            
            // Delete PO (this should also delete PO details due to foreign key cascade)
            boolean success = poService.deletePurchaseOrder(poID);
            
            if (success) {
                session.setAttribute("successMessage", "Xóa đơn hàng PO-" + poID + " thành công!");
            } else {
                session.setAttribute("errorMessage", "Không thể xóa đơn hàng. Vui lòng thử lại.");
            }
            
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "ID đơn hàng không hợp lệ!");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/purchase-order");
    }
}



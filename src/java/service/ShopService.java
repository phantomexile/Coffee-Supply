package service;

import dao.ShopDAO;
import java.util.List;
import model.Shop;


public class ShopService {
    
    private final ShopDAO shopDAO;
    
    public ShopService() {
        this.shopDAO = new ShopDAO();
    }
    
    /**
     * Result class to hold shops with pagination info
     */
    public static class ShopResult {
        private List<Object[]> shopsWithOwner;
        private int totalPages;
        private int currentPage;
        private int totalCount;
        
        public ShopResult(List<Object[]> shopsWithOwner, int totalPages, int currentPage, int totalCount) {
            this.shopsWithOwner = shopsWithOwner;
            this.totalPages = totalPages;
            this.currentPage = currentPage;
            this.totalCount = totalCount;
        }
        
        public List<Object[]> getShopsWithOwner() {
            return shopsWithOwner;
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
     * Get all shops
     */
    public List<Shop> getAllShops() {
        return shopDAO.getAllShops();
    }
    
    /**
     * Get active shops only
     */
    public List<Shop> getActiveShops() {
        return shopDAO.getActiveShops();
    }
    
    /**
     * Get shop by ID
     */
    public Shop getShopById(int shopId) {
        return shopDAO.getShopById(shopId);
    }
    
 
    
    /**
     * Create new shop
     */
    public int createShop(String shopName, String address, String phone, Integer ownerId, boolean isActive, String shopImage) {
        // Validate input
        if (shopName == null || shopName.trim().isEmpty()) {
            throw new IllegalArgumentException("Shop name is required");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address is required");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone is required");
        }
        
        Shop shop = new Shop(shopName, address, phone, ownerId, isActive, shopImage);
        return shopDAO.insertShop(shop);
    }
    
    /**
     * Update shop information
     */
    public boolean updateShop(int shopId, String shopName, String address, String phone, Integer ownerId, boolean isActive, String shopImage) {
        // Validate input
        if (shopName == null || shopName.trim().isEmpty()) {
            throw new IllegalArgumentException("Shop name is required");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address is required");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone is required");
        }
        
        Shop shop = shopDAO.getShopById(shopId);
        if (shop == null) {
            return false;
        }
        
        shop.setShopName(shopName);
        shop.setAddress(address);
        shop.setPhone(phone);
        shop.setOwnerID(ownerId);
        shop.setActive(isActive);
        shop.setShopImage(shopImage);
        
        return shopDAO.updateShop(shop);
    }
    
    /**
     * Delete shop 
     */
    public boolean deleteShop(int shopId) {
        Shop shop = shopDAO.getShopById(shopId);
        if (shop == null) {
            return false;
        }
        return shopDAO.deleteShop(shopId);
    }
    
    
    /**
     * Search shops by name
     */
    public List<Shop> searchShopsByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllShops();
        }
        return shopDAO.searchShopsByName(keyword.trim());
    }
    
    /**
     * Get all shops with owner information
     */
    public List<Object[]> getAllShopsWithOwner() {
        return shopDAO.getAllShopsWithOwner();
    }
    
    /**
     * Get shops by owner ID with owner information (paginated)
     */
    public ShopResult getShopsByOwnerWithOwnerPaginated(int ownerID, int page, int pageSize) {
        return getShopsByOwnerWithOwnerPaginated(ownerID, page, pageSize, null, null, null);
    }
    
    /**
     * Get shops by owner ID with owner information (paginated with filters)
     */
    public ShopResult getShopsByOwnerWithOwnerPaginated(int ownerID, int page, int pageSize, 
                                                       String searchName, String searchAddress, String statusFilter) {
        List<Object[]> shops = shopDAO.getShopsByOwnerWithOwnerPaginated(ownerID, page, pageSize, 
                                                                         searchName, searchAddress, statusFilter);
        int totalCount = shopDAO.getTotalShopsCountByOwner(ownerID, searchName, searchAddress, statusFilter);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        
        return new ShopResult(shops, totalPages, page, totalCount);
    }
    
    /**
     * Get shops by owner ID with owner information
     */
    public List<Object[]> getShopsByOwnerWithOwner(int ownerID) {
        return shopDAO.getShopsByOwnerWithOwner(ownerID);
    }
}

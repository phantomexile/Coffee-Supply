

package controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Setting;
import service.SettingService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
@WebServlet(urlPatterns = {"/admin/setting", "/admin/settings"})
public class SettingController extends HttpServlet {
    private SettingService settingService;

    @Override
    public void init() throws ServletException {
        settingService = new SettingService();
    }
   

   
   
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
       HttpSession session = request.getSession();
       String currentUserRole = (String) session.getAttribute("roleName");

       if(!"Admin".equals(currentUserRole)){
              response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this page.");

              return;
       }

       String action = request.getParameter("action");
       String servletPath = request.getServletPath();

       try{
              // Support alias /admin/settings to list
              if ("/admin/settings".equals(servletPath)) {
                    listSettings(request, response);
                    return;
              }

              if(action == null || action.isEmpty()){
                    listSettings(request, response);
              } else{
                    switch(action){
                           case "create":
                                    showCreateForm(request, response);
                                    break;
                            case "edit":
                                    showEditForm(request, response);
                                    break;
                            case "view":
                                    // Redirect view to edit (edit mode serves as both detail view and edit form)
                                    String viewId = request.getParameter("id");
                                    if (viewId != null && !viewId.isEmpty()) {
                                        response.sendRedirect(request.getContextPath() + "/admin/setting?action=edit&id=" + viewId);
                                    } else {
                                        response.sendRedirect(request.getContextPath() + "/admin/setting");
                                    }
                                    break;
                            case "delete":
                                    deleteSetting(request, response);
                                    break;
                            case "toggle":
                                    toggleSetting(request, response);
                                    break;
                            default:
                                    listSettings(request, response);
                                    break;
                    }
              }
       } catch(Exception e){
              e.printStackTrace();
              request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
              request.getRequestDispatcher("/WEB-INF/views/common/error.jsp").forward(request, response);
       }


    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
       HttpSession session = request.getSession();
       String currentUserRole = (String) session.getAttribute("roleName");

       if(!"Admin".equals(currentUserRole)){
              response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to perform this action.");
              return;
       }

       String action = request.getParameter("action");
       try{
              if(action == null || action.isEmpty()){
                     response.sendRedirect(request.getContextPath() + "/admin/setting");
                     return;
              }

              switch(action){
                     case "create":
                            createSetting(request, response);
                            break;
                     case "update":
                            updateSetting(request, response);
                            break;
                     default:
                            response.sendRedirect(request.getContextPath() + "/admin/setting");
                            break;
              }
       } catch(Exception e){
              e.printStackTrace();
              request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
              request.getRequestDispatcher("/WEB-INF/views/common/error.jsp").forward(request, response);
       }
    }
     private void listSettings(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get pagination parameters
        int page = 1;
        int pageSize = 10;
        
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        
        // Get filter parameters
        String typeFilter = request.getParameter("type");
        if (typeFilter != null && typeFilter.trim().isEmpty()) {
            typeFilter = null;
        }
        
        String searchFilter = request.getParameter("search");
        if (searchFilter != null && searchFilter.trim().isEmpty()) {
            
            searchFilter = null;
        }
        
        String statusFilter = request.getParameter("status");
        if (statusFilter != null && statusFilter.trim().isEmpty()) {
            statusFilter = null;
        }
        
        // Get settings
        List<Setting> settings = settingService.getSettings(page, pageSize, typeFilter, searchFilter, statusFilter);
        int totalSettings = settingService.getTotalSettingsCount(typeFilter, searchFilter, statusFilter);
        int totalPages = (int) Math.ceil((double) totalSettings / pageSize);
        
        // Get available types for filter
        List<String> availableTypes = settingService.getDistinctTypes();
        
        // Set attributes
        request.setAttribute("settings", settings);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalSettings", totalSettings);
        request.setAttribute("typeFilter", typeFilter);
        request.setAttribute("availableTypes", availableTypes);
        
        // Forward to JSP
        request.getRequestDispatcher("/WEB-INF/views/admin/setting-list.jsp").forward(request, response);
    }
    
    
private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get available types for dropdown
        List<String> availableTypes = settingService.getDistinctTypes();
        
        request.setAttribute("availableTypes", availableTypes);
        request.setAttribute("action", "create");
        
        request.getRequestDispatcher("/WEB-INF/views/admin/setting-form.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String settingIdParam = request.getParameter("id");
        if (settingIdParam == null || settingIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/setting");
            return;
        }
        
        try {
            int settingId = Integer.parseInt(settingIdParam);
            Setting setting = settingService.getSettingById(settingId);
            
            if (setting == null) {
                request.setAttribute("error", "Không tìm thấy cài đặt");
                request.getRequestDispatcher("/WEB-INF/views/common/error.jsp").forward(request, response);
                return;
            }
            
            // Get available types for dropdown
            List<String> availableTypes = settingService.getDistinctTypes();
            
            request.setAttribute("setting", setting);
            request.setAttribute("availableTypes", availableTypes);
            request.setAttribute("action", "edit");
            
            request.getRequestDispatcher("/WEB-INF/views/admin/setting-form.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/setting");
        }
    }
    
    private void createSetting(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get form data
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        String value = request.getParameter("value");
        String priorityParam = request.getParameter("priority");
        String description = request.getParameter("description");
        boolean isActive = "true".equals(request.getParameter("isActive"));
        
        // Parse priority with default value
        int priority = 1;
        if (priorityParam != null && !priorityParam.trim().isEmpty()) {
            try {
                priority = Integer.parseInt(priorityParam);
                if (priority < 1) priority = 1;
            } catch (NumberFormatException e) {
                priority = 1;
            }
        }
        
        // Create setting object
        Setting setting = new Setting();
        setting.setName(name);
        setting.setType(type);
        setting.setValue(value);
        setting.setPriority(priority);
        setting.setDescription(description);
        setting.setActive(isActive);
        
        // Create setting with detailed validation
        Map<String, String> errors = settingService.createSetting(setting);
        
        if (errors.isEmpty()) {
            // Success
            request.getSession().setAttribute("successMessage", "Tạo cài đặt thành công");
            response.sendRedirect(request.getContextPath() + "/admin/setting");
        } else {
            // Error - show validation errors
            List<String> availableTypes = settingService.getDistinctTypes();
            request.setAttribute("setting", setting);
            request.setAttribute("availableTypes", availableTypes);
            request.setAttribute("action", "create");
            request.setAttribute("validationErrors", errors);
            
            // Check if there's a system error
            if (errors.containsKey("system")) {
                request.setAttribute("error", errors.get("system"));
            }
            
            request.getRequestDispatcher("/WEB-INF/views/admin/setting-form.jsp").forward(request, response);
        }
    }
    
    private void updateSetting(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get form data
        String settingIdParam = request.getParameter("settingId");
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        String value = request.getParameter("value");
        String priorityParam = request.getParameter("priority");
        String description = request.getParameter("description");
        boolean isActive = "true".equals(request.getParameter("isActive"));
        
        try {
            int settingId = Integer.parseInt(settingIdParam);
            
            // Parse priority with default value
            int priority = 1;
            if (priorityParam != null && !priorityParam.trim().isEmpty()) {
                try {
                    priority = Integer.parseInt(priorityParam);
                    if (priority < 1) priority = 1;
                } catch (NumberFormatException e) {
                    priority = 1;
                }
            }
            
            // Create setting object
            Setting setting = new Setting();
            setting.setSettingID(settingId);
            setting.setName(name);
            setting.setType(type);
            setting.setValue(value);
            setting.setPriority(priority);
            setting.setDescription(description);
            setting.setActive(isActive);
            
            // Update setting with detailed validation
            Map<String, String> errors = settingService.updateSetting(setting);
            
            if (errors.isEmpty()) {
                // Success
                request.getSession().setAttribute("successMessage", "Cập nhật cài đặt thành công");
                response.sendRedirect(request.getContextPath() + "/admin/setting");
            } else {
                // Error - show validation errors
                List<String> availableTypes = settingService.getDistinctTypes();
                request.setAttribute("setting", setting);
                request.setAttribute("availableTypes", availableTypes);
                request.setAttribute("action", "edit");
                request.setAttribute("validationErrors", errors);
                
                // Check if there's a system error
                if (errors.containsKey("system")) {
                    request.setAttribute("error", errors.get("system"));
                }
                
                request.getRequestDispatcher("/WEB-INF/views/admin/setting-form.jsp").forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/setting");
        }
    }
    
    private void deleteSetting(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String settingIdParam = request.getParameter("id");
        
        try {
            int settingId = Integer.parseInt(settingIdParam);
            
            // Delete setting
            String error = settingService.deleteSetting(settingId);
            
            if (error == null) {
                // Success
                request.getSession().setAttribute("successMessage", "Xóa cài đặt thành công");
            } else {
                // Error
                request.getSession().setAttribute("errorMessage", error);
            }
            
            response.sendRedirect(request.getContextPath() + "/admin/setting");
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/setting");
        }
    }
    
    private void toggleSetting(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String settingIdParam = request.getParameter("id");
        
        try {
            int settingId = Integer.parseInt(settingIdParam);
            
            // Toggle setting active status
            String error = settingService.toggleSetting(settingId);
            
            if (error == null) {
                // Success
                request.getSession().setAttribute("successMessage", "Cập nhật trạng thái cài đặt thành công");
            } else {
                // Error
                request.getSession().setAttribute("errorMessage", error);
            }
            
            response.sendRedirect(request.getContextPath() + "/admin/setting");
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/setting");
        }
    }
}
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page pageEncoding="UTF-8" %>

<!-- Left side column. contains the logo and sidebar -->
<aside class="main-sidebar">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
        <!-- Sidebar user panel -->
        <div class="user-panel">
            <div class="pull-left image">
                <c:choose>
                    <c:when test="${not empty sessionScope.user.avatarUrl}">
                        <img src="${pageContext.request.contextPath}${sessionScope.user.avatarUrl}" class="img-circle" alt="User Image">
                    </c:when>
                    <c:otherwise>
                        <img src="https://via.placeholder.com/160x160/00a65a/ffffff/png?text=User" class="img-circle" alt="User Image">
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="pull-left info">
                <p>${sessionScope.user.fullName != null ? sessionScope.user.fullName : 'User'}</p>
                <a href="#"><i class="fa fa-circle text-success"></i> Online</a>
            </div>
        </div>
       
        <ul class="sidebar-menu" style="list-style: none; margin: 0; padding: 0;">
            <li class="header" style="color: #4b646f; background: #1a2226; padding: 10px 25px 10px 15px; font-size: 12px;">MAIN NAVIGATION</li>
            
            <!-- Dashboard - Hide for User role -->
            <c:if test="${sessionScope.roleName != 'User' && sessionScope.user.roleID != 5}">
            <li class="active">
                <c:choose>
                    <c:when test="${sessionScope.roleName == 'Admin'}">
                        <a href="${pageContext.request.contextPath}/admin/dashboard" style="padding: 12px 5px 12px 15px; display: block; color: #b8c7ce; text-decoration: none;">
                            <i class="fa fa-dashboard"></i> <span>Dashboard</span>
                        </a>
                    </c:when>
                    <c:when test="${sessionScope.roleName == 'HR'}">
                        <a href="${pageContext.request.contextPath}/hr/dashboard" style="padding: 12px 5px 12px 15px; display: block; color: #b8c7ce; text-decoration: none;">
                            <i class="fa fa-dashboard"></i> <span>Dashboard</span>
                        </a>
                    </c:when>
                    <c:when test="${sessionScope.roleName == 'Barista'}">
                        <a href="${pageContext.request.contextPath}/barista/dashboard" style="padding: 12px 5px 12px 15px; display: block; color: #b8c7ce; text-decoration: none;">
                            <i class="fa fa-dashboard"></i> <span>Dashboard</span>
                        </a>
                    </c:when>
                    <c:when test="${sessionScope.roleName == 'Inventory'}">
                        <a href="${pageContext.request.contextPath}/inventory/dashboard" style="padding: 12px 5px 12px 15px; display: block; color: #b8c7ce; text-decoration: none;">
                            <i class="fa fa-dashboard"></i> <span>Dashboard</span>
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/dashboard" style="padding: 12px 5px 12px 15px; display: block; color: #b8c7ce; text-decoration: none;">
                            <i class="fa fa-dashboard"></i> <span>Dashboard</span>
                        </a>
                    </c:otherwise>
                </c:choose>
            </li>
            </c:if>
            
            <!-- HR specific menu items -->
            <c:if test="${sessionScope.roleName == 'HR' || sessionScope.roleName == 'hr'}">
                <li class="treeview">
                    <a href="${pageContext.request.contextPath}/user" style="padding: 12px 5px 12px 15px; display: block; color: #b8c7ce; text-decoration: none;">
                        <i class="fa fa-users"></i>
                        <span>Quản lý người dùng</span>
                    </a>
                    <ul class="treeview-menu" style="display: none; list-style: none; margin: 0; padding: 0;">
                        <li><a href="${pageContext.request.contextPath}/user" style="color: #8aa4af; padding: 5px 5px 5px 35px; display: block; text-decoration: none;"><i class="fa fa-circle-o"></i> Danh sách người dùng</a></li>
                        <li><a href="${pageContext.request.contextPath}/user?action=create" style="color: #8aa4af; padding: 5px 5px 5px 35px; display: block; text-decoration: none;"><i class="fa fa-circle-o"></i> Thêm người dùng</a></li>
                    </ul>
                </li>
                
                <li class="treeview">
                    <a href="javascript:void(0)" onclick="toggleMenu(this)" style="padding: 12px 5px 12px 15px; display: block; color: #b8c7ce; text-decoration: none;">
                        <i class="fa fa-clock-o"></i>
                        <span>Quản lý lịch làm việc</span>
                        <span class="pull-right-container" style="float: right;">
                            <i class="fa fa-angle-left pull-right"></i>
                        </span>
                    </a>
                    <ul class="treeview-menu" style="display: none; list-style: none; margin: 0; padding: 0;">
                        <li><a href="${pageContext.request.contextPath}/shift" style="color: #8aa4af; padding: 5px 5px 5px 35px; display: block; text-decoration: none;"><i class="fa fa-circle-o"></i> Danh sách ca làm việc</a></li>
                        <li><a href="${pageContext.request.contextPath}/shift?action=create" style="color: #8aa4af; padding: 5px 5px 5px 35px; display: block; text-decoration: none;"><i class="fa fa-circle-o"></i> Thêm ca làm việc</a></li>
                        <li><a href="${pageContext.request.contextPath}/shift?action=assign" style="color: #8aa4af; padding: 5px 5px 5px 35px; display: block; text-decoration: none;"><i class="fa fa-circle-o"></i> Phân công ca làm việc</a></li>
                        <li><a href="${pageContext.request.contextPath}/shift?action=assignments" style="color: #8aa4af; padding: 5px 5px 5px 35px; display: block; text-decoration: none;"><i class="fa fa-circle-o"></i> Lịch phân công</a></li>
                    </ul>
                </li>
                
            </c:if>
            
            <!-- Inventory Staff specific menu items -->
            
            <c:if test="${sessionScope.roleName == 'Inventory' || sessionScope.roleName == 'inventory'}">
                <li class="treeview">
                    <a href="javascript:void(0)" onclick="toggleInventoryMenu(this)" style="padding: 12px 5px 12px 15px; display: block; color: #b8c7ce; text-decoration: none;">
                        <i class="fa fa-cubes"></i>
                        <span>Quản lý kho</span>
                        <span class="pull-right-container" style="float: right;">
                            <i class="fa fa-angle-left pull-right"></i>
                        </span>
                    </a>
                    <ul class="treeview-menu" style="display: none; list-style: none; margin: 0; padding: 0;">
                        <li><a href="${pageContext.request.contextPath}/ingredient" style="color: #8aa4af; padding: 5px 5px 5px 35px; display: block; text-decoration: none;"><i class="fa fa-circle-o"></i> Danh sách nguyên liệu</a></li>
                       
                    </ul>
                </li>
                
                <li class="treeview">
                    <a href="javascript:void(0)" onclick="togglePurchaseMenu(this)" style="padding: 12px 5px 12px 15px; display: block; color: #b8c7ce; text-decoration: none;">
                        <i class="fa fa-shopping-cart"></i>
                        <span>Đơn đặt hàng</span>
                        <span class="pull-right-container" style="float: right;">
                            <i class="fa fa-angle-left pull-right"></i>
                        </span>
                    </a>
                    <ul class="treeview-menu" style="display: none; list-style: none; margin: 0; padding: 0;">
                        <li><a href="${pageContext.request.contextPath}/purchase-order" style="color: #8aa4af; padding: 5px 5px 5px 35px; display: block; text-decoration: none;"><i class="fa fa-circle-o"></i> Danh sách đơn hàng</a></li>
                    </ul>
                </li>
                
                <li>
<!--                    <a href="${pageContext.request.contextPath}/supplier" style="padding: 12px 5px 12px 15px; display: block; color: #b8c7ce; text-decoration: none;">
                        <i class="fa fa-truck"></i> <span>Nhà cung cấp</span>
                    </a>-->
                </li>
                <li class="treeview">
                    <a href="javascript:void(0)" onclick="toggleMenu(this)">
                        <i class="fa fa-exclamation-triangle"></i>
                        <span>Vấn đề nhập kho</span>
                        <span class="pull-right-container"><i class="fa fa-angle-left pull-right"></i></span>
                    </a>
                    <ul class="treeview-menu">
                        <li><a href="${pageContext.request.contextPath}/issue?action=list"><i class="fa fa-circle-o"></i> Danh sách vấn đề</a></li>
                    </ul>
                </li>

            </c:if>
            
            <!-- Barista specific menu items -->
            <c:if test="${sessionScope.roleName == 'Barista' || sessionScope.roleName == 'barista'}">
                <li>
                    <a href="${pageContext.request.contextPath}/barista/orders" style="padding: 12px 5px 12px 15px; display: block; color: #b8c7ce; text-decoration: none;">
                        <i class="fa fa-coffee"></i> <span>Đơn hàng</span>
                    </a>
                </li>
                                 <li><a href="${pageContext.request.contextPath}/barista/issues"><i class="fa fa-exclamation-triangle"></i> <span>Vấn đề nhập kho</span></a></li>
                <li>
                    <a href="${pageContext.request.contextPath}/barista/menu" style="padding: 12px 5px 12px 15px; display: block; color: #b8c7ce; text-decoration: none;">
                        <i class="fa fa-list"></i> <span>Menu</span>
                    </a>
                </li>
                
                <li>
                    <a href="${pageContext.request.contextPath}/barista/schedule" style="padding: 12px 5px 12px 15px; display: block; color: #b8c7ce; text-decoration: none;">
                        <i class="fa fa-clock-o"></i> <span>Ca làm việc</span>
                    </a>
                </li>
            </c:if>
            
            <!-- Admin specific menu items -->
            <c:if test="${sessionScope.roleName == 'Admin' || sessionScope.roleName == 'admin'}">
                <li class="treeview">
                    <a href="${pageContext.request.contextPath}/user" style="padding: 12px 5px 12px 15px; display: block; color: #b8c7ce; text-decoration: none;">
                        <i class="fa fa-users"></i>
                        <span>Quản lý người dùng</span>
                    </a>
                    <ul class="treeview-menu" style="display: none; list-style: none; margin: 0; padding: 0;">
                        <li><a href="${pageContext.request.contextPath}/user" style="color: #8aa4af; padding: 5px 5px 5px 35px; display: block; text-decoration: none;"><i class="fa fa-circle-o"></i> Danh sách người dùng</a></li>
                        <li><a href="${pageContext.request.contextPath}/user?action=create" style="color: #8aa4af; padding: 5px 5px 5px 35px; display: block; text-decoration: none;"><i class="fa fa-circle-o"></i> Thêm người dùng</a></li>
                    </ul>
                </li>
                
                <li>
                    <a href="${pageContext.request.contextPath}/admin/products" style="padding: 12px 5px 12px 15px; display: block; color: #b8c7ce; text-decoration: none;">
                        <i class="fa fa-cube"></i> <span>Quản lý sản phẩm</span>
                    </a>
                </li>
                
                
                
                <li>
                    <a href="${pageContext.request.contextPath}/admin/supplier?action=list" style="padding: 12px 5px 12px 15px; display: block; color: #b8c7ce; text-decoration: none;">
                        <i class="fa fa-truck"></i> <span>Quản lý nhà cung cấp</span>
                    </a>
                </li>
                
                <li>
                    <a href="${pageContext.request.contextPath}/purchase-order?action=list" style="padding: 12px 5px 12px 15px; display: block; color: #b8c7ce; text-decoration: none;">
                        <i class="fa fa-shopping-cart"></i> <span>Danh sách PO</span>
                    </a>
                </li>
                
                <li>
                    <a href="${pageContext.request.contextPath}/admin/settings" style="padding: 12px 5px 12px 15px; display: block; color: #b8c7ce; text-decoration: none;">
                        <i class="fa fa-cogs"></i> <span>Cài đặt hệ thống</span>
                    </a>
                </li>
                
                
            </c:if>
            
            <!-- User specific menu items -->
            <c:if test="${sessionScope.roleName == 'User' || sessionScope.user.roleID == 5}">
                <li>
                    <a href="${pageContext.request.contextPath}/user/shop?action=list" style="padding: 12px 5px 12px 15px; display: block; color: #b8c7ce; text-decoration: none;">
                        <i class="fa fa-building"></i>
                        <span>Quản lý Shop</span>
                    </a>
                </li>
                
                <li>
                    <a href="${pageContext.request.contextPath}/user/token?action=management" style="padding: 12px 5px 12px 15px; display: block; color: #b8c7ce; text-decoration: none;">
                        <i class="fa fa-key"></i> <span>Token API</span>
                    </a>
                </li>
            </c:if>
            
            <!-- Common menu items for all roles -->
            <li class="header" style="color: #4b646f; background: #1a2226; padding: 10px 25px 10px 15px; font-size: 12px;">CÁ NHÂN</li>
            
            <li>
                <a href="${pageContext.request.contextPath}/profile" style="padding: 12px 5px 12px 15px; display: block; color: #b8c7ce; text-decoration: none;">
                    <i class="fa fa-user"></i> <span>Thông tin cá nhân</span>
                </a>
            </li>
            
            <li>
                <a href="${pageContext.request.contextPath}/shift/my-shifts" style="padding: 12px 5px 12px 15px; display: block; color: #b8c7ce; text-decoration: none;">
                    <i class="fa fa-clock-o"></i> <span>Xem lịch làm việc</span>
                </a>
            </li>
            
            <li class="treeview">
                <a href="javascript:void(0)" onclick="toggleMenu(this)" style="padding: 12px 5px 12px 15px; display: block; color: #b8c7ce; text-decoration: none;">
                    <i class="fa fa-cogs"></i>
                    <span>Cài đặt</span>
                    <span class="pull-right-container" style="float: right;">
                        <i class="fa fa-angle-left pull-right"></i>
                    </span>
                </a>
                <ul class="treeview-menu" style="display: none; list-style: none; margin: 0; padding: 0;">
                    <li><a href="${pageContext.request.contextPath}/profile?action=edit" style="color: #8aa4af; padding: 5px 5px 5px 35px; display: block; text-decoration: none;"><i class="fa fa-circle-o"></i> Chỉnh sửa thông tin</a></li>
                    <li><a href="${pageContext.request.contextPath}/profile?action=change-password" style="color: #8aa4af; padding: 5px 5px 5px 35px; display: block; text-decoration: none;"><i class="fa fa-circle-o"></i> Đổi mật khẩu</a></li>
                </ul>
            </li>
            
            <li>
                <a href="${pageContext.request.contextPath}/logout" style="padding: 12px 5px 12px 15px; display: block; color: #dd4b39; text-decoration: none;">
                    <i class="fa fa-sign-out"></i> <span>Đăng xuất</span>
                </a>
            </li>
        </ul>
    </section>
    <!-- /.sidebar -->
</aside>

<script>
// Đảm bảo toggleMenu function luôn có sẵn
function toggleMenu(element) {
    var menu = element.nextElementSibling;
    var icon = element.querySelector('.fa-angle-left, .fa-angle-down');
    
    if (menu && (menu.style.display === 'none' || menu.style.display === '')) {
        menu.style.display = 'block';
        if (icon) {
            icon.classList.add('fa-angle-down');
            icon.classList.remove('fa-angle-left');
        }
        element.parentElement.classList.add('active');
    } else if (menu) {
        menu.style.display = 'none';
        if (icon) {
            icon.classList.add('fa-angle-left');
            icon.classList.remove('fa-angle-down');
        }
        element.parentElement.classList.remove('active');
    }
}

function toggleInventoryMenu(element) {
    toggleMenu(element);
}

function togglePurchaseMenu(element) {
    toggleMenu(element);
}
function toggleIssueMenu(element) {
    toggleMenu(element);
}
function toggleShopMenu(element) {
    toggleMenu(element);
}
function toggleTokenMenu(element) {
    toggleMenu(element);
}

// Function to show token verification modal
function showTokenModal() {
    // Create modal HTML if it doesn't exist
    if (!document.getElementById('tokenVerifyModal')) {
        var modalHTML = `
            <div class="modal fade" id="tokenVerifyModal" tabindex="-1" role="dialog">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <form method="post" action="${pageContext.request.contextPath}/user/token">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 class="modal-title"><i class="fa fa-key"></i> Xác Thực Token</h4>
                            </div>
                            <div class="modal-body">
                                <input type="hidden" name="action" value="verify">
                                <div class="form-group">
                                    <label for="sidebarToken">Nhập Token (8 ký tự):</label>
                                    <div class="input-group">
                                        <span class="input-group-addon">
                                            <i class="fa fa-lock"></i>
                                        </span>
                                        <input type="text" class="form-control" id="sidebarToken" name="token" 
                                               placeholder="Ví dụ: ABC12345" maxlength="8" 
                                               style="text-transform: uppercase; text-align: center;" required>
                                    </div>
                                </div>
                                <div class="alert alert-info">
                                    <i class="fa fa-info-circle"></i> 
                                    Token gồm 8 ký tự, không phân biệt hoa thường. Hệ thống sẽ tự động chuyển thành chữ in hoa.
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">
                                    <i class="fa fa-times"></i> Hủy
                                </button>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fa fa-check"></i> Xác Thực Token
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        `;
        document.body.insertAdjacentHTML('beforeend', modalHTML);
        
        // Add event listener for uppercase input
        document.getElementById('sidebarToken').addEventListener('input', function() {
            this.value = this.value.toUpperCase();
        });
    }
    
    // Show the modal
    const modal = document.getElementById('tokenVerifyModal');
    if (modal && typeof bootstrap !== 'undefined') {
        new bootstrap.Modal(modal).show();
    } else if (modal) {
        // Fallback for Bootstrap 3
        modal.style.display = 'block';
        modal.classList.add('in');
        document.body.classList.add('modal-open');
    }
}

// Đặt functions vào window object để có thể truy cập globally
if (typeof window !== 'undefined') {
    window.toggleMenu = toggleMenu;
    window.toggleInventoryMenu = toggleInventoryMenu;
    window.togglePurchaseMenu = togglePurchaseMenu;
    window.toggleIssueMenu = toggleIssueMenu;
    window.toggleShopMenu = toggleShopMenu;
    window.toggleTokenMenu = toggleTokenMenu;
    window.showTokenModal = showTokenModal;
}

// Đợi DOM load xong rồi set active menu
document.addEventListener('DOMContentLoaded', function() {
    var currentPath = window.location.pathname;
    var menuItems = document.querySelectorAll('.sidebar-menu a');
    
    menuItems.forEach(function(item) {
        var href = item.getAttribute('href');
        if (href && href !== 'javascript:void(0)' && currentPath.includes(href.split('?')[0])) {
            item.parentElement.classList.add('active');
            
            // If it's a submenu item, expand the parent menu
            var parentTreeview = item.closest('.treeview');
            if (parentTreeview) {
                var parentMenu = parentTreeview.querySelector('.treeview-menu');
                var parentIcon = parentTreeview.querySelector('.fa-angle-left');
                
                if (parentMenu) {
                    parentMenu.style.display = 'block';
                    if (parentIcon) {
                        parentIcon.classList.add('fa-angle-down');
                        parentIcon.classList.remove('fa-angle-left');
                    }
                    parentTreeview.classList.add('active');
                }
            }
        }
    });
    
});


</script>

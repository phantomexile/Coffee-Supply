<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Token API - User | Coffee Shop</title>
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.6 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
    <!-- Ionicons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/AdminLTE.min.css">
    <!-- AdminLTE Skins -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/skins/_all-skins.min.css">
    <style>

        
        /* Token input styling */
        .token-container {
            margin-bottom: 20px;
        }
        
        .token-input-wrapper {
            display: flex;
            border: 1px solid #ddd;
            border-radius: 4px;
            background: #f9f9f9;
        }
        
        .token-input {
            flex: 1;
            padding: 12px 15px;
            border: none;
            background: transparent;
            font-family: 'Courier New', monospace;
            font-size: 16px;
            letter-spacing: 2px;
            outline: none;
        }
        
        .token-btn {
            padding: 12px 15px;
            border: none;
            background: #f0f0f0;
            color: #666;
            cursor: pointer;
            border-left: 1px solid #ddd;
            transition: background-color 0.2s;
        }
        
        .token-btn:hover {
            background: #e0e0e0;
            color: #333;
        }
        
        .token-btn:last-child {
            border-top-right-radius: 3px;
            border-bottom-right-radius: 3px;
        }
        
        /* Shop Token Management Styles */
        .token-display {
            font-family: 'Courier New', monospace;
            font-size: 12px;
            letter-spacing: 1px;
            border-radius: 3px;
            padding: 2px 6px;
            background-color: #f8f9fa !important;
            border: 1px solid #e9ecef;
        }
        
        .btn-group-vertical .btn {
            margin-bottom: 2px;
        }
        
        .btn-group-vertical .btn:last-child {
            margin-bottom: 0;
        }
        
        .table > tbody > tr > td {
            vertical-align: middle;
        }
        
        .shop-actions {
            min-width: 140px;
        }
        
        /* Status labels */
        .label {
            font-size: 11px;
            padding: 4px 8px;
        }
        
        /* Hover effects for token buttons */
        .token-display:hover {
            background-color: #e9ecef !important;
        }
        
        .btn-xs {
            padding: 1px 5px;
            font-size: 11px;
            line-height: 1.5;
            border-radius: 3px;
        }
        
        /* Animation for token generation */
        @keyframes tokenGenerated {
            0% { background-color: #fff3cd; }
            100% { background-color: transparent; }
        }
        
        .token-generated {
            animation: tokenGenerated 2s ease-out;
        }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
    <div class="wrapper">
        <!-- Header -->
        <jsp:include page="../compoment/header.jsp" />
        
        <!-- Sidebar -->
        <jsp:include page="../compoment/sidebar.jsp" />

        <!-- Content Wrapper -->
        <div class="content-wrapper">
            <section class="content-header">
                <h1>
                    <i class="fa fa-key"></i> Token API
                    <small>Manage your API access token</small>
                </h1>
                <ol class="breadcrumb">
                    <li><a href="${pageContext.request.contextPath}/dashboard"><i class="fa fa-dashboard"></i> Home</a></li>
                    <li class="active">Token API</li>
                </ol>
            </section>

            <section class="content">
                <!-- Shop Token Management Interface -->
                <div class="row">
                    <div class="col-md-12">
                        <div class="box box-primary">
                            <div class="box-header with-border">
                                <h3 class="box-title">
                                    <i class="fa fa-key"></i> Quản Lý Token Cho Các Shop
                                </h3>
                                <div class="box-tools pull-right">
                                    <button type="button" class="btn btn-sm btn-info" onclick="refreshPage()">
                                        <i class="fa fa-refresh"></i> Làm Mới
                                    </button>
                                </div>
                            </div>
                            <div class="box-body">
                                <c:choose>
                                    <c:when test="${not empty userShopsWithOwner}">
                                        <!-- Shops List -->
                                        <div class="table-responsive">
                                            <table class="table table-bordered table-striped">
                                                <thead>
                                                    <tr>
                                                        <th>Mã Shop</th>
                                                        <th>Tên Shop</th>
                                                        <th>Địa Chỉ</th>
                                                        <th>Trạng Thái</th>
                                                        <th>Trạng Thái Token</th>
                                                        <th>Thao Tác</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="shopData" items="${userShopsWithOwner}">
                                                        <c:set var="shop" value="${shopData[0]}" />
                                                        <c:set var="owner" value="${shopData[1]}" />
                                                        <c:set var="tokenInfo" value="${shopTokenData[shop.shopID]}" />
                                                        <tr>
                                                            <td>
                                                                <span class="label label-default">#${shop.shopID}</span>
                                                            </td>
                                                            <td>
                                                                <strong>${shop.shopName}</strong>
                                                            </td>
                                                            <td>
                                                                <small class="text-muted">${shop.address}</small>
                                                            </td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${shop.active}">
                                                                        <span class="label label-success">
                                                                            <i class="fa fa-check"></i> Hoạt Động
                                                                        </span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="label label-danger">
                                                                            <i class="fa fa-times"></i> Ngừng Hoạt Động
                                                                        </span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${tokenInfo.hasToken}">
                                                                        <span class="label label-success">
                                                                            <i class="fa fa-key"></i> Có Token
                                                                        </span>
                                                                        <br>
                                                                        <small class="text-muted">
                                                                            <code class="token-display" 
                                                                                  id="token-${shop.shopID}" 
                                                                                  style="cursor: pointer; background: #f5f5f5; padding: 2px 4px;">
                                                                                ••••••••
                                                                            </code>
                                                                            <button type="button" 
                                                                                    class="btn btn-xs btn-default" 
                                                                                    onclick="toggleTokenVisibility(${shop.shopID}, '${tokenInfo.token}')"
                                                                                    title="Ẩn/Hiện Token">
                                                                                <i class="fa fa-eye" id="eye-${shop.shopID}"></i>
                                                                            </button>
                                                                            <button type="button" 
                                                                                    class="btn btn-xs btn-default" 
                                                                                    onclick="copyTokenToClipboard('${tokenInfo.token}', ${shop.shopID})"
                                                                                    title="Sao Chép Token">
                                                                                <i class="fa fa-copy" id="copy-${shop.shopID}"></i>
                                                                            </button>
                                                                        </small>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="label label-warning">
                                                                            <i class="fa fa-exclamation-triangle"></i> Chưa Có Token
                                                                        </span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>
                                                                <div class="btn-group-vertical btn-group-sm">
                                                                    <button type="button" 
                                                                            class="btn btn-success btn-sm" 
                                                                            onclick="generateTokenForShop(${shop.shopID}, '${shop.shopName}')"
                                                                            title="Tạo Token Mới">
                                                                        <i class="fa fa-refresh"></i> Tạo Token
                                                                    </button>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <!-- No Shops Available -->
                                        <div class="alert alert-info text-center" style="margin: 50px 0;">
                                            <i class="fa fa-info-circle fa-3x" style="margin-bottom: 15px; color: #17a2b8;"></i>
                                            <h4>Không Tìm Thấy Shop</h4>
                                            <p>Bạn chưa có shop nào. Hãy tạo shop trước để quản lý token.</p>
                                            <a href="${pageContext.request.contextPath}/user/shop?action=add" 
                                               class="btn btn-primary">
                                                <i class="fa fa-plus"></i> Tạo Shop
                                            </a>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                                
                                <!-- Quick Actions -->
                                <c:if test="${not empty userShopsWithOwner}">
                                    <div class="row" style="margin-top: 20px;">
                                        <div class="col-sm-12">
                                            <div class="alert alert-info">
                                                <i class="fa fa-info-circle"></i>
                                                <strong>Hướng Dẫn:</strong>
                                                <ul style="margin: 10px 0 0 20px;">
                                                    <li>Click <strong>"Tạo Token"</strong> để tạo token truy cập mới cho shop cụ thể</li>
                                                    <li>Mỗi shop có token riêng để đảm bảo bảo mật</li>
                                                    <li>Token sẽ hết hạn sau 24 giờ</li>
                                                    <li>Sử dụng token để truy cập chi tiết shop</li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>
        <!-- /.content-wrapper -->
    </div>
    <!-- ./wrapper -->

    <!-- Token Verification Modal -->
    <div class="modal fade" id="verifyTokenModal" tabindex="-1" role="dialog">
        <div class="modal-dialog modal-sm" role="document">
            <div class="modal-content">
                <form method="post" action="${pageContext.request.contextPath}/user/token">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">
                            <i class="fa fa-key"></i> Verify Token
                        </h4>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" name="action" value="verify">
                        <div class="form-group">
                            <label for="modalTokenInput">Enter Token:</label>
                            <input type="text" class="form-control" id="modalTokenInput" name="token" 
                                   placeholder="••••••••" maxlength="8" 
                                   style="text-transform: uppercase; font-size: 16px; text-align: center; padding: 15px; font-family: 'Courier New', monospace; letter-spacing: 2px;" 
                                   required>
                        </div>
                        <div class="alert alert-info" style="margin-bottom: 0;">
                            <i class="fa fa-info-circle"></i> 
                            Enter your 8-character token to verify access.
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">
                            <i class="fa fa-times"></i> Cancel
                        </button>
                        <button type="submit" class="btn btn-primary">
                            <i class="fa fa-check"></i> Verify
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Original Token Verification Modal -->
    <div class="modal fade" id="verifyTokenModal" tabindex="-1" role="dialog">
        <div class="modal-dialog modal-sm" role="document">
            <div class="modal-content">
                <form method="post" action="${pageContext.request.contextPath}/user/token">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">
                            <i class="fa fa-key"></i> Verify Token
                        </h4>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" name="action" value="verify">
                        <div class="form-group">
                            <label for="modalToken">Enter Token:</label>
                            <input type="text" class="form-control" id="modalToken" name="token" 
                                   placeholder="••••••••" maxlength="8" 
                                   style="text-transform: uppercase; font-size: 16px; text-align: center; padding: 15px;" required>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Verify</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Bootstrap 3.3.6 -->
    <script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
    <!-- AdminLTE App -->
    <script src="${pageContext.request.contextPath}/dist/js/app.min.js"></script>
    
    <script>
        // Global variables for shop tokens
        let shopTokenVisibility = {}; // Track visibility for each shop token
        
        // Initialize page functionality
        function initializePage() {
            // Set up any additional page-specific functionality here
            console.log('Token management page initialized');
        }
        
        // Generate token for specific shop
        function generateTokenForShop(shopId, shopName) {
            if (confirm('Tạo token mới cho "' + shopName + '"? Token cũ sẽ bị vô hiệu hóa.')) {
                // Navigate to generate token endpoint
                window.location.href = '${pageContext.request.contextPath}/user/token?action=generate&shopId=' + shopId;
            }
        }
        
        // Toggle token visibility for specific shop
        function toggleTokenVisibility(shopId, token) {
            const tokenDisplay = document.getElementById('token-' + shopId);
            const eyeIcon = document.getElementById('eye-' + shopId);
            
            if (!tokenDisplay || !eyeIcon) return;
            
            // Toggle visibility state
            shopTokenVisibility[shopId] = !shopTokenVisibility[shopId];
            const isVisible = shopTokenVisibility[shopId];
            
            // Update display
            tokenDisplay.textContent = isVisible ? token : '••••••••';
            eyeIcon.className = isVisible ? 'fa fa-eye-slash' : 'fa fa-eye';
        }
        
        // Copy token to clipboard for specific shop
        function copyTokenToClipboard(token, shopId) {
            if (!token) {
                return;
            }
            
            const textarea = document.createElement('textarea');
            textarea.value = token;
            textarea.style.position = 'fixed';
            textarea.style.opacity = '0';
            document.body.appendChild(textarea);
            textarea.select();
            
            try {
                document.execCommand('copy');
                
                // Visual feedback
                const copyIcon = document.getElementById('copy-' + shopId);
                if (copyIcon) {
                    copyIcon.className = 'fa fa-check';
                    copyIcon.style.color = '#28a745';
                    setTimeout(() => {
                        copyIcon.className = 'fa fa-copy';
                        copyIcon.style.color = '';
                    }, 1500);
                }
            } catch (err) {
                // Silent fail
            } finally {
                document.body.removeChild(textarea);
            }
        }
        
        // Refresh page
        function refreshPage() {
            window.location.reload();
        }
        
        // Function to clear token verification
        function clearTokenVerification() {
            if (confirm('Bạn có chắc muốn hủy xác thực token? Bạn sẽ cần nhập token lại để truy cập shop.')) {
                fetch('${pageContext.request.contextPath}/user/token?action=clear', {
                    method: 'GET'
                })
                .then(response => {
                    if (response.ok) {
                        location.reload();
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Có lỗi xảy ra khi hủy xác thực token.');
                });
            }
        }
        
        // Set active menu
        document.addEventListener('DOMContentLoaded', function() {
            // Remove active class from all sidebar menu items
            const menuItems = document.querySelectorAll('.sidebar-menu li');
            menuItems.forEach(item => item.classList.remove('active'));
            
            // Find and activate token menu item
            const tokenLinks = document.querySelectorAll('.sidebar-menu a[href*="token"]');
            tokenLinks.forEach(link => {
                const listItem = link.closest('li');
                if (listItem) {
                    listItem.classList.add('active');
                }
            });
            
            // Expand token menu if exists
            const tokenMenus = document.querySelectorAll('.sidebar-menu a[href*="token"]');
            tokenMenus.forEach(link => {
                const treeview = link.closest('.treeview');
                if (treeview) {
                    treeview.classList.add('active');
                    const treeviewMenu = treeview.querySelector('.treeview-menu');
                    if (treeviewMenu) {
                        treeviewMenu.style.display = 'block';
                    }
                    const angleIcon = treeview.querySelector('.fa-angle-left');
                    if (angleIcon) {
                        angleIcon.classList.remove('fa-angle-left');
                        angleIcon.classList.add('fa-angle-down');
                    }
                }
            });
        });
    </script>
    
    <!-- Clear session attributes after displaying -->
    <c:remove var="message" scope="session"/>
    <c:remove var="messageType" scope="session"/>
    <c:remove var="generatedToken" scope="session"/>
</body>
</html>
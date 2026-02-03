<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Danh Sách Shop - User | Coffee Shop</title>
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
    <!-- Shop Management CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/shop-management.css">
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
                    <i class="fa fa-building"></i> Shop Của Tôi
                </h1>
                <ol class="breadcrumb">
                    <li><a href="${pageContext.request.contextPath}/views/common/dashboard.jsp"><i class="fa fa-dashboard"></i> Trang chủ</a></li>
                    <li class="active">Shop của tôi</li>
                </ol>
            </section>

            <section class="content">
                <!-- Danh sách shops với khả năng quản lý -->
                <div class="row">
                    <div class="col-xs-12">
                        <!-- Hiển thị thông báo -->
                        <c:if test="${not empty sessionScope.message}">
                            <div class="alert alert-${sessionScope.messageType} alert-dismissible">
                                <button type="button" class="close" data-dismiss="alert">&times;</button>
                                <i class="fa ${sessionScope.messageType eq 'success' ? 'fa-check' : 'fa-ban'}"></i> 
                                ${sessionScope.message}
                            </div>
                            <c:remove var="message" scope="session"/>
                            <c:remove var="messageType" scope="session"/>
                        </c:if>
                        
                        <div class="box box-success">
                            <div class="box-header with-border">
                                <h3 class="box-title">
                                    <i class="fa fa-list"></i>
                                    <span class="label label-success">${shopsWithOwner.size()} shops</span>
                                </h3>
                                <div class="box-tools">
                                    <a href="${pageContext.request.contextPath}/user/shop?action=add" 
                                       class="btn btn-success btn-sm">
                                        <i class="fa fa-plus"></i> Thêm Shop Mới
                                    </a>
                                </div>
                                </div>
                            
                            <!-- Search and Filter Section -->
                            <div class="box-header">
                                <form method="get" action="${pageContext.request.contextPath}/user/shop" id="searchForm">
                                    <input type="hidden" name="action" value="list">
                                    <input type="hidden" name="page" value="1">
                                    <input type="hidden" name="pageSize" value="${pageSize}">
                                    
                                    <div class="row">
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label for="searchName">
                                                    <i class="fa fa-search"></i> Tìm theo tên shop
                                                </label>
                                                <input type="text" class="form-control" id="searchName" name="searchName"
                                                       placeholder="Nhập tên shop để tìm kiếm..." 
                                                       value="${param.searchName}">
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label for="searchAddress">
                                                    <i class="fa fa-map-marker"></i> Tìm theo địa chỉ
                                                </label>
                                                <input type="text" class="form-control" id="searchAddress" name="searchAddress"
                                                       placeholder="Nhập địa chỉ để tìm kiếm..." 
                                                       value="${param.searchAddress}">
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label for="statusFilter">
                                                    <i class="fa fa-filter"></i> Lọc theo trạng thái
                                                </label>
                                                <select class="form-control" id="statusFilter" name="status">
                                                    <option value="">Tất cả trạng thái</option>
                                                    <option value="active" ${param.status == 'active' ? 'selected' : ''}>Đang hoạt động</option>
                                                    <option value="inactive" ${param.status == 'inactive' ? 'selected' : ''}>Ngừng hoạt động</option>
                                                </select>
                                            </div>
                                        </div>

                                    </div>
                                    <div class="row">
                                        <div class="col-md-12">
                                            <button type="submit" class="btn btn-primary">
                                                <i class="fa fa-search"></i> Tìm kiếm
                                            </button>
                                            <button type="button" class="btn btn-default" onclick="clearSearch()">
                                                <i class="fa fa-refresh"></i> Xóa bộ lọc
                                            </button>
                                            <span class="pull-right">
                                                <small class="text-muted">
                                                    <i class="fa fa-info-circle"></i> 
                                                    <c:choose>
                                                        <c:when test="${totalCount > 0}">
                                                            Hiển thị ${((currentPage - 1) * pageSize) + 1} - 
                                                            ${((currentPage - 1) * pageSize) + fn:length(shopsWithOwner)} 
                                                            trong tổng số <strong id="resultCount">${totalCount}</strong> shop
                                                        </c:when>
                                                        <c:otherwise>
                                                            Tìm thấy <strong id="resultCount">${totalCount}</strong> shop
                                                        </c:otherwise>
                                                    </c:choose>
                                                </small>
                                            </span>
                                        </div>
                                    </div>
                                </form>
                            </div>
                            
                            <div class="box-body">
                                    <c:if test="${not empty error}">
                                        <div class="alert alert-danger alert-dismissible">
                                            <button type="button" class="close" data-dismiss="alert">&times;</button>
                                            <i class="fa fa-ban"></i> ${error}
                                        </div>
                                    </c:if>
                                    
                                    <c:if test="${not empty message}">
                                        <div class="alert alert-info alert-dismissible">
                                            <button type="button" class="close" data-dismiss="alert">&times;</button>
                                            <i class="fa fa-info-circle"></i> ${message}
                                        </div>
                                    </c:if>
                                    
                                    <!-- Token Required Section -->
                                    <c:if test="${needToken}">
                                        <div class="row">
                                            <div class="col-md-8 col-md-offset-2">
                                                <div class="box box-warning">
                                                    <div class="box-header with-border">
                                                        <h3 class="box-title">
                                                            <i class="fa fa-lock"></i> Yêu cầu xác thực
                                                        </h3>
                                                    </div>
                                                    <div class="box-body text-center">
                                                        <div class="alert alert-info">
                                                            <h4><i class="fa fa-info-circle"></i> Thông báo bảo mật</h4>
                                                            <p>Để xem danh sách shops của bạn, vui lòng nhập mã token xác thực.</p>
                                                        </div>
                                                        
                                                        <div class="btn-group" role="group">
                                                            <button type="button" class="btn btn-success btn-lg" onclick="showTokenVerificationModal()">
                                                                <i class="fa fa-key"></i> Xác Thực Token
                                                            </button>
                                                            <a href="${pageContext.request.contextPath}/user/shop?action=generateToken" 
                                                               class="btn btn-primary btn-lg"
                                                               onclick="return confirm('Bạn có muốn tạo token mới? Token cũ sẽ hết hiệu lực.')">
                                                                <i class="fa fa-plus-circle"></i> Tạo Token Mới
                                                            </a>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:if>
                                    
                                    <!-- Generated token notification removed -->
                                    
                                    <c:choose>
                                        <c:when test="${empty shopsWithOwner}">
                                            <!-- No notification - empty state -->
                                        </c:when>
                                        <c:otherwise>
                                            <div class="table-responsive">
                                                <table id="shopTable" class="table table-bordered table-striped table-hover">
                                                    <thead>
                                                        <tr>
                                                            <th style="width: 50px;">ID</th>
                                                            <th style="width: 80px;">Hình ảnh</th>
                                                            <th>Tên Shop</th>
                                                            <th>Địa Chỉ</th>
                                                            <th>Tên Chủ Shop</th>
                                                            <th>Số Điện Thoại</th>
                                                            <th style="width: 100px;">Trạng Thái</th>
                                                            <th style="width: 150px;">Ngày Tạo</th>
                                                            <th style="width: 200px;">Hành Động</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach var="shopData" items="${shopsWithOwner}">
                                                            <c:set var="shop" value="${shopData[0]}" />
                                                            <c:set var="ownerName" value="${shopData[1]}" />
                                                            <tr>
                                                                <td><strong>#${shop.shopID}</strong></td>
                                                                <td class="text-center">
                                                                    <c:choose>
                                                                        <c:when test="${not empty shop.shopImage}">
                                                                            <img src="${shop.shopImage}" 
                                                                                 alt="Shop" class="img-circle"
                                                                                 style="width: 40px; height: 40px; object-fit: cover;"
                                                                                 onerror="this.style.display='none'; this.nextElementSibling.style.display='inline-block';">
                                                                            <i class="fa fa-building text-muted" style="font-size: 24px; display: none;"></i>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <i class="fa fa-building text-muted" style="font-size: 24px;"></i>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>
                                                                    <i class="fa fa-building text-primary"></i>
                                                                    <strong>${shop.shopName}</strong>
                                                                </td>
                                                                <td>
                                                                    <i class="fa fa-map-marker text-danger"></i>
                                                                    ${shop.address}
                                                                </td>
                                                                <td>
                                                                    <i class="fa fa-user text-info"></i>
                                                                    <c:choose>
                                                                        <c:when test="${not empty ownerName}">
                                                                            ${ownerName}
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="text-muted">Chưa có</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>
                                                                    <i class="fa fa-phone text-success"></i>
                                                                    ${shop.phone}
                                                                </td>
                                                                <td class="text-center">
                                                                    <c:choose>
                                                                        <c:when test="${shop.active}">
                                                                            <span class="label label-success">
                                                                                <i class="fa fa-check"></i> Hoạt động
                                                                            </span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="label label-danger">
                                                                                <i class="fa fa-times"></i> Ngừng hoạt động
                                                                            </span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>
                                                                    <i class="fa fa-calendar"></i>
                                                                    ${shop.createdAt}
                                                                </td>
                                                                <td class="text-center">
                                                                    <a href="${pageContext.request.contextPath}/user/shop?action=view&shopId=${shop.shopID}" 
                                                                       class="btn btn-primary btn-sm">
                                                                        <i class="fa fa-eye"></i> Xem
                                                                    </a>
                                                                    <!-- Generate Token Button -->
                                                                    <a href="${pageContext.request.contextPath}/user/shop?action=generateToken&shopId=${shop.shopID}" 
                                                                       class="btn btn-info btn-sm"
                                                                       title="Tạo token để xem chi tiết shop này">
                                                                        <i class="fa fa-key"></i> Token
                                                                    </a>
                                                                    <!-- Chỉ hiển thị nút Sửa/Toggle Status nếu user là chủ shop -->
                                                                    <c:if test="${shop.ownerID == sessionScope.user.userID}">
                                                                        <a href="${pageContext.request.contextPath}/user/shop?action=edit&id=${shop.shopID}" 
                                                                           class="btn btn-warning btn-sm">
                                                                            <i class="fa fa-edit"></i> Sửa
                                                                        </a>
                                                                        <!-- Toggle Status Button -->
                                                                        <c:choose>
                                                                            <c:when test="${shop.active}">
                                                                                <a href="${pageContext.request.contextPath}/user/shop?action=toggleStatus&id=${shop.shopID}" 
                                                                                   class="btn btn-danger btn-sm"
                                                                                   onclick="return confirm('Bạn có chắc muốn ngừng hoạt động shop này?')"
                                                                                   title="Ngừng hoạt động">
                                                                                    <i class="fa fa-times-circle"></i>
                                                                                </a>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <a href="${pageContext.request.contextPath}/user/shop?action=toggleStatus&id=${shop.shopID}" 
                                                                                   class="btn btn-success btn-sm"
                                                                                   onclick="return confirm('Bạn có chắc muốn kích hoạt lại shop này?')"
                                                                                   title="Kích hoạt">
                                                                                    <i class="fa fa-check-circle"></i>
                                                                                </a>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </c:if>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                </table>
                                            </div>
                                            
                                            <!-- Pagination Controls -->
                                            <c:if test="${totalPages > 1}">
                                                <div class="box-footer clearfix">
                                                    <div class="row">
                                                        <div class="col-sm-5">
                                                            <div class="dataTables_info" id="example2_info" role="status" aria-live="polite">
                                                                Hiển thị ${((currentPage - 1) * pageSize) + 1} đến 
                                                                ${((currentPage - 1) * pageSize) + fn:length(shopsWithOwner)} 
                                                                trong tổng số ${totalCount} shops
                                                            </div>
                                                        </div>
                                                        <div class="col-sm-7">
                                                            <div class="dataTables_paginate paging_simple_numbers" id="example2_paginate">
                                                                <ul class="pagination">
                                                                    <!-- Define search parameters for pagination -->
                                                                    <c:set var="searchParams" value="action=list&pageSize=${pageSize}" />
                                                                    <c:if test="${not empty param.searchName}">
                                                                        <c:set var="searchParams" value="${searchParams}&searchName=${param.searchName}" />
                                                                    </c:if>
                                                                    <c:if test="${not empty param.searchAddress}">
                                                                        <c:set var="searchParams" value="${searchParams}&searchAddress=${param.searchAddress}" />
                                                                    </c:if>
                                                                    <c:if test="${not empty param.status}">
                                                                        <c:set var="searchParams" value="${searchParams}&status=${param.status}" />
                                                                    </c:if>
                                                                    
                                                                    <!-- First page link -->
                                                                    <c:if test="${currentPage > 1}">
                                                                        <li class="paginate_button previous">
                                                                            <a href="?${searchParams}&page=1" aria-controls="example2" data-dt-idx="0" tabindex="0">« Đầu</a>
                                                                        </li>
                                                                        <li class="paginate_button previous">
                                                                            <a href="?${searchParams}&page=${currentPage - 1}" aria-controls="example2" data-dt-idx="0" tabindex="0">‹ Trước</a>
                                                                        </li>
                                                                    </c:if>

                                                                    <!-- Page numbers -->
                                                                    <c:set var="startPage" value="${currentPage - 2 < 1 ? 1 : currentPage - 2}" />
                                                                    <c:set var="endPage" value="${currentPage + 2 > totalPages ? totalPages : currentPage + 2}" />
                                                                    
                                                                    <c:forEach var="pageNum" begin="${startPage}" end="${endPage}">
                                                                        <c:choose>
                                                                            <c:when test="${pageNum == currentPage}">
                                                                                <li class="paginate_button active">
                                                                                    <a href="#" aria-controls="example2" data-dt-idx="${pageNum}" tabindex="0">${pageNum}</a>
                                                                                </li>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <li class="paginate_button">
                                                                                    <a href="?${searchParams}&page=${pageNum}" aria-controls="example2" data-dt-idx="${pageNum}" tabindex="0">${pageNum}</a>
                                                                                </li>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </c:forEach>

                                                                    <!-- Next and Last page links -->
                                                                    <c:if test="${currentPage < totalPages}">
                                                                        <li class="paginate_button next">
                                                                            <a href="?${searchParams}&page=${currentPage + 1}" aria-controls="example2" data-dt-idx="7" tabindex="0">Tiếp ›</a>
                                                                        </li>
                                                                        <li class="paginate_button next">
                                                                            <a href="?${searchParams}&page=${totalPages}" aria-controls="example2" data-dt-idx="7" tabindex="0">Cuối »</a>
                                                                        </li>
                                                                    </c:if>
                                                                </ul>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:if>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
            </section>
        </div>
        <!-- /.content-wrapper -->
    </div>
    <!-- ./wrapper -->

    <!-- Bootstrap 3.3.6 -->
    <script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
    <!-- AdminLTE App -->
    <script src="${pageContext.request.contextPath}/dist/js/app.min.js"></script>
    
    <script>
        // Search and Filter Functions (Server-side)
        function clearSearch() {
            // Clear all form fields
            document.getElementById('searchName').value = '';
            document.getElementById('searchAddress').value = '';
            document.getElementById('statusFilter').value = '';
            document.querySelector('input[name="pageSize"]').value = '${pageSize}';
            
            // Submit form to clear filters
            document.getElementById('searchForm').submit();
        }
        
        // Auto-search while typing with debounce
        let searchTimeout;
        function setupAutoSearch() {
            const searchInputs = ['searchName', 'searchAddress'];
            const form = document.getElementById('searchForm');
            
            searchInputs.forEach(inputId => {
                document.getElementById(inputId).addEventListener('input', function() {
                    clearTimeout(searchTimeout);
                    searchTimeout = setTimeout(function() {
                        // Reset to page 1 when searching
                        document.querySelector('input[name="page"]').value = '1';
                        form.submit();
                    }, 500); // 500ms debounce for server requests
                });
            });
            
            document.getElementById('statusFilter').addEventListener('change', function() {
                // Reset to page 1 when filtering
                document.querySelector('input[name="page"]').value = '1';
                form.submit();
            });
        }
        

        
        // Initialize search functionality when page loads
        document.addEventListener('DOMContentLoaded', function() {
            setupAutoSearch();
            
            // Auto-uppercase token input in modal
            const modalTokenInput = document.getElementById('modalToken');
            if (modalTokenInput) {
                modalTokenInput.addEventListener('input', function() {
                    this.value = this.value.toUpperCase();
                });
            }
        });
        
        // Function to show token verification modal
        function showTokenVerificationModal() {
            const modal = document.getElementById('tokenVerificationModal');
            if (modal && typeof bootstrap !== 'undefined') {
                new bootstrap.Modal(modal).show();
            } else if (modal) {
                // Fallback for Bootstrap 3
                modal.style.display = 'block';
                modal.classList.add('in');
                document.body.classList.add('modal-open');
            }
        }
    </script>
    
    <!-- Token Verification Modal -->
    <div class="modal fade" id="tokenVerificationModal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <form method="post" action="${pageContext.request.contextPath}/user/shop">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                        <h4 class="modal-title">
                            <i class="fa fa-key"></i> Xác Thực Token
                        </h4>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" name="action" value="verifyToken">
                        
                        <div class="alert alert-info">
                            <i class="fa fa-info-circle"></i> 
                            Để xem danh sách shops của bạn, vui lòng nhập mã token xác thực.
                        </div>
                        
                        <div class="form-group">
                            <label for="modalToken">
                                <i class="fa fa-key"></i> Mã Token (8 ký tự)
                            </label>
                            <div class="input-group">
                                <span class="input-group-addon">
                                    <i class="fa fa-lock"></i>
                                </span>
                                <input type="text" class="form-control" 
                                       id="modalToken" name="token" 
                                       placeholder="Nhập mã token 8 ký tự (VD: ABC12345)"
                                       maxlength="8" 
                                       style="text-transform: uppercase; font-size: 16px; text-align: center; padding: 15px; font-family: 'Courier New', monospace; letter-spacing: 2px;" 
                                       required>
                            </div>
                        </div>
                        
                        <div class="alert alert-warning" style="margin-bottom: 0;">
                            <strong><i class="fa fa-exclamation-triangle"></i> Lưu ý:</strong>
                            <ul style="margin-bottom: 0;">
                                <li>Token có hiệu lực trong 24 giờ</li>
                                <li>Mỗi token chỉ sử dụng được cho tài khoản của bạn</li>
                                <li>Token gồm 8 ký tự, không phân biệt hoa thường</li>
                                <li>Giữ token an toàn và không chia sẻ với người khác</li>
                            </ul>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">
                            <i class="fa fa-times"></i> Hủy
                        </button>
                        <button type="submit" class="btn btn-success">
                            <i class="fa fa-check"></i> Xác Thực Token
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- SweetAlert Library -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    
    <!-- Token Success Popup Script -->
    <c:if test="${showTokenSuccessPopup}">
        <script type="text/javascript">
            document.addEventListener('DOMContentLoaded', function() {
                // Show SweetAlert popup for token success
                Swal.fire({
                    title: 'Token Đã Tạo!',
                    html: '<div style="text-align: left;">' +
                          '<p><strong>Token:</strong> <span style="font-family: monospace; background: #f5f5f5; padding: 2px 5px; border-radius: 3px; font-size: 18px; color: #d63384;">${generatedToken}</span></p>' +
                          '<p><strong>Dành cho:</strong> ${tokenGeneratedFor}</p>' +
                          '<p class="text-warning"><i class="fa fa-exclamation-triangle"></i> <strong>Lưu ý:</strong> Token chỉ dùng để xem chi tiết shop này. Vui lòng lưu lại!</p>' +
                          '</div>',
                    icon: 'success',
                    confirmButtonText: 'Đã lưu token',
                    confirmButtonColor: '#28a745',
                    backdrop: true,
                    allowOutsideClick: false,
                    width: '500px'
                });
            });
        </script>
        <c:remove var="showTokenSuccessPopup" scope="session"/>
        <c:remove var="tokenSuccessMessage" scope="session"/>
        <c:remove var="generatedToken" scope="session"/>
        <c:remove var="generatedTokenShopId" scope="session"/>
        <c:remove var="tokenGeneratedFor" scope="session"/>
    </c:if>

    <!-- Token Error Popup Script -->
    <c:if test="${showTokenErrorPopup}">
        <script type="text/javascript">
            document.addEventListener('DOMContentLoaded', function() {
                // Show SweetAlert popup for token error
                Swal.fire({
                    title: 'Lỗi Token!',
                    text: '${tokenErrorMessage}',
                    icon: 'error',
                    confirmButtonText: 'OK',
                    confirmButtonColor: '#d33',
                    backdrop: true,
                    allowOutsideClick: false
                });
            });
        </script>
        <c:remove var="showTokenErrorPopup" scope="request"/>
        <c:remove var="tokenErrorMessage" scope="request"/>
        <c:remove var="showTokenErrorPopup" scope="session"/>
        <c:remove var="tokenErrorMessage" scope="session"/>
    </c:if>
</body>
</html>

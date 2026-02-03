<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Chi Tiết Shop - User | Coffee Shop</title>
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
                    <i class="fa fa-search"></i> Chi Tiết Shop
                    <small>Xem thông tin chi tiết của 1 shop</small>
                </h1>
                <ol class="breadcrumb">
                    <li><a href="${pageContext.request.contextPath}/views/common/dashboard.jsp"><i class="fa fa-dashboard"></i> Trang chủ</a></li>
                    <li><a href="${pageContext.request.contextPath}/user/shop?action=list"><i class="fa fa-building"></i> Danh sách Shop</a></li>
                    <li class="active">Chi tiết Shop</li>
                </ol>
            </section>

            <section class="content">
                <div class="row">
                    <div class="col-md-8 col-md-offset-2">
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible">
                                <button type="button" class="close" data-dismiss="alert">&times;</button>
                                <i class="fa fa-ban"></i> ${error}
                            </div>
                        </c:if>
                        
                        <!-- Token Required Section -->
                        <c:if test="${needToken}">
                            <div class="box box-warning">
                                <div class="box-header with-border">
                                    <h3 class="box-title">
                                        <i class="fa fa-lock"></i> Yêu cầu xác thực Token
                                    </h3>
                                </div>
                                <div class="box-body text-center">
                                    <div class="alert alert-info">
                                        <h4><i class="fa fa-info-circle"></i> Thông báo bảo mật</h4>
                                        <p>Để xem chi tiết Shop ID ${shopId}, bạn cần nhập token xác thực dành riêng cho shop này.</p>
                                    </div>
                                    
                                    <form method="get" action="${pageContext.request.contextPath}/user/shop" style="margin-top: 20px;">
                                        <input type="hidden" name="action" value="viewDetails">
                                        <input type="hidden" name="shopId" value="${shopId}">
                                        
                                        <div class="form-group" style="max-width: 300px; margin: 0 auto;">
                                            <label for="token"><strong>Nhập Token cho Shop này:</strong></label>
                                            <div class="input-group">
                                                <span class="input-group-addon">
                                                    <i class="fa fa-lock"></i>
                                                </span>
                                                <input type="text" class="form-control input-lg" id="token" name="token" 
                                                       placeholder="Ví dụ: ABC12345" 
                                                       maxlength="8" 
                                                       style="text-transform: uppercase; font-size: 18px; text-align: center; font-family: 'Courier New', monospace; letter-spacing: 2px;" 
                                                       required>
                                            </div>
                                        </div>
                                        
                                        <div style="margin-top: 20px;">
                                            <button type="submit" class="btn btn-success btn-lg">
                                                <i class="fa fa-check"></i> Xác Thực Token
                                            </button>
                                        </div>
                                    </form>
                                    
                                    <div class="alert alert-warning" style="margin-top: 30px; text-align: left;">
                                        <strong><i class="fa fa-exclamation-triangle"></i> Lưu ý:</strong>
                                        <ul style="margin-bottom: 0;">
                                            <li>Mỗi shop có token riêng biệt</li>
                                            <li>Token gồm 8 ký tự, không phân biệt hoa thường</li>
                                            <li>Token có hiệu lực trong 24 giờ</li>
                                            <li>Cần tạo token từ danh sách shop trước khi sử dụng</li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        
                        <!-- Show shop details when token is verified -->
                        
                        <c:if test="${not empty shop}">
                            <div class="box box-widget widget-user-2">
                                <div class="widget-user-header bg-aqua">
                                    <div class="widget-user-image">
                                        <c:choose>
                                            <c:when test="${not empty shop.shopImage}">
                                                <img src="${shop.shopImage}" 
                                                     alt="Shop Image" class="img-circle"
                                                     style="width: 65px; height: 65px; object-fit: cover;"
                                                     onerror="this.style.display='none'; this.nextElementSibling.style.display='block';">
                                                <i class="fa fa-building" style="font-size: 48px; margin-top: 10px; display: none;"></i>
                                            </c:when>
                                            <c:otherwise>
                                                <i class="fa fa-building" style="font-size: 48px; margin-top: 10px;"></i>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <h3 class="widget-user-username">${shop.shopName}</h3>
                                    <h5 class="widget-user-desc">
                                        <c:choose>
                                            <c:when test="${shop.active}">
                                                <span class="label label-success">
                                                    <i class="fa fa-check"></i> Đang hoạt động
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="label label-danger">
                                                    <i class="fa fa-times"></i> Ngừng hoạt động
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </h5>
                                </div>
                                <div class="box-footer no-padding">
                                    <ul class="nav nav-stacked">
                                        <li>
                                            <a href="#">
                                                <i class="fa fa-hashtag text-primary"></i> Mã Shop
                                                <span class="pull-right badge bg-blue">${shop.shopID}</span>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="#">
                                                <i class="fa fa-building text-aqua"></i> Tên Shop
                                                <span class="pull-right text-muted">${shop.shopName}</span>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="#">
                                                <i class="fa fa-map-marker text-red"></i> Địa Chỉ
                                                <span class="pull-right text-muted">${shop.address}</span>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="#">
                                                <i class="fa fa-phone text-green"></i> Số Điện Thoại
                                                <span class="pull-right text-muted">${shop.phone}</span>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="#">
                                                <i class="fa fa-user text-yellow"></i> Chủ Shop ID
                                                <span class="pull-right text-muted">
                                                    <c:choose>
                                                        <c:when test="${shop.ownerID != null}">
                                                            #${shop.ownerID}
                                                        </c:when>
                                                        <c:otherwise>
                                                            <em>Chưa có</em>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </span>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="#">
                                                <i class="fa fa-calendar text-purple"></i> Ngày Tạo
                                                <span class="pull-right text-muted">${shop.createdAt}</span>
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </c:if>

                        <!-- Actions -->
                        <div class="box-footer text-center">
                            <a href="${pageContext.request.contextPath}/user/shop?action=list" 
                               class="btn btn-default">
                                <i class="fa fa-arrow-left"></i> Quay Lại Danh Sách
                            </a>
                            <!-- Add edit/delete buttons if user owns the shop -->
                            <c:if test="${shop.ownerID == sessionScope.user.userID}">
                                <a href="${pageContext.request.contextPath}/user/shop?action=edit&id=${shop.shopID}" 
                                   class="btn btn-warning">
                                    <i class="fa fa-edit"></i> Chỉnh Sửa
                                </a>
                                <a href="${pageContext.request.contextPath}/user/shop?action=delete&id=${shop.shopID}" 
                                   class="btn btn-danger"
                                   onclick="return confirm('Bạn có chắc muốn xóa shop này?')">
                                    <i class="fa fa-trash"></i> Xóa Shop
                                </a>
                            </c:if>
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
    
    <!-- SweetAlert Library -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    
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
    
    <script>
        // Auto-uppercase token input
        document.addEventListener('DOMContentLoaded', function() {
            const tokenInput = document.getElementById('token');
            if (tokenInput) {
                tokenInput.addEventListener('input', function() {
                    this.value = this.value.toUpperCase();
                });
            }
        });
    </script>

</body>
</html>

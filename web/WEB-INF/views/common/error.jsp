<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>
        <c:choose>
            <c:when test="${requestScope['javax.servlet.error.status_code'] == 403}">
                403 - Truy cập bị từ chối - Coffee Shop Management
            </c:when>
            <c:when test="${requestScope['javax.servlet.error.status_code'] == 404}">
                404 - Không tìm thấy trang - Coffee Shop Management
            </c:when>
            <c:otherwise>
                Lỗi hệ thống - Coffee Shop Management
            </c:otherwise>
        </c:choose>
    </title>
    <!-- Tell the browser to be responsive to screen width -->
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
        .error-page {
            width: 600px;
            margin: 20px auto 0 auto;
        }
        .error-page > .headline {
            float: left;
            font-size: 100px;
            font-weight: 300;
            text-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .error-page > .error-content {
            margin-left: 190px;
        }
        .error-page > .error-content > h3 {
            font-weight: 300;
            font-size: 25px;
            margin-bottom: 20px;
        }
        
        /* Enhanced styles for different error types */
        .alert {
            border-radius: 8px;
            border: none;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        
        .alert-info {
            background: linear-gradient(135deg, #d1ecf1 0%, #bee5eb 100%);
            border-left: 4px solid #17a2b8;
        }
        
        .alert-danger {
            background: linear-gradient(135deg, #f8d7da 0%, #f5c6cb 100%);
            border-left: 4px solid #dc3545;
        }
        
        .btn-group {
            margin-top: 20px;
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }
        
        .btn {
            border-radius: 25px;
            padding: 10px 20px;
            font-weight: 600;
            transition: all 0.3s ease;
            border: none;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        
        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.15);
        }
        
        .btn-default {
            background: linear-gradient(135deg, #6c757d 0%, #5a6268 100%);
            color: white;
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #007bff 0%, #0056b3 100%);
        }
        
        .headline.text-red {
            color: #dc3545;
        }
        
        /* Responsive improvements */
        @media (max-width: 991px) {
            .error-page {
                width: 100%;
                margin: 20px 0 0 0;
                padding: 0 15px;
            }
            .error-page > .headline {
                float: none;
                text-align: center;
                margin-bottom: 20px;
            }
            .error-page > .error-content {
                margin-left: 0;
                text-align: center;
            }
        }
        
        @media (max-width: 768px) {
            .error-page > .headline {
                font-size: 80px;
            }
            .error-page > .error-content > h3 {
                font-size: 20px;
            }
            .btn-group {
                justify-content: center;
            }
            .btn {
                flex: 1;
                min-width: 120px;
            }
        }
        
        /* Animation for error icon */
        @keyframes pulse {
            0% { transform: scale(1); }
            50% { transform: scale(1.05); }
            100% { transform: scale(1); }
        }
        
        .fa-warning, .fa-lock, .fa-search, .fa-cogs {
            animation: pulse 2s infinite;
        }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

    <!-- Check if user is logged in to show header/sidebar -->
    <c:if test="${not empty sessionScope.user}">
        <!-- Include Header -->
        <%@include file="../compoment/header.jsp" %>
        
        <!-- Include Sidebar -->
        <%@include file="../compoment/sidebar.jsp" %>
    </c:if>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper" style="${empty sessionScope.user ? 'margin-left: 0;' : ''}">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                <c:choose>
                    <c:when test="${requestScope['javax.servlet.error.status_code'] == 403}">
                        Truy cập bị từ chối
                        <small>Bạn không có quyền truy cập</small>
                    </c:when>
                    <c:when test="${requestScope['javax.servlet.error.status_code'] == 404}">
                        Không tìm thấy trang
                        <small>Trang bạn tìm kiếm không tồn tại</small>
                    </c:when>
                    <c:otherwise>
                        Lỗi hệ thống
                        <small>Đã xảy ra lỗi</small>
                    </c:otherwise>
                </c:choose>
            </h1>
            <c:if test="${not empty sessionScope.user}">
                <ol class="breadcrumb">
                    <li><a href="${pageContext.request.contextPath}/"><i class="fa fa-dashboard"></i> Trang chủ</a></li>
                    <li class="active">
                        <c:choose>
                            <c:when test="${requestScope['javax.servlet.error.status_code'] == 403}">403 - Truy cập bị từ chối</c:when>
                            <c:when test="${requestScope['javax.servlet.error.status_code'] == 404}">404 - Không tìm thấy</c:when>
                            <c:otherwise>Lỗi hệ thống</c:otherwise>
                        </c:choose>
                    </li>
                </ol>
            </c:if>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="error-page">
                <h2 class="headline text-red">
                    <c:choose>
                        <c:when test="${requestScope['javax.servlet.error.status_code'] == 403}">
                            403
                        </c:when>
                        <c:when test="${requestScope['javax.servlet.error.status_code'] == 404}">
                            404
                        </c:when>
                        <c:when test="${requestScope['javax.servlet.error.status_code'] == 500}">
                            500
                        </c:when>
                        <c:otherwise>
                            <i class="fa fa-warning text-red"></i>
                        </c:otherwise>
                    </c:choose>
                </h2>
                
                <div class="error-content">
                    <h3>
                        <c:choose>
                            <c:when test="${requestScope['javax.servlet.error.status_code'] == 403}">
                                <i class="fa fa-lock text-red"></i> Truy cập bị từ chối!
                            </c:when>
                            <c:when test="${requestScope['javax.servlet.error.status_code'] == 404}">
                                <i class="fa fa-search text-red"></i> Không tìm thấy trang!
                            </c:when>
                            <c:when test="${requestScope['javax.servlet.error.status_code'] == 500}">
                                <i class="fa fa-cogs text-red"></i> Lỗi máy chủ!
                            </c:when>
                            <c:otherwise>
                                <i class="fa fa-warning text-red"></i> Oops! Đã xảy ra lỗi.
                            </c:otherwise>
                        </c:choose>
                    </h3>
                    
                    <!-- Error Message based on type -->
                    <c:choose>
                        <c:when test="${requestScope['javax.servlet.error.status_code'] == 403}">
                            <p>Xin lỗi, bạn không có quyền truy cập vào tài nguyên này. Vui lòng kiểm tra lại quyền hạn của bạn hoặc liên hệ với quản trị viên.</p>
                            
                            <!-- Show user info if logged in -->
                            <c:if test="${not empty sessionScope.user}">
                                <div class="alert alert-info">
                                    <h4><i class="icon fa fa-user"></i> Thông tin tài khoản hiện tại:</h4>
                                    <p><strong>Tên:</strong> ${sessionScope.user.fullName}</p>
                                    <p><strong>Email:</strong> ${sessionScope.user.email}</p>
                                    <p><strong>Vai trò:</strong> ${sessionScope.roleName}</p>
                                </div>
                            </c:if>
                        </c:when>
                        <c:when test="${requestScope['javax.servlet.error.status_code'] == 404}">
                            <p>Trang bạn đang tìm kiếm không tồn tại hoặc đã được di chuyển. Vui lòng kiểm tra lại URL hoặc sử dụng menu điều hướng.</p>
                        </c:when>
                        <c:when test="${requestScope['javax.servlet.error.status_code'] == 500}">
                            <p>Máy chủ đang gặp sự cố kỹ thuật. Chúng tôi đang khắc phục vấn đề này. Vui lòng thử lại sau ít phút.</p>
                        </c:when>
                        <c:when test="${not empty error}">
                            <div class="alert alert-danger">
                                <h4><i class="icon fa fa-ban"></i> Chi tiết lỗi:</h4>
                                ${error}
                            </div>
                        </c:when>
                        <c:when test="${not empty requestScope['javax.servlet.error.message']}">
                            <div class="alert alert-danger">
                                <h4><i class="icon fa fa-ban"></i> Chi tiết lỗi:</h4>
                                ${requestScope['javax.servlet.error.message']}
                            </div>
                        </c:when>
                        <c:otherwise>
                            <p>
                                Chúng tôi đang gặp một số vấn đề kỹ thuật. 
                                Vui lòng thử lại sau hoặc liên hệ với quản trị viên hệ thống.
                            </p>
                        </c:otherwise>
                    </c:choose>
                    
                    <!-- Suggestions based on error type -->
                    <div class="alert alert-info">
                        <h4><i class="icon fa fa-info"></i> Gợi ý:</h4>
                        <ul>
                            <c:choose>
                                <c:when test="${requestScope['javax.servlet.error.status_code'] == 403}">
                                    <c:choose>
                                        <c:when test="${empty sessionScope.user}">
                                            <li>Bạn chưa đăng nhập. Vui lòng đăng nhập để tiếp tục.</li>
                                            <li>Kiểm tra lại thông tin đăng nhập của bạn.</li>
                                        </c:when>
                                        <c:otherwise>
                                            <li>Kiểm tra lại quyền hạn của tài khoản hiện tại.</li>
                                            <li>Liên hệ với quản trị viên để được cấp quyền truy cập.</li>
                                            <li>Đảm bảo bạn đang truy cập đúng chức năng cho vai trò của mình.</li>
                                        </c:otherwise>
                                    </c:choose>
                                    <li>Thử đăng xuất và đăng nhập lại với tài khoản khác.</li>
                                    <li>Liên hệ bộ phận hỗ trợ kỹ thuật nếu vấn đề vẫn tiếp tục.</li>
                                </c:when>
                                <c:when test="${requestScope['javax.servlet.error.status_code'] == 404}">
                                    <li>Kiểm tra lại đường dẫn URL có chính xác không.</li>
                                    <li>Sử dụng menu điều hướng để truy cập các trang khác.</li>
                                    <li>Quay về trang chủ và điều hướng từ đó.</li>
                                    <li>Liên hệ quản trị viên nếu bạn cho rằng trang này nên tồn tại.</li>
                                </c:when>
                                <c:when test="${requestScope['javax.servlet.error.status_code'] == 500}">
                                    <li>Thử làm mới trang (F5) sau vài giây.</li>
                                    <li>Kiểm tra kết nối internet của bạn.</li>
                                    <li>Thử truy cập lại sau 5-10 phút.</li>
                                    <li>Liên hệ bộ phận hỗ trợ kỹ thuật nếu lỗi vẫn tiếp tục.</li>
                                </c:when>
                                <c:otherwise>
                                    <li>Kiểm tra lại đường dẫn URL</li>
                                    <li>Đảm bảo bạn đã đăng nhập với quyền phù hợp</li>
                                    <li>Thử làm mới trang (F5)</li>
                                    <li>Liên hệ với bộ phận hỗ trợ kỹ thuật</li>
                                </c:otherwise>
                            </c:choose>
                        </ul>
                    </div>
                    
                    <div class="btn-group">
                        <button type="button" onclick="history.back()" class="btn btn-default">
                            <i class="fa fa-arrow-left"></i> Quay lại
                        </button>
                        <c:choose>
                            <c:when test="${not empty sessionScope.user}">
                                <c:choose>
                                    <c:when test="${sessionScope.roleName == 'HR'}">
                                        <a href="${pageContext.request.contextPath}/hr/dashboard" class="btn btn-primary">
                                            <i class="fa fa-home"></i> Trang chủ HR
                                        </a>
                                    </c:when>
                                    <c:when test="${sessionScope.roleName == 'Admin'}">
                                        <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-primary">
                                            <i class="fa fa-home"></i> Trang chủ Admin
                                        </a>
                                    </c:when>
                                    <c:when test="${sessionScope.roleName == 'Inventory'}">
                                        <a href="${pageContext.request.contextPath}/inventory/dashboard" class="btn btn-primary">
                                            <i class="fa fa-home"></i> Trang chủ Inventory
                                        </a>
                                    </c:when>
                                    <c:when test="${sessionScope.roleName == 'Barista'}">
                                        <a href="${pageContext.request.contextPath}/barista/dashboard" class="btn btn-primary">
                                            <i class="fa fa-home"></i> Trang chủ Barista
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                                            <i class="fa fa-home"></i> Trang chủ
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">
                                    <i class="fa fa-sign-in"></i> Đăng nhập
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </section>
    </div>
    
    <!-- Include Footer only if user is logged in -->
    <c:if test="${not empty sessionScope.user}">
        <!-- Include Footer -->
        <%@include file="../compoment/footer.jsp" %>
    </c:if>

</div>

<!-- Bootstrap -->
<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
<!-- AdminLTE App -->
<script src="${pageContext.request.contextPath}/dist/js/app.min.js"></script>

<script>
    // Auto-redirect after 30 seconds if no user interaction
    setTimeout(function() {
        if (confirm('Bạn có muốn quay lại trang trước đó không?')) {
            history.back();
        }
    }, 30000);
</script>

</body>
</html>
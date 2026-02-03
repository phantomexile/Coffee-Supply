<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>Chi tiết người dùng - Coffee Shop</title>
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
            .profile-user-img {
                border: 3px solid #adb5bd;
                margin: 0 auto;
                padding: 3px;
                width: 100px;
                height: 100px;
            }

            .label-lg {
                font-size: 14px;
                padding: 8px 12px;
            }

            .box-profile .profile-username {
                font-size: 25px;
                font-weight: 300;
                margin: 10px 0 5px;
            }

            .list-group-item {
                border: 1px solid #f4f4f4;
                padding: 10px 15px;
            }

            .list-group-item b {
                color: #666;
            }

            .well {
                background-color: #f9f9f9;
                border: 1px solid #e3e3e3;
                border-radius: 4px;
                box-shadow: inset 0 1px 1px rgba(0,0,0,.05);
                margin-bottom: 20px;
                min-height: 20px;
                padding: 19px;
            }

            .well-sm {
                padding: 9px;
                border-radius: 3px;
            }

            .btn-lg {
                padding: 10px 20px;
                font-size: 16px;
                margin-right: 10px;
            }

            .margin-r-5 {
                margin-right: 5px;
            }

            .text-bold {
                font-weight: 700;
            }

            .table th {
                background-color: #f7f7f7;
                font-weight: 600;
                color: #555;
            }

            .box-header .box-title {
                font-size: 18px;
                font-weight: 600;
            }

            .box-profile {
                display: flex;
                flex-direction: column;
                justify-content: space-between;
                min-height: 500px; /* tùy ý */
            }

            .profile-user-img {
                border: 3px solid #adb5bd;
                margin: 0 auto;
                padding: 3px;
                width: 160px;      /* Tăng từ 100px → 150px */
                height: 160px;
                object-fit: cover; /* Giúp ảnh không bị méo */
            }

            .box-profile .profile-username {
                font-size: 30px;   /* Tăng từ 25px → 30px */
                font-weight: 400;  /* Đậm hơn một chút */
                margin: 15px 0 8px;
            }

            .label-lg {
                font-size: 16px;   /* Tăng từ 14px → 16px */
                padding: 18px 18px;
                display: inline-block;
            }

            .text-muted.text-center {
                margin-top: 10px;
            }


        </style>
    </head>
    <body class="hold-transition skin-blue sidebar-mini">
        <div class="wrapper">

            <!-- Include Header -->
            <%@include file="../compoment/header.jsp" %>

            <!-- Include Sidebar -->
            <%@include file="../compoment/sidebar.jsp" %>

            <!-- Content Wrapper. Contains page content -->
            <div class="content-wrapper">
                <!-- Content Header (Page header) -->
                <section class="content-header">
                    <h1>
                        Chi tiết người dùng
                        <small>Thông tin chi tiết tài khoản</small>
                    </h1>
                    <ol class="breadcrumb">
                        <li><a href="${pageContext.request.contextPath}/hr/dashboard"><i class="fa fa-dashboard"></i> Dashboard</a></li>
                        <li><a href="${pageContext.request.contextPath}/user">Quản lý người dùng</a></li>
                        <li class="active">Chi tiết</li>
                    </ol>
                </section>

                <!-- Main content -->
                <section class="content">
                    <!-- Error Message -->
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible">
                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                            <h4><i class="icon fa fa-ban"></i> Lỗi!</h4>
                            ${error}
                        </div>
                    </c:if>

                    <!-- User Profile -->
                    <c:if test="${not empty user}">
                        <div class="row">
                            <!-- Profile Overview -->
                            <div class="col-md-4">
                                <div class="box box-primary">
                                    <div class="box-body box-profile">
                                        <img class="profile-user-img img-responsive img-circle" 
                                             src="${pageContext.request.contextPath}/dist/img/user4-128x128.jpg" 
                                             alt="User profile picture">

                                        <h3 class="profile-username text-center">${user.fullName}</h3>

                                        <p class="text-muted text-center">
                                            <c:choose>
                                                <c:when test="${user.roleID == 1}">
                                                    <span class="label label-success label-lg">HR - Nhân sự</span>
                                                </c:when>
                                                <c:when test="${user.roleID == 2}">
                                                    <span class="label label-danger label-lg">Admin - Quản trị viên</span>
                                                </c:when>
                                                <c:when test="${user.roleID == 3}">
                                                    <span class="label label-warning label-lg">Inventory - Quản lý kho</span>
                                                </c:when>
                                                <c:when test="${user.roleID == 4}">
                                                    <span class="label label-info label-lg">Barista - Pha chế</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="label label-default label-lg">User - Xem thông tin shop</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </p>


                                    </div>
                                </div>
                            </div>

                            <!-- Detailed Information -->
                            <div class="col-md-8">
                                <!-- Basic Information -->
                                <div class="box box-info">
                                    <div class="box-header with-border">
                                        <h3 class="box-title"><i class="fa fa-info-circle"></i> Thông tin chi tiết</h3>
                                    </div>
                                    <div class="box-body">
                                        <div class="row">
                                            <div class="col-md-6">
                                                <table class="table table-bordered">
                                                    <tr>
                                                        <th><i class="fa fa-envelope"></i> ID:</th>
                                                        <td>${user.userID}</td>
                                                    </tr>
                                                    <tr>
                                                        <th width="40%"><i class="fa fa-user"></i> Họ và tên:</th>
                                                        <td><strong>${user.fullName}</strong></td>
                                                    </tr>
                                                    <tr>
                                                        <th><i class="fa fa-envelope"></i> Giới tính:</th>
                                                        <td>${user.gender}</td>
                                                    </tr>
                                                    <tr>
                                                        <th><i class="fa fa-envelope"></i> Email:</th>
                                                        <td>${user.email}</td>
                                                    </tr>
                                                    <tr>
                                                        <th><i class="fa fa-phone"></i> Số điện thoại:</th>
                                                        <td>${not empty user.phone ? user.phone : '<em class="text-muted">Chưa cập nhật</em>'}</td>
                                                    </tr>
                                                </table>
                                            </div>
                                            <div class="col-md-6">
                                                <table class="table table-bordered">
                                                    <tr>
                                                        <th width="40%"><i class="fa fa-users"></i> Vai trò:</th>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${user.roleID == 1}">
                                                                    <span class="label label-success">HR - Nhân sự</span>
                                                                </c:when>
                                                                <c:when test="${user.roleID == 2}">
                                                                    <span class="label label-danger">Admin - Quản trị viên</span>
                                                                </c:when>
                                                                <c:when test="${user.roleID == 3}">
                                                                    <span class="label label-warning">Inventory - Quản lý kho</span>
                                                                </c:when>
                                                                <c:when test="${user.roleID == 4}">
                                                                    <span class="label label-info">Barista - Pha chế</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="label label-default">Unknown</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th><i class="fa fa-calendar"></i> Ngày tạo:</th>
                                                        <td><fmt:formatDate value="${user.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                    </tr>
                                                    <tr>
                                                        <th><i class="fa fa-toggle-on"></i> Trạng thái:</th>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${user.active}">
                                                                    <span class="label label-success">Hoạt động</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="label label-danger">Ngưng hoạt động</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="col-md-12">
                                                <h4><i class="fa fa-map-marker"></i> Địa chỉ liên hệ</h4>
                                                <div class="well well-sm">
                                                    ${not empty user.address ? user.address : '<em class="text-muted">Chưa cập nhật địa chỉ</em>'}
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!-- Action Buttons -->
                                <div class="box box-success">
                                    <div class="box-header with-border">
                                        <h3 class="box-title"><i class="fa fa-cogs"></i> Thao tác</h3>
                                    </div>
                                    <div class="box-body">
                                        <div class="row">
                                            <div class="col-md-12">
                                                <a href="${pageContext.request.contextPath}/user?action=edit&id=${user.userID}" 
                                                   class="btn btn-primary btn-lg">
                                                    <i class="fa fa-edit"></i> Chỉnh sửa thông tin
                                                </a>
                                                <a href="${pageContext.request.contextPath}/user?action=changePassword&id=${user.userID}" 
                                                   class="btn btn-warning btn-lg">
                                                    <i class="fa fa-key"></i> Đổi mật khẩu
                                                </a>
                                                <a href="${pageContext.request.contextPath}/user" 
                                                   class="btn btn-default btn-lg">
                                                    <i class="fa fa-arrow-left"></i> Quay lại danh sách
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                </div>
            </div>
        </div>
    </div>
</c:if>
</section>
<!-- /.content -->
</div>
<!-- /.content-wrapper -->

<!-- Delete Confirmation Modal -->
<div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="deleteModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="deleteModalLabel">Xác nhận xóa tài khoản</h4>
            </div>
            <div class="modal-body">
                <div class="text-center">
                    <i class="fa fa-exclamation-triangle text-yellow fa-3x"></i>
                    <h4>Cảnh báo!</h4>
                    <p>Bạn có chắc chắn muốn xóa tài khoản của <strong id="deleteUserName"></strong>?</p>
                    <p class="text-muted"><small>Thao tác này sẽ vô hiệu hóa tài khoản và không thể hoàn tác.</small></p>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">
                    <i class="fa fa-times"></i> Hủy
                </button>
                <form method="POST" action="${pageContext.request.contextPath}/user" style="display: inline;">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="userId" id="deleteUserId">
                    <button type="submit" class="btn btn-danger">
                        <i class="fa fa-trash"></i> Xóa tài khoản
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

</div>
<!-- ./wrapper -->

<!-- jQuery 2.2.3 -->
<script src="${pageContext.request.contextPath}/plugins/jQuery/jquery-2.2.3.min.js"></script>
<!-- Bootstrap 3.3.6 -->
<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
<!-- AdminLTE App -->
<script src="${pageContext.request.contextPath}/dist/js/app.min.js"></script>

<script>
    function confirmDelete(userId, userName) {
        document.getElementById('deleteUserId').value = userId;
        document.getElementById('deleteUserName').textContent = userName;
        $('#deleteModal').modal('show');
    }

    // Auto-hide alerts after 5 seconds
    setTimeout(function () {
        $('.alert').fadeOut();
    }, 5000);
</script>
</body>
</html>
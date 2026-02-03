<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Profile - Coffee Shop Management</title>
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    
    <!-- Bootstrap CSS từ CDN -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <!-- AdminLTE CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/AdminLTE.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/skins/_all-skins.min.css">
    
    <!-- jQuery từ CDN -->
    <script src="https://code.jquery.com/jquery-2.2.4.min.js" integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44=" crossorigin="anonymous"></script>
    
    <style>
        .profile-card {
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .profile-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 30px;
            text-align: center;
        }
        .profile-img {
            width: 120px;
            height: 120px;
            border-radius: 50%;
            border: 5px solid white;
            margin-bottom: 15px;
        }
        .profile-info {
            padding: 30px;
        }
        .info-item {
            display: flex;
            justify-content: space-between;
            padding: 15px 0;
            border-bottom: 1px solid #eee;
        }
        .info-label {
            font-weight: bold;
            color: #666;
        }
        .info-value {
            color: #333;
        }
        .action-buttons {
            padding: 20px;
            background: #f9f9f9;
            text-align: center;
        }
        .btn-custom {
            margin: 0 5px;
        }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
       <jsp:include page="../compoment/header.jsp" />

    <jsp:include page="../compoment/sidebar.jsp" />

    <!-- Content Wrapper -->
    <div class="content-wrapper">
        <section class="content-header">
            <h1>
                Thông tin cá nhân
                <small>Xem và quản lý thông tin của bạn</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${pageContext.request.contextPath}/"><i class="fa fa-dashboard"></i> Home</a></li>
                <li class="active">Profile</li>
            </ol>
        </section>

        <section class="content">
            <!-- Alert messages -->
            <c:if test="${not empty success}">
                <div class="alert alert-success alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                    <h4><i class="icon fa fa-check"></i> Thành công!</h4>
                    ${success}
                </div>
            </c:if>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                    <h4><i class="icon fa fa-ban"></i> Lỗi!</h4>
                    ${error}
                </div>
            </c:if>
            
            <c:if test="${not empty warning}">
                <div class="alert alert-warning alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                    <h4><i class="icon fa fa-warning"></i> Cảnh báo!</h4>
                    ${warning}
                </div>
            </c:if>

            <div class="row">
                <div class="col-md-8 col-md-offset-2">
                    <div class="box profile-card">
                        <!-- Profile Header -->
                        <div class="profile-header">
                            <c:choose>
                                <c:when test="${not empty profileUser.avatarUrl}">
                                    <img src="${pageContext.request.contextPath}${profileUser.avatarUrl}" 
                                         alt="Profile Picture" class="profile-img"
                                         onerror="if(!this.dataset.error){this.dataset.error='1';this.src='https://via.placeholder.com/120x120/ffffff/667eea?text=${profileUser.fullName.substring(0,1)}';}">
                                </c:when>
                                <c:otherwise>
                                    <img src="https://via.placeholder.com/120x120/ffffff/667eea?text=${profileUser.fullName.substring(0,1)}" 
                                         alt="Profile Picture" class="profile-img">
                                </c:otherwise>
                            </c:choose>
                            <h3>${profileUser.fullName}</h3>
                            <p><i class="fa fa-envelope"></i> ${profileUser.email}</p>
                            <p><i class="fa fa-user-tag"></i> ${sessionScope.roleName}</p>
                        </div>

                        <!-- Profile Information -->
                        <div class="profile-info">
                            <div class="info-item">
                                <span class="info-label"><i class="fa fa-user"></i> Họ và tên:</span>
                                <span class="info-value">${profileUser.fullName}</span>
                            </div>
                            
                            <div class="info-item">
                                <span class="info-label"><i class="fa fa-envelope"></i> Email:</span>
                                <span class="info-value">${profileUser.email}</span>
                            </div>
                            
                            <div class="info-item">
                                <span class="info-label"><i class="fa fa-phone"></i> Số điện thoại:</span>
                                <span class="info-value">${not empty profileUser.phone ? profileUser.phone : 'Chưa cập nhật'}</span>
                            </div>
                            
                            <div class="info-item">
                                <span class="info-label"><i class="fa fa-map-marker"></i> Địa chỉ:</span>
                                <span class="info-value">${not empty profileUser.address ? profileUser.address : 'Chưa cập nhật'}</span>
                            </div>
                            
                            <div class="info-item">
                                <span class="info-label"><i class="fa fa-venus-mars"></i> Giới tính:</span>
                                <span class="info-value">
                                    <c:choose>
                                        <c:when test="${profileUser.gender == 'Nam'}">
                                            <i class="fa fa-mars text-primary"></i> Nam
                                        </c:when>
                                        <c:when test="${profileUser.gender == 'Nữ'}">
                                            <i class="fa fa-venus text-danger"></i> Nữ
                                        </c:when>
                                        <c:otherwise>
                                            Chưa cập nhật
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </div>
                            
                            <div class="info-item">
                                <span class="info-label"><i class="fa fa-user-tag"></i> Vai trò:</span>
                                <span class="info-value">
                                    <span class="label label-primary">${sessionScope.roleName}</span>
                                </span>
                            </div>
                            
                            <div class="info-item">
                                <span class="info-label"><i class="fa fa-check-circle"></i> Trạng thái:</span>
                                <span class="info-value">
                                    <c:choose>
                                        <c:when test="${profileUser.active}">
                                            <span class="label label-success">Hoạt động</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="label label-danger">Không hoạt động</span>
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </div>
                            
                            <div class="info-item" style="border-bottom: none;">
                                <span class="info-label"><i class="fa fa-calendar"></i> Ngày tạo:</span>
                                <span class="info-value">
                                    <fmt:formatDate value="${profileUser.createdAt}" pattern="dd/MM/yyyy HH:mm" />
                                </span>
                            </div>
                        </div>

                        <!-- Action Buttons -->
                        <div class="action-buttons">
                            <a href="${pageContext.request.contextPath}/profile?action=edit" class="btn btn-primary btn-custom">
                                <i class="fa fa-edit"></i> Chỉnh sửa thông tin
                            </a>
                            <a href="${pageContext.request.contextPath}/profile?action=change-password" class="btn btn-warning btn-custom">
                                <i class="fa fa-key"></i> Đổi mật khẩu
                            </a>
                            <a href="javascript:history.back()" class="btn btn-default btn-custom">
                                <i class="fa fa-arrow-left"></i> Quay lại
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>

<!-- Bootstrap JS từ CDN -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>

<script>
<script>
// Auto hide alerts after 5 seconds
setTimeout(function() {
    $('.alert').fadeOut('slow');
}, 5000);
</script>

</body>
</html>
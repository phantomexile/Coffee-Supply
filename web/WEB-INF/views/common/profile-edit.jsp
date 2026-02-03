<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Chỉnh sửa Profile - Coffee Shop Management</title>
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
        .form-group label {
            font-weight: bold;
        }
        .required {
            color: red;
        }
        .profile-form {
            background: white;
            padding: 30px;
            border-radius: 5px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        /* Avatar Upload Styles */
        .avatar-upload-section {
            display: flex;
            align-items: center;
            gap: 20px;
            padding: 15px;
            border: 2px dashed #ddd;
            border-radius: 8px;
            background-color: #f9f9f9;
        }
        
        .current-avatar {
            flex-shrink: 0;
        }
        
        .avatar-preview {
            width: 120px;
            height: 120px;
            border-radius: 50%;
            object-fit: cover;
            border: 3px solid #fff;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        
        .avatar-upload-controls {
            display: flex;
            flex-direction: column;
            gap: 10px;
        }
        
        .avatar-info {
            flex: 1;
            display: flex;
            align-items: center;
        }
        
        .avatar-upload-controls .btn {
            min-width: 140px;
        }
        
        @media (max-width: 768px) {
            .avatar-upload-section {
                flex-direction: column;
                text-align: center;
            }
            
            .avatar-upload-controls {
                flex-direction: row;
                justify-content: center;
            }
        }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <!-- Main Header -->
    <header class="main-header">
        <a href="${pageContext.request.contextPath}/" class="logo">
            <span class="logo-mini"><b>CS</b></span>
            <span class="logo-lg"><b>Coffee</b>Shop</span>
        </a>
        <nav class="navbar navbar-static-top">
            <a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button">
                <span class="sr-only">Toggle navigation</span>
            </a>
            <div class="navbar-custom-menu">
                <ul class="nav navbar-nav">
                    <li class="dropdown user user-menu">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                            <c:choose>
                                <c:when test="${not empty sessionScope.user.avatarUrl}">
                                    <img src="${pageContext.request.contextPath}${sessionScope.user.avatarUrl}" class="user-image" alt="User Image">
                                </c:when>
                                <c:otherwise>
                                    <img src="https://via.placeholder.com/160x160/00a65a/ffffff/png?text=${sessionScope.user.fullName.substring(0,1)}" class="user-image" alt="User Image">
                                </c:otherwise>
                            </c:choose>
                            <span class="hidden-xs">${sessionScope.user.fullName}</span>
                        </a>
                        <ul class="dropdown-menu">
                            <li class="user-header">
                                <c:choose>
                                    <c:when test="${not empty sessionScope.user.avatarUrl}">
                                        <img src="${pageContext.request.contextPath}${sessionScope.user.avatarUrl}" class="img-circle" alt="User Image">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="https://via.placeholder.com/160x160/00a65a/ffffff/png?text=${sessionScope.user.fullName.substring(0,1)}" class="img-circle" alt="User Image">
                                    </c:otherwise>
                                </c:choose>
                                <p>
                                    ${sessionScope.user.fullName}
                                    <small>Thành viên từ ${sessionScope.user.createdAt != null ? sessionScope.user.createdAt : 'N/A'}</small>
                                </p>
                            </li>
                            <li class="user-footer">
                                <div class="pull-left">
                                    <a href="${pageContext.request.contextPath}/profile" class="btn btn-default btn-flat">Profile</a>
                                </div>
                                <div class="pull-right">
                                    <a href="${pageContext.request.contextPath}/login?action=logout" class="btn btn-default btn-flat">Đăng xuất</a>
                                </div>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
        </nav>
    </header>

    <!-- Include Sidebar -->
    <jsp:include page="../compoment/sidebar.jsp" />

    <!-- Content Wrapper -->
    <div class="content-wrapper">
        <section class="content-header">
            <h1>
                Chỉnh sửa thông tin cá nhân
                <small>Cập nhật thông tin của bạn</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${pageContext.request.contextPath}/"><i class="fa fa-dashboard"></i> Home</a></li>
                <li><a href="${pageContext.request.contextPath}/profile">Profile</a></li>
                <li class="active">Chỉnh sửa</li>
            </ol>
        </section>

        <section class="content">
            <!-- Alert messages -->
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
                    <div class="profile-form">
                        <form action="${pageContext.request.contextPath}/profile" method="post" id="profileForm" enctype="multipart/form-data">
                            <input type="hidden" name="action" value="update-profile">
                            
                            <div class="row">
                                <div class="col-md-12">
                                    <h3><i class="fa fa-user"></i> Thông tin cá nhân</h3>
                                    <hr>
                                </div>
                            </div>
                            
                            <!-- Avatar Upload Section -->
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <label>Ảnh đại diện</label>
                                        <div class="avatar-upload-section">
                                            <div class="current-avatar">
                                                <c:choose>
                                                    <c:when test="${not empty profileUser.avatarUrl}">
                                                        <img src="${pageContext.request.contextPath}${profileUser.avatarUrl}" 
                                                             alt="Current Avatar" class="avatar-preview" id="currentAvatar">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img src="https://via.placeholder.com/150x150/00a65a/ffffff/png?text=${profileUser.fullName.substring(0,1)}" 
                                                             alt="Default Avatar" class="avatar-preview" id="currentAvatar">
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div class="avatar-upload-controls">
                                                <input type="file" class="form-control" id="avatarFile" name="avatarFile" 
                                                       accept="image/*" style="display: none;">
                                                <button type="button" class="btn btn-info btn-sm" onclick="document.getElementById('avatarFile').click();">
                                                    <i class="fa fa-upload"></i> Chọn ảnh mới
                                                </button>                                         
                                            </div>
                                            <div class="avatar-info">
                                                <small class="help-block text-muted">
                                                    <i class="fa fa-info-circle"></i> 
                                                    Chọn ảnh JPG, PNG hoặc GIF. Kích thước tối đa 2MB.
                                                </small>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            
                     
                            
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="fullName">Họ và tên <span class="required">*</span></label>
                                        <input type="text" class="form-control" id="fullName" name="fullName" 
                                               value="${profileUser.fullName}" required maxlength="100">
                                        <small class="help-block">Nhập họ và tên đầy đủ của bạn</small>
                                    </div>
                                </div>
                                
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="email">Email <span class="required">*</span></label>
                                        <input type="email" class="form-control" id="email" name="email" 
                                               value="${profileUser.email}" readonly>
                                        <small class="help-block">Email sẽ được sử dụng để đăng nhập</small>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                         <label for="phone">Số điện thoại</label>
                         <!--
                          Requirements:
                          - Must start with '0'
                          - Maximum 11 digits
                          - Digits only (no letters)
                         -->
                         <input type="tel" class="form-control" id="phone" name="phone"
                             value="${profileUser.phone}" maxlength="11"
                             pattern="^0[0-9]{1,10}$"
                             inputmode="numeric"
                             oninput="this.value = this.value.replace(/[^0-9]/g, '').slice(0,11);"
                             title="Bắt đầu bằng 0, tối đa 11 chữ số, chỉ nhập chữ số">
                         <small class="help-block">Nhập số điện thoại liên hệ — bắt đầu bằng 0, tối đa 11 chữ số</small>
                                    </div>
                                </div>
                                
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Vai trò hiện tại</label>
                                        <input type="text" class="form-control" value="${sessionScope.roleName}" readonly>
                                        <small class="help-block">Vai trò không thể thay đổi</small>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <label for="address">Địa chỉ</label>
                                        <textarea class="form-control" id="address" name="address" 
                                                  rows="3" maxlength="200" placeholder="Nhập địa chỉ của bạn">${profileUser.address}</textarea>
                                        <small class="help-block">Nhập địa chỉ nơi ở hiện tại</small>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                                <label>Giới tính <span class="text-danger">*</span></label>
                                                <div class="form-group" role="radiogroup" aria-labelledby="genderLabel">
                                                    <label class="radio-inline">
                                                        <input type="radio" id="gender_male" name="gender" value="Nam" required
                                                            ${profileUser.gender == 'Nam' ? 'checked' : ''}> Nam
                                                    </label>
                                                    <label class="radio-inline">
                                                        <input type="radio" id="gender_female" name="gender" value="Nữ"
                                                            ${profileUser.gender == 'Nữ' ? 'checked' : ''}> Nữ
                                                    </label>
                                                </div>
                                                <small class="help-block">Chọn giới tính của bạn</small>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-12">
                                    <hr>
                                    <div class="form-group text-center">
                                        <button type="submit" class="btn btn-primary btn-lg">
                                            <i class="fa fa-save"></i> Lưu thay đổi
                                        </button>
                                        <a href="${pageContext.request.contextPath}/profile" class="btn btn-default btn-lg">
                                            <i class="fa fa-times"></i> Hủy bỏ
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>

<!-- Bootstrap JS từ CDN -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>

<script>
$(document).ready(function() {
    // Auto hide alerts after 5 seconds
    setTimeout(function() {
        $('.alert').fadeOut('slow');
    }, 5000);
    
    // Form validation
    $('#profileForm').on('submit', function(e) {
        var fullName = $('#fullName').val().trim();
        
        if (fullName === '') {
            alert('Vui lòng nhập họ và tên');
            $('#fullName').focus();
            e.preventDefault();
            return false;
        }
    });
    
    // Phone number formatting
    $('#phone').on('input', function() {
        var value = $(this).val();
        // Remove any non-digit characters except +, -, (), space
        value = value.replace(/[^\d+\-\s()]/g, '');
        $(this).val(value);
    });
    
    // Avatar file input change handler
    $('#avatarFile').on('change', function() {
        var file = this.files[0];
        if (file) {
            // Validate file type
            if (!file.type.match('image.*')) {
                alert('Vui lòng chọn file ảnh hợp lệ (JPG, PNG, GIF)');
                $(this).val('');
                return;
            }
            
            // Validate file size (2MB = 2 * 1024 * 1024 bytes)
            if (file.size > 2 * 1024 * 1024) {
                alert('Kích thước file không được vượt quá 2MB');
                $(this).val('');
                return;
            }
            
            // Show preview
            var reader = new FileReader();
            reader.onload = function(e) {
                $('#currentAvatar').attr('src', e.target.result);             
            };
            reader.readAsDataURL(file);
        }
    });
});

// Function to remove avatar
function removeAvatar() {
    if (confirm('Bạn có chắc chắn muốn xóa ảnh đại diện?')) {
        $('#currentAvatar').attr('src', 'https://via.placeholder.com/150x150/00a65a/ffffff/png?text=${profileUser.fullName.substring(0,1)}');
        $('#avatarFile').val('');   
        $('#removeAvatarBtn').hide();
    }
}
</script>

</body>
</html>
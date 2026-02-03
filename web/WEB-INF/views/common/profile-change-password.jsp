<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Đổi mật khẩu - Coffee Shop Management</title>
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
        .password-form {
            background: white;
            padding: 30px;
            border-radius: 5px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .password-strength {
            margin-top: 5px;
        }
        .strength-weak { color: #d9534f; }
        .strength-medium { color: #f0ad4e; }
        .strength-strong { color: #5cb85c; }
        .password-toggle {
            cursor: pointer;
            position: absolute;
            right: 10px;
            top: 50%;
            transform: translateY(-50%);
        }
        .password-wrapper {
            position: relative;
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
                            <img src="https://via.placeholder.com/160x160/00a65a/ffffff/png?text=${sessionScope.user.fullName.substring(0,1)}" class="user-image" alt="User Image">
                            <span class="hidden-xs">${sessionScope.user.fullName}</span>
                        </a>
                        <ul class="dropdown-menu">
                            <li class="user-header">
                                <img src="https://via.placeholder.com/160x160/00a65a/ffffff/png?text=${sessionScope.user.fullName.substring(0,1)}" class="img-circle" alt="User Image">
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
                Đổi mật khẩu
                <small>Thay đổi mật khẩu đăng nhập</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${pageContext.request.contextPath}/"><i class="fa fa-dashboard"></i> Home</a></li>
                <li><a href="${pageContext.request.contextPath}/profile">Profile</a></li>
                <li class="active">Đổi mật khẩu</li>
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

            <div class="row">
                <div class="col-md-6 col-md-offset-3">
                    <div class="password-form">
                        <div class="text-center mb-20">
                            <h3><i class="fa fa-key"></i> Đổi mật khẩu</h3>
                            <p class="text-muted">Vui lòng nhập thông tin để thay đổi mật khẩu</p>
                            <hr>
                        </div>
                        
                        <form action="${pageContext.request.contextPath}/profile" method="post" id="changePasswordForm">
                            <input type="hidden" name="action" value="change-password">
                            
                            <div class="form-group">
                                <label for="currentPassword">Mật khẩu hiện tại <span class="required">*</span></label>
                                <div class="password-wrapper">
                                    <input type="password" class="form-control" id="currentPassword" name="currentPassword" 
                                           required placeholder="Nhập mật khẩu hiện tại">
                                    <i class="fa fa-eye password-toggle" data-target="currentPassword"></i>
                                </div>
                                <small class="help-block">Nhập mật khẩu bạn đang sử dụng</small>
                            </div>
                            
                            <div class="form-group">
                                <label for="newPassword">Mật khẩu mới <span class="required">*</span></label>
                                <div class="password-wrapper">
                                    <input type="password" class="form-control" id="newPassword" name="newPassword" 
                                           required placeholder="Nhập mật khẩu mới" minlength="6">
                                    <i class="fa fa-eye password-toggle" data-target="newPassword"></i>
                                </div>
                                <div class="password-strength" id="passwordStrength"></div>
                                <small class="help-block">Mật khẩu phải có ít nhất 6 ký tự</small>
                            </div>
                            
                            <div class="form-group">
                                <label for="confirmPassword">Xác nhận mật khẩu mới <span class="required">*</span></label>
                                <div class="password-wrapper">
                                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" 
                                           required placeholder="Nhập lại mật khẩu mới">
                                    <i class="fa fa-eye password-toggle" data-target="confirmPassword"></i>
                                </div>
                                <div id="passwordMatch" class="help-block"></div>
                            </div>
                            
                            <div class="form-group text-center">
                                <button type="submit" class="btn btn-warning btn-lg" id="submitBtn">
                                    <i class="fa fa-key"></i> Đổi mật khẩu
                                </button>
                                <a href="${pageContext.request.contextPath}/profile" class="btn btn-default btn-lg">
                                    <i class="fa fa-times"></i> Hủy bỏ
                                </a>
                            </div>
                        </form>
                        
                        <div class="alert alert-info">
                            <h4><i class="fa fa-info-circle"></i> Lưu ý bảo mật:</h4>
                            <ul>
                                <li>Mật khẩu nên có ít nhất 8 ký tự</li>
                                <li>Sử dụng kết hợp chữ hoa, chữ thường, số và ký tự đặc biệt</li>
                                <li>Không sử dụng thông tin cá nhân dễ đoán</li>
                                <li>Thay đổi mật khẩu định kỳ để đảm bảo an toàn</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>

<!-- Bootstrap JS từ CDN (jQuery needed for Bootstrap) -->
<script src="https://code.jquery.com/jquery-2.2.4.min.js" integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44=" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>

<script>
// Vanilla JavaScript - No jQuery dependency for custom logic
document.addEventListener('DOMContentLoaded', function() {
    // Auto hide alerts after 5 seconds
    setTimeout(function() {
        const alerts = document.querySelectorAll('.alert');
        alerts.forEach(function(alert) {
            alert.style.transition = 'opacity 0.5s';
            alert.style.opacity = '0';
            setTimeout(function() {
                alert.style.display = 'none';
            }, 500);
        });
    }, 5000);
    
    // Password toggle visibility
    const passwordToggles = document.querySelectorAll('.password-toggle');
    passwordToggles.forEach(function(toggle) {
        toggle.addEventListener('click', function() {
            const target = this.getAttribute('data-target');
            const input = document.getElementById(target);
            const icon = this;
            
            if (input.getAttribute('type') === 'password') {
                input.setAttribute('type', 'text');
                icon.classList.remove('fa-eye');
                icon.classList.add('fa-eye-slash');
            } else {
                input.setAttribute('type', 'password');
                icon.classList.remove('fa-eye-slash');
                icon.classList.add('fa-eye');
            }
        });
    });
    
    // Password strength checker
    const newPasswordField = document.getElementById('newPassword');
    const passwordStrengthElement = document.getElementById('passwordStrength');
    
    newPasswordField.addEventListener('input', function() {
        const password = this.value;
        const strength = checkPasswordStrength(password);
        
        if (password.length === 0) {
            passwordStrengthElement.innerHTML = '';
            return;
        }
        
        if (strength.score < 2) {
            passwordStrengthElement.innerHTML = '<span class="strength-weak">Yếu: ' + strength.feedback + '</span>';
        } else if (strength.score < 4) {
            passwordStrengthElement.innerHTML = '<span class="strength-medium">Trung bình: ' + strength.feedback + '</span>';
        } else {
            passwordStrengthElement.innerHTML = '<span class="strength-strong">Mạnh: Mật khẩu tốt!</span>';
        }
    });
    
    // Password match checker
    const confirmPasswordField = document.getElementById('confirmPassword');
    const passwordMatchElement = document.getElementById('passwordMatch');
    
    confirmPasswordField.addEventListener('input', function() {
        const newPassword = newPasswordField.value;
        const confirmPassword = this.value;
        
        if (confirmPassword.length === 0) {
            passwordMatchElement.innerHTML = '';
            return;
        }
        
        if (newPassword === confirmPassword) {
            passwordMatchElement.innerHTML = '<span style="color: green;"><i class="fa fa-check"></i> Mật khẩu khớp</span>';
        } else {
            passwordMatchElement.innerHTML = '<span style="color: red;"><i class="fa fa-times"></i> Mật khẩu không khớp</span>';
        }
    });
    
    // Form validation
    const changePasswordForm = document.getElementById('changePasswordForm');
    const currentPasswordField = document.getElementById('currentPassword');
    
    changePasswordForm.addEventListener('submit', function(e) {
        const currentPassword = currentPasswordField.value;
        const newPassword = newPasswordField.value;
        const confirmPassword = confirmPasswordField.value;
        
        if (currentPassword === '') {
            alert('Vui lòng nhập mật khẩu hiện tại');
            currentPasswordField.focus();
            e.preventDefault();
            return false;
        }
        
        if (newPassword === '') {
            alert('Vui lòng nhập mật khẩu mới');
            newPasswordField.focus();
            e.preventDefault();
            return false;
        }
        
        if (newPassword.length < 6) {
            alert('Mật khẩu mới phải có ít nhất 6 ký tự');
            newPasswordField.focus();
            e.preventDefault();
            return false;
        }
        
        if (newPassword !== confirmPassword) {
            alert('Xác nhận mật khẩu không khớp');
            confirmPasswordField.focus();
            e.preventDefault();
            return false;
        }
        
        if (currentPassword === newPassword) {
            alert('Mật khẩu mới phải khác mật khẩu hiện tại');
            newPasswordField.focus();
            e.preventDefault();
            return false;
        }
        
        return true;
    });
    
    function checkPasswordStrength(password) {
        var score = 0;
        var feedback = [];
        
        if (password.length >= 8) score++;
        else feedback.push('ít nhất 8 ký tự');
        
        if (/[a-z]/.test(password)) score++;
        else feedback.push('chữ thường');
        
        if (/[A-Z]/.test(password)) score++;
        else feedback.push('chữ hoa');
        
        if (/[0-9]/.test(password)) score++;
        else feedback.push('số');
        
        if (/[^A-Za-z0-9]/.test(password)) score++;
        else feedback.push('ký tự đặc biệt');
        
        return {
            score: score,
            feedback: feedback.length > 0 ? 'Thiếu: ' + feedback.join(', ') : 'Đầy đủ'
        };
    }
});
</script>

</body>
</html>
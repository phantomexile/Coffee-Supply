<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Đăng Nhập - CoffeeLux</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <style>
            body {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                min-height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            }

            .login-container {
                background: white;
                border-radius: 20px;
                box-shadow: 0 20px 40px rgba(0,0,0,0.1);
                padding: 40px;
                width: 100%;
                max-width: 450px;
                position: relative;
                overflow: hidden;
            }

            .login-container::before {
                content: '';
                position: absolute;
                top: 0;
                left: 0;
                right: 0;
                height: 4px;
                background: linear-gradient(90deg, #667eea, #764ba2);
            }

            .avatar-container {
                text-align: center;
                margin-bottom: 30px;
            }

            .avatar {
                width: 80px;
                height: 80px;
                background: linear-gradient(135deg, #8B4513, #D2691E);
                border-radius: 50%;
                display: inline-flex;
                align-items: center;
                justify-content: center;
                margin-bottom: 20px;
                box-shadow: 0 10px 20px rgba(0,0,0,0.1);
            }

            .avatar i {
                font-size: 32px;
                color: white;
            }

            .logo {
                display: flex;
                align-items: center;
                justify-content: center;
                margin-bottom: 20px;
            }

            .logo i {
                font-size: 28px;
                color: #667eea;
                margin-right: 10px;
            }

            .logo-text {
                font-size: 28px;
                font-weight: bold;
                color: #667eea;
                margin: 0;
            }

            .login-title {
                font-size: 24px;
                font-weight: 600;
                color: #333;
                text-align: center;
                margin-bottom: 10px;
            }

            .login-subtitle {
                font-size: 14px;
                color: #666;
                text-align: center;
                margin-bottom: 30px;
            }

            .form-group {
                margin-bottom: 20px;
                position: relative;
            }

            .form-label {
                font-weight: 500;
                color: #333;
                margin-bottom: 8px;
                display: flex;
                align-items: center;
            }

            .form-label i {
                margin-right: 8px;
                color: #667eea;
                width: 16px;
            }

            .form-control {
                border: 2px solid #e1e5e9;
                border-radius: 12px;
                padding: 12px 16px;
                font-size: 16px;
                transition: all 0.3s ease;
                background: #f8f9fa;
            }

            .form-control:focus {
                border-color: #667eea;
                box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
                background: white;
            }

            .password-toggle {
                position: absolute;
                right: 15px;
                top: 50%;
                transform: translateY(-50%);
                background: none;
                border: none;
                color: #666;
                cursor: pointer;
                font-size: 16px;
            }

            .password-toggle:hover {
                color: #667eea;
            }

            .form-check {
                margin: 20px 0;
            }

            .form-check-input {
                width: 18px;
                height: 18px;
                border: 2px solid #e1e5e9;
                border-radius: 4px;
            }

            .form-check-input:checked {
                background-color: #667eea;
                border-color: #667eea;
            }

            .form-check-label {
                color: #666;
                font-size: 14px;
                margin-left: 8px;
            }

            .btn-login {
                background: linear-gradient(135deg, #667eea, #764ba2);
                border: none;
                border-radius: 12px;
                padding: 14px 30px;
                font-size: 16px;
                font-weight: 600;
                color: white;
                width: 100%;
                transition: all 0.3s ease;
                display: flex;
                align-items: center;
                justify-content: center;
                gap: 10px;
            }

            .btn-login:hover {
                transform: translateY(-2px);
                box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
            }

            .btn-login:active {
                transform: translateY(0);
            }

            .forgot-password {
                text-align: center;
                margin-top: 20px;
            }

            .forgot-password a {
                color: #667eea;
                text-decoration: none;
                font-size: 14px;
                display: inline-flex;
                align-items: center;
                gap: 5px;
                transition: color 0.3s ease;
            }

            .forgot-password a:hover {
                color: #764ba2;
            }

            .alert {
                border-radius: 12px;
                border: none;
                margin-bottom: 20px;
            }

            .alert-danger {
                background: #fee;
                color: #c33;
            }

            .alert-success {
                background: #efe;
                color: #363;
            }

            @media (max-width: 480px) {
                .login-container {
                    margin: 20px;
                    padding: 30px 20px;
                }
            }

            /* Inline Error Alert - Like in image */
            .inline-error-alert {
                background: #fee;
                border: 1px solid #fcc;
                border-radius: 8px;
                padding: 12px 16px;
                margin-bottom: 20px;
                display: flex;
                align-items: center;
                color: #d32f2f;
                font-size: 14px;
                font-weight: 500;
            }

            .inline-error-alert i {
                color: #d32f2f;
                margin-right: 8px;
                font-size: 16px;
            }

            /* Hide reCAPTCHA elements */
            .g-recaptcha {
                display: none !important;
            }

            .grecaptcha-badge {
                visibility: hidden !important;
                opacity: 0 !important;
            }

            iframe[src*="recaptcha"] {
                display: none !important;
            }

            div[class*="recaptcha"] {
                display: none !important;
            }
        </style>
    </head>
    <body>
        <div class="login-container">
            <!-- Avatar -->
            <div class="avatar-container">
                <div class="avatar">
                    <i class="fas fa-user"></i>
                </div>
            </div>

            <!-- Logo -->
            <div class="logo">
                <i class="fas fa-coffee"></i>
                <h1 class="logo-text">CoffeeLux</h1>
            </div>

            <!-- Title -->
            <h2 class="login-title">Đăng Nhập Hệ Thống</h2>
            <p class="login-subtitle">Quản lý cửa hàng cà phê chuyên nghiệp</p>

            <!-- Messages -->
            <c:if test="${not empty error}">
                <div class="inline-error-alert">
                    <i class="fas fa-exclamation-triangle"></i>
                    ${error}
                </div>
            </c:if>

            <!-- Success Message -->
            <c:if test="${not empty param.message}">
                <div class="alert alert-success">
                    <i class="fas fa-check-circle"></i>
                    ${param.message}
                </div>
            </c:if>

            <!-- Info Message -->
            <c:if test="${not empty message}">
                <div class="alert alert-info">
                    <i class="fas fa-info-circle"></i>
                    ${message}
                </div>
            </c:if>


            <c:if test="${not empty success}">
                <div class="alert alert-success">
                    <i class="fas fa-check-circle"></i> ${success}
                </div>
            </c:if>

            <!-- Login Form -->
            <form action="${pageContext.request.contextPath}/login" method="post" id="loginForm">
                <div class="form-group">
                    <label for="email" class="form-label">
                        <i class="fas fa-envelope"></i>
                        Email
                    </label>
                    <input type="email" 
                           class="form-control" 
                           id="email" 
                           name="email" 
                           value="${email}" 
                           placeholder="Nhập email của bạn"
                           required>
                </div>

                <div class="form-group">
                    <label for="password" class="form-label">
                        <i class="fas fa-lock"></i>
                        Mật khẩu
                    </label>
                    <div class="position-relative">
                        <input type="password" 
                               class="form-control" 
                               id="password" 
                               name="password" 
                               placeholder="Nhập mật khẩu"
                               required>
                        <button type="button" class="password-toggle" onclick="togglePasswordVisibility()">
                            <i class="fas fa-eye" id="passwordToggleIcon"></i>
                        </button>
                    </div>
                </div>

                <div class="form-check">
                    <input class="form-check-input" 
                           type="checkbox" 
                           id="rememberMe" 
                           name="rememberMe" 
                           value="true">
                    <label class="form-check-label" for="rememberMe">
                        Ghi nhớ đăng nhập
                    </label>
                </div>

                <button type="submit" class="btn btn-login" id="submitButton">
                    <i class="fas fa-arrow-right"></i>
                    ĐĂNG NHẬP
                </button>
            </form>

            <div class="text-center mt-3">
                <button type="button" 
                        class="btn btn-outline-danger btn-lg w-100"
                        onclick="window.location.href='${pageContext.request.contextPath}/google-auth'"
                        style="border-radius: 50px; display: flex; align-items: center; justify-content: center; gap: 10px;">
                    <i class="fab fa-google"></i>
                    Đăng nhập bằng Google
                </button>
            </div>

            <div class="forgot-password">
                <a href="${pageContext.request.contextPath}/forgot-password">
                    <i class="fas fa-question-circle"></i>
                    Quên mật khẩu?
                </a>
            </div>
        </div>

        <!-- Scripts -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
        
        <script>
            // Toggle password visibility
            function togglePasswordVisibility() {
                const passwordInput = document.getElementById('password');
                const toggleIcon = document.getElementById('passwordToggleIcon');

                if (passwordInput.type === 'password') {
                    passwordInput.type = 'text';
                    toggleIcon.classList.remove('fa-eye');
                    toggleIcon.classList.add('fa-eye-slash');
                } else {
                    passwordInput.type = 'password';
                    toggleIcon.classList.remove('fa-eye-slash');
                    toggleIcon.classList.add('fa-eye');
                }
            }

            // Show forgot password modal
            function showForgotPassword() {
                alert('Tính năng quên mật khẩu đang được phát triển. Vui lòng liên hệ quản trị viên.');
            }

            // Form validation - ONLY for regular login form, NOT Google login
            document.getElementById('loginForm').addEventListener('submit', function (e) {
                const email = document.getElementById('email').value;
                const password = document.getElementById('password').value;

                if (!email || !password) {
                    e.preventDefault();
                    alert('Vui lòng nhập đầy đủ thông tin đăng nhập.');
                    return;
                }

                // Email validation
                const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                if (!emailRegex.test(email)) {
                    e.preventDefault();
                    alert('Vui lòng nhập địa chỉ email hợp lệ.');
                    return;
                }

                // Skip reCAPTCHA check since it's hidden
                // const recaptchaResponse = grecaptcha.getResponse();
                // if (!recaptchaResponse) {
                //     e.preventDefault();
                //     // Create inline error alert for reCAPTCHA error
                //     const titleElement = document.querySelector('.login-title');
                //     const existingError = document.querySelector('.inline-error-alert');
                //     if (existingError) {
                //         existingError.remove();
                //     }
                //     
                //     const errorDiv = document.createElement('div');
                //     errorDiv.className = 'inline-error-alert';
                //     errorDiv.innerHTML = '<i class="fas fa-exclamation-triangle"></i>Vui lòng xác nhận reCAPTCHA.';
                //     titleElement.parentNode.insertBefore(errorDiv, titleElement.nextElementSibling);
                //     return;
                // }
            });                            
            
            // Auto-focus on email field
            document.addEventListener('DOMContentLoaded', function () {
                document.getElementById('email').focus();
                
                // Handle Google OAuth error messages
                const urlParams = new URLSearchParams(window.location.search);
                const error = urlParams.get('error');
                
                if (error) {
                    let errorMessage = '';
                    switch (error) {
                        case 'account_not_found':
                            errorMessage = 'Tài khoản Google này chưa được đăng ký trong hệ thống. Vui lòng liên hệ quản trị viên.';
                            break;
                        case 'account_inactive':
                            errorMessage = 'Tài khoản của bạn đã bị vô hiệu hóa. Vui lòng liên hệ quản trị viên.';
                            break;
                        case 'oauth_denied':
                            errorMessage = 'Bạn đã từ chối quyền truy cập Google. Vui lòng thử lại.';
                            break;
                        case 'missing_auth_code':
                            errorMessage = 'Lỗi xác thực Google. Vui lòng thử lại.';
                            break;
                        case 'token_exchange_failed':
                            errorMessage = 'Không thể xác thực với Google. Vui lòng thử lại.';
                            break;
                        case 'unverified_email':
                            errorMessage = 'Email Google của bạn chưa được xác minh. Vui lòng xác minh email trước.';
                            break;
                        case 'oauth_processing_failed':
                            errorMessage = 'Lỗi xử lý đăng nhập Google. Vui lòng thử lại sau.';
                            break;
                        case 'oauth_setup_failed':
                            errorMessage = 'Lỗi cấu hình Google OAuth. Vui lòng liên hệ quản trị viên.';
                            break;
                        case 'oauth_error':
                            errorMessage = 'Lỗi đăng nhập Google. Vui lòng thử lại.';
                            break;
                        default:
                            errorMessage = 'Đã xảy ra lỗi. Vui lòng thử lại.';
                    }
                    
                    if (errorMessage) {
                        // Create inline error alert for Google OAuth errors
                        const titleElement = document.querySelector('.login-title');
                        const errorDiv = document.createElement('div');
                        errorDiv.className = 'inline-error-alert';
                        errorDiv.innerHTML = '<i class="fas fa-exclamation-triangle"></i>' + errorMessage;
                        titleElement.parentNode.insertBefore(errorDiv, titleElement.nextElementSibling);
                    }
                }
            });
        </script>
    </body>
</html>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quên Mật Khẩu - Coffee Shop Management</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }

        .forgot-password-container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            max-width: 500px;
            width: 100%;
            padding: 40px;
            animation: slideIn 0.5s ease-out;
        }

        @keyframes slideIn {
            from {
                opacity: 0;
                transform: translateY(-30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .header {
            text-align: center;
            margin-bottom: 30px;
        }

        .header .icon {
            font-size: 60px;
            color: #667eea;
            margin-bottom: 15px;
        }

        .header h2 {
            color: #333;
            font-size: 28px;
            margin-bottom: 10px;
            font-weight: 600;
        }

        .header p {
            color: #666;
            font-size: 14px;
            line-height: 1.6;
        }

        .alert {
            border-radius: 10px;
            padding: 15px;
            margin-bottom: 20px;
            border: none;
        }

        .alert-success {
            background-color: #d4edda;
            color: #155724;
            border-left: 4px solid #28a745;
        }

        .alert-danger {
            background-color: #f8d7da;
            color: #721c24;
            border-left: 4px solid #dc3545;
        }

        .alert i {
            margin-right: 10px;
        }

        .form-group {
            margin-bottom: 25px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #333;
            font-weight: 500;
            font-size: 14px;
        }

        .form-group label i {
            margin-right: 5px;
            color: #667eea;
        }

        .form-control {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e0e0e0;
            border-radius: 10px;
            font-size: 15px;
            transition: all 0.3s ease;
        }

        .form-control:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }

        .btn-submit {
            width: 100%;
            padding: 14px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            margin-top: 10px;
        }

        .btn-submit:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
        }

        .btn-submit:active {
            transform: translateY(0);
        }

        .btn-submit i {
            margin-right: 8px;
        }

        .back-to-login {
            text-align: center;
            margin-top: 25px;
            padding-top: 25px;
            border-top: 1px solid #e0e0e0;
        }

        .back-to-login a {
            color: #667eea;
            text-decoration: none;
            font-weight: 500;
            transition: all 0.3s ease;
        }

        .back-to-login a:hover {
            color: #764ba2;
        }

        .back-to-login i {
            margin-right: 5px;
        }

        .info-box {
            background-color: #e7f3ff;
            border-left: 4px solid #2196F3;
            padding: 15px;
            border-radius: 5px;
            margin-top: 20px;
            font-size: 13px;
            color: #0c5460;
        }

        .info-box strong {
            display: block;
            margin-bottom: 5px;
        }

        .loading {
            display: none;
            text-align: center;
            margin-top: 10px;
        }

        .loading.active {
            display: block;
        }

        .spinner {
            border: 3px solid #f3f3f3;
            border-top: 3px solid #667eea;
            border-radius: 50%;
            width: 30px;
            height: 30px;
            animation: spin 1s linear infinite;
            margin: 0 auto;
        }

        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }

        @media (max-width: 576px) {
            .forgot-password-container {
                padding: 30px 20px;
            }

            .header h2 {
                font-size: 24px;
            }
        }
    </style>
</head>
<body>
    <div class="forgot-password-container">
        <!-- Header -->
        <div class="header">
            <div class="icon">
                <i class="fas fa-key"></i>
            </div>
            <h2>Quên Mật Khẩu</h2>
            <p>Nhập địa chỉ email của bạn và chúng tôi sẽ gửi link đặt lại mật khẩu</p>
        </div>
        
        <!-- Success Message -->
        <c:if test="${not empty success}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i>
                ${success}
            </div>
        </c:if>
        
        <!-- Error Message -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                <i class="fas fa-exclamation-circle"></i>
                ${error}
            </div>
        </c:if>
        
        <!-- Forgot Password Form -->
        <form id="forgotPasswordForm" method="post" action="${pageContext.request.contextPath}/forgot-password">
            <div class="form-group">
                <label for="email">
                    <i class="fas fa-envelope"></i>
                    Địa chỉ Email
                </label>
                <input 
                    type="email" 
                    id="email" 
                    name="email" 
                    class="form-control" 
                    placeholder="Nhập email đã đăng ký"
                    value="${email != null ? email : ''}"
                    required
                    autocomplete="email"
                >
            </div>
            
            <button type="submit" class="btn-submit" id="submitBtn">
                <i class="fas fa-paper-plane"></i>
                Gửi Link Đặt Lại Mật Khẩu
            </button>
            
            <div class="loading" id="loading">
                <div class="spinner"></div>
                <p style="margin-top: 10px; color: #666;">Đang gửi email...</p>
            </div>
        </form>
        
        <!-- Info Box -->
        <div class="info-box">
            <strong><i class="fas fa-info-circle"></i> Lưu ý:</strong>
            <ul style="margin: 5px 0 0 20px; padding: 0;">
                <li>Link đặt lại mật khẩu có hiệu lực trong <strong>2 giờ</strong></li>
                <li>Vui lòng kiểm tra cả thư mục <strong>Spam/Junk</strong></li>
                <li>Nếu không nhận được email, vui lòng thử lại sau 5 phút</li>
            </ul>
        </div>
        
        <!-- Back to Login -->
        <div class="back-to-login">
            <a href="${pageContext.request.contextPath}/login">
                <i class="fas fa-arrow-left"></i>
                Quay lại trang đăng nhập
            </a>
        </div>
    </div>

    <script>
        // Vanilla JavaScript - No jQuery dependency
        document.addEventListener('DOMContentLoaded', function() {
            const forgotPasswordForm = document.getElementById('forgotPasswordForm');
            const submitBtn = document.getElementById('submitBtn');
            const loading = document.getElementById('loading');
            
            // Form submission
            if (forgotPasswordForm) {
                forgotPasswordForm.addEventListener('submit', function() {
                    if (submitBtn) {
                        submitBtn.disabled = true;
                    }
                    if (loading) {
                        loading.classList.add('active');
                    }
                });
            }
            
            // Auto-hide success/error messages after 10 seconds
            setTimeout(function() {
                const alerts = document.querySelectorAll('.alert');
                alerts.forEach(function(alert) {
                    alert.style.transition = 'opacity 0.5s';
                    alert.style.opacity = '0';
                    setTimeout(function() {
                        alert.style.display = 'none';
                    }, 500);
                });
            }, 10000);
        });
    </script>
</body>
</html>

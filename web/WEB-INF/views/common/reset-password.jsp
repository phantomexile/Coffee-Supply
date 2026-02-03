<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt Lại Mật Khẩu - Coffee Shop Management</title>
    
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

        .reset-password-container {
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
        }

        .email-display {
            background-color: #f8f9fa;
            padding: 12px 15px;
            border-radius: 10px;
            margin-bottom: 25px;
            text-align: center;
            border: 2px solid #e0e0e0;
        }

        .email-display i {
            color: #667eea;
            margin-right: 8px;
        }

        .email-display strong {
            color: #333;
            font-size: 15px;
        }

        .alert {
            border-radius: 10px;
            padding: 15px;
            margin-bottom: 20px;
            border: none;
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
            margin-bottom: 20px;
            position: relative;
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

        .password-input-wrapper {
            position: relative;
        }

        .form-control {
            width: 100%;
            padding: 12px 45px 12px 15px;
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

        .password-toggle {
            position: absolute;
            right: 15px;
            top: 50%;
            transform: translateY(-50%);
            background: none;
            border: none;
            cursor: pointer;
            color: #666;
            font-size: 18px;
            transition: color 0.3s ease;
        }

        .password-toggle:hover {
            color: #667eea;
        }

        .password-strength {
            margin-top: 8px;
            font-size: 13px;
        }

        .strength-bar {
            height: 4px;
            background-color: #e0e0e0;
            border-radius: 2px;
            margin-top: 5px;
            overflow: hidden;
        }

        .strength-bar-fill {
            height: 100%;
            width: 0%;
            transition: all 0.3s ease;
        }

        .strength-weak { background-color: #dc3545; width: 33%; }
        .strength-medium { background-color: #ffc107; width: 66%; }
        .strength-strong { background-color: #28a745; width: 100%; }

        .password-requirements {
            background-color: #e7f3ff;
            border-left: 4px solid #2196F3;
            padding: 12px;
            border-radius: 5px;
            margin-top: 15px;
            font-size: 13px;
        }

        .password-requirements ul {
            margin: 5px 0 0 20px;
            padding: 0;
            color: #0c5460;
        }

        .password-requirements li {
            margin: 3px 0;
        }

        .password-requirements li.valid {
            color: #28a745;
        }

        .password-requirements li.valid::before {
            content: "✓ ";
            font-weight: bold;
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
            margin-top: 20px;
        }

        .btn-submit:hover:not(:disabled) {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
        }

        .btn-submit:disabled {
            opacity: 0.6;
            cursor: not-allowed;
        }

        .btn-submit:active {
            transform: translateY(0);
        }

        .btn-submit i {
            margin-right: 8px;
        }

        @media (max-width: 576px) {
            .reset-password-container {
                padding: 30px 20px;
            }

            .header h2 {
                font-size: 24px;
            }
        }
    </style>
</head>
<body>
    <div class="reset-password-container">
        <!-- Header -->
        <div class="header">
            <div class="icon">
                <i class="fas fa-lock-open"></i>
            </div>
            <h2>Đặt Lại Mật Khẩu</h2>
            <p>Tạo mật khẩu mới cho tài khoản của bạn</p>
        </div>
        
        <!-- Email Display -->
        <c:if test="${not empty email}">
            <div class="email-display">
                <i class="fas fa-user"></i>
                <strong>${email}</strong>
            </div>
        </c:if>
        
        <!-- Error Message -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                <i class="fas fa-exclamation-circle"></i>
                ${error}
            </div>
        </c:if>
        
        <!-- Reset Password Form -->
        <form id="resetPasswordForm" method="post" action="${pageContext.request.contextPath}/reset-password">
            <input type="hidden" name="token" value="${token}">
            
            <div class="form-group">
                <label for="newPassword">
                    <i class="fas fa-key"></i>
                    Mật Khẩu Mới
                </label>
                <div class="password-input-wrapper">
                    <input 
                        type="password" 
                        id="newPassword" 
                        name="newPassword" 
                        class="form-control" 
                        placeholder="Nhập mật khẩu mới"
                        required
                        minlength="6"
                    >
                    <button type="button" class="password-toggle" onclick="togglePassword('newPassword')">
                        <i class="fas fa-eye" id="newPasswordIcon"></i>
                    </button>
                </div>
                <div class="password-strength" id="passwordStrength" style="display: none;">
                    <span id="strengthText"></span>
                    <div class="strength-bar">
                        <div class="strength-bar-fill" id="strengthBar"></div>
                    </div>
                </div>
            </div>
            
            <div class="form-group">
                <label for="confirmPassword">
                    <i class="fas fa-check-double"></i>
                    Xác Nhận Mật Khẩu
                </label>
                <div class="password-input-wrapper">
                    <input 
                        type="password" 
                        id="confirmPassword" 
                        name="confirmPassword" 
                        class="form-control" 
                        placeholder="Nhập lại mật khẩu mới"
                        required
                        minlength="6"
                    >
                    <button type="button" class="password-toggle" onclick="togglePassword('confirmPassword')">
                        <i class="fas fa-eye" id="confirmPasswordIcon"></i>
                    </button>
                </div>
                <small id="passwordMatch" style="display: none;"></small>
            </div>
            
            <!-- Password Requirements -->
            <div class="password-requirements">
                <strong><i class="fas fa-info-circle"></i> Yêu cầu mật khẩu:</strong>
                <ul id="requirements">
                    <li id="req-length">Ít nhất 6 ký tự</li>
                    <li id="req-match">Mật khẩu xác nhận phải khớp</li>
                </ul>
            </div>
            
            <button type="submit" class="btn-submit" id="submitBtn" disabled>
                <i class="fas fa-save"></i>
                Đặt Lại Mật Khẩu
            </button>
        </form>
    </div>

    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-2.2.4.min.js"></script>
    
    <script>
        // Toggle password visibility
        function togglePassword(fieldId) {
            var field = document.getElementById(fieldId);
            var icon = document.getElementById(fieldId + 'Icon');
            
            if (field.type === 'password') {
                field.type = 'text';
                icon.classList.remove('fa-eye');
                icon.classList.add('fa-eye-slash');
            } else {
                field.type = 'password';
                icon.classList.remove('fa-eye-slash');
                icon.classList.add('fa-eye');
            }
        }
        
        $(document).ready(function() {
            var newPasswordField = $('#newPassword');
            var confirmPasswordField = $('#confirmPassword');
            var submitBtn = $('#submitBtn');
            
            // Check password strength
            newPasswordField.on('input', function() {
                var password = $(this).val();
                var strength = 0;
                var strengthText = '';
                var strengthClass = '';
                
                if (password.length >= 6) {
                    strength++;
                    $('#req-length').addClass('valid');
                } else {
                    $('#req-length').removeClass('valid');
                }
                
                if (password.length >= 8) strength++;
                if (/[A-Z]/.test(password)) strength++;
                if (/[0-9]/.test(password)) strength++;
                if (/[^A-Za-z0-9]/.test(password)) strength++;
                
                if (password.length === 0) {
                    $('#passwordStrength').hide();
                } else {
                    $('#passwordStrength').show();
                    
                    if (strength <= 2) {
                        strengthText = 'Yếu';
                        strengthClass = 'strength-weak';
                    } else if (strength <= 3) {
                        strengthText = 'Trung bình';
                        strengthClass = 'strength-medium';
                    } else {
                        strengthText = 'Mạnh';
                        strengthClass = 'strength-strong';
                    }
                    
                    $('#strengthText').text('Độ mạnh: ' + strengthText);
                    $('#strengthBar').removeClass('strength-weak strength-medium strength-strong').addClass(strengthClass);
                }
                
                checkPasswordMatch();
            });
            
            // Check password match
            confirmPasswordField.on('input', function() {
                checkPasswordMatch();
            });
            
            function checkPasswordMatch() {
                var newPassword = newPasswordField.val();
                var confirmPassword = confirmPasswordField.val();
                
                if (confirmPassword.length === 0) {
                    $('#passwordMatch').hide();
                    $('#req-match').removeClass('valid');
                    submitBtn.prop('disabled', true);
                    return;
                }
                
                if (newPassword === confirmPassword && newPassword.length >= 6) {
                    $('#passwordMatch').show().css('color', '#28a745').text('✓ Mật khẩu khớp');
                    $('#req-match').addClass('valid');
                    submitBtn.prop('disabled', false);
                } else {
                    $('#passwordMatch').show().css('color', '#dc3545').text('✗ Mật khẩu không khớp');
                    $('#req-match').removeClass('valid');
                    submitBtn.prop('disabled', true);
                }
            }
            
            // Form submission
            $('#resetPasswordForm').on('submit', function(e) {
                var newPassword = newPasswordField.val();
                var confirmPassword = confirmPasswordField.val();
                
                if (newPassword.length < 6) {
                    e.preventDefault();
                    alert('Mật khẩu phải có ít nhất 6 ký tự');
                    return false;
                }
                
                if (newPassword !== confirmPassword) {
                    e.preventDefault();
                    alert('Mật khẩu xác nhận không khớp');
                    return false;
                }
                
                submitBtn.prop('disabled', true).html('<i class="fas fa-spinner fa-spin"></i> Đang xử lý...');
            });
        });
    </script>
</body>
</html>

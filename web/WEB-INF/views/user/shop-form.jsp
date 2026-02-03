<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>${empty shop ? 'Thêm' : 'Sửa'} Shop - User | Coffee Shop</title>
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
    
    <!-- Phone Validation Styling -->
    <style>
        /* Validation states for form controls */
        .form-control.is-valid {
            border-color: #28a745;
            box-shadow: 0 0 0 0.2rem rgba(40, 167, 69, 0.25);
        }
        
        .form-control.is-invalid {
            border-color: #dc3545;
            box-shadow: 0 0 0 0.2rem rgba(220, 53, 69, 0.25);
        }
        
        /* Help text styling */
        .help-block {
            margin-top: 5px;
            font-size: 12px;
            font-weight: 500;
        }
        
        .help-block.text-success {
            color: #28a745 !important;
        }
        
        .help-block.text-danger {
            color: #dc3545 !important;
        }
        
        /* Phone field specific styling */
        .phone-validation-container {
            position: relative;
        }
        
        .phone-validation-icon {
            position: absolute;
            right: 10px;
            top: 50%;
            transform: translateY(-50%);
            font-size: 16px;
            z-index: 3;
        }
        
        .phone-validation-icon.valid {
            color: #28a745;
        }
        
        .phone-validation-icon.invalid {
            color: #dc3545;
        }
        
        /* Enhanced error messages */
        .validation-message {
            padding: 8px 12px;
            border-radius: 4px;
            margin-top: 5px;
            font-size: 13px;
            font-weight: 500;
        }
        
        .validation-message.success {
            background-color: #d4edda;
            border: 1px solid #c3e6cb;
            color: #155724;
        }
        
        .validation-message.error {
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            color: #721c24;
        }
        
        /* Form field focus enhancements */
        .form-control:focus.is-valid {
            border-color: #28a745;
            box-shadow: 0 0 0 0.2rem rgba(40, 167, 69, 0.35);
        }
        
        .form-control:focus.is-invalid {
            border-color: #dc3545;
            box-shadow: 0 0 0 0.2rem rgba(220, 53, 69, 0.35);
        }
        
        /* Animation for validation feedback */
        .help-block, .validation-message {
            transition: all 0.3s ease;
            opacity: 1;
        }
        
        .help-block.fade-in {
            animation: fadeIn 0.3s ease-in;
        }
        
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(-5px); }
            to { opacity: 1; transform: translateY(0); }
        }
        
        /* Submit button state */
        .btn-primary:disabled {
            background-color: #6c757d;
            border-color: #6c757d;
            cursor: not-allowed;
        }
    </style>
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
                    <i class="fa fa-building"></i> ${empty shop ? 'Thêm Shop Mới' : 'Sửa Shop'}
                    <small>${empty shop ? 'Tạo shop của bạn' : 'Chỉnh sửa thông tin shop'}</small>
                </h1>
                <ol class="breadcrumb">
                    <li><a href="${pageContext.request.contextPath}/views/common/dashboard.jsp"><i class="fa fa-dashboard"></i> Trang chủ</a></li>
                    <li><a href="${pageContext.request.contextPath}/user/shop?action=list">Danh sách Shop</a></li>
                    <li class="active">${empty shop ? 'Thêm' : 'Sửa'}</li>
                </ol>
            </section>

            <section class="content">
                <div class="row">
                    <div class="col-md-8 col-md-offset-2">
                        <div class="box box-primary">
                            <div class="box-header with-border">
                                <h3 class="box-title">
                                    <i class="fa fa-edit"></i> Thông tin Shop
                                </h3>
                            </div>
                            
                            <form method="post" action="${pageContext.request.contextPath}/user/shop" enctype="multipart/form-data">
                                <div class="box-body">
                                    <input type="hidden" name="action" value="${empty shop ? 'add' : 'edit'}">
                                    <c:if test="${not empty shop}">
                                        <input type="hidden" name="shopId" value="${shop.shopID}">
                                    </c:if>
                                    
                                    <!-- Hiển thị thông báo lỗi -->
                                    <c:if test="${not empty sessionScope.message}">
                                        <div class="alert alert-${sessionScope.messageType} alert-dismissible">
                                            <button type="button" class="close" data-dismiss="alert">&times;</button>
                                            <i class="fa ${sessionScope.messageType eq 'success' ? 'fa-check' : 'fa-ban'}"></i> 
                                            ${sessionScope.message}
                                        </div>
                                        <c:remove var="message" scope="session"/>
                                        <c:remove var="messageType" scope="session"/>
                                    </c:if>

                                    <div class="form-group">
                                        <label for="shopName">
                                            <i class="fa fa-building"></i> Tên Shop 
                                            <span class="text-red">*</span>
                                        </label>
                                        <input type="text" class="form-control" id="shopName" name="shopName" 
                                               value="${shop.shopName}" placeholder="Nhập tên shop" required>
                                    </div>

                                    <div class="form-group">
                                        <label for="address">
                                            <i class="fa fa-map-marker"></i> Địa chỉ 
                                            <span class="text-red">*</span>
                                        </label>
                                        <textarea class="form-control" id="address" name="address" rows="3" 
                                                  placeholder="Nhập địa chỉ shop" required>${shop.address}</textarea>
                                    </div>

                                    <div class="form-group">
                                        <label for="phone">
                                            <i class="fa fa-phone"></i> Số điện thoại 
                                            <span class="text-red">*</span>
                                        </label>
                                        <input type="tel" class="form-control" id="phone" name="phone" 
                                               value="${shop.phone}" placeholder="Nhập số điện thoại" required>
                                    </div>

                                    <!-- Shop Image Upload -->
                                    <div class="form-group">
                                        <label for="shopImage">
                                            <i class="fa fa-image"></i> Hình ảnh Shop
                                        </label>
                                        
                                        <c:if test="${not empty shop && not empty shop.shopImage}">
                                            <div class="current-image-section" style="margin-bottom: 10px;">
                                                <p><strong>Hình ảnh hiện tại:</strong></p>
                                                <img src="${shop.shopImage}" 
                                                     alt="Shop Image" class="img-thumbnail" 
                                                     style="max-width: 200px; max-height: 200px;"
                                                     onerror="this.style.display='none';">
                                            </div>
                                        </c:if>
                                        
                                        <input type="file" class="form-control" id="shopImage" name="shopImage" 
                                               accept="image/*">
                                        <p class="help-block">
                                            <i class="fa fa-info-circle"></i> 
                                            Chọn file ảnh (JPG, JPEG, PNG, GIF). Dung lượng tối đa: 5MB
                                        </p>
                                    </div>

                                    <div class="form-group">
                                        <div class="checkbox">
                                            <label>
                                                <input type="checkbox" name="isActive" value="true" 
                                                       ${empty shop || shop.active ? 'checked' : ''}>
                                                <i class="fa fa-check-circle text-success"></i> Shop đang hoạt động
                                            </label>
                                        </div>
                                    </div>

                                    <c:if test="${not empty shop}">
                                        <div class="callout callout-info">
                                            <h4><i class="fa fa-info-circle"></i> Thông tin bổ sung</h4>
                                            <p><strong>ID Shop:</strong> #${shop.shopID}</p>
                                            <p><strong>Ngày tạo:</strong> ${shop.createdAt}</p>
                                        </div>
                                    </c:if>
                                </div>
                                
                                <div class="box-footer">
                                    <button type="submit" class="btn btn-primary">
                                        <i class="fa fa-save"></i> ${empty shop ? 'Thêm Shop' : 'Cập Nhật'}
                                    </button>
                                    <a href="${pageContext.request.contextPath}/user/shop?action=list" 
                                       class="btn btn-default">
                                        <i class="fa fa-arrow-left"></i> Quay lại
                                    </a>
                                </div>
                            </form>
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
        // Form validation
        document.addEventListener('DOMContentLoaded', function() {
            const form = document.querySelector('form');
            if (form) {
                form.addEventListener('submit', function(e) {
                    const shopName = document.getElementById('shopName').value.trim();
                    const address = document.getElementById('address').value.trim();
                    const phone = document.getElementById('phone').value.trim();
                    
                    // Clear ALL previous error styling and messages
                    document.querySelectorAll('.form-control').forEach(control => {
                        control.classList.remove('is-invalid', 'is-valid');
                    });
                    document.querySelectorAll('.validation-message').forEach(msg => msg.remove());
                    document.querySelectorAll('.help-block').forEach(help => help.remove());
                    document.querySelectorAll('.alert-danger').forEach(alert => alert.remove());
                    document.querySelectorAll('.phone-validation-icon').forEach(icon => icon.remove());
                    document.querySelectorAll('.phone-validation-container').forEach(container => {
                        container.classList.remove('phone-validation-container');
                    });
                    
                    let hasErrors = false;
                    let errorFields = [];
                    
                    const shopNameField = document.getElementById('shopName');
                    if (!shopName) {
                        shopNameField.classList.add('is-invalid');
                        shopNameField.insertAdjacentHTML('afterend', '<div class="validation-message error"><i class="fa fa-exclamation-triangle"></i> Tên shop không được để trống!</div>');
                        errorFields.push('Tên shop');
                        hasErrors = true;
                    }
                    
                    const addressField = document.getElementById('address');
                    if (!address) {
                        addressField.classList.add('is-invalid');
                        addressField.insertAdjacentHTML('afterend', '<div class="validation-message error"><i class="fa fa-exclamation-triangle"></i> Địa chỉ không được để trống!</div>');
                        errorFields.push('Địa chỉ');
                        hasErrors = true;
                    }
                    
                    const phoneField = document.getElementById('phone');
                    if (!phone) {
                        phoneField.classList.add('is-invalid');
                        phoneField.insertAdjacentHTML('afterend', '<div class="validation-message error"><i class="fa fa-exclamation-triangle"></i> Số điện thoại không được để trống!</div>');
                        errorFields.push('Số điện thoại');
                        hasErrors = true;
                    } else if (!isValidPhoneNumber(phone)) {
                        phoneField.classList.add('is-invalid');
                        const errorMsg = '<div class="validation-message error">' +
                                      '<i class="fa fa-exclamation-triangle"></i> ' +
                                      '<strong>Số điện thoại không đúng định dạng!</strong><br>' +
                                      'Vui lòng nhập theo định dạng:<br>' +
                                      '• <strong>0123456789</strong> (10 số bắt đầu bằng 0)<br>' +
                                      '• <strong>+84123456789</strong> (11 số với +84)<br>' +
                                      '• <strong>84123456789</strong> (10 số với 84)' +
                                      '</div>';
                        phoneField.insertAdjacentHTML('afterend', errorMsg);
                        errorFields.push('Số điện thoại');
                        hasErrors = true;
                    }
                    
                    if (hasErrors) {
                        e.preventDefault();
                        
                        // Show summary error message
                        const errorSummary = '<div class="alert alert-danger alert-dismissible" style="margin-top: 15px;">' +
                                          '<button type="button" class="close" data-dismiss="alert">&times;</button>' +
                                          '<h4><i class="fa fa-ban"></i> Vui lòng kiểm tra lại thông tin!</h4>' +
                                          'Các trường sau cần được điền chính xác: <strong>' + errorFields.join(', ') + '</strong>' +
                                          '</div>';
                        
                        // Add error summary at the top of the form
                        const boxBody = document.querySelector('.box-body');
                        if (boxBody) {
                            boxBody.insertAdjacentHTML('afterbegin', errorSummary);
                        }
                        
                        // Scroll to first error field
                        const firstInvalid = document.querySelector('.is-invalid');
                        if (firstInvalid) {
                            firstInvalid.scrollIntoView({ behavior: 'smooth', block: 'center' });
                        }
                        
                        return false;
                    }
            });
            
            // Real-time phone number validation
            const phoneInput = document.getElementById('phone');
            if (phoneInput) {
                phoneInput.addEventListener('input', function() {
                    const phone = this.value.trim();
                    const phoneField = this;
                    
                    // Clean up all existing validation elements first
                    const siblings = phoneField.parentNode.children;
                    Array.from(siblings).forEach(sibling => {
                        if (sibling.classList.contains('help-block') || sibling.classList.contains('validation-message')) {
                            sibling.remove();
                        }
                    });
                    phoneField.classList.remove('is-valid', 'is-invalid');
                    removeValidationIcon(phoneField);
                    
                    if (phone === '') {
                        // Empty field - reset to neutral state, no messages
                        updateSubmitButton();
                        return;
                    }
                    
                    if (isValidPhoneNumber(phone)) {
                        // Valid phone number
                        phoneField.classList.add('is-valid');
                        phoneField.insertAdjacentHTML('afterend', '<div class="help-block text-success fade-in"><i class="fa fa-check"></i> Định dạng số điện thoại hợp lệ</div>');
                        phoneField.insertAdjacentHTML('afterend', '<div class="validation-message success"><i class="fa fa-check-circle"></i> Số điện thoại hợp lệ</div>');
                        addValidationIcon(phoneField, 'valid');
                    } else {
                        // Invalid phone number
                        phoneField.classList.add('is-invalid');
                        phoneField.insertAdjacentHTML('afterend', '<div class="help-block text-danger fade-in"><i class="fa fa-times"></i> Định dạng không hợp lệ</div>');
                        
                        const errorMsg = '<div class="validation-message error">' +
                                      '<i class="fa fa-exclamation-triangle"></i> ' +
                                      '<strong>Định dạng không hợp lệ!</strong><br>' +
                                      'Vui lòng nhập theo các định dạng sau:<br>' +
                                      '• <code>0123456789</code> (10 số bắt đầu bằng 0)<br>' +
                                      '• <code>+84123456789</code> (có mã quốc gia +84)<br>' +
                                      '• <code>84123456789</code> (bắt đầu bằng 84)' +
                                      '</div>';
                        phoneField.insertAdjacentHTML('afterend', errorMsg);
                        addValidationIcon(phoneField, 'invalid');
                    }
                    
                    // Update submit button state
                    updateSubmitButton();
                });
            }
            
            // Add validation icon
            function addValidationIcon(field, type) {
                removeValidationIcon(field);
                const iconClass = type === 'valid' ? 'fa-check-circle valid' : 'fa-times-circle invalid';
                const icon = '<i class="fa ' + iconClass + ' phone-validation-icon"></i>';
                field.parentNode.classList.add('phone-validation-container');
                field.parentNode.insertAdjacentHTML('beforeend', icon);
            }
            
            // Remove validation icon
            function removeValidationIcon(field) {
                field.parentNode.classList.remove('phone-validation-container');
                const icons = field.parentNode.querySelectorAll('.phone-validation-icon');
                icons.forEach(icon => icon.remove());
            }
            
            // Update submit button state based on validation
            function updateSubmitButton() {
                const submitBtn = document.querySelector('button[type="submit"]');
                let allValid = true;
                
                // Check if phone is valid (if not empty)
                const phoneInput = document.getElementById('phone');
                const phone = phoneInput ? phoneInput.value.trim() : '';
                if (phone && !isValidPhoneNumber(phone)) {
                    allValid = false;
                }
                
                // Check required fields
                const shopNameInput = document.getElementById('shopName');
                const addressInput = document.getElementById('address');
                const shopName = shopNameInput ? shopNameInput.value.trim() : '';
                const address = addressInput ? addressInput.value.trim() : '';
                if (!shopName || !address || !phone) {
                    allValid = false;
                }
                
                if (submitBtn) {
                    if (allValid) {
                        submitBtn.disabled = false;
                        submitBtn.classList.remove('btn-secondary');
                        submitBtn.classList.add('btn-primary');
                    } else {
                        submitBtn.disabled = true;
                        submitBtn.classList.remove('btn-primary');
                        submitBtn.classList.add('btn-secondary');
                    }
                }
            }
            
            // Also validate other required fields
            const shopNameInput = document.getElementById('shopName');
            const addressInput = document.getElementById('address');
            [shopNameInput, addressInput].forEach(input => {
                if (input) {
                    input.addEventListener('input', updateSubmitButton);
                }
            });
            
            function isValidPhoneNumber(phone) {
                // Remove all spaces, dashes, parentheses for validation
                const cleanPhone = phone.replace(/[\s\-\(\)]/g, '');
                
                // Vietnamese phone number patterns:
                // 1. 10 digits starting with 0 (domestic format): 0xxxxxxxxx
                // 2. 11 digits starting with +84 (international format): +84xxxxxxxxx
                // 3. 9 digits without leading 0 (for +84 format): after +84
                
                const patterns = [
                    /^0[0-9]{9}$/,           // 0123456789 (10 digits starting with 0)
                    /^\+84[0-9]{9}$/,       // +84123456789 (11 digits with +84)
                    /^84[0-9]{9}$/          // 84123456789 (10 digits starting with 84)
                ];
                
                return patterns.some(pattern => pattern.test(cleanPhone));
            }
        });
    </script>
</body>
</html>
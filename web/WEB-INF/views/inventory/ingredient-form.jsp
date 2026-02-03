<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>${isEdit ? 'Chỉnh sửa' : 'Thêm mới'} nguyên liệu - Coffee Shop</title>
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/AdminLTE.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/skins/_all-skins.min.css">
</head>
<body class="hold-transition skin-blue sidebar-mini">
    <div class="wrapper">
        <%@include file="../compoment/sidebar.jsp" %>
        <%@include file="../compoment/header.jsp" %>
        
        <div class="content-wrapper">
            <section class="content-header">
                <h1>
                    ${isEdit ? 'Chỉnh sửa' : 'Thêm mới'} nguyên liệu
                    <small>${isEdit ? 'Cập nhật thông tin nguyên liệu' : 'Thêm nguyên liệu mới vào kho'}</small>
                </h1>
                <ol class="breadcrumb">
                    <li><a href="${pageContext.request.contextPath}/inventory/dashboard"><i class="fa fa-dashboard"></i> Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/ingredient">Quản lý nguyên liệu</a></li>
                    <li class="active">${isEdit ? 'Chỉnh sửa' : 'Thêm mới'}</li>
                </ol>
            </section>

            <section class="content">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                        <h4><i class="icon fa fa-ban"></i> Lỗi!</h4>
                        ${error}
                    </div>
                </c:if>

                <div class="row">
                    <div class="col-md-10 col-md-offset-1">
                        <div class="box box-primary">
                            <div class="box-header with-border">
                                <h3 class="box-title">${isEdit ? 'Chỉnh sửa' : 'Thêm mới'} nguyên liệu</h3>
                            </div>
                            
                            <form method="POST" action="${pageContext.request.contextPath}/ingredient">
                                <div class="box-body">
                                    <input type="hidden" name="action" value="${isEdit ? 'update' : 'create'}">
                                    <c:if test="${isEdit}">
                                        <input type="hidden" name="ingredientId" value="${ingredient.ingredientID}">
                                    </c:if>

                                    <div class="row">
                                        <div class="col-md-8">
                                            <div class="form-group">
                                                <label for="name">Tên nguyên liệu <span style="color: red;">*</span></label>
                                                <input type="text" name="name" id="name" class="form-control" 
                                                       value="${isEdit ? ingredient.name : param.name}" 
                                                       placeholder="VD: Cà phê Arabica hạt cao cấp..." 
                                                       required maxlength="100">
                                                <small class="help-block">Tên nguyên liệu phải duy nhất trong hệ thống</small>
                                            </div>
                                            
                                            <div class="form-group">
                                                <label for="description">Mô tả</label>
                                                <textarea name="description" id="description" class="form-control" 
                                                          rows="3" maxlength="255" 
                                                          placeholder="VD: Hạt cà phê Arabica nguyên chất, chất lượng cao từ Tây Nguyên...">${isEdit ? ingredient.description : param.description}</textarea>
                                                <small class="help-block">Mô tả chi tiết về nguyên liệu (tối đa 255 ký tự)</small>
                                                <small class="help-block text-muted">
                                                    <span id="charCount">0</span>/255 ký tự
                                                </small>
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label>Trạng thái <span style="color: red;">*</span></label>
                                                <div class="radio">
                                                    <label>
                                                        <input type="radio" name="active" value="true" 
                                                               ${(isEdit && ingredient.active) || (!isEdit && param.active == 'true') || (!isEdit && empty param.active) ? 'checked' : ''}>
                                                        Hoạt động
                                                    </label>
                                                </div>
                                                <div class="radio">
                                                    <label>
                                                        <input type="radio" name="active" value="false" 
                                                               ${(isEdit && !ingredient.active) || (!isEdit && param.active == 'false') ? 'checked' : ''}>
                                                        Ngừng hoạt động
                                                    </label>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label for="stockQuantity">Số lượng tồn kho <span style="color: red;">*</span></label>
                                                <input type="number" step="0.01" min="0" name="stockQuantity" 
                                                       id="stockQuantity" class="form-control" 
                                                       value="${isEdit ? ingredient.stockQuantity : (empty param.stockQuantity ? '0' : param.stockQuantity)}" 
                                                       placeholder="0.00" required>
                                                <small class="help-block">Số lượng hiện có trong kho</small>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label for="unitId">Đơn vị tính <span style="color: red;">*</span></label>
                                                <select name="unitId" id="unitId" class="form-control" required>
                                                    <option value="">-- Chọn đơn vị --</option>
                                                    <c:forEach var="unit" items="${units}">
                                                        <option value="${unit.settingID}" 
                                                                ${(isEdit && ingredient.unitID == unit.settingID) || (!isEdit && param.unitId == unit.settingID) ? 'selected' : ''}>
                                                            ${unit.value} - ${unit.description}
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                                <small class="help-block">Đơn vị để đo lường nguyên liệu</small>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-12">
                                            <div class="form-group">
                                                <label for="supplierId">Nhà cung cấp <span style="color: red;">*</span></label>
                                                <select name="supplierId" id="supplierId" class="form-control" required>
                                                    <option value="">-- Chọn nhà cung cấp --</option>
                                                    <c:forEach var="supplier" items="${suppliers}">
                                                        <option value="${supplier.supplierID}" 
                                                                ${(isEdit && ingredient.supplierID == supplier.supplierID) || (!isEdit && param.supplierId == supplier.supplierID) ? 'selected' : ''}>
                                                            ${supplier.supplierName} - ${supplier.contactName}
                                                            <c:if test="${not empty supplier.phone}">
                                                                (${supplier.phone})
                                                            </c:if>
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                                <small class="help-block">Nhà cung cấp chính của nguyên liệu này</small>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="box-footer text-center">
                                    <a href="${pageContext.request.contextPath}/ingredient" class="btn btn-default btn-lg">
                                        <i class="fa fa-times"></i> Hủy
                                    </a>
                                    <button type="submit" class="btn btn-primary btn-lg">
                                        <i class="fa fa-save"></i> ${isEdit ? 'Cập nhật' : 'Thêm mới'}
                                    </button>
                                    <c:if test="${isEdit}">
                                        <a href="${pageContext.request.contextPath}/ingredient?action=view&id=${ingredient.ingredientID}" 
                                           class="btn btn-info btn-lg">
                                            <i class="fa fa-eye"></i> Xem chi tiết
                                        </a>
                                    </c:if>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </div>

    <!-- Bootstrap 3.3.6 -->
    <script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/dist/js/app.min.js"></script>
    
    <script>
        // Vanilla JavaScript - No jQuery dependency for custom logic
        document.addEventListener('DOMContentLoaded', function() {
            const descriptionField = document.getElementById('description');
            const charCountElement = document.getElementById('charCount');
            const nameField = document.getElementById('name');
            const stockQuantityField = document.getElementById('stockQuantity');
            const unitIdField = document.getElementById('unitId');
            const supplierIdField = document.getElementById('supplierId');
            const formElement = document.querySelector('form');
            
            // Character counter for description
            function updateCharCount() {
                const length = descriptionField.value.length;
                charCountElement.textContent = length;
                if (length > 255) {
                    charCountElement.style.color = 'red';
                } else if (length > 200) {
                    charCountElement.style.color = 'orange';
                } else {
                    charCountElement.style.color = '';
                }
            }
            
            // Initial count
            updateCharCount();
            
            // Update on input
            descriptionField.addEventListener('input', updateCharCount);
            
            // Form validation and submission
            formElement.addEventListener('submit', function(e) {
                const name = nameField.value.trim();
                const description = descriptionField.value;
                const stockQuantity = stockQuantityField.value;
                const unitId = unitIdField.value;
                const supplierId = supplierIdField.value;
                const activeRadio = document.querySelector('input[name="active"]:checked');
                const active = activeRadio ? activeRadio.value : null;
                
                // Validate description length
                if (description && description.length > 255) {
                    alert('Mô tả không được vượt quá 255 ký tự');
                    descriptionField.focus();
                    e.preventDefault();
                    return false;
                }
                
                if (!name) {
                    alert('Vui lòng nhập tên nguyên liệu');
                    nameField.focus();
                    e.preventDefault();
                    return false;
                }
                
                if (name.length > 100) {
                    alert('Tên nguyên liệu không được quá 100 ký tự');
                    nameField.focus();
                    e.preventDefault();
                    return false;
                }
                
                if (!stockQuantity || parseFloat(stockQuantity) < 0) {
                    alert('Số lượng tồn kho phải là số không âm');
                    stockQuantityField.focus();
                    e.preventDefault();
                    return false;
                }
                
                if (!unitId) {
                    alert('Vui lòng chọn đơn vị tính');
                    unitIdField.focus();
                    e.preventDefault();
                    return false;
                }
                
                if (!supplierId) {
                    alert('Vui lòng chọn nhà cung cấp');
                    supplierIdField.focus();
                    e.preventDefault();
                    return false;
                }
                
                if (!active) {
                    alert('Vui lòng chọn trạng thái');
                    document.querySelector('input[name="active"]').focus();
                    e.preventDefault();
                    return false;
                }
                
                // Show loading overlay
                const loadingDiv = document.createElement('div');
                loadingDiv.style.cssText = 'position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.7); z-index: 9999; display: flex; justify-content: center; align-items: center;';
                loadingDiv.innerHTML = '<div style="width: 50px; height: 50px; border: 5px solid #f3f3f3; border-top: 5px solid #3c8dbc; border-radius: 50%; animation: spin 1s linear infinite;"></div>';
                document.body.appendChild(loadingDiv);
                
                // Add CSS animation if not exists
                if (!document.getElementById('spin-style')) {
                    const style = document.createElement('style');
                    style.id = 'spin-style';
                    style.textContent = '@keyframes spin { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }';
                    document.head.appendChild(style);
                }
                
                return true;
            });
            
            // Auto dismiss alerts
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
        });
    </script>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${po != null ? 'Chỉnh sửa' : 'Tạo mới'} Đơn hàng</title>
    
    <!-- Bootstrap 3.3.6 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/AdminLTE.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/skins/_all-skins.min.css">
    
    <style>
        body {
            background-color: #f4f4f4;
        }
        
        .content-wrapper {
            margin-left: 230px;
            padding: 20px;
            min-height: 100vh;
        }
        
        .page-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 30px;
            border-radius: 10px;
            margin-bottom: 30px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
        
        .page-header h1 {
            margin: 0;
            font-size: 28px;
            font-weight: 600;
            color: white;
        }
        
        .box {
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
            border-top: 3px solid #3c8dbc;
        }
        
        .box-header {
            background: #f9f9f9;
            border-bottom: 1px solid #ddd;
            padding: 10px 15px;
        }
        
        .box-header h3 {
            margin: 0;
            color: #333;
            font-weight: 600;
            font-size: 18px;
        }
        
        .form-group label {
            font-weight: 600;
            color: #555;
        }
        
        .form-control, .select2-container--default .select2-selection--single {
            border: 1px solid #ddd;
        }
        
        .form-control:focus {
            border-color: #3c8dbc;
            box-shadow: 0 0 5px rgba(60, 141, 188, 0.3);
        }
        
        .detail-row {
            background: #f9f9f9;
            border: 1px solid #ddd;
            padding: 15px;
            margin-bottom: 15px;
        }
        
        .detail-row:hover {
            border-color: #3c8dbc;
        }
        
        .btn-primary {
            background-color: #3c8dbc;
            border-color: #3c8dbc;
        }
        
        .btn-primary:hover {
            background-color: #357ca5;
            border-color: #357ca5;
        }
        
        .btn-success {
            background-color: #00a65a;
            border-color: #00a65a;
        }
        
        .btn-success:hover {
            background-color: #008d4c;
            border-color: #008d4c;
        }
        
        .btn-danger {
            background-color: #dd4b39;
            border-color: #dd4b39;
        }
        
        .btn-danger:hover {
            background-color: #d33724;
            border-color: #d33724;
        }
        
        .required-star {
            color: #dd4b39;
            font-weight: bold;
        }
        
        .breadcrumb {
            background: transparent;
            padding: 10px 0;
        }
        
        .section-title {
            color: #3c8dbc;
            font-weight: 600;
            font-size: 18px;
            margin-bottom: 15px;
            border-bottom: 2px solid #3c8dbc;
            padding-bottom: 5px;
        }
        
        .alert {
            border: none;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        }
        
        .help-block {
            color: #999;
            font-size: 12px;
            margin-top: 5px;
        }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    
    <!-- Include Header -->
    <jsp:include page="../compoment/header.jsp" />
    
    <!-- Include Sidebar -->
    <jsp:include page="../compoment/sidebar.jsp" />
    
    <div class="content-wrapper">
        <!-- Page Header -->
        <div class="page-header">
            <h1>
                <i class="fa ${po != null ? 'fa-edit' : 'fa-plus-circle'}"></i> 
                ${po != null ? 'Chỉnh sửa' : 'Tạo mới'} Đơn hàng
            </h1>
            <ol class="breadcrumb" style="background: transparent; padding: 10px 0;">
                <c:choose>
                    <c:when test="${sessionScope.roleName == 'Admin'}">
                        <li><a href="${pageContext.request.contextPath}/admin/dashboard" style="color: white;"><i class="fa fa-dashboard"></i> Dashboard</a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="${pageContext.request.contextPath}/inventory/dashboard" style="color: white;"><i class="fa fa-dashboard"></i> Dashboard</a></li>
                    </c:otherwise>
                </c:choose>
                <li><a href="${pageContext.request.contextPath}/purchase-order?action=list" style="color: white;">Danh sách đơn hàng</a></li>
                <li class="active" style="color: rgba(255,255,255,0.8);">${po != null ? 'Chỉnh sửa' : 'Tạo mới'}</li>
            </ol>
        </div>
        
        <!-- Success/Error Messages -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible">
                <button type="button" class="close" data-dismiss="alert">&times;</button>
                <i class="fa fa-exclamation-circle"></i> ${errorMessage}
            </div>
        </c:if>
        
        <!-- Form -->
        <form action="${pageContext.request.contextPath}/purchase-order" method="post" id="poForm">
            <input type="hidden" name="action" value="${po != null ? 'update' : 'create'}">
            <c:if test="${po != null}">
                <input type="hidden" name="poID" value="${po.poID}">
            </c:if>
            
            <!-- Purchase Order Information -->
            <div class="box">
                <div class="box-header">
                    <h3 class="box-title"><i class="fa fa-info-circle"></i> Thông tin đơn hàng</h3>
                </div>
                <div class="box-body">
                    <div class="row">
                        <!-- Hidden Shop ID field - default to shop 1 -->
                        <input type="hidden" name="shopID" value="${po != null ? po.shopID : 1}">
                        
                        <div class="col-md-12">
                            <div class="form-group">
                                <label for="supplierID">
                                    <i class="fa fa-truck"></i> Nhà cung cấp 
                                    <span class="required-star">*</span>
                                </label>
                                <select class="form-control" id="supplierID" name="supplierID" required>
                                    <option value="">-- Chọn nhà cung cấp --</option>
                                    <c:forEach var="supplier" items="${suppliers}">
                                        <c:if test="${supplier.active}">
                                            <option value="${supplier.supplierID}" 
                                                    ${po != null && po.supplierID == supplier.supplierID ? 'selected' : ''}>
                                                ${supplier.supplierName}
                                            </option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                                <span class="help-block">Chọn nhà cung cấp nguyên liệu</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Purchase Order Details -->
            <div class="box" style="margin-top: 20px;">
                <div class="box-header">
                    <h3 class="box-title"><i class="fa fa-list"></i> Chi tiết nguyên liệu</h3>
                </div>
                <div class="box-body">
                    <div id="detailsContainer">
                        <c:choose>
                            <c:when test="${not empty details}">
                                <!-- Existing details when editing -->
                                <c:forEach var="detail" items="${details}">
                                    <div class="detail-row">
                                        <div class="row">
                                            <div class="col-md-4">
                                                <div class="form-group">
                                                    <label>
                                                        <i class="fa fa-cube"></i> Nguyên liệu 
                                                        <span class="required-star">*</span>
                                                    </label>
                                                    <select class="form-control ingredient-select" name="ingredientID[]" 
                                                            data-selected-id="${detail.ingredientID}" 
                                                            data-selected-name="${detail.ingredientName}" 
                                                            data-row-index="${status.index}"
                                                            required>
                                                        <option value="">-- Chọn nguyên liệu --</option>
                                                        <c:set var="ingredientFound" value="false" />
                                                        <c:forEach var="ingredient" items="${ingredients}">
                                                            <option value="${ingredient.ingredientID}" 
                                                                    data-supplier-id="${ingredient.supplierID}"
                                                                    data-unit-id="${ingredient.unitID}"
                                                                    ${ingredient.ingredientID == detail.ingredientID ? 'selected="selected"' : ''}>
                                                                ${ingredient.name}
                                                            </option>
                                                            <c:if test="${ingredient.ingredientID == detail.ingredientID}">
                                                                <c:set var="ingredientFound" value="true" />
                                                            </c:if>
                                                        </c:forEach>
                                                        <!-- Add the selected ingredient if it's not in the list -->
                                                        <c:if test="${not ingredientFound and not empty detail.ingredientID}">
                                                            <option value="${detail.ingredientID}" selected="selected" data-supplier-id="0" data-unit-id="${detail.unitID}">
                                                                ${detail.ingredientName} (ID: ${detail.ingredientID} - Không còn trong danh sách)
                                                            </option>
                                                        </c:if>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="col-md-3">
                                                <div class="form-group">
                                                    <label>
                                                        <i class="fa fa-calculator"></i> Số lượng 
                                                        <span class="required-star">*</span>
                                                    </label>
                                                    <input type="number" class="form-control" name="quantity[]" 
                                                           value="${detail.quantity}" step="0.01" min="0.01" 
                                                           placeholder="Nhập số lượng" required>
                                                </div>
                                            </div>
                                            <div class="col-md-3">
                                                <div class="form-group">
                                                    <label>
                                                        <i class="fa fa-balance-scale"></i> Đơn vị 
                                                        <span class="required-star">*</span>
                                                    </label>
                                                    <select class="form-control unit-select" name="unitID[]" 
                                                            data-row-index="${status.index}"
                                                            required>
                                                        <option value="">-- Chọn đơn vị --</option>
                                                        <c:forEach var="unit" items="${units}">
                                                            <option value="${unit.settingID}"
                                                                    <c:if test="${unit.settingID eq detail.unitID}">selected</c:if>>
                                                                ${unit.value}
                                                            </option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="col-md-2">
                                                <label style="display: block;">&nbsp;</label>
                                                <button type="button" class="btn btn-danger btn-block" onclick="removeDetail(this)">
                                                    <i class="fa fa-trash"></i> Xóa
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <!-- Template row for new PO -->
                                <div class="detail-row">
                                    <div class="row">
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label>
                                                    <i class="fa fa-cube"></i> Nguyên liệu 
                                                    <span class="required-star">*</span>
                                                </label>
                                                <select class="form-control ingredient-select" name="ingredientID[]" required>
                                                    <option value="">-- Chọn nguyên liệu --</option>
                                                    <c:forEach var="ingredient" items="${ingredients}">
                                                        <option value="${ingredient.ingredientID}" 
                                                                data-supplier-id="${ingredient.supplierID}"
                                                                data-unit-id="${ingredient.unitID}">
                                                            ${ingredient.name}
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="form-group">
                                                <label>
                                                    <i class="fa fa-calculator"></i> Số lượng 
                                                    <span class="required-star">*</span>
                                                </label>
                                                <input type="number" class="form-control" name="quantity[]" step="0.01" min="0.01" placeholder="Nhập số lượng" required>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="form-group">
                                                <label>
                                                    <i class="fa fa-balance-scale"></i> Đơn vị 
                                                    <span class="required-star">*</span>
                                                </label>
                                                <select class="form-control unit-select" name="unitID[]" required>
                                                    <option value="">-- Chọn đơn vị --</option>
                                                    <c:forEach var="unit" items="${units}">
                                                        <option value="${unit.settingID}">${unit.value}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-2">
                                            <label style="display: block;">&nbsp;</label>
                                            <button type="button" class="btn btn-danger btn-block" onclick="removeDetail(this)">
                                                <i class="fa fa-trash"></i> Xóa
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    
                    <button type="button" class="btn btn-success" onclick="addDetail()">
                        <i class="fa fa-plus"></i> Thêm nguyên liệu
                    </button>
                </div>
            </div>
            
            <!-- Action Buttons -->
            <div class="box" style="margin-top: 20px;">
                <div class="box-body">
                    <div class="row">
                        <div class="col-xs-6">
                            <a href="${pageContext.request.contextPath}/purchase-order?action=list" class="btn btn-default btn-lg btn-block">
                                <i class="fa fa-arrow-left"></i> Quay lại
                            </a>
                        </div>
                        <div class="col-xs-6">
                            <button type="submit" class="btn btn-primary btn-lg btn-block">
                                <i class="fa fa-save"></i> ${po != null ? 'Cập nhật đơn hàng' : 'Tạo đơn hàng'}
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>

<!-- Bootstrap 3.3.6 -->
<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
<!-- AdminLTE App -->
<script src="${pageContext.request.contextPath}/dist/js/app.min.js"></script>

<script>
    let detailRowTemplate;
    
    document.addEventListener('DOMContentLoaded', function() {
        // Store template for adding new rows BEFORE any initialization
        const detailRows = document.querySelectorAll('#detailsContainer .detail-row');
        if (detailRows.length > 0) {
            detailRowTemplate = detailRows[detailRows.length - 1].outerHTML;
        }
        
        // Force set selected values for ingredient dropdowns using data attribute
        const ingredientSelects = document.querySelectorAll('.ingredient-select');
        ingredientSelects.forEach(function(select) {
            const selectedId = select.getAttribute('data-selected-id');
            
            if (selectedId) {
                const selectedIdStr = String(selectedId);
                const targetOption = select.querySelector('option[value="' + selectedIdStr + '"]');
                
                if (targetOption) {
                    select.value = selectedIdStr;
                }
            }
        });
        
        // Force set selected values for unit dropdowns
        const unitSelects = document.querySelectorAll('.unit-select');
        unitSelects.forEach(function(select) {
            const selectedOption = select.querySelector('option[selected]');
            
            if (selectedOption) {
                const selectedValue = selectedOption.value;
                select.value = selectedValue;
            }
        });
        
        // Filter ingredients when supplier changes
        const supplierSelect = document.getElementById('supplierID');
        if (supplierSelect) {
            supplierSelect.addEventListener('change', function() {
                filterIngredientsBySupplier();
            });
        }
        
        // Auto-select unit when ingredient is selected (using event delegation)
        document.addEventListener('change', function(e) {
            if (e.target.classList.contains('ingredient-select')) {
                const ingredientSelect = e.target;
                const row = ingredientSelect.closest('.detail-row');
                const unitSelect = row.querySelector('.unit-select');
                const selectedOption = ingredientSelect.options[ingredientSelect.selectedIndex];
                const unitID = selectedOption ? selectedOption.getAttribute('data-unit-id') : null;
                
                if (unitID && parseInt(unitID) > 0) {
                    unitSelect.value = unitID;
                    console.log('Auto-selected unit:', unitID, 'for ingredient:', ingredientSelect.value);
                } else {
                    if (!unitSelect.value) {
                        unitSelect.value = '';
                    }
                }
            }
        });
    });
    
    function filterIngredientsBySupplier() {
        const supplierSelect = document.getElementById('supplierID');
        const selectedSupplierID = supplierSelect ? supplierSelect.value : null;
        
        // Loop through all ingredient selects
        const ingredientSelects = document.querySelectorAll('.ingredient-select');
        ingredientSelects.forEach(function(select) {
            const currentValue = select.value;
            const options = select.querySelectorAll('option');
            
            // Show/hide options based on supplier
            options.forEach(function(option) {
                const optionSupplierID = option.getAttribute('data-supplier-id');
                
                if (!optionSupplierID || !selectedSupplierID) {
                    // Show all if no supplier selected or it's the placeholder
                    option.disabled = false;
                    option.style.display = '';
                } else if (optionSupplierID == selectedSupplierID) {
                    // Show if matches selected supplier
                    option.disabled = false;
                    option.style.display = '';
                } else {
                    // Hide if doesn't match
                    option.disabled = true;
                    option.style.display = 'none';
                    // Clear selection if current value doesn't match supplier
                    if (option.value == currentValue) {
                        select.value = '';
                    }
                }
            });
        });
    }
    
    function addDetail() {
        const container = document.getElementById('detailsContainer');
        if (!detailRowTemplate) {
            alert('Không thể thêm dòng mới. Vui lòng tải lại trang.');
            return;
        }
        
        // Create new row from template
        const tempDiv = document.createElement('div');
        tempDiv.innerHTML = detailRowTemplate;
        const newRow = tempDiv.firstElementChild;
        
        // Clear values for new row
        const selects = newRow.querySelectorAll('select');
        selects.forEach(function(select) {
            select.value = '';
        });
        
        const inputs = newRow.querySelectorAll('input');
        inputs.forEach(function(input) {
            if (input.type !== 'hidden') {
                input.value = '';
            }
        });
        
        container.appendChild(newRow);
        
        // Apply supplier filter to new row
        filterIngredientsBySupplier();
        
        // Add event listener for auto-select unit when ingredient changes
        const newIngredientSelect = newRow.querySelector('.ingredient-select');
        if (newIngredientSelect) {
            newIngredientSelect.addEventListener('change', function() {
                const ingredientSelect = this;
                const row = ingredientSelect.closest('.detail-row');
                const unitSelect = row.querySelector('.unit-select');
                const selectedOption = ingredientSelect.options[ingredientSelect.selectedIndex];
                const unitID = selectedOption ? selectedOption.getAttribute('data-unit-id') : null;
                
                if (unitID && parseInt(unitID) > 0) {
                    unitSelect.value = unitID;
                    console.log('Auto-selected unit:', unitID, 'for ingredient:', ingredientSelect.value);
                } else {
                    unitSelect.value = '';
                }
            });
        }
    }
    
    function removeDetail(button) {
        const container = document.getElementById('detailsContainer');
        const rows = container.querySelectorAll('.detail-row');
        
        if (rows.length > 1) {
            const detailRow = button.closest('.detail-row');
            if (detailRow) {
                detailRow.remove();
            }
        } else {
            alert('Phải có ít nhất 1 nguyên liệu trong đơn hàng!');
        }
    }
    
    // Form validation
    document.addEventListener('DOMContentLoaded', function() {
        const poForm = document.getElementById('poForm');
        if (poForm) {
            poForm.addEventListener('submit', function(e) {
                const ingredientSelects = document.querySelectorAll('select[name="ingredientID[]"]');
                const quantities = document.querySelectorAll('input[name="quantity[]"]');
                const unitSelects = document.querySelectorAll('select[name="unitID[]"]');
                
                if (ingredientSelects.length > 0) {
                    for (let i = 0; i < ingredientSelects.length; i++) {
                        if (!ingredientSelects[i].value) {
                            e.preventDefault();
                            alert('Vui lòng chọn nguyên liệu cho tất cả các dòng!');
                            ingredientSelects[i].focus();
                            return;
                        }
                        
                        if (!quantities[i].value || parseFloat(quantities[i].value) <= 0) {
                            e.preventDefault();
                            alert('Số lượng phải lớn hơn 0!');
                            quantities[i].focus();
                            return;
                        }
                        
                        if (!unitSelects[i].value) {
                            e.preventDefault();
                            alert('Vui lòng chọn đơn vị cho tất cả các dòng!');
                            unitSelects[i].focus();
                            return;
                        }
                    }
                    
                    // Check for duplicate ingredients
                    const selectedIngredients = Array.from(ingredientSelects).map(s => s.value);
                    const uniqueIngredients = new Set(selectedIngredients);
                    if (selectedIngredients.length !== uniqueIngredients.size) {
                        e.preventDefault();
                        alert('Không được chọn trùng nguyên liệu!');
                        return;
                    }
                }
            });
        }
    });
</script>

</body>
</html>

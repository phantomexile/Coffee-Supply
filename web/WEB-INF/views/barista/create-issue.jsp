<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Tạo yêu nguyên liệu - Coffee Shop Management</title>
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.6 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
    <!-- AdminLTE -->
    <link rel="stylesheet" href="https://adminlte.io/themes/AdminLTE/dist/css/AdminLTE.min.css">
    <link rel="stylesheet" href="https://adminlte.io/themes/AdminLTE/dist/css/skins/_all-skins.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar-custom.css">
    <style>
        .ingredient-row {
            padding: 10px;
            border-bottom: 1px solid #eee;
        }
        .ingredient-row:hover {
            background-color: #f5f5f5;
        }
        .quantity-input {
            width: 120px;
            display: inline-block;
        }
        .info-box {
            background-color: #f9f9f9;
            padding: 15px;
            border-radius: 4px;
            margin-bottom: 20px;
        }
        .info-row {
            margin-bottom: 10px;
        }
        .info-label {
            font-weight: bold;
            display: inline-block;
            width: 150px;
        }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

    <!-- Include Header -->
    <%@include file="../compoment/header.jsp" %>
    
    <!-- Include Sidebar -->
    <%@include file="../compoment/sidebar.jsp" %>

    <!-- Content Wrapper -->
    <div class="content-wrapper">
        <!-- Content Header -->
        <section class="content-header">
            <h1>
                Tạo yêu cầu nguyên liệu
                <small>Báo cáo nhập nguyên liệu</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${pageContext.request.contextPath}/barista/dashboard"><i class="fa fa-dashboard"></i> Trang chủ</a></li>
                <li><a href="${pageContext.request.contextPath}/barista/issues">Vấn đề nhập kho</a></li>
                <li class="active">Tạo yêu cầu</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-md-10 col-md-offset-1">
                    <div class="box box-primary">
                        <div class="box-header with-border">
                            <h3 class="box-title">
                                <i class="fa fa-exclamation-triangle"></i> Thông tin yêu nhập nguyên liệu
                            </h3>
                        </div>
                        
                        <!-- Error Message -->
                        <c:if test="${not empty sessionScope.errorMessage}">
                            <div class="alert alert-danger alert-dismissible" style="margin: 15px;">
                                <button type="button" class="close" data-dismiss="alert">&times;</button>
                                <i class="icon fa fa-ban"></i> ${sessionScope.errorMessage}
                            </div>
                            <c:remove var="errorMessage" scope="session"/>
                        </c:if>
                        
                        <form method="post" action="${pageContext.request.contextPath}/barista/create-issue" id="createIssueForm">
                            <div class="box-body">
                                <div class="callout callout-info">
                                    <h4><i class="fa fa-info-circle"></i> Lưu ý</h4>
                                    <p>Yêu cầu sẽ được gửi đến Inventory Staff để phê duyệt. Vui lòng điền đầy đủ thông tin.</p>
                                </div>
                                
                                <!-- Supplier Selection -->
                                <div class="form-group">
                                    <label for="supplierId">
                                        Tên nhà cung cấp <span class="text-danger">*</span>
                                    </label>
                                    <select class="form-control" id="supplierId" name="supplierId" required>
                                        <option value="">-- Chọn nhà cung cấp --</option>
                                        <c:forEach var="supplier" items="${suppliers}">
                                            <option value="${supplier.supplierID}">${supplier.supplierName}</option>
                                        </c:forEach>
                                    </select>
                                    <p class="help-block">Chọn nhà cung cấp để hiển thị đúng danh sách nguyên liệu</p>
                                </div>
                                
                                <!-- Requester Info -->
                                <div class="info-box">
                                    <div class="info-row">
                                        <span class="info-label">Người yêu cầu:</span>
                                        <span>${currentUser.fullName}</span>
                                    </div>
                                    <div class="info-row">
                                        <span class="info-label">Thời gian yêu cầu:</span>
                                        <span><fmt:formatDate value="${currentTime}" pattern="dd/MM/yyyy HH:mm"/></span>
                                    </div>
                                </div>
                                
                                <!-- Description/Notes -->
                                <div class="form-group">
                                    <label for="description">
                                        Ghi chú <span class="text-danger">*</span>
                                    </label>
                                    <textarea class="form-control" 
                                              id="description" 
                                              name="description" 
                                              rows="4" 
                                              maxlength="500"
                                              placeholder="Nhập ghi chú về yêu cầu nhập kho..."
                                              required></textarea>
                                    <p class="help-block">
                                        Mô tả chi tiết về yêu cầu nhập kho (tối đa 500 ký tự)
                                        <span id="charCount" class="pull-right text-muted">0/500</span>
                                    </p>
                                </div>
                                
                                <!-- Hidden issue type -->
                                <input type="hidden" name="issueType" value="RESTOCK_REQUEST">
                                
                                <!-- Ingredients List -->
                                <div class="form-group">
                                    <label>Danh sách nguyên liệu <span class="text-danger">*</span></label>
                                    <p class="help-block">Chọn các nguyên liệu cần nhập kho và nhập số lượng cho mỗi nguyên liệu</p>
                                    
                                    <div class="box box-info" style="margin-top: 10px;">
                                        <div class="box-header">
                                            <h3 class="box-title">Chọn nguyên liệu</h3>
                                        </div>
                                        <div class="box-body" style="max-height: 400px; overflow-y: auto;">
                                            <table class="table table-bordered table-hover">
                                                <thead>
                                                    <tr>
                                                        <th style="width: 50px;">
                                                            <input type="checkbox" id="selectAll" title="Chọn tất cả" disabled>
                                                        </th>
                                                        <th>Nguyên liệu</th>
                                                        <th>Số lượng tồn kho</th>
                                                        <th>Đơn vị</th>
                                                        <th>Số lượng yêu cầu</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="ingredient" items="${ingredients}">
                                                        <tr class="ingredient-row" data-supplier-id="${ingredient.supplierID}" style="display: none;">
                                                            <td>
                                                                <input type="checkbox" 
                                                                       class="ingredient-checkbox" 
                                                                       name="selectedIngredients" 
                                                                       value="${ingredient.ingredientID}"
                                                                       data-ingredient-id="${ingredient.ingredientID}">
                                                            </td>
                                                            <td><strong>${ingredient.name}</strong></td>
                                                            <td>${ingredient.stockQuantity}</td>
                                                            <td>${ingredient.unitName != null ? ingredient.unitName : 'N/A'}</td>
                                                            <td>
                                                                <input type="number" 
                                                                       class="form-control quantity-input quantity-field" 
                                                                       name="quantity_${ingredient.ingredientID}" 
                                                                       id="quantity_${ingredient.ingredientID}"
                                                                       min="0.01" 
                                                                       step="0.01" 
                                                                       placeholder="0.00"
                                                                       disabled
                                                                       style="display: none;">
                                                                <span class="quantity-placeholder text-muted">-</span>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                    <c:if test="${not empty ingredients}">
                                                        <tr id="supplierPromptRow">
                                                            <td colspan="5" class="text-center text-muted">
                                                                Vui lòng chọn nhà cung cấp để xem nguyên liệu tương ứng
                                                            </td>
                                                        </tr>
                                                        <tr id="emptySupplierRow" style="display: none;">
                                                            <td colspan="5" class="text-center text-warning">
                                                                Nhà cung cấp này chưa có nguyên liệu nào
                                                            </td>
                                                        </tr>
                                                    </c:if>
                                                    <c:if test="${empty ingredients}">
                                                        <tr>
                                                            <td colspan="5" class="text-center">
                                                                <em>Không có nguyên liệu nào trong hệ thống</em>
                                                            </td>
                                                        </tr>
                                                    </c:if>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                                
                                <div class="callout callout-warning">
                                    <p><i class="fa fa-clock-o"></i> <strong>Trạng thái:</strong> Yêu cầu sẽ ở trạng thái "Chờ xử lý" và chờ Inventory Staff phê duyệt</p>
                                </div>
                            </div>
                            
                            <div class="box-footer">
                                <a href="${pageContext.request.contextPath}/barista/issues" class="btn btn-default">
                                    <i class="fa fa-arrow-left"></i> Quay lại
                                </a>
                                <button type="submit" class="btn btn-primary pull-right" id="submitBtn">
                                    <i class="fa fa-paper-plane"></i> Gửi yêu cầu
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    </div>
    <!-- /.content-wrapper -->
    
    <!-- Include Footer -->
    <%@include file="../compoment/footer.jsp" %>
</div>
<!-- ./wrapper -->

<!-- Bootstrap 3.3.6 (jQuery still needed for Bootstrap) -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
<!-- AdminLTE App -->
<script src="https://adminlte.io/themes/AdminLTE/dist/js/app.min.js"></script>

<script>
    // Vanilla JavaScript - No jQuery dependency for custom logic
    document.addEventListener('DOMContentLoaded', function() {
        const selectAll = document.getElementById('selectAll');
        const supplierDropdown = document.getElementById('supplierId');
        const ingredientRows = document.querySelectorAll('.ingredient-row');
        const supplierPromptRow = document.getElementById('supplierPromptRow');
        const emptySupplierRow = document.getElementById('emptySupplierRow');

        function toggleQuantityField(checkbox) {
            const ingredientId = checkbox.getAttribute('data-ingredient-id');
            const quantityField = document.getElementById('quantity_' + ingredientId);
            const placeholder = quantityField.nextElementSibling;

            if (checkbox.checked) {
                quantityField.style.display = 'inline-block';
                quantityField.disabled = false;
                quantityField.focus();
                if (placeholder) placeholder.style.display = 'none';
            } else {
                quantityField.style.display = 'none';
                quantityField.disabled = true;
                quantityField.value = '';
                if (placeholder) placeholder.style.display = 'inline';
            }
        }

        function resetSelections() {
            const checkboxes = document.querySelectorAll('.ingredient-checkbox');
            checkboxes.forEach(function(checkbox) {
                checkbox.checked = false;
                toggleQuantityField(checkbox);
            });
            selectAll.checked = false;
        }

        function updateSelectAllState() {
            const visibleCheckboxes = Array.from(document.querySelectorAll('.ingredient-row'))
                .filter(row => row.style.display !== 'none')
                .map(row => row.querySelector('.ingredient-checkbox'))
                .filter(cb => cb !== null);
            
            if (visibleCheckboxes.length === 0) {
                selectAll.checked = false;
                return;
            }
            
            const checkedVisible = visibleCheckboxes.filter(cb => cb.checked).length;
            selectAll.checked = (checkedVisible === visibleCheckboxes.length);
        }

        function filterIngredientsBySupplier(supplierId) {
            let visibleCount = 0;
            
            ingredientRows.forEach(function(row) {
                const rowSupplierId = row.getAttribute('data-supplier-id') 
                    ? row.getAttribute('data-supplier-id').toString() 
                    : '';
                
                if (supplierId && rowSupplierId === supplierId) {
                    row.style.display = '';
                    visibleCount++;
                } else {
                    row.style.display = 'none';
                }
            });

            if (!supplierId) {
                if (supplierPromptRow) supplierPromptRow.style.display = '';
                if (emptySupplierRow) emptySupplierRow.style.display = 'none';
                selectAll.disabled = true;
                selectAll.checked = false;
            } else if (visibleCount === 0) {
                if (supplierPromptRow) supplierPromptRow.style.display = 'none';
                if (emptySupplierRow) emptySupplierRow.style.display = '';
                selectAll.disabled = true;
                selectAll.checked = false;
            } else {
                if (supplierPromptRow) supplierPromptRow.style.display = 'none';
                if (emptySupplierRow) emptySupplierRow.style.display = 'none';
                selectAll.disabled = false;
            }

            updateSelectAllState();
        }

        // Handle checkbox change - show/hide quantity input and update select all state
        const ingredientCheckboxes = document.querySelectorAll('.ingredient-checkbox');
        ingredientCheckboxes.forEach(function(checkbox) {
            checkbox.addEventListener('change', function() {
                toggleQuantityField(this);
                updateSelectAllState();
            });
        });

        // Handle select all checkbox
        selectAll.addEventListener('change', function() {
            const isChecked = this.checked;
            const visibleRows = Array.from(ingredientRows).filter(row => row.style.display !== 'none');
            
            visibleRows.forEach(function(row) {
                const checkbox = row.querySelector('.ingredient-checkbox');
                if (checkbox) {
                    checkbox.checked = isChecked;
                    toggleQuantityField(checkbox);
                }
            });
            updateSelectAllState();
        });

        // Filter ingredients whenever supplier changes
        supplierDropdown.addEventListener('change', function() {
            resetSelections();
            const selectedSupplier = this.value;
            filterIngredientsBySupplier(selectedSupplier);
        });

        // Initial filter on page load
        filterIngredientsBySupplier(supplierDropdown.value);

        // Character counter for description
        const descriptionField = document.getElementById('description');
        const charCount = document.getElementById('charCount');
        
        descriptionField.addEventListener('input', function() {
            const currentLength = this.value.length;
            const maxLength = 500;
            charCount.textContent = currentLength + '/' + maxLength;
            
            charCount.classList.remove('text-muted', 'text-warning', 'text-danger');
            
            if (currentLength > maxLength * 0.9 && currentLength < maxLength) {
                charCount.classList.add('text-warning');
            } else if (currentLength >= maxLength) {
                charCount.classList.add('text-danger');
            } else {
                charCount.classList.add('text-muted');
            }
        });

        // Form validation before submit
        const createIssueForm = document.getElementById('createIssueForm');
        createIssueForm.addEventListener('submit', function(e) {
            const visibleRows = Array.from(ingredientRows).filter(row => row.style.display !== 'none');
            const selectedIngredients = visibleRows
                .map(row => row.querySelector('.ingredient-checkbox'))
                .filter(cb => cb && cb.checked);

            if (selectedIngredients.length === 0) {
                e.preventDefault();
                alert('Vui lòng chọn ít nhất một nguyên liệu');
                return false;
            }

            // Validate description length
            const description = descriptionField.value.trim();
            if (description.length === 0) {
                e.preventDefault();
                alert('Vui lòng nhập ghi chú');
                descriptionField.focus();
                return false;
            }
            if (description.length > 500) {
                e.preventDefault();
                alert('Ghi chú không được vượt quá 500 ký tự');
                descriptionField.focus();
                return false;
            }

            let hasError = false;
            for (let i = 0; i < selectedIngredients.length; i++) {
                const checkbox = selectedIngredients[i];
                const ingredientId = checkbox.getAttribute('data-ingredient-id');
                const quantityField = document.getElementById('quantity_' + ingredientId);
                const quantity = quantityField.value;

                if (!quantity || quantity.trim() === '' || parseFloat(quantity) <= 0) {
                    hasError = true;
                    alert('Vui lòng nhập số lượng hợp lệ (lớn hơn 0) cho nguyên liệu ID: ' + ingredientId);
                    quantityField.focus();
                    break;
                }
            }

            if (hasError) {
                e.preventDefault();
                return false;
            }

            // Enable all quantity fields before submit
            selectedIngredients.forEach(function(checkbox) {
                const ingredientId = checkbox.getAttribute('data-ingredient-id');
                const quantityField = document.getElementById('quantity_' + ingredientId);
                quantityField.disabled = false;
            });
        });

        // Auto dismiss alerts after 5 seconds
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

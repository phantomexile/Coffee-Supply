<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>Quản lý nguyên liệu - Coffee Shop</title>
        <!-- Tell the browser to be responsive to screen width -->
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
        <style>
            .ingredient-card {
                border: 1px solid #e0e0e0;
                border-radius: 8px;
                transition: all 0.3s ease;
                background: #fff;
            }

            .ingredient-card:hover {
                box-shadow: 0 4px 12px rgba(0,0,0,0.1);
                transform: translateY(-2px);
            }

            .stock-low {
                background-color: #fff5f5;
                
            }

            .stock-medium {
                background-color: #fffbf0;
                
            }

            .stock-good {
                background-color: #f0fff4;
                
            }

            .filter-section {
                background: #f8f9fa;
                padding: 20px;
                border-radius: 8px;
                margin-bottom: 20px;
            }

            .action-buttons .btn {
                margin-right: 5px;
                margin-bottom: 5px;
            }
            
            /* Remove orange line from STT column */
            table.table thead th:first-child,
            table.table tbody td:first-child {
                border-left: none !important;
            }
        </style>
    </head>
    <body class="hold-transition skin-blue sidebar-mini">
        <div class="wrapper">
            <%@include file="../compoment/sidebar.jsp" %>
            <%@include file="../compoment/header.jsp" %>

            <div class="content-wrapper">
                <section class="content-header">
                    <h1>
                        Quản lý nguyên liệu
                        <small>Danh sách tất cả nguyên liệu</small>
                    </h1>
                    <ol class="breadcrumb">
                        <li><a href="${pageContext.request.contextPath}/inventory/dashboard"><i class="fa fa-dashboard"></i> Dashboard</a></li>
                        <li class="active">Quản lý nguyên liệu</li>
                    </ol>
                </section>

                <section class="content">
                    <!-- Alert Messages -->
                    <c:if test="${not empty param.success}">
                        <div class="alert alert-success alert-dismissible">
                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                            <h4><i class="icon fa fa-check"></i> Thành công!</h4>
                            ${param.success}
                        </div>
                    </c:if>



                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible">
                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                            <h4><i class="icon fa fa-ban"></i> Lỗi!</h4>
                            ${error}
                        </div>
                    </c:if>

                    <!-- Filter and Search Section -->
                    <div class="filter-section">
                        <form method="GET" action="${pageContext.request.contextPath}/ingredient">
                            <div class="row">
                                <div class="col-md-3">
                                    <div class="form-group">
                                        <label>Tìm kiếm:</label>
                                        <input type="text" name="search" class="form-control" 
                                               placeholder="Tên nguyên liệu..." 
                                               value="${searchKeyword}">
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="form-group">
                                        <label>Nhà Cung Cấp:</label>
                                        <select name="supplier" class="form-control">
                                            <option value="">Tất cả nhà cung cấp</option>
                                            <c:forEach var="supplier" items="${suppliers}">
                                                <option value="${supplier.supplierID}" 
                                                        ${supplierFilter == supplier.supplierID ? 'selected' : ''}>
                                                    ${supplier.supplierName}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-md-2">
                                    <div class="form-group">
                                        <label>Trạng thái:</label>
                                        <select name="active" class="form-control">
                                            <option value="" ${activeOnly == null ? 'selected' : ''}>Tất cả</option>
                                            <option value="true" ${activeOnly == true ? 'selected' : ''}>Hoạt động</option>
                                            <option value="false" ${activeOnly == false ? 'selected' : ''}>Ngừng hoạt động</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-md-2">
                                    <div class="form-group">
                                        <label>&nbsp;</label>
                                        <button type="submit" class="btn btn-primary btn-block">
                                            <i class="fa fa-search"></i> Tìm kiếm
                                        </button>
                                    </div>
                                </div>
                                <div class="col-md-2">
                                    <div class="form-group">
                                        <label>&nbsp;</label>
                                        <a href="${pageContext.request.contextPath}/ingredient?action=create" 
                                           class="btn btn-success btn-block">
                                            <i class="fa fa-plus"></i> Thêm mới
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>



                    <!-- Ingredients List -->
                    <div class="row">
                        <div class="col-md-12">
                            <div class="box">
<!--                                <div class="box-header">
                                    <h3 class="box-title">
                                        Danh sách nguyên liệu 
                                        <span class="label label-info">${totalIngredients} kết quả</span>
                                    </h3>
                                </div>-->
                                <div class="box-body">
                                    <c:choose>
                                        <c:when test="${empty ingredients}">
                                            <div class="text-center">
                                                <p class="text-muted">Không có nguyên liệu nào được tìm thấy.</p>
                                                <a href="${pageContext.request.contextPath}/ingredient?action=create" 
                                                   class="btn btn-success">
                                                    <i class="fa fa-plus"></i> Thêm nguyên liệu đầu tiên
                                                </a>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="table-responsive">
                                                <table class="table table-striped table-hover">
                                                    <thead>
                                                        <tr>
                                                            <th>STT</th>
                                                            <th>Tên Nguyên liệu</th>
                                                            <th>Số lượng hàng tồn kho</th>
                                                            <th>Đơn vị</th>
                                                            <th>Nhà cung cấp</th>
                                                            <th>Trạng Thái</th>
                                                            <th>Thao Tác</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach var="ingredient" items="${ingredients}" varStatus="status">
                                                            <tr class="ingredient-row
                                                                <c:choose>
                                                                    <c:when test="${ingredient.stockQuantity lt 10}">stock-low</c:when>
                                                                    <c:when test="${ingredient.stockQuantity lt 50}">stock-medium</c:when>
                                                                    <c:otherwise>stock-good</c:otherwise>
                                                                </c:choose>">
                                                                <td>${(currentPage - 1) * 10 + status.count}</td>
                                                                <td>
                                                                    <strong>${ingredient.name}</strong>
                                                                    <c:if test="${ingredient.stockQuantity lt 10}">
                                                                        <span class="label label-danger">Sắp hết</span>
                                                                    </c:if>
                                                                </td>
                                                                <td>
                                                                    <span class="stock-quantity" data-ingredient-id="${ingredient.ingredientID}">
                                                                        <fmt:formatNumber value="${ingredient.stockQuantity}" pattern="#,##0.##"/>
                                                                    </span>
                                                                    <!--                                                                    <button type="button" class="btn btn-xs btn-link update-stock-btn" 
                                                                                                                                                data-ingredient-id="${ingredient.ingredientID}"
                                                                                                                                                data-ingredient-name="${ingredient.name}"
                                                                                                                                                data-current-quantity="${ingredient.stockQuantity}">
                                                                                                                                            <i class="fa fa-edit"></i>
                                                                                                                                        </button>-->
                                                                    
                                                                </td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${not empty ingredient.unitName}">
                                                                            ${ingredient.unitName}
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            Unit ${ingredient.unitID}
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${not empty ingredient.supplierName}">
                                                                            ${ingredient.supplierName}
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            Supplier ${ingredient.supplierID}
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${ingredient.active}">
                                                                            <span class="label label-success">Hoạt động</span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="label label-danger">Ngừng hoạt động</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                            
                                                                </td>
                                                                <td class="action-buttons">
                                                                    <a href="${pageContext.request.contextPath}/ingredient?action=view&id=${ingredient.ingredientID}" 
                                                                       class="btn btn-xs btn-info" title="Xem chi tiết">
                                                                        <i class="fa fa-eye"></i>
                                                                    </a>
                                                                    <a href="${pageContext.request.contextPath}/ingredient?action=edit&id=${ingredient.ingredientID}" 
                                                                       class="btn btn-xs btn-warning" title="Chỉnh sửa">
                                                                        <i class="fa fa-edit"></i>
                                                                    </a>
                                                                    <button type="button" class="btn ${ingredient.active ? 'btn-danger' : 'btn-success'} btn-xs" 
                                                                            title="${ingredient.active ? 'Vô hiệu hóa' : 'Kích hoạt'}"
                                                                            onclick="toggleActive(${ingredient.ingredientID}, ${ingredient.active})">
                                                                        <i class="fa ${ingredient.active ? 'fa-ban' : 'fa-check'}"></i>
                                                                    </button>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                </table>
                                            </div>

                                            <!-- Pagination -->
                                            <c:if test="${totalPages > 1}">
                                                <div class="text-center">
                                                    <ul class="pagination">
                                                        <c:if test="${currentPage > 1}">
                                                            <li><a href="?page=${currentPage - 1}&search=${searchKeyword}&supplier=${supplierFilter}&active=${activeOnly}">&laquo;</a></li>
                                                            </c:if>

                                                        <c:forEach begin="1" end="${totalPages}" var="page">
                                                            <li class="${page == currentPage ? 'active' : ''}">
                                                                <a href="?page=${page}&search=${searchKeyword}&supplier=${supplierFilter}&active=${activeOnly}">${page}</a>
                                                            </li>
                                                        </c:forEach>

                                                        <c:if test="${currentPage < totalPages}">
                                                            <li><a href="?page=${currentPage + 1}&search=${searchKeyword}&supplier=${supplierFilter}&active=${activeOnly}">&raquo;</a></li>
                                                            </c:if>
                                                    </ul>
                                                </div>
                                            </c:if>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
            </div>
        </div>

        <!-- Quick Stock Update Modal -->
        <div class="modal fade" id="stockUpdateModal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                        <h4 class="modal-title">Cập nhật số lượng tồn kho</h4>
                    </div>
                    <form method="POST" action="${pageContext.request.contextPath}/ingredient">
                        <div class="modal-body">
                            <input type="hidden" name="action" value="update-stock">
                            <input type="hidden" name="ingredientId" id="stockIngredientId">

                            <div class="form-group">
                                <label>Nguyên liệu:</label>
                                <p id="stockIngredientName" class="form-control-static"></p>
                            </div>

                            <div class="form-group">
                                <label>Số lượng hiện tại:</label>
                                <p id="stockCurrentQuantity" class="form-control-static"></p>
                            </div>

                            <div class="form-group">
                                <label for="newQuantity">Số lượng mới: <span class="text-danger">*</span></label>
                                <input type="number" step="0.01" min="0" name="newQuantity" 
                                       id="newQuantity" class="form-control" required>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Hủy</button>
                            <button type="submit" class="btn btn-primary">Cập nhật</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Delete Confirmation Modal -->
        <div class="modal fade" id="deleteModal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                        <h4 class="modal-title">Xác nhận xóa</h4>
                    </div>
                    <div class="modal-body">
                        <p>Bạn có chắc chắn muốn xóa nguyên liệu <strong id="deleteIngredientName"></strong>?</p>
                        <p class="text-muted">Hành động này sẽ đặt nguyên liệu thành trạng thái không hoạt động.</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Hủy</button>
                        <form method="POST" action="${pageContext.request.contextPath}/ingredient" style="display: inline;">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="id" id="deleteIngredientId">
                            <button type="submit" class="btn btn-danger">XÃ³a</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Bootstrap 3.3.6 -->
        <script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
        <!-- AdminLTE App -->
        <script src="${pageContext.request.contextPath}/dist/js/app.min.js"></script>
        <script>
            // Event handlers for buttons with data attributes
            document.addEventListener('DOMContentLoaded', function() {
                const updateStockButtons = document.querySelectorAll('.update-stock-btn');
                updateStockButtons.forEach(function(btn) {
                    btn.addEventListener('click', function() {
                        const ingredientId = this.getAttribute('data-ingredient-id');
                        const ingredientName = this.getAttribute('data-ingredient-name');
                        const currentQuantity = this.getAttribute('data-current-quantity');
                        quickUpdateStock(ingredientId, ingredientName, currentQuantity);
                    });
                });

                const deleteButtons = document.querySelectorAll('.delete-btn');
                deleteButtons.forEach(function(btn) {
                    btn.addEventListener('click', function() {
                        const ingredientId = this.getAttribute('data-ingredient-id');
                        const ingredientName = this.getAttribute('data-ingredient-name');
                        confirmDelete(ingredientId, ingredientName);
                    });
                });
            });

            function quickUpdateStock(ingredientId, ingredientName, currentQuantity) {
                document.getElementById('stockIngredientId').value = ingredientId;
                document.getElementById('stockIngredientName').textContent = ingredientName;
                document.getElementById('stockCurrentQuantity').textContent = currentQuantity;
                document.getElementById('newQuantity').value = currentQuantity;
                
                // Show modal using Bootstrap's vanilla JS API
                const modal = document.getElementById('stockUpdateModal');
                if (modal && typeof bootstrap !== 'undefined') {
                    const bsModal = new bootstrap.Modal(modal);
                    bsModal.show();
                } else if (modal) {
                    // Fallback for Bootstrap 3
                    modal.style.display = 'block';
                    document.body.classList.add('modal-open');
                    const backdrop = document.createElement('div');
                    backdrop.className = 'modal-backdrop fade in';
                    document.body.appendChild(backdrop);
                }
            }

            function confirmDelete(ingredientId, ingredientName) {
                document.getElementById('deleteIngredientId').value = ingredientId;
                document.getElementById('deleteIngredientName').textContent = ingredientName;
                
                // Show modal using Bootstrap's vanilla JS API
                const modal = document.getElementById('deleteModal');
                if (modal && typeof bootstrap !== 'undefined') {
                    const bsModal = new bootstrap.Modal(modal);
                    bsModal.show();
                } else if (modal) {
                    // Fallback for Bootstrap 3
                    modal.style.display = 'block';
                    document.body.classList.add('modal-open');
                    const backdrop = document.createElement('div');
                    backdrop.className = 'modal-backdrop fade in';
                    document.body.appendChild(backdrop);
                }
            }

            function exportIngredients() {
                // Implementation for export functionality
                alert('Chức năng xuất báo cáo sẽ được triển khai sau');
            }

            function bulkUpdateStock() {
                // Implementation for bulk update functionality
                alert('Chức năng cập nhật hàng loạt sẽ được triển khai sau');
            }

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
            
            function toggleActive(ingredientId, isActive) {
                const action = isActive ? 'vô hiệu hóa' : 'kích hoạt';
                const message = 'Bạn có chắc chắn muốn ' + action + ' nguyên liệu này?';
                
                if (confirm(message)) {
                    window.location.href = '${pageContext.request.contextPath}/ingredient?action=toggle&id=' + ingredientId;
                }
            }
        </script>
    </body>
</html>












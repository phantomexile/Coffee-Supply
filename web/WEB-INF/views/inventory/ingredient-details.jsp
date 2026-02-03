<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Chi tiết nguyên liệu - Coffee Shop</title>
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
        .detail-card {
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }

        .detail-header {
            padding: 20px;
            border-bottom: 1px solid #e0e0e0;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 8px 8px 0 0;
        }

        .detail-body {
            padding: 25px;
        }

        .info-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 12px 0;
            border-bottom: 1px solid #f0f0f0;
        }

        .info-row:last-child {
            border-bottom: none;
        }

        .info-label {
            font-weight: 600;
            color: #4a5568;
            font-size: 14px;
        }

        .info-value {
            color: #2d3748;
            font-size: 14px;
        }

        .stock-status {
            padding: 6px 12px;
            border-radius: 4px;
            font-size: 12px;
            font-weight: 600;
        }

        .stock-low {
            background-color: #fed7d7;
            color: #c53030;
        }

        .stock-medium {
            background-color: #feebc8;
            color: #d69e2e;
        }

        .stock-good {
            background-color: #c6f6d5;
            color: #2f855a;
        }

        .action-section {
            background: #f7fafc;
            padding: 20px;
            border-radius: 0 0 8px 8px;
            text-align: center;
        }

        .stat-item {
            text-align: center;
            padding: 15px;
            background: #f8f9fa;
            border-radius: 6px;
            margin-bottom: 15px;
        }

        .stat-number {
            font-size: 24px;
            font-weight: bold;
            color: #2d3748;
        }

        .stat-label {
            color: #718096;
            font-size: 12px;
            margin-top: 5px;
        }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <%@ include file="../compoment/sidebar.jsp" %>
    <%@ include file="../compoment/header.jsp" %>

    <div class="content-wrapper">
        <section class="content-header">
            <h1>
                Chi tiết nguyên liệu
                <small>Thông tin chi tiết về ${ingredient.name}</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${pageContext.request.contextPath}/inventory/dashboard"><i class="fa fa-dashboard"></i> Dashboard</a></li>
                <li><a href="${pageContext.request.contextPath}/ingredient">Quản lý nguyên liệu</a></li>
                <li class="active">Chi tiết</li>
            </ol>
        </section>

        <section class="content">
            <c:choose>
                <c:when test="${empty ingredient}">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="alert alert-warning">
                                <h4><i class="icon fa fa-warning"></i> Không tìm thấy!</h4>
                                Nguyên liệu không tồn tại hoặc đã bị xóa.
                                <br><br>
                                <a href="${pageContext.request.contextPath}/ingredient" class="btn btn-primary">
                                    <i class="fa fa-arrow-left"></i> Quay lại danh sách
                                </a>
                            </div>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="row">
                        <!-- Main Information -->
                        <div class="col-md-8">
                            <div class="detail-card">
                                <div class="detail-header">
                                    <h3>
                                        <i class="fa fa-cube"></i>
                                        ${ingredient.name}
                                        <c:choose>
                                            <c:when test="${ingredient.active}">
                                                <span class="label label-success pull-right">Hoạt động</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="label label-danger pull-right">Ngừng hoạt động</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </h3>
                                    <p>Mã nguyên liệu: #${ingredient.ingredientID}</p>
                                </div>

                                <div class="detail-body">
                                    <div class="info-row">
                                        <span class="info-label">
                                            <i class="fa fa-tag"></i> Tên nguyên liệu:
                                        </span>
                                        <span class="info-value">${ingredient.name}</span>
                                    </div>

                                    <div class="info-row">
                                        <span class="info-label">
                                            <i class="fa fa-boxes"></i> Số lượng tồn kho:
                                        </span>
                                        <span class="info-value">
                                            <strong>
                                                <fmt:formatNumber value="${ingredient.stockQuantity}" pattern="#,##0.##"/>
                                            </strong>
                                            <span class="stock-status
                                                <c:choose>
                                                    <c:when test="${ingredient.stockQuantity lt 10}">stock-low</c:when>
                                                    <c:when test="${ingredient.stockQuantity lt 50}">stock-medium</c:when>
                                                    <c:otherwise>stock-good</c:otherwise>
                                                </c:choose>">
                                                <c:choose>
                                                    <c:when test="${ingredient.stockQuantity lt 10}">Sắp hết</c:when>
                                                    <c:when test="${ingredient.stockQuantity lt 50}">Ít</c:when>
                                                    <c:otherwise>Đủ</c:otherwise>
                                                </c:choose>
                                            </span>
                                        </span>
                                    </div>

                                    <div class="info-row">
                                        <span class="info-label">
                                            <i class="fa fa-ruler"></i> Đơn vị tính:
                                        </span>
                                        <span class="info-value">Unit ID: ${ingredient.unitID}</span>
                                    </div>

                                    <div class="info-row">
                                        <span class="info-label">
                                            <i class="fa fa-truck"></i> Nhà cung cấp:
                                        </span>
                                        <span class="info-value">Supplier ID: ${ingredient.supplierID}</span>
                                    </div>

<!--                                    <div class="info-row">
                                        <span class="info-label">
                                            <i class="fa fa-calendar"></i> Ngày tạo:
                                        </span>
                                        <span class="info-value">
                                            <fmt:formatDate value="${ingredient.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                        </span>
                                    </div>-->

                                    <div class="info-row">
                                        <span class="info-label">
                                            <i class="fa fa-toggle-on"></i> Trạng thái:
                                        </span>
                                        <span class="info-value">
                                            <c:choose>
                                                <c:when test="${ingredient.active}">
                                                    <span class="label label-success">Đang hoạt động</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="label label-danger">Ngừng hoạt động</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </span>
                                    </div>
                                </div>

                                <div class="action-section">
<!--                                    <a href="${pageContext.request.contextPath}/ingredient?action=edit&id=${ingredient.ingredientID}"
                                       class="btn btn-warning btn-lg">
                                        <i class="fa fa-edit"></i> Chỉnh sửa
                                    </a>-->

<!--                                    <button type="button" class="btn btn-info btn-lg update-stock-btn"
                                            data-ingredient-id="${ingredient.ingredientID}"
                                            data-ingredient-name="${ingredient.name}"
                                            data-current-quantity="${ingredient.stockQuantity}">
                                        <i class="fa fa-warehouse"></i> Cập nhật tồn kho
                                    </button>-->

                                 

                                    <a href="${pageContext.request.contextPath}/ingredient"
                                       class="btn btn-default btn-lg">
                                        <i class="fa fa-arrow-left"></i> Quay lại
                                    </a>
                                </div>
                            </div>
                        </div>

                       
                    </div>
                </c:otherwise>
            </c:choose>
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
                <p class="text-muted">Hành động này sẽ đặt nguyên liệu về trạng thái không hoạt động.</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Hủy</button>
                <form method="POST" action="${pageContext.request.contextPath}/ingredient" style="display: inline;">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" id="deleteIngredientId">
                    <button type="submit" class="btn btn-danger">Xóa</button>
                </form>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/dist/js/app.min.js"></script>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const updateStockButtons = document.querySelectorAll('.update-stock-btn');
        updateStockButtons.forEach(function(btn) {
            btn.addEventListener('click', function() {
                const id = this.getAttribute('data-ingredient-id');
                const name = this.getAttribute('data-ingredient-name');
                const qty = this.getAttribute('data-current-quantity');
                quickUpdateStock(id, name, qty);
            });
        });

        const deleteButtons = document.querySelectorAll('.delete-btn');
        deleteButtons.forEach(function(btn) {
            btn.addEventListener('click', function() {
                const id = this.getAttribute('data-ingredient-id');
                const name = this.getAttribute('data-ingredient-name');
                confirmDelete(id, name);
            });
        });
    });

    function quickUpdateStock(id, name, qty) {
        document.getElementById('stockIngredientId').value = id;
        document.getElementById('stockIngredientName').textContent = name;
        document.getElementById('stockCurrentQuantity').textContent = qty;
        document.getElementById('newQuantity').value = qty;
        
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

    function confirmDelete(id, name) {
        document.getElementById('deleteIngredientId').value = id;
        document.getElementById('deleteIngredientName').textContent = name;
        
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
</script>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách Đơn hàng - Purchase Orders</title>
    
    <!-- Bootstrap 3.3.6 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/AdminLTE.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/skins/_all-skins.min.css">
    
    <style>
        body {
            background-color: #ecf0f5;
        }
        
        .content-wrapper {
            margin-left: 230px;
            padding: 20px;
            min-height: 100vh;
        }
        
        .status-badge {
            padding: 5px 12px;
            border-radius: 3px;
            font-size: 11px;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.3px;
            display: inline-block;
        }
        
        .status-badge.status-Pending {
            background: #f39c12;
            color: #fff;
        }
        
        .status-badge.status-Approved {
            background: #00c0ef;
            color: #fff;
        }
        
        .status-badge.status-Shipping {
            background: #605ca8;
            color: #fff;
        }
        
        .status-badge.status-Received {
            background: #00a65a;
            color: #fff;
        }
        
        .status-badge.status-Cancelled {
            background: #dd4b39;
            color: #fff;
        }
        
        .box {
            border-radius: 3px;
            box-shadow: 0 1px 1px rgba(0,0,0,0.1);
            border-top: 3px solid #3c8dbc;
        }
        
        .box-header {
            background: #f9f9f9;
            border-bottom: 1px solid #f4f4f4;
            padding: 10px;
        }
        
        .table > thead > tr > th {
            background: #3c8dbc;
            color: white;
            font-weight: 600;
            border: none;
            text-transform: uppercase;
            font-size: 12px;
        }
        
        .table > tbody > tr:hover {
            background-color: #f5f5f5;
        }
        
        .table > tbody > tr > td {
            vertical-align: middle;
        }
        
        .btn-action {
            margin: 2px;
            padding: 5px 10px;
            border-radius: 3px;
        }
        
        .alert {
            border-radius: 3px;
        }
        
        .breadcrumb {
            background: transparent;
            padding: 10px 0;
        }
        
        .form-inline .form-group {
            margin-bottom: 10px;
        }
        
        .form-inline label {
            font-weight: 600;
            color: #333;
        }
        
        .form-inline .form-control {
            border-radius: 3px;
            border: 1px solid #ddd;
        }
        
        .form-inline .form-control:focus {
            border-color: #3c8dbc;
            box-shadow: 0 0 5px rgba(60, 141, 188, 0.3);
        }
        
        .form-inline .btn {
            border-radius: 3px;
            font-weight: 600;
        }
        
        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #999;
        }
        
        .empty-state i {
            font-size: 64px;
            margin-bottom: 20px;
            opacity: 0.3;
        }
        
        /* Sorting Styles */
        .sortable {
            cursor: pointer;
            position: relative;
            padding-right: 20px;
            user-select: none;
        }
        
        .sortable:hover {
            background-color: rgba(0,0,0,0.1);
        }
        
        .sortable .sort-icon {
            position: absolute;
            right: 8px;
            top: 50%;
            transform: translateY(-50%);
            font-size: 12px;
            opacity: 0.5;
        }
        
        .sortable:hover .sort-icon {
            opacity: 0.8;
        }
        
        .sortable.asc .sort-icon,
        .sortable.desc .sort-icon {
            opacity: 1;
        }
        
        .sortable.asc .sort-icon::before {
            content: "\f0de";
        }
        
        .sortable.desc .sort-icon::before {
            content: "\f0dd";
        }
        
        .sortable:not(.asc):not(.desc) .sort-icon::before {
            content: "\f0dc";
        }
        
        /* Pagination Styles */
        .pagination-wrapper {
            margin-top: 20px;
            text-align: center;
        }
        
        .pagination {
            display: inline-block;
            margin: 0;
        }
        
        .pagination > li > a,
        .pagination > li > span {
            border-radius: 3px;
            margin: 0 2px;
        }
        
        .pagination > .active > a {
            background-color: #3c8dbc;
            border-color: #3c8dbc;
        }
        
        .pagination-info {
            margin-top: 10px;
            color: #666;
            font-size: 13px;
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
        <!-- Content Header -->
        <section class="content-header">
            <h1>
                Quản lý Đơn hàng Nguyên liệu
                <small>Danh sách đơn hàng</small>
            </h1>
            <ol class="breadcrumb">
                <c:choose>
                    <c:when test="${sessionScope.roleName == 'Admin'}">
                        <li><a href="${pageContext.request.contextPath}/admin/dashboard"><i class="fa fa-dashboard"></i> Trang chủ</a></li>
                    </c:when>
                    <c:otherwise>
                <li><a href="${pageContext.request.contextPath}/inventory/dashboard"><i class="fa fa-dashboard"></i> Trang chủ</a></li>
                    </c:otherwise>
                </c:choose>
                <li class="active">Danh sách đơn hàng</li>
            </ol>
        </section>
        
        <!-- Main Content -->
        <section class="content">
            <!-- Success/Error Messages -->
            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                    <i class="fa fa-check-circle"></i> ${sessionScope.successMessage}
                </div>
                <c:remove var="successMessage" scope="session"/>
            </c:if>
            
            <c:if test="${not empty sessionScope.errorMessage}">
                <div class="alert alert-danger alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                    <i class="fa fa-exclamation-circle"></i> ${sessionScope.errorMessage}
                </div>
                <c:remove var="errorMessage" scope="session"/>
            </c:if>
        
        <div class="box">
            <div class="box-header with-border">
                <h3 class="box-title"><i class="fa fa-list"></i> Danh sách đơn hàng</h3>
<!--                <div class="box-tools pull-right">
                    <span class="label label-primary">Tổng số: ${totalItems != null ? totalItems : 0} đơn hàng</span>
                </div>-->
            </div>
            <div class="box-body">
                <!-- Filter and Search Section -->
                <div class="row" style="margin-bottom: 20px;">
                    <div class="col-md-12">
                        <form action="${pageContext.request.contextPath}/purchase-order" method="get" class="form-inline">
                            <div class="form-group" style="margin-right: 10px;">
                                <label style="margin-right: 5px;">
                                    <i class="fa fa-search"></i> Tìm kiếm:
                                </label>
                                <input type="text" name="search" class="form-control" 
                                       placeholder="ID" 
                                       value="${currentSearch}" 
                                       style="width: 200px;">
                            </div>
                            
                            <div class="form-group" style="margin-right: 10px;">
                                <label style="margin-right: 5px;">
                                    <i class="fa fa-truck"></i> Nhà cung cấp:
                                </label>
                                <select name="supplier" class="form-control" style="width: 200px;">
                                    <option value="">-- Tất cả --</option>
                                    <c:forEach var="supplier" items="${suppliers}">
                                        <option value="${supplier.supplierID}" 
                                                ${currentSupplier != null && currentSupplier == supplier.supplierID ? 'selected' : ''}>
                                            ${supplier.supplierName}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            
                            <div class="form-group" style="margin-right: 10px;">
                                <label style="margin-right: 5px;">
                                    <i class="fa fa-info-circle"></i> Trạng thái:
                                </label>
                                <select name="status" class="form-control" style="width: 200px;">
                                    <option value="">-- Tất cả --</option>
                                    <c:forEach var="status" items="${statuses}">
                                        <option value="${status.settingID}" 
                                                ${currentStatus != null && currentStatus == status.settingID ? 'selected' : ''}>
                                            ${status.value}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            
                            <button type="submit" class="btn btn-primary">
                                <i class="fa fa-filter"></i> Lọc
                            </button>
                            <a href="${pageContext.request.contextPath}/purchase-order" class="btn btn-default">
                                <i class="fa fa-refresh"></i> Đặt lại
                            </a>
                        </form>
                    </div>
                </div>
                
                <div class="row" style="margin-bottom: 15px;">
                    <div class="col-xs-12">
                        <a href="${pageContext.request.contextPath}/purchase-order?action=new" class="btn btn-primary">
                            <i class="fa fa-plus"></i> Tạo đơn hàng mới
                        </a>
                    </div>
                </div>
                
                <div class="table-responsive">
                    <table class="table table-striped table-hover">
                        <thead>
                            <tr>
                                <th class="sortable ${param.sortBy == 'id' ? param.sortOrder : ''}" data-sort="id">
                                    Mã đơn hàng
                                    <i class="fa sort-icon"></i>
                                </th>
                               
<!--                                class="sortable ${param.sortBy == 'supplier' ? param.sortOrder : ''}" data-sort="supplier"-->
                                <th>
                                    Nhà cung cấp
                                   
                                </th>
                                <th>
                                    Người tạo
                                    
                                </th>
                                <th>
                                    Trạng thái
                                    
                                </th>                          
                                <th style="width: 180px;">Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty poList}">
                                    <tr>
                                        <td colspan="8">
                                            <div class="empty-state">
                                                <i class="fa fa-inbox"></i>
                                                <h4>Chưa có đơn hàng nào</h4>
                                                <p>Nhấn "Tạo đơn hàng mới" để bắt đầu</p>
                                            </div>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="po" items="${poList}">
                                        <tr>
                                            <td><strong style="color: #667eea;">PO-${po.poID}</strong></td>
                                           
                                            <td><i class="fa fa-truck"></i> ${po.supplierName}</td>
                                            <td><i class="fa fa-user"></i> ${po.createdByName}</td>
                                            <td>
                                                <span class="status-badge status-${po.statusName}">${po.statusName}</span>
                                            </td>
        
                                            <td>
                                                <a href="${pageContext.request.contextPath}/purchase-order?action=view&id=${po.poID}" 
                                                   class="btn btn-info btn-xs btn-action" title="Xem chi tiết">
                                                    <i class="fa fa-eye"></i>
                                                </a>
                                                <c:if test="${po.statusID == 20}">
                                                    <a href="${pageContext.request.contextPath}/purchase-order?action=edit&id=${po.poID}" 
                                                       class="btn btn-warning btn-xs btn-action" title="Chỉnh sửa">
                                                        <i class="fa fa-edit"></i>
                                                    </a>
                                                    <button type="button" 
                                                            class="btn btn-danger btn-xs btn-action" 
                                                            title="Xóa"
                                                            onclick="confirmDeletePO(${po.poID}, 'PO-${po.poID}')">
                                                        <i class="fa fa-trash"></i>
                                                    </button>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
                
                <!-- Pagination -->
                <c:if test="${totalPages > 1}">
                    <div class="pagination-wrapper">
                        <ul class="pagination">
                            <!-- Previous button -->
                            <c:if test="${currentPage > 1}">
                                <li>
                                    <a href="?page=${currentPage - 1}&search=${currentSearch}&supplier=${currentSupplier}&status=${currentStatus}&sortBy=${param.sortBy}&sortOrder=${param.sortOrder}">
                                        <i class="fa fa-chevron-left"></i>
                                    </a>
                                </li>
                            </c:if>
                            
                            <!-- Page numbers -->
                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <c:choose>
                                    <c:when test="${currentPage eq i}">
                                        <li class="active">
                                            <span>${i}</span>
                                        </li>
                                    </c:when>
                                    <c:otherwise>
                                        <li>
                                            <a href="?page=${i}&search=${currentSearch}&supplier=${currentSupplier}&status=${currentStatus}&sortBy=${param.sortBy}&sortOrder=${param.sortOrder}">
                                                ${i}
                                            </a>
                                        </li>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                            
                            <!-- Next button -->
                            <c:if test="${currentPage < totalPages}">
                                <li>
                                    <a href="?page=${currentPage + 1}&search=${currentSearch}&supplier=${currentSupplier}&status=${currentStatus}&sortBy=${param.sortBy}&sortOrder=${param.sortOrder}">
                                        <i class="fa fa-chevron-right"></i>
                                    </a>
                                </li>
                            </c:if>
                        </ul>
                        
<!--                        <div class="pagination-info">
                            Hiển thị ${(currentPage - 1) * 5 + 1} - ${(currentPage - 1) * 5 + poList.size()} trong tổng số ${totalItems} đơn hàng
                        </div>-->
                    </div>
                </c:if>
            </div>
        </div>
        </section>
    </div>
</div>

<!-- Delete Confirmation Modal -->
<div class="modal fade" id="deletePOModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">
                    <i class="fa fa-exclamation-triangle text-danger"></i> Xác nhận xóa
                </h4>
            </div>
            <div class="modal-body">
                <p>Bạn có chắc chắn muốn xóa đơn hàng <strong id="deletePOName"></strong>?</p>
                <p class="text-muted">
                    <i class="fa fa-info-circle"></i> Hành động này sẽ xóa vĩnh viễn đơn hàng và tất cả chi tiết đơn hàng liên quan.
                </p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">
                    <i class="fa fa-times"></i> Hủy
                </button>
                <form method="POST" action="${pageContext.request.contextPath}/purchase-order" style="display: inline;">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" id="deletePOId">
                    <button type="submit" class="btn btn-danger">
                        <i class="fa fa-trash"></i> Xóa
                    </button>
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
// Function to show modal (Bootstrap 3 compatible)
function showModal(modalId) {
    const modal = document.getElementById(modalId);
    if (!modal) {
        console.error('Modal not found:', modalId);
        return;
    }
    
    // Try Bootstrap 5 API first
    if (typeof bootstrap !== 'undefined') {
        const bsModal = new bootstrap.Modal(modal);
        bsModal.show();
    } else {
        // Fallback for Bootstrap 3 - manual modal show
        // Remove any existing backdrop first
        const existingBackdrop = document.querySelector('.modal-backdrop');
        if (existingBackdrop) {
            existingBackdrop.remove();
        }
        
        // Show modal
        modal.style.display = 'block';
        modal.style.opacity = '0';
        modal.classList.add('in');
        modal.setAttribute('aria-hidden', 'false');
        document.body.classList.add('modal-open');
        document.body.style.overflow = 'hidden';
        
        // Create backdrop
        const backdrop = document.createElement('div');
        backdrop.className = 'modal-backdrop fade in';
        backdrop.setAttribute('data-modal-id', modalId);
        backdrop.style.zIndex = '1040';
        document.body.appendChild(backdrop);
        
        // Ensure modal has proper z-index
        modal.style.zIndex = '1050';
        
        // Trigger fade in animation
        setTimeout(function() {
            modal.style.opacity = '1';
            modal.style.transition = 'opacity 0.3s';
        }, 10);
    }
}

// Function to hide modal (Bootstrap 3 compatible)
function hideModal(modalId) {
    const modal = document.getElementById(modalId);
    if (!modal) return;
    
    // Try Bootstrap 5 API first
    if (typeof bootstrap !== 'undefined') {
        const bsModal = bootstrap.Modal.getInstance(modal);
        if (bsModal) {
            bsModal.hide();
        } else {
            modal.style.display = 'none';
        }
    } else {
        // Fallback for Bootstrap 3 - manual modal hide
        modal.classList.remove('in');
        modal.style.opacity = '0';
        modal.setAttribute('aria-hidden', 'true');
        modal.style.transition = 'opacity 0.3s';
        
        setTimeout(function() {
            modal.style.display = 'none';
            document.body.classList.remove('modal-open');
            document.body.style.overflow = '';
            
            // Remove backdrop for this specific modal
            const backdrop = document.querySelector('.modal-backdrop[data-modal-id="' + modalId + '"]');
            if (backdrop) {
                backdrop.remove();
            } else {
                // Fallback: remove any backdrop if modal ID doesn't match
                const anyBackdrop = document.querySelector('.modal-backdrop');
                if (anyBackdrop) {
                    anyBackdrop.remove();
                }
            }
        }, 300); // Wait for fade animation
    }
}

function confirmDeletePO(poID, poName) {
    console.log('confirmDeletePO() called with:', poID, poName);
    const deletePOIdInput = document.getElementById('deletePOId');
    const deletePONameElement = document.getElementById('deletePOName');
    
    if (!deletePOIdInput || !deletePONameElement) {
        console.error('Delete modal elements not found!');
        alert('Lỗi: Không tìm thấy modal xóa. Vui lòng tải lại trang.');
        return;
    }
    
    deletePOIdInput.value = poID;
    deletePONameElement.textContent = poName;
    
    // Show modal
    showModal('deletePOModal');
}

document.addEventListener('DOMContentLoaded', function() {
    // Sorting functionality
    const sortableElements = document.querySelectorAll('.sortable');
    sortableElements.forEach(function(element) {
        element.addEventListener('click', function() {
            const sortBy = this.getAttribute('data-sort');
            const currentOrder = this.classList.contains('asc') ? 'asc' : 
                                this.classList.contains('desc') ? 'desc' : '';
            
            // Determine new sort order
            let newOrder = 'asc';
            if (currentOrder === 'asc') {
                newOrder = 'desc';
            }
            
            // Build URL with sort parameters while preserving filters
            const urlParams = new URLSearchParams(window.location.search);
            urlParams.set('sortBy', sortBy);
            urlParams.set('sortOrder', newOrder);
            
            // Navigate to new URL
            window.location.href = window.location.pathname + '?' + urlParams.toString();
        });
    });
    
    // Auto-submit filter on change
    const supplierSelect = document.querySelector('select[name="supplier"]');
    const statusSelect = document.querySelector('select[name="status"]');
    
    if (supplierSelect) {
        supplierSelect.addEventListener('change', function() {
            const form = this.closest('form');
            if (form) {
                form.submit();
            }
        });
    }
    
    if (statusSelect) {
        statusSelect.addEventListener('change', function() {
            const form = this.closest('form');
            if (form) {
                form.submit();
            }
        });
    }
    
    // Handle close buttons in delete modal
    const allCloseButtons = document.querySelectorAll('[data-dismiss="modal"]');
    allCloseButtons.forEach(function(btn) {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            const modal = this.closest('.modal');
            if (modal && modal.id) {
                hideModal(modal.id);
            }
        });
    });
    
    // Handle backdrop clicks to close modal
    document.addEventListener('click', function(e) {
        if (e.target.classList.contains('modal-backdrop')) {
            const modalId = e.target.getAttribute('data-modal-id');
            if (modalId) {
                hideModal(modalId);
            } else {
                // Fallback: find the visible modal and close it
                const visibleModal = document.querySelector('.modal.in');
                if (visibleModal && visibleModal.id) {
                    hideModal(visibleModal.id);
                }
            }
        }
    });
    
    // Handle ESC key to close modal
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape' || e.keyCode === 27) {
            const visibleModal = document.querySelector('.modal.in');
            if (visibleModal && visibleModal.id) {
                hideModal(visibleModal.id);
            }
        }
    });
});
</script>

</body>
</html>

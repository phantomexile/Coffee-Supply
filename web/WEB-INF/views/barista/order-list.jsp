<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Danh sách đơn hàng - Coffee Shop Management</title>
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.6 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
    <!-- AdminLTE -->
    <link rel="stylesheet" href="https://adminlte.io/themes/AdminLTE/dist/css/AdminLTE.min.css">
    <link rel="stylesheet" href="https://adminlte.io/themes/AdminLTE/dist/css/skins/_all-skins.min.css">
    <style>
        body {
            background-color: #ecf0f5;
        }
        
        .content-wrapper {
            margin-left: 230px;
            padding: 20px;
            min-height: 100vh;
        }
        
        .page-header {
            background: white;
            color: #333;
            padding: 20px;
            border-radius: 3px;
            margin-bottom: 20px;
            border-left: 4px solid #3c8dbc;
            box-shadow: 0 1px 1px rgba(0,0,0,0.1);
        }
        
        .page-header h1 {
            margin: 0;
            font-size: 24px;
            font-weight: 500;
            color: #333;
        }
        
        /* Bỏ màu nền của header bảng */
        .table thead th {
            background-color: transparent !important;
            color: #333 !important;
            border-bottom: 2px solid #ddd;
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
        
        .status-badge.status-New {
            background: #00c0ef;
            color: #fff;
        }
        
        .status-badge.status-Preparing {
            background: #f39c12;
            color: #fff;
        }
        
        .status-badge.status-Ready {
            background: #00a65a;
            color: #fff;
        }
        
        .status-badge.status-Completed {
            background: #777;
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
        
        .pagination {
            margin: 0;
        }
        
        .pagination > li > a,
        .pagination > li > span {
            color: #3c8dbc;
            border: 1px solid #ddd;
            padding: 6px 12px;
            margin: 0 2px;
        }
        
        .pagination > li > a:hover,
        .pagination > li > a:focus {
            background-color: #eee;
            border-color: #3c8dbc;
            color: #23527c;
        }
        
        .pagination > .active > a,
        .pagination > .active > a:hover,
        .pagination > .active > a:focus {
            background-color: #3c8dbc;
            border-color: #3c8dbc;
            color: #fff;
            cursor: default;
        }
        
        .pagination > .disabled > a,
        .pagination > .disabled > a:hover,
        .pagination > .disabled > a:focus {
            color: #999;
            cursor: not-allowed;
            background-color: #fff;
            border-color: #ddd;
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
        <!-- Page Header -->
        <div class="page-header">
            <h1><i class="fa fa-coffee"></i> Quản lý Đơn hàng</h1>
            <ol class="breadcrumb" style="background: transparent; padding-top: 10px; margin: 10px 0 0 0;">
                <li><a href="${pageContext.request.contextPath}/barista/dashboard"><i class="fa fa-dashboard"></i> Trang chủ</a></li>
                <li class="active">Danh sách đơn hàng</li>
            </ol>
        </div>

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
                <i class="fa fa-exclamation-circle"></i> ${fn:escapeXml(sessionScope.errorMessage)}
            </div>
            <c:remove var="errorMessage" scope="session"/>
        </c:if>

        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-header with-border">
                            <h3 class="box-title"><i class="fa fa-list"></i> Danh sách đơn hàng</h3>
                            <div class="box-tools pull-right">
                                <!-- Import/Export Buttons -->
                                <button type="button" class="btn btn-success btn-sm" data-toggle="modal" data-target="#importModal">
                                    <i class="fa fa-upload"></i> Import Excel
                                </button>
                                <a href="${pageContext.request.contextPath}/barista/orders/template" class="btn btn-info btn-sm">
                                    <i class="fa fa-download"></i> Tải Template
                                </a>
                                <a href="${pageContext.request.contextPath}/barista/orders/export" class="btn btn-primary btn-sm">
                                    <i class="fa fa-file-excel-o"></i> Export Excel
                                </a>
                                <span class="label label-primary" style="margin-left: 10px;">Tổng số: ${totalCount} đơn hàng</span>
                            </div>
                        </div>
                        
                        <div class="box-body">
                            <!-- Define base URL for orders -->
                            <c:set var="ordersUrl" value="${pageContext.request.contextPath}/barista/orders"/>
                            
                            <!-- Filter Section -->
                            <div class="row" style="margin-bottom: 20px;">
                                <div class="col-md-12">
                                    <form method="get" action="${ordersUrl}" class="form-inline">
                                        <div class="form-group" style="margin-right: 10px;">
                                            <label style="margin-right: 5px;">
                                                <i class="fa fa-info-circle"></i> Trạng thái:
                                            </label>
                                            <select class="form-control" name="status" style="width: 200px;">
                                                <option value="">-- Tất cả --</option>
                                                <c:forEach var="status" items="${orderStatuses}">
                                                    <option value="${status.settingID}" 
                                                            ${statusFilter == status.settingID ? 'selected' : ''}>
                                                        ${status.value}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        
                                        <!-- Keep current page when filtering, but reset to page 1 -->
                                        <input type="hidden" name="page" value="1">
                                        
                                        <button type="submit" class="btn btn-primary">
                                            <i class="fa fa-filter"></i> Lọc
                                        </button>
                                        <a href="${ordersUrl}" class="btn btn-default">
                                            <i class="fa fa-refresh"></i> Đặt lại
                                        </a>
                                    </form>
                                </div>
                            </div>
                            
                            <!-- Orders Table -->
                            <div class="table-responsive">
                                <table class="table table-striped table-hover">
                                    <thead>
                                        <tr>
                                            <th>Mã đơn hàng</th>
                                            <th>Cửa hàng</th>
                                            <th>Người tạo</th>
                                            <th>Trạng thái</th>
                                            <th>Ngày tạo</th>
                                            <th style="width: 120px;">Thao tác</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:choose>
                                            <c:when test="${empty orders}">
                                                    <tr>
                                                    <td colspan="6">
                                                        <div class="empty-state">
                                                            <i class="fa fa-inbox"></i>
                                                            <h4>Không có đơn hàng nào</h4>
                                                            <p>Chưa có đơn hàng trong hệ thống</p>
                                                        </div>
                            </td>
                        </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="order" items="${orders}">
                                                    <tr>
                                                        <td><strong style="color: #667eea;">ORD-${order.orderID}</strong></td>
                                                        <td><i class="fa fa-building-o"></i> ${order.shopName != null ? order.shopName : 'N/A'}</td>
                                                        <td><i class="fa fa-user"></i> ${order.createdByName != null ? order.createdByName : 'N/A'}</td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${order.statusName != null}">
                                                                    <span class="status-badge status-${order.statusName}">${order.statusName}</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="status-badge">N/A</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td>
                                                            <i class="fa fa-calendar"></i>
                                                            <c:choose>
                                                                <c:when test="${order.createdAt != null}">
                                                                    <fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    N/A
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td>
                                                            <a href="${pageContext.request.contextPath}/barista/order-details?id=${order.orderID}" 
                                                               class="btn btn-info btn-xs btn-action" title="Xem chi tiết">
                                                                <i class="fa fa-eye"></i>
                                                            </a>
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
                                <div class="text-center" style="margin-top: 20px;">
                                    <ul class="pagination">
                                        <!-- First page -->
                                        <c:if test="${currentPage > 1}">
                                            <li>
                                                <a href="${ordersUrl}?page=1<c:if test='${statusFilter != null}'>&status=${statusFilter}</c:if>">
                                                    <i class="fa fa-angle-double-left"></i>
                                                </a>
                                            </li>
                                        </c:if>
                                        
                                        <!-- Previous page -->
                                        <c:if test="${currentPage > 1}">
                                            <li>
                                                <a href="${ordersUrl}?page=${currentPage - 1}<c:if test='${statusFilter != null}'>&status=${statusFilter}</c:if>">
                                                    <i class="fa fa-angle-left"></i>
                                                </a>
                                            </li>
                                        </c:if>
                                        
                                        <!-- Page numbers -->
                                        <c:choose>
                                            <c:when test="${totalPages <= 10}">
                                                <c:forEach begin="1" end="${totalPages}" var="pageNum">
                                                    <li class="${pageNum == currentPage ? 'active' : ''}">
                                                        <a href="${ordersUrl}?page=${pageNum}<c:if test='${statusFilter != null}'>&status=${statusFilter}</c:if>">
                                                            ${pageNum}
                                                        </a>
                                                    </li>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <!-- Show first page -->
                                                <c:if test="${currentPage > 3}">
                                                    <li>
                                                        <a href="${ordersUrl}?page=1<c:if test='${statusFilter != null}'>&status=${statusFilter}</c:if>">1</a>
                                                    </li>
                                                    <c:if test="${currentPage > 4}">
                                                        <li class="disabled"><a href="#">...</a></li>
                                                    </c:if>
                                                </c:if>
                                                
                                                <!-- Show pages around current page -->
                                                <c:forEach begin="${currentPage > 2 ? currentPage - 2 : 1}" 
                                                           end="${currentPage + 2 < totalPages ? currentPage + 2 : totalPages}" 
                                                           var="pageNum">
                                                    <li class="${pageNum == currentPage ? 'active' : ''}">
                                                        <a href="${ordersUrl}?page=${pageNum}<c:if test='${statusFilter != null}'>&status=${statusFilter}</c:if>">
                                                            ${pageNum}
                                                        </a>
                                                    </li>
                                                </c:forEach>
                                                
                                                <!-- Show last page -->
                                                <c:if test="${currentPage < totalPages - 2}">
                                                    <c:if test="${currentPage < totalPages - 3}">
                                                        <li class="disabled"><a href="#">...</a></li>
                                                    </c:if>
                                                    <li>
                                                        <a href="${ordersUrl}?page=${totalPages}<c:if test='${statusFilter != null}'>&status=${statusFilter}</c:if>">${totalPages}</a>
                                                    </li>
                                                </c:if>
                                            </c:otherwise>
                                        </c:choose>
                                        
                                        <!-- Next page -->
                                        <c:if test="${currentPage < totalPages}">
                                            <li>
                                                <a href="${ordersUrl}?page=${currentPage + 1}<c:if test='${statusFilter != null}'>&status=${statusFilter}</c:if>">
                                                    <i class="fa fa-angle-right"></i>
                                                </a>
                                            </li>
                                        </c:if>
                                        
                                        <!-- Last page -->
                                        <c:if test="${currentPage < totalPages}">
                                            <li>
                                                <a href="${ordersUrl}?page=${totalPages}<c:if test='${statusFilter != null}'>&status=${statusFilter}</c:if>">
                                                    <i class="fa fa-angle-double-right"></i>
                                                </a>
                                            </li>
                                        </c:if>
                                    </ul>
                                    <p style="margin-top: 10px; color: #666;">
                                        <c:choose>
                                            <c:when test="${totalCount > 0}">
                                                Hiển thị ${(currentPage - 1) * pageSize + 1} - ${currentPage * pageSize > totalCount ? totalCount : currentPage * pageSize} của ${totalCount} đơn hàng
                                            </c:when>
                                            <c:otherwise>
                                                Không có đơn hàng nào
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                </div>
                            </c:if>
                            <c:if test="${totalPages == 1 && totalCount > 0}">
                                <div class="text-center" style="margin-top: 20px;">
                                    <p style="color: #666;">
                                        Hiển thị tất cả ${totalCount} đơn hàng
                                    </p>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
    
    <!-- Include Footer -->
    <%@include file="../compoment/footer.jsp" %>

</div>

<!-- Import Modal -->
<div class="modal fade" id="importModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title"><i class="fa fa-upload"></i> Import Đơn hàng từ Excel</h4>
            </div>
            <form action="${pageContext.request.contextPath}/barista/orders/import" method="post" enctype="multipart/form-data">
                <div class="modal-body">
                    <div class="alert alert-info">
                        <i class="fa fa-info-circle"></i> 
                        <strong>Hướng dẫn:</strong>
                        <ul style="margin-top: 10px; margin-bottom: 0;">
                            <li>Tải template Excel bằng nút "Tải Template"</li>
                            <li>Điền thông tin đơn hàng vào template</li>
                            <li><strong>Shop ID</strong> và <strong>Created By ID</strong> là bắt buộc</li>
                            <li><strong>Status ID</strong> để trống sẽ tự động set = <strong>New (29)</strong></li>
                            <li>Xem sheet "Reference" trong template để biết các Status ID</li>
                            <li>Upload file đã điền để import</li>
                        </ul>
                    </div>
                    
                    <div class="form-group">
                        <label>Chọn file Excel:</label>
                        <input type="file" name="file" class="form-control" accept=".xlsx,.xls" required>
                        <p class="help-block">Chỉ chấp nhận file Excel (.xlsx, .xls)</p>
                    </div>
                    
                    <div class="alert alert-warning">
                        <i class="fa fa-exclamation-triangle"></i>
                        <strong>Lưu ý:</strong> Status mặc định cho đơn hàng import là <strong>"New"</strong>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-success">
                        <i class="fa fa-upload"></i> Import
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- jQuery 2.2.3 -->
<script src="https://code.jquery.com/jquery-2.2.3.min.js"></script>
<!-- Bootstrap 3.3.6 JS -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<!-- AdminLTE App -->
<script src="https://adminlte.io/themes/AdminLTE/dist/js/app.min.js"></script>

</body>
</html>

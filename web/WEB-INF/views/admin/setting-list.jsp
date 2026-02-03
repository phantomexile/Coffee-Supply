<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Quản lý cài đặt hệ thống - Coffee Shop Management</title>
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
        .table-actions {
            white-space: nowrap;
        }
        .table-actions .btn {
            margin-right: 5px;
        }
        .badge.bg-blue {
            background-color: #3c8dbc !important;
            color: white;
            font-weight: bold;
        }
        .priority-badge {
            font-size: 12px;
            padding: 4px 8px;
            border-radius: 3px;
        }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

    <!-- Include Header (if missing, falls back to none) -->
    <%@include file="../compoment/header.jsp" %>
    
    <!-- Include Sidebar -->
    <%@include file="../compoment/sidebar.jsp" %>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                Quản lý cài đặt hệ thống
                <small>Cấu hình hệ thống</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${pageContext.request.contextPath}/admin/dashboard"><i class="fa fa-dashboard"></i> Trang chủ</a></li>
                <li class="active">Cài đặt hệ thống</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-header">
                            <h3 class="box-title">Danh sách cài đặt hệ thống</h3>
                            <div class="box-tools pull-right">
                                <a href="${pageContext.request.contextPath}/admin/setting?action=create" 
                                   class="btn btn-primary btn-sm">
                                    <i class="fa fa-plus"></i> Thêm cài đặt mới
                                </a>
                            </div>
                        </div>
                        
                        <div class="box-body">
                            <!-- Success Message -->
                            <c:if test="${not empty sessionScope.successMessage}">
                                <div class="alert alert-success alert-dismissible">
                                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                    <h4><i class="icon fa fa-check"></i> Thành công!</h4>
                                    ${sessionScope.successMessage}
                                </div>
                                <c:remove var="successMessage" scope="session"/>
                            </c:if>
                            
                            <!-- Error Message -->
                            <c:if test="${not empty error}">
                                <div class="alert alert-danger alert-dismissible">
                                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                    <h4><i class="icon fa fa-ban"></i> Lỗi!</h4>
                                    ${error}
                                </div>
                            </c:if>
                            
                            <!-- Filters -->
                            <div class="row" style="margin-bottom: 15px;">
                                <div class="col-md-3">
                                    <form method="get" action="${pageContext.request.contextPath}/admin/setting">
                                        <div class="form-group">
                                            <label>Tìm kiếm theo tên, giá trị hoặc mô tả:</label>
                                            <div class="input-group">
                                                <input type="text" class="form-control" name="search" 
                                                       value="${param.search}" placeholder="Search...">
                                                <span class="input-group-btn">
                                                    <button class="btn btn-default" type="submit">
                                                        <i class="fa fa-search"></i>
                                                    </button>
                                                </span>
                                            </div>
                                        </div>
                                        <c:if test="${not empty param.type}">
                                            <input type="hidden" name="type" value="${param.type}">
                                        </c:if>
                                        <c:if test="${not empty param.status}">
                                            <input type="hidden" name="status" value="${param.status}">
                                        </c:if>
                                    </form>
                                </div>
                                <div class="col-md-3">
                                    <form method="get" action="${pageContext.request.contextPath}/admin/setting">
                                        <div class="form-group">
                                            <label>Lọc theo loại cài đặt:</label>
                                            <select class="form-control" name="type" onchange="this.form.submit()">
                                                <option value="">Tất cả loại</option>
                                                <c:forEach var="availableType" items="${availableTypes}">
                                                    <option value="${availableType}" ${param.type == availableType ? 'selected' : ''}>
                                                        ${availableType}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <c:if test="${not empty param.search}">
                                            <input type="hidden" name="search" value="${param.search}">
                                        </c:if>
                                        <c:if test="${not empty param.status}">
                                            <input type="hidden" name="status" value="${param.status}">
                                        </c:if>
                                    </form>
                                </div>
                                <div class="col-md-3">
                                    <form method="get" action="${pageContext.request.contextPath}/admin/setting">
                                        <div class="form-group">
                                            <label>Lọc theo trạng thái:</label>
                                            <select class="form-control" name="status" onchange="this.form.submit()">
                                                <option value="">Tất cả trạng thái</option>
                                                <option value="1" ${param.status == '1' ? 'selected' : ''}>Hoạt động</option>
                                                <option value="0" ${param.status == '0' ? 'selected' : ''}>Không hoạt động</option>
                                            </select>
                                        </div>
                                        <c:if test="${not empty param.search}">
                                            <input type="hidden" name="search" value="${param.search}">
                                        </c:if>
                                        <c:if test="${not empty param.type}">
                                            <input type="hidden" name="type" value="${param.type}">
                                        </c:if>
                                    </form>
                                </div>
                                <div class="col-md-3">
                                    <div class="form-group">
                                        <label>&nbsp;</label>
                                        <div>
                                            <a href="${pageContext.request.contextPath}/admin/setting" class="btn btn-default">
                                                <i class="fa fa-refresh"></i> Xóa bộ lọc
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Settings Table -->
                            <div class="table-responsive">
                                <table class="table table-bordered table-striped">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Tên</th>
                                            <th>Loại cài đặt</th>
                                            <th>Giá trị</th>
                                            <th>Độ ưu tiên</th>
                                            <th>Trạng thái</th>
                                            <th>Thao tác</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="setting" items="${settings}">
                                            <tr>
                                                <td>${setting.settingID}</td>
                                                <td><strong>${setting.name}</strong></td>
                                                <td>${setting.type}</td>
                                                <td><code>${setting.value}</code></td>
                                                <td>
                                                    <span class="badge bg-blue priority-badge">${setting.priority}</span>
                                                </td>
                                                <td>
                                                    <span class="label ${setting.active ? 'label-success' : 'label-danger'}">
                                                        ${setting.active ? 'Hoạt động' : 'Không hoạt động'}
                                                    </span>
                                                </td>
                                                <td class="table-actions">
                                                    <a href="${pageContext.request.contextPath}/admin/setting?action=edit&id=${setting.settingID}" 
                                                       class="btn btn-warning btn-xs" title="Chỉnh sửa">
                                                        <i class="fa fa-edit"></i>
                                                    </a>
                                                    <button type="button" class="btn ${setting.active ? 'btn-danger' : 'btn-success'} btn-xs" 
                                                            title="${setting.active ? 'Vô hiệu hóa' : 'Kích hoạt'}"
                                                            onclick="toggleActive(${setting.settingID}, ${setting.active})">
                                                        <i class="fa ${setting.active ? 'fa-ban' : 'fa-check'}"></i>
                                                    </button>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty settings}">
                                            <tr>
                                                <td colspan="7" class="text-center">
                                                    <em>Không có cài đặt nào được tìm thấy</em>
                                                </td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                            
                            <!-- Pagination -->
                            <c:if test="${totalPages > 1}"> 
                                <div class="text-center">
                                    <ul class="pagination">
                                        <c:if test="${currentPage > 1}">
                                            <li>
                                                <a href="?page=${currentPage - 1}&search=${param.search}&type=${param.type}&status=${param.status}">
                                                    <i class="fa fa-angle-left"></i>
                                                </a>
                                            </li>
                                        </c:if>
                                        
                                        <c:forEach begin="1" end="${totalPages}" var="pageNum">
                                            <li class="${pageNum == currentPage ? 'active' : ''}">
                                                <a href="?page=${pageNum}&search=${param.search}&type=${param.type}&status=${param.status}">
                                                    ${pageNum}
                                                </a>
                                            </li>
                                        </c:forEach>
                                        
                                        <c:if test="${currentPage < totalPages}">
                                            <li>
                                                <a href="?page=${currentPage + 1}&search=${param.search}&type=${param.type}&status=${param.status}">
                                                    <i class="fa fa-angle-right"></i>
                                                </a>
                                            </li>
                                        </c:if>
                                    </ul>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
    
    <!-- Include Footer if available -->
    <%@include file="../compoment/footer.jsp" %>

</div>

<!-- jQuery (fallback to CDN if local not found) -->
<script src="${pageContext.request.contextPath}/plugins/jQuery/jquery-2.2.3.min.js"></script>
<script>if(typeof jQuery === 'undefined'){document.write('<script src="https://code.jquery.com/jquery-2.2.4.min.js"><\/script>');}</script>
<!-- Bootstrap -->
<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
<!-- AdminLTE App -->
<script src="${pageContext.request.contextPath}/dist/js/app.min.js"></script>

<script>
    function toggleActive(settingId, isActive) {
        var action = isActive ? 'vô hiệu hóa' : 'kích hoạt';
        var message = 'Bạn có chắc chắn muốn ' + action + ' cài đặt này?';
        
        if (confirm(message)) {
            window.location.href = '${pageContext.request.contextPath}/admin/setting?action=toggle&id=' + settingId;
        }
    }
</script>

</body>
</html>
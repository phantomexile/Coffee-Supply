<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý Ca Làm Việc - CoffeeLux</title>
    <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
    
    <!-- Bootstrap 3.3.4 -->
    <link href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
    <!-- Font Awesome Icons -->
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
    <!-- Theme style -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet" type="text/css" />
    <link href="https://cdnjs.cloudflare.com/ajax/libs/admin-lte/2.3.11/css/skins/_all-skins.min.css" rel="stylesheet" type="text/css" />
    
    <style>
        .status-badge {
            padding: 5px 10px;
            border-radius: 3px;
            font-size: 12px;
            font-weight: bold;
        }
        .status-active {
            background-color: #00a65a;
            color: white;
        }
        .status-inactive {
            background-color: #dd4b39;
            color: white;
        }
        .shift-time {
            font-weight: bold;
            color: #3c8dbc;
        }
        .action-buttons {
            white-space: nowrap;
        }
        .description-cell {
            max-width: 200px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
        .description-cell:hover {
            overflow: visible;
            white-space: normal;
            background-color: #f9f9f9;
            z-index: 10;
            position: relative;
            box-shadow: 0 2px 8px rgba(0,0,0,0.15);
            padding: 8px;
        }
        .shift-name {
            font-weight: bold;
            color: #333;
        }
        .manager-name {
            color: #666;
        }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <!-- Header -->
    <header class="main-header">
        <a href="${pageContext.request.contextPath}/dashboard" class="logo">
            <span class="logo-mini"><b>C</b>LX</span>
            <span class="logo-lg"><b>Coffee</b>Lux</span>
        </a>
        <nav class="navbar navbar-static-top" role="navigation">
            <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
                <span class="sr-only">Toggle navigation</span>
            </a>
        </nav>
    </header>

    <!-- Sidebar -->
    <jsp:include page="../compoment/sidebar.jsp" />

    <!-- Content Wrapper -->
    <div class="content-wrapper">
        <!-- Content Header -->
        <section class="content-header">
            <h1>
                Quản lý Ca Làm Việc
                <small>Danh sách tất cả ca làm việc</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${pageContext.request.contextPath}/dashboard"><i class="fa fa-dashboard"></i> Dashboard</a></li>
                <li class="active">Quản lý Ca Làm Việc</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">
            <!-- Success/Error Messages -->
            <c:if test="${param.success == 'create'}">
                <div class="alert alert-success alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                    <i class="icon fa fa-check"></i> Tạo ca làm việc thành công!
                </div>
            </c:if>
            <c:if test="${param.success == 'update'}">
                <div class="alert alert-success alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                    <i class="icon fa fa-check"></i> Cập nhật ca làm việc thành công!
                </div>
            </c:if>
            <c:if test="${param.success == 'toggle'}">
                <div class="alert alert-success alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                    <i class="icon fa fa-check"></i> Đổi trạng thái ca làm việc thành công!
                </div>
            </c:if>
            <c:if test="${param.success == 'delete'}">
                <div class="alert alert-success alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                    <i class="icon fa fa-check"></i> Xóa ca làm việc thành công!
                </div>
            </c:if>
            <c:if test="${param.error != null}">
                <div class="alert alert-danger alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                    <i class="icon fa fa-ban"></i> Có lỗi xảy ra. Vui lòng thử lại!
                </div>
            </c:if>

            <!-- Shifts Table Box -->
            <div class="box box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title">Danh sách ca làm việc</h3>
                    <div class="box-tools pull-right">
                        <a href="${pageContext.request.contextPath}/shift?action=create" class="btn btn-success btn-sm">
                            <i class="fa fa-plus"></i> Thêm ca làm việc mới
                        </a>
                    </div>
                </div>
                <div class="box-body">
                    <div class="table-responsive">
                        <table class="table table-bordered table-striped table-hover">
                            <thead>
                                <tr>
                                    <th style="width: 50px;">ID</th>
                                    <th style="width: 150px;">Tên ca</th>
                                    <th style="width: 100px;">Giờ bắt đầu</th>
                                    <th style="width: 100px;">Giờ kết thúc</th>
                                    <th style="width: 200px;">Mô tả</th>
                                    <th style="width: 90px;">Trạng thái</th>
                                    <th style="width: 150px;">Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty shifts}">
                                        <tr>
                                            <td colspan="8" class="text-center">
                                                <i class="fa fa-info-circle"></i> Chưa có ca làm việc nào
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="shift" items="${shifts}">
                                            <tr>
                                                <td>${shift.shiftID}</td>
                                                <td><strong>${shift.shiftName}</strong></td>
                                                <td class="shift-time">
                                                    <i class="fa fa-clock-o"></i> 
                                                    <fmt:formatDate value="${shift.startTime}" pattern="HH:mm" type="time"/>
                                                </td>
                                                <td class="shift-time">
                                                    <i class="fa fa-clock-o"></i> 
                                                    <fmt:formatDate value="${shift.endTime}" pattern="HH:mm" type="time"/>
                                                </td>
                                                <td>${shift.description}</td>
                                                
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${shift.active}">
                                                            <span class="status-badge status-active">
                                                                <i class="fa fa-check"></i> Hoạt động
                                                            </span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="status-badge status-inactive">
                                                                <i class="fa fa-times"></i> Dừng hoạt động
                                                            </span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="action-buttons">
                                                    <a href="${pageContext.request.contextPath}/shift?action=edit&id=${shift.shiftID}" 
                                                       class="btn btn-primary btn-xs" title="Chỉnh sửa">
                                                        <i class="fa fa-edit"></i>
                                                    </a>
                                                    <a href="${pageContext.request.contextPath}/shift/toggle?id=${shift.shiftID}" 
                                                       class="btn btn-warning btn-xs" 
                                                       title="${shift.active ? 'Hủy kích hoạt' : 'Kích hoạt'}"
                                                       onclick="return confirm('Bạn có chắc muốn đổi trạng thái ca làm việc này?')">
                                                        <i class="fa ${shift.active ? 'fa-pause' : 'fa-play'}"></i>
                                                    </a>
                                                    <a href="${pageContext.request.contextPath}/shift/delete?id=${shift.shiftID}" 
                                                       class="btn btn-danger btn-xs" title="Xóa"
                                                       onclick="return confirm('Bạn có chắc muốn xóa ca làm việc này? Tất cả phân công liên quan sẽ bị xóa.')">
                                                        <i class="fa fa-trash"></i>
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="box-footer clearfix">
                    <div class="pull-left">
                        <span class="text-muted">Tổng số ca: <strong>${shifts.size()}</strong></span>
                    </div>
                </div>
            </div>
        </section>
    </div>

    <!-- Footer -->
    <footer class="main-footer">
        <strong>Copyright &copy; 2024 <a href="#">CoffeeLux</a>.</strong> All rights reserved.
    </footer>
</div>

<!-- Scripts -->
<script src="${pageContext.request.contextPath}/bootstrap/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/admin-lte/2.3.11/js/app.min.js"></script>

<script>
    // Auto-hide alerts after 5 seconds
    setTimeout(function() {
        $('.alert').fadeOut('slow');
    }, 5000);
</script>
</body>
</html>

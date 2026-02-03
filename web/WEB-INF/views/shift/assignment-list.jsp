<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Danh Sách Phân Công - CoffeeLux</title>
    <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
    
    <!-- Bootstrap 3.3.4 -->
    <link href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
    <!-- Font Awesome Icons -->
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
    <!-- Theme style -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet" type="text/css" />
    <link href="https://cdnjs.cloudflare.com/ajax/libs/admin-lte/2.3.11/css/skins/_all-skins.min.css" rel="stylesheet" type="text/css" />
    
    <style>
        .shift-badge {
            padding: 5px 10px;
            border-radius: 3px;
            font-size: 11px;
            font-weight: bold;
            display: inline-block;
            margin-bottom: 5px;
        }
        .shift-morning {
            background-color: #f39c12;
            color: white;
        }
        .shift-afternoon {
            background-color: #00a65a;
            color: white;
        }
        .shift-night {
            background-color: #605ca8;
            color: white;
        }
        .shift-default {
            background-color: #3c8dbc;
            color: white;
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
                Danh Sách Phân Công
                <small>Lịch phân công ca làm việc cho nhân viên</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${pageContext.request.contextPath}/dashboard"><i class="fa fa-dashboard"></i> Dashboard</a></li>
                <li><a href="${pageContext.request.contextPath}/shift">Quản lý Ca Làm Việc</a></li>
                <li class="active">Danh sách phân công</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">
            <!-- Success/Error Messages -->
            <c:if test="${param.success == 'assign'}">
                <div class="alert alert-success alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                    <i class="icon fa fa-check"></i> Phân công nhân viên thành công!
                </div>
            </c:if>
            <c:if test="${param.success == 'delete'}">
                <div class="alert alert-success alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                    <i class="icon fa fa-check"></i> Xóa phân công thành công!
                </div>
            </c:if>
            <c:if test="${param.error != null}">
                <div class="alert alert-danger alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                    <i class="icon fa fa-ban"></i> Có lỗi xảy ra. Vui lòng thử lại!
                </div>
            </c:if>

            <!-- Assignments Table Box -->
            <div class="box box-info">
                <div class="box-header with-border">
                    <h3 class="box-title">Lịch phân công ca làm việc</h3>
                    <div class="box-tools pull-right">
                        <a href="${pageContext.request.contextPath}/shift?action=assign" class="btn btn-warning btn-sm">
                            <i class="fa fa-user-plus"></i> Phân công mới
                        </a>
                    </div>
                </div>
                <div class="box-body">
                    <div class="table-responsive">
                        <table class="table table-bordered table-striped table-hover">
                            <thead>
                                <tr>
                                    <th style="width: 50px;">ID</th>
                                    <th>Ca làm việc</th>
                                    <th>Giờ làm việc</th>
                                    <th>Nhân viên</th>
                                    <th>Email</th>
                                    <th style="width: 120px;">Ngày làm việc</th>
                                    <th>Ghi chú</th>
                                    <th style="width: 80px;">Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty assignments}">
                                        <tr>
                                            <td colspan="8" class="text-center">
                                                <i class="fa fa-info-circle"></i> Chưa có phân công nào
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="assignment" items="${assignments}">
                                            <tr>
                                                <td>${assignment.assignmentID}</td>
                                                <td>
                                                    <span class="shift-badge shift-default">
                                                        <i class="fa fa-clock-o"></i> ${assignment.shiftName}
                                                    </span>
                                                </td>
                                                <td>
                                                    <strong>${assignment.startTime}</strong> - <strong>${assignment.endTime}</strong>
                                                </td>
                                                <td>
                                                    <i class="fa fa-user"></i> ${assignment.userName}
                                                </td>
                                                <td>
                                                    <i class="fa fa-envelope"></i> ${assignment.userEmail}
                                                </td>
                                                <td>
                                                    <fmt:formatDate value="${assignment.assignedDate}" pattern="dd/MM/yyyy"/>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${not empty assignment.notes}">
                                                            ${assignment.notes}
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-muted">-</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/shift/delete-assignment?id=${assignment.assignmentID}" 
                                                       class="btn btn-danger btn-xs" 
                                                       title="Xóa phân công"
                                                       onclick="return confirm('Bạn có chắc muốn xóa phân công này?')">
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
                        <span class="text-muted">Tổng số phân công: <strong>${assignments.size()}</strong></span>
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

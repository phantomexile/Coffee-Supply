<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Phân Công Nhân Viên - CoffeeLux</title>
    <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
    
    <!-- Bootstrap 3.3.4 -->
    <link href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
    <!-- Font Awesome Icons -->
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
    <!-- Theme style -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet" type="text/css" />
    <link href="https://cdnjs.cloudflare.com/ajax/libs/admin-lte/2.3.11/css/skins/_all-skins.min.css" rel="stylesheet" type="text/css" />
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
                Phân Công Nhân Viên
                <small>Gán nhân viên vào ca làm việc</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${pageContext.request.contextPath}/dashboard"><i class="fa fa-dashboard"></i> Dashboard</a></li>
                <li><a href="${pageContext.request.contextPath}/shift">Quản lý Ca Làm Việc</a></li>
                <li class="active">Phân công nhân viên</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                    <i class="icon fa fa-ban"></i> ${errorMessage}
                </div>
            </c:if>

            <div class="row">
                <div class="col-md-8 col-md-offset-2">
                    <div class="box box-warning">
                        <div class="box-header with-border">
                            <h3 class="box-title">
                                <i class="fa fa-user-plus"></i> Phân công ca làm việc
                            </h3>
                        </div>

                        <form action="${pageContext.request.contextPath}/shift" method="POST">
                            <input type="hidden" name="action" value="assign">

                            <div class="box-body">
                                <!-- Select Shift -->
                                <div class="form-group">
                                    <label for="shiftID">Ca làm việc <span class="text-danger">*</span></label>
                                    <select class="form-control" id="shiftID" name="shiftID" required>
                                        <option value="">-- Chọn ca làm việc --</option>
                                        <c:forEach var="shift" items="${shifts}">
                                            <option value="${shift.shiftID}">
                                                ${shift.shiftName} (${shift.startTime} - ${shift.endTime})
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <span class="help-block">Chọn ca làm việc cần phân công</span>
                                </div>

                                <!-- Select User -->
                                <div class="form-group">
                                    <label for="userID">Nhân viên <span class="text-danger">*</span></label>
                                    <select class="form-control" id="userID" name="userID" required>
                                        <option value="">-- Chọn nhân viên --</option>
                                        <c:forEach var="user" items="${users}">
                                            <option value="${user.userID}">
                                                ${user.fullName} - ${user.email}
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <span class="help-block">Chọn nhân viên được phân công</span>
                                </div>

                                <!-- Assigned Date -->
                                <div class="form-group">
                                    <label for="assignedDate">Ngày làm việc <span class="text-danger">*</span></label>
                                    <input type="date" class="form-control" id="assignedDate" name="assignedDate" 
                                           required min="${pageContext.request.getAttribute('currentDate')}">
                                    <span class="help-block">Chọn ngày nhân viên sẽ làm ca này</span>
                                </div>

                                <!-- Notes -->
                                <div class="form-group">
                                    <label for="notes">Ghi chú</label>
                                    <textarea class="form-control" id="notes" name="notes" 
                                              rows="3" placeholder="Ghi chú thêm về phân công này (nếu có)..."></textarea>
                                </div>

                                <div class="callout callout-info">
                                    <h4><i class="icon fa fa-info"></i> Lưu ý:</h4>
                                    <p>Một nhân viên có thể được phân công nhiều ca khác nhau trong ngày. 
                                       Vui lòng kiểm tra lịch để tránh xung đột thời gian.</p>
                                </div>
                            </div>

                            <div class="box-footer">
                                <button type="submit" class="btn btn-warning">
                                    <i class="fa fa-user-plus"></i> Phân công
                                </button>
                                <a href="${pageContext.request.contextPath}/shift" class="btn btn-default">
                                    <i class="fa fa-times"></i> Hủy
                                </a>
                                <a href="${pageContext.request.contextPath}/shift?action=assignments" class="btn btn-info pull-right">
                                    <i class="fa fa-list"></i> Xem danh sách phân công
                                </a>
                            </div>
                        </form>
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


</body>
</html>

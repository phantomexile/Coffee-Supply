<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${mode == 'create' ? 'Thêm' : 'Chỉnh sửa'} Ca Làm Việc - CoffeeLux</title>
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
                ${mode == 'create' ? 'Thêm' : 'Chỉnh sửa'} Ca Làm Việc
                <small>${mode == 'create' ? 'Tạo ca làm việc mới' : 'Cập nhật thông tin ca làm việc'}</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${pageContext.request.contextPath}/dashboard"><i class="fa fa-dashboard"></i> Dashboard</a></li>
                <li><a href="${pageContext.request.contextPath}/shift">Quản lý Ca Làm Việc</a></li>
                <li class="active">${mode == 'create' ? 'Thêm' : 'Chỉnh sửa'}</li>
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
                    <div class="box box-primary">
                        <div class="box-header with-border">
                            <h3 class="box-title">
                                <i class="fa fa-clock-o"></i> 
                                ${mode == 'create' ? 'Thông tin ca làm việc mới' : 'Chỉnh sửa thông tin ca làm việc'}
                            </h3>
                        </div>

                        <form action="${pageContext.request.contextPath}/shift" method="POST">
                            <input type="hidden" name="action" value="${mode == 'create' ? 'create' : 'update'}">
                            <c:if test="${mode == 'edit'}">
                                <input type="hidden" name="shiftID" value="${shift.shiftID}">
                            </c:if>

                            <div class="box-body">
                                <!-- Shift Name -->
                                <div class="form-group">
                                    <label for="shiftName">Tên ca làm việc <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" id="shiftName" name="shiftName" 
                                           value="${shift.shiftName}" required
                                           placeholder="Ví dụ: Ca sáng, Ca chiều, Ca tối...">
                                </div>

                                <!-- Start Time -->
                                <div class="form-group">
                                    <label for="startTime">Giờ bắt đầu <span class="text-danger">*</span></label>
                                    <input type="time" class="form-control" id="startTime" name="startTime" 
                                           value="${mode == 'edit' ? shift.startTime : ''}" required>
                                    <span class="help-block">Chọn giờ bắt đầu ca làm việc</span>
                                </div>

                                <!-- End Time -->
                                <div class="form-group">
                                    <label for="endTime">Giờ kết thúc <span class="text-danger">*</span></label>
                                    <input type="time" class="form-control" id="endTime" name="endTime" 
                                           value="${mode == 'edit' ? shift.endTime : ''}" required>
                                    <span class="help-block">Chọn giờ kết thúc ca làm việc</span>
                                </div>

                                <!-- Description -->
                                <div class="form-group">
                                    <label for="description">Mô tả</label>
                                    <textarea class="form-control" id="description" name="description" 
                                              rows="3" placeholder="Mô tả chi tiết về ca làm việc...">${shift.description}</textarea>
                                </div>

                                <!-- Manager -->
                                

                                <!-- Is Active -->
                                <div class="form-group">
                                    <div class="checkbox">
                                        <label>
                                            <input type="checkbox" name="isActive" 
                                                   ${mode == 'create' || shift.active ? 'checked' : ''}>
                                            <strong>Kích hoạt ca làm việc</strong>
                                        </label>
                                        <span class="help-block">Ca làm việc có thể được gán cho nhân viên khi được kích hoạt</span>
                                    </div>
                                </div>
                            </div>

                            <div class="box-footer">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fa fa-save"></i> 
                                    ${mode == 'create' ? 'Tạo ca làm việc' : 'Cập nhật'}
                                </button>
                                <a href="${pageContext.request.contextPath}/shift" class="btn btn-default">
                                    <i class="fa fa-times"></i> Hủy
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

<script>
    // Validate time inputs
    $(document).ready(function() {
        $('form').submit(function(e) {
            var startTime = $('#startTime').val();
            var endTime = $('#endTime').val();
            
            if (startTime && endTime && startTime === endTime) {
                alert('Giờ bắt đầu và giờ kết thúc không được trùng nhau!');
                e.preventDefault();
                return false;
            }
        });
    });
</script>
</body>
</html>

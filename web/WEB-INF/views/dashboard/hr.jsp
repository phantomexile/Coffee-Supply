<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>HR Dashboard - Coffee Shop Management</title>
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
    </head>
    <body class="hold-transition skin-blue sidebar-mini">
        <div class="wrapper">

            <!-- Include Header -->
            <%@include file="../compoment/header.jsp" %>

            <!-- Include Sidebar -->
            <%@include file="../compoment/sidebar.jsp" %>

            <!-- Content Wrapper. Contains page content -->
            <div class="content-wrapper">
                <!-- Content Header (Page header) -->
                <section class="content-header">
                    <h1>
                        HR Dashboard
                        <small>Quản lý nhân sự</small>
                    </h1>
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">HR Dashboard</li>
                    </ol>
                </section>

                <!-- Main content -->
                <section class="content">
                    <!-- Welcome Section -->
                    <div class="row">
                        <div class="col-md-12">
                            <div class="box">
                                <div class="box-header with-border">
                                    <h3 class="box-title">Chào mừng, ${sessionScope.user.fullName}!</h3>
                                    <p>Phòng Nhân sự - Quản lý nhân viên và tuyển dụng</p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Statistics Cards -->
                    <div class="row">
                        <div class="col-lg-4 col-xs-6">
                            <!-- small box -->
                            <div class="small-box bg-aqua">
                                <div class="inner">
                                    <h3>${totalEmployees}</h3>
                                    <p>Tổng nhân viên</p>
                                </div>
                                <div class="icon">
                                    <i class="fa fa-users"></i>
                                </div>
                                <a href="${pageContext.request.contextPath}/user" class="small-box-footer">
                                    Xem chi tiết <i class="fa fa-arrow-circle-right"></i>
                                </a>
                            </div>
                        </div>

                        <div class="col-lg-4 col-xs-6">
                            <!-- small box -->
                            <div class="small-box bg-green">
                                <div class="inner">
                                    <h3>${activeUser}</h3>
                                    <p>Người dùng hoạt động</p>
                                </div>
                                <div class="icon">
                                    <i class="fa fa-check-circle"></i>
                                </div>
                                <a href="${pageContext.request.contextPath}/user" class="small-box-footer">
                                    Xem chi tiết <i class="fa fa-arrow-circle-right"></i>
                                </a>
                            </div>
                        </div>


                    </div>

                    <!-- /.row -->

                    <!-- User Distribution -->
                    <div class="row">
                        <div class="col-md-12">
                            <div class="box box-info">
                                <div class="box-header with-border">
                                    <h3 class="box-title"><i class="fa fa-pie-chart"></i> Phân bố người dùng theo vai trò</h3>
                                </div>
                                <div class="box-body">
                                    <div class="row">
                                        <div class="col-md-3 col-sm-6">
                                            <div class="info-box bg-blue">
                                                <span class="info-box-icon"><i class="fa fa-users"></i></span>
                                                <div class="info-box-content">
                                                    <span class="info-box-text">HR - Nhân sự</span>
                                                    <span class="info-box-number">${hrCount}</span>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="col-md-3 col-sm-6">
                                            <div class="info-box bg-yellow">
                                                <span class="info-box-icon"><i class="fa fa-cubes"></i></span>
                                                <div class="info-box-content">
                                                    <span class="info-box-text">Inventory</span>
                                                    <span class="info-box-number">${inventoryCount}</span>
                                                </div>
                                            </div>
                                        </div>


                                        <div class="col-md-3 col-sm-6">
                                            <div class="info-box bg-green">
                                                <span class="info-box-icon"><i class="fa fa-coffee"></i></span>
                                                <div class="info-box-content">
                                                    <span class="info-box-text">Barista</span>
                                                    <span class="info-box-number">${baristaCount}</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>



                    <!-- Charts Row -->
                    <div class="row">
                        <!-- Bar Chart: Employee Distribution by Role -->
                        <div class="col-md-6">
                            <div class="box box-primary">
                                <div class="box-header with-border">
                                    <h3 class="box-title"><i class="fa fa-bar-chart"></i> Phân bố nhân viên theo vị trí</h3>
                                </div>
                                <div class="box-body">
                                    <div style="position: relative; height: 300px;">
                                        <canvas id="roleDistributionChart"></canvas>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Pie Chart: Active/Inactive Ratio -->
                        <div class="col-md-6">
                            <div class="box box-success">
                                <div class="box-header with-border">
                                    <h3 class="box-title"><i class="fa fa-pie-chart"></i> Tỷ lệ nhân viên hoạt động</h3>
                                </div>
                                <div class="box-body">
                                    <div style="position: relative; height: 300px;">
                                        <canvas id="activeStatusChart"></canvas>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- /.row -->

                    <div class="row">
                        <!-- Employee Status -->
                        <div class="col-md-12">
                            <div class="box box-info">
                                <div class="box-header with-border">
                                    <h3 class="box-title">Danh sách nhân viên hoạt động</h3>
                                </div>
                                <div class="box-body">
                                    <div class="table-responsive">
                                        <table class="table table-striped">
                                            <thead>
                                                <tr>
                                                    <th>Nhân viên</th>
                                                    <th>Email</th>
                                                    <th>Vị trí</th>
                                                    <th>Trạng thái</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${not empty employeesStatus}">
                                                        <c:forEach var="employee" items="${employeesStatus}" begin="0" end="9">
                                                            <tr>
                                                                <td>
                                                                    <span class="username">${employee.fullName}</span>
                                                                </td>
                                                                <td>${employee.email}</td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${employee.roleID == 3}">
                                                                            <span class="label label-primary">Quản lý kho</span>
                                                                        </c:when>
                                                                        <c:when test="${employee.roleID == 4}">
                                                                            <span class="label label-info">Barista</span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            Nhân viên
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${employee.active}">
                                                                            <span class="label label-success">Hoạt động</span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="label label-danger">Không hoạt động</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <tr>
                                                            <td colspan="4" class="text-center">Không có dữ liệu nhân viên</td>
                                                        </tr>
                                                    </c:otherwise>
                                                </c:choose>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="box-footer text-center">
                                        <a href="${pageContext.request.contextPath}/user" class="btn btn-primary">
                                            <i class="fa fa-users"></i> Xem tất cả nhân viên
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- /.row -->

                </section>
                <!-- /.content -->
            </div>
            <!-- /.content-wrapper -->

            <!-- Include Footer -->
            <footer class="main-footer">
                <div class="pull-right hidden-xs">
                    <b>Version</b> 2.3.8
                </div>
                <strong>Copyright &copy; 2014-2016 <a href="http://almsaeedstudio.com">Almsaeed Studio</a>.</strong> All rights
                reserved.
            </footer>

            <!-- Control Sidebar -->
            <aside class="control-sidebar control-sidebar-dark">
                <!-- Create the tabs -->
                <ul class="nav nav-tabs nav-justified control-sidebar-tabs">
                    <li><a href="#control-sidebar-home-tab" data-toggle="tab"><i class="fa fa-home"></i></a></li>
                    <li><a href="#control-sidebar-settings-tab" data-toggle="tab"><i class="fa fa-gears"></i></a></li>
                </ul>
            </aside>
            <!-- /.control-sidebar -->
            <!-- Add the sidebar's background. This div must be placed
                 immediately after the control sidebar -->
            <div class="control-sidebar-bg"></div>
        </div>
        <!-- ./wrapper -->

        <!-- jQuery 2.2.3 -->
        <script src="https://code.jquery.com/jquery-2.2.3.min.js"></script>
        <!-- jQuery UI 1.11.4 -->
        <script src="https://code.jquery.com/ui/1.11.4/jquery-ui.min.js"></script>
        <!-- Resolve conflict in jQuery UI tooltip with Bootstrap tooltip -->
        <script>
            $.widget.bridge('uibutton', $.ui.button);
        </script>
        <!-- Bootstrap 3.3.6 -->
        <script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
        <!-- AdminLTE App -->
        <script src="${pageContext.request.contextPath}/dist/js/app.min.js"></script>
        <!-- Chart.js -->
        <script src="https://cdn.jsdelivr.net/npm/chart.js@3.9.1/dist/chart.min.js"></script>

        <script>
            $(document).ready(function () {
                // Bar Chart: Employee Distribution by Role (excluding Admin)
                var roleCtx = document.getElementById('roleDistributionChart').getContext('2d');
                var roleChart = new Chart(roleCtx, {
                    type: 'bar',
                    data: {
                        labels: ['HR', 'Quản lý kho', 'Barista'],
                        datasets: [{
                                label: 'Số lượng nhân viên',
                                data: [
            ${hrCount},
            ${inventoryCount},
            ${baristaCount}
                                ],
                                backgroundColor: [
                                    'rgba(0, 166, 90, 0.7)', // Green for HR
                                    'rgba(60, 141, 188, 0.7)', // Blue for Inventory
                                    'rgba(243, 156, 18, 0.7)'  // Orange for Barista
                                ],
                                borderColor: [
                                    'rgba(0, 166, 90, 1)',
                                    'rgba(60, 141, 188, 1)',
                                    'rgba(243, 156, 18, 1)'
                                ],
                                borderWidth: 1
                            }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        plugins: {
                            legend: {
                                display: false
                            },
                            title: {
                                display: false
                            }
                        },
                        scales: {
                            y: {
                                beginAtZero: true,
                                ticks: {
                                    stepSize: 1
                                }
                            }
                        }
                    }
                });

                // Pie Chart: Active/Inactive Status (excluding Admin)
                var statusCtx = document.getElementById('activeStatusChart').getContext('2d');
                var statusChart = new Chart(statusCtx, {
                    type: 'pie',
                    data: {
                        labels: ['Hoạt động', 'Không hoạt động'],
                        datasets: [{
                                data: [${activeUser}, ${inactiveUser}],
                                backgroundColor: [
                                    'rgba(0, 166, 90, 0.7)', // Green for Active
                                    'rgba(221, 75, 57, 0.7)'  // Red for Inactive
                                ],
                                borderColor: [
                                    'rgba(0, 166, 90, 1)',
                                    'rgba(221, 75, 57, 1)'
                                ],
                                borderWidth: 1
                            }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        plugins: {
                            legend: {
                                position: 'bottom',
                                labels: {
                                    padding: 15,
                                    font: {
                                        size: 13
                                    }
                                }
                            },
                            tooltip: {
                                callbacks: {
                                    label: function (context) {
                                        var label = context.label || '';
                                        var value = context.parsed || 0;
                                        var total = context.dataset.data.reduce((a, b) => a + b, 0);
                                        var percentage = ((value / total) * 100).toFixed(1);
                                        return label + ': ' + value + ' (' + percentage + '%)';
                                    }
                                }
                            }
                        }
                    }
                });
            });
        </script>

    </body>
</html>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Admin Dashboard - Coffee Shop Management</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.6 từ CDN -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
    <!-- Ionicons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/AdminLTE.min.css">
    <!-- AdminLTE Skins -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/skins/_all-skins.min.css">
    <!-- Morris charts -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/plugins/morris/morris.css">
    <!-- Sidebar improvements -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar-improvements.css">
    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <!-- jQuery từ CDN - load trước tiên -->
    <script src="https://code.jquery.com/jquery-2.2.4.min.js" integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44=" crossorigin="anonymous"></script>
    
    <style>
        .info-box {
            display: block;
            min-height: 90px;
            background: #fff;
            width: 100%;
            box-shadow: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24);
            border-radius: 2px;
            margin-bottom: 15px;
        }
        
        .info-box-icon {
            border-top-left-radius: 2px;
            border-top-right-radius: 0;
            border-bottom-right-radius: 0;
            border-bottom-left-radius: 2px;
            display: block;
            float: left;
            height: 90px;
            width: 90px;
            text-align: center;
            font-size: 45px;
            line-height: 90px;
            background: rgba(0,0,0,0.2);
        }
        
        .info-box-content {
            padding: 5px 10px;
            margin-left: 90px;
        }
        
        .info-box-number {
            display: block;
            font-weight: bold;
            font-size: 18px;
        }
        
        .info-box-text {
            display: block;
            font-size: 14px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }
        
        .bg-red { background-color: #dd4b39 !important; }
        .bg-green { background-color: #00a65a !important; }
        .bg-yellow { background-color: #f39c12 !important; }
        .bg-aqua { background-color: #00c0ef !important; }
        .bg-blue { background-color: #3c8dbc !important; }
        .bg-purple { background-color: #605ca8 !important; }
        
        .small-box {
            border-radius: 2px;
            position: relative;
            display: block;
            margin-bottom: 20px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24);
        }
        
        .small-box > .inner {
            padding: 15px;
        }
        
        .small-box .icon {
            -webkit-transition: all .3s linear;
            -o-transition: all .3s linear;
            transition: all .3s linear;
            position: absolute;
            top: -10px;
            right: 10px;
            z-index: 0;
            font-size: 90px;
            color: rgba(0,0,0,0.15);
        }
        
        .small-box p {
            font-size: 15px;
        }
        
        .small-box h3 {
            font-size: 38px;
            font-weight: bold;
            margin: 0 0 10px 0;
            white-space: nowrap;
            padding: 0;
        }
        
        .small-box-footer {
            position: relative;
            text-align: center;
            padding: 3px 0;
            color: #fff;
            color: rgba(255,255,255,0.8);
            display: block;
            z-index: 10;
            background: rgba(0,0,0,0.1);
            text-decoration: none;
        }
        
        .small-box-footer:hover {
            color: #fff;
            background: rgba(0,0,0,0.15);
        }
    </style>
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
                Admin Dashboard
                <small>Quản trị hệ thống</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${pageContext.request.contextPath}/admin/dashboard"><i class="fa fa-dashboard"></i> Home</a></li>
                <li class="active">Admin Dashboard</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">
            
            <!-- Error Message -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                    <h4><i class="icon fa fa-ban"></i> Lỗi!</h4>
                    ${error}
                </div>
            </c:if>
            
            <!-- Welcome Message -->
            <div class="row">
                <div class="col-md-12">
                    <div class="alert alert-info alert-dismissible">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                        <h4><i class="icon fa fa-info"></i> Xin chào, ${sessionScope.user.fullName}!</h4>
                        Chào mừng bạn đến với bảng điều khiển quản trị hệ thống Coffee Shop.
                        <br><small>Dữ liệu được cập nhật từ cơ sở dữ liệu thực tế.</small>
                    </div>
                </div>
            </div>

            <!-- Stats Overview -->
            <div class="row">
                <div class="col-lg-3 col-xs-6">
                    <!-- small box -->
                    <div class="small-box bg-aqua">
                        <div class="inner">
                            <h3>${totalUsers}</h3>
                            <p>Tổng số người dùng</p>
                        </div>
                        <div class="icon">
                            <i class="fa fa-users"></i>
                        </div>
                        <a href="${pageContext.request.contextPath}/user" class="small-box-footer">
                            Xem chi tiết <i class="fa fa-arrow-circle-right"></i>
                        </a>
                    </div>
                </div>
                
                <div class="col-lg-3 col-xs-6">
                    <!-- small box -->
                    <div class="small-box bg-green">
                        <div class="inner">
                            <h3>${activeUsers}</h3>
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
                
                <div class="col-lg-3 col-xs-6">
                    <!-- small box -->
                    <div class="small-box bg-yellow">
                        <div class="inner">
                            <h3>${hrCount + inventoryCount + baristaCount}</h3>
                            <p>Nhân viên</p>
                        </div>
                        <div class="icon">
                            <i class="fa fa-user-plus"></i>
                        </div>
                        <a href="${pageContext.request.contextPath}/user" class="small-box-footer">
                            Xem chi tiết <i class="fa fa-arrow-circle-right"></i>
                        </a>
                    </div>
                </div>
                
                <div class="col-lg-3 col-xs-6">
                    <!-- small box -->
                    <div class="small-box bg-red">
                        <div class="inner">
                            <h3>${adminCount}</h3>
                            <p>Quản trị viên</p>
                        </div>
                        <div class="icon">
                            <i class="fa fa-shield"></i>
                        </div>
                        <a href="${pageContext.request.contextPath}/user" class="small-box-footer">
                            Xem chi tiết <i class="fa fa-arrow-circle-right"></i>
                        </a>
                    </div>
                </div>
            </div>

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
                                    <div class="info-box bg-red">
                                        <span class="info-box-icon"><i class="fa fa-shield"></i></span>
                                        <div class="info-box-content">
                                            <span class="info-box-text">Admin</span>
                                            <span class="info-box-number">${adminCount}</span>
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

            <!-- Charts Section -->
            <div class="row">
                <div class="col-md-6">
                    <!-- User Distribution Pie Chart -->
                    <div class="box box-primary">
                        <div class="box-header with-border">
                            <h3 class="box-title"><i class="fa fa-pie-chart"></i> Phân bố người dùng theo vai trò</h3>
                        </div>
                        <div class="box-body">
                            <canvas id="userRoleChart" width="400" height="400"></canvas>
                        </div>
                    </div>
                </div>
                
                <div class="col-md-6">
                    <!-- User Status Chart -->
                    <div class="box box-success">
                        <div class="box-header with-border">
                            <h3 class="box-title"><i class="fa fa-bar-chart"></i> Trạng thái người dùng</h3>
                        </div>
                        <div class="box-body">
                            <canvas id="userStatusChart" width="400" height="400"></canvas>
                        </div>
                    </div>
                </div>
            </div>
            


            <!-- System Information -->
            <div class="row">
                <div class="col-md-8">
                    <!-- Recent Activities -->
                    <div class="box box-primary">
                        <div class="box-header with-border">
                            <h3 class="box-title">Hoạt động gần đây</h3>
                            <div class="box-tools pull-right">
                                <button type="button" class="btn btn-box-tool" data-widget="collapse">
                                    <i class="fa fa-minus"></i>
                                </button>
                            </div>
                        </div>
                        <div class="box-body">
                            <ul class="timeline timeline-inverse">
                                <!-- timeline time label -->
                                <li class="time-label">
                                    <span class="bg-red">
                                        <fmt:formatDate value="<%= new java.util.Date() %>" pattern="dd/MM/yyyy"/>
                                    </span>
                                </li>
                                
                                <!-- timeline item -->
                                <li>
                                    <i class="fa fa-user bg-blue"></i>
                                    <div class="timeline-item">
                                        <span class="time"><i class="fa fa-clock-o"></i> 12:05</span>
                                        <h3 class="timeline-header">Người dùng mới được tạo</h3>
                                        <div class="timeline-body">
                                            Barista "Nguyễn Văn A" đã được thêm vào hệ thống
                                        </div>
                                    </div>
                                </li>
                                
                                <!-- timeline item -->
                                <li>
                                    <i class="fa fa-cog bg-yellow"></i>
                                    <div class="timeline-item">
                                        <span class="time"><i class="fa fa-clock-o"></i> 11:30</span>
                                        <h3 class="timeline-header">Cập nhật hệ thống</h3>
                                        <div class="timeline-body">
                                            Phân quyền người dùng đã được cập nhật
                                        </div>
                                    </div>
                                </li>
                                
                                <!-- timeline item -->
                                <li>
                                    <i class="fa fa-warning bg-purple"></i>
                                    <div class="timeline-item">
                                        <span class="time"><i class="fa fa-clock-o"></i> 10:15</span>
                                        <h3 class="timeline-header">Cảnh báo bảo mật</h3>
                                        <div class="timeline-body">
                                            Đã phát hiện một số lần đăng nhập không thành công
                                        </div>
                                    </div>
                                </li>
                                
                                <!-- timeline item -->
                                <li>
                                    <i class="fa fa-database bg-green"></i>
                                    <div class="timeline-item">
                                        <span class="time"><i class="fa fa-clock-o"></i> 09:00</span>
                                        <h3 class="timeline-header">Sao lưu dữ liệu</h3>
                                        <div class="timeline-body">
                                            Sao lưu tự động đã hoàn thành thành công
                                        </div>
                                    </div>
                                </li>
                                
                                <!-- END timeline item -->
                                <li>
                                    <i class="fa fa-clock-o bg-gray"></i>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                
                <div class="col-md-4">
                    <!-- System Status -->
                    <div class="box box-success">
                        <div class="box-header with-border">
                            <h3 class="box-title">Trạng thái hệ thống</h3>
                            <div class="box-tools pull-right">
                                <button type="button" class="btn btn-box-tool" data-widget="collapse">
                                    <i class="fa fa-minus"></i>
                                </button>
                            </div>
                        </div>
                        <div class="box-body">
                            <div class="info-box bg-green">
                                <span class="info-box-icon"><i class="fa fa-check"></i></span>
                                <div class="info-box-content">
                                    <span class="info-box-text">Máy chủ</span>
                                    <span class="info-box-number">${serverStatus == 'online' ? 'Online' : 'Offline'}</span>
                                    <div class="progress">
                                        <div class="progress-bar" style="width: ${serverStatus == 'online' ? '100' : '0'}%"></div>
                                    </div>
                                    <span class="progress-description">
                                        ${serverStatus == 'online' ? 'Hoạt động bình thường' : 'Có vấn đề'}
                                    </span>
                                </div>
                            </div>
                            
                            <div class="info-box bg-blue">
                                <span class="info-box-icon"><i class="fa fa-database"></i></span>
                                <div class="info-box-content">
                                    <span class="info-box-text">Cơ sở dữ liệu</span>
                                    <span class="info-box-number">${databaseStatus == 'connected' ? 'Kết nối' : 'Mất kết nối'}</span>
                                    <div class="progress">
                                        <div class="progress-bar" style="width: ${databaseStatus == 'connected' ? '100' : '0'}%"></div>
                                    </div>
                                    <span class="progress-description">
                                        ${databaseStatus == 'connected' ? 'Kết nối ổn định' : 'Kiểm tra kết nối'}
                                    </span>
                                </div>
                            </div>
                            
                            <div class="info-box bg-aqua">
                                <span class="info-box-icon"><i class="fa fa-users"></i></span>
                                <div class="info-box-content">
                                    <span class="info-box-text">Người dùng hoạt động</span>
                                    <span class="info-box-number">${activeUsers}</span>
                                    <div class="progress">
                                        <div class="progress-bar" style="width: ${(activeUsers * 100) / totalUsers}%"></div>
                                    </div>
                                    <span class="progress-description">${(activeUsers * 100) / totalUsers}% tổng số người dùng</span>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Quick Actions -->
                    <div class="box box-info">
                        <div class="box-header with-border">
                            <h3 class="box-title">Thao tác nhanh</h3>
                        </div>
                        <div class="box-body">
                            <a href="${pageContext.request.contextPath}/user?action=create" class="btn btn-app">
                                <i class="fa fa-user-plus"></i> Thêm người dùng
                            </a>
                            <a href="javascript:void(0)" class="btn btn-app">
                                <i class="fa fa-cogs"></i> Cài đặt
                            </a>
                            <a href="javascript:void(0)" class="btn btn-app">
                                <i class="fa fa-shield"></i> Phân quyền
                            </a>
                            <a href="javascript:void(0)" class="btn btn-app">
                                <i class="fa fa-download"></i> Sao lưu
                            </a>
                            <a href="javascript:void(0)" class="btn btn-app">
                                <i class="fa fa-bar-chart"></i> Báo cáo
                            </a>
                            <a href="javascript:void(0)" class="btn btn-app">
                                <i class="fa fa-envelope"></i> Thông báo
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            
                                        
                                        
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

</div>
<!-- ./wrapper -->

<!-- jQuery 2.2.3 từ CDN -->
<script src="https://code.jquery.com/jquery-2.2.4.min.js" integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44=" crossorigin="anonymous"></script>
<!-- Bootstrap 3.3.6 từ CDN -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
<!-- Chart.js cho biểu đồ -->
<script src="https://cdn.jsdelivr.net/npm/chart.js@2.9.4/dist/Chart.min.js"></script>

<!-- AdminLTE App (nếu có) -->
<!--<script>
// Define toggleMenu function immediately để tránh lỗi undefined
function toggleMenu(element) {
    var menu = element.nextElementSibling;
    var icon = element.querySelector('.fa-angle-left, .fa-angle-down');
    
    if (menu && (menu.style.display === 'none' || menu.style.display === '')) {
        menu.style.display = 'block';
        if (icon) {
            icon.classList.add('fa-angle-down');
            icon.classList.remove('fa-angle-left');
        }
        element.parentElement.classList.add('active');
    } else if (menu) {
        menu.style.display = 'none';
        if (icon) {
            icon.classList.add('fa-angle-left');
            icon.classList.remove('fa-angle-down');
        }
        element.parentElement.classList.remove('active');
    }
}

function toggleInventoryMenu(element) {
    toggleMenu(element);
}

function togglePurchaseMenu(element) {
    toggleMenu(element);
}

// Đặt functions vào window object
window.toggleMenu = toggleMenu;
window.toggleInventoryMenu = toggleInventoryMenu;
window.togglePurchaseMenu = togglePurchaseMenu;

console.log('Toggle functions defined in dashboard');
</script>-->

<!-- Custom Sidebar Script -->
<script src="${pageContext.request.contextPath}/js/sidebar.js"></script>

<script>
//    // Override nếu cần thiết
//    if (typeof toggleMenu === 'undefined') {
//        function toggleMenu(element) {
//            var menu = element.nextElementSibling;
//            var icon = element.querySelector('.fa-angle-left, .fa-angle-down');
//            
//            if (menu && (menu.style.display === 'none' || menu.style.display === '')) {
//                menu.style.display = 'block';
//                if (icon) {
//                    icon.classList.add('fa-angle-down');
//                    icon.classList.remove('fa-angle-left');
//                }
//                element.parentElement.classList.add('active');
//            } else if (menu) {
//                menu.style.display = 'none';
//                if (icon) {
//                    icon.classList.add('fa-angle-left');
//                    icon.classList.remove('fa-angle-down');
//                }
//                element.parentElement.classList.remove('active');
//            }
//        }
//        
//        // Đặt functions vào window object
//        window.toggleMenu = toggleMenu;
//        window.toggleInventoryMenu = toggleMenu;
//        window.togglePurchaseMenu = toggleMenu;
//    }

    // User Role Distribution Chart
    const userRoleCtx = document.getElementById('userRoleChart').getContext('2d');
    const userRoleChart = new Chart(userRoleCtx, {
        type: 'pie',
        data: {
            labels: ['HR', 'Admin', 'Inventory', 'Barista'],
            datasets: [{
                data: [${hrCount}, ${adminCount}, ${inventoryCount}, ${baristaCount}],
                backgroundColor: [
                    '#3c8dbc',  // Blue for HR
                    '#dd4b39',  // Red for Admin
                    '#f39c12',  // Orange for Inventory
                    '#00a65a'   // Green for Barista
                ],
                borderColor: '#fff',
                borderWidth: 2
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: {
                        padding: 20,
                        usePointStyle: true
                    }
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            return context.label + ': ' + context.parsed ;
                        }
                    }
                }
            }
        }
    });

    // User Status Chart
    const userStatusCtx = document.getElementById('userStatusChart').getContext('2d');
    const userStatusChart = new Chart(userStatusCtx, {
        type: 'doughnut',
        data: {
            labels: ['Hoạt động', 'Không hoạt động'],
            datasets: [{
                data: [${activeUsers}, ${totalUsers - activeUsers}],
                backgroundColor: [
                    '#00a65a',  // Green for active
                    '#dd4b39'   // Red for inactive
                ],
                borderColor: '#fff',
                borderWidth: 3
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            cutout: '60%',
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: {
                        padding: 20,
                        usePointStyle: true
                    }
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            const total = ${totalUsers};
                            const percentage = Math.round((context.parsed / total) * 100);
                            return context.label + ': ' + context.parsed + ' (' + percentage + '%)';
                        }
                    }
                }
            }
        }
    });

    // System Activity Chart (Sample data - would be real-time in production)
//    const systemActivityCtx = document.getElementById('systemActivityChart').getContext('2d');
//    const systemActivityChart = new Chart(systemActivityCtx, {
//        type: 'line',
//        data: {
//            labels: ['06:00', '08:00', '10:00', '12:00', '14:00', '16:00', '18:00', '20:00', '22:00'],
//            datasets: [
//                {
//                    label: 'Đăng nhập',
//                    data: [2, 8, 15, 12, 9, 11, 7, 3, 1],
//                    borderColor: '#3c8dbc',
//                    backgroundColor: 'rgba(60, 141, 188, 0.1)',
//                    tension: 0.4,
//                    fill: true
//                },
//                {
//                    label: 'Tạo người dùng',
//                    data: [0, 1, 3, 2, 1, 2, 1, 0, 0],
//                    borderColor: '#00a65a',
//                    backgroundColor: 'rgba(0, 166, 90, 0.1)',
//                    tension: 0.4,
//                    fill: true
//                },
//                {
//                    label: 'Cập nhật hệ thống',
//                    data: [1, 0, 2, 1, 3, 1, 0, 1, 0],
//                    borderColor: '#f39c12',
//                    backgroundColor: 'rgba(243, 156, 18, 0.1)',
//                    tension: 0.4,
//                    fill: true
//                }
//            ]
//        },
//        options: {
//            responsive: true,
//            maintainAspectRatio: false,
//            interaction: {
//                mode: 'index',
//                intersect: false,
//            },
//            plugins: {
//                legend: {
//                    position: 'top',
//                    labels: {
//                        usePointStyle: true,
//                        padding: 20
//                    }
//                }
//            },
//            scales: {
//                x: {
//                    display: true,
//                    title: {
//                        display: true,
//                        text: 'Thời gian'
//                    }
//                },
//                y: {
//                    display: true,
//                    title: {
//                        display: true,
//                        text: 'Số lượng hoạt động'
//                    },
//                    beginAtZero: true
//                }
//            }
//        }
//    });

    // Auto refresh charts every 5 minutes (optional)
    setInterval(function() {
        // In production, you would fetch new data via AJAX and update charts
        console.log('Charts would be refreshed with new data');
    }, 300000); // 5 minutes
</script>

<!-- Include sidebar JavaScript -->
<script src="${pageContext.request.contextPath}/js/sidebar.js"></script>

</body>
</html>
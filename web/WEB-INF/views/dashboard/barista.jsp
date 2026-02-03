<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Barista Dashboard - Coffee Shop Management</title>
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
        
        .bg-coffee { background-color: #8B4513 !important; }
        .bg-green { background-color: #00a65a !important; }
        .bg-yellow { background-color: #f39c12 !important; }
        .bg-aqua { background-color: #00c0ef !important; }
        .bg-blue { background-color: #3c8dbc !important; }
        .bg-purple { background-color: #605ca8 !important; }
        .bg-orange { background-color: #ff6b35 !important; }
        
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
        
        .order-item {
            background: #f9f9f9;
            border-left: 4px solid #3c8dbc;
            margin-bottom: 10px;
            padding: 10px;
            border-radius: 3px;
        }
        
        .order-urgent {
            border-left-color: #dd4b39;
            background: #fdf2f2;
        }
        
        .order-status {
            float: right;
            font-weight: bold;
        }
        
        .status-new { color: #f39c12; }
        .status-preparing { color: #3c8dbc; }
        .status-ready { color: #00a65a; }
        
        .menu-item {
            background: #fff;
            border: 1px solid #ddd;
            border-radius: 5px;
            padding: 15px;
            margin-bottom: 15px;
            text-align: center;
            transition: transform 0.2s;
        }
        
        .menu-item:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.15);
        }
        
        .menu-item img {
            width: 80px;
            height: 80px;
            border-radius: 50%;
            margin-bottom: 10px;
        }
        
        .price {
            color: #27ae60;
            font-weight: bold;
            font-size: 16px;
        }
        
        .shift-info {
            background: linear-gradient(45deg, #3c8dbc, #5bc0de);
            color: white;
            padding: 20px;
            border-radius: 5px;
            margin-bottom: 20px;
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
                Barista Dashboard
                <small>Quản lý pha chế</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${pageContext.request.contextPath}/barista/dashboard"><i class="fa fa-dashboard"></i> Home</a></li>
                <li class="active">Barista Dashboard</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">
            <!-- Issue Statistics -->
            <div class="row">
                <div class="col-md-12">
                    <h4><i class="fa fa-wrench"></i> Thống kê Issues</h4>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-3 col-xs-6">
                    <div class="small-box bg-aqua">
                        <div class="inner">
                            <h3>${totalIssues}</h3>
                            <p>Tổng Issue</p>
                        </div>
                        <div class="icon">
                            <i class="fa fa-tasks"></i>
                        </div>
                        <a href="${pageContext.request.contextPath}/barista/issues" class="small-box-footer">
                            Xem chi tiết <i class="fa fa-arrow-circle-right"></i>
                        </a>
                    </div>
                </div>
                
                <div class="col-lg-3 col-xs-6">
                    <div class="small-box bg-yellow">
                        <div class="inner">
                            <h3>${issuePendingCount}</h3>
                            <p>Đã báo cáo</p>
                        </div>
                        <div class="icon">
                            <i class="fa fa-exclamation-circle"></i>
                        </div>
                        <a href="${pageContext.request.contextPath}/barista/issues?status=reported" class="small-box-footer">
                            Xem chi tiết <i class="fa fa-arrow-circle-right"></i>
                        </a>
                    </div>
                </div>
                
                <div class="col-lg-3 col-xs-6">
                    <div class="small-box bg-blue">
                        <div class="inner">
                            <h3>${issueProcessingCount}</h3>
                            <p>Đang điều tra</p>
                        </div>
                        <div class="icon">
                            <i class="fa fa-search"></i>
                        </div>
                        <a href="${pageContext.request.contextPath}/barista/issues?status=investigation" class="small-box-footer">
                            Xem chi tiết <i class="fa fa-arrow-circle-right"></i>
                        </a>
                    </div>
                </div>
                
                <div class="col-lg-3 col-xs-6">
                    <div class="small-box bg-green">
                        <div class="inner">
                            <h3>${issueCompletedCount}</h3>
                            <p>Đã giải quyết</p>
                        </div>
                        <div class="icon">
                            <i class="fa fa-check-circle"></i>
                        </div>
                        <a href="${pageContext.request.contextPath}/barista/issues?status=resolved" class="small-box-footer">
                            Xem chi tiết <i class="fa fa-arrow-circle-right"></i>
                        </a>
                    </div>
                </div>
            </div>

            <!-- Order Statistics -->
            <div class="row">
                <div class="col-md-12">
                    <h4><i class="fa fa-shopping-cart"></i> Thống kê Đơn hàng</h4>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-3 col-xs-6">
                    <div class="small-box bg-purple">
                        <div class="inner">
                            <h3>${totalOrders}</h3>
                            <p>Tổng đơn hàng</p>
                        </div>
                        <div class="icon">
                            <i class="fa fa-shopping-bag"></i>
                        </div>
                        <a href="${pageContext.request.contextPath}/barista/orders" class="small-box-footer">
                            Xem chi tiết <i class="fa fa-arrow-circle-right"></i>
                        </a>
                    </div>
                </div>
                
                <div class="col-lg-3 col-xs-6">
                    <div class="small-box bg-yellow">
                        <div class="inner">
                            <h3>${orderPendingCount}</h3>
                            <p>Chờ xử lý</p>
                        </div>
                        <div class="icon">
                            <i class="fa fa-hourglass-start"></i>
                        </div>
                        <a href="${pageContext.request.contextPath}/barista/orders?status=pending" class="small-box-footer">
                            Xem chi tiết <i class="fa fa-arrow-circle-right"></i>
                        </a>
                    </div>
                </div>
                
                <div class="col-lg-3 col-xs-6">
                    <div class="small-box bg-blue">
                        <div class="inner">
                            <h3>${orderProcessingCount}</h3>
                            <p>Đang pha chế</p>
                        </div>
                        <div class="icon">
                            <i class="fa fa-coffee"></i>
                        </div>
                        <a href="${pageContext.request.contextPath}/barista/orders?status=processing" class="small-box-footer">
                            Xem chi tiết <i class="fa fa-arrow-circle-right"></i>
                        </a>
                    </div>
                </div>
                
                <div class="col-lg-3 col-xs-6">
                    <div class="small-box bg-green">
                        <div class="inner">
                            <h3>${orderCompletedCount}</h3>
                            <p>Hoàn thành</p>
                        </div>
                        <div class="icon">
                            <i class="fa fa-check-square"></i>
                        </div>
                        <a href="${pageContext.request.contextPath}/barista/orders?status=completed" class="small-box-footer">
                            Xem chi tiết <i class="fa fa-arrow-circle-right"></i>
                        </a>
                    </div>
                </div>
            </div>

            <!-- Charts Row -->
            <div class="row">
                <div class="col-md-6">
                    <div class="box box-success">
                        <div class="box-header with-border">
                            <h3 class="box-title"><i class="fa fa-bar-chart"></i> Biểu đồ Issue theo trạng thái</h3>
                        </div>
                        <div class="box-body">
                            <div style="position: relative; height: 300px;">
                                <canvas id="issueChart"></canvas>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="col-md-6">
                    <div class="box box-info">
                        <div class="box-header with-border">
                            <h3 class="box-title"><i class="fa fa-pie-chart"></i> Biểu đồ Đơn hàng theo trạng thái</h3>
                        </div>
                        <div class="box-body">
                            <div style="position: relative; height: 300px;">
                                <canvas id="orderChart"></canvas>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Recent Issues -->
            <div class="row">
                <div class="col-md-6">
                    <div class="box box-primary">
                        <div class="box-header with-border">
                            <h3 class="box-title"><i class="fa fa-wrench"></i> Issue gần đây</h3>
                            <div class="box-tools pull-right">
                                <button type="button" class="btn btn-box-tool" data-widget="collapse">
                                    <i class="fa fa-minus"></i>
                                </button>
                            </div>
                        </div>
                        <div class="box-body">
                            <c:choose>
                                <c:when test="${empty recentIssues}">
                                    <div class="alert alert-info">
                                        <i class="fa fa-info-circle"></i> Không có issue nào
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="table-responsive">
                                        <table class="table table-striped table-hover table-condensed">
                                            <thead>
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Nguyên liệu</th>
                                                    <th>Số lượng</th>
                                                    <th>Trạng thái</th>
                                                    <th>Hành động</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="issue" items="${recentIssues}">
                                                    <tr>
                                                        <td>#${issue.issueID}</td>
                                                        <td>${issue.ingredientName}</td>
                                                        <td>${issue.quantity}</td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${issue.statusName == 'Reported'}">
                                                                    <span class="label label-warning">Đã báo cáo</span>
                                                                </c:when>
                                                                <c:when test="${issue.statusName == 'Under Investigation'}">
                                                                    <span class="label label-info">Đang điều tra</span>
                                                                </c:when>
                                                                <c:when test="${issue.statusName == 'Resolved'}">
                                                                    <span class="label label-success">Đã giải quyết</span>
                                                                </c:when>
                                                                <c:when test="${issue.statusName == 'Rejected'}">
                                                                    <span class="label label-danger">Từ chối</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="label label-default">${issue.statusName}</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td>
                                                            <a href="${pageContext.request.contextPath}/barista/edit-issue?id=${issue.issueID}" 
                                                               class="btn btn-xs btn-primary">
                                                                <i class="fa fa-edit"></i>
                                                            </a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>

                <!-- Recent Orders -->
                <div class="col-md-6">
                    <div class="box box-success">
                        <div class="box-header with-border">
                            <h3 class="box-title"><i class="fa fa-shopping-cart"></i> Đơn hàng gần đây</h3>
                            <div class="box-tools pull-right">
                                <button type="button" class="btn btn-box-tool" data-widget="collapse">
                                    <i class="fa fa-minus"></i>
                                </button>
                            </div>
                        </div>
                        <div class="box-body">
                            <c:choose>
                                <c:when test="${empty recentOrders}">
                                    <div class="alert alert-info">
                                        <i class="fa fa-info-circle"></i> Không có đơn hàng nào
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="table-responsive">
                                        <table class="table table-striped table-hover table-condensed">
                                            <thead>
                                                <tr>
                                                    <th>Order ID</th>
                                                    <th>Cửa hàng</th>
                                                    <th>Trạng thái</th>
                                                    <th>Ngày tạo</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="order" items="${recentOrders}">
                                                    <tr>
                                                        <td>#${order.orderID}</td>
                                                        <td>${order.shopName}</td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${order.statusName == 'New'}">
                                                                    <span class="label label-warning">Mới</span>
                                                                </c:when>
                                                                <c:when test="${order.statusName == 'Preparing'}">
                                                                    <span class="label label-info">Đang pha</span>
                                                                </c:when>
                                                                <c:when test="${order.statusName == 'Ready'}">
                                                                    <span class="label label-primary">Sẵn sàng</span>
                                                                </c:when>
                                                                <c:when test="${order.statusName == 'Completed'}">
                                                                    <span class="label label-success">Hoàn thành</span>
                                                                </c:when>
                                                                <c:when test="${order.statusName == 'Cancelled'}">
                                                                    <span class="label label-danger">Đã hủy</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="label label-default">${order.statusName}</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td>
                                                            <c:if test="${not empty order.createdAt}">
                                                                ${order.createdAt.toString().substring(0, 16).replace('T', ' ')}
                                                            </c:if>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>

    <!-- Include Footer -->
    <%@include file="../compoment/footer.jsp" %>

</div>

<!-- Chart.js -->
<script src="https://cdn.jsdelivr.net/npm/chart.js@3.9.1/dist/chart.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/admin-lte@3.2/dist/js/adminlte.min.js"></script>

<script>
$(document).ready(function() {
    // Issue Chart - Bar Chart
    const issueCtx = document.getElementById('issueChart').getContext('2d');
    const issueChart = new Chart(issueCtx, {
        type: 'bar',
        data: {
            labels: ['Đã báo cáo', 'Đang điều tra', 'Đã giải quyết', 'Từ chối'],
            datasets: [{
                label: 'Số lượng Issue',
                data: [${issuePendingCount}, ${issueProcessingCount}, ${issueCompletedCount}, ${issueRejectedCount}],
                backgroundColor: [
                    'rgba(243, 156, 18, 0.7)',  // Yellow - Reported
                    'rgba(60, 141, 188, 0.7)',  // Blue - Under Investigation
                    'rgba(0, 166, 90, 0.7)',    // Green - Resolved
                    'rgba(221, 75, 57, 0.7)'    // Red - Rejected
                ],
                borderColor: [
                    'rgba(243, 156, 18, 1)',
                    'rgba(60, 141, 188, 1)',
                    'rgba(0, 166, 90, 1)',
                    'rgba(221, 75, 57, 1)'
                ],
                borderWidth: 2
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
                    display: true,
                    text: 'Phân bố Issue theo trạng thái',
                    font: {
                        size: 16
                    }
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

    // Order Chart - Doughnut Chart
    const orderCtx = document.getElementById('orderChart').getContext('2d');
    const orderChart = new Chart(orderCtx, {
        type: 'doughnut',
        data: {
            labels: ['Chờ xử lý', 'Đang pha chế', 'Hoàn thành', 'Đã hủy'],
            datasets: [{
                label: 'Số lượng đơn hàng',
                data: [${orderPendingCount}, ${orderProcessingCount}, ${orderCompletedCount}, ${orderCancelledCount}],
                backgroundColor: [
                    'rgba(243, 156, 18, 0.8)',  // Yellow - Pending
                    'rgba(60, 141, 188, 0.8)',  // Blue - Processing
                    'rgba(0, 166, 90, 0.8)',    // Green - Completed
                    'rgba(221, 75, 57, 0.8)'    // Red - Cancelled
                ],
                borderColor: [
                    'rgba(243, 156, 18, 1)',
                    'rgba(60, 141, 188, 1)',
                    'rgba(0, 166, 90, 1)',
                    'rgba(221, 75, 57, 1)'
                ],
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
                        padding: 15,
                        font: {
                            size: 12
                        }
                    }
                },
                title: {
                    display: true,
                    text: 'Phân bố đơn hàng theo trạng thái',
                    font: {
                        size: 16
                    }
                }
            }
        }
    });
});
</script>

</body>
</html>



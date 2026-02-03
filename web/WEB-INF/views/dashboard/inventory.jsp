<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Dashboard - Inventory Staff | Coffee Shop</title>
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
    <!-- Morris chart -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/morris.js/0.5.1/morris.css">
    <!-- Custom Dashboard CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/inventory-dashboard.css">
    
    <style>
        .info-box {
            display: block;
            min-height: 90px;
            background: #fff;
            width: 100%;
            box-shadow: 0 1px 1px rgba(0,0,0,0.1);
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
        
        .bg-aqua { background-color: #00c0ef !important; }
        .bg-green { background-color: #00a65a !important; }
        .bg-yellow { background-color: #f39c12 !important; }
        .bg-red { background-color: #dd4b39 !important; }
        
        .chart-container {
            position: relative;
            height: 300px;
            margin-bottom: 20px;
        }
        
        .small-box {
            border-radius: 2px;
            position: relative;
            display: block;
            margin-bottom: 20px;
            box-shadow: 0 1px 1px rgba(0,0,0,0.1);
        }
        
        .small-box > .inner {
            padding: 10px;
        }
        
        .small-box h3 {
            font-size: 30px;
            font-weight: bold;
            margin: 0 0 10px 0;
            white-space: nowrap;
            padding: 0;
        }
        
        .small-box p {
            font-size: 15px;
        }
        
        .small-box .icon {
            position: absolute;
            top: auto;
            bottom: 5px;
            right: 5px;
            z-index: 0;
            font-size: 90px;
            color: rgba(0,0,0,0.15);
        }
        
        .activity-item {
            border-left: 3px solid #e0e0e0;
            padding: 10px 15px;
            margin-bottom: 10px;
            background: #f9f9f9;
            border-radius: 0 5px 5px 0;
        }
        
        .activity-time {
            color: #666;
            font-size: 12px;
        }
        
        .low-stock-alert {
            border-left-color: #d73925;
            background: #fff5f5;
        }
        
        .reorder-alert {
            border-left-color: #f39c12;
            background: #fffbf0;
        }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

    <!-- Include Header -->
    <%@ include file="../compoment/header.jsp" %>

    <!-- Include Sidebar -->
    <%@ include file="../compoment/sidebar.jsp" %>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                Dashboard Inventory
                <small>Tổng quan kho hàng</small>
                <button class="btn btn-sm btn-info pull-right" onclick="refreshDashboard()" style="margin-left: 10px;">
                    <i class="fa fa-refresh"></i> Làm mới
                </button>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${pageContext.request.contextPath}/"><i class="fa fa-dashboard"></i> Home</a></li>
                <li class="active">Dashboard</li>
            </ol>
            
            <!-- Chart Status Alert -->
            <div id="chartStatusAlert" class="alert alert-info" style="display: none;">
                <i class="fa fa-info-circle"></i> Đang tải biểu đồ...
            </div>
        </section>

        <!-- Main content -->
        <section class="content">
            
            <!-- Info boxes -->
            <div class="row">
                <div class="col-md-3 col-sm-6 col-xs-12">
                    <div class="small-box bg-aqua">
                        <div class="inner">
                            <h3><c:out value="${totalIngredients != null ? totalIngredients : 0}"/></h3>
                            <p>Tổng nguyên liệu</p>
                        </div>
                        <div class="icon">
                            <i class="ion ion-bag"></i>
                        </div>
                        <a href="${pageContext.request.contextPath}/ingredient" class="small-box-footer">
                            Xem chi tiết <i class="fa fa-arrow-circle-right"></i>
                        </a>
                    </div>
                </div>

                <div class="col-md-3 col-sm-6 col-xs-12">
                    <div class="small-box bg-green">
                        <div class="inner">
                            <h3><c:out value="${stockGoodCount != null ? stockGoodCount : 0}"/></h3>
                            <p>Kho đủ hàng</p>
                        </div>
                        <div class="icon">
                            <i class="ion ion-stats-bars"></i>
                        </div>
                        <a href="${pageContext.request.contextPath}/ingredient" class="small-box-footer">
                            Xem chi tiết <i class="fa fa-arrow-circle-right"></i>
                        </a>
                    </div>
                </div>

                <div class="col-md-3 col-sm-6 col-xs-12">
                    <div class="small-box bg-yellow">
                        <div class="inner">
                            <h3><c:out value="${stockLowCount != null ? stockLowCount : 0}"/></h3>
                            <p>Sắp hết hàng</p>
                        </div>
                        <div class="icon">
                            <i class="ion ion-person-add"></i>
                        </div>
                        <a href="${pageContext.request.contextPath}/ingredient?action=low-stock" class="small-box-footer">
                            Xem chi tiết <i class="fa fa-arrow-circle-right"></i>
                        </a>
                    </div>
                </div>

                <div class="col-md-3 col-sm-6 col-xs-12">
                    <div class="small-box bg-red">
                        <div class="inner">
                            <h3><c:out value="${stockOutCount != null ? stockOutCount : 0}"/></h3>
                            <p>Hết hàng</p>
                        </div>
                        <div class="icon">
                            <i class="ion ion-pie-graph"></i>
                        </div>
                        <a href="${pageContext.request.contextPath}/ingredient?action=low-stock" class="small-box-footer">
                            Xem chi tiết <i class="fa fa-arrow-circle-right"></i>
                        </a>
                    </div>
                </div>
            </div>

            <!-- Charts Row -->
            <div class="row">
                <!-- Stock Status Chart -->
                <div class="col-md-6">
                    <div class="box box-primary">
                        <div class="box-header with-border">
                            <h3 class="box-title">Tình trạng kho hàng</h3>
                            <div class="box-tools pull-right">
                                <button type="button" class="btn btn-box-tool" data-widget="collapse">
                                    <i class="fa fa-minus"></i>
                                </button>
                            </div>
                        </div>
                        <div class="box-body">
                            <div class="chart-container">
                                <div id="stockStatusLoading" class="text-center" style="padding: 50px;">
                                    <i class="fa fa-spinner fa-spin fa-2x"></i>
                                    <p>Đang tải biểu đồ...</p>
                                </div>
                                <canvas id="stockStatusChart" style="display: none;"></canvas>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Category Distribution Chart -->
                <div class="col-md-6">
                    <div class="box box-success">
                        <div class="box-header with-border">
                            <h3 class="box-title">Phân bố theo loại nguyên liệu</h3>
                            <div class="box-tools pull-right">
                                <button type="button" class="btn btn-box-tool" data-widget="collapse">
                                    <i class="fa fa-minus"></i>
                                </button>
                            </div>
                        </div>
                        <div class="box-body">
                            <div class="chart-container">
                                <div id="categoryLoading" class="text-center" style="padding: 50px;">
                                    <i class="fa fa-spinner fa-spin fa-2x"></i>
                                    <p>Đang tải biểu đồ...</p>
                                </div>
                                <canvas id="categoryChart" style="display: none;"></canvas>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Stock Levels Chart -->
            <div class="row">
                <div class="col-md-12">
                    <div class="box box-info">
                        <div class="box-header with-border">
                            <h3 class="box-title">Mức tồn kho theo nguyên liệu</h3>
                            <div class="box-tools pull-right">
                                <button type="button" class="btn btn-box-tool" data-widget="collapse">
                                    <i class="fa fa-minus"></i>
                                </button>
                            </div>
                        </div>
                        <div class="box-body">
                            <div class="chart-container" style="height: 400px;">
                                <div id="stockLevelsLoading" class="text-center" style="padding: 50px;">
                                    <i class="fa fa-spinner fa-spin fa-2x"></i>
                                    <p>Đang tải biểu đồ...</p>
                                </div>
                                <canvas id="stockLevelsChart" style="display: none;"></canvas>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Recent Activities and Alerts -->
            <div class="row">
                <!-- Recent Activities -->
                <div class="col-md-6">
                    <div class="box box-warning">
                        <div class="box-header with-border">
                            <h3 class="box-title">Hoạt động gần đây</h3>
                            <div class="box-tools pull-right">
                                <button type="button" class="btn btn-box-tool" data-widget="collapse">
                                    <i class="fa fa-minus"></i>
                                </button>
                            </div>
                        </div>
                        <div class="box-body">
                            <div class="activity-item">
                                <strong>Cập nhật tồn kho</strong>
                                <span class="activity-time pull-right">10 phút trước</span>
                                <br>
                                <small>Đã cập nhật số lượng cà phê rang xay: +50kg</small>
                            </div>
                            <div class="activity-item low-stock-alert">
                                <strong>Cảnh báo hết hàng</strong>
                                <span class="activity-time pull-right">1 giờ trước</span>
                                <br>
                                <small>Sữa tươi chỉ còn 5 lít, cần nhập thêm</small>
                            </div>
                            <div class="activity-item reorder-alert">
                                <strong>Đề xuất đặt hàng</strong>
                                <span class="activity-time pull-right">2 giờ trước</span>
                                <br>
                                <small>Đường trắng dưới mức tối thiểu, nên đặt hàng mới</small>
                            </div>
                            <div class="activity-item">
                                <strong>Nhập hàng</strong>
                                <span class="activity-time pull-right">1 ngày trước</span>
                                <br>
                                <small>Đã nhập 100kg cà phê hạt từ nhà cung cấp ABC</small>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Low Stock Alerts -->
                <div class="col-md-6">
                    <div class="box box-danger">
                        <div class="box-header with-border">
                            <h3 class="box-title">Cảnh báo tồn kho thấp</h3>
                            <div class="box-tools pull-right">
                                <button type="button" class="btn btn-box-tool" data-widget="collapse">
                                    <i class="fa fa-minus"></i>
                                </button>
                            </div>
                        </div>
                        <div class="box-body">
                            <c:choose>
                                <c:when test="${not empty lowStockIngredients}">
                                    <c:forEach var="ingredient" items="${lowStockIngredients}" end="4">
                                        <div class="activity-item low-stock-alert">
                                            <strong><c:out value="${ingredient.name}"/></strong>
                                            <span class="pull-right">
                                                <span class="badge bg-red">
                                                    <fmt:formatNumber value="${ingredient.stockQuantity}" pattern="#.##"/> 
                                                    <c:choose>
                                                        <c:when test="${ingredient.unitID == 1}">kg</c:when>
                                                        <c:when test="${ingredient.unitID == 2}">lít</c:when>
                                                        <c:when test="${ingredient.unitID == 3}">chai</c:when>
                                                        <c:when test="${ingredient.unitID == 4}">cái</c:when>
                                                        <c:otherwise>đơn vị</c:otherwise>
                                                    </c:choose>
                                                </span>
                                            </span>
                                            <br>
                                            <small>Mức tối thiểu: 10 đơn vị</small>
                                        </div>
                                    </c:forEach>
                                    <c:if test="${fn:length(lowStockIngredients) > 5}">
                                        <div class="text-center">
                                            <a href="${pageContext.request.contextPath}/ingredient?action=low-stock" class="btn btn-sm btn-warning">
                                                Xem tất cả (<c:out value="${fn:length(lowStockIngredients)}"/> mục)
                                            </a>
                                        </div>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <div class="text-center text-muted">
                                        <i class="fa fa-check-circle fa-3x"></i>
                                        <p>Tất cả nguyên liệu đều có đủ tồn kho</p>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

    <!-- Include Footer -->
    <%@ include file="../compoment/footer.jsp" %>

</div>
<!-- ./wrapper -->

<!-- jQuery 2.2.3 (use CDN as fallback) -->
<script src="${pageContext.request.contextPath}/plugins/jQuery/jquery-2.2.3.min.js"></script>
<script>
// jQuery fallback
if (typeof jQuery === 'undefined') {
    console.log('Local jQuery failed, loading from CDN...');
    document.write('<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js"><\/script>');
}
</script>

<!-- Bootstrap 3.3.6 -->
<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
<script>
// Bootstrap fallback
if (typeof jQuery === 'undefined' || typeof jQuery.fn.modal === 'undefined') {
    console.log('Local Bootstrap failed, loading from CDN...');
    document.write('<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"><\/script>');
}
</script>

<!-- AdminLTE App -->
<script src="${pageContext.request.contextPath}/dist/js/app.min.js"></script>
<script>
// AdminLTE fallback - if app.min.js fails, continue without it
if (typeof jQuery === 'undefined') {
    console.warn('AdminLTE may not work properly without jQuery');
}
</script>

<!-- Chart.js (load after jQuery) -->
<script src="https://cdn.jsdelivr.net/npm/chart.js@3.9.1/dist/chart.min.js"></script>
<script>
// Fallback if CDN fails
if (typeof Chart === 'undefined') {
    console.log('Primary Chart.js CDN failed, trying backup...');
    var script = document.createElement('script');
    script.src = 'https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.9.1/chart.min.js';
    script.onload = function() {
        console.log('Backup Chart.js loaded successfully');
        // Reinitialize dashboard after backup loads
        if (typeof initializeDashboard === 'function') {
            initializeDashboard();
        }
    };
    script.onerror = function() {
        console.error('All Chart.js CDNs failed');
        showAllChartsError();
    };
    document.head.appendChild(script);
}
</script>

<!-- Hidden data for charts -->
<script type="application/json" id="chartData">
{
    "stockGoodCount": <c:out value="${stockGoodCount != null ? stockGoodCount : 0}"/>,
    "stockLowCount": <c:out value="${stockLowCount != null ? stockLowCount : 0}"/>,
    "stockOutCount": <c:out value="${stockOutCount != null ? stockOutCount : 0}"/>,
    "ingredients": [
        <c:forEach var="ingredient" items="${ingredients}" varStatus="status">
        {
            "name": "<c:out value='${ingredient.name}'/>",
            "currentStock": <c:out value="${ingredient.stockQuantity}"/>,
            "minStock": 10
        }<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ]
}
</script>

<!-- Dashboard JavaScript (load last) -->
<script src="${pageContext.request.contextPath}/js/inventory-dashboard.js"></script>

<!-- Debug Script -->
<script>
console.log('Dashboard page loaded');
console.log('jQuery available:', typeof jQuery !== 'undefined');
console.log('$ available:', typeof $ !== 'undefined');
console.log('Chart.js available:', typeof Chart !== 'undefined');

// Wait for jQuery to be available before checking DOM elements
function checkDOMElements() {
    if (typeof jQuery !== 'undefined') {
        jQuery(document).ready(function($) {
            console.log('DOM ready with jQuery');
            console.log('stockStatusChart canvas:', document.getElementById('stockStatusChart'));
            console.log('categoryChart canvas:', document.getElementById('categoryChart'));
            console.log('stockLevelsChart canvas:', document.getElementById('stockLevelsChart'));
            console.log('chartData element:', document.getElementById('chartData'));
            
            // Try to get chart data
            try {
                var chartDataEl = document.getElementById('chartData');
                if (chartDataEl) {
                    var dataText = chartDataEl.textContent || chartDataEl.innerText;
                    console.log('Chart data text:', dataText);
                    var parsedData = JSON.parse(dataText);
                    console.log('Parsed chart data:', parsedData);
                }
            } catch (e) {
                console.error('Error parsing chart data:', e);
            }
        });
    } else {
        console.log('jQuery not available yet, retrying...');
        setTimeout(checkDOMElements, 100);
    }
}

checkDOMElements();

// Fallback: If all else fails, try to initialize charts with pure JavaScript after 3 seconds
setTimeout(function() {
    if (typeof Chart !== 'undefined' && document.getElementById('stockStatusChart')) {
        console.log('Attempting fallback chart initialization...');
        try {
            // Simple chart without jQuery dependency
            var ctx = document.getElementById('stockStatusChart').getContext('2d');
            document.getElementById('stockStatusLoading').style.display = 'none';
            document.getElementById('stockStatusChart').style.display = 'block';
            
            new Chart(ctx, {
                type: 'doughnut',
                data: {
                    labels: ['Đủ hàng', 'Sắp hết', 'Hết hàng'],
                    datasets: [{
                        data: [18, 2, 0],
                        backgroundColor: ['#00a65a', '#f39c12', '#dd4b39']
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false
                }
            });
            console.log('Fallback chart created successfully');
        } catch (e) {
            console.error('Fallback chart failed:', e);
        }
    }
}, 3000);
</script>

</body>
</html>
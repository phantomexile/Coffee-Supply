<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ca Làm Việc Của Tôi - CoffeeLux</title>
    <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
    
    <!-- Bootstrap 3.3.4 -->
    <link href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
    <!-- Font Awesome Icons -->
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
    <!-- Theme style -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/admin-lte/2.3.11/css/AdminLTE.min.css" rel="stylesheet" type="text/css" />
    <link href="https://cdnjs.cloudflare.com/ajax/libs/admin-lte/2.3.11/css/skins/_all-skins.min.css" rel="stylesheet" type="text/css" />
    
    <style>
        .shift-card {
            border-left: 4px solid #3c8dbc;
            margin-bottom: 15px;
            transition: transform 0.2s;
        }
        .shift-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        .shift-card.today {
            border-left-color: #00a65a;
            background-color: #f0f9ff;
        }
        .shift-card.upcoming {
            border-left-color: #f39c12;
        }
        .shift-card.past {
            border-left-color: #dd4b39;
            opacity: 0.7;
        }
        .shift-time {
            font-size: 24px;
            font-weight: bold;
            color: #3c8dbc;
        }
        .shift-date {
            font-size: 18px;
            color: #666;
        }
        .shift-name {
            font-size: 20px;
            font-weight: bold;
            color: #333;
        }
        .info-box-custom {
            display: block;
            min-height: 90px;
            background: #fff;
            width: 100%;
            box-shadow: 0 1px 1px rgba(0,0,0,0.1);
            border-radius: 2px;
            margin-bottom: 15px;
        }
        .info-box-custom-icon {
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
        .info-box-custom-content {
            padding: 5px 10px;
            margin-left: 90px;
        }
        .calendar-icon {
            display: inline-block;
            padding: 10px;
            background: #3c8dbc;
            color: white;
            border-radius: 5px;
            text-align: center;
            min-width: 60px;
        }
        .calendar-day {
            font-size: 24px;
            font-weight: bold;
            line-height: 1;
        }
        .calendar-month {
            font-size: 12px;
            text-transform: uppercase;
        }
        .no-shifts-message {
            text-align: center;
            padding: 50px 20px;
            color: #999;
        }
        .no-shifts-message i {
            font-size: 64px;
            margin-bottom: 20px;
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
                <i class="fa fa-calendar"></i> Ca Làm Việc Của Tôi
                <small>Lịch trình ca làm việc được phân công</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${pageContext.request.contextPath}/dashboard"><i class="fa fa-dashboard"></i> Dashboard</a></li>
                <li class="active">Ca làm việc của tôi</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">
            <!-- User Info Box -->
            <div class="row">
                <div class="col-md-12">
                    <div class="box box-primary">
                        <div class="box-body">
                            <div class="info-box-custom">
                                <span class="info-box-custom-icon bg-aqua">
                                    <i class="fa fa-user"></i>
                                </span>
                                <div class="info-box-custom-content">
                                    <span class="info-box-text">Nhân viên</span>
                                    <span class="info-box-number">${currentUser.fullName}</span>
                                    <span class="info-box-text"><i class="fa fa-envelope"></i> ${currentUser.email}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Statistics -->
            <div class="row">
                <div class="col-md-4 col-sm-6 col-xs-12">
                    <div class="info-box">
                        <span class="info-box-icon bg-aqua"><i class="fa fa-calendar-check-o"></i></span>
                        <div class="info-box-content">
                            <span class="info-box-text">Tổng số ca</span>
                            <span class="info-box-number">${myAssignments.size()}</span>
                        </div>
                    </div>
                </div>

                <div class="col-md-4 col-sm-6 col-xs-12">
                    <div class="info-box">
                        <span class="info-box-icon bg-green"><i class="fa fa-calendar"></i></span>
                        <div class="info-box-content">
                            <span class="info-box-text">Ca sắp tới</span>
                            <span class="info-box-number">
                                <c:set var="upcomingCount" value="0"/>
                                <c:forEach var="assignment" items="${myAssignments}">
                                    <c:if test="${assignment.assignedDate >= pageContext.request.getAttribute('currentDate')}">
                                        <c:set var="upcomingCount" value="${upcomingCount + 1}"/>
                                    </c:if>
                                </c:forEach>
                                ${upcomingCount}
                            </span>
                        </div>
                    </div>
                </div>

                <div class="col-md-4 col-sm-6 col-xs-12">
                    <div class="info-box">
                        <span class="info-box-icon bg-yellow"><i class="fa fa-clock-o"></i></span>
                        <div class="info-box-content">
                            <span class="info-box-text">Ca hôm nay</span>
                            <span class="info-box-number">
                                <c:set var="todayCount" value="0"/>
                                <jsp:useBean id="now" class="java.util.Date"/>
                                <fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="todayDate"/>
                                <c:forEach var="assignment" items="${myAssignments}">
                                    <fmt:formatDate value="${assignment.assignedDate}" pattern="yyyy-MM-dd" var="assignDate"/>
                                    <c:if test="${assignDate == todayDate}">
                                        <c:set var="todayCount" value="${todayCount + 1}"/>
                                    </c:if>
                                </c:forEach>
                                ${todayCount}
                            </span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Shifts List -->
            <div class="box box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title">
                        <i class="fa fa-list"></i> Danh sách ca làm việc
                    </h3>
                </div>
                <div class="box-body">
                    <c:choose>
                        <c:when test="${empty myAssignments}">
                            <div class="no-shifts-message">
                                <i class="fa fa-calendar-times-o"></i>
                                <h3>Chưa có ca làm việc nào được phân công</h3>
                                <p>Vui lòng liên hệ với quản lý để được phân công ca làm việc.</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="row">
                                <c:forEach var="assignment" items="${myAssignments}">
                                    <jsp:useBean id="now2" class="java.util.Date"/>
                                    <fmt:formatDate value="${now2}" pattern="yyyy-MM-dd" var="todayDate2"/>
                                    <fmt:formatDate value="${assignment.assignedDate}" pattern="yyyy-MM-dd" var="assignDate2"/>
                                    
                                    <c:set var="cardClass" value="shift-card"/>
                                    <c:choose>
                                        <c:when test="${assignDate2 == todayDate2}">
                                            <c:set var="cardClass" value="shift-card today"/>
                                            <c:set var="cardLabel" value="Hôm nay"/>
                                            <c:set var="labelClass" value="label-success"/>
                                        </c:when>
                                        <c:when test="${assignment.assignedDate > now2}">
                                            <c:set var="cardClass" value="shift-card upcoming"/>
                                            <c:set var="cardLabel" value="Sắp tới"/>
                                            <c:set var="labelClass" value="label-warning"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="cardClass" value="shift-card past"/>
                                            <c:set var="cardLabel" value="Đã qua"/>
                                            <c:set var="labelClass" value="label-default"/>
                                        </c:otherwise>
                                    </c:choose>
                                    
                                    <div class="col-md-6">
                                        <div class="box ${cardClass}">
                                            <div class="box-body">
                                                <div class="row">
                                                    <!-- Date Icon -->
                                                    <div class="col-xs-3">
                                                        <div class="calendar-icon">
                                                            <div class="calendar-day">
                                                                <fmt:formatDate value="${assignment.assignedDate}" pattern="dd"/>
                                                            </div>
                                                            <div class="calendar-month">
                                                                <fmt:formatDate value="${assignment.assignedDate}" pattern="MMM"/>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    
                                                    <!-- Shift Info -->
                                                    <div class="col-xs-9">
                                                        <div class="shift-name">
                                                            ${assignment.shiftName}
                                                            <span class="label ${labelClass} pull-right">${cardLabel}</span>
                                                        </div>
                                                        <div class="shift-date">
                                                            <i class="fa fa-calendar"></i>
                                                            <fmt:formatDate value="${assignment.assignedDate}" pattern="EEEE, dd/MM/yyyy"/>
                                                        </div>
                                                        <div class="shift-time">
                                                            <i class="fa fa-clock-o"></i>
                                                            ${assignment.startTime} - ${assignment.endTime}
                                                        </div>
                                                        <c:if test="${not empty assignment.notes}">
                                                            <div style="margin-top: 10px;">
                                                                <span class="label label-info"><i class="fa fa-sticky-note"></i> Ghi chú</span>
                                                                <p style="margin-top: 5px; color: #666;">${assignment.notes}</p>
                                                            </div>
                                                        </c:if>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <c:if test="${not empty myAssignments}">
                    <div class="box-footer">
                        <p class="text-muted">
                            <i class="fa fa-info-circle"></i> 
                            Vui lòng xác nhận với quản lý nếu có thay đổi về lịch trình làm việc.
                        </p>
                    </div>
                </c:if>
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

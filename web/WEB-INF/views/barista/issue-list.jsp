<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Danh sách báo cáo nhập kho - Coffee Shop Management</title>
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.6 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
    <!-- AdminLTE -->
    <link rel="stylesheet" href="https://adminlte.io/themes/AdminLTE/dist/css/AdminLTE.min.css">
    <link rel="stylesheet" href="https://adminlte.io/themes/AdminLTE/dist/css/skins/_all-skins.min.css">
    
    <!-- Custom Pagination CSS -->
    <style>
        .dataTables_info {
            padding-top: 8px;
            font-size: 14px;
            color: #555;
            margin-bottom: 15px;
            text-align: center;
        }
        
        .dataTables_paginate {
            text-align: center;
            margin-top: 10px;
        }
        
        .pagination {
            margin: 0;
        }
        
        .pagination > li > a,
        .pagination > li > span {
            color: #337ab7;
            padding: 6px 12px;
        }
        
        .pagination > li.active > a,
        .pagination > li.active > span {
            background-color: #337ab7;
            border-color: #337ab7;
        }
        
        .pagination > li.disabled > span {
            color: #777;
            cursor: not-allowed;
        }
        
        .pagination > li > a:hover {
            color: #23527c;
            background-color: #eee;
            border-color: #ddd;
        }
        
        .paginate_button {
            display: inline;
        }
        
        @media (max-width: 768px) {
            .dataTables_info {
                text-align: center;
                margin-bottom: 10px;
            }
            
            .dataTables_paginate {
                text-align: center;
            }
        }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

    <!-- Include Header -->
    <%@include file="../compoment/header.jsp" %>
    
    <!-- Include Sidebar -->
    <%@include file="../compoment/sidebar.jsp" %>

    <!-- Content Wrapper -->
    <div class="content-wrapper">
        <!-- Content Header -->
        <section class="content-header">
            <h1>
                Danh sách báo cáo nhập kho
                <small>Quản lý nhập kho</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${pageContext.request.contextPath}/barista/dashboard"><i class="fa fa-dashboard"></i> Trang chủ</a></li>
                <li class="active">Báo cáo sự cố</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-header">
                            <h3 class="box-title">
                                Tất cả báo cáo nhập kho 
                                <small>(${totalIssues} bản ghi)</small>
                            </h3>
                            <div class="box-tools pull-right">
                                <a href="${pageContext.request.contextPath}/barista/create-issue" class="btn btn-primary btn-sm">
                                    <i class="fa fa-plus"></i> Tạo yêu cầu nhập nguyên liệu
                                </a>
                            </div>
                        </div>
                        
                        <div class="box-body">
                            <!-- Success Message -->
                            <c:if test="${not empty sessionScope.successMessage}">
                                <div class="alert alert-success alert-dismissible">
                                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                                    <i class="icon fa fa-check"></i> ${sessionScope.successMessage}
                                </div>
                                <c:remove var="successMessage" scope="session"/>
                            </c:if>
                            
                            <!-- Error Message -->
                            <c:if test="${not empty sessionScope.errorMessage}">
                                <div class="alert alert-danger alert-dismissible">
                                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                                    <i class="icon fa fa-ban"></i> ${sessionScope.errorMessage}
                                </div>
                                <c:remove var="errorMessage" scope="session"/>
                            </c:if>
                            
                            <!-- Filter and Page Size -->
                            <div class="row" style="margin-bottom: 15px;">
                                <div class="col-md-3 col-md-offset-2">
                                    <form method="get" action="${pageContext.request.contextPath}/barista/issues">
                                        <input type="hidden" name="page" value="1">
                                        <input type="hidden" name="pageSize" value="${pageSize}">
                                        <div class="form-group text-center">
                                            <label>Lọc theo trạng thái:</label>
                                            <select class="form-control" name="status" onchange="this.form.submit()">
                                                <option value="">Tất cả</option>
                                                <c:forEach var="status" items="${issueStatuses}">
                                                    <option value="${status.settingID}" 
                                                            ${statusFilter == status.settingID ? 'selected' : ''}>
                                                        ${status.value}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </form>
                                </div>
                                <div class="col-md-3">
                                    <form method="get" action="${pageContext.request.contextPath}/barista/issues">
                                        <input type="hidden" name="page" value="1">
                                        <input type="hidden" name="status" value="${statusFilter}">
                                        <div class="form-group text-center">
                                            <label>Hiển thị:</label>
                                            <select class="form-control" name="pageSize" onchange="this.form.submit()">
                                                <option value="5" ${pageSize == 5 ? 'selected' : ''}>5 báo cáo/trang</option>
                                                <option value="10" ${pageSize == 10 ? 'selected' : ''}>10 báo cáo/trang</option>
                                                <option value="25" ${pageSize == 25 ? 'selected' : ''}>25 báo cáo/trang</option>
                                                <option value="50" ${pageSize == 50 ? 'selected' : ''}>50 báo cáo/trang</option>
                                            </select>
                                        </div>
                                    </form>
                                </div>
                                <div class="col-md-2">
                                    <div class="form-group text-center">
                                        <label>&nbsp;</label>
                                        <div class="form-control-static">
                                            <strong>Trang ${currentPage}/${totalPages}</strong>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Issues Table -->
                            <div class="table-responsive">
                                <table class="table table-bordered table-striped">
                                    <thead>
                                        <tr>
                                            <th class="sortable-stt" onclick="sortBySTT('issueTableBody')" style="cursor: pointer;">
                                                ID <i class="fa fa-sort"></i>
                                            </th>
                                            <th>Loại</th>
                                            <th>Nguyên liệu</th>
                                            
                                            <th>Số lượng</th>
                                            <th>Trạng thái</th>
                                            <th>Người tạo</th>
                                            <th>Người xác nhận</th>
                                            <th>Ngày tạo</th>
                                            <th>Thao tác</th>
                                        </tr>
                                    </thead>
                                    <tbody id="issueTableBody">
                                        <c:forEach var="issue" items="${issues}">
                                            <tr>
                                                <td>${issue.issueID}</td>
                                                <td>
                                                    <span class="label label-${issue.issueType == 'RESTOCK_REQUEST' ? 'primary' : 'warning'}">
                                                        ${issue.issueType == 'RESTOCK_REQUEST' ? 'Nhập thêm' : 'Báo lỗi'}
                                                    </span>
                                                </td>
                                                <td><strong>${issue.ingredientName}</strong></td>
                                                
                                                <td>${issue.quantity} ${issue.unitName}</td>
                                                <td>
                                                    <span class="label label-${issue.statusName == 'Reported' ? 'warning' : 
                                                                                 issue.statusName == 'Under Investigation' ? 'info' :
                                                                                 issue.statusName == 'Resolved' ? 'success' : 'danger'}">
                                                        ${issue.statusName}
                                                    </span>
                                                </td>
                                                <td>${issue.createdByName}</td>
                                                <td>${issue.confirmedByName != null ? issue.confirmedByName : '<i>Chưa xác nhận</i>'}</td>
                                                <td><fmt:formatDate value="${issue.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/barista/issue-details?id=${issue.issueID}" 
                                                       class="btn btn-info btn-xs">
                                                        <i class="fa fa-eye"></i> Xem
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty issues}">
                                            <tr>
                                                <td colspan="9" class="text-center">
                                                    <em>Không có báo cáo sự cố nào</em>
                                                </td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                            
                            <!-- Pagination -->
                            <c:if test="${totalPages > 1}">
                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="dataTables_info text-center">
                                            Hiển thị <strong>${(currentPage - 1) * pageSize + 1}</strong> đến 
                                            <strong>${currentPage * pageSize > totalIssues ? totalIssues : currentPage * pageSize}</strong> 
                                            trong tổng số <strong>${totalIssues}</strong> báo cáo
                                        </div>
                                        <div class="dataTables_paginate paging_simple_numbers text-center">
                                            <ul class="pagination">
                                                <!-- Build URL parameters -->
                                                <c:set var="urlParams" value="" />
                                                <c:if test="${not empty statusFilter}">
                                                    <c:set var="urlParams" value="${urlParams}&status=${statusFilter}" />
                                                </c:if>
                                                <c:if test="${not empty pageSize}">
                                                    <c:set var="urlParams" value="${urlParams}&pageSize=${pageSize}" />
                                                </c:if>
                                                
                                                <!-- First Page -->
                                                <c:if test="${currentPage > 1}">
                                                    <li class="paginate_button previous">
                                                        <a href="?page=1${urlParams}">
                                                            <i class="fa fa-angle-double-left"></i>
                                                        </a>
                                                    </li>
                                                </c:if>
                                                
                                                <!-- Previous Page -->
                                                <c:if test="${currentPage > 1}">
                                                    <li class="paginate_button previous">
                                                        <a href="?page=${currentPage - 1}${urlParams}">
                                                            <i class="fa fa-angle-left"></i>
                                                        </a>
                                                    </li>
                                                </c:if>
                                                
                                                <!-- Page Numbers -->
                                                <c:set var="startPage" value="${currentPage - 2 < 1 ? 1 : currentPage - 2}" />
                                                <c:set var="endPage" value="${currentPage + 2 > totalPages ? totalPages : currentPage + 2}" />
                                                
                                                <c:if test="${startPage > 1}">
                                                    <li class="paginate_button">
                                                        <a href="?page=1${urlParams}">
                                                            1
                                                        </a>
                                                    </li>
                                                    <c:if test="${startPage > 2}">
                                                        <li class="paginate_button disabled">
                                                            <span>...</span>
                                                        </li>
                                                    </c:if>
                                                </c:if>
                                                
                                                <c:forEach begin="${startPage}" end="${endPage}" var="pageNum">
                                                    <li class="paginate_button ${pageNum == currentPage ? 'active' : ''}">
                                                        <a href="?page=${pageNum}${urlParams}">
                                                            ${pageNum}
                                                        </a>
                                                    </li>
                                                </c:forEach>
                                                
                                                <c:if test="${endPage < totalPages}">
                                                    <c:if test="${endPage < totalPages - 1}">
                                                        <li class="paginate_button disabled">
                                                            <span>...</span>
                                                        </li>
                                                    </c:if>
                                                    <li class="paginate_button">
                                                        <a href="?page=${totalPages}${urlParams}">
                                                            ${totalPages}
                                                        </a>
                                                    </li>
                                                </c:if>
                                                
                                                <!-- Next Page -->
                                                <c:if test="${currentPage < totalPages}">
                                                    <li class="paginate_button next">
                                                        <a href="?page=${currentPage + 1}${urlParams}">
                                                            <i class="fa fa-angle-right"></i>
                                                        </a>
                                                    </li>
                                                </c:if>
                                                
                                                <!-- Last Page -->
                                                <c:if test="${currentPage < totalPages}">
                                                    <li class="paginate_button next">
                                                        <a href="?page=${totalPages}${urlParams}">
                                                            <i class="fa fa-angle-double-right"></i>
                                                        </a>
                                                    </li>
                                                </c:if>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                            
                            <!-- Show info even with single page -->
                            <c:if test="${totalPages <= 1 && totalIssues > 0}">
                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="dataTables_info text-center">
                                            Hiển thị tất cả <strong>${totalIssues}</strong> báo cáo
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
    
    <!-- Include Footer -->
    <%@include file="../compoment/footer.jsp" %>

</div>

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-2.2.4.min.js"></script>
<!-- Bootstrap -->
<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
<!-- AdminLTE App -->
<script src="https://adminlte.io/themes/AdminLTE/dist/js/app.min.js"></script>
<!-- Table Sort -->
<script src="${pageContext.request.contextPath}/js/table-sort.js"></script>

</body>
</html>

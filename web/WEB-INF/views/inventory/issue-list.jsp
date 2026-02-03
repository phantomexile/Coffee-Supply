<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Quản lý vấn đề nhập kho - Coffee Shop</title>
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
        .issue-card {
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            transition: all 0.3s ease;
            background: #fff;
            margin-bottom: 15px;
        }
        
        .issue-card:hover {
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            transform: translateY(-2px);
        }
        
        .status-reported {
            background-color: #fff5f5;
            border-left: 4px solid #e53e3e;
        }
        
        .status-investigation {
            background-color: #fffbf0;
            border-left: 4px solid #ff8c00;
        }
        
        .status-resolved {
            background-color: #f0fff4;
            border-left: 4px solid #38a169;
        }
        
        .status-rejected {
            background-color: #f5f5f5;
            border-left: 4px solid #718096;
        }
        
        .filter-section {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
        }
        
        .action-buttons .btn {
            margin-right: 5px;
            margin-bottom: 5px;
        }
        
        .badge-status {
            padding: 5px 10px;
            border-radius: 4px;
            font-size: 12px;
        }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
    <div class="wrapper">
        <%@include file="../compoment/sidebar.jsp" %>
        <%@include file="../compoment/header.jsp" %>
        
        <div class="content-wrapper">
            <section class="content-header">
                <h1>
                    Quản lý vấn đề nguyên liệu
                    <small>Danh sách tất cả các vấn đề</small>
                </h1>
                <ol class="breadcrumb">
                    <li><a href="${pageContext.request.contextPath}/inventory-dashboard"><i class="fa fa-dashboard"></i> Dashboard</a></li>
                    <li class="active">Quản lý vấn đề</li>
                </ol>
            </section>

            <section class="content">
                <!-- Alert Messages -->
                <c:if test="${param.success eq '1'}">
                    <div class="alert alert-success alert-dismissible">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                        <h4><i class="icon fa fa-check"></i> Thành công!</h4>
                        Tạo vấn đề thành công
                    </div>
                </c:if>
                
                <c:if test="${param.success eq '2'}">
                    <div class="alert alert-success alert-dismissible">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                        <h4><i class="icon fa fa-check"></i> Thành công!</h4>
                        Cập nhật vấn đề thành công
                    </div>
                </c:if>
                
                <c:if test="${param.error eq '1'}">
                    <div class="alert alert-danger alert-dismissible">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                        <h4><i class="icon fa fa-ban"></i> Lỗi!</h4>
                        Không thể tạo vấn đề. Vui lòng thử lại.
                    </div>
                </c:if>
                
                <c:if test="${param.error eq '2'}">
                    <div class="alert alert-danger alert-dismissible">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                        <h4><i class="icon fa fa-ban"></i> Lỗi!</h4>
                        Không thể cập nhật vấn đề. Vui lòng thử lại.
                    </div>
                </c:if>
                
                <c:if test="${param.error eq '3'}">
                    <div class="alert alert-danger alert-dismissible">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                        <h4><i class="icon fa fa-ban"></i> Lỗi!</h4>
                        Có lỗi xảy ra. Vui lòng thử lại.
                    </div>
                </c:if>
                
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                        <h4><i class="icon fa fa-ban"></i> Lỗi!</h4>
                        ${error}
                    </div>
                </c:if>

                <!-- Filter Section -->
                <div class="filter-section">
                    <form method="get" action="${pageContext.request.contextPath}/issue">
                        <input type="hidden" name="action" value="list">
                        <div class="row">
                            <div class="col-md-3">
                                <div class="form-group">
                                    <label>Trạng thái</label>
                                    <select name="statusFilter" class="form-control">
                                        <option value="all" ${empty selectedStatus ? 'selected' : ''}>Tất cả</option>
                                        <c:forEach items="${statusList}" var="status">
                                            <option value="${status.settingID}" 
                                                ${selectedStatus eq status.settingID ? 'selected' : ''}>
                                                ${status.value}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group">
                                    <label>Nguyên liệu</label>
                                    <select name="ingredientFilter" class="form-control">
                                        <option value="all" ${empty selectedIngredient ? 'selected' : ''}>Tất cả</option>
                                        <c:forEach items="${ingredientList}" var="ingredient">
                                            <option value="${ingredient.ingredientID}" 
                                                ${selectedIngredient eq ingredient.ingredientID ? 'selected' : ''}>
                                                ${ingredient.name}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group">
                                    <label>Số bản ghi/trang</label>
                                    <select name="pageSize" class="form-control">
                                        <option value="10" ${pageSize eq 10 ? 'selected' : ''}>10</option>
                                        <option value="20" ${pageSize eq 20 ? 'selected' : ''}>20</option>
                                        <option value="50" ${pageSize eq 50 ? 'selected' : ''}>50</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group">
                                    <label>&nbsp;</label><br>
                                    <button type="submit" class="btn btn-primary">
                                        <i class="fa fa-filter"></i> Lọc
                                    </button>
                                    <a href="${pageContext.request.contextPath}/issue?action=list" class="btn btn-default">
                                        <i class="fa fa-refresh"></i> Đặt lại
                                    </a>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>

                <!-- Issues List -->
                <div class="box">
                    <div class="box-header with-border">
                        <h3 class="box-title">Danh sách vấn đề (${totalCount} kết quả)</h3>
                    </div>
                    <div class="box-body">
                        <c:choose>
                            <c:when test="${empty issues}">
                                <div class="alert alert-info">
                                    <i class="icon fa fa-info"></i>
                                    Không có vấn đề nào được tìm thấy.
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover">
                                        <thead>
                                            <tr>
                                                <th class="sortable-stt" onclick="sortBySTT('issueTableBody')" style="width: 50px; cursor: pointer;">
                                                    ID <i class="fa fa-sort"></i>
                                                </th>
                                                <th>Nguyên liệu</th>
                                                <th>Mô tả</th>
                                                <th style="width: 100px;">Số lượng</th>
                                                <th style="width: 120px;">Trạng thái</th>
                                                <th style="width: 120px;">Người tạo</th>
<!--                                                <th style="width: 120px;">Ngày tạo</th>-->
                                                <th style="width: 150px;">Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody id="issueTableBody">
                                            <c:forEach items="${issues}" var="issue">
                                                <tr>
                                                    <td>${issue.issueID}</td>
                                                    <td><strong>${issue.ingredientName}</strong></td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${fn:length(issue.description) > 50}">
                                                                ${fn:substring(issue.description, 0, 50)}...
                                                            </c:when>
                                                            <c:otherwise>
                                                                ${issue.description}
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <fmt:formatNumber value="${issue.quantity}" pattern="#,##0.##"/> 
                                                        ${issue.unitName}
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${issue.statusName eq 'Reported'}">
                                                                <span class="label label-danger">${issue.statusName}</span>
                                                            </c:when>
                                                            <c:when test="${issue.statusName eq 'Under Investigation'}">
                                                                <span class="label label-warning">${issue.statusName}</span>
                                                            </c:when>
                                                            <c:when test="${issue.statusName eq 'Resolved'}">
                                                                <span class="label label-success">${issue.statusName}</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="label label-default">${issue.statusName}</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>${issue.createdByName}</td>
<!--                                                    <td>
                                                        <fmt:formatDate value="${issue.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                                    </td>-->
                                                    <td class="action-buttons">
                                                        <a href="${pageContext.request.contextPath}/issue?action=view&id=${issue.issueID}" 
                                                           class="btn btn-xs btn-info" title="Xem chi tiết">
                                                            <i class="fa fa-eye"></i>
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>

                                <!-- Pagination -->
                                <c:if test="${totalPages > 1}">
                                    <div class="text-center">
                                        <ul class="pagination">
                                            <c:if test="${currentPage > 1}">
                                                <li>
                                                    <a href="${pageContext.request.contextPath}/issue?action=list&page=${currentPage - 1}&pageSize=${pageSize}&statusFilter=${selectedStatus}&ingredientFilter=${selectedIngredient}">
                                                        &laquo; Trước
                                                    </a>
                                                </li>
                                            </c:if>
                                            
                                            <c:forEach begin="1" end="${totalPages}" var="i">
                                                <c:choose>
                                                    <c:when test="${currentPage eq i}">
                                                        <li class="active"><span>${i}</span></li>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <li>
                                                            <a href="${pageContext.request.contextPath}/issue?action=list&page=${i}&pageSize=${pageSize}&statusFilter=${selectedStatus}&ingredientFilter=${selectedIngredient}">
                                                                ${i}
                                                            </a>
                                                        </li>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                            
                                            <c:if test="${currentPage < totalPages}">
                                                <li>
                                                    <a href="${pageContext.request.contextPath}/issue?action=list&page=${currentPage + 1}&pageSize=${pageSize}&statusFilter=${selectedStatus}&ingredientFilter=${selectedIngredient}">
                                                        Sau &raquo;
                                                    </a>
                                                </li>
                                            </c:if>
                                        </ul>
                                    </div>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </section>
        </div>

        <%@include file="../compoment/footer.jsp" %>
    </div>

    <!-- jQuery from CDN -->
    <script src="https://code.jquery.com/jquery-2.2.4.min.js"></script>
    <!-- Bootstrap 3.3.6 -->
    <script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
    <!-- AdminLTE App -->
    <script src="https://adminlte.io/themes/AdminLTE/dist/js/app.min.js"></script>
    <!-- Table Sort -->
    <script src="${pageContext.request.contextPath}/js/table-sort.js"></script>
    
    <script>
        // Auto dismiss alerts after 5 seconds
        setTimeout(function() {
            $('.alert').fadeOut('slow');
        }, 5000);
    </script>
</body>
</html>
</html>

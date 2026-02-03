<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Quản lý nhà cung cấp - Coffee Shop Management</title>
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
    <!-- Sidebar improvements -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar-improvements.css">
    <style>
        .action-buttons {
            white-space: nowrap;
        }
        .action-buttons .btn {
            margin-right: 3px;
            display: inline-block;
            vertical-align: middle;
        }
        .action-buttons .btn:last-child {
            margin-right: 0;
        }
    </style>
    <!-- jQuery từ CDN - load trước tiên -->
    <script src="https://code.jquery.com/jquery-2.2.4.min.js" integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44=" crossorigin="anonymous"></script>
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
                    Quản lý nhà cung cấp
                    <small>Danh sách nhà cung cấp</small>
                </h1>
                <ol class="breadcrumb">
                    <li><a href="${pageContext.request.contextPath}/admin/dashboard"><i class="fa fa-dashboard"></i> Dashboard</a></li>
                    <li class="active">Nhà cung cấp</li>
                </ol>
            </section>

            <!-- Main content -->
            <section class="content">
                <!-- Display messages -->
                <c:if test="${not empty sessionScope.successMessage}">
                    <div class="alert alert-success alert-dismissible">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                        <i class="icon fa fa-check"></i> ${sessionScope.successMessage}
                        <c:remove var="successMessage" scope="session"/>
                    </div>
                </c:if>
                
                <c:if test="${not empty sessionScope.errorMessage}">
                    <div class="alert alert-danger alert-dismissible">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                        <i class="icon fa fa-ban"></i> ${sessionScope.errorMessage}
                        <c:remove var="errorMessage" scope="session"/>
                    </div>
                </c:if>
                
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">
                        ${error}
                    </div>
                </c:if>

                <div class="row">
                    <div class="col-xs-12">
                        <div class="box box-primary">
                            <div class="box-header with-border">
                                <h3 class="box-title">Danh sách nhà cung cấp</h3>
                                <div class="box-tools">
                                    <a href="${pageContext.request.contextPath}/admin/supplier?action=new" class="btn btn-success btn-sm">
                                        <i class="fa fa-plus"></i> Thêm nhà cung cấp
                                    </a>
                                </div>
                            </div>
                            
                            <div class="box-body">
                                <!-- Search Form -->
                                <form method="get" action="${pageContext.request.contextPath}/admin/supplier" class="form-inline" style="margin-bottom: 15px;">
                                    <input type="hidden" name="action" value="list">
                                    <div class="form-group">
                                        <input type="text" name="search" class="form-control" 
                                               placeholder="Tìm kiếm theo tên, email, phone..." 
                                               value="${searchKeyword}" style="width: 300px;">
                                    </div>
                                    <div class="form-group" style="margin-left: 10px;">
                                        <select name="status" class="form-control" style="width: 150px;">
                                            <option value="">Tất cả trạng thái</option>
                                            <option value="active" ${statusFilter == 'active' ? 'selected' : ''}>Hoạt động</option>
                                            <option value="inactive" ${statusFilter == 'inactive' ? 'selected' : ''}>Không hoạt động</option>
                                        </select>
                                    </div>
                                    <button type="submit" class="btn btn-primary">
                                        <i class="fa fa-search"></i> Tìm kiếm
                                    </button>
                                    <c:if test="${not empty searchKeyword || not empty statusFilter}">
                                        <a href="${pageContext.request.contextPath}/admin/supplier?action=list" class="btn btn-default">
                                            <i class="fa fa-refresh"></i> Xóa lọc
                                        </a>
                                    </c:if>
                                </form>

                                <!-- Supplier Table -->
                                <div class="table-responsive">
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Tên nhà cung cấp</th>
                                                <th>Người liên hệ</th>
                                                <th>Email</th>
                                                <th>Số điện thoại</th>
                                                <th>Trạng thái</th>
                                                <th>Hành động</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${suppliers == null || empty suppliers}">
                                                    <tr>
                                                        <td colspan="7" style="text-align: center;">
                                                            <em>Không tìm thấy nhà cung cấp nào.</em>
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="supplier" items="${suppliers}">
                                                        <tr>
                                                            <td>${supplier.supplierID != null ? supplier.supplierID : 'N/A'}</td>
                                                            <td>
                                                                <strong>${supplier.supplierName != null ? supplier.supplierName : 'Chưa cập nhật'}</strong>
                                                            </td>
                                                            <td>${supplier.contactName != null ? supplier.contactName : 'Chưa cập nhật'}</td>
                                                            <td>${supplier.email != null ? supplier.email : 'Chưa cập nhật'}</td>
                                                            <td>${supplier.phone != null ? supplier.phone : 'Chưa cập nhật'}</td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${supplier != null && supplier.active == true}">
                                                                        <span class="label label-success">Hoạt động</span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="label label-danger">Không hoạt động</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td class="action-buttons">
                                                                <c:if test="${supplier.supplierID != null}">
                                                                    <a href="${pageContext.request.contextPath}/admin/supplier?action=details&id=${supplier.supplierID}" 
                                                                       class="btn btn-info btn-xs" title="Xem chi tiết">
                                                                        <i class="fa fa-eye"></i>
                                                                    </a>
                                                                    <a href="${pageContext.request.contextPath}/admin/supplier?action=edit&id=${supplier.supplierID}" 
                                                                       class="btn btn-warning btn-xs" title="Chỉnh sửa">
                                                                        <i class="fa fa-edit"></i>
                                                                    </a>
                                                                    <button type="button" class="btn ${supplier.active ? 'btn-danger' : 'btn-success'} btn-xs" 
                                                                            title="${supplier.active ? 'Vô hiệu hóa' : 'Kích hoạt'}"
                                                                            onclick="toggleActive(${supplier.supplierID}, ${supplier.active})">
                                                                        <i class="fa ${supplier.active ? 'fa-ban' : 'fa-check'}"></i>
                                                                    </button>
                                                                </c:if>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                        </tbody>
                                    </table>
                                </div>
                                
                                <!-- Pagination -->
                                <c:if test="${totalPages > 1}">
                                    <div class="row">
                                        <div class="col-sm-5">
                                            
                                        </div>
                                        <div class="col-sm-7">
                                            <div class="dataTables_paginate paging_simple_numbers">
                                                <ul class="pagination">
                                                    <!-- Previous page -->
                                                    <c:if test="${currentPage > 1}">
                                                        <li class="paginate_button previous">
                                                            <a href="${pageContext.request.contextPath}/admin/supplier?action=list&page=${currentPage - 1}&status=${statusFilter}&search=${searchKeyword}"><i class="fa fa-angle-left"></i></a>
                                                        </li>
                                                    </c:if>
                                                    
                                                    <!-- Page numbers -->
                                                    <c:forEach begin="1" end="${totalPages}" var="pageNum">
                                                        <c:choose>
                                                            <c:when test="${pageNum == currentPage}">
                                                                <li class="paginate_button active">
                                                                    <a href="#">${pageNum}</a>
                                                                </li>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <li class="paginate_button">
                                                                    <a href="${pageContext.request.contextPath}/admin/supplier?action=list&page=${pageNum}&status=${statusFilter}&search=${searchKeyword}">${pageNum}</a>
                                                                </li>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:forEach>
                                                    
                                                    <!-- Next page -->
                                                    <c:if test="${currentPage < totalPages}">
                                                        <li class="paginate_button next">
                                                            <a href="${pageContext.request.contextPath}/admin/supplier?action=list&page=${currentPage + 1}&status=${statusFilter}&search=${searchKeyword}"><i class="fa fa-angle-right"></i></a>
                                                        </li>
                                                    </c:if>
                                                </ul>
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
    </div>

    <!-- Bootstrap 3.3.7 từ CDN -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
    <!-- AdminLTE App -->
    <script src="${pageContext.request.contextPath}/dist/js/app.min.js"></script>
    <!-- Sidebar script -->
    <jsp:include page="../compoment/sidebar-scripts.jsp" />
    
    <script>
        function toggleActive(supplierId, currentStatus) {
            var action = currentStatus ? 'ngưng hoạt động' : 'kích hoạt';
            var confirmMessage = 'Bạn có chắc chắn muốn ' + action + ' nhà cung cấp này?';
            
            if (confirm(confirmMessage)) {
                var url = '${pageContext.request.contextPath}/admin/supplier?action=toggle-status&id=' + supplierId + 
                         '&page=${currentPage}&status=${statusFilter}&search=${searchKeyword}';
                window.location.href = url;
            }
        }
    </script>
</body>
</html>

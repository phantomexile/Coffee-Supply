<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Chi tiết nhà cung cấp - Coffee Shop Management</title>
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
                    Chi tiết nhà cung cấp
                    <small>${supplier != null && supplier.supplierName != null ? supplier.supplierName : 'Không xác định'}</small>
                </h1>
                <ol class="breadcrumb">
                    <li><a href="${pageContext.request.contextPath}/admin/dashboard"><i class="fa fa-dashboard"></i> Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/supplier?action=list">Nhà cung cấp</a></li>
                    <li class="active">Chi tiết</li>
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
                
                <!-- Check if supplier exists -->
                <c:if test="${empty supplier}">
                    <div class="alert alert-danger">
                        <h4><i class="icon fa fa-ban"></i> Lỗi!</h4>
                        Không tìm thấy thông tin nhà cung cấp.
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <a href="${pageContext.request.contextPath}/admin/supplier?action=list" class="btn btn-default">
                                <i class="fa fa-arrow-left"></i> Quay lại danh sách
                            </a>
                        </div>
                    </div>
                </c:if>
                
                <c:if test="${not empty supplier}">

				<div class="row">
					<!-- Supplier Information -->
					<div class="col-md-12">
						<div class="box box-primary">
							<div class="box-header with-border">
								<h3 class="box-title">
									<i class="fa fa-info-circle"></i>
									${supplier != null && supplier.supplierName != null ? supplier.supplierName : 'Thông tin nhà cung cấp'}
								</h3>
								<div class="box-tools">
									<a href="${pageContext.request.contextPath}/admin/supplier?action=list" 
									   class="btn btn-default btn-sm">
										<i class="fa fa-arrow-left"></i> Quay lại
									</a>
								</div>
							</div>

							<div class="box-body">
								<div class="row">
									<div class="col-md-12">
										<table class="table" style="width:auto;">
											<tr>
												<th style="width:180px;">Mã nhà cung cấp:</th>
												<td>${supplier.supplierID}</td>
											</tr>
											<tr>
												<th>Tên nhà cung cấp:</th>
												<td>${supplier.supplierName}</td>
											</tr>
											<tr>
												<th>Người liên hệ:</th>
												<td>${not empty supplier.contactName ? supplier.contactName : '<em>Chưa cập nhật</em>'}</td>
											</tr>
											<tr>
												<th>Email:</th>
												<td>
													<c:choose>
														<c:when test="${not empty supplier.email}">
															<a href="mailto:${supplier.email}">${supplier.email}</a>
														</c:when>
														<c:otherwise>
															<em>Chưa cập nhật</em>
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
											<tr>
												<th>Số điện thoại:</th>
												<td>
													<c:choose>
														<c:when test="${not empty supplier.phone}">
															<a href="tel:${supplier.phone}">${supplier.phone}</a>
														</c:when>
														<c:otherwise>
															<em>Chưa cập nhật</em>
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
											<tr>
												<th>Địa chỉ:</th>
												<td>${not empty supplier.address ? supplier.address : '<em>Chưa cập nhật</em>'}</td>
											</tr>
											<tr>
												<th>Trạng thái:</th>
												<td>
													<c:choose>
														<c:when test="${supplier.active}"><span class="label label-success">Hoạt động</span></c:when>
														<c:otherwise><span class="label label-danger">Không hoạt động</span></c:otherwise>
													</c:choose>
												</td>
											</tr>
										</table>
									</div>
								</div>
							</div>

							<div class="box-footer">
								<a href="${pageContext.request.contextPath}/admin/supplier?action=list" class="btn btn-default">
									<i class="fa fa-arrow-left"></i> Quay lại danh sách
								</a>
							</div>
						</div>
					</div>
				</div>

				<!-- Ingredient list for this supplier -->
				<div class="row">
					<div class="col-md-12">
						<div class="box box-info">
							<div class="box-header with-border">
								<h3 class="box-title">Danh sách nguyên liệu của nhà cung cấp</h3>
							</div>

							<div class="box-body">
								<c:choose>
									<c:when test="${empty ingredients}">
										<div style="text-align:center; padding:32px; color:#bbb;">
											<i class="fa fa-inbox fa-2x"></i>
											<p>Nhà cung cấp hiện chưa có nguyên liệu nào.</p>
										</div>
									</c:when>
									<c:otherwise>
										<table class="table table-striped">
											<thead>
											<tr>
												<th>Mã NL</th>
												<th>Tên nguyên liệu</th>
												<th>Trạng thái</th>
											</tr>
											</thead>
											<tbody>
											<c:forEach var="ing" items="${ingredients}">
												<tr>
													<td>${ing.ingredientID}</td>
													<td>${ing.name}</td>
													<td>
														<c:choose>
															<c:when test="${ing.active}">
																<span class="label label-success">Hoạt động</span>
															</c:when>
															<c:otherwise>
																<span class="label label-danger">Không hoạt động</span>
															</c:otherwise>
														</c:choose>
													</td>
												</tr>
											</c:forEach>
											</tbody>
										</table>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</div>
				</div>

				</c:if>
            </section>
        </div>
    </div>

    <!-- Bootstrap 3.3.7 từ CDN -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
    <!-- AdminLTE App -->
    <script src="${pageContext.request.contextPath}/dist/js/app.min.js"></script>
    <!-- Sidebar script -->
    <jsp:include page="../compoment/sidebar-scripts.jsp" />
</body>
</html>


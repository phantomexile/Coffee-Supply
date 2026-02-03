<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Chi tiết đơn hàng - Coffee Shop Management</title>
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.6 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
    <!-- AdminLTE -->
    <link rel="stylesheet" href="https://adminlte.io/themes/AdminLTE/dist/css/AdminLTE.min.css">
    <link rel="stylesheet" href="https://adminlte.io/themes/AdminLTE/dist/css/skins/_all-skins.min.css">
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
                Chi tiết đơn hàng
                <small>Đơn hàng #${order.orderID}</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${pageContext.request.contextPath}/barista/dashboard"><i class="fa fa-dashboard"></i> Trang chủ</a></li>
                <li><a href="${pageContext.request.contextPath}/barista/orders">Danh sách đơn hàng</a></li>
                <li class="active">Chi tiết</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-md-8">
                    <!-- Order Details Box -->
                    <div class="box box-primary">
                        <div class="box-header with-border">
                            <h3 class="box-title">Thông tin đơn hàng #${order.orderID}</h3>
                        </div>
                        
                        <div class="box-body">
                            <!-- Success/Error Messages -->
                            <c:if test="${not empty sessionScope.successMessage}">
                                <div class="alert alert-success alert-dismissible">
                                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                                    <i class="icon fa fa-check"></i> ${sessionScope.successMessage}
                                </div>
                                <c:remove var="successMessage" scope="session"/>
                            </c:if>
                            
                            <c:if test="${not empty sessionScope.errorMessage}">
                                <div class="alert alert-danger alert-dismissible">
                                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                                    <i class="icon fa fa-ban"></i> ${sessionScope.errorMessage}
                                </div>
                                <c:remove var="errorMessage" scope="session"/>
                            </c:if>
                            
                            <table class="table table-bordered">
                                <tr>
                                    <th width="30%">Mã đơn hàng:</th>
                                    <td>#${order.orderID}</td>
                                </tr>
                                <tr>
                                    <th>Cửa hàng:</th>
                                    <td>${order.shopName}</td>
                                </tr>
                                <tr>
                                    <th>Người tạo:</th>
                                    <td>${order.createdByName}</td>
                                </tr>
                                <tr>
                                    <th>Trạng thái:</th>
                                    <td>
                                        <span class="label label-${order.statusName == 'New' ? 'info' : 
                                                                     order.statusName == 'Preparing' ? 'warning' :
                                                                     order.statusName == 'Ready' ? 'success' :
                                                                     order.statusName == 'Completed' ? 'default' : 'danger'}">
                                            ${order.statusName}
                                        </span>
                                    </td>
                                </tr>
                                <tr>
                                    <th>Thời gian tạo:</th>
                                    <td><fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                                </tr>
                            </table>
                            
                            <!-- Order Items -->
                            <h4>Danh sách sản phẩm:</h4>
                            <div class="table-responsive">
                                <table class="table table-bordered table-striped">
                                    <thead>
                                        <tr>
                                            <th>STT</th>
                                            <th>Tên sản phẩm</th>
                                            <th>Danh mục</th>
                                            <th>Số lượng</th>
                                            <th>Đơn giá</th>
                                            <th>Thành tiền</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="detail" items="${orderDetails}" varStatus="status">
                                            <tr>
                                                <td>${status.index + 1}</td>
                                                <td>${detail.productName}</td>
                                                <td>${detail.categoryName}</td>
                                                <td>${detail.quantity}</td>
                                                <td><fmt:formatNumber value="${detail.price}" type="currency" currencySymbol="đ"/></td>
                                                <td><fmt:formatNumber value="${detail.subtotal}" type="currency" currencySymbol="đ"/></td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty orderDetails}">
                                            <tr>
                                                <td colspan="6" class="text-center">Không có sản phẩm nào</td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                    <tfoot>
                                        <tr>
                                            <th colspan="5" class="text-right">Tổng cộng:</th>
                                            <th><fmt:formatNumber value="${total}" type="currency" currencySymbol="đ"/></th>
                                        </tr>
                                    </tfoot>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="col-md-4">
                    <!-- Actions Box -->
                    <div class="box box-success">
                        <div class="box-header with-border">
                            <h3 class="box-title">Thao tác</h3>
                        </div>
                        
                        <div class="box-body">
                            <c:if test="${order.statusName != 'Completed' && order.statusName != 'Cancelled'}">
                                <!-- Determine next status based on current status -->
                                <c:set var="nextStatusName" value="" />
                                <c:choose>
                                    <c:when test="${order.statusName == 'New'}">
                                        <c:set var="nextStatusName" value="Preparing" />
                                    </c:when>
                                    <c:when test="${order.statusName == 'Preparing'}">
                                        <c:set var="nextStatusName" value="Ready" />
                                    </c:when>
                                    <c:when test="${order.statusName == 'Ready'}">
                                        <c:set var="nextStatusName" value="Completed" />
                                    </c:when>
                                </c:choose>
                                
                                <!-- Show next status button -->
                                <c:if test="${not empty nextStatusName}">
                                    <form method="post" action="${pageContext.request.contextPath}/barista/orders">
                                        <input type="hidden" name="action" value="updateStatus">
                                        <input type="hidden" name="id" value="${order.orderID}">
                                        
                                        <!-- Find and set the next status ID -->
                                        <c:forEach var="status" items="${orderStatuses}">
                                            <c:if test="${status.value == nextStatusName}">
                                                <input type="hidden" name="statusId" value="${status.settingID}">
                                            </c:if>
                                        </c:forEach>
                                        
                                        <button type="submit" class="btn btn-success btn-block">
                                            <i class="fa fa-arrow-right"></i> Chuyển sang: ${nextStatusName}
                                        </button>
                                    </form>
                                    
                                    <hr>
                                </c:if>
                                
                                <button type="button" class="btn btn-danger btn-block" data-toggle="modal" data-target="#cancelOrderModal">
                                    <i class="fa fa-times"></i> Hủy đơn hàng
                                </button>
                            </c:if>
                            
                            <c:if test="${order.statusName == 'Completed' || order.statusName == 'Cancelled'}">
                                <div class="alert alert-info">
                                    <i class="fa fa-info-circle"></i>
                                    Đơn hàng đã ${order.statusName == 'Completed' ? 'hoàn thành' : 'bị hủy'}, không thể thay đổi trạng thái.
                                </div>
                            </c:if>
                            
                            <hr>
                            
                            <a href="${pageContext.request.contextPath}/barista/orders" class="btn btn-default btn-block">
                                <i class="fa fa-arrow-left"></i> Quay lại danh sách
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
    
    <!-- Include Footer -->
    <%@include file="../compoment/footer.jsp" %>

</div>

<!-- Cancel Order Modal -->
<div class="modal fade" id="cancelOrderModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <form method="post" action="${pageContext.request.contextPath}/barista/orders">
                <input type="hidden" name="action" value="cancel">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">
                        <i class="fa fa-times-circle text-danger"></i> Hủy đơn hàng #${order.orderID}
                    </h4>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="id" value="${order.orderID}">
                    
                    <div class="form-group">
                        <label for="cancelReason">Lý do hủy đơn: <span class="text-danger">*</span></label>
                        <textarea class="form-control" id="cancelReason" name="cancelReason" 
                                  rows="4" required minlength="10" maxlength="200"
                                  placeholder="Vui lòng nhập lý do hủy đơn hàng (tối thiểu 10 ký tự)..."></textarea>
                        <span class="help-block">Lý do hủy đơn phải từ 10-200 ký tự và sẽ được lưu lại để tham khảo.</span>
                    </div>
                    
                    <div class="alert alert-warning">
                        <i class="fa fa-warning"></i> 
                        <strong>Cảnh báo:</strong> Hành động này không thể hoàn tác!
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">
                        <i class="fa fa-times"></i> Đóng
                    </button>
                    <button type="submit" class="btn btn-danger">
                        <i class="fa fa-check"></i> Xác nhận hủy đơn
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- jQuery 2.2.3 -->
<script src="https://code.jquery.com/jquery-2.2.3.min.js"></script>
<!-- Bootstrap 3.3.6 JS -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<!-- AdminLTE App -->
<script src="https://adminlte.io/themes/AdminLTE/dist/js/app.min.js"></script>

</body>
</html>

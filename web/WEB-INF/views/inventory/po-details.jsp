<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết Đơn hàng - PO Details</title>
    
    <!-- Bootstrap 3.3.6 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/AdminLTE.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/skins/_all-skins.min.css">
    <!-- Custom sidebar styles -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar-custom.css">
    
    <style>
        body {
            background-color: #ecf0f5;
        }
        
        .content-wrapper {
            margin-left: 230px;
            padding: 20px;
            min-height: 100vh;
        }
        
        .page-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 30px;
            border-radius: 10px;
            margin-bottom: 30px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
        
        .page-header h1 {
            margin: 0;
            font-size: 28px;
            font-weight: 600;
        }
        
        .info-box-custom {
            background: white;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.08);
            border-left: 4px solid #667eea;
        }
        
        .info-box-custom p {
            margin: 10px 0;
            font-size: 14px;
        }
        
        .info-box-custom strong {
            color: #667eea;
        }
        
        .status-badge {
            padding: 8px 16px;
            border-radius: 20px;
            font-size: 13px;
            font-weight: 600;
            text-transform: uppercase;
        }
        
        .status-pending {
            background: linear-gradient(135deg, #f7b733 0%, #fc4a1a 100%);
            color: white;
        }
        
        .status-approved {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            color: white;
        }
        
        .status-shipping {
            background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
            color: white;
        }
        
        .status-received {
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            color: white;
        }
        
        .status-cancelled {
            background: linear-gradient(135deg, #ee0979 0%, #ff6a00 100%);
            color: white;
        }
        
        .box {
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.08);
        }
        
        .box-header {
            border-radius: 10px 10px 0 0;
        }
        
        .btn-action {
            border-radius: 25px;
            padding: 10px 25px;
            margin: 5px;
            transition: all 0.3s;
            border: none;
        }
        
        .btn-action:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
        }
        
        .modal {
            z-index: 1050;
        }
        
        .modal-backdrop {
            z-index: 1040;
        }
        
        .modal-content {
            border-radius: 15px;
            border: none;
            box-shadow: 0 5px 25px rgba(0,0,0,0.3);
        }
        
        .modal-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px 15px 0 0;
            border: none;
        }
        
        .modal-header.bg-danger {
            background: linear-gradient(135deg, #dc3545 0%, #c82333 100%) !important;
        }
        
        .workflow-info {
            background: #f8f9fa;
            border-left: 4px solid #17a2b8;
            padding: 20px;
            margin: 20px 0;
            border-radius: 5px;
            text-align: center;
        }
        
        .current-status-label {
            font-size: 16px;
            font-weight: 600;
            color: #333;
            margin-bottom: 10px;
        }
        
        .current-status-wrapper {
            display: inline-flex;
            align-items: center;
            gap: 10px;
        }
        
        .current-status-step {
            width: 48px;
            height: 48px;
            border-radius: 50%;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: #fff;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: bold;
            font-size: 18px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
    <div class="wrapper">
        <!-- Include Header -->
        <jsp:include page="../compoment/header.jsp" />
        
        <!-- Include Sidebar -->
        <jsp:include page="../compoment/sidebar.jsp" />
        
        <!-- Content Wrapper -->
        <div class="content-wrapper">
            <!-- Page Header -->
            <div class="page-header">
                <h1><i class="fa fa-file-text-o"></i> Chi tiết Đơn hàng #PO-${po.poID}</h1>
                <ol class="breadcrumb" style="background: transparent; padding: 10px 0;">
                    <c:choose>
                        <c:when test="${sessionScope.roleName == 'Admin'}">
                            <li><a href="${pageContext.request.contextPath}/admin/dashboard" style="color: white;"><i class="fa fa-dashboard"></i> Dashboard</a></li>
                        </c:when>
                        <c:otherwise>
                            <li><a href="${pageContext.request.contextPath}/inventory/dashboard" style="color: white;"><i class="fa fa-dashboard"></i> Dashboard</a></li>
                        </c:otherwise>
                    </c:choose>
                    <li><a href="${pageContext.request.contextPath}/purchase-order?action=list" style="color: white;">Danh sách đơn hàng</a></li>
                    <li class="active" style="color: rgba(255,255,255,0.8);">Chi tiết PO-${po.poID}</li>
                </ol>
            </div>
            
            <!-- Success/Error Messages -->
            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert">×</button>
                    <h4><i class="icon fa fa-check"></i> Thành công!</h4>
                    ${sessionScope.successMessage}
                </div>
                <c:remove var="successMessage" scope="session"/>
            </c:if>
            
            <c:if test="${not empty sessionScope.errorMessage}">
                <div class="alert alert-danger alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert">×</button>
                    <h4><i class="icon fa fa-ban"></i> Lỗi!</h4>
                    ${sessionScope.errorMessage}
                </div>
                <c:remove var="errorMessage" scope="session"/>
            </c:if>
            
            <!-- Workflow Information -->
            <div class="workflow-info">
                <h4><i class="fa fa-info-circle"></i> Trạng thái đơn hàng hiện tại</h4>
                <p class="current-status-label">Trạng thái chỉ được chuyển theo thứ tự: <strong>Pending → Approved → Shipping → (Received hoặc Cancelled)</strong></p>
                <div class="current-status-wrapper">
                    <div class="current-status-step">
                        <c:choose>
                            <c:when test="${po.statusID == 20}">1</c:when>
                            <c:when test="${po.statusID == 21}">2</c:when>
                            <c:when test="${po.statusID == 22}">3</c:when>
                            <c:otherwise>4</c:otherwise>
                        </c:choose>
                    </div>
                    <c:choose>
                        <c:when test="${po.statusID == 20}">
                            <span class="status-badge status-pending">Pending</span>
                        </c:when>
                        <c:when test="${po.statusID == 21}">
                            <span class="status-badge status-approved">Approved</span>
                        </c:when>
                        <c:when test="${po.statusID == 22}">
                            <span class="status-badge status-shipping">Shipping</span>
                        </c:when>
                        <c:when test="${po.statusID == 23}">
                            <span class="status-badge status-received">Received</span>
                        </c:when>
                        <c:when test="${po.statusID == 24}">
                            <span class="status-badge status-cancelled">Cancelled</span>
                        </c:when>
                        <c:otherwise>
                            <span class="status-badge">Unknown</span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            
            <!-- Rejection/Cancellation Reason -->
            <c:if test="${po.statusID == 24 && not empty po.rejectReason}">
                <div class="alert alert-danger" style="border-left: 4px solid #dc3545; border-radius: 5px;">
                    <h4 style="margin-top: 0;">
                        <i class="fa fa-exclamation-triangle"></i> Lý do hủy đơn hàng
                    </h4>
                    <p style="margin-bottom: 0; font-size: 14px; white-space: pre-wrap;">${po.rejectReason}</p>
                </div>
            </c:if>
            
            <!-- PO Information -->
            <div class="row">
                <div class="col-md-12">
                    <div class="box box-primary">
                        <div class="box-header with-border">
                            <h3 class="box-title"><i class="fa fa-info-circle"></i> Thông tin đơn hàng</h3>
                        </div>
                        <div class="box-body">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="info-box-custom">
                                        <p><strong><i class="fa fa-hashtag"></i> Mã đơn hàng:</strong> PO-${po.poID}</p>
                                       
                                        <p><strong><i class="fa fa-truck"></i> Nhà cung cấp:</strong> ${po.supplierName}</p>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="info-box-custom">
                                        <p><strong><i class="fa fa-user"></i> Người tạo:</strong> ${po.createdByName}</p>
<!--                                        <p><strong><i class="fa fa-calendar"></i> Ngày tạo:</strong> 
                                            <fmt:formatDate value="${po.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                        </p>-->
                                        <p><strong><i class="fa fa-flag"></i> Trạng thái:</strong>
                                            <c:choose>
                                                <c:when test="${po.statusID == 20}">
                                                    <span class="status-badge status-pending">Pending</span>
                                                </c:when>
                                                <c:when test="${po.statusID == 21}">
                                                    <span class="status-badge status-approved">Approved</span>
                                                </c:when>
                                                <c:when test="${po.statusID == 22}">
                                                    <span class="status-badge status-shipping">Shipping</span>
                                                </c:when>
                                                <c:when test="${po.statusID == 23}">
                                                    <span class="status-badge status-received">Received</span>
                                                </c:when>
                                                <c:when test="${po.statusID == 24}">
                                                    <span class="status-badge status-cancelled">Cancelled</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="status-badge">Unknown</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </p>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Action Buttons -->
                            <div class="text-center" style="margin-top: 20px;">
                                <a href="${pageContext.request.contextPath}/purchase-order?action=list" class="btn btn-default btn-action">
                                    <i class="fa fa-arrow-left"></i> Quay lại
                                </a>
                                
                                <c:if test="${po.statusID == 20}">
                                    
                                    <%-- Admin có thêm nút duyệt và từ chối --%>
                                    <c:if test="${sessionScope.roleName == 'Admin'}">
                                        <form method="post"
                                              action="${pageContext.request.contextPath}/purchase-order"
                                              style="display: inline;"
                                              onsubmit="return confirm('Bạn có chắc chắn muốn duyệt đơn hàng này?')">
                                            <input type="hidden" name="action" value="approve">
                                            <input type="hidden" name="id" value="${po.poID}">
                                            <input type="hidden" name="poID" value="${po.poID}">
                                            <button type="submit" class="btn btn-success btn-action">
                                                <i class="fa fa-check"></i> Duyệt đơn hàng
                                            </button>
                                        </form>
                                        <button type="button" class="btn btn-danger btn-action" onclick="showRejectModal()">
                                            <i class="fa fa-times"></i> Từ chối đơn hàng
                                        </button>
                                    </c:if>
                                </c:if>
                                
                                <!-- Status 21 (Approved) - Inventory staff chuyển sang Shipping -->
                                <c:if test="${po.statusID == 21}">
                                    <button type="button" class="btn btn-primary btn-action js-update-status" data-po-id="${po.poID}" data-status-id="22">
                                        <i class="fa fa-truck"></i> Chuyển sang vận chuyển (Shipping)
                                    </button>
                                </c:if>
                                
                                <!-- Status 22 (Shipping) - có thể chuyển sang Received hoặc Cancelled -->
                                <c:if test="${po.statusID == 22}">
                                    <button type="button" class="btn btn-success btn-action js-update-status" data-po-id="${po.poID}" data-status-id="23">
                                        <i class="fa fa-check"></i> Đã nhận hàng (Received)
                                    </button>
                                    <button type="button" class="btn btn-danger btn-action" onclick="showCancelModal()">
                                        <i class="fa fa-ban"></i> Hủy đơn (Cancel)
                                    </button>
                                </c:if>
                                
                                <!-- Status 23 (Received) and 24 (Cancelled) are final states - no actions available -->
                                <c:if test="${po.statusID == 23 || po.statusID == 24}">
                                    <div class="alert alert-info" style="margin-top: 10px;">
                                        <i class="fa fa-info-circle"></i> 
                                        Đơn hàng đã hoàn tất. Không thể thực hiện thêm thao tác nào.
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- PO Details Table -->
            <div class="row">
                <div class="col-md-12">
                    <div class="box box-success">
                        <div class="box-header with-border">
                            <h3 class="box-title"><i class="fa fa-list"></i> Chi tiết nguyên liệu</h3>
                            <%-- Ẩn nút thêm khi Pending (cả admin và staff) --%>
                            <%-- Nút thêm chỉ hiển thị khi đã được duyệt và có thể chỉnh sửa --%>
                        </div>
                        <div class="box-body">
                            <div class="table-responsive">
                                <table class="table table-bordered table-striped table-hover">
                                    <thead>
                                        <tr>
                                            <th style="width: 50px;">STT</th>
                                            <th style="width: 100px;">Mã chi tiết</th>
                                            <th>Nguyên liệu</th>
                                            <th style="width: 120px;">Số lượng đặt</th>
                                            <th style="width: 100px;">Đơn vị</th>
                                            <%-- Ẩn cột "Số lượng nhận" khi Pending (chưa nhận hàng) --%>
                                            <c:if test="${po.statusID != 20}">
                                                <th style="width: 120px;">Số lượng nhận</th>
                                            </c:if>
                                            <%-- Ẩn cột "Thao tác" khi Pending (cả admin và staff) --%>
                                            <c:if test="${po.statusID != 20}">
                                                <th style="width: 150px;">Thao tác</th>
                                            </c:if>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:choose>
                                            <c:when test="${empty details}">
                                                <tr>
                                                    <c:set var="colCount" value="5"/>
                                                    <c:if test="${po.statusID != 20}">
                                                        <c:set var="colCount" value="${colCount + 1}"/>
                                                    </c:if>
                                                    <c:if test="${po.statusID != 20}">
                                                        <c:set var="colCount" value="${colCount + 1}"/>
                                                    </c:if>
                                                    <td colspan="${colCount}" class="text-center" style="padding: 50px;">
                                                        <i class="fa fa-inbox" style="font-size: 60px; color: #ccc;"></i>
                                                        <p style="color: #999; margin-top: 15px; font-size: 16px;">Chưa có chi tiết nào</p>
                                                    </td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="detail" items="${details}" varStatus="status">
                                                    <tr>
                                                        <td>${status.index + 1}</td>
                                                        <td><span class="label label-info">POD-${detail.poDetailID}</span></td>
                                                        <td>${detail.ingredientName}</td>
                                                        <td><span class="badge bg-blue">${detail.quantity}</span></td>
                                                        <td><span class="label label-default">${detail.unitName}</span></td>
                                                        <%-- Ẩn cột "Số lượng nhận" khi Pending (chưa nhận hàng) --%>
                                                        <c:if test="${po.statusID != 20}">
                                                            <td><span class="badge bg-green">${detail.receivedQuantity}</span></td>
                                                        </c:if>
                                                        <%-- Ẩn cột "Thao tác" khi Pending (cả admin và staff) --%>
                                                        <c:if test="${po.statusID != 20}">
                                                            <td>
                                                                <%-- Cho phép chỉnh sửa khi Shipping (22) --%>
                                                                <c:if test="${po.statusID == 22}">
                                                                    <button type="button" class="btn btn-warning btn-xs" 
                                                                            data-toggle="modal" 
                                                                            data-target="#editDetailModal${detail.poDetailID}">
                                                                        <i class="fa fa-edit"></i> Cập nhật SL nhận
                                                                    </button>
                                                                </c:if>
                                                            </td>
                                                        </c:if>
                                                    </tr>
                                                    
                                                    <%-- Chỉ hiển thị Edit Modal khi không phải Pending --%>
                                                    <c:if test="${po.statusID != 20}">
                                                    <!-- Edit Detail Modal -->
                                                    <div class="modal fade" id="editDetailModal${detail.poDetailID}" tabindex="-1">
                                                        <div class="modal-dialog">
                                                            <div class="modal-content">
                                                                <div class="modal-header">
                                                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                                                    <h4 class="modal-title">
                                                                        ${po.statusID == 22 ? 'Cập nhật số lượng nhận' : 'Chỉnh sửa chi tiết'}
                                                                    </h4>
                                                                </div>
                                                                <form action="${pageContext.request.contextPath}/purchase-order" method="post">
                                                                    <div class="modal-body">
                                                                        <input type="hidden" name="action" value="update-detail">
                                                                        <input type="hidden" name="poDetailID" value="${detail.poDetailID}">
                                                                        <input type="hidden" name="poID" value="${po.poID}">
                                                                        
                                                                        <div class="form-group">
                                                                            <label>Nguyên liệu</label>
                                                                            <select name="ingredientID" class="form-control" required
                                                                                    ${po.statusID == 22 ? 'disabled' : ''}>
                                                                                <c:forEach var="ingredient" items="${ingredients}">
                                                                                    <option value="${ingredient.ingredientID}" 
                                                                                            ${ingredient.ingredientID == detail.ingredientID ? 'selected' : ''}>
                                                                                        ${ingredient.name}
                                                                                    </option>
                                                                                </c:forEach>
                                                                            </select>
                                                                            <%-- Hidden input để submit ingredientID khi disabled --%>
                                                                            <c:if test="${po.statusID == 22}">
                                                                                <input type="hidden" name="ingredientID" value="${detail.ingredientID}">
                                                                            </c:if>
                                                                        </div>
                                                                        
                                                                        <div class="form-group">
                                                                            <label>Số lượng đặt</label>
                                                                            <input type="number" name="quantity" class="form-control" 
                                                                                   value="${detail.quantity}" step="0.01" required
                                                                                   ${po.statusID == 22 ? 'readonly' : ''}>
                                                                        </div>
                                                                        
                                                                        <div class="form-group">
                                                                            <label>Đơn vị</label>
                                                                            <select name="unitID" class="form-control" required
                                                                                    ${po.statusID == 22 ? 'disabled' : ''}>
                                                                                <c:forEach var="unit" items="${units}">
                                                                                    <option value="${unit.settingID}" 
                                                                                            ${unit.settingID == detail.unitID ? 'selected' : ''}>
                                                                                        ${unit.value}
                                                                                    </option>
                                                                                </c:forEach>
                                                                            </select>
                                                                            <%-- Hidden input để submit unitID khi disabled --%>
                                                                            <c:if test="${po.statusID == 22}">
                                                                                <input type="hidden" name="unitID" value="${detail.unitID}">
                                                                            </c:if>
                                                                        </div>
                                                                        
                                                                        <div class="form-group">
                                                                            <label>Số lượng nhận 
                                                                                <c:if test="${po.statusID == 22}">
                                                                                    <span class="text-danger">*</span>
                                                                                </c:if>
                                                                            </label>
                                                                            <input type="number" name="receivedQuantity" class="form-control" 
                                                                                   value="${detail.receivedQuantity}" step="0.01" required
                                                                                   max="${detail.quantity}"
                                                                                   ${po.statusID == 20 || po.statusID == 24 ? 'disabled readonly' : ''}>
                                                                            <c:if test="${po.statusID == 20}">
                                                                                <p class="help-block text-muted">
                                                                                    <i class="fa fa-info-circle"></i> Số lượng nhận sẽ được cập nhật khi đơn hàng chuyển sang trạng thái Shipping
                                                                                </p>
                                                                            </c:if>
                                                                            <c:if test="${po.statusID == 22}">
                                                                                <p class="help-block text-info">
                                                                                    <i class="fa fa-info-circle"></i> Nhập số lượng thực tế nhận được. Tối đa: <strong>${detail.quantity}</strong>
                                                                                </p>
                                                                            </c:if>
                                                                            <c:if test="${po.statusID == 24}">
                                                                                <p class="help-block text-danger">
                                                                                    <i class="fa fa-ban"></i> Đơn hàng đã bị hủy, không thể cập nhật số lượng nhận
                                                                                </p>
                                                                            </c:if>
                                                                        </div>
                                                                    </div>
                                                                    <div class="modal-footer">
                                                                        <button type="button" class="btn btn-default" data-dismiss="modal">
                                                                            ${po.statusID == 24 ? 'Đóng' : 'Hủy'}
                                                                        </button>
                                                                        <c:if test="${po.statusID != 24}">
                                                                            <button type="submit" class="btn btn-primary">Cập nhật</button>
                                                                        </c:if>
                                                                    </div>
                                                                </form>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    </c:if>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Reject Modal -->
    <div class="modal fade" id="rejectModal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header bg-danger">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title" style="color: white;"><i class="fa fa-times-circle"></i> Từ chối đơn hàng</h4>
                </div>
                <form id="rejectForm" action="${pageContext.request.contextPath}/purchase-order" method="post" onsubmit="return validateRejectForm()">
                    <div class="modal-body">
                        <input type="hidden" name="action" value="reject">
                        <input type="hidden" name="id" value="${po.poID}">
                        
                        <div class="alert alert-warning">
                            <i class="fa fa-exclamation-triangle"></i> Bạn đang từ chối đơn hàng <strong>PO-${po.poID}</strong>. Vui lòng cung cấp lý do từ chối.
                        </div>
                        
                        <div class="form-group">
                            <label>Lý do từ chối <span class="text-danger">*</span></label>
                            <textarea id="rejectReasonInput" name="reason" class="form-control" rows="4" 
                                      placeholder="Nhập lý do từ chối đơn hàng (tối thiểu 10 ký tự)..." 
                                      style="resize: vertical;" required></textarea>
                            <small class="help-block text-muted">
                                <i class="fa fa-info-circle"></i> Vui lòng nhập ít nhất 10 ký tự để mô tả lý do từ chối.
                            </small>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Đóng</button>
                        <button type="submit" class="btn btn-danger"><i class="fa fa-times-circle"></i> Xác nhận từ chối</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <!-- Cancel Modal (for Shipping status) -->
    <div class="modal fade" id="cancelModal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header bg-danger">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title" style="color: white;"><i class="fa fa-ban"></i> Hủy đơn hàng</h4>
                </div>
                <form id="cancelForm" action="${pageContext.request.contextPath}/purchase-order" method="post" onsubmit="return validateCancelForm()">
                    <div class="modal-body">
                        <input type="hidden" name="action" value="cancel">
                        <input type="hidden" name="id" value="${po.poID}">
                        
                        <div class="alert alert-danger">
                            <i class="fa fa-exclamation-triangle"></i> Bạn đang hủy đơn hàng <strong>PO-${po.poID}</strong>. Vui lòng cung cấp lý do hủy.
                        </div>
                        
                        <div class="form-group">
                            <label>Lý do hủy <span class="text-danger">*</span></label>
                            <textarea id="cancelReasonInput" name="cancelReason" class="form-control" rows="4" 
                                      placeholder="Nhập lý do hủy đơn hàng (tối thiểu 10 ký tự)..." 
                                      style="resize: vertical;"></textarea>
                            <small class="help-block text-muted">
                                <i class="fa fa-info-circle"></i> Vui lòng nhập ít nhất 10 ký tự để mô tả lý do hủy.
                            </small>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Đóng</button>
                        <button type="submit" class="btn btn-danger"><i class="fa fa-ban"></i> Xác nhận hủy đơn</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <!-- Bootstrap 3.3.6 -->
    <script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
    <!-- AdminLTE App -->
    <script src="${pageContext.request.contextPath}/dist/js/app.min.js"></script>
    
    <script>
        // ===== GLOBAL FUNCTIONS =====
        
        // Function để hiện cancel modal
        function showCancelModal() {
            console.log('showCancelModal() called');
            showModal('cancelModal');
        }
        
        // Function để hiện reject modal
        function showRejectModal() {
            console.log('showRejectModal() called');
            const modal = document.getElementById('rejectModal');
            if (!modal) {
                console.error('Reject modal not found!');
                alert('Lỗi: Không tìm thấy modal từ chối đơn hàng. Vui lòng tải lại trang.');
                return;
            }
            showModal('rejectModal');
            
            // Focus vào textarea sau khi modal mở
            setTimeout(function() {
                const rejectReasonInput = document.getElementById('rejectReasonInput');
                if (rejectReasonInput) {
                    rejectReasonInput.focus();
                }
            }, 300);
        }
        
        function updateStatus(poID, statusID, statusName) {
            console.log('Updating status:', poID, statusID, statusName);
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = '${pageContext.request.contextPath}/purchase-order';
            
            const actionInput = document.createElement('input');
            actionInput.type = 'hidden';
            actionInput.name = 'action';
            actionInput.value = 'update-status';
            form.appendChild(actionInput);
            
            const idInput = document.createElement('input');
            idInput.type = 'hidden';
            idInput.name = 'id';
            idInput.value = poID;
            form.appendChild(idInput);
            
            const statusInput = document.createElement('input');
            statusInput.type = 'hidden';
            statusInput.name = 'statusID';
            statusInput.value = statusID;
            form.appendChild(statusInput);
            
            document.body.appendChild(form);
            form.submit();
        }
        
        function validateCancelForm() {
            console.log('=== validateCancelForm() called ===');
            const cancelReasonInput = document.getElementById('cancelReasonInput');
            
            if (!cancelReasonInput) {
                console.error('Cancel reason input not found!');
                alert('Lỗi: Không tìm thấy trường nhập lý do!');
                return false;
            }
            
            const cancelReason = cancelReasonInput.value.trim();
            console.log('Cancel reason:', cancelReason);
            console.log('Cancel reason length:', cancelReason.length);
            
            if (cancelReason.length === 0) {
                alert('⚠️ Vui lòng nhập lý do hủy đơn hàng!');
                cancelReasonInput.focus();
                return false;
            }
            
            if (cancelReason.length < 10) {
                alert('⚠️ Lý do hủy phải có ít nhất 10 ký tự!\n\nBạn đã nhập: ' + cancelReason.length + ' ký tự\nCần thêm: ' + (10 - cancelReason.length) + ' ký tự');
                cancelReasonInput.focus();
                return false;
            }
            
            console.log('✓ Cancel form validation passed');
            console.log('Submitting form...');
            return true;
        }
        
        function validateRejectForm() {
            console.log('=== validateRejectForm() called ===');
            const rejectReasonInput = document.getElementById('rejectReasonInput');
            
            if (!rejectReasonInput) {
                console.error('Reject reason input not found!');
                alert('Lỗi: Không tìm thấy trường nhập lý do!');
                return false;
            }
            
            const rejectReason = rejectReasonInput.value.trim();
            console.log('Reject reason:', rejectReason);
            console.log('Reject reason length:', rejectReason.length);
            
            if (rejectReason.length === 0) {
                alert('⚠️ Vui lòng nhập lý do từ chối đơn hàng!');
                rejectReasonInput.focus();
                return false;
            }
            
            if (rejectReason.length < 10) {
                alert('⚠️ Lý do từ chối phải có ít nhất 10 ký tự!\n\nBạn đã nhập: ' + rejectReason.length + ' ký tự\nCần thêm: ' + (10 - rejectReason.length) + ' ký tự');
                rejectReasonInput.focus();
                return false;
            }
            
            console.log('✓ Reject form validation passed');
            console.log('Submitting form...');
            return true;
        }
        
        // Function to show modal (Bootstrap 3 compatible)
        function showModal(modalId) {
            console.log('showModal() called with modalId:', modalId);
            const modal = document.getElementById(modalId);
            if (!modal) {
                console.error('Modal not found:', modalId);
                alert('Lỗi: Không tìm thấy modal với ID: ' + modalId);
                return;
            }
            
            console.log('Modal found, attempting to show...');
            
            // Try Bootstrap 5 API first
            if (typeof bootstrap !== 'undefined') {
                console.log('Using Bootstrap 5 API');
                const bsModal = new bootstrap.Modal(modal);
                bsModal.show();
            } else {
                console.log('Using Bootstrap 3 fallback');
                // Fallback for Bootstrap 3 - manual modal show
                // Remove any existing backdrop first
                const existingBackdrop = document.querySelector('.modal-backdrop');
                if (existingBackdrop) {
                    existingBackdrop.remove();
                }
                
                // Ensure modal is visible
                modal.style.display = 'block';
                modal.style.opacity = '0';
                modal.classList.add('in');
                modal.setAttribute('aria-hidden', 'false');
                document.body.classList.add('modal-open');
                document.body.style.overflow = 'hidden';
                
                // Create backdrop
                const backdrop = document.createElement('div');
                backdrop.className = 'modal-backdrop fade in';
                backdrop.setAttribute('data-modal-id', modalId);
                backdrop.style.zIndex = '1040';
                document.body.appendChild(backdrop);
                
                // Ensure modal has proper z-index
                modal.style.zIndex = '1050';
                
                console.log('Modal displayed, backdrop created');
                
                // Trigger fade in animation
                setTimeout(function() {
                    modal.style.opacity = '1';
                    modal.style.transition = 'opacity 0.3s';
                    console.log('Modal fade in animation triggered');
                }, 10);
            }
        }
        
        // Function to hide modal (Bootstrap 3 compatible)
        function hideModal(modalId) {
            const modal = document.getElementById(modalId);
            if (!modal) return;
            
            // Try Bootstrap 5 API first
            if (typeof bootstrap !== 'undefined') {
                const bsModal = bootstrap.Modal.getInstance(modal);
                if (bsModal) {
                    bsModal.hide();
                } else {
                    modal.style.display = 'none';
                }
            } else {
                // Fallback for Bootstrap 3 - manual modal hide
                modal.classList.remove('in');
                modal.style.opacity = '0';
                modal.setAttribute('aria-hidden', 'true');
                modal.style.transition = 'opacity 0.3s';
                
                setTimeout(function() {
                    modal.style.display = 'none';
                    document.body.classList.remove('modal-open');
                    document.body.style.overflow = '';
                    
                    // Remove backdrop for this specific modal
                    const backdrop = document.querySelector('.modal-backdrop[data-modal-id="' + modalId + '"]');
                    if (backdrop) {
                        backdrop.remove();
                    } else {
                        // Fallback: remove any backdrop if modal ID doesn't match
                        const anyBackdrop = document.querySelector('.modal-backdrop');
                        if (anyBackdrop) {
                            anyBackdrop.remove();
                        }
                    }
                }, 300); // Wait for fade animation
            }
        }
        
        // ===== DOCUMENT READY =====
        document.addEventListener('DOMContentLoaded', function() {
            console.log('=== PO Details Page Loaded ===');
            
            // Handle buttons with data-toggle="modal" and data-target
            const modalButtons = document.querySelectorAll('[data-toggle="modal"][data-target]');
            modalButtons.forEach(function(btn) {
                btn.addEventListener('click', function(e) {
                    e.preventDefault();
                    const target = this.getAttribute('data-target');
                    if (target) {
                        // Remove # if present
                        const modalId = target.replace('#', '');
                        showModal(modalId);
                    }
                });
            });
            
            // Handle backdrop clicks to close modal (using event delegation)
            document.addEventListener('click', function(e) {
                if (e.target.classList.contains('modal-backdrop')) {
                    const modalId = e.target.getAttribute('data-modal-id');
                    if (modalId) {
                        hideModal(modalId);
                    } else {
                        // Fallback: find the visible modal and close it
                        const visibleModal = document.querySelector('.modal.in');
                        if (visibleModal && visibleModal.id) {
                            hideModal(visibleModal.id);
                        }
                    }
                }
            });
            
            // Handle ESC key to close modal
            document.addEventListener('keydown', function(e) {
                if (e.key === 'Escape' || e.keyCode === 27) {
                    const visibleModal = document.querySelector('.modal.in');
                    if (visibleModal && visibleModal.id) {
                        hideModal(visibleModal.id);
                    }
                }
            });
            
            // Handle close buttons in all modals
            const allCloseButtons = document.querySelectorAll('[data-dismiss="modal"]');
            allCloseButtons.forEach(function(btn) {
                btn.addEventListener('click', function(e) {
                    e.preventDefault();
                    const modal = this.closest('.modal');
                    if (modal && modal.id) {
                        hideModal(modal.id);
                    }
                });
            });
            
            // Handle form submission in edit detail modals - close modal after submit
            const editDetailForms = document.querySelectorAll('form[action*="purchase-order"]');
            editDetailForms.forEach(function(form) {
                form.addEventListener('submit', function(e) {
                    // Let form submit normally, but close modal after a short delay
                    // This allows the form to submit before closing
                    setTimeout(function() {
                        const modal = form.closest('.modal');
                        if (modal && modal.id) {
                            hideModal(modal.id);
                        }
                    }, 100);
                });
            });
            
            // Reset form khi đóng modal (Bootstrap 3)
            const cancelModal = document.getElementById('cancelModal');
            const rejectModal = document.getElementById('rejectModal');
            const cancelForm = document.getElementById('cancelForm');
            const rejectForm = document.getElementById('rejectForm');
            
            if (cancelModal && cancelForm) {
                // Use custom event for Bootstrap 3
                const observer = new MutationObserver(function(mutations) {
                    if (cancelModal.style.display === 'none' && cancelForm) {
                        cancelForm.reset();
                    }
                });
                observer.observe(cancelModal, { attributes: true, attributeFilter: ['style'] });
            }
            
            if (rejectModal && rejectForm) {
                // Use custom event for Bootstrap 3
                const observer = new MutationObserver(function(mutations) {
                    if (rejectModal.style.display === 'none' && rejectForm) {
                        rejectForm.reset();
                    }
                });
                observer.observe(rejectModal, { attributes: true, attributeFilter: ['style'] });
            }
            
            // Update status buttons
            const updateStatusButtons = document.querySelectorAll('.js-update-status');
            updateStatusButtons.forEach(function(btn) {
                btn.addEventListener('click', function() {
                    const poId = this.getAttribute('data-po-id');
                    const statusId = this.getAttribute('data-status-id');
                    console.log('Update status button clicked:', poId, statusId);
                    updateStatus(poId, statusId);
                });
            });
        });
    </script>
</body>
</html>

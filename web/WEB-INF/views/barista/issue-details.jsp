<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Chi tiết báo cáo nguyên liệu - Coffee Shop Management</title>
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
                Chi tiết báo nguyên liệu
                <small>Báo cáo Nguyên liệu #${issue.issueID}</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${pageContext.request.contextPath}/barista/dashboard"><i class="fa fa-dashboard"></i> Trang chủ</a></li>
                <li><a href="${pageContext.request.contextPath}/barista/issues">Báo cáo nguyên liệu</a></li>
                <li class="active">Chi tiết</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-md-8">
                    <!-- Issue Details Box -->
                    <div class="box box-warning">
                        <div class="box-header with-border">
                            <h3 class="box-title">
                                <i class="fa fa-exclamation-triangle"></i>
                                Thông tin báo cáo nguyên liệu #${issue.issueID}
                            </h3>
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
                            
                            <!-- Requester Info -->
                            <div class="info-box" style="background-color: #f9f9f9; padding: 15px; border-radius: 4px; margin-bottom: 20px;">
                                <div class="info-row" style="margin-bottom: 10px;">
                                    <span class="info-label" style="font-weight: bold; display: inline-block; width: 150px;">Người yêu cầu:</span>
                                    <span>${issue.createdByName}</span>
                                </div>
                                <div class="info-row" style="margin-bottom: 10px;">
                                    <span class="info-label" style="font-weight: bold; display: inline-block; width: 150px;">Thời gian yêu cầu:</span>
                                    <span><fmt:formatDate value="${issue.createdAt}" pattern="dd/MM/yyyy HH:mm"/></span>
                                </div>
                            </div>
                            
                            <!-- Description/Notes -->
                            <div class="form-group">
                                <label for="description">
                                    Ghi chú <span class="text-danger">*</span>
                                </label>
                                <div class="well well-sm">
                                    ${issue.description}
                                </div>
                                <p class="help-block">Mô tả chi tiết về yêu cầu nhập kho</p>
                            </div>
                            
                            <!-- Ingredients List -->
                            <div class="form-group">
                                <label>Danh sách nguyên liệu <span class="text-danger">*</span></label>
                                <p class="help-block">Thông tin nguyên liệu trong yêu cầu nhập kho</p>
                                
                                <div class="box box-info" style="margin-top: 10px;">
                                    <div class="box-header">
                                        <h3 class="box-title">Chi tiết nguyên liệu</h3>
                                    </div>
                                    <div class="box-body">
                                        <table class="table table-bordered table-hover">
                                            <thead>
                                                <tr>
                                                    <th>Mã báo cáo</th>
                                                    <th>Nguyên liệu</th>
                                                    <th>Số lượng yêu cầu</th>
                                                    <th>Đơn vị</th>
                                                    <th>Trạng thái</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr>
                                                    <td><strong>#${issue.issueID}</strong></td>
                                                    <td><strong>${issue.ingredientName}</strong></td>
                                                    <td><strong class="text-danger">${issue.quantity}</strong></td>
                                                    <td>${issue.unitName != null ? issue.unitName : 'N/A'}</td>
                                                    <td>
                                                        <span class="label label-${issue.statusName == 'Reported' ? 'warning' : 
                                                                                     issue.statusName == 'Under Investigation' ? 'info' :
                                                                                     issue.statusName == 'Resolved' ? 'success' : 'danger'}">
                                                            ${issue.statusName}
                                                        </span>
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Confirmation Info -->
                            <c:if test="${issue.confirmedByName != null}">
                                <div class="info-box" style="background-color: #f0f8ff; padding: 15px; border-radius: 4px; margin-bottom: 20px;">
                                    <div class="info-row" style="margin-bottom: 10px;">
                                        <span class="info-label" style="font-weight: bold; display: inline-block; width: 150px;">Người xác nhận:</span>
                                        <span>${issue.confirmedByName}</span>
                                    </div>
                                </div>
                            </c:if>
                            
                            <!-- Alert based on status -->
                            <c:if test="${issue.statusName == 'Reported'}">
                                <div class="alert alert-warning">
                                    <h4><i class="fa fa-warning"></i> Lưu ý:</h4>
                                    Báo cáo này đang chờ được xem xét và xác nhận. Vui lòng kiểm tra và xác nhận nếu bạn có thẩm quyền.
                                </div>
                            </c:if>
                            
                            <c:if test="${issue.statusName == 'Under Investigation'}">
                                <div class="alert alert-info">
                                    <h4><i class="fa fa-search"></i> Đang điều tra:</h4>
                                    Báo cáo đang được điều tra và xử lý. Vui lòng theo dõi để có thông tin cập nhật.
                                </div>
                            </c:if>
                            
                            <c:if test="${issue.statusName == 'Resolved'}">
                                <div class="alert alert-success">
                                    <h4><i class="fa fa-check-circle"></i> Đã giải quyết:</h4>
                                    Báo cáo này đã được giải quyết thành công.
                                </div>
                            </c:if>
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
                            <!-- Action Buttons -->
                            
                            <!-- Show status-based messages and actions -->
                            <c:choose>
                                <%-- Status 25: Pending (Chờ xử lý) - Waiting for Inventory Staff approval --%>
                                <c:when test="${issue.statusID == 25}">
                                    <div class="callout callout-warning">
                                        <h4><i class="fa fa-clock-o"></i> Chờ phê duyệt</h4>
                                        <p>Yêu cầu sự cố đang chờ Inventory Staff phê duyệt. Bạn chưa thể xử lý.</p>
                                    </div>
                                    
                                    <!-- Edit Button - Only for Pending status -->
                                    <a href="${pageContext.request.contextPath}/barista/edit-issue?id=${issue.issueID}" 
                                       class="btn btn-warning btn-block">
                                        <i class="fa fa-edit"></i> Chỉnh sửa yêu cầu
                                    </a>
                                </c:when>
                                
                                <%-- Status 26: In Progress (Đang xử lý) - Approved by Inventory Staff --%>
                                <c:when test="${issue.statusID == 26}">
                                    <div class="callout callout-info">
                                        <h4><i class="fa fa-check"></i> Đã được phê duyệt</h4>
                                        <p>Yêu cầu đã được Inventory Staff phê duyệt. Bạn có thể giải quyết.</p>
                                    </div>
                                    
                                    <!-- Resolve Button -->
                                    <form method="post" action="${pageContext.request.contextPath}/barista/issues?action=resolve">
                                        <input type="hidden" name="id" value="${issue.issueID}">
                                        <button type="submit" class="btn btn-success btn-block">
                                            <i class="fa fa-check-circle"></i> Giải quyết xong
                                        </button>
                                    </form>
                                </c:when>
                                
                                <%-- Status 27: Resolved (Đã giải quyết) --%>
                                <c:when test="${issue.statusID == 27}">
                                    <div class="callout callout-success">
                                        <h4><i class="fa fa-check-circle"></i> Đã giải quyết</h4>
                                        <p>Sự cố này đã được giải quyết thành công.</p>
                                    </div>
                                </c:when>
                                
                                <%-- Status 28: Rejected (Từ chối) --%>
                                <c:when test="${issue.statusID == 28}">
                                    <div class="callout callout-danger">
                                        <h4><i class="fa fa-times-circle"></i> Đã từ chối</h4>
                                        <p>Yêu cầu sự cố này đã bị từ chối bởi Inventory Staff.</p>
                                    </div>
                                </c:when>
                                
                                <%-- Other statuses --%>
                                <c:otherwise>
                                    <div class="callout callout-default">
                                        <p>Trạng thái: ${issue.statusName}</p>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                            
                            <c:if test="${issue.statusID != 27 && issue.statusID != 28}">
                                <div class="alert alert-info">
                                    <i class="fa fa-info-circle"></i>
                                    Sự cố đã ${issue.statusName == 'Resolved' ? 'được giải quyết' : 'bị từ chối'}, không thể thay đổi trạng thái.
                                </div>
                                <hr>
                            </c:if>
                            
                            <a href="${pageContext.request.contextPath}/barista/issues" class="btn btn-default btn-block">
                                <i class="fa fa-arrow-left"></i> Quay lại danh sách
                            </a>
                        </div>
                    </div>
                    
                    <!-- Additional Info Box -->
                    <div class="box box-info">
                        <div class="box-header with-border">
                            <h3 class="box-title">Thông tin bổ sung</h3>
                        </div>
                        <div class="box-body">
                            <p><strong>Báo cáo:</strong> Nguyên liệu cần nhập</p>
                            <p><strong>Độ ưu tiên:</strong> 
                                <span class="label ${issue.quantity > 10 ? 'label-danger' : 
                                                     issue.quantity > 5 ? 'label-warning' : 'label-info'}">
                                    ${issue.quantity > 10 ? 'Cao' : issue.quantity > 5 ? 'Trung bình' : 'Thấp'}
                                </span>
                            </p>
                            <hr>
                            <p class="text-muted">
                                <small>
                                    <i class="fa fa-info-circle"></i>
                                    Hãy liên hệ với quản lý kho để xử lý sự cố này nếu cần thiết.
                                </small>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
    
    <!-- Include Footer -->
    <%@include file="../compoment/footer.jsp" %>

</div>

<!-- Reject Issue Modal -->
<div class="modal fade" id="rejectIssueModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <form method="post" action="${pageContext.request.contextPath}/barista/issues?action=reject">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">
                        <i class="fa fa-times-circle text-danger"></i> Từ chối xử lý báo cáo #${issue.issueID}
                    </h4>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="id" value="${issue.issueID}">
                    
                    <div class="form-group">
                        <label for="rejectionReason">Lý do từ chối: <span class="text-danger">*</span></label>
                        <textarea class="form-control" id="rejectionReason" name="rejectionReason" 
                                  rows="4" required placeholder="Vui lòng nhập lý do từ chối xử lý sự cố..."></textarea>
                        <span class="help-block">Ví dụ: Không đủ thẩm quyền, sự cố không thuộc phạm vi xử lý, vv.</span>
                    </div>
                    
                    <div class="alert alert-warning">
                        <i class="fa fa-warning"></i> 
                        <strong>Lưu ý:</strong> Sự cố sẽ chuyển sang trạng thái "Rejected" và không thể thay đổi sau đó.
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">
                        <i class="fa fa-times"></i> Đóng
                    </button>
                    <button type="submit" class="btn btn-danger">
                        <i class="fa fa-check"></i> Xác nhận từ chối
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

<script>
// Debug modal
$(document).ready(function() {
    console.log('jQuery loaded:', typeof jQuery !== 'undefined');
    console.log('Bootstrap loaded:', typeof $.fn.modal !== 'undefined');
    
    // Ensure modal works
    $('#rejectIssueModal').on('show.bs.modal', function (e) {
        console.log('Modal is showing');
    });
});
</script>

</body>
</html>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Chi tiết vấn đề - Coffee Shop</title>
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.6 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/AdminLTE.min.css">
    <!-- AdminLTE Skins -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/skins/_all-skins.min.css">
    <style>
        .detail-box {
            background: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }
        
        .info-row {
            padding: 10px 0;
            border-bottom: 1px solid #f0f0f0;
        }
        
        .info-row:last-child {
            border-bottom: none;
        }
        
        .info-label {
            font-weight: bold;
            color: #666;
        }
        
        .info-value {
            color: #333;
        }
        
        .status-badge {
            padding: 8px 15px;
            border-radius: 4px;
            font-size: 14px;
            display: inline-block;
        }
        
        .status-reported {
            background-color: #fee;
            color: #c00;
            border: 1px solid #fcc;
        }
        
        .status-investigation {
            background-color: #fff3cd;
            color: #856404;
            border: 1px solid #ffeaa7;
        }
        
        .status-resolved {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        
        .status-rejected {
            background-color: #e2e3e5;
            color: #383d41;
            border: 1px solid #d6d8db;
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
                    Chi tiết vấn đề
                    <small>Thông tin chi tiết vấn đề #${issue.issueID}</small>
                </h1>
                <ol class="breadcrumb">
                    <li><a href="${pageContext.request.contextPath}/inventory-dashboard"><i class="fa fa-dashboard"></i> Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/issue?action=list">Quản lý vấn đề</a></li>
                    <li class="active">Chi tiết vấn đề</li>
                </ol>
            </section>

            <section class="content">
                <!-- Error Alert from Session -->
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger alert-dismissible">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                        <h4><i class="icon fa fa-ban"></i> Lỗi!</h4>
                        ${errorMessage}
                    </div>
                </c:if>
                
                <!-- Error Alert from URL Parameter (fallback) -->
                <c:if test="${not empty param.error and empty errorMessage}">
                    <div class="alert alert-danger alert-dismissible">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                        <h4><i class="icon fa fa-ban"></i> Lỗi!</h4>
                        Đã xảy ra lỗi. Vui lòng thử lại.
                    </div>
                </c:if>
                
                <!-- Success Alert -->
                <c:if test="${not empty param.success}">
                    <div class="alert alert-success alert-dismissible">
                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                        <h4><i class="icon fa fa-check"></i> Thành công!</h4>
                        <c:choose>
                            <c:when test="${param.success eq '2'}">
                                Cập nhật vấn đề thành công
                            </c:when>
                            <c:when test="${param.success eq '3'}">
                                Phê duyệt sự cố thành công
                            </c:when>
                            <c:when test="${param.success eq '4'}">
                                Từ chối sự cố thành công
                            </c:when>
                            <c:otherwise>
                                ${param.success}
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:if>

                <div class="row">
                    <div class="col-md-8">
                        <!-- Issue Information -->
                        <div class="box box-primary">
                            <div class="box-header with-border">
                                <h3 class="box-title">Thông tin vấn đề</h3>
                            </div>
                            <div class="box-body">
                                <!-- Requester Info -->
                                <div class="callout callout-info">
                                    <h4><i class="fa fa-info-circle"></i> Thông tin yêu cầu</h4>
                                    <div class="info-row">
                                        <div class="row">
                                            <div class="col-md-4 info-label">Người yêu cầu:</div>
                                            <div class="col-md-8 info-value">
                                                <i class="fa fa-user"></i> ${issue.createdByName}
                                            </div>
                                        </div>
                                    </div>
                                    <div class="info-row">
                                        <div class="row">
                                            <div class="col-md-4 info-label">Thời gian yêu cầu:</div>
                                            <div class="col-md-8 info-value">
                                                <i class="fa fa-calendar"></i> 
                                                <fmt:formatDate value="${issue.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                
                                <!-- Description/Notes -->
                                <div class="form-group">
                                    <label>
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
                                                        <th>ID vấn đề</th>
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
                                                        <td><strong class="text-danger">
                                                            <fmt:formatNumber value="${issue.quantity}" pattern="#,##0.##"/>
                                                        </strong></td>
                                                        <td>${issue.unitName != null ? issue.unitName : 'N/A'}</td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${issue.statusName eq 'Reported'}">
                                                                    <span class="status-badge status-reported">
                                                                        <i class="fa fa-exclamation-circle"></i> ${issue.statusName}
                                                                    </span>
                                                                </c:when>
                                                                <c:when test="${issue.statusName eq 'Under Investigation'}">
                                                                    <span class="status-badge status-investigation">
                                                                        <i class="fa fa-search"></i> ${issue.statusName}
                                                                    </span>
                                                                </c:when>
                                                                <c:when test="${issue.statusName eq 'Resolved'}">
                                                                    <span class="status-badge status-resolved">
                                                                        <i class="fa fa-check-circle"></i> ${issue.statusName}
                                                                    </span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="status-badge status-rejected">
                                                                        <i class="fa fa-times-circle"></i> ${issue.statusName}
                                                                    </span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                                
                                <!-- Confirmation Info -->
                                <c:if test="${not empty issue.confirmedByName}">
                                    <div class="callout callout-success">
                                        <h4><i class="fa fa-check-circle"></i> Thông tin xác nhận</h4>
                                        <div class="info-row">
                                            <div class="row">
                                                <div class="col-md-4 info-label">Người xác nhận:</div>
                                                <div class="col-md-8 info-value">
                                                    <i class="fa fa-user-check"></i> ${issue.confirmedByName}
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>
                            </div>
                            
                            <div class="box-footer">
                                <!-- Show Approve/Reject buttons only for Pending status (StatusID = 25) -->
                                <c:if test="${issue.statusID == 25}">
                                    <form method="post" action="${pageContext.request.contextPath}/issue" style="display: inline;">
                                        <input type="hidden" name="action" value="approve">
                                        <input type="hidden" name="id" value="${issue.issueID}">
                                        <button type="submit" class="btn btn-success" onclick="return confirm('Xác nhận phê duyệt yêu cầu sự cố này?')">
                                            <i class="fa fa-check"></i> Phê duyệt
                                        </button>
                                    </form>
                                    
                                    <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#rejectModal">
                                        <i class="fa fa-times"></i> Từ chối
                                    </button>
                                </c:if>
                                
                                <a href="${pageContext.request.contextPath}/issue?action=list" 
                                   class="btn btn-default pull-right">
                                    <i class="fa fa-arrow-left"></i> Quay lại danh sách
                                </a>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-4">
                        <!-- Quick Actions -->
                        <div class="box box-success">
                            <div class="box-header with-border">
                                <h3 class="box-title">Thao tác nhanh</h3>
                            </div>
                            <div class="box-body">
                                <a href="${pageContext.request.contextPath}/ingredient?action=view&id=${issue.ingredientID}" 
                                   class="btn btn-block btn-info">
                                    <i class="fa fa-box"></i> Xem thông tin nguyên liệu
                                </a>
                                
                                <a href="${pageContext.request.contextPath}/issue?action=list&ingredientFilter=${issue.ingredientID}" 
                                   class="btn btn-block btn-default">
                                    <i class="fa fa-filter"></i> Xem các vấn đề khác của nguyên liệu này
                                </a>
                            </div>
                        </div>
                        
                        <!-- Status Guide -->
                        <div class="box box-info">
                            <div class="box-header with-border">
                                <h3 class="box-title">Hướng dẫn trạng thái</h3>
                            </div>
                            <div class="box-body">
                                <p><strong>Reported (Đã báo cáo):</strong><br>
                                   Vấn đề vừa được báo cáo và chờ xử lý</p>
                                
                                <p><strong>Under Investigation:</strong><br>
                                   Đang điều tra và xác minh vấn đề</p>
                                
                                <p><strong>Resolved (Đã giải quyết):</strong><br>
                                   Vấn đề đã được xử lý xong</p>
                                
                                <p><strong>Rejected (Từ chối):</strong><br>
                                   Vấn đề không hợp lệ hoặc không cần xử lý</p>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>

        <%@include file="../compoment/footer.jsp" %>
    </div>

    <!-- Reject Modal -->
    <div class="modal fade" id="rejectModal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <form method="post" action="${pageContext.request.contextPath}/issue">
                    <div class="modal-header bg-danger">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">
                            <i class="fa fa-times-circle"></i> Từ chối yêu cầu sự cố
                        </h4>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" name="action" value="reject">
                        <input type="hidden" name="id" value="${issue.issueID}">
                        
                        <div class="form-group">
                            <label for="rejectionReason">
                                Lý do từ chối <span class="text-danger">*</span>
                            </label>
                            <textarea class="form-control" 
                                      id="rejectionReason" 
                                      name="rejectionReason" 
                                      rows="4" 
                                      placeholder="Nhập lý do từ chối yêu cầu..."
                                      required></textarea>
                        </div>
                        
                        <div class="callout callout-warning">
                            <p><i class="fa fa-warning"></i> Yêu cầu sẽ chuyển sang trạng thái "Từ chối" sau khi xác nhận.</p>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Hủy</button>
                        <button type="submit" class="btn btn-danger">
                            <i class="fa fa-times"></i> Xác nhận từ chối
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- jQuery from CDN -->
    <script src="https://code.jquery.com/jquery-2.2.4.min.js"></script>
    <!-- Bootstrap 3.3.6 -->
    <script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
    <!-- AdminLTE App -->
    <script src="https://adminlte.io/themes/AdminLTE/dist/js/app.min.js"></script>
    
    <script>
        // Auto dismiss alerts after 5 seconds
        setTimeout(function() {
            $('.alert').fadeOut('slow');
        }, 5000);
        }, 5000);
    </script>
</body>
</html>

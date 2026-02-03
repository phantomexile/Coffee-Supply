<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Chỉnh Sửa Yêu Cầu Sự Cố - Coffee Shop</title>
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.6 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
    <!-- AdminLTE -->
    <link rel="stylesheet" href="https://adminlte.io/themes/AdminLTE/dist/css/AdminLTE.min.css">
    <link rel="stylesheet" href="https://adminlte.io/themes/AdminLTE/dist/css/skins/_all-skins.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar-custom.css">
    <style>
        .info-box {
            background-color: #f9f9f9;
            border: 1px solid #e0e0e0;
            padding: 15px;
            border-radius: 4px;
            margin-bottom: 20px;
        }
        .info-row {
            display: flex;
            margin-bottom: 8px;
        }
        .info-label {
            font-weight: bold;
            width: 150px;
        }
        .quantity-badge {
            display: inline-block;
            padding: 6px 12px;
            border-radius: 4px;
            background: #3c8dbc;
            color: #fff;
            font-weight: bold;
        }
        .current-ingredient {
            background-color: #fff7e6 !important;
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
                Chỉnh sửa yêu cầu sự cố
                <small>Cập nhật thông tin yêu cầu nhập kho</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${pageContext.request.contextPath}/barista/dashboard"><i class="fa fa-dashboard"></i> Trang chủ</a></li>
                <li><a href="${pageContext.request.contextPath}/barista/issues">Vấn đề nhập kho</a></li>
                <li class="active">Chỉnh sửa yêu cầu</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-md-10 col-md-offset-1">
                    <div class="box box-primary">
                        <div class="box-header with-border">
                            <h3 class="box-title"><i class="fa fa-edit"></i> Thông tin chỉnh sửa</h3>
                        </div>

                        <div class="box-body">
                            <!-- Messages -->
                            <c:if test="${not empty sessionScope.errorMessage}">
                                <div class="alert alert-danger alert-dismissible">
                                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                                    <i class="icon fa fa-ban"></i> ${sessionScope.errorMessage}
                                </div>
                                <c:remove var="errorMessage" scope="session"/>
                            </c:if>

                            <c:if test="${not empty sessionScope.successMessage}">
                                <div class="alert alert-success alert-dismissible">
                                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                                    <i class="icon fa fa-check"></i> ${sessionScope.successMessage}
                                </div>
                                <c:remove var="successMessage" scope="session"/>
                            </c:if>

                            <form action="${pageContext.request.contextPath}/barista/edit-issue" method="post" id="editIssueForm">
                                <input type="hidden" name="id" value="${issue.issueID}">

                                <div class="callout callout-info">
                                    <h4><i class="fa fa-info-circle"></i> Lưu ý</h4>
                                    <p>Yêu cầu vẫn giữ trạng thái <strong>Chờ xử lý</strong> sau khi cập nhật và chờ Inventory Staff phê duyệt.</p>
                                </div>

                                <div class="info-box">
                                    <div class="info-row">
                                        <span class="info-label">Mã yêu cầu:</span>
                                        <span>#${issue.issueID}</span>
                                    </div>
                                    <div class="info-row">
                                        <span class="info-label">Người tạo:</span>
                                        <span>${issue.createdByName}</span>
                                    </div>
                                    <div class="info-row">
                                        <span class="info-label">Thời gian tạo:</span>
                                        <span><fmt:formatDate value="${issue.createdAt}" pattern="dd/MM/yyyy HH:mm:ss"/></span>
                                    </div>
                                    <div class="info-row">
                                        <span class="info-label">Trạng thái:</span>
                                        <span>${issue.statusName}</span>
                                    </div>
                                </div>

                                <!-- Supplier -->
                                <div class="form-group">
                                    <label><i class="fa fa-industry"></i> Tên nhà cung cấp</label>
                                    <input type="text" class="form-control" value="${not empty supplierName ? supplierName : 'Không xác định'}" disabled>
                                    <input type="hidden" name="issueType" value="${issue.issueType != null ? issue.issueType : 'RESTOCK_REQUEST'}">
                                </div>

                                <!-- Ingredient selection -->
                                <div class="form-group">
                                    <label for="ingredientId"><i class="fa fa-cube"></i> Nguyên liệu <span class="text-danger">*</span></label>
                                    <select class="form-control" id="ingredientId" name="ingredientId" required>
                                        <option value="">-- Chọn nguyên liệu --</option>
                                        <c:forEach var="ingredient" items="${ingredients}">
                                            <option value="${ingredient.ingredientID}" ${ingredient.ingredientID == issue.ingredientID ? 'selected' : ''}>
                                                ${ingredient.name}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <!-- Quantity -->
                                <div class="form-group">
                                    <label for="quantity"><i class="fa fa-sort-numeric-asc"></i> Số lượng yêu cầu <span class="text-danger">*</span></label>
                                    <input type="number" class="form-control" id="quantity" name="quantity"
                                           min="0.01" step="0.01" value="${issue.quantity}" required>
                                    <p class="help-block">Số lượng phải lớn hơn 0.</p>
                                </div>

                                <!-- Notes -->
                                <div class="form-group">
                                    <label for="description"><i class="fa fa-sticky-note"></i> Ghi chú <span class="text-danger">*</span></label>
                                    <textarea class="form-control" id="description" name="description" rows="5" placeholder="Nhập ghi chú chi tiết cho yêu cầu" required>${issueNotes}</textarea>
                                    <p class="help-block">Thông tin rõ ràng giúp Inventory Staff xử lý nhanh hơn.</p>
                                </div>

                                <!-- Ingredient summary -->
                                <div class="box box-info">
                                    <div class="box-header with-border">
                                        <h3 class="box-title"><i class="fa fa-cubes"></i> Nguyên liệu trong yêu cầu</h3>
                                    </div>
                                    <div class="box-body">
                                        <div class="table-responsive">
                                            <table class="table table-bordered table-striped">
                                                <thead>
                                                    <tr>
                                                        <th>Nguyên liệu</th>
                                                        <th>Tồn kho hiện tại</th>
                                                        <th>Đơn vị</th>
                                                        <th>Số lượng yêu cầu</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="item" items="${relatedIssues}">
                                                        <c:set var="detail" value="${relatedIngredientMap[item.ingredientID]}" />
                                                        <tr class="${item.issueID == issue.issueID ? 'current-ingredient' : ''}">
                                                            <td><strong>${item.ingredientName}</strong></td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${detail != null && detail.stockQuantity != null}">
                                                                        ${detail.stockQuantity}
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <em class="text-muted">N/A</em>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${not empty item.unitName}">
                                                                        ${item.unitName}
                                                                    </c:when>
                                                                    <c:when test="${detail != null && not empty detail.unitName}">
                                                                        ${detail.unitName}
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <em class="text-muted">N/A</em>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>
                                                                <span class="quantity-badge">${item.quantity}</span>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                    <c:if test="${empty relatedIssues}">
                                                        <tr>
                                                            <td colspan="4" class="text-center"><em>Không có nguyên liệu nào</em></td>
                                                        </tr>
                                                    </c:if>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>

                                <div class="box-footer">
                                    <a href="${pageContext.request.contextPath}/barista/issue-details?id=${issue.issueID}" class="btn btn-default">
                                        <i class="fa fa-arrow-left"></i> Quay lại
                                    </a>
                                    <button type="submit" class="btn btn-warning pull-right">
                                        <i class="fa fa-save"></i> Lưu thay đổi
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>

    <!-- Footer -->
    <%@include file="../compoment/footer.jsp" %>
</div>

<!-- Scripts -->
<script src="https://code.jquery.com/jquery-2.2.4.min.js"></script>
<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
<script src="https://adminlte.io/themes/AdminLTE/dist/js/app.min.js"></script>
<script>
    $(function() {
        $('#editIssueForm').on('submit', function(e) {
            var ingredientId = $('#ingredientId').val();
            var quantity = parseFloat($('#quantity').val());
            var description = $('#description').val().trim();

            if (!ingredientId) {
                alert('Vui lòng chọn nguyên liệu');
                e.preventDefault();
                return false;
            }

            if (isNaN(quantity) || quantity <= 0) {
                alert('Số lượng phải lớn hơn 0');
                e.preventDefault();
                return false;
            }

            if (!description) {
                alert('Vui lòng nhập ghi chú cho yêu cầu');
                e.preventDefault();
                return false;
            }
        });
    });
</script>
</body>
</html>

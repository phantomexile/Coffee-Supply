<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>${action eq 'create' ? 'Thêm' : 'Chỉnh sửa'} cài đặt hệ thống - Coffee Shop Management</title>
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/AdminLTE.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/skins/_all-skins.min.css">
    
    <style>
        .required { color: #dd4b39; }
        .counter { font-size: 12px; color: #777; float: right; }
        .help-block { font-weight: bold; }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

    <%@include file="../compoment/header.jsp" %>
    <%@include file="../compoment/sidebar.jsp" %>

    
    <div class="content-wrapper">
        <section class="content-header">
            <h1>
                ${action eq 'create' ? 'Thêm cài đặt mới' : 'Chi tiết cài đặt'}
                <small>Quản lý cài đặt hệ thống</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${pageContext.request.contextPath}/admin/dashboard"><i class="fa fa-dashboard"></i> Trang chủ</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/setting">Cài đặt hệ thống</a></li>
                <li class="active">${action eq 'create' ? 'Thêm mới' : 'Chi tiết'}</li>
            </ol>
        </section>

        <section class="content">
            <div class="row">
                <div class="col-md-12">
                    <div class="box box-primary">
                        <div class="box-header with-border">
                            <h3 class="box-title">
                                <i class="fa fa-${action eq 'create' ? 'plus' : 'info-circle'}"></i>
                                ${action eq 'create' ? 'Thêm cài đặt mới' : 'Chi tiết cài đặt'}
                            </h3>
                        </div>
                        
                        <form method="post" action="${pageContext.request.contextPath}/admin/setting?action=${action eq 'create' ? 'create' : 'update'}">
                            <c:if test="${action eq 'edit'}">
                                <input type="hidden" name="settingId" value="${setting.settingID}">
                            </c:if>
                            <div class="box-body">
                                <c:if test="${not empty error}">
                                    <div class="alert alert-danger alert-dismissible">
                                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                        <h4><i class="icon fa fa-ban"></i> Lỗi!</h4>
                                        ${error}
                                    </div>
                                </c:if>
                                
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="form-group ${not empty validationErrors.name ? 'has-error' : ''}">
                                            <label for="name">Tên cài đặt <span class="required">*</span></label>
                                            <input type="text" class="form-control" id="name" name="name" 
                                                   value="${setting.name}"
                                                   placeholder="Nhập tên cài đặt...">
                                            <c:if test="${not empty validationErrors.name}">
                                                <span class="help-block">${validationErrors.name}</span>
                                            </c:if>
                                            <small class="text-muted">Tên cài đặt phải là duy nhất trong hệ thống</small>
                                        </div>
                                    </div>
                                    
                                    <div class="col-md-6">
                                        <div class="form-group ${not empty validationErrors.type ? 'has-error' : ''}">
                                            <label for="type">Loại cài đặt <span class="required">*</span></label>
                                            <select class="form-control" id="type" name="type">
                                                <option value="">Chọn loại cài đặt</option>
                                                <option value="Role" ${setting.type == 'Role' ? 'selected' : ''}>Vai trò (Role)</option>
                                                <option value="Category" ${setting.type == 'Category' ? 'selected' : ''}>Danh mục (Category)</option>
                                                <option value="Unit" ${setting.type == 'Unit' ? 'selected' : ''}>Đơn vị (Unit)</option>
                                                <option value="POStatus" ${setting.type == 'POStatus' ? 'selected' : ''}>Trạng thái PO (POStatus)</option>
                                                <option value="IssueStatus" ${setting.type == 'IssueStatus' ? 'selected' : ''}>Trạng thái sự cố (IssueStatus)</option>
                                                <option value="OrderStatus" ${setting.type == 'OrderStatus' ? 'selected' : ''}>Trạng thái đơn hàng (OrderStatus)</option>
                                            </select>
                                            <c:if test="${not empty validationErrors.type}">
                                                <span class="help-block">${validationErrors.type}</span>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                                
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="form-group ${not empty validationErrors.value ? 'has-error' : ''}">
                                            <label for="value">Giá trị <span class="required">*</span></label>
                                            <input type="text" class="form-control" id="value" name="value" 
                                                   value="${setting.value}"
                                                   placeholder="Nhập giá trị cài đặt...">
                                            <c:if test="${not empty validationErrors.value}">
                                                <span class="help-block">${validationErrors.value}</span>
                                            </c:if>
                                            <small class="text-muted">Giá trị phải là duy nhất trong cùng loại cài đặt</small>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-group ${not empty validationErrors.priority ? 'has-error' : ''}">
                                            <label for="priority">Độ ưu tiên <span class="required">*</span></label>
                                            <input type="number" class="form-control" id="priority" name="priority" 
                                                   value="${setting.priority != null ? setting.priority : 1}"
                                                   min="1" max="999"
                                                   placeholder="Nhập độ ưu tiên...">
                                            <c:if test="${not empty validationErrors.priority}">
                                                <span class="help-block">${validationErrors.priority}</span>
                                            </c:if>
                                            <small class="text-muted">Số nhỏ hơn = ưu tiên cao hơn</small>
                                        </div>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label>Trạng thái</label>
                                    <div class="radio">
                                        <label style="margin-right:20px;">
                                            <input type="radio" name="isActive" value="true" 
                                                   ${setting.active || action eq 'create' ? 'checked' : ''}>
                                            Hoạt động
                                        </label>
                                        <label>
                                            <input type="radio" name="isActive" value="false" 
                                                   ${!setting.active && action ne 'create' ? 'checked' : ''}>
                                            Không hoạt động
                                        </label>
                                    </div>
                                    <small class="text-muted">Chỉ các cài đặt được kích hoạt mới có hiệu lực</small>
                                </div>

                                <div class="form-group ${not empty validationErrors.description ? 'has-error' : ''}">
                                    <label for="description" style="width:100%;">Mô tả
                                        <span id="descriptionCounter" class="counter">0/255 kí tự</span>
                                    </label>
                                    <textarea class="form-control" id="description" name="description" 
                                              rows="6" maxlength="255"
                                              placeholder="Nhập mô tả cho cài đặt...">${setting.description}</textarea>
                                    <c:if test="${not empty validationErrors.description}">
                                        <span class="help-block">${validationErrors.description}</span>
                                    </c:if>
                                </div>
                            </div>
                            
                            <div class="box-footer">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fa fa-save"></i> 
                                    ${action eq 'create' ? 'Thêm cài đặt' : 'Cập nhật'}
                                </button>
                                <a href="${pageContext.request.contextPath}/admin/setting" class="btn btn-default">
                                    <i class="fa fa-times"></i> Hủy
                                </a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    </div>
    
    <%@include file="../compoment/footer.jsp" %>

</div>

<script src="${pageContext.request.contextPath}/plugins/jQuery/jquery-2.2.3.min.js"></script>
<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/dist/js/app.min.js"></script>

<script>
    (function() {
        var description = document.getElementById('description');
        var counter = document.getElementById('descriptionCounter');
        if (description && counter) {
            var update = function() {
                var len = description.value ? description.value.length : 0;
                counter.textContent = len + '/255 kí tự';
            };
            description.addEventListener('input', update);
            update();
        }
    })();
    
    // Form validation removed - using backend validation only
</script>

</body>
</html>


<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>${action eq 'new' ? 'Thêm' : 'Chỉnh sửa'} nhà cung cấp - Coffee Shop Management</title>
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
                    ${action eq 'new' ? 'Thêm' : 'Chỉnh sửa'} nhà cung cấp
                    <small>${action eq 'new' ? 'Tạo mới nhà cung cấp' : 'Cập nhật thông tin nhà cung cấp'}</small>
                </h1>
                <ol class="breadcrumb">
                    <li><a href="${pageContext.request.contextPath}/admin/dashboard"><i class="fa fa-dashboard"></i> Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/supplier?action=list">Nhà cung cấp</a></li>
                    <li class="active">${action eq 'new' ? 'Thêm mới' : 'Chỉnh sửa'}</li>
                </ol>
            </section>

            <!-- Main content -->
            <section class="content">

                <div class="row">
                    <div class="col-md-8 col-md-offset-2">
                        <div class="box box-primary">
                            <div class="box-header with-border">
                            <h3 class="box-title">
                                <i class="fa fa-edit"></i> 
                                ${action eq 'new' ? 'Thông tin nhà cung cấp mới' : 'Cập nhật thông tin nhà cung cấp'}
                            </h3>
                            </div>
                            
                            <!-- Form -->
                            <c:set var="errors" value="${validationErrors}" />
                            <form method="post" 
                                  action="${pageContext.request.contextPath}/admin/supplier?action=${action eq 'new' ? 'new' : 'edit'}">
                                
                                <c:if test="${action eq 'edit'}">
                                    <input type="hidden" name="supplierId" value="${supplier.supplierID}">
                                </c:if>
                                
                                <div class="box-body">
                                    <c:if test="${not empty errors['system']}">
                                        <div class="alert alert-danger">${errors['system']}</div>
                                    </c:if>
                                    <c:if test="${not empty errors['supplierId']}">
                                        <div class="alert alert-danger">${errors['supplierId']}</div>
                                    </c:if>
                                    <!-- Supplier Name -->
                                    <div class="form-group" id="supplierNameGroup">
                                        <label for="supplierName">
                                            Tên nhà cung cấp <span style="color: red;"></span>
                                        </label>
                                        <input type="text" 
                                               class="form-control" 
                                               id="supplierName" 
                                               name="supplierName" 
                                               placeholder="Nhập tên nhà cung cấp"
                                               value="${supplier.supplierName != null ? supplier.supplierName : ''}">
                                        <c:if test="${not empty errors['supplierName']}">
                                            <span class="help-block" style="color: #dd4b39;">${errors['supplierName']}</span>
                                        </c:if>
                                        <span class="help-block" id="supplierNameError" style="display: none; color: #dd4b39;"></span>
                                    </div>

                                    <!-- Contact Name -->
                                    <div class="form-group">
                                        <label for="contactName">Người liên hệ</label>
                                        <input type="text"
                                               class="form-control"
                                               id="contactName"
                                               name="contactName"
                                               placeholder="Nhập tên người liên hệ"
                                               value="${supplier.contactName != null ? supplier.contactName : ''}">
                                        <c:if test="${not empty errors['contactName']}">
                                            <span class="help-block" style="color: #dd4b39;">${errors['contactName']}</span>
                                        </c:if>
                                    </div>

                                    <!-- Email -->
                                    <div class="form-group" id="emailGroup">
                                        <label for="email">Email</label>
                                        <input type="text" 
                                               class="form-control" 
                                               id="email" 
                                               name="email" 
                                               placeholder="Nhập địa chỉ email"
                                               value="${supplier.email != null ? supplier.email : ''}">
                                        <c:if test="${not empty errors['email']}">
                                            <span class="help-block" style="color: #dd4b39;">${errors['email']}</span>
                                        </c:if>
                                        <span class="help-block" id="emailError" style="display: none; color: #dd4b39;"></span>
                                    </div>

                                    <!-- Phone -->
                                    <div class="form-group" id="phoneGroup">
                                        <label for="phone">Số điện thoại</label>
                                        <input type="tel" 
                                               class="form-control" 
                                               id="phone" 
                                               name="phone" 
                                               placeholder="Nhập số điện thoại"
                                               value="${supplier.phone != null ? supplier.phone : ''}">
                                        <c:if test="${not empty errors['phone']}">
                                            <span class="help-block" style="color: #dd4b39;">${errors['phone']}</span>
                                        </c:if>
                                        <span class="help-block" id="phoneError" style="display: none; color: #dd4b39;"></span>
                                    </div>

                                    <!-- Address -->
                                    <div class="form-group">
                                        <label for="address">Địa chỉ</label>
                                        <textarea class="form-control" 
                                                  id="address" 
                                                  name="address" 
                                                  rows="3" 
                                                  placeholder="Nhập địa chỉ chi tiết">${supplier.address != null ? supplier.address : ''}</textarea>
                                        <c:if test="${not empty errors['address']}">
                                            <span class="help-block" style="color: #dd4b39;">${errors['address']}</span>
                                        </c:if>
                                    </div>

                                    <!-- Is Active -->
                                    <div class="form-group">
                                        <label>Trạng thái</label>
                                        <div class="radio">
                                            <label style="margin-right:20px;">
                                                <input type="radio" name="isActive" value="true" 
                                                       ${(supplier != null && supplier.active == true) || (action eq 'new') ? 'checked' : ''}>
                                                Hoạt động
                                            </label>
                                            <label>
                                                <input type="radio" name="isActive" value="false" 
                                                       ${(supplier != null && supplier.active == false) && (action ne 'new') ? 'checked' : ''}>
                                                Không hoạt động
                                            </label>
                                        </div>
                                    </div>
                                </div>

                                <div class="box-footer">
                                    <button type="submit" class="btn btn-primary">
                                        <i class="fa fa-save"></i> ${action eq 'new' ? 'Tạo mới' : 'Cập nhật'}
                                    </button>
                                    <a href="${pageContext.request.contextPath}/admin/supplier?action=list" 
                                       class="btn btn-default">
                                        <i class="fa fa-times"></i> Hủy
                                    </a>
                                </div>
                            </form>
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
    
</body>
</html>

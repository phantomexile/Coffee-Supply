<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>${product != null ? 'Cập nhật' : 'Tạo mới'} Sản phẩm - Coffee Shop Management</title>
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
        .content-wrapper {
            background-color: #ecf0f5;
            min-height: 100vh;
        }
        
        .box {
            border-radius: 3px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24);
            transition: all 0.3s ease;
        }
        
        /* Ensure columns are visible */
        .box-body .row {
            margin-left: -15px;
            margin-right: -15px;
            display: block;
            overflow: hidden;
        }
        
        .box-body .row:after {
            content: "";
            display: table;
            clear: both;
        }
        
        .box-body .col-md-6 {
            position: relative;
            min-height: 1px;
            padding-left: 15px;
            padding-right: 15px;
            display: block !important;
            visibility: visible !important;
        }
        
        @media (min-width: 992px) {
            .box-body .col-md-6 {
                width: 50% !important;
                float: left !important;
            }
        }
        
        /* Force right column visibility */
        .box-body .col-md-6:last-child {
            display: block !important;
            opacity: 1 !important;
        }
        
        .box:hover {
            box-shadow: 0 3px 6px rgba(0,0,0,0.16), 0 3px 6px rgba(0,0,0,0.23);
        }
        
        .box-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 3px 3px 0 0;
            padding: 15px;
        }
        
        .box-header .box-title {
            font-size: 20px;
            font-weight: 600;
            margin: 0;
            color: white;
        }
        
        .box-body {
            padding: 25px;
            background: white;
        }
        
        .form-group {
            margin-bottom: 25px;
        }
        
        .form-group label {
            font-weight: 600;
            color: #333;
            margin-bottom: 8px;
            display: block;
            font-size: 14px;
        }
        
        .form-group label.required:after {
            content: " *";
            color: #e74c3c;
            font-weight: bold;
        }
        
        .form-control {
            border-radius: 4px;
            border: 2px solid #e0e0e0;
            padding: 10px 15px;
            font-size: 14px;
            transition: all 0.3s ease;
        }
        
        .form-control:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
            outline: none;
        }
        
        .form-control:hover {
            border-color: #bbb;
        }
        
        textarea.form-control {
            resize: vertical;
            min-height: 100px;
        }
        
        .input-group-addon {
            background: #667eea;
            color: white;
            border: 2px solid #667eea;
            font-weight: 600;
            padding: 10px 15px;
        }
        
        .help-block {
            color: #7f8c8d;
            font-size: 12px;
            margin-top: 5px;
            font-style: italic;
        }
        
        .image-preview {
            max-width: 100%;
            max-height: 300px;
            margin-top: 15px;
            display: none;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        
        input[type="file"] {
            border: 2px dashed #e0e0e0;
            padding: 15px;
            border-radius: 4px;
            background: #f9f9f9;
            cursor: pointer;
            transition: all 0.3s ease;
            width: 100%;
        }
        
        input[type="file"]:hover {
            border-color: #667eea;
            background: #f0f4ff;
        }
        
        .img-thumbnail {
            padding: 8px;
            background-color: #fff;
            border: 2px solid #dee2e6;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            transition: transform 0.3s ease;
        }
        
        .img-thumbnail:hover {
            transform: scale(1.05);
            box-shadow: 0 4px 8px rgba(0,0,0,0.15);
        }
        
        .checkbox label {
            font-weight: 500;
            color: #555;
            padding-left: 5px;
        }
        
        .checkbox input[type="checkbox"] {
            width: 18px;
            height: 18px;
            margin-right: 8px;
            cursor: pointer;
        }
        
        .box-footer {
            background: #f9f9f9;
            border-top: 1px solid #e0e0e0;
            padding: 15px 25px;
            border-radius: 0 0 3px 3px;
        }
        input, select {
    padding: 8px 16px !important;
    height: 40px !important;
    font-size: 16px;
}
select {
    background-position: right 12px center;
    padding-right: 32px !important;
}
label {
    margin-bottom: 6px;
    font-weight: 500;
}
        .btn {
            padding: 10px 24px;
            font-size: 14px;
            font-weight: 600;
            border-radius: 4px;
            transition: all 0.3s ease;
            border: none;
            cursor: pointer;
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            box-shadow: 0 2px 4px rgba(102, 126, 234, 0.4);
        }
        
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(102, 126, 234, 0.6);
        }
        
        .btn-default {
            background: #ecf0f1;
            color: #555;
            border: 2px solid #bdc3c7;
        }
        
        .btn-default:hover {
            background: #bdc3c7;
            color: #333;
            transform: translateY(-2px);
        }
        
        .alert {
            border-radius: 4px;
            padding: 15px 20px;
            margin-bottom: 20px;
            border: none;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        
        .alert-danger {
            background: #fee;
            color: #c33;
            border-left: 4px solid #e74c3c;
        }
        
        .alert-success {
            background: #efe;
            color: #2c7;
            border-left: 4px solid #27ae60;
        }
        
        .breadcrumb {
            background: transparent;
            padding: 0;
            margin-bottom: 20px;
        }
        
        .breadcrumb > li + li:before {
            content: ">\00a0";
            color: #999;
        }
        
        .image-upload-area {
            border: 2px dashed #e0e0e0;
            border-radius: 8px;
            padding: 20px;
            text-align: center;
            background: #fafafa;
            transition: all 0.3s ease;
        }
        
        .image-upload-area:hover {
            border-color: #667eea;
            background: #f0f4ff;
        }
        
        .current-image-label {
            font-size: 13px;
            font-weight: 600;
            color: #555;
            margin-bottom: 10px;
            display: inline-block;
        }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <!-- Include header -->
    <jsp:include page="../compoment/header.jsp" />
    
    <!-- Include sidebar -->
    <jsp:include page="../compoment/sidebar.jsp" />
    
    <!-- Content Wrapper -->
    <div class="content-wrapper">
        <!-- Content Header -->
        <section class="content-header">
            <h1>
                ${product != null ? 'Cập nhật' : 'Tạo mới'} Sản phẩm
                <small>Quản lý sản phẩm</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${pageContext.request.contextPath}/admin/dashboard"><i class="fa fa-dashboard"></i> Dashboard</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/products">Sản phẩm</a></li>
                <li class="active">${product != null ? 'Cập nhật' : 'Tạo mới'}</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">
            <!-- Error/Success Messages -->
            <c:if test="${not empty sessionScope.errorMessage}">
                <div class="alert alert-danger alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                    <h4><i class="icon fa fa-ban"></i> Lỗi!</h4>
                    ${sessionScope.errorMessage}
                </div>
                <c:remove var="errorMessage" scope="session"/>
            </c:if>

            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                    <h4><i class="icon fa fa-check"></i> Thành công!</h4>
                    ${sessionScope.successMessage}
                </div>
                <c:remove var="successMessage" scope="session"/>
            </c:if>

            <!-- DEBUG INFO -->
            <div style="display:none;">
                Product: ${product != null ? product.productID : 'NULL'}
                Categories: ${not empty categories ? categories.size() : 0}
            </div>
            
            <div class="row">
                <div class="col-md-12">
                    <div class="box box-primary">
                        <div class="box-header with-border">
                            <h3 class="box-title">Thông tin sản phẩm</h3>
                        </div>
                        
                        <form action="${pageContext.request.contextPath}/admin/products" 
                              method="post" 
                              enctype="multipart/form-data"
                              id="productForm">
                            
                            <input type="hidden" name="action" value="${product != null ? 'update' : 'create'}">
                            <c:if test="${product != null}">
                                <input type="hidden" name="productId" value="${product.productID}">
                            </c:if>
                            
                            <div class="box-body">
                                <div class="row">
                                    <!-- Left Column -->
                                    <div class="col-md-6">
                                        <!-- Product Name -->
                                        <div class="form-group">
                                            <label for="productName" class="required">Tên sản phẩm</label>
                                            <input type="text" 
                                                   class="form-control" 
                                                   id="productName" 
                                                   name="productName" 
                                                   value="${product.productName}" 
                                                   required
                                                   placeholder="Nhập tên sản phẩm">
                                            <span class="help-block">Tên sản phẩm phải rõ ràng và dễ hiểu</span>
                                        </div>

                                        <!-- Category -->
                                        <div class="form-group">
                                            <label for="categoryId" class="required">Danh mục</label>
                                            <select class="form-control" id="categoryId" name="categoryId" required>
                                                <option value="">-- Chọn danh mục --</option>
                                                <c:forEach var="category" items="${categories}">
                                                    <option value="${category[0]}" 
                                                            ${product != null && product.categoryID == category[0] ? 'selected' : ''}>
                                                        ${category[1]}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>

                                        <!-- Price -->
                                        <div class="form-group">
                                            <label for="price" class="required">Giá</label>
                                            <div class="input-group">
                                                <input type="number" 
                                                       class="form-control" 
                                                       id="price" 
                                                       name="price" 
                                                       value="${product.price}" 
                                                       step="0.01" 
                                                       min="0"
                                                       required
                                                       placeholder="0.00">
                                                <span class="input-group-addon">VNĐ</span>
                                            </div>
                                            <span class="help-block">Giá bán của sản phẩm</span>
                                        </div>

                                        <!-- Is Active -->
                                        <div class="form-group">
                                            <div class="checkbox">
                                                <label>
                                                    <input type="checkbox" 
                                                           name="isActive" 
                                                           ${product == null || product.isActive ? 'checked' : ''}>
                                                    <strong>Kích hoạt sản phẩm</strong>
                                                </label>
                                            </div>
                                            <span class="help-block">Sản phẩm kích hoạt sẽ hiển thị trên hệ thống</span>
                                        </div>
                                    </div>

                                    <!-- Right Column -->
                                    <div class="col-md-6">
                                        <!-- Description -->
                                        <div class="form-group">
                                            <label for="description">Mô tả</label>
                                            <textarea class="form-control" 
                                                      id="description" 
                                                      name="description" 
                                                      rows="6"
                                                      placeholder="Nhập mô tả sản phẩm"><c:out value="${product.description}" default=""/></textarea>
                                            <span class="help-block">Mô tả chi tiết về sản phẩm</span>
                                        </div>

                                        <!-- Image Upload -->
                                        <div class="form-group">
                                            <label for="imageFile">Hình ảnh sản phẩm</label>
                                            <div class="image-upload-area">
                                                <input type="file" 
                                                       id="imageFile" 
                                                       name="imageFile" 
                                                       accept="image/jpeg,image/png,image/jpg,image/gif"
                                                       onchange="previewImage(this)">
                                                <span class="help-block">
                                                    <i class="fa fa-cloud-upload"></i> Định dạng: JPG, JPEG, PNG, GIF. Kích thước tối đa: 10MB
                                                </span>
                                            </div>
                                            
                                            <!-- Image Preview -->
                                            <c:if test="${product != null && not empty product.imageUrl}">
                                                <div style="margin-top: 15px;">
                                                    <span class="current-image-label">
                                                        <i class="fa fa-image"></i> Hình ảnh hiện tại:
                                                    </span>
                                                    <div style="margin-top: 10px;">
                                                        <img src="${pageContext.request.contextPath}${product.imageUrl}" 
                                                             class="img-thumbnail" 
                                                             style="max-width: 250px; max-height: 250px; display: block;"
                                                             onerror="if(!this.dataset.error){this.dataset.error='1';this.style.display='none';this.parentElement.innerHTML='<div style=\'padding:20px;text-align:center;color:#999;\'><i class=\'fa fa-image fa-2x\'></i><p>Không thể tải hình ảnh</p></div>';}">
                                                    </div>
                                                </div>
                                            </c:if>
                                            
                                            <img id="imagePreview" class="img-thumbnail image-preview" alt="Preview">
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="box-footer">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fa fa-save"></i> ${product != null ? 'Cập nhật' : 'Tạo mới'}
                                </button>
                                <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-default">
                                    <i class="fa fa-times"></i> Hủy
                                </a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    </div>

    <!-- Include footer -->
    <jsp:include page="../compoment/footer.jsp" />
</div>

<!-- jQuery 2.2.3 -->
<script src="${pageContext.request.contextPath}/plugins/jQuery/jquery-2.2.3.min.js"></script>
<!-- Bootstrap 3.3.6 -->
<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
<!-- AdminLTE App -->
<script src="${pageContext.request.contextPath}/dist/js/app.min.js"></script>

<script>
    // Image preview function
    function previewImage(input) {
        var preview = document.getElementById('imagePreview');
        
        if (input.files && input.files[0]) {
            // Check file size (10MB = 10 * 1024 * 1024 bytes)
            if (input.files[0].size > 10 * 1024 * 1024) {
                alert('Kích thước file không được vượt quá 10MB!');
                input.value = '';
                preview.style.display = 'none';
                return;
            }
            
            // Check file type
            var fileType = input.files[0].type;
            if (!['image/jpeg', 'image/png', 'image/jpg', 'image/gif'].includes(fileType)) {
                alert('Chỉ chấp nhận file JPG, JPEG, PNG, GIF!');
                input.value = '';
                preview.style.display = 'none';
                return;
            }
            
            var reader = new FileReader();
            
            reader.onload = function(e) {
                preview.src = e.target.result;
                preview.style.display = 'block';
            }
            
            reader.readAsDataURL(input.files[0]);
        } else {
            preview.style.display = 'none';
        }
    }

    // Form validation
    $(document).ready(function() {
        $('#productForm').on('submit', function(e) {
            var price = parseFloat($('#price').val());
            
            if (price < 0) {
                alert('Giá sản phẩm không được âm!');
                e.preventDefault();
                return false;
            }
            
            return true;
        });
    });
</script>
</body>
</html>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<c:set var="title" value="Chi tiết sản phẩm - Coffee Shop Management" scope="request"/> <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Admin Dashboard - Coffee Shop Management</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.6 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
    <!-- Ionicons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/AdminLTE.min.css">
    <!-- AdminLTE Skins -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/skins/_all-skins.min.css">
    <!-- Morris charts -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/plugins/morris/morris.css">
    <!-- Sidebar improvements -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar-improvements.css">
    <!-- Chart.js -->
    
    <style>
        .content-wrapper {
            margin-left: 230px;
            min-height: 100vh;
            background-color: #f4f4f4;
            padding: 20px;
        }
        
        .content-header {
            margin-bottom: 20px;
        }
        
        .content-header h1 {
            margin: 0;
            font-size: 24px;
            font-weight: 300;
        }
        
        .breadcrumb {
            background: transparent;
            margin-top: 5px;
            padding: 0;
            font-size: 12px;
        }
        
        .box {
            background: #fff;
            border-top: 3px solid #d2d6de;
            margin-bottom: 20px;
            width: 100%;
            box-shadow: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24);
        }
        
        .box-header {
            border-bottom: 1px solid #f4f4f4;
            padding: 10px 15px;
            position: relative;
        }
        
        .box-title {
            font-size: 18px;
            margin: 0;
            line-height: 1.8;
        }
        
        .box-body {
            padding: 15px;
        }
        
        .btn {
            display: inline-block;
            padding: 6px 12px;
            margin-bottom: 0;
            font-size: 14px;
            font-weight: normal;
            line-height: 1.42857143;
            text-align: center;
            white-space: nowrap;
            vertical-align: middle;
            cursor: pointer;
            border: 1px solid transparent;
            border-radius: 4px;
            text-decoration: none;
        }
        
        .btn-primary {
            color: #fff;
            background-color: #3c8dbc;
            border-color: #367fa9;
        }
        
        .btn-default {
            color: #333;
            background-color: #fff;
            border-color: #ccc;
        }
        
        .label {
            display: inline;
            padding: .2em .6em .3em;
            font-size: 75%;
            font-weight: bold;
            line-height: 1;
            color: #fff;
            text-align: center;
            white-space: nowrap;
            vertical-align: baseline;
            border-radius: .25em;
        }
        
        .label-success {
            background-color: #5cb85c;
        }
        
        .label-danger {
            background-color: #d9534f;
        }
        
        .info-item {
            display: flex;
            margin-bottom: 15px;
            padding: 10px 0;
            border-bottom: 1px solid #eee;
        }
        
        .info-label {
            font-weight: bold;
            width: 150px;
            color: #333;
        }
        
        .info-value {
            flex: 1;
            color: #666;
        }
        
        .product-details {
            background: #f9f9f9;
            padding: 20px;
            border-radius: 4px;
            margin-bottom: 20px;
        }
        
        .price-display {
            font-size: 24px;
            color: #e74c3c;
            font-weight: bold;
        }
        
        .category-name {
            color: #3498db;
            font-weight: 500;
        }
        
        .category-name i {
            margin-right: 5px;
            color: #7f8c8d;
        }
        
        .nav-tabs {
            border-bottom: 2px solid #ddd;
            margin-bottom: 20px;
        }
        
        .nav-tabs > li > a {
            border-radius: 4px 4px 0 0;
            margin-right: 2px;
            line-height: 1.42857143;
            border: 1px solid transparent;
            padding: 10px 15px;
        }
        
        .nav-tabs > li.active > a,
        .nav-tabs > li.active > a:hover,
        .nav-tabs > li.active > a:focus {
            color: #555;
            cursor: default;
            background-color: #fff;
            border: 1px solid #ddd;
            border-bottom-color: transparent;
        }
        
        .tab-content {
            padding: 20px 0;
        }
        
        .tab-pane {
            display: none !important;
        }
        
        .tab-pane.active {
            display: block !important;
        }
        
        /* Ensure tab links are clickable */
        .nav-tabs > li > a {
            cursor: pointer;
        }
        
        .nav-tabs > li > a:hover {
            background-color: #f5f5f5;
        }
        
        .bom-card-container {
            display: flex;
            justify-content: center;
            margin-top: 20px;
        }
        
        .bom-product-card {
            border: 1px solid #ddd;
            border-radius: 4px;
            margin-bottom: 15px;
            transition: box-shadow 0.3s;
            display: flex;
            flex-direction: column;
            width: 30%;
            max-width: 280px;
            min-width: 220px;
        }
        
        .bom-product-card:hover {
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        
        .bom-product-image-container {
            width: 100%;
            height: 140px;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 4px;
            background: #f9f9f9;
            border-bottom: 2px solid #3c8dbc;
            overflow: hidden;
        }
        
        .bom-product-image {
            max-width: 100%;
            max-height: 100%;
            width: auto;
            height: auto;
            object-fit: contain;
        }
        
        .bom-product-image-placeholder {
            width: 100%;
            height: 140px;
            background: #f4f4f4;
            display: flex;
            align-items: center;
            justify-content: center;
            border-bottom: 2px solid #3c8dbc;
        }
        
        .bom-product-image-placeholder i {
            font-size: 32px;
            color: #999;
        }
        
        .bom-product-body {
            padding: 12px;
            flex-grow: 1;
            display: flex;
            flex-direction: column;
        }
        
        .bom-product-name {
            font-weight: bold;
            font-size: 17px;
            margin-bottom: 10px;
            color: #333;
        }
        
        .bom-product-formula {
            color: #666;
            font-size: 11px;
            min-height: 45px;
            margin-bottom: 10px;
            line-height: 1.4;
            flex-grow: 1;
            font-style: italic;
        }
        
        .bom-product-footer {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding-top: 8px;
            border-top: 1px solid #eee;
        }
        
        .bom-product-price {
            font-weight: bold;
            color: #3c8dbc;
            font-size: 13px;
        }
        
        .bom-empty {
            text-align: center;
            padding: 60px 40px;
            color: #999;
            background: #f9f9f9;
            border-radius: 8px;
            border: 2px dashed #ddd;
        }
        
        .bom-empty i {
            font-size: 64px;
            margin-bottom: 20px;
            color: #ddd;
        }
        
        .bom-empty h4 {
            color: #666;
            margin-bottom: 10px;
        }
        
        .bom-empty p {
            color: #999;
            margin-bottom: 20px;
        }
        
        @media (max-width: 768px) {
            .bom-product-card {
                width: 100%;
                max-width: 100%;
            }
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
                Chi tiết sản phẩm
                <small>${product.productName}</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${pageContext.request.contextPath}/admin/dashboard"><i class="fa fa-dashboard"></i> Trang chủ</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/products">Quản lý sản phẩm</a></li>
                <li class="active">Chi tiết sản phẩm</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                    <i class="icon fa fa-ban"></i> ${error}
                </div>
            </c:if>

            <div class="box">
                <div class="box-header">
                    <h3 class="box-title">Thông tin sản phẩm</h3>
                    <div class="box-tools pull-right">
                        <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-default">
                            <i class="fa fa-arrow-left"></i> Quay lại danh sách
                        </a>
                    </div>
                </div>
                
                <div class="box-body">
                    <!-- Tabs Navigation -->
                    <ul class="nav nav-tabs" role="tablist">
                        <li role="presentation" class="active">
                            <a href="#product-details-tab" aria-controls="product-details-tab" role="tab" data-toggle="tab">
                                <i class="fa fa-info-circle"></i> Chi tiết sản phẩm
                            </a>
                        </li>
                        <li role="presentation">
                            <a href="#recipe-tab" aria-controls="recipe-tab" role="tab" data-toggle="tab">
                                <i class="fa fa-list-alt"></i> Công thức
                            </a>
                        </li>
                    </ul>
                    
                    <!-- Tab Content -->
                    <div class="tab-content">
                        <!-- Tab 1: Chi tiết sản phẩm -->
                        <div role="tabpanel" class="tab-pane active" id="product-details-tab">
                    <div class="product-details">
                        <!-- Hình ảnh ở đầu -->
                        <div class="info-item" style="border-bottom: 2px solid #ddd; padding-bottom: 20px; margin-bottom: 20px;">
                            <div class="info-label">Hình ảnh:</div>
                            <div class="info-value">
                                <c:choose>
                                    <c:when test="${not empty product.imageUrl}">
                                        <div style="margin-top: 10px;">
                                            <img src="${pageContext.request.contextPath}${product.imageUrl}" 
                                                 alt="${product.productName}"
                                                 style="max-width: 200px; max-height: 200px; border: 1px solid #ddd; border-radius: 4px; padding: 5px;"
                                                 onerror="if(!this.dataset.error){this.dataset.error='1';this.style.display='none';this.parentElement.innerHTML='<div style=\'width:150px;height:150px;background:#f0f0f0;display:flex;align-items:center;justify-content:center;border:1px dashed #ccc;border-radius:4px;\'><div style=\'text-align:center;color:#999;\'><i class=\'fa fa-image fa-2x\'></i><p>Không thể tải hình ảnh</p></div></div>';}">
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div style="width: 150px; height: 150px; background: #f0f0f0; display: flex; align-items: center; justify-content: center; border: 1px dashed #ccc; border-radius: 4px;">
                                            <div style="text-align: center; color: #999;">
                                                <i class="fa fa-image fa-2x"></i>
                                                <p>Không có hình ảnh</p>
                                            </div>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        
                        <div class="info-item">
                            <div class="info-label">ID sản phẩm:</div>
                            <div class="info-value">${product.productID}</div>
                        </div>
                        
                        <div class="info-item">
                            <div class="info-label">Tên sản phẩm:</div>
                            <div class="info-value">
                                <strong style="font-size: 18px;">${product.productName}</strong>
                                <c:if test="${not empty product.categoryName}">
                                    <span class="category-name" style="margin-left: 15px;">
                                        <i class="fa fa-tag"></i> ${product.categoryName}
                                    </span>
                                </c:if>
                            </div>
                        </div>
                        
                        <div class="info-item">
                            <div class="info-label">Giá:</div>
                            <div class="info-value">
                                <span class="price-display">
                                    <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="₫" />
                                </span>
                            </div>
                        </div>
                        
                        <div class="info-item">
                            <div class="info-label">Trạng thái:</div>
                            <div class="info-value">
                                <c:choose>
                                    <c:when test="${product.active}">
                                        <span class="label label-success">
                                            <i class="fa fa-check"></i> Đang hoạt động
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="label label-danger">
                                            <i class="fa fa-ban"></i> Không hoạt động
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        
                        <!-- Mô tả ở cuối -->
                        <div class="info-item" style="border-top: 2px solid #ddd; padding-top: 20px; margin-top: 20px; border-bottom: none;">
                            <div class="info-label">Mô tả:</div>
                            <div class="info-value">
                                <c:choose>
                                    <c:when test="${not empty product.description}">
                                        ${product.description}
                                    </c:when>
                                    <c:otherwise>
                                        <em style="color: #999;">Không có mô tả</em>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        

                    </div>
                        </div>
                        
                        <!-- Tab 2: Công thức (BOM) -->
                        <div role="tabpanel" class="tab-pane" id="recipe-tab">
                            <!-- Success message for recipe -->
                            <c:if test="${not empty recipeSuccessMessage}">
                                <div class="alert alert-success alert-dismissible" style="margin-bottom: 20px;">
                                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                                    <i class="icon fa fa-check"></i> ${recipeSuccessMessage}
                                </div>
                            </c:if>
                            
                            <c:choose>
                                <c:when test="${not empty bomItems && bomItems.size() > 0}">
                                    <div style="margin-bottom: 20px; display: flex; justify-content: space-between; align-items: center;">
                                        <div>
                                            <h4 style="margin: 0; color: #333;">
                                                <i class="fa fa-cube"></i> Công thức sản phẩm: ${product.productName}
                                            </h4>
                                            <p style="color: #666; margin-top: 5px; margin-bottom: 0;">Danh sách nguyên liệu và định lượng</p>
                                        </div>
                                        <a href="${pageContext.request.contextPath}/admin/recipe?action=edit&id=${product.productID}" 
                                           class="btn btn-warning">
                                            <i class="fa fa-edit"></i> Chỉnh sửa công thức
                                        </a>
                                    </div>
                                    
                                    <div class="bom-card-container">
                                        <div class="bom-product-card">
                                            <!-- Product Image -->
                                            <c:choose>
                                                <c:when test="${not empty product.imageUrl}">
                                                    <div class="bom-product-image-container">
                                                        <img src="${pageContext.request.contextPath}${product.imageUrl}" 
                                                             alt="${product.productName}" 
                                                             class="bom-product-image"
                                                             onerror="this.onerror=null; var container=this.parentElement; container.className='bom-product-image-placeholder'; container.innerHTML='<i class=\\'fa fa-mug-hot\\'></i>';">
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="bom-product-image-placeholder">
                                                        <i class="fa fa-mug-hot"></i>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                            
                                            <!-- Product Body -->
                                            <div class="bom-product-body">
                                                <div class="bom-product-name">${product.productName}</div>
                                                <div class="bom-product-formula">
                                                    ${formattedRecipe}
                                                </div>
                                                <div class="bom-product-footer">
                                                    <c:if test="${not empty product.categoryName}">
                                                        <span class="badge bg-blue">${product.categoryName}</span>
                                                    </c:if>
                                                    <div style="display: flex; gap: 8px; align-items: center;">
                                                        
                                                        <span class="bom-product-price">
                                                            <fmt:formatNumber value="${product.price}" type="number" maxFractionDigits="0"/> đ
                                                        </span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="bom-empty">
                                        <i class="fa fa-list-alt"></i>
                                        <h4>Chưa có công thức</h4>
                                        <p>Sản phẩm này chưa có công thức (BOM).</p>
                                        <a href="${pageContext.request.contextPath}/admin/recipe?action=add&productId=${product.productID}" 
                                           class="btn btn-primary btn-xs" title="Thêm công thức">
                                            <i class="fa fa-plus" style="font-size: 10px;"></i> Thêm công thức
                                        </a>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>

<!-- jQuery (must be loaded first) -->
<script src="${pageContext.request.contextPath}/plugins/jQuery/jquery-2.2.3.min.js"></script>
<script>
    // Fallback jQuery if local file not found
    if (typeof jQuery === 'undefined') {
        document.write('<script src="https://code.jquery.com/jquery-2.2.4.min.js"><\/script>');
    }
</script>
<!-- Bootstrap (requires jQuery) -->
<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
<!-- AdminLTE App -->
<script src="${pageContext.request.contextPath}/dist/js/app.min.js"></script>

<script>
    // Chờ trang tải xong rồi mới chạy code
    document.addEventListener('DOMContentLoaded', function() {
        
        // Tự động mở tab "recipe-tab" nếu URL có hash #recipe-tab
        // (Khi redirect từ RecipeController sau khi lưu công thức thành công)
        if (window.location.hash === '#recipe-tab') {
            var recipeTabLink = document.querySelector('a[href="#recipe-tab"]');
            if (recipeTabLink) {
                // Click vào tab link để mở tab (Bootstrap sẽ tự động xử lý)
                recipeTabLink.click();
                
                // Cuộn trang đến phần tabs sau 100ms
                setTimeout(function() {
                    var navTabs = document.querySelector('.nav-tabs');
                    if (navTabs) {
                        // Lấy vị trí của nav-tabs trên trang
                        var navTabsTop = navTabs.getBoundingClientRect().top + window.pageYOffset;
                        // Cuộn đến vị trí đó (trừ 20px để có khoảng trống)
                        window.scrollTo({
                            top: navTabsTop - 20,
                            behavior: 'smooth' // Cuộn mượt mà
                        });
                    }
                }, 100);
            }
        }
        
        // Bootstrap đã tự động xử lý click vào tab (nhờ data-toggle="tab")
        // Không cần thêm event listener backup
    });
</script>

</body>
</html>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>${product != null ? 'Chỉnh sửa' : 'Thêm'} Công thức - Admin Dashboard</title>
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
        .ingredient-row {
            margin-bottom: 15px;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 4px;
            background: #f9f9f9;
        }
        .btn-remove {
            margin-top: 25px;
        }
        .ingredient-row select,
        .ingredient-row input {
            background-color: white !important;
            color: #333 !important;
        }
        .ingredient-row .form-control {
            background-color: white !important;
        }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

    <%@include file="../compoment/header.jsp" %>
    <%@include file="../compoment/sidebar.jsp" %>

    <div class="content-wrapper">
        <section class="content-header">
            <h1>
                ${product != null ? 'Chỉnh sửa' : 'Thêm'} Công thức
                <small>Quản lý công thức sản phẩm</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${pageContext.request.contextPath}/admin/dashboard"><i class="fa fa-dashboard"></i> Trang chủ</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/products">Quản lý sản phẩm</a></li>
                <c:if test="${product != null}">
                    <li><a href="${pageContext.request.contextPath}/admin/products/view?id=${product.productID}">${product.productName}</a></li>
                </c:if>
                <li class="active">${product != null ? 'Chỉnh sửa' : 'Thêm'} Công thức</li>
            </ol>
        </section>

        <section class="content">
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-header">
                            <h3 class="box-title">${product != null ? 'Chỉnh sửa' : 'Thêm'} Công thức</h3>
                        </div>
                        
                        <form id="recipe-form" method="post" action="${pageContext.request.contextPath}/admin/recipe">
                            <input type="hidden" name="action" value="save">
                            <div class="box-body">
                                <!-- Success Message -->
                                <c:if test="${not empty successMessage}">
                                    <div class="alert alert-success alert-dismissible">
                                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                        <h4><i class="icon fa fa-check"></i> Thành công!</h4>
                                        ${successMessage}
                                    </div>
                                </c:if>
                                <!-- Error Message -->
                                <c:if test="${not empty sessionScope.recipeErrorMessage}">
                                    <div class="alert alert-danger alert-dismissible">
                                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                        <h4><i class="icon fa fa-ban"></i> Lỗi!</h4>
                                        ${sessionScope.recipeErrorMessage}
                                        <c:remove var="recipeErrorMessage" scope="session"/>
                                    </div>
                                </c:if>
                                
                                <div class="form-group">
                                    <label>Sản phẩm <span class="text-red">*</span></label>
                                    <c:choose>
                                        <c:when test="${product != null}">
                                            <input type="hidden" name="productId" value="${product.productID}">
                                            <input type="text" class="form-control" value="${product.productName}" readonly>
                                        </c:when>
                                        <c:otherwise>
                                            <select name="productId" class="form-control" required>
                                                <option value="">-- Chọn sản phẩm --</option>
                                                <c:forEach var="p" items="${products}">
                                                    <option value="${p.productID}">${p.productName}</option>
                                                </c:forEach>
                                            </select>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <div class="form-group">
                                    <label>Nguyên liệu <span class="text-red">*</span></label>
                                    <div id="ingredients-container">
                                        <c:choose>
                                            <c:when test="${product != null && bomItems != null && bomItems.size() > 0}">
                                                <c:forEach var="item" items="${bomItems}" varStatus="status">
                                                    <div class="ingredient-row">
                                                        <div class="row">
                                                            <div class="col-md-4">
                                                                <label>Nguyên liệu</label>
                                                                <select name="ingredientId" class="form-control ingredient-select" required>
                                                                    <option value="">-- Chọn nguyên liệu --</option>
                                                                    <c:forEach var="ing" items="${ingredients}">
                                                                        <c:if test="${ing != null && ing.ingredientID != null}">
                                                                            <c:choose>
                                                                                <c:when test="${ing.ingredientID == item.ingredientId}">
                                                                                    <option value="${ing.ingredientID}" selected><c:out value="${ing.name}" /></option>
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                    <option value="${ing.ingredientID}"><c:out value="${ing.name}" /></option>
                                                                                </c:otherwise>
                                                                            </c:choose>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                            <div class="col-md-3">
                                                                <label>Số lượng</label>
                                                                <input type="number" name="quantity" class="form-control quantity-input" 
                                                                       value="<fmt:formatNumber value='${item.quantity}' minFractionDigits='0' maxFractionDigits='4'/>" step="0.01" min="0" max="9999" required>
                                                            </div>
                                                            <div class="col-md-3">
                                                                <label>Đơn vị</label>
                                                                <select name="unitId" class="form-control unit-select" required>
                                                                    <option value="">-- Chọn đơn vị --</option>
                                                                    <c:forEach var="unit" items="${units}">
                                                                        <c:choose>
                                                                            <c:when test="${item.unitId != null && unit.settingID == item.unitId}">
                                                                                <option value="${unit.settingID}" selected><c:out value="${unit.value}" /></option>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <option value="${unit.settingID}"><c:out value="${unit.value}" /></option>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                            <div class="col-md-2">
                                                                <label>&nbsp;</label>
                                                                <button type="button" class="btn btn-danger btn-block btn-remove">
                                                                    <i class="fa fa-trash"></i> Xóa
                                                                </button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="ingredient-row">
                                                    <div class="row">
                                                        <div class="col-md-4">
                                                            <label>Nguyên liệu</label>
                                                            <select name="ingredientId" class="form-control ingredient-select" required>
                                                                <option value="">-- Chọn nguyên liệu --</option>
                                                                <c:forEach var="ing" items="${ingredients}">
                                                                    <c:if test="${ing != null && ing.ingredientID != null}">
                                                                        <option value="${ing.ingredientID}"><c:out value="${ing.name}" /></option>
                                                                    </c:if>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                        <div class="col-md-3">
                                                            <label>Số lượng</label>
                                                            <input type="number" name="quantity" class="form-control quantity-input" 
                                                                   step="0.01" min="0" max="9999" required>
                                                        </div>
                                                        <div class="col-md-3">
                                                            <label>Đơn vị</label>
                                                            <select name="unitId" class="form-control unit-select" required>
                                                                <option value="">-- Chọn đơn vị --</option>
                                                                <c:forEach var="unit" items="${units}">
                                                                    <c:if test="${unit != null && unit.settingID != null}">
                                                                        <option value="${unit.settingID}"><c:out value="${unit.value}" /></option>
                                                                    </c:if>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                        <div class="col-md-2">
                                                            <label>&nbsp;</label>
                                                            <button type="button" class="btn btn-danger btn-block btn-remove">
                                                                <i class="fa fa-trash"></i> Xóa
                                                            </button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <button type="button" id="add-ingredient-btn" class="btn btn-success">
                                        <i class="fa fa-plus"></i> Thêm nguyên liệu
                                    </button>
                                </div>
                            </div>
                            
                            <div class="box-footer">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fa fa-save"></i> Lưu công thức
                                </button>
                                <c:choose>
                                    <c:when test="${product != null}">
                                        <!-- Nếu đang chỉnh sửa, quay về product detail với tab công thức -->
                                        <a href="${pageContext.request.contextPath}/admin/products/view?id=${product.productID}#recipe-tab" class="btn btn-default">
                                            <i class="fa fa-times"></i> Hủy
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <!-- Nếu đang thêm mới, quay về danh sách sản phẩm -->
                                        <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-default">
                                            <i class="fa fa-times"></i> Hủy
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    </div>
    
    <%@include file="../compoment/footer.jsp" %>

</div>

<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/dist/js/app.min.js"></script>

<script>
// Store template for ingredients (for dynamic adding rows) - using safer method
var ingredientsDataArray = [];
<c:forEach var="ing" items="${ingredients}">
<c:if test="${ing != null && ing.ingredientID != null}">
ingredientsDataArray.push({id: ${ing.ingredientID}, name: '<c:out value="${ing.name}" escapeXml="true" />'});
</c:if>
</c:forEach>
const ingredientsData = ingredientsDataArray;

var unitsDataArray = [];
<c:forEach var="unit" items="${units}">
<c:if test="${unit != null && unit.settingID != null}">
unitsDataArray.push({id: ${unit.settingID}, value: '<c:out value="${unit.value}" escapeXml="true" />'});
</c:if>
</c:forEach>
const unitsData = unitsDataArray;


function buildIngredientOptions() {
    var options = '<option value="">-- Chọn nguyên liệu --</option>';
    if (ingredientsData && ingredientsData.length > 0) {
        ingredientsData.forEach(function(ing) {
            if (ing && ing.id) {
                var name = String(ing.name || '').replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
                options += '<option value="' + ing.id + '">' + name + '</option>';
            }
        });
    }
    return options;
}

function buildUnitOptions() {
    var options = '<option value="">-- Chọn đơn vị --</option>';
    if (unitsData && unitsData.length > 0) {
        unitsData.forEach(function(unit) {
            if (unit && unit.id) {
                var value = String(unit.value || '').replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
                options += '<option value="' + unit.id + '">' + value + '</option>';
            }
        });
    }
    return options;
}

function addIngredient() {
    var container = document.getElementById('ingredients-container');
    var row = document.createElement('div');
    row.className = 'ingredient-row';
    
    var ingredientOptions = buildIngredientOptions();
    var unitOptions = buildUnitOptions();
    
    row.innerHTML = '<div class="row">' +
        '<div class="col-md-4">' +
            '<label>Nguyên liệu</label>' +
            '<select name="ingredientId" class="form-control ingredient-select" required>' +
                ingredientOptions +
            '</select>' +
        '</div>' +
        '<div class="col-md-3">' +
            '<label>Số lượng</label>' +
            '<input type="number" name="quantity" class="form-control quantity-input" step="0.01" min="0" max="9999" required>' +
        '</div>' +
        '<div class="col-md-3">' +
            '<label>Đơn vị</label>' +
            '<select name="unitId" class="form-control unit-select" required>' +
                unitOptions +
            '</select>' +
        '</div>' +
        '<div class="col-md-2">' +
            '<label>&nbsp;</label>' +
            '<button type="button" class="btn btn-danger btn-block btn-remove">' +
                '<i class="fa fa-trash"></i> Xóa' +
            '</button>' +
        '</div>' +
    '</div>';
    container.appendChild(row);
}

function removeIngredient(btn) {
    var container = document.getElementById('ingredients-container');
    if (container.children.length > 1) {
        btn.closest('.ingredient-row').remove();
    } else {
        alert('Phải có ít nhất một nguyên liệu!');
    }
}

document.addEventListener('DOMContentLoaded', function() {
    var addIngredientBtn = document.getElementById('add-ingredient-btn');
    if (addIngredientBtn) {
        addIngredientBtn.addEventListener('click', function() {
            addIngredient();
        });
    }

    var ingredientsContainer = document.getElementById('ingredients-container');
    if (ingredientsContainer) {
        ingredientsContainer.addEventListener('click', function(event) {
            var removeBtn = event.target.closest('.btn-remove');
            if (removeBtn && ingredientsContainer.contains(removeBtn)) {
                removeIngredient(removeBtn);
            }
        });
    }

});
</script>

</body>
</html>


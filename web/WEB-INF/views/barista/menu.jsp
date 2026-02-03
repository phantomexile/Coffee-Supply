<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Menu pha chế - Coffee Shop Management</title>
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
    <!-- AdminLTE -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/AdminLTE.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/skins/_all-skins.min.css">

    <style>
        body {
            background-color: #ecf0f5;
        }

        .content-wrapper {
            margin-left: 230px;
            padding: 20px;
            min-height: 100vh;
        }

        .menu-filters {
            background: #ffffff;
            border-radius: 4px;
            padding: 15px 20px;
            margin-bottom: 20px;
            box-shadow: 0 1px 2px rgba(0, 0, 0, 0.08);
            border-left: 4px solid #3c8dbc;
        }

        .menu-filters .form-group {
            margin-right: 15px;
            margin-bottom: 10px;
        }

        .menu-filters label {
            font-weight: 600;
            color: #333;
            margin-right: 6px;
        }

        .menu-card {
            background: #ffffff;
            border-radius: 8px;
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            height: 100%;
            display: flex;
            flex-direction: column;
            border: 1px solid #e5e9f2;
            transition: all 0.2s ease;
        }

        .menu-card:hover {
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.12);
            transform: translateY(-4px);
        }

        .menu-card-image {
            background: #f8fafc;
            padding: 20px;
            text-align: center;
            border-bottom: 1px solid #e6eef6;
            min-height: 220px;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .menu-card-image img {
            max-height: 180px;
            max-width: 100%;
            object-fit: contain;
            border-radius: 6px;
            border: 1px solid #edf2f7;
            background: #ffffff;
            padding: 8px;
        }

        .menu-card-image .placeholder {
            width: 100%;
            height: 180px;
            display: flex;
            align-items: center;
            justify-content: center;
            border: 2px dashed #cbd5e1;
            border-radius: 6px;
            color: #94a3b8;
            background: #fff;
            flex-direction: column;
            gap: 8px;
            font-size: 13px;
        }

        .menu-card-body {
            padding: 18px 20px;
            display: flex;
            flex-direction: column;
            flex: 1;
        }

        .menu-card-header {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            margin-bottom: 14px;
            gap: 10px;
        }

        .menu-card-title {
            font-size: 18px;
            font-weight: 600;
            color: #1f2937;
            margin: 0;
        }

        .menu-price {
            font-size: 17px;
            font-weight: 700;
            color: #2563eb;
        }

        .menu-category {
            display: inline-block;
            background: #2563eb;
            color: #fff;
            padding: 4px 10px;
            border-radius: 999px;
            font-size: 11px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            margin-bottom: 10px;
        }

        .menu-description {
            color: #4b5563;
            font-size: 13px;
            min-height: 45px;
            margin-bottom: 14px;
        }

        .recipe-table {
            width: 100%;
            border-collapse: collapse;
            font-size: 13px;
            background: #f8fafc;
            border: 1px solid #e2e8f0;
        }

        .recipe-table thead tr {
            background: #e2e8f0;
            color: #1e293b;
        }

        .recipe-table th,
        .recipe-table td {
            padding: 8px 10px;
            border-bottom: 1px solid #e2e8f0;
        }

        .recipe-table th {
            font-weight: 600;
            text-transform: uppercase;
            font-size: 11px;
            letter-spacing: 0.4px;
        }

        .recipe-table tbody tr:last-child td {
            border-bottom: none;
        }

        .recipe-empty {
            text-align: center;
            color: #94a3b8;
            padding: 16px 10px;
            font-style: italic;
        }

        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #95a5a6;
            background: #fff;
            border-radius: 6px;
            border: 2px dashed #dfe6e9;
        }

        .empty-state i {
            font-size: 48px;
            margin-bottom: 16px;
            color: #bdc3c7;
        }

        .pagination {
            margin: 0;
        }

        .pagination > li > a,
        .pagination > li > span {
            color: #3c8dbc;
        }

        .pagination > .active > a,
        .pagination > .active > span,
        .pagination > .active > a:focus,
        .pagination > .active > a:hover {
            background-color: #3c8dbc;
            border-color: #3c8dbc;
        }

        @media (max-width: 767px) {
            .content-wrapper {
                margin-left: 0;
            }

            .menu-filters {
                padding: 15px;
            }
        }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

    <!-- Header -->
    <jsp:include page="../compoment/header.jsp" />

    <!-- Sidebar -->
    <jsp:include page="../compoment/sidebar.jsp" />

    <div class="content-wrapper">
        <!-- Content Header -->
        <section class="content-header">
            <h1>
                Menu pha chế
                <small>Danh sách sản phẩm có công thức</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${pageContext.request.contextPath}/barista/dashboard"><i class="fa fa-dashboard"></i> Trang chủ</a></li>
                <li class="active">Menu pha chế</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">
            <!-- Flash messages -->
            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                    <i class="fa fa-check-circle"></i> ${sessionScope.successMessage}
                </div>
                <c:remove var="successMessage" scope="session" />
            </c:if>

            <c:if test="${not empty sessionScope.errorMessage}">
                <div class="alert alert-danger alert-dismissible">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                    <i class="fa fa-exclamation-circle"></i> ${sessionScope.errorMessage}
                </div>
                <c:remove var="errorMessage" scope="session" />
            </c:if>

            <!-- Filters -->
            <div class="menu-filters">
                <form method="get" action="${pageContext.request.contextPath}/barista/menu" class="form-inline">
                    <div class="form-group">
                        <label for="search"><i class="fa fa-search"></i> Tìm kiếm:</label>
                        <input type="text"
                               id="search"
                               name="search"
                               class="form-control"
                               placeholder="Tên sản phẩm hoặc mô tả..."
                               value="${fn:escapeXml(searchTerm)}"
                               style="width: 220px;">
                    </div>

                    <div class="form-group">
                        <label for="category"><i class="fa fa-tag"></i> Danh mục:</label>
                        <select id="category" name="category" class="form-control" style="width: 200px;">
                            <option value="">-- Tất cả --</option>
                            <c:forEach var="category" items="${categories}">
                                <option value="${category.settingID}"
                                        <c:if test="${categoryFilter != null && categoryFilter == category.settingID}">selected</c:if>>
                                    <c:out value="${category.value}" />
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <button type="submit" class="btn btn-primary">
                        <i class="fa fa-filter"></i> Lọc
                    </button>

                    <a href="${pageContext.request.contextPath}/barista/menu" class="btn btn-default">
                        <i class="fa fa-refresh"></i> Đặt lại
                    </a>
                </form>
            </div>

            <c:choose>
                <c:when test="${empty products}">
                    <div class="empty-state">
                        <i class="fa fa-coffee"></i>
                        <h3>Chưa có công thức nào được tạo</h3>
                        <p>Liên hệ quản trị viên để thêm công thức cho sản phẩm.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="row">
                        <c:forEach var="product" items="${products}">
                            <div class="col-md-4 col-sm-6">
                                <div class="menu-card">
                                    <div class="menu-card-image">
                                        <c:choose>
                                            <c:when test="${not empty product.imageUrl}">
                                                <img src="${pageContext.request.contextPath}${product.imageUrl}"
                                                     alt="${product.productName}"
                                                     onerror="this.onerror=null;this.style.display='none';this.parentNode.innerHTML='<div class=&quot;placeholder&quot;><i class=&quot;fa fa-image&quot;></i><p>Không có hình ảnh</p></div>';">
                                            </c:when>
                                            <c:otherwise>
                                                <div class="placeholder">
                                                    <div>
                                                        <i class="fa fa-image fa-2x"></i>
                                                        <p>Không có hình ảnh</p>
                                                    </div>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="menu-card-body">
                                        <c:if test="${not empty product.categoryName}">
                                            <span class="menu-category">
                                                <c:out value="${product.categoryName}" />
                                            </span>
                                        </c:if>

                                        <div class="menu-card-header">
                                            <h3 class="menu-card-title">
                                                <c:out value="${product.productName}" />
                                            </h3>
                                            <span class="menu-price">
                                                <fmt:formatNumber value="${product.price}" type="number" maxFractionDigits="0" /> ₫
                                            </span>
                                        </div>

                                        <p class="menu-description">
                                            <c:choose>
                                                <c:when test="${not empty product.description}">
                                                    <c:out value="${product.description}" />
                                                </c:when>
                                                <c:otherwise>
                                                    <em>Chưa có mô tả cho sản phẩm này.</em>
                                                </c:otherwise>
                                            </c:choose>
                                        </p>

                                        <c:set var="bomItems" value="${productBOMMap[product.productID]}" />
                                        <table class="recipe-table">
                                            <thead>
                                                <tr>
                                                    <th>Nguyên liệu</th>
                                                    <th class="text-right">Định lượng</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${not empty bomItems}">
                                                        <c:forEach var="item" items="${bomItems}">
                                                            <tr>
                                                                <td><c:out value="${item.ingredientName}" /></td>
                                                                <td class="text-right">
                                                                    <fmt:formatNumber value="${item.quantity}" minFractionDigits="0" maxFractionDigits="3" />
                                                                    <c:if test="${not empty item.unitName}">
                                                                        <span><c:out value="${item.unitName}" /></span>
                                                                    </c:if>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <tr>
                                                            <td colspan="2" class="recipe-empty">Chưa có công thức pha chế.</td>
                                                        </tr>
                                                    </c:otherwise>
                                                </c:choose>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <!-- Pagination -->
                    <c:if test="${totalPages > 1}">
                        <div class="text-center" style="margin-top: 20px;">
                            <ul class="pagination">
                                <c:if test="${currentPage > 1}">
                                    <c:url var="prevUrl" value="/barista/menu">
                                        <c:param name="page" value="${currentPage - 1}" />
                                        <c:if test="${not empty searchTerm}">
                                            <c:param name="search" value="${searchTerm}" />
                                        </c:if>
                                        <c:if test="${categoryFilter != null}">
                                            <c:param name="category" value="${categoryFilter}" />
                                        </c:if>
                                    </c:url>
                                    <li>
                                        <a href="${prevUrl}">
                                            <i class="fa fa-angle-left"></i>
                                        </a>
                                    </li>
                                </c:if>

                                <c:forEach begin="1" end="${totalPages}" var="pageNumber">
                                    <c:url var="pageUrl" value="/barista/menu">
                                        <c:param name="page" value="${pageNumber}" />
                                        <c:if test="${not empty searchTerm}">
                                            <c:param name="search" value="${searchTerm}" />
                                        </c:if>
                                        <c:if test="${categoryFilter != null}">
                                            <c:param name="category" value="${categoryFilter}" />
                                        </c:if>
                                    </c:url>
                                    <li class="<c:if test='${pageNumber == currentPage}'>active</c:if>">
                                        <a href="${pageUrl}">${pageNumber}</a>
                                    </li>
                                </c:forEach>

                                <c:if test="${currentPage < totalPages}">
                                    <c:url var="nextUrl" value="/barista/menu">
                                        <c:param name="page" value="${currentPage + 1}" />
                                        <c:if test="${not empty searchTerm}">
                                            <c:param name="search" value="${searchTerm}" />
                                        </c:if>
                                        <c:if test="${categoryFilter != null}">
                                            <c:param name="category" value="${categoryFilter}" />
                                        </c:if>
                                    </c:url>
                                    <li>
                                        <a href="${nextUrl}">
                                            <i class="fa fa-angle-right"></i>
                                        </a>
                                    </li>
                                </c:if>
                            </ul>
                            <p style="margin-top: 10px; color: #666;">
                                Hiển thị ${(currentPage - 1) * pageSize + 1}
                                -
                                <c:choose>
                                    <c:when test="${currentPage * pageSize > totalCount}">
                                        ${totalCount}
                                    </c:when>
                                    <c:otherwise>
                                        ${currentPage * pageSize}
                                    </c:otherwise>
                                </c:choose>
                                trong tổng số ${totalCount} sản phẩm
                            </p>
                        </div>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </section>
    </div>

    <!-- Footer -->
    <jsp:include page="../compoment/footer.jsp" />
</div>

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-2.2.3.min.js"></script>
<!-- Bootstrap JS -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<!-- AdminLTE -->
<script src="${pageContext.request.contextPath}/dist/js/app.min.js"></script>
</body>
</html>


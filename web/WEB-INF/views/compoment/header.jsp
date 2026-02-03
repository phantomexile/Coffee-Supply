<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page pageEncoding="UTF-8" %>

<header class="main-header">
    <!-- Logo -->
    <a href="${pageContext.request.contextPath}/" class="logo">
        <!-- mini logo for sidebar mini 50x50 pixels -->
        <span class="logo-mini"><b>C</b>S</span>
        <!-- logo for regular state and mobile devices -->
        <span class="logo-lg"><b>Coffee</b>Shop</span>
    </a>
    <!-- Header Navbar: style can be found in header.less -->
    <nav class="navbar navbar-static-top">
        <!-- Sidebar toggle button-->
        <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
            <span class="sr-only">Toggle navigation</span>
        </a>

        <div class="navbar-custom-menu">
            <ul class="nav navbar-nav">
                <!-- User Account: style can be found in dropdown.less -->
                <li class="dropdown user user-menu">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <c:choose>
                            <c:when test="${not empty sessionScope.user.avatarUrl}">
                                <img src="${pageContext.request.contextPath}${sessionScope.user.avatarUrl}" class="user-image" alt="User Image">
                            </c:when>
                            <c:otherwise>
                                <img src="${pageContext.request.contextPath}/dist/img/user2-160x160.jpg" class="user-image" alt="User Image">
                            </c:otherwise>
                        </c:choose>
                        <span class="hidden-xs">${sessionScope.user.fullName != null ? sessionScope.user.fullName : 'User'}</span>
                    </a>
                    <ul class="dropdown-menu">
                        <!-- User image -->
                        <li class="user-header">
                            <c:choose>
                                <c:when test="${not empty sessionScope.user.avatarUrl}">
                                    <img src="${pageContext.request.contextPath}${sessionScope.user.avatarUrl}" class="img-circle" alt="User Image">
                                </c:when>
                                <c:otherwise>
                                    <img src="${pageContext.request.contextPath}/dist/img/user2-160x160.jpg" class="img-circle" alt="User Image">
                                </c:otherwise>
                            </c:choose>
                            <p>
                                ${sessionScope.user.fullName != null ? sessionScope.user.fullName : 'User'} - ${sessionScope.roleName != null ? sessionScope.roleName : 'Staff'}
                                <small>Thành viên từ ${sessionScope.user.createdAt != null ? sessionScope.user.createdAt : 'N/A'}</small>
                            </p>
                        </li>
                        <!-- No extra buttons in body; keep actions in footer only -->
                        <!-- Menu Footer-->
                        <li class="user-footer">
                            <div class="pull-left">
                                <a href="${pageContext.request.contextPath}/profile" class="btn btn-default btn-flat">Hồ sơ</a>
                            </div>
                            <div class="pull-right">
                                <a href="${pageContext.request.contextPath}/logout" class="btn btn-default btn-flat">Đăng xuất</a>
                            </div>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </nav>
</header>

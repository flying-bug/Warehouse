<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8"/>
    <title>Warehouse Clothing - Dashboard</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Warehouse Clothing Management Dashboard with Doctris Theme"/>
    <meta name="author" content="Anh Bo & Shreethemes"/>

    <link rel="shortcut icon" href="<%= request.getContextPath() %>/assets/images/favicon.ico.png">
    <link href="<%= request.getContextPath() %>/assets/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="<%= request.getContextPath() %>/assets/css/simplebar.css" rel="stylesheet" type="text/css"/>
    <link href="<%= request.getContextPath() %>/assets/css/materialdesignicons.min.css" rel="stylesheet" type="text/css"/>
    <link href="<%= request.getContextPath() %>/assets/css/remixicon.css" rel="stylesheet" type="text/css"/>
    <link href="https://unicons.iconscout.com/release/v3.0.6/css/line.css" rel="stylesheet">
    <link href="<%= request.getContextPath() %>/assets/css/style.min.css" rel="stylesheet" type="text/css" id="theme-opt"/>
    <style>
        .sidebar-brand .logo-text-custom span {
            font-size: 1rem;
            font-weight: 600;
            color: #3c4858;
        }
        .sidebar-brand .logo-text-custom .highlight {
            color: #4f46e5;
        }
        .dark-mode .sidebar-brand .logo-text-custom span {
            color: #e5e7eb;
        }
        .dark-mode .sidebar-brand .logo-text-custom .highlight {
            color: #818cf8;
        }
        .top-header .dropdown-menu .dropdown-item small.text-muted {
            white-space: normal;
        }
        /* Đảm bảo nội dung chính không bị header che mất */
        .layout-specing {
            padding-top: 70px; /* Anh có thể cần điều chỉnh giá trị này cho khớp với chiều cao của top-header */
        }
    </style>
</head>

<body>


<div class="page-wrapper doctris-theme toggled">
    <nav id="sidebar" class="sidebar-wrapper">
        <div class="sidebar-content" data-simplebar style="height: calc(100% - 60px);">
            <div class="sidebar-brand">
                <a href="<%= request.getContextPath() %>/dashboardAdmin">
                    <img src="<%= request.getContextPath() %>/images/logo.png" height="50" class="logo-light-mode" alt="WC">
                    <span class="logo-text-custom ms-2" style="color: orangered">Warehouse <span class="highlight" style="color: #40c18b">Clothing</span></span>
                </a>
            </div>

            <ul class="sidebar-menu pt-3">
                <li>
                    <a href="<%= request.getContextPath() %>/#">
                        <i class="uil uil-dashboard me-2 d-inline-block"></i> Dashboard
                    </a>
                </li>
                <li class="sidebar-dropdown">
                    <a href="javascript:void(0)">
                        <i class="uil uil-users-alt me-2 d-inline-block"></i> Accounts
                    </a>
                    <div class="sidebar-submenu">
                        <ul>
                            <li><a href="<%= request.getContextPath() %>/viewListAccounts"><i class="uil uil-list-ul me-1"></i> List of Accounts</a></li>
                            <li><a href="<%= request.getContextPath() %>/addAccount"><i class="uil uil-user-plus me-1"></i> Add Account</a></li>
                        </ul>
                    </div>
                </li>
                <li class="sidebar-dropdown">
                    <a href="javascript:void(0)">
                        <i class="uil uil-cube me-2 d-inline-block"></i> Products
                    </a>
                    <div class="sidebar-submenu">
                        <ul>
                            <li><a href="<%= request.getContextPath() %>/viewListProducts"><i class="uil uil-list-ul me-1"></i> List of Products</a></li>
                            <li><a href="<%= request.getContextPath() %>/addProduct"><i class="uil uil-plus-circle me-1"></i> Add Product</a></li>
                        </ul>
                    </div>
                </li>
                <li class="sidebar-dropdown">
                    <a href="javascript:void(0)">
                        <i class="uil uil-truck-loading me-2 d-inline-block"></i> Suppliers
                    </a>
                    <div class="sidebar-submenu">
                        <ul>
                            <li><a href="<%= request.getContextPath() %>/viewListSuppliers"><i class="uil uil-list-ul me-1"></i> List of Suppliers</a></li>
                            <li><a href="<%= request.getContextPath() %>/addSupplier"><i class="uil uil-plus-circle me-1"></i> Add Supplier</a></li>
                        </ul>
                    </div>
                </li>
                <li class="sidebar-dropdown">
                    <a href="javascript:void(0)">
                        <i class="uil uil-store me-2 d-inline-block"></i> Warehouses
                    </a>
                    <div class="sidebar-submenu">
                        <ul>
                            <li><a href="<%= request.getContextPath() %>/viewListWarehouses"><i class="uil uil-list-ul me-1"></i> List of Warehouses</a></li>
                            <li><a href="<%= request.getContextPath() %>/addWarehouse"><i class="uil uil-plus-circle me-1"></i> Add Warehouse</a></li>
                        </ul>
                    </div>
                </li>
                <li class="sidebar-dropdown">
                    <a href="javascript:void(0)">
                        <i class="uil uil-clipboard-alt me-2 d-inline-block"></i> Inventory
                    </a>
                    <div class="sidebar-submenu">
                        <ul>
                            <li><a href="<%= request.getContextPath() %>/viewListInventory"><i class="uil uil-list-ul me-1"></i> List of Inventory</a></li>
                            <li><a href="<%= request.getContextPath() %>/addInventory"><i class="uil uil-plus-circle me-1"></i> Add Inventory</a></li>
                        </ul>
                    </div>
                </li>

                <li class="sidebar-dropdown">
                    <a href="javascript:void(0)">
                        <i class="uil uil-clipboard-alt me-2 d-inline-block"></i> Inventory
                    </a>
                    <div class="sidebar-submenu">
                        <ul>
                            <li><a href="<%= request.getContextPath() %>/viewListInventory"><i class="uil uil-list-ul me-1"></i> List of Inventory</a></li>
                            <li><a href="<%= request.getContextPath() %>/addInventory"><i class="uil uil-plus-circle me-1"></i> Add Inventory</a></li>
                        </ul>
                    </div>
                </li>

                <li class="sidebar-dropdown">
                    <a href="javascript:void(0)">
                        <i class="uil uil-clipboard-alt me-2 d-inline-block"></i> Inventory
                    </a>
                    <div class="sidebar-submenu">
                        <ul>
                            <li><a href="<%= request.getContextPath() %>/viewListInventory"><i class="uil uil-list-ul me-1"></i> List of Inventory</a></li>
                            <li><a href="<%= request.getContextPath() %>/addInventory"><i class="uil uil-plus-circle me-1"></i> Add Inventory</a></li>
                        </ul>
                    </div>
                </li>
            </ul>
        </div>

        <ul class="sidebar-footer list-unstyled mb-0">
            <li class="list-inline-item mb-0 ms-1">
                <a href="#" class="btn btn-icon btn-pills btn-soft-primary">
                    <i class="uil uil-comment icons"></i>
                </a>
            </li>
        </ul>
    </nav>

    <main class="page-content bg-light">
        <div class="top-header">
            <div class="header-bar d-flex justify-content-between border-bottom">
                <div class="d-flex align-items-center">
                    <a href="#" class="logo-icon d-md-none">
                        <img src="<%= request.getContextPath() %>/assets/images/logo-icon.png" height="30" alt="WC">
                    </a>
                    <a id="close-sidebar" class="btn btn-icon btn-pills btn-soft-primary ms-2" href="#">
                        <i class="uil uil-bars"></i>
                    </a>
                    <div class="search-bar p-0 d-none d-md-block ms-2">
                        <div id="search" class="menu-search mb-0">
                            <form role="search" method="get" id="searchform" class="searchform">
                                <div>
                                    <input type="text" class="form-control border rounded-pill" name="s" id="s" placeholder="Search Keywords...">
                                    <input type="submit" id="searchsubmit" value="Search">
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <ul class="list-unstyled mb-0">
                    <li class="list-inline-item mb-0 ms-1">
                        <div class="dropdown dropdown-primary">
                            <c:choose>
                                <c:when test="${not empty account.profile_image}">
                                    <button type="button"
                                            class="btn btn-icon btn-pills btn-soft-primary dropdown-toggle p-0"
                                            data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                        <img src="${pageContext.request.contextPath}/${account.profile_image}"
                                             alt="Avatar"
                                             class="rounded-circle"
                                             style="width: 40px; height: 40px; object-fit: cover;">
                                    </button>
                                </c:when>
                                <c:otherwise>
                                    <button type="button"
                                            class="btn btn-icon btn-pills btn-soft-primary dropdown-toggle p-0"
                                            data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                        <i class="uil uil-user-circle" style="font-size: 1.8rem; vertical-align: middle;"></i>
                                    </button>
                                </c:otherwise>
                            </c:choose>

                            <div class="dropdown-menu dd-menu dropdown-menu-end bg-white shadow border-0 mt-3 py-3" style="min-width: 260px;">
                                <a class="dropdown-item d-flex align-items-center text-dark" href="#">
                                    <c:choose>
                                        <c:when test="${not empty account.profile_image}">
                                            <img src="${pageContext.request.contextPath}/${account.profile_image}"
                                                 alt="Profile Image"
                                                 class="rounded-circle shadow"
                                                 style="width: 45px; height: 45px; object-fit: cover;">
                                        </c:when>
                                        <c:otherwise>
                                            <i class="uil uil-user-circle align-middle me-2" style="font-size: 2rem;"></i>
                                        </c:otherwise>
                                    </c:choose>
                                    <div class="flex-1 ms-3">
                                        <span class="d-block mb-1">${account.full_name}</span>
                                        <small class="text-muted">
                                            Role:
                                            <c:choose>
                                                <c:when test="${account.role_id == 1}">Admin</c:when>
                                                <c:when test="${account.role_id == 2}">Store Manager</c:when>
                                                <c:when test="${account.role_id == 3}">Warehouse Staff</c:when>
                                                <c:when test="${account.role_id == 4}">Purchasing Staff</c:when>
                                                <c:when test="${account.role_id == 5}">Sales Staff</c:when>
                                                <c:otherwise>Unknown</c:otherwise>
                                            </c:choose>
                                        </small>
                                    </div>
                                </a>

                                <a class="dropdown-item text-dark" href="#"><span class="mb-0 d-inline-block me-1"><i class="uil uil-setting align-middle h6"></i></span> Profile Settings</a>
                                <div class="dropdown-divider border-top"></div>
                                <a class="dropdown-item text-dark" href="<%= request.getContextPath() %>/logout">
                                    <span class="mb-0 d-inline-block me-1"><i class="uil uil-sign-out-alt align-middle h6"></i></span> Logout
                                </a>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>

        <div class="container-fluid">
            <div class="layout-specing">

                <c:if test="${not empty PAGE_CONTENT}">
                    <jsp:include page="${PAGE_CONTENT}" />
                </c:if>

                <c:if test="${empty PAGE_CONTENT}">
                    <h5 class="mb-0">Warehouse Clothing Main Content</h5>
                    <p>Select an option from the sidebar.</p>
                </c:if>

            </div>
        </div>

        <footer class="footer py-3 bg-white shadow">
            <div class="container-fluid">
                <div class="row align-items-center">
                    <div class="col">
                        <div class="text-sm-start text-center">
                            <p class="mb-0 text-muted">
                                <script>document.write(new Date().getFullYear())</script> © Warehouse Clothing. Design by SWP Pro!
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </footer>
    </main>
</div>

<script src="<%= request.getContextPath() %>/assets/js/bootstrap.bundle.min.js"></script>
<script src="<%= request.getContextPath() %>/assets/js/simplebar.min.js"></script>
<script src="<%= request.getContextPath() %>/assets/js/feather.min.js"></script>
<script src="<%= request.getContextPath() %>/assets/js/app.js"></script>

</body>
</html>
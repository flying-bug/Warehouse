<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard</title>
    <link rel="stylesheet" href="../css/dashboard.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body class="dashboard-sidebar">
<header class="dashboard-header">
    <div class="header-content">
        <div class="header-left">
            <h3>
                Role:
                <span>
                    <c:choose>
                        <c:when test="${account.account_id == 1}">
                            Admin
                        </c:when>
                        <c:when test="${account.account_id == 2}">
                            Store Manager
                        </c:when>
                        <c:when test="${account.account_id == 3}">
                            Warehouse Staff
                        </c:when>
                        <c:when test="${account.account_id == 4}">
                            Purchasing Staff
                        </c:when>
                        <c:when test="${account.account_id == 5}">
                            Sales Staff
                        </c:when>
                        <c:otherwise>
                            Hacker!!!
                        </c:otherwise>
                    </c:choose>
                </span>
            </h3>
        </div>

        <div class="header-right">
            <div class="user-info">
                <i class="fas fa-user-circle"></i>
                <span>${account.full_name}</span>
            </div>
            <div class="user-info">
                <a href="<%= request.getContextPath() %>/logout" class="btn-icon" title="Logout" aria-label="Logout">
                    <i class="fas fa-sign-out-alt"></i> Log Out
                </a>
            </div>
        </div>

    </div>
</header>

<nav id="sidebar" class="sidebar-wrapper">
    <div class="sidebar-content">
        <div class="sidebar-brand">
            <a href="<%= request.getContextPath() %>/dashboardAdmin" aria-label="Warehouse Clothing Dashboard">
                <div class="logo">
                    <span>Warehouse <span class="highlight">Clothing</span></span>
                </div>
            </a>
        </div>

        <ul class="sidebar-menu">
            <li>
                <a href="<%= request.getContextPath() %>/#" aria-label="Dashboard">
                    <i class="fas fa-tachometer-alt"></i> Dashboard
                </a>
            </li>

            <li class="sidebar-dropdown" data-dropdown="accounts">
                <a href="#" aria-expanded="false" aria-controls="accounts-submenu">
                    <i class="fas fa-user-cog"></i> Accounts
                </a>
                <div class="sidebar-submenu" id="accounts-submenu">
                    <ul>
                        <li>
                            <a href="<%= request.getContextPath() %>/viewListAccounts">
                                <i class="fas fa-users"></i> List of Accounts
                            </a>
                        </li>
                        <li>
                            <a href="<%= request.getContextPath() %>/addAccount">
                                <i class="fas fa-user-plus"></i> Add Account
                            </a>
                        </li>
                    </ul>
                </div>
            </li>

            <li class="sidebar-dropdown" data-dropdown="products">
                <a href="#" aria-expanded="false" aria-controls="products-submenu">
                    <i class="fas fa-box-open"></i> Products
                </a>
                <div class="sidebar-submenu" id="products-submenu">
                    <ul>
                        <li>
                            <a href="<%= request.getContextPath() %>/viewListProducts">
                                <i class="fas fa-list"></i> List of Products
                            </a>
                        </li>
                        <li>
                            <a href="<%= request.getContextPath() %>/addProduct">
                                <i class="fas fa-plus-circle"></i> Add Product
                            </a>
                        </li>
                    </ul>
                </div>
            </li>

            <li class="sidebar-dropdown" data-dropdown="suppliers">
                <a href="#" aria-expanded="false" aria-controls="suppliers-submenu">
                    <i class="fas fa-truck"></i> Suppliers
                </a>
                <div class="sidebar-submenu" id="suppliers-submenu">
                    <ul>
                        <li>
                            <a href="<%= request.getContextPath() %>/viewListSuppliers">
                                <i class="fas fa-list"></i> List of Suppliers
                            </a>
                        </li>
                        <li>
                            <a href="<%= request.getContextPath() %>/views/suppliers/addSupplier.jsp">
                                <i class="fas fa-plus-circle"></i> Add Supplier
                            </a>
                        </li>
                    </ul>
                </div>
            </li>

            <li class="sidebar-dropdown" data-dropdown="warehouses">
                <a href="#" aria-expanded="false" aria-controls="warehouses-submenu">
                    <i class="fas fa-warehouse"></i> Warehouses
                </a>
                <div class="sidebar-submenu" id="warehouses-submenu">
                    <ul>
                        <li>
                            <a href="<%= request.getContextPath() %>/viewListWarehouses">
                                <i class="fas fa-list"></i> List of Warehouses
                            </a>
                        </li>
                        <li>
                            <a href="<%= request.getContextPath() %>/views/warehouses/addWarehouse.jsp">
                                <i class="fas fa-plus-circle"></i> Add Warehouse
                            </a>
                        </li>
                    </ul>
                </div>
            </li>

            <li class="sidebar-dropdown" data-dropdown="inventory">
                <a href="#" aria-expanded="false" aria-controls="inventory-submenu">
                    <i class="fas fa-clipboard-list"></i> Inventory
                </a>
                <div class="sidebar-submenu" id="inventory-submenu">
                    <ul>
                        <li>
                            <a href="<%= request.getContextPath() %>/viewListInventory">
                                <i class="fas fa-list"></i> List of Inventory
                            </a>
                        </li>
                        <li>
                            <a href="<%= request.getContextPath() %>/addInventory">
                                <i class="fas fa-plus-circle"></i> Add Inventory
                            </a>
                        </li>
                    </ul>
                </div>
            </li>
        </ul>
    </div>

</nav>

<script>
    // Function to close all dropdowns except the specified one
    function closeAllDropdowns(exceptDropdown) {
        document.querySelectorAll('.sidebar-dropdown').forEach(dropdown => {
            if (dropdown !== exceptDropdown) {
                dropdown.classList.remove('active');
                const toggleLink = dropdown.querySelector('a');
                toggleLink.setAttribute('aria-expanded', 'false');
            }
        });
    }

    // Function to toggle dropdown and save state
    function toggleDropdown(dropdown) {
        const isActive = dropdown.classList.contains('active');
        const willBeActive = !isActive; // Toggle the current dropdown
        dropdown.classList.toggle('active', willBeActive);
        const toggleLink = dropdown.querySelector('a');
        toggleLink.setAttribute('aria-expanded', willBeActive);

        // Save the state to localStorage
        const dropdownId = dropdown.getAttribute('data-dropdown');
        if (willBeActive) {
            localStorage.setItem('activeDropdown', dropdownId);
        } else {
            localStorage.removeItem('activeDropdown');
        }
    }

    // On page load, restore the active dropdown state
    document.addEventListener('DOMContentLoaded', () => {
        const activeDropdownId = localStorage.getItem('activeDropdown');
        if (activeDropdownId) {
            const activeDropdown = document.querySelector(`.sidebar-dropdown[data-dropdown="${activeDropdownId}"]`);
            if (activeDropdown) {
                activeDropdown.classList.add('active');
                const toggleLink = activeDropdown.querySelector('a');
                toggleLink.setAttribute('aria-expanded', 'true');
            }
        }
    });

    // Toggle dropdown menu on click
    document.querySelectorAll('.sidebar-dropdown > a').forEach(item => {
        item.addEventListener('click', function (e) {
            e.preventDefault();
            const parent = this.parentElement;
            toggleDropdown(parent);
        });
    });

    // Prevent submenu links from closing the dropdown
    document.querySelectorAll('.sidebar-submenu a').forEach(link => {
        link.addEventListener('click', function (e) {
            e.stopPropagation(); // Prevent the click from bubbling up
        });
    });
</script>
</body>
</html>
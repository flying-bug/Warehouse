<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Inventory, model.Products, model.Warehouses" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Inventory Management</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/dashboard.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/viewInventory.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">


</head>
<body>

<jsp:include page="../dashboard.jsp"/>

<div class="main-content">
    <h1>Inventory Management</h1>

    <!-- Form lọc -->
    <form method="get" action="filterInventory" class="filter-form">
        <input type="text" name="productCode" placeholder="Product Code" value="${param.productCode}"/>
        <input type="text" name="productName" placeholder="Product Name" value="${param.productName}"/>
        <select name="warehouseId">
            <option value="">All Warehouses</option>
            <c:forEach var="wh" items="${warehouseList}">
                <option value="${wh.warehouseId}" ${param.warehouseId == wh.warehouseId ? 'selected' : ''}>${wh.name}</option>
            </c:forEach>
        </select>
        <button type="submit">Filter</button>
    </form>

    <!-- Bảng tồn kho -->
    <table class="inventory-table">
        <thead>
        <tr>
            <th>Product Code</th>
            <th>Product Name</th>
            <th>Quantity</th>
            <th>Unit</th>
            <th>Warehouse Name</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${not empty inventoryList}">
                <c:forEach var="inv" items="${inventoryList}">
                    <tr>

                        <c:forEach var="p" items="${productList}">
                            <c:if test="${p.productId == inv.productId}">
                                <td>${p.productCode}</td>
                                <td>${p.name}</td>
                                <td>${inv.quantity}</td>
                                <td>${p.unit}</td>
                            </c:if>
                        </c:forEach>


                        <c:forEach var="w" items="${warehouseList}">
                            <c:if test="${w.warehouseId == inv.warehouseId}">
                                <td>${w.name}</td>
                            </c:if>
                        </c:forEach>

                        <td>
                            <a href="editInventory?id=${inv.productId}&warehouse=${inv.warehouseId}" class="btn edit">
                                <i class="fas fa-edit"></i>
                            </a>
                        </td>
                    </tr>

                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="5">No inventory records found.</td>
                </tr>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>

    <!-- Phân trang -->
    <div class="pagination">
        <ul>
            <c:if test="${currentPage > 1}">
                <li><a href="?page=${currentPage - 1}">&laquo; Prev</a></li>
            </c:if>
            <c:forEach var="i" begin="1" end="${totalPages}">
                <li>
                    <a href="?page=${i}" class="${currentPage == i ? 'active' : ''}">${i}</a>
                </li>
            </c:forEach>
            <c:if test="${currentPage < totalPages}">
                <li><a href="?page=${currentPage + 1}">Next &raquo;</a></li>
            </c:if>
        </ul>
    </div>
</div>

</body>
</html>

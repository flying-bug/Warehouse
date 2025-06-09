<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">


<!-- Tiêu đề và Filter -->
<div class="d-md-flex justify-content-between align-items-center mb-3">
    <h5 class="mb-0">Inventory List</h5>
</div>

<form method="get" action="filterInventory" class="row gy-2 gx-3 align-items-center mb-4">
    <div class="col-auto">
        <input type="text" class="form-control" name="productCode" placeholder="Product Code" value="${param.productCode}">
    </div>
    <div class="col-auto">
        <input type="text" class="form-control" name="productName" placeholder="Product Name" value="${param.productName}">
    </div>
    <div class="col-auto">
        <select name="warehouseId" class="form-select">
            <option value="">All Warehouses</option>
            <c:forEach var="wh" items="${warehouseList}">
                <option value="${wh.warehouseId}" ${param.warehouseId == wh.warehouseId ? 'selected' : ''}>
                        ${wh.name}
                </option>
            </c:forEach>
        </select>
    </div>
    <div class="col-auto">
        <button type="submit" class="btn btn-primary"><i class="uil uil-filter"></i> Filter</button>
    </div>
</form>

<!-- Inventory Table -->
<div class="table-responsive shadow rounded">
    <table class="table table-center bg-white mb-0">
        <thead>
        <tr>
            <th class="border-bottom p-3">#</th>
            <th class="border-bottom p-3">Product Code</th>
            <th class="border-bottom p-3">Product Name</th>
            <th class="border-bottom p-3">Quantity</th>
            <th class="border-bottom p-3">Unit</th>
            <th class="border-bottom p-3">Warehouse</th>
            <th class="border-bottom p-3 text-center">Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${not empty inventoryList}">
                <c:forEach var="inv" items="${inventoryList}" varStatus="loop">
                    <tr>
                        <td class="p-3">${loop.index + 1}</td>

                        <c:forEach var="p" items="${productList}">
                            <c:if test="${p.productId == inv.productId}">
                                <td class="p-3">${p.productCode}</td>
                                <td class="p-3">${p.name}</td>
                                <td class="p-3">${inv.quantity}</td>
                                <td class="p-3">${p.unit}</td>
                            </c:if>
                        </c:forEach>

                        <c:forEach var="w" items="${warehouseList}">
                            <c:if test="${w.warehouseId == inv.warehouseId}">
                                <td class="p-3">${w.name}</td>
                            </c:if>
                        </c:forEach>

                        <td class="text-center p-3">
                            <a href="editInventory?id=${inv.productId}&warehouse=${inv.warehouseId}&action=update"
                               class="btn btn-sm btn-icon btn-soft-success" title="Edit">
                                <i class="fas fa-pen"></i>
                            </a>
                        </td>

                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="7" class="text-center p-3">No inventory records found.</td>
                </tr>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</div>

<!-- Pagination -->
<div class="row mt-4">
    <div class="col-12">
        <ul class="pagination justify-content-center mb-0">
            <c:if test="${currentPage > 1}">
                <li class="page-item">
                    <a class="page-link" href="?page=${currentPage - 1}" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
            </c:if>
            <c:forEach var="i" begin="1" end="${totalPages}">
                <li class="page-item ${currentPage == i ? 'active' : ''}">
                    <a class="page-link" href="?page=${i}">${i}</a>
                </li>
            </c:forEach>
            <c:if test="${currentPage < totalPages}">
                <li class="page-item">
                    <a class="page-link" href="?page=${currentPage + 1}" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </c:if>
        </ul>
    </div>
</div>

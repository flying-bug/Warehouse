<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<div class="d-md-flex justify-content-between align-items-center">
    <h5 class="mb-0">Inventory Adjustment History</h5>
</div>

<!-- Filter Form -->
<form method="get" action="filterAdjustment" class="row g-3 mt-3">
    <div class="col-md-3">
        <input type="date" class="form-control" name="adjustmentDate" placeholder="Date" value="${param.adjustmentDate}">
    </div>
    <div class="col-md-3">
        <input type="text" class="form-control" name="productName" placeholder="Product Name" value="${param.productName}">
    </div>
    <div class="col-md-2">
        <select name="warehouseId" class="form-select">
            <option value="0">All Warehouses</option>
            <c:forEach var="wh" items="${warehouseList}">
                <option value="${wh.warehouseId}" ${param.warehouseId == wh.warehouseId ? 'selected' : ''}>${wh.name}</option>
            </c:forEach>
        </select>
    </div>
    <div class="col-md-1">
        <button type="submit" class="btn btn-primary w-100">Filter</button>
    </div>
</form>

<!-- Adjustment Table -->
<div class="table-responsive shadow rounded mt-4">
    <table class="table table-bordered bg-white mb-0 text-center align-middle">
        <thead class="table-light">
        <tr>
            <th>Date</th>
            <th>Product</th>
            <th>Warehouse</th>
            <th>Adjusted By</th>
            <th>Old Quantity</th>
            <th>Change</th>
            <th>New Quantity</th>
            <th>Reason</th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${not empty adjustmentList}">
                <c:forEach var="ad" items="${adjustmentList}">
                    <tr>
                        <td>${ad.adjustmentDate}</td>
                        <td>
                            <c:forEach var="p" items="${productList}">
                                <c:if test="${p.productId == ad.productId}">
                                    ${p.name}
                                </c:if>
                            </c:forEach>
                        </td>
                        <td>
                            <c:forEach var="w" items="${warehouseList}">
                                <c:if test="${w.warehouseId == ad.warehouseId}">
                                    ${w.name}
                                </c:if>
                            </c:forEach>
                        </td>
                        <td>
                            <c:forEach var="a" items="${accountList}">
                                <c:if test="${a.account_id == ad.accountId}">
                                    ${a.full_name}
                                </c:if>
                            </c:forEach>
                        </td>
                        <td>${ad.oldQuantity}</td>
                        <td>${ad.quantityChange}</td>
                        <td>${ad.oldQuantity + ad.quantityChange}</td>
                        <td>${ad.reason}</td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="8" class="text-danger">No adjustment records found.</td>
                </tr>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</div>

<!-- Pagination -->
<div class="pagination mt-4">
    <ul class="pagination justify-content-center">
        <c:if test="${currentPage > 1}">
            <li class="page-item">
                <a class="page-link" href="?page=${currentPage - 1}">&laquo; Prev</a>
            </li>
        </c:if>
        <c:forEach var="i" begin="1" end="${totalPages}">
            <li class="page-item ${currentPage == i ? 'active' : ''}">
                <a class="page-link" href="?page=${i}">${i}</a>
            </li>
        </c:forEach>
        <c:if test="${currentPage < totalPages}">
            <li class="page-item">
                <a class="page-link" href="?page=${currentPage + 1}">Next &raquo;</a>
            </li>
        </c:if>
    </ul>
</div>

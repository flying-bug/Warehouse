<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h5 class="mb-0 text-danger">Low Inventory Warning</h5>
</div>

<div class="table-responsive shadow rounded">
    <table class="table table-center bg-white mb-0">
        <thead>
        <tr>
            <th class="border-bottom p-3">#</th>
            <th class="border-bottom p-3">Product Code</th>
            <th class="border-bottom p-3">Product Name</th>
            <th class="border-bottom p-3">Quantity</th>
            <th class="border-bottom p-3">Min Level</th>
            <th class="border-bottom p-3">Unit</th>
            <th class="border-bottom p-3">Warehouse</th>
            <th class="border-bottom p-3">Last Updated</th>
            <c:if test="${account.role_id == 4}">
                <th class="border-bottom p-3 text-center">Action</th>
            </c:if>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${not empty linventoryList}">
                <c:forEach var="inv" items="${linventoryList}" varStatus="loop">
                    <tr>
                        <td class="p-3">${loop.index + 1}</td>

                        <c:forEach var="p" items="${productList}">
                            <c:if test="${p.productId == inv.productId}">
                                <td class="p-3">${p.productCode}</td>
                                <td class="p-3">${p.name}</td>
                                <td class="p-3 text-danger fw-bold">${inv.quantity}</td>
                                <td class="p-3">${p.minStockLevel}</td>
                                <td class="p-3">${p.unit}</td>
                            </c:if>
                        </c:forEach>

                        <c:forEach var="w" items="${warehouseList}">
                            <c:if test="${w.warehouseId == inv.warehouseId}">
                                <td class="p-3">${w.name}</td>
                            </c:if>
                        </c:forEach>

                        <td class="p-3">
                            <fmt:formatDate value="${inv.last_updated}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>

                        <c:if test="${account.role_id == 4}">
                            <td class="text-center p-3">
                                <a href="selectSupplier?productId=${inv.productId}&warehouseId=${inv.warehouseId}"
                                   class="btn btn-sm btn-outline-primary" title="Request Restock">
                                    <i class="fas fa-plus-circle"></i> Request
                                </a>
                            </td>
                        </c:if>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="9" class="text-center p-3 text-muted">All inventory levels are normal.</td>
                </tr>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</div>

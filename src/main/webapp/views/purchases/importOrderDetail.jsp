<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>

<div class="container-fluid px-4">
    <h4 class="mt-4 mb-3">Import Order Details - ID: ${importOrder.importId}</h4>

    <div class="card shadow-sm rounded mb-4">
        <div class="card-body">
            <p><strong>Supplier:</strong>
                <c:forEach var="s" items="${suppliersList}">
                    <c:if test="${s.supplierId == importOrder.supplierId}">
                        ${s.name}
                    </c:if>
                </c:forEach>
            </p>

            <p><strong>Warehouse:</strong>
                <c:forEach var="w" items="${warehousesList}">
                    <c:if test="${w.warehouseId == importOrder.warehouseId}">
                        ${w.name}
                    </c:if>
                </c:forEach>
            </p>

            <p><strong>Handled By:</strong>
                <c:forEach var="a" items="${accountsList}">
                    <c:if test="${a.account_id == importOrder.accountId}">
                        ${a.full_name}
                    </c:if>
                </c:forEach>
            </p>

            <p><strong>Import Date:</strong> ${importOrder.importDate}</p>
            <p><strong>Status:</strong> ${importOrder.status}</p>
            <p><strong>Note:</strong> ${importOrder.note}</p>

            <!-- Tính tổng tiền -->
            <c:set var="totalCost" value="0" />
            <c:forEach var="detail" items="${importDetails}">
                <c:set var="itemTotal" value="${detail.quantity * detail.costPrice}" />
                <c:set var="totalCost" value="${totalCost + itemTotal}" />
            </c:forEach>
            <p class="fw-bold mb-3">
                Total Purchase:
                <fmt:formatNumber value="${totalCost}" type="number" groupingUsed="true" /> VNĐ
            </p>
        </div>
    </div>

    <div class="card shadow-sm rounded">
        <div class="card-body">
            <h5 class="mb-3">Imported Products</h5>
            <div class="table-responsive">
                <table class="table table-bordered text-center align-middle">
                    <thead class="table-light">
                    <tr>
                        <th>Product</th>
                        <th>Quantity</th>
                        <th>Cost Price</th>
                        <th>Status</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty importDetails}">
                            <c:forEach var="detail" items="${importDetails}">
                                <tr>
                                    <td>
                                        <c:forEach var="p" items="${plist}">
                                            <c:if test="${p.productId == detail.productId}">
                                                ${p.name}
                                            </c:if>
                                        </c:forEach>
                                    </td>

                                    <td>${detail.quantity}</td>
                                    <td>
                                        <fmt:formatNumber value="${detail.costPrice}" type="number" groupingUsed="true" /> VNĐ
                                    </td>
                                    <td>${detail.importStatus}</td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="4" class="text-danger">No details available for this import order.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="mt-3">
        <a href="${pageContext.request.contextPath}/viewListImportOrders" class="btn btn-secondary">
            &larr; Back to Import Orders
        </a>
    </div>
</div>

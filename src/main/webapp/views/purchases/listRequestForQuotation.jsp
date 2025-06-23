<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<div class="container-fluid px-4">
    <!-- Toolbar -->
    <div class="d-flex justify-content-between align-items-center mt-4 mb-3">
        <div class="d-flex gap-2">
            <a href="addImportOrder" class="btn btn-sm btn-primary">New</a>
            <h5 class="mb-0 align-self-center">Requests for Quotation</h5>
        </div>

        <!-- Search bar -->
        <form action="viewListImportOrders" method="get" id="filterForm"
              class="d-flex align-items-center bg-light rounded-pill px-3 py-1 shadow-sm" style="min-width: 300px;">
            <i class="bi bi-search text-muted me-2"></i>
            <input type="text" name="keyword" value="${keyword}" class="form-control border-0"
                   placeholder="Search..." style="background: transparent;">
            <button type="submit" class="btn btn-sm btn-light ms-2"><i class="bi bi-arrow-right"></i></button>
        </form>
    </div>

    <!-- Filter Tags -->
    <div class="mb-3 d-flex flex-wrap gap-2">
        <c:if test="${not empty keyword}">
            <span class="badge bg-secondary-subtle text-dark">
                ${keyword}
                <a href="viewListImportOrders?clearFilter=true" class="text-dark ms-1">×</a>
            </span>
        </c:if>

        <c:if test="${selectW != 'all'}">
            <c:forEach var="w" items="${warehousesList}">
                <c:if test="${w.warehouseId == selectW}">
                    <span class="badge bg-secondary-subtle text-dark">
                        ${w.name}
                        <a href="viewListImportOrders?selectW=all&selectS=${selectS}&selectA=${selectA}&selectStatus=${selectStatus}&fromDate=${fromDate}&toDate=${toDate}&keyword=${keyword}"
                           class="text-dark ms-1">×</a>
                    </span>
                </c:if>
            </c:forEach>
        </c:if>

        <c:if test="${selectS != 'all'}">
            <c:forEach var="s" items="${suppliersList}">
                <c:if test="${s.supplierId == selectS}">
                    <span class="badge bg-secondary-subtle text-dark">
                        ${s.name}
                        <a href="viewListImportOrders?selectS=all&selectW=${selectW}&selectA=${selectA}&selectStatus=${selectStatus}&fromDate=${fromDate}&toDate=${toDate}&keyword=${keyword}"
                           class="text-dark ms-1">×</a>
                    </span>
                </c:if>
            </c:forEach>
        </c:if>

        <c:if test="${selectA != 'all'}">
            <c:forEach var="a" items="${accountsList}">
                <c:if test="${a.account_id == selectA}">
                    <span class="badge bg-secondary-subtle text-dark">
                        ${a.full_name}
                        <a href="viewListImportOrders?selectA=all&selectW=${selectW}&selectS=${selectS}&selectStatus=${selectStatus}&fromDate=${fromDate}&toDate=${toDate}&keyword=${keyword}"
                           class="text-dark ms-1">×</a>
                    </span>
                </c:if>
            </c:forEach>
        </c:if>

        <c:if test="${selectStatus != 'all'}">
            <span class="badge bg-secondary-subtle text-dark">
                ${selectStatus}
                <a href="viewListImportOrders?selectStatus=all&selectW=${selectW}&selectS=${selectS}&selectA=${selectA}&fromDate=${fromDate}&toDate=${toDate}&keyword=${keyword}"
                   class="text-dark ms-1">×</a>
            </span>
        </c:if>

        <c:if test="${not empty fromDate}">
            <span class="badge bg-secondary-subtle text-dark">
                From: ${fromDate}
                <a href="viewListImportOrders?fromDate=&selectW=${selectW}&selectS=${selectS}&selectA=${selectA}&selectStatus=${selectStatus}&toDate=${toDate}&keyword=${keyword}"
                   class="text-dark ms-1">×</a>
            </span>
        </c:if>

        <c:if test="${not empty toDate}">
            <span class="badge bg-secondary-subtle text-dark">
                To: ${toDate}
                <a href="viewListImportOrders?toDate=&selectW=${selectW}&selectS=${selectS}&selectA=${selectA}&selectStatus=${selectStatus}&fromDate=${fromDate}&keyword=${keyword}"
                   class="text-dark ms-1">×</a>
            </span>
        </c:if>
    </div>

    <!-- Table -->
    <!-- Table -->
    <div class="card shadow-sm rounded mb-4">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-bordered text-center align-middle">
                    <thead class="table-light">
                    <tr>
                        <th>*</th>
                        <th>Reference</th>
                        <th>Supplier</th>
                        <th>Warehouse</th>
                        <th>Buyer</th>
                        <th>Order Deadline</th>
                        <th>Activities</th>
                        <th>Source Document</th>
                        <th>Total</th>
                        <th>Status</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty importOrderList}">
                            <c:forEach var="o" items="${importOrderList}">
                                <tr>
                                    <td>
                                        <input type="checkbox" name="selectedIds" value="${o.importId}"/>
                                    </td>

                                    <td>
                                        <a href="viewImportOrderDetail?importId=${o.importId}" class="text-primary  text-decoration-underline fw-bold">
                                                ${o.code}
                                        </a>
                                    </td>

                                    <td>
                                        <c:forEach var="s" items="${suppliersList}">
                                            <c:if test="${s.supplierId == o.supplierId}">${s.name}</c:if>
                                        </c:forEach>
                                    </td>
                                    <td>
                                        <c:forEach var="w" items="${warehousesList}">
                                            <c:if test="${w.warehouseId == o.warehouseId}">${w.name}</c:if>
                                        </c:forEach>
                                    </td>
                                    <td class="d-flex align-items-center justify-content-center gap-2">
                                        <c:forEach var="a" items="${accountsList}">
                                            <c:if test="${a.account_id == o.accountId}">
                                                <img src="${a.profile_image}" alt="Avatar" class="rounded-circle"
                                                     width="30" height="30" style="object-fit: cover;">
                                                ${a.full_name}
                                            </c:if>
                                        </c:forEach>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${o.importDate}" pattern="yyyy-MM-dd"/>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty o.activityNote}">
                                                <c:choose>
                                                    <c:when test="${o.activityNote == 'Check competitors'}">
                                                        <i class="fas fa-check-circle text-success" title="Check competitors">${o.activityNote}</i>
                                                    </c:when>
                                                    <c:when test="${o.activityNote == 'Check optional products'}">
                                                        <i class="fas fa-box text-success" title="Check optional products">${o.activityNote}</i>
                                                    </c:when>
                                                    <c:when test="${o.activityNote == 'Get approval'}">
                                                        <i class="fas fa-times-circle text-danger" title="Get approval">${o.activityNote}</i>
                                                    </c:when>
                                                    <c:when test="${o.activityNote == 'Send specifications'}">
                                                        <i class="fas fa-envelope text-primary" title="Send specifications">${o.activityNote}</i>
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${o.activityNote}
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:when>
                                            <c:otherwise>
                                                <i class="fas fa-clock text-muted" title="No activity assigned"></i>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <td>
                                            ${o.note}
                                    </td>


                                    <td>
                                        <fmt:formatNumber value="${o.totalCost}" type="number" groupingUsed="true"/> ₫
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${o.status == 'Done'}">
                                                <span class="badge bg-success">Purchase Order</span>
                                            </c:when>
                                            <c:when test="${o.status == 'Cancel'}">
                                                <span class="badge bg-danger">Cancelled</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-warning text-dark">RFQ</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
<%--                                    <td>--%>
<%--                                        <a href="viewImportOrderDetail?importId=${o.importId}"--%>
<%--                                           class="btn btn-sm btn-outline-primary me-1" title="View Detail">--%>
<%--                                            <i class="bi bi-eye"></i>--%>
<%--                                        </a>--%>
<%--                                        <c:if test="${o.status == 'Scheduled'}">--%>
<%--                                            <a href="confirmImport?action=update&importId=${o.importId}"--%>
<%--                                               class="btn btn-sm btn-success me-1" title="Mark as Done"--%>
<%--                                               onclick="return confirm('Mark this order as Done?');">--%>
<%--                                                <i class="bi bi-check-lg"></i>--%>
<%--                                            </a>--%>
<%--                                            <a href="confirmImport?action=delete&importId=${o.importId}"--%>
<%--                                               class="btn btn-sm btn-danger" title="Cancel Order"--%>
<%--                                               onclick="return confirm('Cancel this order?');">--%>
<%--                                                <i class="bi bi-x-lg"></i>--%>
<%--                                            </a>--%>
<%--                                        </c:if>--%>
<%--                                    </td>--%>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="10" class="text-danger">No import orders found.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</div>

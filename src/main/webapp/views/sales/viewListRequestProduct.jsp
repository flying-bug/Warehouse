<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

<div class="container-fluid px-4">
    <!-- Toolbar and Filter/Search Bar -->
    <div class="d-flex justify-content-between align-items-center mt-4 mb-3">
        <div>
            <a href="#" class="btn btn-sm btn-primary me-1">New</a>
            <a href="#" class="btn btn-sm btn-outline-secondary">Upload</a>
            <span class="ms-2 fw-bold">Quotations <i class="fas fa-cog"></i></span>
        </div>
        <div class="d-flex align-items-center gap-2">
            <form action="viewListRequestProducts" method="get" id="filterForm"
                  class="d-flex align-items-center bg-light rounded-pill p-2 shadow-sm" style="min-width: 300px;">
                <i class="fas fa-search text-muted me-2"></i>
                <div class="d-flex flex-wrap align-items-center gap-2 flex-grow-1">
                    <c:if test="${not empty keyword}">
            <span class="badge bg-secondary-subtle text-secondary rounded-pill d-flex align-items-center">
                ${keyword}
                <a href="viewListRequestProducts?clearFilter=true
                    &selectW=${selectW}
                    &selectC=${selectC}
                    &selectA=${selectA}
                    &selectS=${selectS}
                    &fromDate=${fromDate}
                    &toDate=${toDate}" class="text-dark ms-1">×</a>
            </span>
                    </c:if>

                    <c:if test="${selectW != 'all'}">
            <span class="badge bg-secondary-subtle text-secondary rounded-pill d-flex align-items-center">
                <c:forEach var="w" items="${warehousesList}">
                    <c:if test="${w.warehouseId == selectW}">${w.name}</c:if>
                </c:forEach>
                <a href="viewListRequestProducts?selectW=all
                    &selectC=${selectC}
                    &selectA=${selectA}
                    &selectS=${selectS}
                    &fromDate=${fromDate}
                    &toDate=${toDate}
                    &keyword=${keyword}" class="text-dark ms-1">×</a>
            </span>
                    </c:if>

                    <c:if test="${selectC != 'all'}">
            <span class="badge bg-secondary-subtle text-secondary rounded-pill d-flex align-items-center">
                <c:forEach var="c" items="${customersList}">
                    <c:if test="${c.customerId == selectC}">${c.name}</c:if>
                </c:forEach>
                <a href="viewListRequestProducts?selectC=all
                    &selectW=${selectW}
                    &selectA=${selectA}
                    &selectS=${selectS}
                    &fromDate=${fromDate}
                    &toDate=${toDate}
                    &keyword=${keyword}" class="text-dark ms-1">×</a>
            </span>
                    </c:if>

                    <c:if test="${selectA != 'all'}">
            <span class="badge bg-secondary-subtle text-secondary rounded-pill d-flex align-items-center">
                <c:forEach var="a" items="${accountsList}">
                    <c:if test="${a.account_id == selectA}">${a.full_name}</c:if>
                </c:forEach>
                <a href="viewListRequestProducts?selectA=all
                    &selectW=${selectW}
                    &selectC=${selectC}
                    &selectS=${selectS}
                    &fromDate=${fromDate}
                    &toDate=${toDate}
                    &keyword=${keyword}" class="text-dark ms-1">×</a>
            </span>
                    </c:if>

                    <c:if test="${selectS != 'all'}">
            <span class="badge bg-secondary-subtle text-secondary rounded-pill d-flex align-items-center">
                ${selectS}
                <a href="viewListRequestProducts?selectS=all
                    &selectW=${selectW}
                    &selectC=${selectC}
                    &selectA=${selectA}
                    &fromDate=${fromDate}
                    &toDate=${toDate}
                    &keyword=${keyword}" class="text-dark ms-1">×</a>
            </span>
                    </c:if>

                    <c:if test="${not empty fromDate}">
            <span class="badge bg-secondary-subtle text-secondary rounded-pill d-flex align-items-center">
                From: ${fromDate}
                <a href="viewListRequestProducts?fromDate=
                    &selectW=${selectW}
                    &selectC=${selectC}
                    &selectA=${selectA}
                    &selectS=${selectS}
                    &toDate=${toDate}
                    &keyword=${keyword}" class="text-dark ms-1">×</a>
            </span>
                    </c:if>

                    <c:if test="${not empty toDate}">
            <span class="badge bg-secondary-subtle text-secondary rounded-pill d-flex align-items-center">
                To: ${toDate}
                <a href="viewListRequestProducts?toDate=
                    &selectW=${selectW}
                    &selectC=${selectC}
                    &selectA=${selectA}
                    &selectS=${selectS}
                    &fromDate=${fromDate}
                    &keyword=${keyword}" class="text-dark ms-1">×</a>
            </span>
                    </c:if>

                    <input type="text" name="keyword" value="${keyword}" class="form-control border-0 flex-grow-1"
                           placeholder="Search..." style="background: transparent;">
                </div>
                <div class="dropdown ms-2">
                    <button class="btn btn-sm btn-light dropdown-toggle" type="button" id="filterDropdown"
                            data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="fas fa-filter"></i>
                    </button>
                    <ul class="dropdown-menu p-3 shadow w-100" aria-labelledby="filterDropdown"
                        style="min-width: 300px;">
                        <div class="row g-2">
                            <div class="col-md-6">
                                <label class="form-label">Warehouse</label>
                                <select name="selectW" class="form-select"
                                        onchange="document.getElementById('filterForm').submit()">
                                    <option value="all" ${selectW == 'all' ? 'selected' : ''}>All</option>
                                    <c:forEach var="w" items="${warehousesList}">
                                        <option value="${w.warehouseId}" ${String.valueOf(w.warehouseId) == selectW ? 'selected' : ''}>${w.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Customer</label>
                                <select name="selectC" class="form-select"
                                        onchange="document.getElementById('filterForm').submit()">
                                    <option value="all" ${selectC == 'all' ? 'selected' : ''}>All</option>
                                    <c:forEach var="c" items="${customersList}">
                                        <option value="${c.customerId}" ${String.valueOf(c.customerId) == selectC ? 'selected' : ''}>${c.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Salesperson</label>
                                <select name="selectA" class="form-select"
                                        onchange="document.getElementById('filterForm').submit()">
                                    <option value="all" ${selectA == 'all' ? 'selected' : ''}>All</option>
                                    <c:forEach var="a" items="${accountsList}">
                                        <c:if test="${a.role_id == 5}">
                                            <option value="${a.account_id}" ${String.valueOf(a.account_id) == selectA ? 'selected' : ''}>${a.full_name}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Status</label>
                                <select name="selectS" class="form-select"
                                        onchange="document.getElementById('filterForm').submit()">
                                    <option value="all" ${selectS == 'all' ? 'selected' : ''}>All</option>
                                    <option value="Scheduled" ${selectS == 'Scheduled' ? 'selected' : ''}>Scheduled
                                    </option>
                                    <option value="Done" ${selectS == 'Done' ? 'selected' : ''}>Done</option>
                                    <option value="Cancel" ${selectS == 'Cancel' ? 'selected' : ''}>Cancel</option>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">From</label>
                                <input type="date" class="form-control" name="fromDate" value="${fromDate}"
                                       onchange="document.getElementById('filterForm').submit()">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">To</label>
                                <input type="date" class="form-control" name="toDate" value="${toDate}"
                                       onchange="document.getElementById('filterForm').submit()">
                            </div>
                        </div>
                    </ul>
                </div>
            </form>


            <div class="d-flex align-items-center">
                <span class="me-2">Page: ${startRecord}-${endRecord} / ${totalRecords}</span>
                <a class="btn btn-sm btn-outline-secondary me-1 ${currentPage == 1 ? 'disabled' : ''}"
                   href="viewListRequestProducts?page=${currentPage - 1}&selectW=${selectW}&selectA=${selectA}&selectC=${selectC}&selectS=${selectS}&fromDate=${fromDate}&toDate=${toDate}">
                    <i class="fas fa-chevron-left"></i>
                </a>
                <a class="btn btn-sm btn-outline-secondary ${currentPage == totalPages ? 'disabled' : ''}"
                   href="viewListRequestProducts?page=${currentPage + 1}&selectW=${selectW}&selectA=${selectA}&selectC=${selectC}&selectS=${selectS}&fromDate=${fromDate}&toDate=${toDate}">
                    <i class="fas fa-chevron-right"></i>
                </a>
            </div>

            <div class="btn-group">
                <button class="btn btn-sm btn-outline-secondary active"><i class="fas fa-bars"></i></button>
                <button class="btn btn-sm btn-outline-secondary"><i class="fas fa-th-list"></i></button>
                <button class="btn btn-sm btn-outline-secondary"><i class="fas fa-calendar-alt"></i></button>
                <button class="btn btn-sm btn-outline-secondary"><i class="fas fa-chart-bar"></i></button>
            </div>
        </div>
    </div>

    <!-- Filter Form -->


    <!-- Table Results -->
    <div class="card shadow-sm rounded mb-4">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-bordered align-middle">
                    <thead class="table-light">
                    <tr>
                        <th><input type="checkbox" id="checkAll"/></th> <!-- Tick All -->
                        <th>Number</th>
                        <th>Creation Date</th>
                        <th>Customer</th>
                        <th>Salesperson</th>
                        <th>Activities</th>
                        <th>Warehouse</th>
                        <th>Total</th>
                        <th>Status</th>

                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty exportOrderList}">
                            <c:forEach var="exportOrder" items="${exportOrderList}">
                                <tr>
                                    <td>
                                        <input type="checkbox" name="selectedIds" value="${exportOrder.exportId}"/>
                                    </td>
                                    <td>
                                        <a href="viewExportOrderDetail?exportId=${exportOrder.exportId}"
                                           class="text-secondary text-decoration-underline">
                                            <strong>${exportOrder.code}</strong>
                                        </a>
                                    </td>


                                    <td>${exportOrder.exportDate}</td>
                                    <td>
                                        <c:forEach var="c" items="${customersList}">
                                            <c:if test="${c.customerId == exportOrder.customerId}">${c.name}</c:if>
                                        </c:forEach>
                                    </td>
                                    <td class="d-flex align-items-center gap-2 justify-content-center">
                                        <c:forEach var="a" items="${accountsList}">
                                            <c:if test="${a.account_id == exportOrder.accountId}">
                                                <img src="${a.profile_image}" alt="Avatar" class="rounded-circle"
                                                     width="32" height="32" style="object-fit: cover;">
                                                <span>${a.full_name}</span>
                                            </c:if>
                                        </c:forEach>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty exportOrder.activityNote}">
                                                <c:choose>
                                                    <c:when test="${exportOrder.activityNote == 'Answer questions'}">
                                                        <i class="fas fa-envelope text-danger"
                                                           title="Answer questions"></i>
                                                    </c:when>
                                                    <c:when test="${exportOrder.activityNote == 'Check delivery requirements'}">
                                                        <i class="fas fa-truck text-warning"
                                                           title="Check delivery requirements"></i>
                                                    </c:when>
                                                    <c:when test="${exportOrder.activityNote == 'Send quotation'}">
                                                        <i class="fas fa-paper-plane text-primary"
                                                           title="Send quotation"></i>
                                                    </c:when>
                                                    <c:when test="${exportOrder.activityNote == 'Follow up'}">
                                                        <i class="fas fa-phone-alt text-info" title="Follow up"></i>
                                                    </c:when>
                                                    <c:otherwise>${exportOrder.activityNote}</c:otherwise>
                                                </c:choose>
                                            </c:when>
                                            <c:otherwise>
                                                <i class="fas fa-clock text-muted" title="No activity assigned"></i>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:forEach var="w" items="${warehousesList}">
                                            <c:if test="${w.warehouseId == exportOrder.warehouseId}">${w.name} (${w.location})</c:if>
                                        </c:forEach>
                                    </td>
                                    <td>
                                        <fmt:formatNumber value="${exportOrder.totalAmount}" type="number"
                                                          groupingUsed="true"/> ₫
                                    </td>


                                    <td>
                                        <c:choose>
                                            <c:when test="${exportOrder.status == 'Done'}">
                                                <span class="badge bg-success">Sales Order</span>
                                            </c:when>
                                            <c:when test="${exportOrder.status == 'Scheduled'}">
                                                <span class="badge bg-info text-dark">Quotation</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-danger">Cancelled</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
<%--                                    <td>--%>
<%--                                        <c:if test="${exportOrder.status == 'Scheduled'}">--%>
<%--                                            <a href="confirmRequest?action=update&exportId=${exportOrder.exportId}"--%>
<%--                                               class="btn btn-sm btn-outline-success me-1"--%>
<%--                                               onclick="return confirm('Are you sure you want to mark this order as Done?');">Done</a>--%>
<%--                                            <a href="confirmRequest?action=delete&exportId=${exportOrder.exportId}"--%>
<%--                                               class="btn btn-sm btn-outline-danger"--%>
<%--                                               onclick="return confirm('Are you sure you want to cancel this order?');">Cancel</a>--%>
<%--                                        </c:if>--%>
<%--                                    </td>--%>
<%--                                </tr>--%>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="9" class="text-danger">No export orders found.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const checkAll = document.getElementById("checkAll");
        const checkboxes = document.querySelectorAll("input[name='selectedIds']");
        checkAll.addEventListener("change", function () {
            checkboxes.forEach(cb => cb.checked = this.checked);
        });
    });
</script>


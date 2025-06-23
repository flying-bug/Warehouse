<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>

<!-- Bootstrap Icons -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">

<div class="container-fluid px-4">
    <div class="d-flex justify-content-between align-items-center mt-4 mb-3">
        <h4 class="mb-0">Import Orders</h4>
    </div>

    <!-- Notification -->
    <c:if test="${not empty param.message}">
        <div class="alert
        <c:choose>
            <c:when test="${param.message == 'update_success' || param.message == 'delete_success'}">alert-success</c:when>
            <c:otherwise>alert-danger</c:otherwise>
        </c:choose>
        alert-dismissible fade show" role="alert">
            <c:choose>
                <c:when test="${param.message == 'update_success'}">Marked as <strong>Done</strong> successfully.</c:when>
                <c:when test="${param.message == 'delete_success'}">Import order <strong>cancelled</strong> successfully.</c:when>
                <c:otherwise>Action failed or unknown error occurred.</c:otherwise>
            </c:choose>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <!-- Filter Form -->
    <form action="viewListImportOrders" method="get" class="row g-3 mb-3">
        <div class="col-md-2">
            <label class="form-label">Warehouse</label>
            <select name="selectW" class="form-select">
                <option value="all" ${selectW == 'all' ? 'selected' : ''}>All</option>
                <c:forEach var="w" items="${warehousesList}">
                    <option value="${w.warehouseId}" ${String.valueOf(w.warehouseId) == selectW ? 'selected' : ''}>${w.name}</option>
                </c:forEach>
            </select>
        </div>

        <div class="col-md-2">
            <label class="form-label">Supplier</label>
            <select name="selectS" class="form-select">
                <option value="all" ${selectS == 'all' ? 'selected' : ''}>All</option>
                <c:forEach var="s" items="${suppliersList}">
                    <option value="${s.supplierId}" ${String.valueOf(s.supplierId) == selectS ? 'selected' : ''}>${s.name}</option>
                </c:forEach>
            </select>
        </div>

        <div class="col-md-2">
            <label class="form-label">Account</label>
            <select name="selectA" class="form-select">
                <option value="all" ${selectA == 'all' ? 'selected' : ''}>All</option>
                <c:forEach var="a" items="${accountsList}">
                    <c:if test="${a.role_id == 4}">
                        <option value="${a.account_id}" ${String.valueOf(a.account_id) == selectA ? 'selected' : ''}>${a.full_name}</option>
                    </c:if>
                </c:forEach>
            </select>
        </div>

        <div class="col-md-2">
            <label class="form-label">Status</label>
            <select name="selectStatus" class="form-select">
                <option value="all" ${selectStatus == 'all' ? 'selected' : ''}>All</option>
                <option value="Scheduled" ${selectStatus == 'Scheduled' ? 'selected' : ''}>Scheduled</option>
                <option value="Done" ${selectStatus == 'Done' ? 'selected' : ''}>Done</option>
                <option value="Cancel" ${selectStatus == 'Cancel' ? 'selected' : ''}>Cancel</option>
            </select>
        </div>

        <div class="col-md-2">
            <label class="form-label">From</label>
            <input type="date" name="fromDate" class="form-control" value="${fromDate}">
        </div>
        <div class="col-md-2">
            <label class="form-label">To</label>
            <input type="date" name="toDate" class="form-control" value="${toDate}">
        </div>

        <div class="col-md-3">
            <label class="form-label">Keyword</label>
            <input type="text" name="keyword" class="form-control" placeholder="Search...">
        </div>

        <div class="col-md-2 align-self-end">
            <button type="submit" class="btn btn-primary w-100">Filter</button>
        </div>
    </form>

    <!-- Table -->
    <div class="card shadow-sm rounded mb-4">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-bordered text-center align-middle">
                    <thead class="table-light">
                    <tr>
                        <th>Import ID</th>
                        <th>Supplier</th>
                        <th>Warehouse</th>
                        <th>Account</th>
                        <th>Date</th>
                        <th>Note</th>
                        <th>Activities</th>
                        <th>Status</th>
                        <th>Total</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty importOrderList}">
                            <c:forEach var="o" items="${importOrderList}">
                                <tr>
                                    <td>${o.importId}</td>
                                    <td><c:forEach var="s" items="${suppliersList}"><c:if test="${s.supplierId == o.supplierId}">${s.name}</c:if></c:forEach></td>
                                    <td><c:forEach var="w" items="${warehousesList}"><c:if test="${w.warehouseId == o.warehouseId}">${w.name}</c:if></c:forEach></td>
                                    <td><c:forEach var="a" items="${accountsList}"><c:if test="${a.account_id == o.accountId}">${a.full_name}</c:if></c:forEach></td>
                                    <td>${o.importDate}</td>
                                    <td>${o.note}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${o.status == 'Done'}"><i class="bi bi-check-circle text-success"></i> Completed</c:when>
                                            <c:when test="${o.status == 'Cancel'}"><i class="bi bi-x-circle text-danger"></i> Cancelled</c:when>
                                            <c:otherwise><i class="bi bi-clock text-warning"></i> Waiting</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${o.status == 'Done'}"><span class="badge bg-success px-3 py-2">Done</span></c:when>
                                            <c:when test="${o.status == 'Cancel'}"><span class="badge bg-danger px-3 py-2">Cancelled</span></c:when>
                                            <c:otherwise><span class="badge bg-warning text-dark px-3 py-2">Scheduled</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><fmt:formatNumber value="${o.totalCost}" type="number" groupingUsed="true"/> VNĐ</td>
                                    <td>
                                        <a href="viewImportOrderDetail?importId=${o.importId}" class="btn btn-sm btn-outline-primary me-1" title="Detail"><i class="bi bi-eye"></i></a>
                                        <c:if test="${o.status == 'Scheduled'}">
                                            <a href="confirmImport?action=update&importId=${o.importId}" class="btn btn-sm btn-success me-1" title="Mark as Done" onclick="return confirm('Mark as Done?');"><i class="bi bi-check-lg"></i></a>
                                            <a href="confirmImport?action=delete&importId=${o.importId}" class="btn btn-sm btn-danger" title="Cancel Order" onclick="return confirm('Cancel this order?');"><i class="bi bi-x-lg"></i></a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr><td colspan="10" class="text-danger">No import orders found.</td></tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Pagination -->
    <c:if test="${totalPages > 1}">
        <nav aria-label="Page navigation">
            <ul class="pagination justify-content-center">
                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                    <a class="page-link" href="viewListImportOrders?page=${currentPage - 1}&selectW=${selectW}&selectS=${selectS}&selectA=${selectA}&selectStatus=${selectStatus}&fromDate=${fromDate}&toDate=${toDate}">Previous</a>
                </li>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <li class="page-item ${i == currentPage ? 'active' : ''}">
                        <a class="page-link" href="viewListImportOrders?page=${i}&selectW=${selectW}&selectS=${selectS}&selectA=${selectA}&selectStatus=${selectStatus}&fromDate=${fromDate}&toDate=${toDate}">${i}</a>
                    </li>
                </c:forEach>
                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                    <a class="page-link" href="viewListImportOrders?page=${currentPage + 1}&selectW=${selectW}&selectS=${selectS}&selectA=${selectA}&selectStatus=${selectStatus}&fromDate=${fromDate}&toDate=${toDate}">Next</a>
                </li>
            </ul>
        </nav>
    </c:if>
</div>

<!-- Optional: Styling -->
<style>
    .table th, .table td {
        vertical-align: middle !important;
    }
    .badge {
        font-size: 0.9em;
        font-weight: 500;
    }
    .form-label {
        font-weight: 600;
    }
</style>

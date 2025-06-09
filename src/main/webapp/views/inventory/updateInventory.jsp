<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<!-- Tiêu đề và Breadcrumb -->
<div class="d-md-flex justify-content-between align-items-center">
    <h5 class="mb-0">Update Inventory</h5>
    <nav aria-label="breadcrumb" class="d-inline-block mt-4 mt-sm-0">
        <ul class="breadcrumb bg-transparent rounded mb-0 p-0">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboardAdmin">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/viewListInventory">Inventory</a></li>
            <li class="breadcrumb-item active" aria-current="page">Update</li>
        </ul>
    </nav>
</div>

<!-- Nội dung -->
<div class="row justify-content-center">
    <div class="col-lg-10 mt-4">
        <div class="card border-0 p-4 rounded shadow">

            <!-- Alert -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <c:choose>
                <c:when test="${not empty inventory && not empty product && not empty warehouse}">
                    <form class="mt-3" action="editInventory" method="post">
                        <!-- Hidden -->
                        <input type="hidden" name="productId" value="${product.productId}">
                        <input type="hidden" name="warehouseId" value="${warehouse.warehouseId}">

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Product Code:</label>
                                <input type="text" class="form-control" value="${product.productCode}" readonly>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Product Name:</label>
                                <input type="text" class="form-control" value="${product.name}" readonly>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Warehouse:</label>
                                <input type="text" class="form-control" value="${warehouse.name}" readonly>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Current Quantity:</label>
                                <input type="number" class="form-control" value="${inventory.quantity}" readonly>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">New Quantity<span class="text-danger">*</span>:</label>
                                <input type="number" class="form-control" name="newQuantity" min="0" required>
                            </div>
                            <div class="col-md-12 mb-3">
                                <label class="form-label">Reason<span class="text-danger">*</span>:</label>
                                <textarea class="form-control" name="reason" rows="3" placeholder="Enter reason for adjustment" required></textarea>
                            </div>
                        </div>

                        <!-- Nút -->
                        <div class="mt-4 text-end">
                            <button type="submit" class="btn btn-primary me-2">
                                <i class="uil uil-check-circle me-1"></i> Update
                            </button>
                            <a href="${pageContext.request.contextPath}/viewListInventory" class="btn btn-light">
                                <i class="uil uil-times me-1"></i> Cancel
                            </a>
                        </div>
                    </form>
                </c:when>

                <c:otherwise>
                    <div class="alert alert-danger">No inventory data available.</div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

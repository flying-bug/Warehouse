<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<%-- File nội dung chi tiết sản phẩm, dùng trong dashboard layout --%>

<div class="d-md-flex justify-content-between align-items-center">
    <h5 class="mb-0">Product Details</h5>
    <nav aria-label="breadcrumb" class="d-inline-block mt-4 mt-sm-0">
        <ul class="breadcrumb bg-transparent rounded mb-0 p-0">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboardAdmin">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/viewListProducts">Products</a></li>
            <li class="breadcrumb-item active" aria-current="page">Details</li>
        </ul>
    </nav>
</div>

<div class="row justify-content-center">
    <div class="col-lg-10 mt-4">
        <div class="card border-0 p-4 rounded shadow">

            <c:if test="${empty p}">
                <div class="alert alert-danger fw-bold text-center">No product information found.</div>
            </c:if>

            <c:if test="${not empty p}">
                <div class="row">
                    <!-- Left Column -->
                    <div class="col-md-6 mb-3">
                        <label class="form-label">Product Code:</label>
                        <div class="form-control bg-light">${p.productCode}</div>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label">Product Name:</label>
                        <div class="form-control bg-light">${p.name}</div>
                    </div>
                    <div class="col-md-12 mb-3">
                        <label class="form-label">Description:</label>
                        <div class="form-control bg-light">${p.description}</div>
                    </div>

                    <div class="col-md-4 mb-3">
                        <label class="form-label">Size:</label>
                        <div class="form-control bg-light">${p.size}</div>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label class="form-label">Color:</label>
                        <div class="form-control bg-light">${p.color}</div>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label class="form-label">Material:</label>
                        <div class="form-control bg-light">${p.material}</div>
                    </div>

                    <div class="col-md-4 mb-3">
                        <label class="form-label">Unit:</label>
                        <div class="form-control bg-light">${p.unit}</div>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label class="form-label">Cost Price:</label>
                        <div class="form-control bg-light">${p.costPrice}</div>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label class="form-label">Sale Price:</label>
                        <div class="form-control bg-light">${p.salePrice}</div>
                    </div>

                    <div class="col-md-4 mb-3">
                        <label class="form-label">Status:</label>
                        <div class="form-control bg-light">${p.status == 1 ? 'Active' : 'Inactive'}</div>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label class="form-label">Minimum Stock Level:</label>
                        <div class="form-control bg-light">${p.minStockLevel}</div>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label class="form-label">Image URL:</label>
                        <div class="form-control bg-light text-truncate" title="${p.image}">${p.image}</div>
                    </div>

                    <!-- Image Preview -->
                    <c:if test="${not empty p.image}">
                        <div class="col-12 text-center mt-3">
                            <img src="${p.image}" alt="Product Image" class="img-thumbnail" style="max-height: 200px;">
                        </div>
                    </c:if>
                </div>

                <div class="mt-4 text-end">
                    <a href="${pageContext.request.contextPath}/viewListProducts" class="btn btn-secondary">
                        <i class="fas fa-arrow-left me-1"></i> Back to List
                    </a>
                </div>
            </c:if>
        </div>
    </div>
</div>

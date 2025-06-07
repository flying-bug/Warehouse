<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<div class="d-md-flex justify-content-between align-items-center">
    <h5 class="mb-0">Update Product: ID = ${p.productId}</h5>
    <nav aria-label="breadcrumb" class="d-inline-block mt-4 mt-sm-0">
        <ul class="breadcrumb bg-transparent rounded mb-0 p-0">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboardAdmin">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/viewListProducts">Products</a></li>
            <li class="breadcrumb-item active" aria-current="page">Update</li>
        </ul>
    </nav>
</div>

<div class="row justify-content-center">
    <div class="col-lg-10 mt-4">
        <div class="card border-0 p-4 rounded shadow">

            <!-- Success / Error Messages -->
            <c:if test="${not empty success}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                        ${success}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <c:if test="${not empty fail}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${fail}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <c:if test="${empty p}">
                <div class="alert alert-danger fw-bold text-center">Không tìm thấy sản phẩm để cập nhật.</div>
            </c:if>

            <!-- Form -->
            <form class="mt-3" action="${pageContext.request.contextPath}/updateProduct" method="post">
                <input type="hidden" name="product_id" value="${p.productId}">

                <div class="row">
                    <!-- Left Column -->
                    <div class="col-md-6">
                        <div class="mb-3">
                            <label class="form-label">Product Code:</label>
                            <input type="text" name="product_code" value="${p.productCode}" class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Name:</label>
                            <input type="text" name="name" value="${p.name}" class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Description:</label>
                            <textarea name="description" class="form-control" required>${p.description}</textarea>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Size:</label>
                            <input type="text" name="size" value="${p.size}" class="form-control">
                        </div>
                    </div>

                    <!-- Right Column -->
                    <div class="col-md-6">
                        <div class="mb-3">
                            <label class="form-label">Color:</label>
                            <input type="text" name="color" value="${p.color}" class="form-control">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Material:</label>
                            <input type="text" name="material" value="${p.material}" class="form-control">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Unit:</label>
                            <input type="text" name="unit" value="${p.unit}" class="form-control">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Supplier:</label>
                            <c:if test="${empty suppliers}">
                                <div class="text-danger">Không có nhà cung cấp nào.</div>
                            </c:if>
                            <c:if test="${not empty suppliers}">
                                <select name="supplier_id" class="form-select" required>
                                    <option value="" disabled>Select a supplier</option>
                                    <c:forEach items="${suppliers}" var="supplier">
                                        <option value="${supplier.supplierId}" ${currentSupplierId == supplier.supplierId ? 'selected' : ''}>${supplier.name}</option>
                                    </c:forEach>
                                </select>
                            </c:if>
                        </div>
                    </div>
                </div>

                <!-- Bottom Section -->
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label">Cost Price:</label>
                        <input type="number" name="cost_price" step="0.01" value="${p.costPrice}" class="form-control" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label">Sale Price:</label>
                        <input type="number" name="sale_price" step="0.01" value="${p.salePrice}" class="form-control" required>
                    </div>
                </div>

                <!-- Action buttons -->
                <div class="text-end">
                    <button type="submit" class="btn btn-primary me-2"><i class="fas fa-save me-1"></i> Update Product</button>
                    <button type="reset" class="btn btn-outline-secondary me-2"><i class="fas fa-redo me-1"></i> Reset</button>
                    <a href="${pageContext.request.contextPath}/viewListProducts" class="btn btn-light"><i class="fas fa-arrow-left me-1"></i> Cancel</a>
                </div>
            </form>
        </div>
    </div>
</div>

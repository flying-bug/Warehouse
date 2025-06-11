<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<%-- File này chỉ chứa nội dung, không có <html>, <head>, <body> --%>

<div class="d-md-flex justify-content-between align-items-center">
    <h5 class="mb-0">Add New Product</h5>
    <nav aria-label="breadcrumb" class="d-inline-block mt-4 mt-sm-0">
        <ul class="breadcrumb bg-transparent rounded mb-0 p-0">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboardAdmin">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/viewListProducts">Products</a></li>
            <li class="breadcrumb-item active" aria-current="page">Add Product</li>
        </ul>
    </nav>
</div>

<div class="row justify-content-center">
    <div class="col-lg-10 mt-4">
        <div class="card border-0 p-4 rounded shadow">
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

            <form class="mt-3" action="${pageContext.request.contextPath}/addProduct" method="post">
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="product_code" class="form-label">Product Code<span class="text-danger">*</span>:</label>
                        <input type="text" id="product_code" name="product_code" class="form-control"
                               placeholder="Enter product code" required
                               value="${product_code}">
                    </div>

                    <div class="col-md-6 mb-3">
                        <label for="name" class="form-label">Product Name<span class="text-danger">*</span>:</label>
                        <input type="text" id="name" name="name" class="form-control"
                               placeholder="Enter product name" required
                               value="${name}">
                    </div>

                    <div class="col-md-12 mb-3">
                        <label for="description" class="form-label">Description:</label>
                        <textarea id="description" name="description" class="form-control"
                                  placeholder="Enter product description" rows="3">${description}</textarea>
                    </div>

                    <div class="col-md-4 mb-3">
                        <label for="size" class="form-label">Size:</label>
                        <input type="text" id="size" name="size" class="form-control"
                               placeholder="Enter product size"
                               value="${size}">
                    </div>

                    <div class="col-md-4 mb-3">
                        <label for="color" class="form-label">Color:</label>
                        <input type="text" id="color" name="color" class="form-control"
                               placeholder="Enter product color"
                               value="${color}">
                    </div>

                    <div class="col-md-4 mb-3">
                        <label for="material" class="form-label">Material:</label>
                        <input type="text" id="material" name="material" class="form-control"
                               placeholder="Enter product material"
                               value="${material}">
                    </div>

                    <div class="col-md-4 mb-3">
                        <label for="unit" class="form-label">Unit<span class="text-danger">*</span>:</label>
                        <input type="text" id="unit" name="unit" class="form-control"
                               placeholder="Enter product unit" required
                               value="${unit}">
                    </div>

                    <div class="col-md-4 mb-3">
                        <label for="cost_price" class="form-label">Cost Price<span class="text-danger">*</span>:</label>
                        <input type="number" step="0.01" min="0" id="cost_price" name="cost_price" class="form-control"
                               placeholder="Enter cost price" required
                               value="${cost_price}">
                    </div>

                    <div class="col-md-4 mb-3">
                        <label for="sale_price" class="form-label">Sale Price<span class="text-danger">*</span>:</label>
                        <input type="number" step="0.01" min="0" id="sale_price" name="sale_price" class="form-control"
                               placeholder="Enter sale price" required
                               value="${sale_price}">
                    </div>

                    <div class="col-md-4 mb-3">
                        <label for="status" class="form-label">Status<span class="text-danger">*</span>:</label>
                        <select id="status" name="status" class="form-select" required>
                            <option value="" disabled selected>Select status</option>
                            <option value="1" ${status == 1 ? 'selected' : ''}>Active</option>
                            <option value="0" ${status == 0 ? 'selected' : ''}>Inactive</option>
                        </select>
                    </div>

                    <div class="col-md-4 mb-3">
                        <label for="image" class="form-label">Image URL:</label>
                        <input type="text" id="image" name="image" class="form-control"
                               placeholder="Enter image URL"
                               value="${image}">
                    </div>

                    <div class="col-md-4 mb-3">
                        <label for="min_stock_level" class="form-label">Minimum Stock Level<span class="text-danger">*</span>:</label>
                        <input type="number" min="0" id="min_stock_level" name="min_stock_level" class="form-control"
                               placeholder="Enter minimum stock level" required
                               value="${min_stock_level}">
                    </div>
                </div>

                <div class="mt-4 text-end">
                    <button type="submit" class="btn btn-primary me-2">
                        <i class="uil uil-save me-1"></i> Save Product
                    </button>
                    <button type="reset" class="btn btn-outline-secondary me-2">
                        <i class="uil uil-redo me-1"></i> Reset
                    </button>
                    <a href="${pageContext.request.contextPath}/viewListProducts" class="btn btn-light">
                        <i class="uil uil-times me-1"></i> Cancel
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>
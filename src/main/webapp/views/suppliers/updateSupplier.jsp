<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<%-- Chỉ chứa nội dung để nhúng vào dashboard.jsp --%>

<div class="d-md-flex justify-content-between align-items-center">
    <h5 class="mb-0"><i class="fas fa-truck"></i> Update Supplier</h5>
    <nav aria-label="breadcrumb" class="d-inline-block mt-4 mt-sm-0">
        <ul class="breadcrumb bg-transparent rounded mb-0 p-0">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboardAdmin">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/viewListSuppliers">Suppliers</a></li>
            <li class="breadcrumb-item active" aria-current="page">Update Supplier</li>
        </ul>
    </nav>
</div>

<div class="row justify-content-center">
    <div class="col-lg-10 mt-4">
        <div class="card border-0 p-4 rounded shadow">

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${errorMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/updateSupplier" method="post" class="mt-3">
                <input type="hidden" name="supplier_id" value="${supplier_id != null ? supplier_id : supplier.supplierId}">

                <div class="row g-4">
                    <div class="col-md-6">
                        <label for="name" class="form-label">Supplier Name<span class="text-danger">*</span>:</label>
                        <input type="text" id="name" name="name"
                               value="${name != null ? name : supplier.name}"
                               class="form-control" placeholder="Enter supplier name" required>
                    </div>

                    <div class="col-md-6">
                        <label for="phone" class="form-label">Phone<span class="text-danger">*</span>:</label>
                        <input type="text" id="phone" name="phone"
                               value="${phone != null ? phone : supplier.phone}"
                               class="form-control" placeholder="Enter supplier phone number" required>
                    </div>

                    <div class="col-12">
                        <label for="address" class="form-label">Address<span class="text-danger">*</span>:</label>
                        <textarea id="address" name="address" class="form-control" rows="3" placeholder="Enter supplier address" required>${address != null ? address : supplier.address}</textarea>
                    </div>
                </div>

                <div class="mt-4 text-end">
                    <button type="submit" class="btn btn-warning me-2">
                        <i class="fas fa-save me-1"></i> Update
                    </button>
                    <button type="reset" class="btn btn-secondary me-2">
                        <i class="fas fa-undo me-1"></i> Reset
                    </button>
                    <a href="${pageContext.request.contextPath}/viewListSuppliers" class="btn btn-danger">
                        <i class="fas fa-times me-1"></i> Cancel
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>

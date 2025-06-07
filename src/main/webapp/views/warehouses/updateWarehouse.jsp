<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<div class="d-md-flex justify-content-between align-items-center">
    <h5 class="mb-0"><i class="fas fa-warehouse"></i> Update Warehouse</h5>
    <nav aria-label="breadcrumb" class="d-inline-block mt-4 mt-sm-0">
        <ul class="breadcrumb bg-transparent rounded mb-0 p-0">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboardAdmin">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/viewListWarehouses">Warehouses</a></li>
            <li class="breadcrumb-item active" aria-current="page">Update</li>
        </ul>
    </nav>
</div>

<div class="row justify-content-center">
    <div class="col-lg-8 mt-4">
        <div class="card border-0 p-4 rounded shadow">

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${errorMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/updateWarehouse" method="post">
                <input type="hidden" name="warehouseId" value="${warehouse.warehouseId}" />

                <div class="mb-3">
                    <label for="name" class="form-label">Warehouse Name<span class="text-danger">*</span>:</label>
                    <input type="text" id="name" name="name" class="form-control" value="${warehouse.name}" required>
                </div>

                <div class="mb-3">
                    <label for="location" class="form-label">Warehouse Location<span class="text-danger">*</span>:</label>
                    <input type="text" id="location" name="location" class="form-control" value="${warehouse.location}" required>
                </div>

                <div class="mt-4 text-end">
                    <button type="submit" class="btn btn-warning me-2"><i class="fas fa-save me-1"></i> Update</button>
                    <button type="reset" class="btn btn-secondary me-2"><i class="fas fa-undo me-1"></i> Reset</button>
                    <a href="${pageContext.request.contextPath}/viewListWarehouses" class="btn btn-danger">
                        <i class="fas fa-times me-1"></i> Cancel
                    </a>
                </div>
            </form>

        </div>
    </div>
</div>

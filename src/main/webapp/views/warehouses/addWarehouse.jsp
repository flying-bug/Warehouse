<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<%-- FILE NÀY CHỈ CHỨA NỘI DUNG FORM, KHÔNG CHỨA LAYOUT --%>

<div class="d-md-flex justify-content-between align-items-center">
    <h5 class="mb-0">Add New Warehouse</h5>
    <nav aria-label="breadcrumb" class="d-inline-block mt-4 mt-sm-0">
        <ul class="breadcrumb bg-transparent rounded mb-0 p-0">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboardAdmin">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/viewListWarehouses">Warehouses</a></li>
            <li class="breadcrumb-item active" aria-current="page">Add Warehouse</li>
        </ul>
    </nav>
</div>

<div class="row justify-content-center">
    <div class="col-lg-8 mt-4">
        <div class="card border-0 p-4 rounded shadow">

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <c:out value="${errorMessage}" />
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/addWarehouse" method="post">
                <div class="row">
                    <div class="col-md-6">
                        <div class="mb-3">
                            <label for="name" class="form-label">Warehouse Name <span class="text-danger">*</span></label>
                            <input type="text" id="name" name="name" class="form-control"
                                   placeholder="Enter warehouse name"
                                   value="${name != null ? name : ''}"
                                   required>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="mb-3">
                            <label for="location" class="form-label">Warehouse Location <span class="text-danger">*</span></label>
                            <input type="text" id="location" name="location" class="form-control"
                                   placeholder="Enter warehouse location"
                                   value="${location != null ? location : ''}"
                                   required>
                        </div>
                    </div>
                </div>

                <div class="text-end mt-4">
                    <button type="submit" class="btn btn-primary"><i class="uil uil-save me-1"></i> Add Warehouse</button>
                    <button type="reset" class="btn btn-outline-secondary mx-2"><i class="uil uil-redo me-1"></i> Reset</button>
                    <a href="${pageContext.request.contextPath}/viewListWarehouses" class="btn btn-light">
                        <i class="uil uil-times me-1"></i> Cancel
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>
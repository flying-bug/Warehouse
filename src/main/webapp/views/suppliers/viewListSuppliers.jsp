<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<div class="d-md-flex justify-content-between align-items-center">
    <h5 class="mb-0">List of Suppliers</h5>
    <a href="${pageContext.request.contextPath}/addSupplier" class="btn btn-primary">
        <i class="uil uil-plus"></i> Add New Supplier
    </a>
</div>

<div class="row mt-4">
    <div class="col-12">
        <div class="table-responsive shadow rounded">
            <table class="table table-bordered bg-white mb-0 text-center align-middle">
                <thead>
                <tr>
                    <th>Supplier ID</th>
                    <th>Name</th>
                    <th>Phone</th>
                    <th>Address</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty suppliers}">
                        <c:forEach var="supplier" items="${suppliers}">
                            <tr>
                                <td>${supplier.supplierId}</td>
                                <td>${supplier.name}</td>
                                <td>${supplier.phone}</td>
                                <td>${supplier.address}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/updateSupplier?supplier_id=${supplier.supplierId}" class="btn btn-sm btn-soft-success">
                                        <i class="uil uil-pen"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/deleteSupplier?supplier_id=${supplier.supplierId}" class="btn btn-sm btn-soft-danger"
                                       onclick="return confirm('Are you sure you want to delete supplier ID: ${supplier.supplierId}?');">
                                        <i class="uil uil-trash"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="5" class="text-danger">No suppliers found!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

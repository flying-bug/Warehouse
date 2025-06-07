<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<div class="d-md-flex justify-content-between align-items-center">
    <h5 class="mb-0">List of Warehouses</h5>
    <a href="${pageContext.request.contextPath}/addWarehouse" class="btn btn-primary">
        <i class="uil uil-plus"></i> Add New Warehouse
    </a>
</div>

<div class="row mt-4">
    <div class="col-12">
        <div class="table-responsive shadow rounded">
            <table class="table table-bordered bg-white mb-0 text-center align-middle">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Location</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty warehouseList}">
                        <c:forEach var="warehouse" items="${warehouseList}">
                            <tr>
                                <td>${warehouse.warehouseId}</td>
                                <td>${warehouse.name}</td>
                                <td>${warehouse.location}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/updateWarehouse?id=${warehouse.warehouseId}" class="btn btn-sm btn-soft-success">
                                        <i class="uil uil-pen"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/deleteWarehouse?id=${warehouse.warehouseId}" class="btn btn-sm btn-soft-danger"
                                       onclick="return confirm('Are you sure delete Warehouse ID: ${warehouse.warehouseId}?')">
                                        <i class="uil uil-trash"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="4" class="text-danger">No warehouses found.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<div class="d-md-flex justify-content-between align-items-center">
    <h5 class="mb-0">Product List</h5>
    <a href="${pageContext.request.contextPath}/addProduct" class="btn btn-primary">
        <i class="uil uil-plus"></i> Add New Product
    </a>
</div>

<div class="row mt-4">
    <div class="col-12">
        <form method="get" action="${pageContext.request.contextPath}/filterListProducts" class="row g-3 align-items-end">
            <div class="col-md-3">
                <label class="form-label">Product Code</label>
                <input type="text" name="productCode" class="form-control" value="${param.productCode}"/>
            </div>
            <div class="col-md-3">
                <label class="form-label">Material</label>
                <input type="text" name="material" class="form-control" value="${param.material}"/>
            </div>
            <div class="col-md-2">
                <label class="form-label">Cost Price From</label>
                <input type="number" step="0.01" name="costPrice" class="form-control" value="${param.costPrice}"/>
            </div>
            <div class="col-md-2">
                <label class="form-label">Cost Price To</label>
                <input type="number" step="0.01" name="salePrice" class="form-control" value="${param.salePrice}"/>
            </div>
            <div class="col-md-2">
                <label class="form-label">Status</label>
                <select name="status" class="form-select">
                    <option value="" ${empty param.status ? 'selected' : ''}>All</option>
                    <option value="1" ${param.status == '1' ? 'selected' : ''}>Active</option>
                    <option value="0" ${param.status == '0' ? 'selected' : ''}>Inactive</option>
                </select>
            </div>
            <div class="col-md-12">
                <button type="submit" class="btn btn-outline-primary">Filter</button>
            </div>
        </form>
    </div>
</div>

<div class="row mt-4">
    <div class="col-12">
        <div class="table-responsive shadow rounded">
            <table class="table table-bordered bg-white mb-0 text-center align-middle">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Product Code</th>
                    <th>Product Name</th>
                    <th>Material</th>
                    <th>Cost Price</th>
                    <th>Sale Price</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty productList}">
                        <c:forEach var="prod" items="${productList}">
                            <tr>
                                <td>${prod.productId}</td>
                                <td>${prod.productCode}</td>
                                <td>${prod.name}</td>
                                <td>${prod.material}</td>
                                <td>$${prod.costPrice}</td>
                                <td>$${prod.salePrice}</td>
                                <td>${prod.status == 1 ? 'Active' : 'Inactive'}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/updateProduct?productId=${prod.productId}" class="btn btn-sm btn-soft-success">
                                        <i class="uil uil-pen"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/deleteProduct?productId=${prod.productId}" class="btn btn-sm btn-soft-danger"
                                       onclick="return confirm('Are you sure you want to set product ID: ${prod.productId} to inactive?')">
                                        <i class="uil uil-trash"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/viewProductDetail?productId=${prod.productId}" class="btn btn-sm btn-soft-warning" title="View details">
                                        <i class="uil uil-eye"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="8" class="text-danger">No products found.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%-- Pagination --%>
<c:if test="${totalPages > 1}">
    <div class="row mt-4">
        <div class="col-12 d-flex justify-content-center">
            <ul class="pagination">
                <c:if test="${currentPage > 1}">
                    <li class="page-item">
                        <a class="page-link" href="${pageContext.request.contextPath}/filterListProducts?page=${currentPage - 1}&productCode=${param.productCode}&material=${param.material}&costPrice=${param.costPrice}&salePrice=${param.salePrice}&status=${param.status}">« Previous</a>
                    </li>
                </c:if>
                <c:forEach var="i" begin="1" end="${totalPages}">
                    <li class="page-item ${currentPage == i ? 'active' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/filterListProducts?page=${i}&productCode=${param.productCode}&material=${param.material}&costPrice=${param.costPrice}&salePrice=${param.salePrice}&status=${param.status}">${i}</a>
                    </li>
                </c:forEach>
                <c:if test="${currentPage < totalPages}">
                    <li class="page-item">
                        <a class="page-link" href="${pageContext.request.contextPath}/filterListProducts?page=${currentPage + 1}&productCode=${param.productCode}&material=${param.material}&costPrice=${param.costPrice}&salePrice=${param.salePrice}&status=${param.status}">Next »</a>
                    </li>
                </c:if>
            </ul>
        </div>
    </div>
</c:if>
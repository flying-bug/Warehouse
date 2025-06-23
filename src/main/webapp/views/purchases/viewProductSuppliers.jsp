<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<h5 class="mb-4">View Suppliers by Product</h5>

<form method="get" action="viewProductSuppliers" class="row g-3 mb-4">
    <div class="col-md-8">
        <label class="form-label">Select Product</label>
        <select name="productId" class="form-select" required>
            <option value="">-- Choose Product --</option>
            <c:forEach var="p" items="${products}">
                <option value="${p.productId}"
                        <c:if test="${p.productId == selectedProductId}">selected</c:if>>
                        ${p.productCode} - ${p.name}
                </option>
            </c:forEach>
        </select>
    </div>
    <div class="col-md-4 align-self-end">
        <button type="submit" class="btn btn-primary">View</button>
    </div>
</form>

<c:if test="${not empty product}">
    <h6 class="mb-3">Suppliers of Product: <span class="text-primary">${product.name}</span></h6>

    <c:choose>
        <c:when test="${not empty suppliers}">
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>#</th>
                    <th>Supplier Name</th>
                    <th>Phone</th>
                    <th>Email</th>
                    <th>Address</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="s" items="${suppliers}" varStatus="loop">
                    <tr>
                        <td>${loop.index + 1}</td>
                        <td>${s.name}</td>
                        <td>${s.phone}</td>
                        <td>${s.email}</td>
                        <td>${s.address}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info">No suppliers found for this product.</div>
        </c:otherwise>
    </c:choose>
</c:if>

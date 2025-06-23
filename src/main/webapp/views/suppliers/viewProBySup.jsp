<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<h5 class="mb-4">Products by Supplier</h5>

<form method="post" action="<%= request.getContextPath() %>/viewProductSuppliers" class="row align-items-end g-3">
    <div class="col-md-8">
        <label class="form-label">Select Supplier</label>
        <select name="supplierId" class="form-select" required>
            <c:forEach var="s" items="${supplierList}">
                <option value="${s.supplierId}" ${s.supplierId == selectedSupplierId ? 'selected' : ''}>
                        ${s.name}
                </option>
            </c:forEach>
        </select>
    </div>
    <div class="col-md-4 text-end">
        <button type="submit" class="btn btn-primary mt-2">View Products</button>
    </div>
</form>


<hr>

<c:if test="${not empty products}">
    <h6>Supplier: <span class="text-primary">${supplier.name}</span></h6>
    <table class="table table-bordered mt-3">
        <thead>
        <tr>
            <th>#</th>
            <th>Product Code</th>
            <th>Name</th>
            <th>Unit</th>
            <th>Status</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="p" items="${products}" varStatus="loop">
            <tr>
                <td>${loop.index + 1}</td>
                <td>${p.productCode}</td>
                <td>${p.name}</td>
                <td>${p.unit}</td>
                <td>
                    <c:choose>
                        <c:when test="${p.status == 1}">
                            <span class="text-success">Active</span>
                        </c:when>
                        <c:otherwise>
                            <span class="text-muted">Inactive</span>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>

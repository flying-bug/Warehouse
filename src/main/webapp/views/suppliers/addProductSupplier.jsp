<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<!-- Select2 CSS -->
<link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
<!-- jQuery + Select2 JS -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>

<h5 class="mb-4">Add Product - Supplier Link</h5>

<c:if test="${param.success == 'true'}">
    <div class="alert alert-success">Inserted successfully!</div>
</c:if>
<c:if test="${param.error == 'exists'}">
    <div class="alert alert-warning">This product is already linked to the selected supplier!</div>
</c:if>

<form method="post" action="productSupplier" class="row g-3">
    <div class="col-md-6">
        <label class="form-label">Product</label>
        <select name="productId" class="form-select select2" required>
            <c:forEach var="p" items="${products}">
                <option value="${p.productId}">${p.productCode} - ${p.name}</option>
            </c:forEach>
        </select>
    </div>

    <div class="col-md-6">
        <label class="form-label">Supplier</label>
        <select name="supplierId" class="form-select select2" required>
            <c:forEach var="s" items="${suppliers}">
                <option value="${s.supplierId}">${s.name}</option>
            </c:forEach>
        </select>
    </div>

    <div class="col-md-4">
        <label class="form-label">Delivery Duration (days)</label>
        <input type="number" name="deliveryDuration" class="form-control" required>
    </div>

    <div class="col-md-4">
        <label class="form-label">Estimated Price</label>
        <input type="number" name="estimatedPrice" class="form-control" step="0.01" required>
    </div>

    <div class="col-md-12">
        <label class="form-label">Policies</label>
        <textarea name="policies" class="form-control" rows="3"></textarea>
    </div>

    <div class="col-12">
        <button type="submit" class="btn btn-primary">Save</button>
    </div>
</form>

<!-- Optional second success alert -->
<c:if test="${param.success == 'true'}">
    <div class="alert alert-success mt-3">Link added successfully!</div>
</c:if>

<!-- Initialize Select2 -->
<script>
    $(document).ready(function () {
        $('.select2').select2({
            placeholder: "Select an option",
            allowClear: true,
            width: '100%'
        });
    });
</script>

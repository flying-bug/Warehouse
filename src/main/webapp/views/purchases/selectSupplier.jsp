<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<!-- jQuery & Select2 -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet"/>
<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>

<div>
    <h5 class="mb-3">Create Import Order</h5>
    <c:if test="${not empty success}">
        <div class="alert alert-success">${success}</div>
    </c:if>

    <c:if test="${not empty fail}">
        <div class="alert alert-danger">${fail}</div>
    </c:if>

    <form action="selectSupplier" method="post">
        <!-- Supplier Info -->
        <div class="row mb-3">
            <div class="col-md-4">
                <label>Supplier:</label>
                <select class="form-select" name="supplierId" id="supplierSelect" required>
                    <option value="">-- Select Supplier --</option>
                    <c:forEach var="s" items="${supplierList}">
                        <option value="${s.supplierId}">${s.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-4">
                <label>Warehouse:</label>
                <select class="form-select" name="warehouseId" required>
                    <option value="">-- Select Warehouse --</option>
                    <c:forEach var="wh" items="${warehouseList}">
                        <option value="${wh.warehouseId}">${wh.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <!-- Add Product -->
        <div class="row mb-3">
            <div class="col-md-4">
                <label>Product:</label>
                <select class="form-select" id="selectProduct">
                    <option value="">-- Select Product --</option>
                    <c:forEach var="p" items="${productList}">
                        <option value="${p.productId}" data-cost="${p.costPrice}">${p.name} (${p.productCode})</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-2 align-self-end">
                <button type="button" class="btn btn-success" onclick="addProductRow()">Add</button>
            </div>
        </div>

        <!-- Import Table -->
        <div class="table-responsive mb-3">
            <table class="table table-bordered" id="importTable">
                <thead>
                <tr>
                    <th>Product</th>
                    <th>Cost Price</th>
                    <th>Quantity</th>
                    <th>Total</th>
                    <th></th>
                </tr>
                </thead>
                <tbody></tbody>
            </table>
        </div>

        <!-- Notes -->
        <div class="row mb-3">
            <div class="col-md-12">
                <label>Note:</label>
                <textarea class="form-control" name="note" rows="3"></textarea>
            </div>
        </div>

        <div class="row">
            <div class="col-md-6">
                <h5>Total Cost: <span id="grandTotal">0.00</span> ₫</h5>
            </div>
            <div class="col-md-6 text-end">
                <button type="submit" class="btn btn-primary">Submit Import</button>
            </div>
        </div>
    </form>
</div>

<script>
    $('#supplierSelect').select2({ placeholder: "-- Select Supplier --", width: '100%' });
    $('#selectProduct').select2({ placeholder: "-- Select Product --", width: '100%' });

    function addProductRow() {
        const supplierId = $('#supplierSelect').val();
        const productSelect = $('#selectProduct');
        const productId = productSelect.val();
        const productText = productSelect.find('option:selected').text();

        if (!supplierId || !productId) {
            alert("Vui lòng chọn nhà cung cấp và sản phẩm.");
            return;
        }

        // ✅ Kiểm tra sản phẩm đã được thêm chưa
        const isDuplicate = $('#importTable input[name="productIds[]"]').toArray()
            .some(input => input.value === productId);
        if (isDuplicate) {
            alert("❗ Sản phẩm này đã được thêm vào danh sách.");
            return;
        }

        // Gọi Ajax để lấy giá estimated từ bảng product_suppliers
        $.ajax({
            url: '/why/getEstimatedPrice',
            method: 'GET',
            data: {
                supplierId: supplierId,
                productId: productId
            },
            success: function (data) {
                const cost = parseFloat(data.estimatedPrice) || 0;

                const row =
                    '<tr>' +
                    '<td><input type="hidden" name="productIds[]" value="' + productId + '" />' + productText + '</td>' +
                    '<td class="cost-price">' + cost.toFixed(2) + '</td>' +
                    '<td><input type="number" name="quantities[]" class="form-control" value="1" min="1" onchange="updateRowTotal(this)"></td>' +
                    '<td class="total">' + cost.toFixed(2) + '</td>' +
                    '<td><button type="button" class="btn btn-danger btn-sm" onclick="removeRow(this)">X</button></td>' +
                    '</tr>';

                $('#importTable tbody').append(row);
                $('#selectProduct').val(null).trigger('change');
                updateGrandTotal();
            },
            error: function (xhr) {
                if (xhr.status === 404) {
                    alert("❌ Sản phẩm không được cung cấp bởi nhà cung cấp đã chọn.");
                } else {
                    alert("⚠️ Đã xảy ra lỗi khi lấy giá nhập.");
                }
            }
        });
    }


    function updateRowTotal(input) {
        const row = $(input).closest('tr');
        const cost = parseFloat(row.find('.cost-price').text());
        const qty = parseInt(input.value) || 0;
        row.find('.total').text((cost * qty).toFixed(2));
        updateGrandTotal();
    }

    function removeRow(btn) {
        $(btn).closest('tr').remove();
        updateGrandTotal();
    }

    function updateGrandTotal() {
        let total = 0;
        $('#importTable tbody tr .total').each(function () {
            total += parseFloat($(this).text()) || 0;
        });
        $('#grandTotal').text(total.toFixed(2));
    }
</script>


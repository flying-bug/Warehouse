<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<!-- jQuery & Select2 -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet"/>
<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>

<div>
    <div class="d-md-flex justify-content-between align-items-center mb-3">
        <h5 class="mb-0">Create Sale Order</h5>
    </div>
    <c:if test="${not empty success}">
        <div class="alert alert-success">${success}</div>
    </c:if>

    <c:if test="${not empty fail}">
        <div class="alert alert-danger">${fail}</div>
    </c:if>

    <form action="createRequestProduct" method="post">
        <!-- Customer Info -->
        <div class="row mb-3">
            <div class="col-md-3">
                <label>Customer Name:</label>
                <input type="text" class="form-control" name="customerName" required>
            </div>
            <div class="col-md-3">
                <label>Phone:</label>
                <input type="text" class="form-control" name="customerPhone" required>
            </div>
            <div class="col-md-3">
                <label>Email:</label>
                <input type="email" class="form-control" name="customerEmail">
            </div>
        </div>

        <div class="row mb-3">
            <div class="col-md-3">
                <label>Address:</label>
                <textarea class="form-control" name="customerAddress" rows="2"></textarea>
            </div>
            <div class="col-md-6">
                <label>Customer Note:</label>
                <textarea class="form-control" name="customerNote" rows="2"></textarea>
            </div>
        </div>
        <hr>
        <br>
        <!-- Section: Add Product -->
        <div class="row mb-4">
            <div class="col-md-3">
                <label>Warehouse:</label>
                <select id="selectWarehouse" class="form-select">
                    <option value="">-- Select Warehouse --</option>
                    <c:forEach var="wh" items="${warehouseList}">
                        <option value="${wh.warehouseId}">${wh.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-3">
                <label>Product:</label>
                <select id="selectProduct" class="form-select">
                    <option value="">-- Select Product --</option>
                    <c:forEach var="p" items="${productList}">
                        <option value="${p.productId}" data-price="${p.salePrice}">${p.name} (${p.productCode})</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-2 align-self-end">
                <button type="button" class="btn btn-success" onclick="fetchAvailableAndAddRow()">Add</button>
            </div>
        </div>

        <!-- Order Table -->
        <div class="table-responsive shadow rounded mb-3">
            <table class="table table-bordered" id="orderTable">
                <thead>
                <tr>
                    <th>Warehouse</th>
                    <th>Product</th>
                    <th>Unit Price</th>
                    <th>Available</th>
                    <th>Quantity</th>
                    <th>Total</th>
                    <th></th>
                </tr>
                </thead>
                <tbody></tbody>
            </table>
        </div>

        <!-- Order Note -->
        <div class="row mb-3">
            <div class="col-md-6">
                <label>Reason for Export:</label>
                <input type="text" class="form-control" name="reason">
            </div>
            <div class="col-md-12">
                <label>Order Note:</label>
                <textarea class="form-control" name="note" rows="3"></textarea>
            </div>
        </div>

        <div class="row">
            <div class="col-md-6 text-start">
                <h5>Total Amount: <span id="grandTotal">0.00</span> $</h5>
            </div>
            <div class="col-md-6 text-end">
                <button type="submit" class="btn btn-primary" onclick="return validateForm()">Submit Order</button>
            </div>
        </div>

    </form>
</div>

<!-- JS Logic -->
<script>
    $('#selectWarehouse').select2({ placeholder: "-- Select Warehouse --", width: '100%' });
    $('#selectProduct').select2({ placeholder: "-- Select Product --", width: '100%' });

    function fetchAvailableAndAddRow() {
        const warehouseId = Number($('#selectWarehouse').val());
        const productId = Number($('#selectProduct').val());

        if (isProductAlreadySelected(productId, warehouseId)) {
            alert("This product is already selected for the same warehouse.");
            return;
        }

        console.log("🧪 warehouseId =", warehouseId, "productId =", productId);

        if (!warehouseId || !productId || isNaN(warehouseId) || isNaN(productId)) {
            alert("Please select a valid warehouse and product.");
            return;
        }

        if (isProductAlreadySelected(productId)) {
            alert("This product is already in the list.");
            return;
        }

        fetch(`/why/getInventoryQuantity?warehouseId=`+ warehouseId + `&productId=`+ productId)
            .then(response => response.json())
            .then(data => {
                console.log("👉 Response:", data);
                console.log("📦 typeof quantity:", typeof data.quantity);

                if (data && typeof data.quantity === 'number') {
                    if (data.quantity === 0) {
                        alert("This product is currently out of stock in the selected warehouse.");
                        return;
                    }
                    addSelectedProduct(data.quantity);
                    $('#selectWarehouse').val(null).trigger('change');
                    $('#selectProduct').val(null).trigger('change');
                } else {
                    alert("Could not retrieve available quantity.");
                }
            })
            .catch(error => {
                console.error("Fetch error:", error);
                alert("Error fetching available quantity.");
            });
    }

    function isProductAlreadySelected(productId, warehouseId) {
        const rows = document.querySelectorAll('#orderTable tbody tr');
        for (const row of rows) {
            const existingProductId = row.querySelector('input[name="productIds[]"]').value;
            const existingWarehouseId = row.querySelector('input[name="warehouseIds[]"]').value;
            if (existingProductId === productId && existingWarehouseId === warehouseId) {
                return true;
            }
        }
        return false;
    }
    function updateGrandTotal() {
        let grandTotal = 0;
        document.querySelectorAll('#orderTable tbody tr .total').forEach(td => {
            grandTotal += parseFloat(td.innerText) || 0;
        });
        document.getElementById('grandTotal').innerText = grandTotal.toFixed(2);
    }



    function isProductAlreadySelected(productId) {
        const existingIds = Array.from(document.querySelectorAll('input[name="productIds[]"]'))
            .map(input => input.value);
        return existingIds.includes(productId.toString());
    }

    function addSelectedProduct(availableQuantity) {
        const warehouseId = $('#selectWarehouse').val();
        const warehouseText = $('#selectWarehouse').find('option:selected').text();
        const productId = $('#selectProduct').val();
        const productOption = $('#selectProduct').find('option:selected');
        const productText = productOption.text();
        const price = parseFloat(productOption.data('price')) || 0;
        const quantity = 1;
        const total = (price * quantity).toFixed(2);
        const tableBody = document.querySelector("#orderTable tbody");

        const row = document.createElement("tr");
        row.innerHTML =
            '<td><input type="hidden" name="warehouseIds[]" value="' + warehouseId + '">' + warehouseText + '</td>' +
            '<td><input type="hidden" name="productIds[]" value="' + productId + '">' + productText + '</td>' +
            '<td class="unit-price">' + price.toFixed(2) + '</td>' +
            '<td class="available">' + availableQuantity + '</td>' +
            '<td><input type="number" name="quantities[]" class="form-control" value="' + quantity + '" min="1" onchange="updateTotal(this)"></td>' +
            '<td class="total">' + total + '</td>' +
            '<td><button type="button" class="btn btn-danger btn-sm" onclick="removeRow(this)">-</button></td>';

        tableBody.appendChild(row);
        updateGrandTotal();
    }


    function updateTotal(input) {
        const row = input.closest('tr');
        const price = parseFloat(row.querySelector('.unit-price').innerText);
        const qty = parseInt(input.value) || 0;
        row.querySelector('.total').innerText = (price * qty).toFixed(2);
        updateGrandTotal();
    }

    function removeRow(button) {
        button.closest('tr').remove();
        updateGrandTotal();
    }

    function validateForm() {
        let valid = true;
        document.querySelectorAll('#orderTable tbody tr').forEach(row => {
            const qty = parseInt(row.querySelector('input[name="quantities[]"]').value) || 0;
            const available = parseInt(row.querySelector('.available').innerText);
            if (qty > available) {
                alert("Quantity exceeds available stock.");
                valid = false;
            }
        });
        return valid;
    }
</script>

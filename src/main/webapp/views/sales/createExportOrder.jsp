<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />

<div class="layout-spacing">
    <div class="d-md-flex justify-content-between align-items-center mb-4">
        <h5 class="mb-0">Create Sale Order</h5>
    </div>

    <form action="createExportOrder" method="post" class="needs-validation" novalidate>
        <!-- Customer Information -->
        <div class="row g-3 mb-4">
            <div class="col-md-3">
                <label for="customerName" class="form-label">Customer Name:</label>
                <input type="text" class="form-control" id="customerName" name="customerName" required>
                <div class="invalid-feedback">Please enter customer name.</div>
            </div>
            <div class="col-md-3">
                <label for="customerPhone" class="form-label">Phone:</label>
                <input type="text" class="form-control" id="customerPhone" name="customerPhone" required>
                <div class="invalid-feedback">Please enter phone number.</div>
            </div>
            <div class="col-md-3">
                <label for="customerEmail" class="form-label">Email:</label>
                <input type="email" class="form-control" id="customerEmail" name="customerEmail">
            </div>
            <div class="col-md-3">
                <label for="customerAddress" class="form-label">Address:</label>
                <textarea class="form-control" id="customerAddress" name="customerAddress" rows="1"></textarea>
            </div>
        </div>
        <div class="row g-3 mb-4">
            <div class="col-md-6">
                <label for="customerNote" class="form-label">Customer Note:</label>
                <textarea class="form-control" id="customerNote" name="customerNote" rows="2"></textarea>
            </div>
            <div class="col-md-6">
                <label for="reason" class="form-label">Reason for Export:</label>
                <input type="text" class="form-control" id="reason" name="reason">
            </div>
        </div>

        <!-- Product Table -->
        <div class="table-responsive shadow rounded mb-4">
            <table class="table table-bordered table-striped" id="orderTable">
                <thead class="table-light">
                <tr>
                    <th>Warehouse</th>
                    <th>Product</th>
                    <th>Unit Price</th>
                    <th>Available</th>
                    <th>Quantity</th>
                    <th>Total</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>
                        <select class="form-select warehouse-select" name="warehouseIds[]" onchange="updateProductOptions(this)" required>
                            <option value="" selected>-- Select Warehouse --</option>
                            <c:forEach var="wh" items="${warehouseList}">
                                <option value="${wh.warehouseId}">${wh.name}</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td>
                        <select name="productIds[]" class="form-select product-select" onchange="handleProductChange(this)" required>
                            <option value="" disabled selected>-- Select Product --</option>
                        </select>
                    </td>
                    <td class="unit-price text-end">0.00</td>
                    <td class="available text-center">-</td>
                    <td><input type="number" name="quantities[]" class="form-control quantity-input" min="1" value="1" onchange="updateTotal(this)" required></td>
                    <td class="total text-end">0.00</td>
                    <td>
                        <button type="button" class="btn btn-success btn-sm" onclick="addRow()">+</button>
                        <button type="button" class="btn btn-danger btn-sm" onclick="removeRow(this)">-</button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <!-- Order Note -->
        <div class="mb-4">
            <label for="orderNote" class="form-label">Order Note:</label>
            <textarea class="form-control" id="orderNote" name="note" rows="3"></textarea>
        </div>

        <div class="text-end">
            <button type="submit" class="btn btn-primary">Submit Order</button>
        </div>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
    const inventoryData = ${inventoryJson != null ? inventoryJson : '[]'};
    const allProducts = ${productJson != null ? productJson : '[]'};

    // Bootstrap validation
    (function () {
        'use strict';
        var forms = document.querySelectorAll('.needs-validation');
        Array.prototype.slice.call(forms).forEach(function (form) {
            form.addEventListener('submit', function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            }, false);
        });
    })();


    function updateProductOptions(warehouseSelect) {
        const row = warehouseSelect.closest('tr');
        const productSelect = row.querySelector('select[name="productIds[]"]');
        const warehouseId = warehouseSelect.value;
        populateProductOptions(productSelect, warehouseId);
        updateProductInfo(productSelect);
    }

    function updateProductInfo(select) {
        const row = select.closest('tr');
        const warehouseSelect = row.querySelector('select[name="warehouseIds[]"]');
        const selectedOption = select.options[select.selectedIndex];
        const price = parseFloat(selectedOption?.getAttribute('data-price') || 0);
        row.querySelector('.unit-price').innerText = price.toFixed(2);

        const productId = select.value;
        const warehouseId = warehouseSelect.value;
        let available = '-';
        if (productId && warehouseId) {
            const inventory = inventoryData.find(item => item.product.productId == productId && item.warehouseId == warehouseId);
            available = inventory ? inventory.inventoryQuantity : 0;
        }
        row.querySelector('.available').innerText = available > 0 ? available : '-';
        updateTotal(row.querySelector('input[name="quantities[]"]'));
    }

    function updateTotal(input) {
        const row = input.closest('tr');
        const price = parseFloat(row.querySelector('.unit-price').innerText) || 0;
        const qty = parseInt(input.value) || 0;
        row.querySelector('.total').innerText = (price * qty).toFixed(2);
    }

    function getSelectedProductIds() {
        const selected = [];
        document.querySelectorAll('select[name="productIds[]"]').forEach(s => {
            if (s.value) selected.push(s.value);
        });
        return selected;
    }

    function handleProductChange(select) {
        const selectedValue = select.value;
        let isDuplicate = false;
        document.querySelectorAll('select[name="productIds[]"]').forEach(s => {
            if (s !== select && s.value === selectedValue) {
                isDuplicate = true;
            }
        });

        if (isDuplicate) {
            alert("This product is already selected in another row.");
            select.value = "";
            $(select).trigger("change.select2");
            updateProductInfo(select);
            return;
        }

        updateProductInfo(select);
    }

    function validateForm() {
        let valid = true;
        document.querySelectorAll('tr').forEach(row => {
            const warehouseSelect = row.querySelector('select[name="warehouseIds[]"]');
            const productSelect = row.querySelector('select[name="productIds[]"]');
            const qtyInput = row.querySelector('input[name="quantities[]"]');
            if (warehouseSelect && productSelect && qtyInput) {
                if (!warehouseSelect.value || !productSelect.value) {
                    alert("Please select both a warehouse and a product for each row.");
                    valid = false;
                } else {
                    const productId = productSelect.value;
                    const warehouseId = warehouseSelect.value;
                    const qty = parseInt(qtyInput.value) || 0;
                    const inventory = inventoryData.find(item => item.product.productId == productId && item.warehouseId == warehouseId);
                    const available = inventory ? inventory.inventoryQuantity : 0;
                    if (qty > available) {
                        alert(`Requested quantity (${qty}) exceeds available stock (${available}) for ${productSelect.options[productSelect.selectedIndex].text}.`);
                        valid = false;
                    }
                }
            }
        });
        return valid;
    }

    function initializeSelect2() {
        $('.product-select').select2({
            placeholder: "-- Select Product --",
            width: '100%'
        }).off('change').on('change', function () {
            handleProductChange(this);
        });

        $('.warehouse-select').select2({
            placeholder: "-- Select Warehouse --",
            width: '100%'
        }).off('change').on('change', function () {
            updateProductOptions(this);
        });
    }

    function addRow() {
        const table = document.getElementById('orderTable').querySelector('tbody');
        const firstRow = table.querySelector('tr');
        const newRow = firstRow.cloneNode(true);

        const oldWarehouseSelect = newRow.querySelector('select[name="warehouseIds[]"]');
        const clonedWarehouseSelect = oldWarehouseSelect.cloneNode(true);
        oldWarehouseSelect.parentNode.replaceChild(clonedWarehouseSelect, oldWarehouseSelect);
        clonedWarehouseSelect.classList.add('warehouse-select');

        const oldProductSelect = newRow.querySelector('select[name="productIds[]"]');
        const clonedProductSelect = oldProductSelect.cloneNode(true);
        oldProductSelect.parentNode.replaceChild(clonedProductSelect, oldProductSelect);
        clonedProductSelect.classList.add('product-select');
        clonedProductSelect.innerHTML = '<option value="" disabled selected>-- Select Product --</option>';

        newRow.querySelectorAll('input').forEach(input => input.value = 1);
        newRow.querySelector('.unit-price').innerText = '0.00';
        newRow.querySelector('.available').innerText = '-';
        newRow.querySelector('.total').innerText = '0.00';

        table.appendChild(newRow);
        initializeSelect2();
    }

    function removeRow(button) {
        const row = button.closest('tr');
        const table = row.parentNode;
        if (table.rows.length > 1) {
            row.remove();
        }
    }

    document.addEventListener('DOMContentLoaded', () => {
        initializeSelect2();
    });

    function populateProductOptions(select, warehouseId) {
        select.innerHTML = '<option value="" disabled selected>-- Select Product --</option>';
        if (!warehouseId) {
            console.warn('No warehouse selected');
            $(select).trigger('change.select2');
            return;
        }

        const filteredInventory = inventoryData.filter(item =>
            item.product &&
            item.inventoryQuantity > 0 &&
            item.warehouseId == warehouseId
        );
        if (filteredInventory.length === 0) {
            console.warn('No products available for warehouse ID:', warehouseId);
            $(select).trigger('change.select2');
            return;
        }

        const usedProductIds = getSelectedProductIds();
        const alreadyUsed = new Set(usedProductIds);

        filteredInventory.forEach(item => {
            const prod = item.product;
            if (!alreadyUsed.has(prod.productId.toString())) {
                const option = document.createElement('option');
                option.value = prod.productId;
                option.text = `${prod.name} (${prod.productCode})`;
                option.setAttribute('data-price', prod.salePrice || 0);
                select.appendChild(option);
            }
        });

        $(select).trigger('change.select2');
    }
</script>
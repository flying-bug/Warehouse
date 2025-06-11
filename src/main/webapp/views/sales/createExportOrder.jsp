<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet"/>

<div class="layout-specing">
    <div class="d-md-flex justify-content-between align-items-center mb-3">
        <h5 class="mb-0">Create Sale Order</h5>
    </div>

    <form action="createExportOrder" method="post">
        <!-- Customer Information -->
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
            <div class="col-md-3">
                <label>Warehouse:</label>
                <select class="form-select" name="warehouseId" id="warehouseId" required onchange="updateAvailableQuantities()">
                    <option value="" disabled selected>-- Select Warehouse --</option>
                    <c:forEach var="wh" items="${warehouseList}">
                        <option value="${wh.warehouseId}">${wh.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-6">
                <label>Address:</label>
                <textarea class="form-control" name="customerAddress" rows="2"></textarea>
            </div>
            <div class="col-md-6">
                <label>Customer Note:</label>
                <textarea class="form-control" name="customerNote" rows="2"></textarea>
            </div>
        </div>

        <!-- Product Table -->
        <div class="table-responsive shadow rounded mb-3">
            <table class="table table-bordered" id="orderTable">
                <thead>
                <tr>
                    <th>Product</th>
                    <th>Unit Price</th>
                    <th>Available</th>
                    <th>Quantity</th>
                    <th>Total</th>
                    <th><button type="button" class="btn btn-success btn-sm" onclick="addRow()">+</button></th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>
                        <select name="productIds[]" class="form-select product-select" onchange="handleProductChange(this)">
                            <option value="" disabled selected>-- Select Product --</option>
                            <c:forEach var="item" items="${productList}">
                                <option value="${item.product.productId}" data-price="${item.product.salePrice}" data-code="${item.product.productCode}">${item.product.name} (${item.product.productCode})</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td class="unit-price">0</td>
                    <td class="available">-</td>
                    <td><input type="number" name="quantities[]" class="form-control" min="1" value="1" onchange="updateTotal(this)"></td>
                    <td class="total">0</td>
                    <td><button type="button" class="btn btn-danger btn-sm" onclick="removeRow(this)">-</button></td>
                </tr>
                </tbody>
            </table>
        </div>

        <!-- Order Reason and Note -->
        <div class="row mb-3">
            <div class="col-md-6">
                <label>Reason for Export:</label>
                <input type="text" class="form-control" name="reason">
            </div>
            <div class="col-md-6">
                <label>Order Note:</label>
                <textarea class="form-control" name="note" rows="3"></textarea>
            </div>
        </div>

        <div class="text-end">
            <button type="submit" class="btn btn-primary" onclick="return validateForm()">Submit Order</button>
        </div>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>

<script>
    // Inventory data from server
    const inventoryData = ${inventoryJson != null ? inventoryJson : '[]'};

    function updateProductInfo(select) {
        const row = select.closest('tr');
        const selectedOption = select.options[select.selectedIndex];
        const price = selectedOption?.getAttribute('data-price') || 0;
        row.querySelector('.unit-price').innerText = parseFloat(price).toFixed(2);

        // Update available quantity
        const productId = select.value;
        const warehouseId = document.getElementById('warehouseId').value;
        let available = '-';
        if (productId && warehouseId) {
            const inventory = inventoryData.find(item => item.product.productId == productId && item.warehouseId == warehouseId);
            available = inventory ? inventory.inventoryQuantity : 0;
        }
        row.querySelector('.available').innerText = available;
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

    function updateSelectOptions() {
        const selected = getSelectedProductIds();
        document.querySelectorAll('select[name="productIds[]"]').forEach(currentSelect => {
            const currentValue = currentSelect.value;
            currentSelect.querySelectorAll('option').forEach(option => {
                if (option.value === "" || option.value === currentValue) {
                    option.disabled = false;
                } else {
                    option.disabled = selected.includes(option.value);
                }
            });
        });
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
            updateSelectOptions();
            return;
        }

        updateProductInfo(select);
        updateSelectOptions();
    }

    function updateAvailableQuantities() {
        document.querySelectorAll('select[name="productIds[]"]').forEach(select => {
            updateProductInfo(select);
        });
    }

    function validateForm() {
        const warehouseId = document.getElementById('warehouseId').value;
        if (!warehouseId) {
            alert("Please select a warehouse.");
            return false;
        }

        let valid = true;
        document.querySelectorAll('tr').forEach(row => {
            const select = row.querySelector('select[name="productIds[]"]');
            const qtyInput = row.querySelector('input[name="quantities[]"]');
            if (select && qtyInput && select.value) {
                const productId = select.value;
                const qty = parseInt(qtyInput.value) || 0;
                const inventory = inventoryData.find(item => item.product.productId == productId && item.warehouseId == warehouseId);
                const available = inventory ? inventory.inventoryQuantity : 0;
                if (qty > available) {
                    alert(`Requested quantity (${qty}) for product ${select.options[select.selectedIndex].text} exceeds available stock (${available}).`);
                    valid = false;
                }
            }
        });
        return valid;
    }

    function initializeSelect2() {
        $('.product-select').select2({
            placeholder: "-- Select Product --",
            width: '100%'
        }).off('change')
            .on('change', function () {
                handleProductChange(this);
            });
    }

    function addRow() {
        const table = document.getElementById('orderTable').querySelector('tbody');
        const currentRowCount = table.querySelectorAll('tr').length;
        const totalProductCount = document.querySelector('select[name="productIds[]"]').querySelectorAll('option').length - 1;

        if (currentRowCount >= totalProductCount) {
            alert("Cannot add more rows as all available products are selected.");
            return;
        }

        const firstRow = table.querySelector('tr');
        const newRow = firstRow.cloneNode(true);

        // Replace Select2
        const oldSelect = newRow.querySelector('select.product-select');
        const clonedSelect = oldSelect.cloneNode(true);
        oldSelect.parentNode.replaceChild(clonedSelect, oldSelect);
        clonedSelect.classList.add('product-select');

        // Reset values
        newRow.querySelectorAll('input').forEach(input => input.value = 1);
        newRow.querySelector('.unit-price').innerText = '0';
        newRow.querySelector('.available').innerText = '-';
        newRow.querySelector('.total').innerText = '0';
        clonedSelect.selectedIndex = 0;

        table.appendChild(newRow);
        initializeSelect2();
        updateSelectOptions();
    }

    function removeRow(button) {
        const row = button.closest('tr');
        const table = row.parentNode;
        if (table.rows.length > 1) {
            row.remove();
            updateSelectOptions();
        }
    }

    document.addEventListener('DOMContentLoaded', () => {
        initializeSelect2();
        document.querySelectorAll('select.product-select').forEach(updateProductInfo);
        updateSelectOptions();
    });
</script>
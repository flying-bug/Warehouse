<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<div class="layout-specing">
    <div class="d-md-flex justify-content-between align-items-center mb-3">
        <h5 class="mb-0">Create Sale Order</h5>
    </div>

    <form action="createExportOrder" method="post">
        <!-- Customer and Warehouse Info -->
        <!-- Customer and Warehouse Info -->
        <div class="row mb-3">
            <div class="col-md-4">
                <label>Customer Name:</label>
                <input type="text" class="form-control" name="customerName" required>
            </div>
            <div class="col-md-4">
                <label>Phone:</label>
                <input type="text" class="form-control" name="customerPhone" required>
            </div>
            <div class="col-md-4">
                <label>Warehouse:</label>
                <select class="form-select" name="warehouseId" required>
                    <c:forEach var="wh" items="${warehouseList}">
                        <option value="${wh.warehouseId}">${wh.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>


        <!-- Product Selection Table -->
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
                        <select name="productIds" class="form-select" onchange="updateProductInfo(this)">
                            <c:forEach var="p" items="${productList}">
                                <option value="${p.productId}" data-price="${p.salePrice}" data-unit="${p.unit}">
                                        ${p.name} (${p.productCode})
                                </option>
                            </c:forEach>
                        </select>
                    </td>
                    <td class="unit-price">0</td>
                    <td class="available">?</td>
                    <td><input type="number" name="quantities" class="form-control" min="1" value="1" onchange="updateTotal(this)"></td>
                    <td class="total">0</td>
                    <td><button type="button" class="btn btn-danger btn-sm" onclick="removeRow(this)">-</button></td>
                </tr>
                </tbody>
            </table>
        </div>

        <!-- Note and Submit -->
        <div class="mb-3">
            <label>Note:</label>
            <textarea class="form-control" name="note" rows="3"></textarea>
        </div>

        <div class="text-end">
            <button type="submit" class="btn btn-primary">Submit Order</button>
        </div>
    </form>
</div>

<script>
    function updateProductInfo(select) {
        const row = select.closest('tr');
        const price = select.options[select.selectedIndex].getAttribute('data-price');
        row.querySelector('.unit-price').innerText = price;
        updateTotal(row.querySelector('input[name="quantities"]'));
    }

    function updateTotal(input) {
        const row = input.closest('tr');
        const price = parseFloat(row.querySelector('.unit-price').innerText) || 0;
        const qty = parseInt(input.value) || 0;
        row.querySelector('.total').innerText = (price * qty).toFixed(2);
    }

    function addRow() {
        const table = document.getElementById('orderTable').querySelector('tbody');
        const firstRow = table.querySelector('tr');
        const newRow = firstRow.cloneNode(true);
        newRow.querySelectorAll('input, select').forEach(e => {
            if (e.tagName === 'SELECT') e.selectedIndex = 0;
            else e.value = 1;
        });
        newRow.querySelectorAll('.unit-price, .total').forEach(e => e.innerText = '0');
        table.appendChild(newRow);
    }

    function removeRow(button) {
        const row = button.closest('tr');
        const table = row.parentNode;
        if (table.rows.length > 1) row.remove();
    }

    // Initialize price on load
    document.addEventListener('DOMContentLoaded', () => {
        document.querySelectorAll('select').forEach(updateProductInfo);
    });
</script>

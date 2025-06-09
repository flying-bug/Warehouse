<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<!-- Select2 CSS -->
<link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />

<div class="layout-specing">
    <div class="d-md-flex justify-content-between align-items-center mb-3">
        <h5 class="mb-0">Create Sale Order</h5>
    </div>

    <form action="createExportOrder" method="post">
        <!-- Customer Info -->
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
                    <th>
                        <button type="button" class="btn btn-success btn-sm" onclick="addRow()">+</button>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>
                        <select name="productIds" class="form-select product-select" onchange="updateProductInfo(this)">
                            <option value="" disabled selected>-- Select Product --</option>
                            <c:forEach var="p" items="${productList}">
                                <option value="${p.productId}" data-price="${p.salePrice}" data-unit="${p.unit}">
                                        ${p.name} (${p.productCode})
                                </option>
                            </c:forEach>
                        </select>
                    </td>
                    <td class="unit-price">0</td>
                    <td class="available">-</td>
                    <td><input type="number" name="quantities" class="form-control" min="1" value="1" onchange="updateTotal(this)"></td>
                    <td class="total">0</td>
                    <td><button type="button" class="btn btn-danger btn-sm" onclick="removeRow(this)">-</button></td>
                </tr>
                </tbody>
            </table>
        </div>

        <!-- Note -->
        <div class="mb-3">
            <label>Note:</label>
            <textarea class="form-control" name="note" rows="3"></textarea>
        </div>

        <div class="text-end">
            <button type="submit" class="btn btn-primary">Submit Order</button>
        </div>
    </form>
</div>

<!-- Select2 JS -->
<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>

<script>
    function updateProductInfo(select) {
        const row = select.closest('tr');
        const selectedOption = select.options[select.selectedIndex];
        const price = selectedOption?.getAttribute('data-price') || 0;
        row.querySelector('.unit-price').innerText = price;
        updateTotal(row.querySelector('input[name="quantities"]'));
    }

    function updateTotal(input) {
        const row = input.closest('tr');
        const price = parseFloat(row.querySelector('.unit-price').innerText) || 0;
        const qty = parseInt(input.value) || 0;
        row.querySelector('.total').innerText = (price * qty).toFixed(2);
    }

    // ✅ Lấy danh sách sản phẩm đã chọn
    function getSelectedProductIds() {
        const selectedIds = [];
        document.querySelectorAll('select[name="productIds"]').forEach(select => {
            const val = select.value;
            if (val) selectedIds.push(val);
        });
        return selectedIds;
    }

    // ✅ Disable option đã chọn ở các dòng khác
    function updateSelectOptions() {
        const selectedIds = getSelectedProductIds();
        document.querySelectorAll('select[name="productIds"]').forEach(currentSelect => {
            const currentValue = currentSelect.value;
            currentSelect.querySelectorAll('option').forEach(option => {
                if (option.value === "" || option.value === currentValue) {
                    option.disabled = false;
                } else {
                    option.disabled = selectedIds.includes(option.value);
                }
            });
        });
    }

    function handleProductChange(select) {
        const selectedValue = select.value;
        const selectedIds = getSelectedProductIds();

        // Kiểm tra nếu giá trị vừa chọn đã tồn tại ở dòng khác
        let isDuplicate = false;
        document.querySelectorAll('select[name="productIds"]').forEach(s => {
            if (s !== select && s.value === selectedValue) {
                isDuplicate = true;
            }
        });

        if (isDuplicate) {
            alert("This product is already selected in another row. Please choose a different product.");
            select.value = "";  // reset lại select
            $(select).trigger("change.select2"); // re-trigger để update UI
            updateProductInfo(select);
            updateSelectOptions();
            return;
        }

        // Nếu không bị trùng thì update info
        updateProductInfo(select);
        updateSelectOptions();
    }


    // ✅ Select2 kèm xử lý onchange
    function initializeSelect2() {
        $('.product-select').select2({
            placeholder: "-- Select Product --",
            width: '100%'
        }).off('change')
            .on('change', function () {
                handleProductChange(this);
            });
    }



    // ✅ Xóa dòng → cập nhật lại danh sách sản phẩm
    function removeRow(button) {
        const row = button.closest('tr');
        const table = row.parentNode;
        if (table.rows.length > 1) {
            row.remove();
            updateSelectOptions();
        }
    }

    // ✅ Gọi khi load trang
    document.addEventListener('DOMContentLoaded', () => {
        initializeSelect2();
        document.querySelectorAll('select.product-select').forEach(updateProductInfo);
        updateSelectOptions();
    });

    function addRow() {
        const table = document.getElementById('orderTable').querySelector('tbody');
        const currentRowCount = table.querySelectorAll('tr').length;
        const totalProductCount = document.querySelector('select[name="productIds"]').querySelectorAll('option:not([disabled])').length;

        if (currentRowCount >= totalProductCount) {
            alert("You cannot add more rows than available products.");
            return;
        }

        const firstRow = table.querySelector('tr');
        const newRow = firstRow.cloneNode(true);

        // ✅ Gỡ bỏ Select2 DOM cũ nếu có
        const oldSelect = newRow.querySelector('select.product-select');
        const clonedSelect = oldSelect.cloneNode(true); // clone lại select sạch
        oldSelect.parentNode.replaceChild(clonedSelect, oldSelect); // replace

        // ✅ Reset các giá trị trong dòng mới
        newRow.querySelectorAll('input').forEach(input => input.value = 1);
        clonedSelect.selectedIndex = 0;

        newRow.querySelector('.unit-price').innerText = '0';
        newRow.querySelector('.available').innerText = '-';
        newRow.querySelector('.total').innerText = '0';

        // ✅ Gắn lại class để apply Select2
        clonedSelect.classList.add('product-select');

        table.appendChild(newRow);

        // ✅ Reinitialize Select2 và update lại option
        initializeSelect2();
        updateSelectOptions();
    }


</script>






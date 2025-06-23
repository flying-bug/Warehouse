<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<div class="container mt-4">
    <h4 class="text-center mb-4">BIÊN BẢN KIỂM KÊ VẬT TƯ, CÔNG CỤ, SẢN PHẨM, HÀNG HÓA</h4>
    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${sessionScope.success}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="success" scope="session" />
    </c:if>

    <c:if test="${not empty sessionScope.fail}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${sessionScope.fail}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="fail" scope="session" />
    </c:if>


    <form action="${pageContext.request.contextPath}/stockCheck" method="post">
    <div class="mb-3">
            <label>Thời điểm bắt đầu kiểm kê:</label>
            <input type="datetime-local" class="form-control" name="checkStartDate" required />
        </div>
        <div class="mb-3">
            <label>Thời điểm kết thúc kiểm kê:</label>
            <input type="datetime-local" class="form-control" name="checkEndDate" required />
        </div>

        <div class="mb-3">
            <label>Chọn kho hàng:</label>
            <select name="warehouseId" class="form-select" id="warehouseSelect" onchange="redirectToWarehouse()">
                <option value="">-- Chọn kho --</option>
                <c:forEach var="wh" items="${warehouseList}">
                    <option value="${wh.warehouseId}"
                            <c:if test="${wh.warehouseId == warehouseId}">selected</c:if>>
                            ${wh.name}
                    </option>
                </c:forEach>
            </select>
        </div>

        <c:if test="${not empty inventoryList}">
            <div class="table-responsive">
                <table class="table table-bordered">
                    <thead class="table-light">
                    <tr>
                        <th>STT</th>
                        <th>Tên SP</th>
                        <th>Mã SP</th>
                        <th>ĐVT</th>
                        <th>Theo sổ kế toán</th>
                        <th>Thực tế</th>
                        <th>Thừa/Thiếu</th>
                        <th>Chất lượng</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" items="${inventoryList}" varStatus="loop">
                        <tr>
                            <td>${loop.index + 1}</td>
                            <c:set var="product" value="${productMap[item.productId]}" />
                            <td>${product.name}</td>
                            <td>${product.productCode}</td>
                            <td>${product.unit}</td>
                            <td>
                                <input type="number" class="form-control" name="systemQuantities[]" value="${item.quantity}" readonly />
                                <input type="hidden" name="productIds[]" value="${item.productId}" />
                            </td>
                            <td><input type="number" class="form-control" name="actualQuantities[]" min="0" required /></td>
                            <td>
                                <select name="differenceStatus[]" class="form-select">
                                    <option value="=">=</option>
                                    <option value="thua">Thừa</option>
                                    <option value="thieu">Thiếu</option>
                                </select>
                            </td>
                            <td><input type="text" name="quality[]" class="form-control" placeholder="Tình trạng" /></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="mb-3">
                <label>Ghi chú chung:</label>
                <textarea name="note" class="form-control" rows="3"></textarea>
            </div>

            <div class="text-end">
                <button type="submit" class="btn btn-primary">Lập biên bản</button>
            </div>
        </c:if>
    </form>
</div>

<script>
    const contextPath = '${pageContext.request.contextPath}';
</script>

<script>
    function redirectToWarehouse() {
        const select = document.getElementById("warehouseSelect");
        const selectedWarehouse = select.value;
        const url = new URL(window.location.href);
        const currentWarehouse = url.searchParams.get("warehouseId");

        if (selectedWarehouse) {
            // Nếu chọn lại cùng kho thì ép reload với timestamp
            if (currentWarehouse === selectedWarehouse) {
                window.location.href = contextPath + "/stockCheck?warehouseId=" + selectedWarehouse + "&t=" + new Date().getTime();
            } else {
                window.location.href = contextPath + "/stockCheck?warehouseId=" + selectedWarehouse;
            }
        }
    }
</script>

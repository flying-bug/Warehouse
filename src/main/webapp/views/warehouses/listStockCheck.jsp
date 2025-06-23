<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>

<div class="container mt-4">
    <h4 class="text-center mb-4">DANH SÁCH BIÊN BẢN KIỂM KÊ</h4>

    <c:if test="${not empty stockCheckList}">
        <div class="table-responsive">
            <table class="table table-bordered">
                <thead class="table-light">
                <tr>
                    <th>Mã kiểm kê</th>
                    <th>Tên kho</th>
                    <th>Người kiểm kê</th>
                    <th>Ngày kiểm kê</th>
                    <th>Trạng thái</th>
                    <th>Ghi chú</th>
                    <th>Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="check" items="${stockCheckList}">
                    <tr>
                        <td>${check.checkId}</td>
                        <td>${warehouseMap[check.warehouseId].name}</td>
                        <td>${accountMap[check.accountId].full_name}</td>
                        <td><fmt:formatDate value="${check.checkDate}" pattern="dd-MM-yyyy HH:mm"/><br>
                            -> <fmt:formatDate value="${check.endDate}" pattern="dd-MM-yyyy HH:mm"/>
                        </td>
                        <td>
                            <form action="${pageContext.request.contextPath}/updateStockCheckStatus" method="post" class="d-flex">
                                <input type="hidden" name="checkId" value="${check.checkId}" />
                                <select name="status" class="form-select form-select-sm me-2">
                                    <option value="Pending" ${check.status == 'Pending' ? 'selected' : ''}>Pending</option>
                                    <option value="Completed" ${check.status == 'Completed' ? 'selected' : ''}>Completed</option>
                                    <option value="Cancelled" ${check.status == 'Cancelled' ? 'selected' : ''}>Cancelled</option>
                                </select>
                                <button type="submit" class="btn btn-sm btn-outline-primary">Cập nhật</button>
                            </form>
                        </td>
                        <td>${check.note}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/stockCheckDetail?checkId=${check.checkId}" class="btn btn-sm btn-info">Chi tiết</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>

    <c:if test="${empty stockCheckList}">
        <div class="alert alert-warning text-center">Không có dữ liệu kiểm kê nào.</div>
    </c:if>
</div>

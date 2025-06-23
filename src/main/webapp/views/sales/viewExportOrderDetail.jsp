<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>

<div class="container-fluid px-4">

    <!-- HEADER -->
    <div class="d-flex justify-content-between align-items-center mt-4 mb-3">
        <div>
            <h6 class="text-muted mb-0">Quotations</h6>
            <h3 class="fw-bold text-primary">${exportOrder.code}</h3>
        </div>
        <div>
            <button class="btn btn-primary btn-sm">Send by Email</button>
            <button class="btn btn-success btn-sm">Confirm</button>
            <button class="btn btn-outline-secondary btn-sm">Preview</button>
            <button class="btn btn-outline-danger btn-sm">Cancel</button>
        </div>
    </div>

    <!-- ORDER INFO -->
    <div class="card shadow-sm rounded mb-4">
        <div class="card-body d-flex justify-content-between">
            <div>
                <table class="table table-sm table-borderless">
                    <c:forEach var="c" items="${customersList}">
                        <c:if test="${c.customerId == exportOrder.customerId}">
                            <tr>
                                <th class="text-start">Customer:</th>
                                <td>${c.name}</td>
                            </tr>
                            <tr>
                                <th></th>
                                <td>${c.email}</td>
                            </tr>
                            <tr>
                                <th></th>
                                <td>${c.phone}</td>
                            </tr>
                            <tr>
                                <th></th>
                                <td>${c.address}</td>
                            </tr>
                            <tr>
                                <th></th>
                                <td>${c.note}</td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </table>
            </div>
            <div class="text-start">
                <p><strong>Expiration:</strong>
                    <span><fmt:formatDate value="${exportOrder.exportDate}" pattern="dd/MM/yyyy HH:mm"/></span>
                </p>

                <p><strong>Payment Terms:</strong> <span style="color: red">*</span>${exportOrder.paymentTerms} Days</p>
            </div>
            <div>
                <p><strong>Warehouse:</strong>
                    <c:forEach var="w" items="${warehousesList}">
                        <c:if test="${w.warehouseId == exportOrder.warehouseId}">
                            ${w.name}
                        </c:if>
                    </c:forEach>
                </p>
                <p><strong>Handled By:</strong>
                    <c:forEach var="a" items="${accountsList}">
                        <c:if test="${a.account_id == exportOrder.accountId}">
                            ${a.full_name}
                        </c:if>
                    </c:forEach>
                </p>
            </div>
        </div>
    </div>

    <!-- PRODUCT TABLE -->
    <div class="card shadow-sm rounded">
        <div class="card-body">
            <h5 class="mb-3">Order Lines</h5>
            <div class="table-responsive">
                <table class="table table-bordered text-center align-middle mb-0">
                    <thead class="table-light">
                    <tr>
                        <th>Product</th>
                        <th>Quantity</th>
                        <th>Delivered</th>
                        <th>Invoiced</th>
                        <th>Unit Price</th>
                        <th>Taxes</th>
                        <th>Amount</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty list}">
                            <c:forEach var="exd" items="${list}">
                                <tr>
                                    <td>
                                        <c:forEach var="p" items="${plist}">
                                            <c:if test="${p.productId == exd.productId}">
                                                <b>${p.name}</b><br/>
                                                <small class="text-muted fst-italic">${p.description}</small>
                                            </c:if>
                                        </c:forEach>
                                    </td>
                                    <td>${exd.quantity}</td>
                                    <td>${exd.quantityDelivered}</td>
                                    <td>${exd.quantityInvoiced}</td>
                                    <td><fmt:formatNumber value="${exd.salePrice}" type="number"/> đ</td>
                                    <td>${exd.taxPercent}%</td>
                                    <td><fmt:formatNumber value="${exd.lineTotal}" type="number"/> đ</td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="7" class="text-danger">No products found.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- TOTAL -->
    <div class="text-end mt-3 pe-2">
        <p>Untaxed Amount: <strong><fmt:formatNumber value="${totalUntaxed}" type="number"/> đ</strong></p>
        <!-- Hiển thị từng dòng VAT -->
        <c:forEach var="entry" items="${vatMap}">
            <p>VAT ${entry.key}%:
                <fmt:formatNumber value="${entry.value}" type="number"/> đ
            </p>
        </c:forEach>

        <h5>Total: <strong><fmt:formatNumber value="${grandTotal}" type="number"/> đ</strong></h5>
    </div>
</div>

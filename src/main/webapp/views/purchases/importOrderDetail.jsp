<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>

<div class="container-fluid px-4">

    <!-- HEADER -->
    <div class="d-flex justify-content-between align-items-center mt-4 mb-3">
        <div>
            <h6 class="text-muted mb-0">Requests for Quotation</h6>
            <h3 class="fw-bold text-primary">${importOrder.code}</h3>
        </div>
        <div>

            <a href="${pageContext.request.contextPath}/viewListImportOrders" class="btn btn-primary btn-sm">Send by
                Email</a>

            <a href="${pageContext.request.contextPath}/viewListImportOrders" class="btn btn-success btn-sm">Confirm
                Order</a>

            <a href="${pageContext.request.contextPath}/viewListImportOrders" class="btn btn-outline-secondary btn-sm">Print
                RFQ</a>

            <a href="${pageContext.request.contextPath}/listRequestForQuotation" class="btn btn-outline-danger btn-sm">Cancel</a>

        </div>
    </div>

    <!-- ORDER DETAILS -->
    <div class="card shadow-sm rounded mb-4">
        <div class="card-body row">
            <!-- LEFT SIDE -->
            <div class="col-md-6">
                <p><strong>Vendor:</strong>
                    <c:forEach var="s" items="${suppliersList}">
                        <c:if test="${s.supplierId == importOrder.supplierId}">
                            ${s.name}
                        </c:if>
                    </c:forEach>
                </p>
                <p><strong>Curency:</strong> VND</p>
                <hr>
                <p><strong>Buyer:</strong>
                    <c:forEach var="a" items="${accountsList}">
                        <c:if test="${a.account_id == importOrder.accountId}">
                            ${a.full_name}
                        </c:if>
                    </c:forEach>
                </p>
                <p><strong>Note:</strong> ${importOrder.note}</p>
            </div>

            <!-- RIGHT SIDE -->
            <div class="col-md-6">
                <p><strong>Order Deadline:</strong>
                    <fmt:formatDate value="${importOrder.orderDeadline}" pattern="dd/MM/yyyy HH:mm"/>
                </p>
                <p><strong>Expected Arrival:</strong>
                    <fmt:formatDate value="${importOrder.expectedArrival}" pattern="dd/MM/yyyy HH:mm"/>
                </p>

                <p><strong>Deliver To:</strong>
                    <c:forEach var="w" items="${warehousesList}">
                        <c:if test="${w.warehouseId == importOrder.warehouseId}">
                            ${w.name}
                        </c:if>
                    </c:forEach>
                </p>

                <p><strong>Status:</strong> ${importOrder.status}</p>
            </div>
        </div>
    </div>

    <!-- PRODUCT TABLE -->
    <div class="card shadow-sm rounded mb-4">
        <div class="card-body">
            <h5 class="mb-3">Products</h5>
            <div class="table-responsive">
                <table class="table table-bordered text-center align-middle mb-0">
                    <thead class="table-light">
                    <tr>
                        <th>Product</th>
                        <th>Quantity</th>
                        <th>Unit Price</th>
                        <th>Taxes</th>
                        <th>Amount</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:set var="totalUntaxed" value="0"/>
                    <c:set var="vatMap" value="${empty vatMap ? {} : vatMap}"/>

                    <c:forEach var="detail" items="${importDetails}">
                        <c:set var="productName" value=""/>
                        <c:forEach var="p" items="${plist}">
                            <c:if test="${p.productId == detail.productId}">
                                <c:set var="productName" value="${p.name}"/>
                            </c:if>
                        </c:forEach>

                        <c:set var="lineTotal" value="${detail.quantity * detail.costPrice}"/>
                        <c:set var="totalUntaxed" value="${totalUntaxed + lineTotal}"/>

                        <tr>
                            <td>${productName}</td>
                            <td>${detail.quantity}</td>
                            <td><fmt:formatNumber value="${detail.costPrice}" type="number" groupingUsed="true"/> đ</td>
                            <td>${detail.taxPercent}%</td>
                            <td><fmt:formatNumber value="${lineTotal}" type="number" groupingUsed="true"/> đ</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- TOTALS -->
    <div class="text-end pe-2">
        <p>Untaxed Amount: <strong><fmt:formatNumber value="${totalUntaxed}" type="number" groupingUsed="true"/>
            đ</strong></p>

        <c:set var="grandTotal" value="${totalUntaxed}"/>
        <c:forEach var="entry" items="${vatMap}">
            <c:set var="grandTotal" value="${grandTotal + entry.value}"/>
            <p>VAT ${entry.key}%: <fmt:formatNumber value="${entry.value}" type="number" groupingUsed="true"/> đ</p>
        </c:forEach>

        <h5>Total: <strong><fmt:formatNumber value="${grandTotal}" type="number" groupingUsed="true"/> đ</strong></h5>
    </div>


</div>

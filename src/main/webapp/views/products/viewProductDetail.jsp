<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>View Product Details</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/dashboard.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/addAccount.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <style>
        label {
            font-weight: bold!important;
            color: #333;
            margin-bottom: 4px;
        }

        .form-group div {
            background-color: #fdf6e3; /* nền vàng nhẹ */
            padding: 6px 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            margin-top: 4px;
        }
    </style>


</head>
<body>

<jsp:include page="../dashboard.jsp"/>

<div class="main-content">
    <div class="form-container">
        <h2><i class="fas fa-box-open"></i> Product Details: ID = ${p.productId}</h2>
        <c:if test="${empty p}">
            <div style="color: red; font-weight: bold; text-align: center; margin: 20px;">
                Không tìm thấy thông tin sản phẩm.
            </div>
        </c:if>

        <div class="form-grid">
            <!-- Left Column -->
            <div>
                <div class="form-group">
                    <label>Product Code:</label>
                    <div>${p.productCode}</div>
                </div>

                <div class="form-group">
                    <label>Name:</label>
                    <div>${p.name}</div>
                </div>

                <div class="form-group">
                    <label>Description:</label>
                    <div>${p.description}</div>
                </div>

                <div class="form-group">
                    <label>Size:</label>
                    <div>${p.size}</div>
                </div>
            </div>

            <!-- Right Column -->
            <div>
                <div class="form-group">
                    <label>Color:</label>
                    <div>${p.color}</div>
                </div>

                <div class="form-group">
                    <label>Material:</label>
                    <div>${p.material}</div>
                </div>

                <div class="form-group">
                    <label>Unit:</label>
                    <div>${p.unit}</div>
                </div>

                <div class="form-group">
                    <label>Supplier:</label>
                    <div>
                        <c:choose>
                            <c:when test="${not empty suppliers}">
                                <c:forEach items="${suppliers}" var="supplier">
                                    <c:if test="${currentSupplierId == supplier.supplierId}">
                                        ${supplier.name}
                                    </c:if>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                Không có nhà cung cấp.
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>

        <!-- Bottom Section -->
        <div class="form-group">
            <label>Cost Price ($):</label>
            <div>${p.costPrice}</div>
        </div>

        <div class="form-group">
            <label>Sale Price ($):</label>
            <div>${p.salePrice}</div>
        </div>

        <a href="<%= request.getContextPath() %>/viewListProducts"
           class="btn btn-submit"
           style="font-size: 13px; padding: 4px 10px; height: auto;">
            <i class="fas fa-arrow-left"></i> Back
        </a>

    </div>
</div>

</body>
</html>

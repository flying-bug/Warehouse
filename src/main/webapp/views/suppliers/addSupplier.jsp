<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- File này chỉ chứa nội dung, không có <html>, <head>, <body> --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>


<div class="d-md-flex justify-content-between align-items-center">
    <h5 class="mb-0">Add New Supplier</h5>
    <nav aria-label="breadcrumb" class="d-inline-block mt-4 mt-sm-0">
        <ul class="breadcrumb bg-transparent rounded mb-0 p-0">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboardAdmin">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/viewListSuppliers">Suppliers</a></li>
            <li class="breadcrumb-item active" aria-current="page">Add Supplier</li>
        </ul>
    </nav>
</div>

<div class="row justify-content-center">
    <div class="col-lg-10 mt-4">
        <div class="card border-0 p-4 rounded shadow">
            <!-- Hiển thị thông báo lỗi nếu có -->
            <%
                String errorMessage = (String) request.getAttribute("errorMessage");
                if (errorMessage != null) {
            %>
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <%= errorMessage %>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <% } %>

            <form class="mt-3" action="${pageContext.request.contextPath}/addSupplier" method="post">
                <div class="row">
                    <div class="col-md-12">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="name" class="form-label">Supplier Name<span class="text-danger">*</span>:</label>
                                <input type="text" id="name" name="name" class="form-control" 
                                       placeholder="Enter supplier name" required
                                       value="<%= request.getAttribute("name") != null ? request.getAttribute("name") : "" %>">
                            </div>

                            <div class="col-md-6 mb-3">
                                <label for="phone" class="form-label">Phone<span class="text-danger">*</span>:</label>
                                <input type="text" id="phone" name="phone" class="form-control" 
                                       placeholder="Enter phone number" required
                                       value="<%= request.getAttribute("phone") != null ? request.getAttribute("phone") : "" %>">
                            </div>

                            <div class="col-md-12 mb-3">
                                <label for="address" class="form-label">Address<span class="text-danger">*</span>:</label>
                                <textarea id="address" name="address" class="form-control" 
                                          placeholder="Enter supplier address" required rows="3"><%=
                                    request.getAttribute("address") != null ? request.getAttribute("address") : ""
                                %></textarea>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="mt-4 text-end">
                    <button type="submit" class="btn btn-primary me-2">
                        <i class="uil uil-save me-1"></i> Save Supplier
                    </button>
                    <button type="reset" class="btn btn-outline-secondary me-2">
                        <i class="uil uil-redo me-1"></i> Reset
                    </button>

                    <a href="${pageContext.request.contextPath}/viewListSuppliers" class="btn btn-danger">
                        <i class="fas fa-times me-1"></i> Cancel
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>

<%@ page import="model.Roles" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<%-- File này chỉ chứa nội dung, không có <html>, <head>, <body> --%>

<div class="d-md-flex justify-content-between align-items-center">
    <h5 class="mb-0">Add New Account</h5>
    <nav aria-label="breadcrumb" class="d-inline-block mt-4 mt-sm-0">
        <ul class="breadcrumb bg-transparent rounded mb-0 p-0">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboardAdmin">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/viewListAccounts">Accounts</a></li>
            <li class="breadcrumb-item active" aria-current="page">Add Account</li>
        </ul>
    </nav>
</div>

<div class="row justify-content-center">
    <div class="col-lg-10 mt-4">
        <div class="card border-0 p-4 rounded shadow">

            <c:if test="${not empty success}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                        ${success}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <c:if test="${not empty fail}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${fail}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <form class="mt-3" action="${pageContext.request.contextPath}/addAccount" method="post" enctype="multipart/form-data">
                <input type="hidden" name="action" value="save">
                <div class="row">
                    <div class="col-md-4 text-center">
                        <label class="form-label d-block">Profile Image:</label>
                        <img id="imagePreview"
                             src="${pageContext.request.contextPath}/assets/images/client/default-avatar.png"
                             alt="Preview Image" class="avatar avatar-xl-large img-thumbnail rounded-circle shadow"
                             style="width: 150px; height: 150px; object-fit: cover;"/>
                        <input type="file" id="profile_image" name="profile_image" accept="image/*" class="form-control form-control-sm mt-3">
                    </div>

                    <div class="col-md-8">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="account_name" class="form-label">Username<span class="text-danger">*</span>:</label>
                                <input type="text" id="account_name" name="account_name" value="${account_name}" class="form-control" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="password" class="form-label">Password<span class="text-danger">*</span>:</label>
                                <div class="input-group">
                                    <input type="password" id="password" name="password" class="form-control" required autocomplete="new-password">
                                    <span class="input-group-text" onclick="togglePassword(this)" style="cursor: pointer;"><i class="uil uil-eye"></i></span>
                                </div>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="full_name" class="form-label">Full Name<span class="text-danger">*</span>:</label>
                                <input type="text" id="full_name" name="full_name" value="${full_name}" class="form-control" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="email" class="form-label">Email<span class="text-danger">*</span>:</label>
                                <input type="email" id="email" name="email" value="${email}" class="form-control" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="phone" class="form-label">Phone<span class="text-danger">*</span>:</label>
                                <input type="text" id="phone" name="phone" value="${phone}" class="form-control" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="role_id" class="form-label">Role<span class="text-danger">*</span>:</label>
                                <select id="role_id" name="role_id" class="form-select" required>
                                    <option value="" disabled <c:if test="${empty selectedRoleId}">selected</c:if>>Select a role</option>
                                    <c:forEach items="${roles}" var="role">
                                        <option value="${role.role_id}" <c:if test="${role.role_id == selectedRoleId}">selected</c:if>>${role.role_name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="mt-4 text-end">
                    <button type="submit" class="btn btn-primary me-2"><i class="uil uil-save me-1"></i> Save Account</button>
                    <button type="reset" class="btn btn-outline-secondary me-2"><i class="uil uil-redo me-1"></i> Reset</button>
                    <a href="${pageContext.request.contextPath}/viewListAccounts" class="btn btn-light"><i class="uil uil-times me-1"></i> Cancel</a>
                </div>
            </form>
        </div>
    </div>
</div>

<%-- Script riêng cho form này --%>
<script>
    document.getElementById('profile_image').addEventListener('change', function (event) {
        const file = event.target.files[0];
        const reader = new FileReader();
        const imgPreview = document.getElementById('imagePreview');

        if (file) {
            reader.onload = function () {
                imgPreview.src = reader.result;
            };
            reader.readAsDataURL(file);
        } else {
            imgPreview.src = "${pageContext.request.contextPath}/assets/images/client/default-avatar.png";
        }
    });

    function togglePassword(element) {
        const passwordInput = document.getElementById("password");
        const icon = element.querySelector("i");

        if (passwordInput.type === "password") {
            passwordInput.type = "text";
            icon.classList.remove("uil-eye");
            icon.classList.add("uil-eye-slash");
        } else {
            passwordInput.type = "password";
            icon.classList.remove("uil-eye-slash");
            icon.classList.add("uil-eye");
        }
    }
</script>
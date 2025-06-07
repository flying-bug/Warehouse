<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<div class="d-md-flex justify-content-between align-items-center">
    <h5 class="mb-0">Update Account: ID = ${a.account_id}</h5>
    <nav aria-label="breadcrumb" class="d-inline-block mt-4 mt-sm-0">
        <ul class="breadcrumb bg-transparent rounded mb-0 p-0">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboardAdmin">Dashboard</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/viewListAccounts">Accounts</a></li>
            <li class="breadcrumb-item active" aria-current="page">Update</li>
        </ul>
    </nav>
</div>

<div class="row justify-content-center">
    <div class="col-lg-10 mt-4">
        <div class="card border-0 p-4 rounded shadow">

            <!-- Alert message -->
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
            <c:if test="${empty a}">
                <div class="alert alert-danger fw-bold text-center">Không tìm thấy tài khoản cần cập nhật.</div>
            </c:if>

            <!-- Form -->
            <form class="mt-3" action="${pageContext.request.contextPath}/updateAccount" method="post" enctype="multipart/form-data">
                <input type="hidden" name="account_id" value="${a.account_id}">

                <div class="row">
                    <!-- Left column -->
                    <div class="col-md-4 text-center">
                        <label class="form-label d-block">Profile Image:</label>
                        <img id="imagePreview"
                             src="${pageContext.request.contextPath}/${a.profile_image != null ? a.profile_image : 'assets/images/client/default-avatar.png'}"
                             alt="Profile" class="avatar avatar-xl-large img-thumbnail rounded-circle shadow"
                             style="width: 150px; height: 150px; object-fit: cover;"/>
                        <input type="file" id="profile_image" name="profile_image" accept="image/*" class="form-control form-control-sm mt-3">
                    </div>

                    <!-- Right column -->
                    <div class="col-md-8">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Username:</label>
                                <input type="text" name="account_name" value="${a.account_name}" class="form-control" readonly>
                            </div>

                            <div class="col-md-6 mb-3">
                                <label class="form-label">Password:</label>
                                <div class="input-group">
                                    <input type="password" name="password" value="${a.password}" id="password" class="form-control">
                                    <span class="input-group-text" onclick="togglePassword(this)" style="cursor: pointer;"><i class="uil uil-eye"></i></span>
                                </div>
                            </div>

                            <div class="col-md-6 mb-3">
                                <label class="form-label">Full Name:</label>
                                <input type="text" name="full_name" value="${a.full_name}" class="form-control">
                            </div>

                            <div class="col-md-6 mb-3">
                                <label class="form-label">Email:</label>
                                <input type="email" name="email" value="${a.email}" class="form-control" readonly>
                            </div>

                            <div class="col-md-6 mb-3">
                                <label class="form-label">Phone:</label>
                                <input type="text" name="phone" value="${a.phone}" class="form-control">
                            </div>

                            <div class="col-md-6 mb-3">
                                <label class="form-label">Role:</label>
                                <select name="role_id" class="form-select">
                                    <option value="" disabled ${empty a.role_id ? 'selected' : ''}>Select a role</option>
                                    <c:forEach items="${roles}" var="role">
                                        <option value="${role.role_id}" ${role.role_id == a.role_id ? 'selected' : ''}>${role.role_name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Action buttons -->
                <div class="mt-4 text-end">
                    <button type="submit" class="btn btn-primary me-2"><i class="uil uil-save me-1"></i> Update</button>
                    <button type="reset" class="btn btn-outline-secondary me-2"><i class="uil uil-redo me-1"></i> Reset</button>
                    <a href="${pageContext.request.contextPath}/viewListAccounts" class="btn btn-light"><i class="uil uil-times me-1"></i> Cancel</a>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- JS script -->
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

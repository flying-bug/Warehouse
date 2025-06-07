<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Update Account</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/dashboard.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/addAccount.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>

<jsp:include page="../dashboard.jsp"/>


<div class="main-content">
    <div class="form-container">
        <h2><i class="fas fa-user-edit"></i> Update Account: ID = ${a.account_id}</h2>

        <%-- Hiển thị thông báo thành công --%>
        <c:if test="${not empty success}">
            <div style="color: green; font-weight: bold;">
                    ${success}
            </div><br><br>
        </c:if>

        <%-- Hiển thị thông báo lỗi --%>
        <c:if test="${not empty fail}">
            <div style="color: red; font-weight: bold;">
                    ${fail}
            </div><br><br>
        </c:if>

        <c:if test="${empty a}">
            <div style="color: red; font-weight: bold; text-align: center; margin: 20px;">
                Không tìm thấy thông tin tài khoản cần cập nhật.
            </div>
        </c:if>


        <form action="<%= request.getContextPath() %>/updateAccount" method="post" enctype="multipart/form-data">
            <input type="hidden" name="account_id" value="${a.account_id}">

            <div class="form-grid">
                <!-- Left Column -->
                <div>
                    <div class="form-group">
                        <label for="account_name">Username:</label>
                        <input type="text" id="account_name" name="account_name" value="${a.account_name}" >
                    </div>

                    <div class="form-group">
                        <label for="password">Password:</label>
                        <div style="position: relative;">
                            <input type="password" id="password" name="password" value="${a.password}"
                                   style="padding-right: 30px;">
                            <i class="fas fa-eye toggle-password" onclick="togglePassword()"
                               style="position: absolute; top: 50%; right: 10px; transform: translateY(-50%); cursor: pointer;"></i>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="profile_image">Profile Image:</label>
                        <input type="file" id="profile_image" name="profile_image" accept="image/*">
                        <img src="${pageContext.request.contextPath}/${a.profile_image!=null?a.profile_image:''}"
                             class="avatar rounded shadow mt-3" height="250" width="250" alt="Account Lyly ">


                    </div>
                </div>

                <!-- Right Column -->
                <div>
                    <div class="form-group">
                        <label for="email">Email:</label>
                        <input type="email" id="email" name="email" value="${a.email}" required>
                    </div>

                    <div class="form-group">
                        <label for="phone">Phone:</label>
                        <input type="text" id="phone" name="phone" value="${a.phone}">
                    </div>

                    <div class="form-group">
                        <label for="full_name">Full Name:</label>
                        <input type="text" id="full_name" name="full_name" value="${a.full_name}">
                    </div>

                    <div class="form-group">
                        <label for="role_id">Role:</label>
                        <select id="role_id" name="role_id">
                            <option value="" disabled ${empty a.role_id ? 'selected' : ''}>Select a role</option>
                            <c:forEach items="${roles}" var="role">
                                <option value="${role.role_id}" ${role.role_id == a.role_id ? 'selected="selected"' : ''}>
                                        ${role.role_name}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                </div>
            </div>
            <div class="form-group">
                <button type="submit" class="btn-submit"><i class="fas fa-save"></i> Update Account</button>
                <button type="reset" class="btn-submit"><i class="fas fa-save"></i> Reset</button>
                <a href="<%= request.getContextPath() %>/viewListAccounts" class="btn btn-submit"><i
                        class="fas fa-arrow-left"></i> Cancel</a>
            </div>
        </form>
    </div>
</div>

<script>
    // Preview uploaded image
    document.getElementById('profile_image').addEventListener('change', function (event) {
        const file = event.target.files[0];
        const reader = new FileReader();

        reader.onload = function () {
            const img = document.getElementById('imagePreview');
            img.src = reader.result;
            img.style.display = 'block';
        };

        if (file) {
            reader.readAsDataURL(file);
        }
    });

    function togglePassword() {
        const passwordInput = document.getElementById("password");
        const icon = document.querySelector(".toggle-password");

        if (passwordInput.type === "password") {
            passwordInput.type = "text";
            icon.classList.remove("fa-eye");
            icon.classList.add("fa-eye-slash");
        } else {
            passwordInput.type = "password";
            icon.classList.remove("fa-eye-slash");
            icon.classList.add("fa-eye");
        }
    }

</script>

</body>
</html>
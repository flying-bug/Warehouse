<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<div class="d-md-flex justify-content-between align-items-center">
    <h5 class="mb-0">List of Accounts</h5>
    <a href="${pageContext.request.contextPath}/addAccount" class="btn btn-primary">
        <i class="uil uil-plus"></i> Add New Account
    </a>
</div>

<div class="row mt-4">
    <div class="col-12">
        <div class="table-responsive shadow rounded">
            <table class="table table-bordered bg-white mb-0 text-center align-middle">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Full Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty accountList}">
                        <c:forEach var="acc" items="${accountList}">
                            <tr>
                                <td>${acc.account_id}</td>
                                <td>${acc.full_name}</td>
                                <td>${acc.email}</td>
                                <td>${acc.phone}</td>
                                <td>
                                    <a href="updateAccount?id=${acc.account_id}" class="btn btn-sm btn-soft-success">
                                        <i class="uil uil-pen"></i>
                                    </a>
                                    <a href="deleteAccount?id=${acc.account_id}" class="btn btn-sm btn-soft-danger"
                                       onclick="return confirm('Are you sure delete Account ID: ${acc.account_id}?')">
                                        <i class="uil uil-trash"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="5" class="text-danger">No accounts found.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
                    <th>Role</th>
                    <th>Status</th>
                    <th>Created At</th>
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
                                    <c:choose>
                                        <c:when test="${acc.role_id == 1}">Admin</c:when>
                                        <c:when test="${acc.role_id == 2}">Store Manager</c:when>
                                        <c:when test="${acc.role_id == 3}">Warehouse Staff</c:when>
                                        <c:when test="${acc.role_id == 4}">Purchasing Staff</c:when>
                                        <c:when test="${acc.role_id == 5}">Sales Staff</c:when>
                                        <c:otherwise>Unknown</c:otherwise>
                                    </c:choose>
                                </td>

                                <td>
                                    <span class="badge bg-${acc.status == 1 ? 'success' : 'secondary'}">
                                            ${acc.status == 1 ? 'Active' : 'Inactive'}
                                    </span>
                                </td>
                                <td>
                                    <fmt:formatDate value="${acc.created_at}" pattern="yyyy-MM-dd HH:mm"/>
                                </td>
                                <td>
                                    <a href="updateAccount?id=${acc.account_id}" class="btn btn-sm btn-soft-success">
                                        <i class="uil uil-pen"></i>
                                    </a>

                                    <c:if test="${acc.status == 1}">
                                        <a href="deleteAccount?id=${acc.account_id}" class="btn btn-sm btn-soft-danger"
                                           onclick="return confirm('Are you sure to deactivate Account ID: ${acc.account_id}?')">
                                            <i class="uil uil-trash"></i>
                                        </a>
                                    </c:if>

                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="7" class="text-danger">No accounts found.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

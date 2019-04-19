<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!doctype html>
<html lang="en">
<head>
    <title>用户列表</title>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link href="<c:url value='/static/css/bootstrap.css' />" rel="stylesheet"/>
    <link href="<c:url value='/static/css/app.css' />" rel="stylesheet"/>
</head>
<body>
<div class="generic-container">
    <%@include file="authheader.jsp"%>
    <div class="panel panel-default">
        <div class="panel-heading">
            <span class="lead">List of Users</span>
        </div>
        <table class="table table-hover">
            <thead>
            <tr>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Email</th>
                <th>SSO ID</th>
                <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                    <th width="100"></th>
                </sec:authorize>
                <sec:authorize access="hasRole('ADMIN')">
                    <th width="100"></th>
                </sec:authorize>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${users}" var="user">
                <tr>
                    <td>${user.firstName}</td>
                    <td>${user.lastName}</td>
                    <td>${user.email}</td>
                    <td>${user.ssoId}</td>
                    <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                        <td>
                            <a href="<c:url value="/edit-user-${user.ssoId}"/>" class="btn btn-success custom-width">Edit</a>
                        </td>
                    </sec:authorize>
                    <sec:authorize access="hasRole('ADMIN')">
                        <td>
                            <a href="<c:url value="/delete-user-${user.ssoId}"/>" class="btn btn-danger custom-width">Delete</a>
                        </td>
                    </sec:authorize>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <sec:authorize access="hasRole('ADMIN')">
        <div class="well">
            <a href="<c:url value='/newUser'/>">Add New User</a>
        </div>
    </sec:authorize>

</div>

</body>
</html>

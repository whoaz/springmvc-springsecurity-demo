<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>注册成功</title>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link href="<c:url value='/static/css/bootstrap.css' />" rel="stylesheet"/>
    <link href="<c:url value='/static/css/app.css' />" rel="stylesheet"/>
</head>
<body>
<div class="generic-container">
    <%@include file="authheader.jsp"%>
    <div class="alert alert-success lead">
        ${success}
    </div>
    <span class="well floatRight">
        Go to <a href="<c:url value='/list'/> ">用户列表</a>
    </span>
</div>
</body>
</html>

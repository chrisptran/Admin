<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome Page</title>
</head>
<body>
    <h1>Welcome Page <c:out value="${user.username}"></c:out></h1>
    
    <form id="logoutForm" method="POST" action="/users/logout">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="submit" value="Logout!" />
    </form>
    
    <p>Username: ${user.username}</p>
    <p>Email: ${user.email}</p>
    <p>Signup Date: ${user.createdAt}</p>
    <p>Last Sign In: ${user.lastLogin}</p>
</body>
</html>
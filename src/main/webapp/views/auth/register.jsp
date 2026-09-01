<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register</title>
    <link rel="stylesheet" href="<c:url value='/assets/app-theme.css'/>">
</head>
<body class="theme-music">
<div class="theme-shell">
    <div class="panel form-shell">
        <div class="eyebrow">Register</div>
        <h1>Create a new account</h1>
        <p class="inline-note">After registration, the system sends an OTP by email so you can activate the account.</p>
        <c:if test="${not empty error}"><div class="error-box">${error}</div></c:if>
        <form method="post" action="<c:url value='/register'/>">
            <div class="form-group">
                <label>Full Name</label>
                <input class="form-input" type="text" name="fullName" value="${param.fullName}" placeholder="Your full name">
            </div>
            <div class="form-group">
                <label>Username</label>
                <input class="form-input" type="text" name="username" value="${param.username}" placeholder="your username">
            </div>
            <div class="form-group">
                <label>Email</label>
                <input class="form-input" type="email" name="email" value="${param.email}" placeholder="email@example.com">
            </div>
            <div class="form-group">
                <label>Password</label>
                <input class="form-input" type="password" name="password" placeholder="At least 6 characters">
            </div>
            <div class="form-group">
                <label>Confirm Password</label>
                <input class="form-input" type="password" name="confirmPassword" placeholder="Retype password">
            </div>
            <div class="form-actions">
                <button class="btn btn-primary" type="submit">Register and Send OTP</button>
                <a class="btn btn-secondary" href="<c:url value='/login'/>">Back to Login</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>

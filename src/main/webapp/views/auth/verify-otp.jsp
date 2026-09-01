<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Verify OTP</title>
    <link rel="stylesheet" href="<c:url value='/assets/app-theme.css'/>">
</head>
<body class="theme-music">
<div class="theme-shell">
    <div class="panel form-shell">
        <div class="eyebrow">OTP Verification</div>
        <h1>Confirm your OTP</h1>
        <p class="inline-note">Email being verified: <strong>${email}</strong></p>
        <c:if test="${not empty param.message}"><div class="message-box">${param.message}</div></c:if>
        <c:if test="${not empty error}"><div class="error-box">${error}</div></c:if>
        <form method="post" action="<c:url value='/verify-otp'/>">
            <input type="hidden" name="purpose" value="${purpose}">
            <input type="hidden" name="email" value="${email}">
            <div class="form-group">
                <label>OTP Code</label>
                <input class="form-input" type="text" name="otp" maxlength="6" placeholder="6 digits">
            </div>
            <div class="form-actions">
                <button class="btn btn-primary" type="submit">Verify OTP</button>
                <a class="btn btn-secondary" href="<c:url value='/login'/>">Back to Login</a>
            </div>
        </form>
        <c:if test="${purpose == 'REGISTER'}">
            <form method="post" action="<c:url value='/resend-otp'/>">
                <input type="hidden" name="email" value="${email}">
                <div class="form-actions">
                    <button class="btn btn-secondary" type="submit">Resend OTP</button>
                </div>
            </form>
        </c:if>
    </div>
</div>
</body>
</html>

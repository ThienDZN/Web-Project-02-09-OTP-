package vn.iotstar.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.config.OtpPurpose;
import vn.iotstar.config.SessionConstants;
import vn.iotstar.entity.UserAccount;
import vn.iotstar.service.IAuthService;
import vn.iotstar.service.impl.AuthServiceImpl;

@WebServlet(urlPatterns = {
        "/login",
        "/logout",
        "/register",
        "/verify-otp",
        "/forgot-password",
        "/reset-password",
        "/resend-otp"
})
public class AuthController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final IAuthService authService = new AuthServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        if ("/logout".equals(servletPath)) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            resp.sendRedirect(req.getContextPath() + "/home?message=" + encode("You have been logged out successfully."));
            return;
        }
        if ("/login".equals(servletPath)) {
            req.getRequestDispatcher("/views/auth/login.jsp").forward(req, resp);
            return;
        }
        if ("/register".equals(servletPath)) {
            req.getRequestDispatcher("/views/auth/register.jsp").forward(req, resp);
            return;
        }
        if ("/verify-otp".equals(servletPath)) {
            req.setAttribute("purpose", valueOrDefault(req.getParameter("purpose"), OtpPurpose.REGISTER));
            req.setAttribute("email", valueOrDefault(req.getParameter("email"), ""));
            req.getRequestDispatcher("/views/auth/verify-otp.jsp").forward(req, resp);
            return;
        }
        if ("/forgot-password".equals(servletPath)) {
            req.getRequestDispatcher("/views/auth/forgot-password.jsp").forward(req, resp);
            return;
        }
        if ("/reset-password".equals(servletPath)) {
            String email = valueOrDefault(req.getParameter("email"), "");
            HttpSession session = req.getSession(false);
            String allowedEmail = session == null ? null : (String) session.getAttribute(SessionConstants.RESET_PASSWORD_EMAIL);
            if (allowedEmail == null || !allowedEmail.equalsIgnoreCase(email)) {
                resp.sendRedirect(req.getContextPath() + "/forgot-password?message=" + encode("Please verify the OTP before resetting your password."));
                return;
            }
            req.setAttribute("email", email);
            req.getRequestDispatcher("/views/auth/reset-password.jsp").forward(req, resp);
            return;
        }
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        try {
            if ("/login".equals(servletPath)) {
                handleLogin(req, resp);
                return;
            }
            if ("/register".equals(servletPath)) {
                handleRegister(req, resp);
                return;
            }
            if ("/verify-otp".equals(servletPath)) {
                handleVerifyOtp(req, resp);
                return;
            }
            if ("/forgot-password".equals(servletPath)) {
                handleForgotPassword(req, resp);
                return;
            }
            if ("/reset-password".equals(servletPath)) {
                handleResetPassword(req, resp);
                return;
            }
            if ("/resend-otp".equals(servletPath)) {
                handleResendOtp(req, resp);
                return;
            }
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserAccount user = authService.login(req.getParameter("usernameOrEmail"), req.getParameter("password"));
        req.getSession(true).setAttribute(SessionConstants.CURRENT_USER, user);
        if ("ADMIN".equalsIgnoreCase(user.getRoleName())) {
            resp.sendRedirect(req.getContextPath() + "/admin/products?message=" + encode("Admin login successful."));
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/home?message=" + encode("Login successful."));
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String deliveryMessage = authService.register(
                req.getParameter("fullName"),
                req.getParameter("username"),
                req.getParameter("email"),
                req.getParameter("password"),
                req.getParameter("confirmPassword"));
        resp.sendRedirect(req.getContextPath() + "/verify-otp?purpose=" + OtpPurpose.REGISTER
                + "&email=" + encode(req.getParameter("email"))
                + "&message=" + encode(deliveryMessage));
    }

    private void handleVerifyOtp(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String purpose = valueOrDefault(req.getParameter("purpose"), OtpPurpose.REGISTER);
        String email = req.getParameter("email");
        String otp = req.getParameter("otp");
        if (OtpPurpose.RESET_PASSWORD.equalsIgnoreCase(purpose)) {
            authService.verifyResetPasswordOtp(email, otp);
            req.getSession(true).setAttribute(SessionConstants.RESET_PASSWORD_EMAIL, email.trim().toLowerCase());
            resp.sendRedirect(req.getContextPath() + "/reset-password?email=" + encode(email));
            return;
        }
        authService.verifyRegistrationOtp(email, otp);
        resp.sendRedirect(req.getContextPath() + "/login?message=" + encode("Your account has been activated. You can log in now."));
    }

    private void handleForgotPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String deliveryMessage = authService.sendResetPasswordOtp(req.getParameter("email"));
        resp.sendRedirect(req.getContextPath() + "/verify-otp?purpose=" + OtpPurpose.RESET_PASSWORD
                + "&email=" + encode(req.getParameter("email"))
                + "&message=" + encode(deliveryMessage));
    }

    private void handleResetPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        authService.resetPassword(email, req.getParameter("password"), req.getParameter("confirmPassword"));
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.removeAttribute(SessionConstants.RESET_PASSWORD_EMAIL);
        }
        resp.sendRedirect(req.getContextPath() + "/login?message=" + encode("Password updated successfully. Please log in again."));
    }

    private void handleResendOtp(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        String deliveryMessage = authService.resendRegistrationOtp(email);
        resp.sendRedirect(req.getContextPath() + "/verify-otp?purpose=" + OtpPurpose.REGISTER
                + "&email=" + encode(email)
                + "&message=" + encode(deliveryMessage));
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private String valueOrDefault(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value;
    }
}

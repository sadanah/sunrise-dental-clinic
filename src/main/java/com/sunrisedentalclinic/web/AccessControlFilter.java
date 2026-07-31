package com.sunrisedentalclinic.web;

import com.sunrisedentalclinic.domain.Session;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class AccessControlFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getRequestURI().substring(request.getContextPath().length());
        String ctx = request.getContextPath();

        // Public pages — no auth required
        if (path.equals("/login") || path.equals("/login.jsp") || path.startsWith("/css/") || path.startsWith("/images/") || path.equals("/index.jsp") || path.equals("/")) {
            chain.doFilter(req, res);
            return;
        }

        HttpSession httpSession = request.getSession(false);
        Session appSession = (httpSession != null) ? (Session) httpSession.getAttribute("appSession") : null;

        if (appSession == null || !appSession.isValid()) {
            response.sendRedirect(ctx + "/login.jsp");
            return;
        }

        String role = appSession.getRole();

        // Admin-only pages
        if ((path.startsWith("/admin/") || path.equals("/admin-dashboard.jsp")
                || path.equals("/manage-staff.jsp") || path.equals("/manage-treatment.jsp")
                || path.equals("/generate-report.jsp") || path.equals("/report-result.jsp"))
                && !"ADMIN".equals(role)) {
            response.sendRedirect(ctx + "/login.jsp");
            return;
        }

        // Receptionist-only pages
        if ((path.equals("/receptionist-dashboard.jsp")
                || path.startsWith("/appointments/")
                || path.equals("/billing")
                || path.equals("/register-appointment.jsp")
                || path.equals("/cancel-appointment.jsp")
                || path.equals("/search-appointment.jsp")
                || path.equals("/generate-bill.jsp"))
                && !"RECEPTIONIST".equals(role)) {
            response.sendRedirect(ctx + "/login.jsp");
            return;
        }

        // Dentist-only pages
        if ((path.startsWith("/dentist/") || path.equals("/dentist-dashboard.jsp"))
                && !"DENTIST".equals(role)) {
            response.sendRedirect(ctx + "/login.jsp");
            return;
        }

        chain.doFilter(req, res);
    }
}
package com.sunrisedentalclinic.web;

import com.sunrisedentalclinic.domain.Session;
import com.sunrisedentalclinic.exception.AuthenticationException;
import com.sunrisedentalclinic.service.IAuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final IAuthService authService;

    public LoginServlet() {
        this(ServiceFactory.getAuthService());
    }

    public LoginServlet(IAuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            Session session = authService.login(username, password);

            HttpSession httpSession = request.getSession(true);
            httpSession.setAttribute("appSession", session);
            httpSession.setMaxInactiveInterval(30 * 60);

            switch (session.getRole()) {
                case "ADMIN" -> response.sendRedirect("admin-dashboard.jsp");
                case "DENTIST" -> response.sendRedirect("dentist/dashboard");
                default -> response.sendRedirect("receptionist-dashboard.jsp");
            }
        } catch (AuthenticationException e) {
            request.setAttribute("errorMessage", "Invalid username or password");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
package com.sunrisedentalclinic.web;

import com.sunrisedentalclinic.domain.Session;
import com.sunrisedentalclinic.exception.AuthenticationException;
import com.sunrisedentalclinic.service.IAuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServletTest {

    @Mock private IAuthService authService;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HttpSession session;

    @Test
    void doPost_validCredentials_redirectsToCorrectDashboard() throws Exception {
        LoginServlet servlet = new LoginServlet(authService); // isolated, no ServiceFactory/DB involved

        when(request.getParameter("username")).thenReturn("ksilva");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getSession(true)).thenReturn(session);

        Session domainSession = new Session("SID1", LocalDateTime.now(), LocalDateTime.now().plusMinutes(30));
        domainSession.setStaffID("R001");
        domainSession.setRole("RECEPTIONIST");

        when(authService.login("ksilva", "password123")).thenReturn(domainSession);

        servlet.doPost(request, response);

        verify(response).sendRedirect("receptionist-dashboard.jsp");
    }

    @Test
    void doPost_invalidCredentials_forwardsBackToLoginWithError() throws Exception {
        LoginServlet servlet = new LoginServlet(authService);

        when(request.getParameter("username")).thenReturn("ksilva");
        when(request.getParameter("password")).thenReturn("wrongpassword");
        when(authService.login("ksilva", "wrongpassword"))
                .thenThrow(new AuthenticationException("Invalid username or password"));

        javax.servlet.RequestDispatcher dispatcher = mock(javax.servlet.RequestDispatcher.class);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(dispatcher).forward(request, response);
        verify(request).setAttribute(eq("errorMessage"), anyString());
    }
}
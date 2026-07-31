package com.sunrisedentalclinic.web;

import com.sunrisedentalclinic.domain.Session;
import com.sunrisedentalclinic.exception.AccessDeniedException;
import com.sunrisedentalclinic.report.RevenueReport;
import com.sunrisedentalclinic.service.IAdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenerateReportServletTest {

    @Mock private IAdminService adminService;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HttpSession httpSession;

    private GenerateReportServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new GenerateReportServlet(adminService);
    }

    @Test
    void doGet_nonAdminRole_redirectsToLogin() throws Exception {
        Session receptionistSession = new Session("S1", LocalDateTime.now(), LocalDateTime.now().plusMinutes(30));
        receptionistSession.setRole("RECEPTIONIST");

        when(request.getSession(false)).thenReturn(httpSession);
        when(httpSession.getAttribute("appSession")).thenReturn(receptionistSession);

        servlet.doGet(request, response);

        verify(response).sendRedirect("login.jsp");
        verifyNoInteractions(adminService);
    }

    @Test
    void doGet_adminRole_forwardsToReportForm() throws Exception {
        Session adminSession = new Session("S1", LocalDateTime.now(), LocalDateTime.now().plusMinutes(30));
        adminSession.setRole("ADMIN");

        when(request.getSession(false)).thenReturn(httpSession);
        when(httpSession.getAttribute("appSession")).thenReturn(adminSession);

        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/generate-report.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void doPost_adminRole_generatesReportSuccessfully() throws Exception {
        Session adminSession = new Session("S1", LocalDateTime.now(), LocalDateTime.now().plusMinutes(30));
        adminSession.setRole("ADMIN");

        when(request.getSession(false)).thenReturn(httpSession);
        when(httpSession.getAttribute("appSession")).thenReturn(adminSession);
        when(request.getParameter("type")).thenReturn("REVENUE");
        when(request.getParameter("startDate")).thenReturn("2026-07-01");
        when(request.getParameter("endDate")).thenReturn("2026-07-29");

        RevenueReport mockReport = mock(RevenueReport.class);
        when(adminService.generateReport(eq("REVENUE"), eq("S1"), any(), any(), any()))
                .thenReturn(mockReport);

        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/report-result.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(dispatcher).forward(request, response);
        verify(request).setAttribute("report", mockReport);
    }

    @Test
    void doPost_serviceLayerDeniesAccess_showsErrorEvenIfServletCheckPassed() throws Exception {
        // Simulates the defense-in-depth scenario: servlet thinks role is ADMIN,
        // but AdminService's own check fails for some reason (e.g. stale/tampered session)
        Session adminSession = new Session("S1", LocalDateTime.now(), LocalDateTime.now().plusMinutes(30));
        adminSession.setRole("ADMIN");

        when(request.getSession(false)).thenReturn(httpSession);
        when(httpSession.getAttribute("appSession")).thenReturn(adminSession);
        when(request.getParameter("type")).thenReturn("REVENUE");
        when(request.getParameter("startDate")).thenReturn("2026-07-01");
        when(request.getParameter("endDate")).thenReturn("2026-07-29");

        when(adminService.generateReport(any(), any(), any(), any(), any()))
                .thenThrow(new AccessDeniedException("Only Admin users can generate reports"));

        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/generate-report.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(dispatcher).forward(request, response);
        verify(request).setAttribute(eq("errorMessage"), anyString());
    }
}
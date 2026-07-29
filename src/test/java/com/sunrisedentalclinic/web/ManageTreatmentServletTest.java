package com.sunrisedentalclinic.web;

import com.sunrisedentalclinic.dao.TreatmentTypeDAO;
import com.sunrisedentalclinic.domain.Session;
import com.sunrisedentalclinic.domain.TreatmentType;
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
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManageTreatmentServletTest {

    @Mock private IAdminService adminService;
    @Mock private TreatmentTypeDAO treatmentTypeDAO;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HttpSession httpSession;

    private ManageTreatmentServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new ManageTreatmentServlet(adminService, treatmentTypeDAO);
    }

    private void mockSession(String role) {
        Session session = new Session("S1", LocalDateTime.now(), LocalDateTime.now().plusMinutes(30));
        session.setRole(role);
        when(request.getSession(false)).thenReturn(httpSession);
        when(httpSession.getAttribute("appSession")).thenReturn(session);
    }

    @Test
    void doGet_nonAdminRole_redirectsToLogin() throws Exception {
        mockSession("DENTIST");

        servlet.doGet(request, response);

        verify(response).sendRedirect("login.jsp");
        verifyNoInteractions(treatmentTypeDAO);
    }

    @Test
    void doGet_adminRole_loadsTreatmentListAndForwards() throws Exception {
        mockSession("ADMIN");

        List<TreatmentType> treatments = Collections.emptyList();
        when(treatmentTypeDAO.findAll()).thenReturn(treatments);

        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/manage-treatment.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request).setAttribute("treatments", treatments);
        verify(dispatcher).forward(request, response);
    }

    @Test
    void doPost_createAction_callsAdminServiceAndReloadsList() throws Exception {
        mockSession("ADMIN");

        when(request.getParameter("action")).thenReturn("create");
        when(request.getParameter("treatmentID")).thenReturn("T004");
        when(request.getParameter("treatmentName")).thenReturn("Whitening");
        when(request.getParameter("baseCost")).thenReturn("8000.00");

        when(treatmentTypeDAO.findAll()).thenReturn(Collections.emptyList());

        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/manage-treatment.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(adminService).manageTreatment(eq("create"), any(TreatmentType.class));
        verify(request).setAttribute(eq("successMessage"), anyString());
        verify(dispatcher).forward(request, response);
    }

    @Test
    void doPost_deleteAction_callsAdminServiceWithCorrectTreatmentID() throws Exception {
        mockSession("ADMIN");

        when(request.getParameter("action")).thenReturn("delete");
        when(request.getParameter("treatmentID")).thenReturn("T001");
        when(treatmentTypeDAO.findAll()).thenReturn(Collections.emptyList());

        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/manage-treatment.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(adminService).manageTreatment(eq("delete"), argThat(t -> "T001".equals(t.getTreatmentID())));
        verify(request).setAttribute(eq("successMessage"), anyString());
    }

    @Test
    void doPost_invalidBaseCost_showsErrorMessage() throws Exception {
        mockSession("ADMIN");

        when(request.getParameter("action")).thenReturn("create");
        when(request.getParameter("treatmentID")).thenReturn("T004");
        when(request.getParameter("treatmentName")).thenReturn("Whitening");
        when(request.getParameter("baseCost")).thenReturn("not-a-number");

        when(treatmentTypeDAO.findAll()).thenReturn(Collections.emptyList());

        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/manage-treatment.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errorMessage"), anyString());
        verify(adminService, never()).manageTreatment(any(), any());
    }
}
package com.sunrisedentalclinic.web;

import com.sunrisedentalclinic.dao.StaffDAO;
import com.sunrisedentalclinic.domain.Session;
import com.sunrisedentalclinic.domain.Staff;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManageStaffServletTest {

    @Mock private IAdminService adminService;
    @Mock private StaffDAO staffDAO;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HttpSession httpSession;

    private ManageStaffServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new ManageStaffServlet(adminService, staffDAO);
    }

    private void mockSession(String role) {
        Session session = new Session("S1", LocalDateTime.now(), LocalDateTime.now().plusMinutes(30));
        session.setRole(role);
        when(request.getSession(false)).thenReturn(httpSession);
        when(httpSession.getAttribute("appSession")).thenReturn(session);
    }

    @Test
    void doGet_nonAdminRole_redirectsToLogin() throws Exception {
        mockSession("RECEPTIONIST");

        servlet.doGet(request, response);

        verify(response).sendRedirect("login.jsp");
        verifyNoInteractions(staffDAO);
    }

    @Test
    void doGet_adminRole_loadsStaffListAndForwards() throws Exception {
        mockSession("ADMIN");

        List<Staff> staffList = Collections.emptyList();
        when(staffDAO.findAll()).thenReturn(staffList);

        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/manage-staff.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request).setAttribute("staffList", staffList);
        verify(dispatcher).forward(request, response);
    }

    @Test
    void doPost_createAction_callsAdminServiceAndReloadsList() throws Exception {
        mockSession("ADMIN");

        when(request.getParameter("action")).thenReturn("create");
        when(request.getParameter("role")).thenReturn("RECEPTIONIST");
        when(request.getParameter("staffID")).thenReturn("R002");
        when(request.getParameter("name")).thenReturn("Nimal Perera");
        when(request.getParameter("contactNo")).thenReturn("0771112233");
        when(request.getParameter("address")).thenReturn("Colombo");
        when(request.getParameter("username")).thenReturn("nperera2");
        when(request.getParameter("password")).thenReturn("securepass123");

        when(staffDAO.findAll()).thenReturn(Collections.emptyList());

        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/manage-staff.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(adminService).manageStaff(eq("create"), any(Staff.class));
        verify(request).setAttribute(eq("successMessage"), anyString());
        verify(dispatcher).forward(request, response);
    }

    @Test
    void doPost_deleteAction_callsAdminServiceWithCorrectStaffID() throws Exception {
        mockSession("ADMIN");

        when(request.getParameter("action")).thenReturn("delete");
        when(request.getParameter("staffID")).thenReturn("R001");
        when(staffDAO.findAll()).thenReturn(Collections.emptyList());

        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/manage-staff.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(adminService).manageStaff(eq("delete"), argThat(staff -> "R001".equals(staff.getStaffID())));
        verify(request).setAttribute(eq("successMessage"), anyString());
    }
}
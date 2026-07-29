package com.sunrisedentalclinic.web;

import com.sunrisedentalclinic.dao.DentistDAO;
import com.sunrisedentalclinic.dao.PatientDAO;
import com.sunrisedentalclinic.dao.TreatmentTypeDAO;
import com.sunrisedentalclinic.domain.Appointment;
import com.sunrisedentalclinic.domain.Session;
import com.sunrisedentalclinic.exception.SlotUnavailableException;
import com.sunrisedentalclinic.service.impl.ClinicFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterAppointmentServletTest {

    @Mock private ClinicFacade clinicFacade;
    @Mock private PatientDAO patientDAO;
    @Mock private DentistDAO dentistDAO;
    @Mock private TreatmentTypeDAO treatmentTypeDAO;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HttpSession httpSession;

    private RegisterAppointmentServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new RegisterAppointmentServlet(clinicFacade, patientDAO, dentistDAO, treatmentTypeDAO);

        Session appSession = new Session("S1", LocalDateTime.now(), LocalDateTime.now().plusMinutes(30));
        appSession.setStaffID("R001");
        appSession.setRole("RECEPTIONIST");

        when(request.getSession(false)).thenReturn(httpSession);
        when(httpSession.getAttribute("appSession")).thenReturn(appSession);

        lenient().when(patientDAO.findAll()).thenReturn(Collections.emptyList());
        lenient().when(dentistDAO.findAll()).thenReturn(Collections.emptyList());
        lenient().when(treatmentTypeDAO.findAll()).thenReturn(Collections.emptyList());
    }

    @Test
    void doPost_validData_forwardsToConfirmation() throws Exception {
        when(request.getParameter("patientID")).thenReturn("P001");
        when(request.getParameter("dentistID")).thenReturn("D001");
        when(request.getParameter("treatmentID")).thenReturn("T001");
        when(request.getParameter("date")).thenReturn(LocalDate.now().plusDays(3).toString());
        when(request.getParameter("time")).thenReturn("10:00");

        Appointment appt = new Appointment("APT001", LocalDate.now().plusDays(3), LocalTime.of(10, 0));
        when(clinicFacade.registerAppointment(eq("P001"), eq("D001"), eq("T001"), eq("R001"), any(), any()))
                .thenReturn(appt);

        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/appointment-confirmation.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(dispatcher).forward(request, response);
        verify(request).setAttribute("appointment", appt);
    }

    @Test
    void doPost_slotUnavailable_forwardsBackWithError() throws Exception {
        when(request.getParameter("patientID")).thenReturn("P001");
        when(request.getParameter("dentistID")).thenReturn("D001");
        when(request.getParameter("treatmentID")).thenReturn("T001");
        when(request.getParameter("date")).thenReturn(LocalDate.now().plusDays(3).toString());
        when(request.getParameter("time")).thenReturn("10:00");

        when(clinicFacade.registerAppointment(any(), any(), any(), any(), any(), any()))
                .thenThrow(new SlotUnavailableException("Slot already booked"));

        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/register-appointment.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(dispatcher).forward(request, response);
        verify(request).setAttribute(eq("errorMessage"), anyString());
    }
}
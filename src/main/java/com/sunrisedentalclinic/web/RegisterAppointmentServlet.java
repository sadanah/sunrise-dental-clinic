package com.sunrisedentalclinic.web;

import com.sunrisedentalclinic.domain.Appointment;
import com.sunrisedentalclinic.exception.SlotUnavailableException;
import com.sunrisedentalclinic.service.impl.ClinicFacade;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

@WebServlet("/appointments/register")
public class RegisterAppointmentServlet extends HttpServlet {

    private final ClinicFacade clinicFacade;

    public RegisterAppointmentServlet() {
        this(ServiceFactory.getClinicFacade());
    }

    public RegisterAppointmentServlet(ClinicFacade clinicFacade) {
        this.clinicFacade = clinicFacade;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.hasRole(request, "RECEPTIONIST")) {
            response.sendRedirect("login.jsp");
            return;
        }
        request.getRequestDispatcher("/register-appointment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.hasRole(request, "RECEPTIONIST")) {
            response.sendRedirect("login.jsp");
            return;
        }

        String patientID = request.getParameter("patientID");
        String dentistID = request.getParameter("dentistID");
        String treatmentID = request.getParameter("treatmentID");
        String staffID = SessionUtil.getCurrentSession(request).getStaffID();

        // Basic server-side validation (required regardless of client-side checks)
        if (isBlank(patientID) || isBlank(dentistID) || isBlank(treatmentID)
                || isBlank(request.getParameter("date")) || isBlank(request.getParameter("time"))) {
            request.setAttribute("errorMessage", "All fields are required.");
            request.getRequestDispatcher("/register-appointment.jsp").forward(request, response);
            return;
        }

        try {
            LocalDate date = LocalDate.parse(request.getParameter("date"));
            LocalTime time = LocalTime.parse(request.getParameter("time"));

            Appointment appointment = clinicFacade.registerAppointment(
                    patientID, dentistID, treatmentID, staffID, date, time);

            request.setAttribute("appointment", appointment);
            request.getRequestDispatcher("/appointment-confirmation.jsp").forward(request, response);

        } catch (SlotUnavailableException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/register-appointment.jsp").forward(request, response);
        } catch (java.time.format.DateTimeParseException e) {
            request.setAttribute("errorMessage", "Invalid date or time format.");
            request.getRequestDispatcher("/register-appointment.jsp").forward(request, response);
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
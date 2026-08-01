package com.sunrisedentalclinic.web;

import com.sunrisedentalclinic.domain.Appointment;
import com.sunrisedentalclinic.exception.AppointmentNotFoundException;
import com.sunrisedentalclinic.exception.InvalidCancellationException;
import com.sunrisedentalclinic.service.impl.ClinicFacade;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/appointments/cancel")
public class CancelAppointmentServlet extends HttpServlet {

    private final ClinicFacade clinicFacade;

    public CancelAppointmentServlet() {
        this(ServiceFactory.getClinicFacade());
    }

    public CancelAppointmentServlet(ClinicFacade clinicFacade) {
        this.clinicFacade = clinicFacade;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.hasRole(request, "RECEPTIONIST")) {
            response.sendRedirect("login.jsp");
            return;
        }

        String appointmentNo = request.getParameter("appointmentNo");
        if (appointmentNo != null && !appointmentNo.trim().isEmpty()) {
            try {
                Appointment appointment = clinicFacade.searchAppointment(appointmentNo);
                request.setAttribute("appointment", appointment);
            } catch (AppointmentNotFoundException e) {
                request.setAttribute("errorMessage", e.getMessage());
            }
        }

        request.getRequestDispatcher("/cancel-appointment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.hasRole(request, "RECEPTIONIST")) {
            response.sendRedirect("login.jsp");
            return;
        }

        String appointmentNo = request.getParameter("appointmentNo");

        try {
            clinicFacade.cancelAppointment(appointmentNo);
            request.setAttribute("successMessage", "Appointment " + appointmentNo + " cancelled successfully.");
        } catch (AppointmentNotFoundException | InvalidCancellationException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }

        request.getRequestDispatcher("/cancel-appointment.jsp").forward(request, response);
    }
}
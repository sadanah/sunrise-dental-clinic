package com.sunrisedentalclinic.web;

import com.sunrisedentalclinic.domain.Appointment;
import com.sunrisedentalclinic.exception.AppointmentNotFoundException;
import com.sunrisedentalclinic.service.impl.ClinicFacade;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/appointments/search")
public class SearchAppointmentServlet extends HttpServlet {

    private final ClinicFacade clinicFacade;

    public SearchAppointmentServlet() {
        this(ServiceFactory.getClinicFacade());
    }

    public SearchAppointmentServlet(ClinicFacade clinicFacade) {
        this.clinicFacade = clinicFacade;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.isAuthenticated(request)) {
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

        request.getRequestDispatcher("/search-appointment.jsp").forward(request, response);
    }
}
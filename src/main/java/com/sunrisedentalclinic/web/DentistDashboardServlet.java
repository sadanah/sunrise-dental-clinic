package com.sunrisedentalclinic.web;

import com.sunrisedentalclinic.domain.Appointment;
import com.sunrisedentalclinic.domain.Session;
import com.sunrisedentalclinic.service.IAppointmentService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/dentist/dashboard")
public class DentistDashboardServlet extends HttpServlet {

    private final IAppointmentService appointmentService;

    public DentistDashboardServlet() {
        this(ServiceFactory.getAppointmentService()); // ASSUMPTION: confirm this accessor exists
    }

    public DentistDashboardServlet(IAppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.hasRole(request, "DENTIST")) {
            response.sendRedirect("login.jsp");
            return;
        }

        Session session = SessionUtil.getCurrentSession(request);
        String dentistID = session.getStaffID(); // ASSUMPTION: confirm this getter name

        List<Appointment> upcoming = appointmentService.getUpcomingAppointmentsForDentist(dentistID);
        request.setAttribute("upcomingAppointments", upcoming);

        request.getRequestDispatcher("/dentist-dashboard.jsp").forward(request, response);
    }
}
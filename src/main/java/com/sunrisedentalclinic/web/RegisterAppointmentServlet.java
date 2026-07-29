package com.sunrisedentalclinic.web;

import com.sunrisedentalclinic.dao.DentistDAO;
import com.sunrisedentalclinic.dao.PatientDAO;
import com.sunrisedentalclinic.dao.TreatmentTypeDAO;
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
    private final PatientDAO patientDAO;
    private final DentistDAO dentistDAO;
    private final TreatmentTypeDAO treatmentTypeDAO;

    public RegisterAppointmentServlet() {
        this(ServiceFactory.getClinicFacade(), ServiceFactory.getPatientDAO(),
                ServiceFactory.getDentistDAO(), ServiceFactory.getTreatmentTypeDAO());
    }

    public RegisterAppointmentServlet(ClinicFacade clinicFacade, PatientDAO patientDAO,
                                      DentistDAO dentistDAO, TreatmentTypeDAO treatmentTypeDAO) {
        this.clinicFacade = clinicFacade;
        this.patientDAO = patientDAO;
        this.dentistDAO = dentistDAO;
        this.treatmentTypeDAO = treatmentTypeDAO;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.hasRole(request, "RECEPTIONIST")) {
            response.sendRedirect("login.jsp");
            return;
        }

        loadDropdownData(request);
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

        if (isBlank(patientID) || isBlank(dentistID) || isBlank(treatmentID)
                || isBlank(request.getParameter("date")) || isBlank(request.getParameter("time"))) {
            request.setAttribute("errorMessage", "All fields are required.");
            loadDropdownData(request);
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
            loadDropdownData(request);
            request.getRequestDispatcher("/register-appointment.jsp").forward(request, response);
        } catch (java.time.format.DateTimeParseException e) {
            request.setAttribute("errorMessage", "Invalid date or time format.");
            loadDropdownData(request);
            request.getRequestDispatcher("/register-appointment.jsp").forward(request, response);
        }
    }

    private void loadDropdownData(HttpServletRequest request) {
        request.setAttribute("patients", patientDAO.findAll());
        request.setAttribute("dentists", dentistDAO.findAll());
        request.setAttribute("treatments", treatmentTypeDAO.findAll());
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
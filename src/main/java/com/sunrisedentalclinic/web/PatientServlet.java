package com.sunrisedentalclinic.web;

import com.sunrisedentalclinic.domain.Patient;
import com.sunrisedentalclinic.exception.PatientNotFoundException;
import com.sunrisedentalclinic.service.impl.ClinicFacade;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/patients")
public class PatientServlet extends HttpServlet {

    private final ClinicFacade clinicFacade;

    public PatientServlet() {
        this(ServiceFactory.getClinicFacade());
    }

    public PatientServlet(ClinicFacade clinicFacade) {
        this.clinicFacade = clinicFacade;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.hasRole(request, "RECEPTIONIST")) {
            response.sendRedirect("login.jsp");
            return;
        }

        String patientID = request.getParameter("patientID");
        if (patientID != null && !patientID.trim().isEmpty()) {
            try {
                Patient patient = clinicFacade.searchPatient(patientID);
                request.setAttribute("patient", patient);
            } catch (PatientNotFoundException e) {
                request.setAttribute("errorMessage", e.getMessage());
            }
        }

        request.setAttribute("patients", clinicFacade.listPatients());
        request.getRequestDispatcher("/manage-patients.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.hasRole(request, "RECEPTIONIST")) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("create".equals(action)) {
                String patientID = request.getParameter("patientID");
                clinicFacade.registerPatient(
                        patientID,
                        request.getParameter("name"),
                        request.getParameter("contactNo"),
                        request.getParameter("address")
                );
                request.setAttribute("successMessage", "Patient " + patientID + " added successfully.");
            } else if ("delete".equals(action)) {
                String patientID = request.getParameter("patientID");
                clinicFacade.deletePatient(patientID);
                request.setAttribute("successMessage", "Patient " + patientID + " deleted successfully.");
            }
        } catch (RuntimeException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }

        request.setAttribute("patients", clinicFacade.listPatients());
        request.getRequestDispatcher("/manage-patients.jsp").forward(request, response);
    }
}
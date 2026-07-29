package com.sunrisedentalclinic.web;

import com.sunrisedentalclinic.dao.TreatmentTypeDAO;
import com.sunrisedentalclinic.domain.TreatmentType;
import com.sunrisedentalclinic.service.IAdminService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/admin/treatments")
public class ManageTreatmentServlet extends HttpServlet {

    private final IAdminService adminService;
    private final TreatmentTypeDAO treatmentTypeDAO;

    public ManageTreatmentServlet() {
        this(ServiceFactory.getAdminService(), ServiceFactory.getTreatmentTypeDAO());
    }

    public ManageTreatmentServlet(IAdminService adminService, TreatmentTypeDAO treatmentTypeDAO) {
        this.adminService = adminService;
        this.treatmentTypeDAO = treatmentTypeDAO;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.hasRole(request, "ADMIN")) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<TreatmentType> treatments = treatmentTypeDAO.findAll();
        request.setAttribute("treatments", treatments);
        request.getRequestDispatcher("/manage-treatment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.hasRole(request, "ADMIN")) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        String treatmentID = request.getParameter("treatmentID");

        try {
            if ("delete".equals(action)) {
                TreatmentType dummy = new TreatmentType(treatmentID, "", BigDecimal.ZERO);
                adminService.manageTreatment("delete", dummy);
                request.setAttribute("successMessage", "Treatment " + treatmentID + " deleted.");
            } else {
                String treatmentName = request.getParameter("treatmentName");
                BigDecimal baseCost = new BigDecimal(request.getParameter("baseCost"));
                TreatmentType treatment = new TreatmentType(treatmentID, treatmentName, baseCost);
                adminService.manageTreatment(action, treatment);
                request.setAttribute("successMessage", "Treatment " + treatmentID + " " + action + "d successfully.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid base cost value.");
        }

        List<TreatmentType> treatments = treatmentTypeDAO.findAll();
        request.setAttribute("treatments", treatments);
        request.getRequestDispatcher("/manage-treatment.jsp").forward(request, response);
    }
}
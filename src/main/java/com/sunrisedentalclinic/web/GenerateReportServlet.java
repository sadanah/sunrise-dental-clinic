package com.sunrisedentalclinic.web;

import com.sunrisedentalclinic.domain.Session;
import com.sunrisedentalclinic.exception.AccessDeniedException;
import com.sunrisedentalclinic.report.Report;
import com.sunrisedentalclinic.service.IAdminService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/admin/reports")
public class GenerateReportServlet extends HttpServlet {

    private final IAdminService adminService;

    public GenerateReportServlet() {
        this(ServiceFactory.getAdminService());
    }

    public GenerateReportServlet(IAdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.hasRole(request, "ADMIN")) {
            response.sendRedirect("login.jsp");
            return;
        }
        request.getRequestDispatcher("/generate-report.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.hasRole(request, "ADMIN")) {
            response.sendRedirect("login.jsp");
            return;
        }

        String type = request.getParameter("type");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String dentistID = request.getParameter("dentistID");
        Session session = SessionUtil.getCurrentSession(request);

        try {
            LocalDate startDate = (startDateStr != null && !startDateStr.isEmpty()) ? LocalDate.parse(startDateStr) : null;
            LocalDate endDate = (endDateStr != null && !endDateStr.isEmpty()) ? LocalDate.parse(endDateStr) : null;

            if (type.equals("DENTIST_SCHEDULE") && (startDate == null || dentistID == null || dentistID.isEmpty())) {
                request.setAttribute("errorMessage", "Dentist ID and date are required for this report type.");
                request.getRequestDispatcher("/generate-report.jsp").forward(request, response);
                return;
            }
            if ((type.equals("REVENUE")) && (startDate == null || endDate == null)) {
                request.setAttribute("errorMessage", "Start and end dates are required for this report type.");
                request.getRequestDispatcher("/generate-report.jsp").forward(request, response);
                return;
            }
            if (type.equals("DAILY_APPOINTMENTS") && startDate == null) {
                request.setAttribute("errorMessage", "Start date is required for this report type.");
                request.getRequestDispatcher("/generate-report.jsp").forward(request, response);
                return;
            }

            Report report = adminService.generateReport(type, session.getSessionID(), startDate, endDate, dentistID);
            request.setAttribute("report", report);
            request.getRequestDispatcher("/report-result.jsp").forward(request, response);

        } catch (AccessDeniedException e) {
            // Belt-and-braces: AdminService independently re-checks the role via the session,
            // even though the servlet already guarded this above — defense in depth.
            request.setAttribute("errorMessage", "Access denied.");
            request.getRequestDispatcher("/generate-report.jsp").forward(request, response);
        } catch (java.time.format.DateTimeParseException e) {
            request.setAttribute("errorMessage", "Invalid date format.");
            request.getRequestDispatcher("/generate-report.jsp").forward(request, response);
        }
    }
}
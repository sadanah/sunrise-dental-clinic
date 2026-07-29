package com.sunrisedentalclinic.web;

import com.sunrisedentalclinic.domain.Bill;
import com.sunrisedentalclinic.exception.AppointmentNotFoundException;
import com.sunrisedentalclinic.service.IBillingService;
import com.sunrisedentalclinic.service.impl.ClinicFacade;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/billing")
public class BillingServlet extends HttpServlet {

    private final ClinicFacade clinicFacade;
    private final IBillingService billingService;

    public BillingServlet() {
        this(ServiceFactory.getClinicFacade(), ServiceFactory.getBillingService());
    }

    public BillingServlet(ClinicFacade clinicFacade, IBillingService billingService) {
        this.clinicFacade = clinicFacade;
        this.billingService = billingService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.hasRole(request, "RECEPTIONIST")) {
            response.sendRedirect("login.jsp");
            return;
        }
        request.getRequestDispatcher("/generate-bill.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.hasRole(request, "RECEPTIONIST")) {
            response.sendRedirect("login.jsp");
            return;
        }

        String appointmentNo = request.getParameter("appointmentNo");
        String discountParam = request.getParameter("discountPercent");
        String action = request.getParameter("action"); // "generate" or "print"

        if (appointmentNo == null || appointmentNo.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Appointment number is required.");
            request.getRequestDispatcher("/generate-bill.jsp").forward(request, response);
            return;
        }

        try {
            Bill bill = clinicFacade.generateBill(appointmentNo);

            if (discountParam != null && !discountParam.trim().isEmpty()) {
                BigDecimal discountPercent = new BigDecimal(discountParam);
                if (discountPercent.compareTo(BigDecimal.ZERO) < 0 || discountPercent.compareTo(BigDecimal.valueOf(100)) > 0) {
                    request.setAttribute("errorMessage", "Discount must be between 0 and 100.");
                    request.getRequestDispatcher("/generate-bill.jsp").forward(request, response);
                    return;
                }
                bill = billingService.applyDiscount(bill, discountPercent);
            }

            if ("print".equals(action)) {
                billingService.printReceipt(bill.getBillID());
            }

            request.setAttribute("bill", bill);
            request.getRequestDispatcher("/bill-receipt.jsp").forward(request, response);

        } catch (AppointmentNotFoundException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/generate-bill.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid discount value.");
            request.getRequestDispatcher("/generate-bill.jsp").forward(request, response);
        }
    }
}
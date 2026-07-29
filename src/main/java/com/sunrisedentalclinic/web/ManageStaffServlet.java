package com.sunrisedentalclinic.web;

import com.sunrisedentalclinic.dao.StaffDAO;
import com.sunrisedentalclinic.domain.*;
import com.sunrisedentalclinic.service.IAdminService;
import com.sunrisedentalclinic.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/admin/staff")
public class ManageStaffServlet extends HttpServlet {

    private final IAdminService adminService;
    private final StaffDAO staffDAO;

    public ManageStaffServlet() {
        this(ServiceFactory.getAdminService(), ServiceFactory.getStaffDAO());
    }

    public ManageStaffServlet(IAdminService adminService, StaffDAO staffDAO) {
        this.adminService = adminService;
        this.staffDAO = staffDAO;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.hasRole(request, "ADMIN")) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<Staff> staffList = staffDAO.findAll();
        request.setAttribute("staffList", staffList);
        request.getRequestDispatcher("/manage-staff.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.hasRole(request, "ADMIN")) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("delete".equals(action)) {
                String staffID = request.getParameter("staffID");
                Staff dummy = new Receptionist(0, "", "", "", staffID, "", "");
                adminService.manageStaff("delete", dummy);
                request.setAttribute("successMessage", "Staff " + staffID + " deleted.");
            } else {
                Staff staff = buildStaffFromRequest(request);
                adminService.manageStaff(action, staff);
                request.setAttribute("successMessage", "Staff " + staff.getStaffID() + " " + action + "d successfully.");
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }

        // Reload the list and redisplay
        List<Staff> staffList = staffDAO.findAll();
        request.setAttribute("staffList", staffList);
        request.getRequestDispatcher("/manage-staff.jsp").forward(request, response);
    }

    private Staff buildStaffFromRequest(HttpServletRequest request) {
        String role = request.getParameter("role");
        String personIdStr = "0"; // AUTO_INCREMENT — value ignored by StaffDAO.save()
        int personID = Integer.parseInt(personIdStr);
        String name = request.getParameter("name");
        String contactNo = request.getParameter("contactNo");
        String address = request.getParameter("address");
        String staffID = request.getParameter("staffID");
        String username = request.getParameter("username");
        String plainPassword = request.getParameter("password");
        String passwordHash = PasswordUtil.hash(plainPassword);

        switch (role) {
            case "DENTIST":
                String specialization = request.getParameter("specialization");
                BigDecimal consultationFee = new BigDecimal(request.getParameter("consultationFee"));
                return new Dentist(personID, name, contactNo, address, staffID, username, passwordHash,
                        specialization, consultationFee);
            case "RECEPTIONIST":
                return new Receptionist(personID, name, contactNo, address, staffID, username, passwordHash);
            case "ADMIN":
                return new Admin(personID, name, contactNo, address, staffID, username, passwordHash);
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
}
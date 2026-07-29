package com.sunrisedentalclinic.web;

import com.sunrisedentalclinic.domain.Bill;
import com.sunrisedentalclinic.domain.Session;
import com.sunrisedentalclinic.exception.AppointmentNotFoundException;
import com.sunrisedentalclinic.service.IBillingService;
import com.sunrisedentalclinic.service.impl.ClinicFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillingServletTest {

    @Mock private ClinicFacade clinicFacade;
    @Mock private IBillingService billingService;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HttpSession httpSession;

    private BillingServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new BillingServlet(clinicFacade, billingService);

        Session appSession = new Session("S1", LocalDateTime.now(), LocalDateTime.now().plusMinutes(30));
        appSession.setStaffID("R001");
        appSession.setRole("RECEPTIONIST");

        when(request.getSession(false)).thenReturn(httpSession);
        when(httpSession.getAttribute("appSession")).thenReturn(appSession);
    }

    @Test
    void doPost_noDiscount_forwardsToReceipt() throws Exception {
        when(request.getParameter("appointmentNo")).thenReturn("APT001");
        when(request.getParameter("discountPercent")).thenReturn("");
        when(request.getParameter("action")).thenReturn("generate");

        Bill bill = new Bill("B001", new BigDecimal("2500.00"), new BigDecimal("3500.00"));
        when(clinicFacade.generateBill("APT001")).thenReturn(bill);

        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/bill-receipt.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(dispatcher).forward(request, response);
        verify(billingService, never()).applyDiscount(any(), any());
        verify(request).setAttribute("bill", bill);
    }

    @Test
    void doPost_withDiscount_appliesDiscountBeforeForwarding() throws Exception {
        when(request.getParameter("appointmentNo")).thenReturn("APT001");
        when(request.getParameter("discountPercent")).thenReturn("10");
        when(request.getParameter("action")).thenReturn("generate");

        Bill originalBill = new Bill("B001", new BigDecimal("2500.00"), new BigDecimal("3500.00"));
        Bill discountedBill = new Bill("B001", new BigDecimal("2500.00"), new BigDecimal("3500.00"));

        when(clinicFacade.generateBill("APT001")).thenReturn(originalBill);
        when(billingService.applyDiscount(eq(originalBill), eq(new BigDecimal("10")))).thenReturn(discountedBill);

        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/bill-receipt.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(billingService).applyDiscount(originalBill, new BigDecimal("10"));
        verify(request).setAttribute("bill", discountedBill);
    }

    @Test
    void doPost_printAction_callsPrintReceipt() throws Exception {
        when(request.getParameter("appointmentNo")).thenReturn("APT001");
        when(request.getParameter("discountPercent")).thenReturn("");
        when(request.getParameter("action")).thenReturn("print");

        Bill bill = new Bill("B001", new BigDecimal("2500.00"), new BigDecimal("3500.00"));
        when(clinicFacade.generateBill("APT001")).thenReturn(bill);

        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/bill-receipt.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(billingService).printReceipt(bill.getBillID());
    }

    @Test
    void doPost_appointmentNotFound_forwardsBackWithError() throws Exception {
        when(request.getParameter("appointmentNo")).thenReturn("BADID");
        when(request.getParameter("discountPercent")).thenReturn("");

        when(clinicFacade.generateBill("BADID"))
                .thenThrow(new AppointmentNotFoundException("No appointment found: BADID"));

        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/generate-bill.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(dispatcher).forward(request, response);
        verify(request).setAttribute(eq("errorMessage"), anyString());
    }
}
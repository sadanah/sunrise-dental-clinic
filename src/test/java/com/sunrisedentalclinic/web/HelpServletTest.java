package com.sunrisedentalclinic.web;

import com.sunrisedentalclinic.domain.Session;
import com.sunrisedentalclinic.service.IHelpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HelpServletTest {

    @Mock private IHelpService helpService;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HttpSession httpSession;

    private HelpServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new HelpServlet(helpService);
    }

    @Test
    void doGet_unauthenticated_redirectsToLogin() throws Exception {
        when(request.getSession(false)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("login.jsp");
        verifyNoInteractions(helpService);
    }

    @Test
    void doGet_authenticated_noTopicSelected_showsTopicListOnly() throws Exception {
        Session session = new Session("S1", LocalDateTime.now(), LocalDateTime.now().plusMinutes(30));
        session.setRole("RECEPTIONIST");
        when(request.getSession(false)).thenReturn(httpSession);
        when(httpSession.getAttribute("appSession")).thenReturn(session);

        List<String> topics = Arrays.asList("login", "register-appointment");
        when(helpService.listHelpTopics()).thenReturn(topics);
        when(request.getParameter("topic")).thenReturn(null);

        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/help.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request).setAttribute("topics", topics);
        verify(request, never()).setAttribute(eq("helpContent"), any());
        verify(dispatcher).forward(request, response);
    }

    @Test
    void doGet_authenticated_topicSelected_showsHelpContent() throws Exception {
        Session session = new Session("S1", LocalDateTime.now(), LocalDateTime.now().plusMinutes(30));
        session.setRole("ADMIN");
        when(request.getSession(false)).thenReturn(httpSession);
        when(httpSession.getAttribute("appSession")).thenReturn(session);

        when(helpService.listHelpTopics()).thenReturn(Arrays.asList("login"));
        when(request.getParameter("topic")).thenReturn("login");
        when(helpService.displayHelp("login")).thenReturn("Enter your username and password...");

        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/help.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request).setAttribute("selectedTopic", "login");
        verify(request).setAttribute("helpContent", "Enter your username and password...");
        verify(dispatcher).forward(request, response);
    }
}
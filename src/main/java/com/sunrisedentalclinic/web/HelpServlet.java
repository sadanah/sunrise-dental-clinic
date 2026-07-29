package com.sunrisedentalclinic.web;

import com.sunrisedentalclinic.service.IHelpService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/help")
public class HelpServlet extends HttpServlet {

    private final IHelpService helpService;

    public HelpServlet() {
        this(ServiceFactory.getHelpService());
    }

    public HelpServlet(IHelpService helpService) {
        this.helpService = helpService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtil.isAuthenticated(request)) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<String> topics = helpService.listHelpTopics();
        request.setAttribute("topics", topics);

        String selectedTopic = request.getParameter("topic");
        if (selectedTopic != null && !selectedTopic.trim().isEmpty()) {
            String content = helpService.displayHelp(selectedTopic);
            request.setAttribute("selectedTopic", selectedTopic);
            request.setAttribute("helpContent", content);
        }

        request.getRequestDispatcher("/help.jsp").forward(request, response);
    }
}
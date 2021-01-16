package com.sussia.soa1.servlets;

import com.sussia.soa1.repositories.HumanBeingRepository;
import com.sussia.soa1.services.HumanBeingService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;


@WebServlet("/human-beings/name/starts_with/*")
public class NameStartsWithServlet extends HttpServlet {
    @Autowired
    public HumanBeingService service;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name;
        try {
             name = URLDecoder.decode(req.getRequestURL().toString().split("starts_with/")[1], "UTF-8");
        } catch (Exception e) {
            resp.setStatus(400);
            return;
        }

        try {
            String beings = service.getAllByName(name);
            resp.setContentType("application/xml");
            resp.getWriter().println(beings);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
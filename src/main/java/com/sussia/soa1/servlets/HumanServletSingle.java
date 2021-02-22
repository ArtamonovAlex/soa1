package com.sussia.soa1.servlets;

import com.sussia.soa1.SpringUtils;
import com.sussia.soa1.services.HumanBeingService;
import org.xml.sax.SAXParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.UnmarshalException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.stream.Collectors;

@WebServlet("/human-beings/*")
public class HumanServletSingle extends HttpServlet {

    private HumanBeingService service = SpringUtils.ctx.getBean(HumanBeingService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long nullableId = parseId(req.getRequestURL().toString());
        long id;
        if (nullableId == null) {
            resp.setStatus(404);
            return;
        }
        id = nullableId;
        try {
            String being = service.getById(id);
            if (being == null) {
                resp.setStatus(404);
                return;
            }
            resp.setContentType("application/xml");
            resp.getWriter().println(being);
            resp.setStatus(200);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        Long nullableId = parseId(req.getRequestURL().toString());
        long id;
        if (nullableId == null) {
            resp.setStatus(404);
            return;
        }
        id = nullableId;

        try {
            String being = service.getById(id);
            if (being == null) {
                resp.setStatus(404);
                return;
            }
            service.delete(id);
            resp.setStatus(204);

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long nullableId = parseId(req.getRequestURL().toString());
        long id;
        if (nullableId == null) {
            resp.setStatus(404);
            return;
        }
        id = nullableId;

        try {
            String being = service.getById(id);
            if (being == null) {
                resp.setStatus(404);
                return;
            }
            String xml =  new BufferedReader(new InputStreamReader(req.getInputStream())).lines().collect(
                    Collectors.joining("\n"));
            if (xml.trim().length() == 0) {
                // return error that jsonBody is empty
                resp.setStatus(400);
                return;
            }
            service.save(xml, id);
            resp.setStatus(204);
        }
        catch (UnmarshalException e) {
            Throwable linked = e.getLinkedException();
            if (linked instanceof SAXParseException) {
                SAXParseException parseExp = (SAXParseException) linked;
                System.out.println(parseExp.getMessage().replaceFirst(".*: ", ""));
                resp.setStatus(400);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long nullableId = parseId(req.getRequestURL().toString());
        if (nullableId == null) {
            resp.setStatus(404);
        } else {
            resp.setStatus(405);
        }
    }

    private Long parseId(String query) {
        String idStr;
        long id;
        try {
            idStr = URLDecoder.decode(query.split("human-beings/")[1], "UTF-8");
            id = Long.parseLong(idStr);
            if (id < 1) throw new Exception("");
        } catch (Exception e) {
            return null;
        }
        return id;
    }
}


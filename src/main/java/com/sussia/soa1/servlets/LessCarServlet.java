package com.sussia.soa1.servlets;


import com.sussia.soa1.SpringUtils;
import com.sussia.soa1.repositories.HumanBeingRepository;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

@WebServlet("/human-beings/count/car/cool/less/*")
public class LessCarServlet extends HttpServlet {

    public HumanBeingRepository repository = SpringUtils.ctx.getBean(HumanBeingRepository.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String coolStr;
        Boolean cool;
        try {
            coolStr = URLDecoder.decode(req.getRequestURL().toString().split("less/")[1], "UTF-8");
            if (coolStr.split("/").length > 1) {
                resp.setStatus(404);
                return;
            }
            switch (coolStr) {
                case "true":
                    cool = true;
                    break;
                case "false":
                    cool = false;
                    break;
                case "null":
                    resp.getWriter().println(0);
                    return;
                default:
                    resp.setStatus(400);
                    return;
            }
            long count = repository.countAllByCar_CoolLessThan(cool);
            long nullCount = repository.countAllByCar_Cool(null);
            resp.getWriter().println(count + nullCount);
        } catch (Exception e) {
            resp.setStatus(400);
        }
    }
}

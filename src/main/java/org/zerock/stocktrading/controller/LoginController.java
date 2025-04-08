package org.zerock.stocktrading.controller;

import lombok.extern.log4j.Log4j2;
import org.zerock.stocktrading.dto.MemberDTO;
import org.zerock.stocktrading.service.MemberSerivce;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginController", urlPatterns = "/login")
@Log4j2
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        log.info("login get.....");
        req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        log.info("login post....");
        String user_id = req.getParameter("user_id");
        String password = req.getParameter("password");

        try{
             MemberDTO memberDTO = MemberSerivce.INSTANCE.login(user_id,password);
             req.setAttribute("memberDTO",memberDTO);
             req.getRequestDispatcher("/WEB-INF/main.jsp").forward(req, resp);
        }catch (Exception e){
            resp.sendRedirect("/login?result=error");
        }
    }


}

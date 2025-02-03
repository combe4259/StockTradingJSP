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

@WebServlet(urlPatterns = "/register")
@Log4j2
public class RegisterController extends HttpServlet {

    private MemberSerivce memberSerivce = MemberSerivce.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        log.info("register get....");

        req.getRequestDispatcher("/WEB-INF/register.jsp").forward(req,resp);;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        log.info("register post...");

        MemberDTO memberDTO = MemberDTO.builder()
                        .user_id(req.getParameter("user_id"))
                        .password(req.getParameter("password"))
                        .username(req.getParameter("username"))
                        .build();

        try{
            memberSerivce.register(memberDTO);
            log.info(memberDTO);
            log.info("-------------------");
            log.info("register success");
            resp.sendRedirect("/login");

        }catch (Exception e){
            log.info("REGISTER error");
            resp.sendRedirect("/register?result=error");
        }




    }
}

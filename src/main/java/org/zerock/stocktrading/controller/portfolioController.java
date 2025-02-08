package org.zerock.stocktrading.controller;

import lombok.extern.log4j.Log4j2;
import org.zerock.stocktrading.manager.WebSocketManager;
import org.zerock.stocktrading.service.PortfolioService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


@WebServlet(urlPatterns = "/portfolio")
@Log4j2
public class portfolioController extends HttpServlet {
    private final PortfolioService portfolioService = new PortfolioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{

        log.info("portfolio GET....");
        try {
//             모든 데이터 조회
            Map<String, String> portfolioData = portfolioService.getPortfolioData();
            log.info("포트폴리오 데이터 예시 출력"+portfolioData);

            // JSP로 데이터 전달
            req.setAttribute("portfolioData", portfolioData);
            req.getRequestDispatcher("/WEB-INF/portfolio.jsp").forward(req, resp);
        } catch (Exception e) {
            log.error("포트폴리오 데이터 조회 실패", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "포트폴리오 조회 실패");
        }
    }
}

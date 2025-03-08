package org.zerock.stocktrading.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.zerock.stocktrading.service.PortfolioService;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


@WebServlet(urlPatterns = "/stockcode", asyncSupported = true)
@Log4j2
public class PortfolioController extends HttpServlet {
    private final PortfolioService portfolioService = new PortfolioService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException{
        log.info("Portfolio GET....");
        req.getRequestDispatcher("/WEB-INF/stockcode.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("Portfolio POST......");
        String stockCode = req.getParameter("stockCode");

        // 비동기 처리를 시작
        AsyncContext asyncContext = req.startAsync();

        CompletableFuture<Map<String, String>> portfolioDataFuture = portfolioService.getPortfolioData(stockCode);
        portfolioDataFuture.thenAccept(portfolioData -> {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> priceData = objectMapper.readValue(portfolioData.get("price"), Map.class);
                Map<String, Object> balanceData = objectMapper.readValue(portfolioData.get("balance"), Map.class);
                Map<String, Object> tradesData = objectMapper.readValue(portfolioData.get("trades"), Map.class);
                Map<String, Object> orderData = objectMapper.readValue(portfolioData.get("order"), Map.class);

                // asyncContext의 request를 사용
                asyncContext.getRequest().setAttribute("priceData", priceData);
                asyncContext.getRequest().setAttribute("balanceData", balanceData);
                asyncContext.getRequest().setAttribute("tradesData", tradesData);
                asyncContext.getRequest().setAttribute("orderData", orderData);

                // 비동기 디스패치로 포워딩 처리
                asyncContext.dispatch("/WEB-INF/portfolio.jsp");
            } catch (Exception e) {
                log.error("포트폴리오 데이터 처리 중 오류 발생", e);
                try {
                    HttpServletResponse asyncResp = (HttpServletResponse) asyncContext.getResponse();
                    asyncResp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "포트폴리오 데이터 처리 실패");
                } catch (IOException ioException) {
                    log.error("응답 오류", ioException);
                } finally {
                    asyncContext.complete();
                }
            }
        });
    }

}

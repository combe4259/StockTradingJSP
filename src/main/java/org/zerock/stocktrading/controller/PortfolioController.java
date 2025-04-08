package org.zerock.stocktrading.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.extern.log4j.Log4j2;
import org.zerock.stocktrading.service.PortfolioService;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
@WebServlet(urlPatterns = "/stockcode", asyncSupported = true)
@Log4j2
public class PortfolioController extends HttpServlet {

    private final PortfolioService portfolioService = new PortfolioService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Portfolio GET request");
        req.getRequestDispatcher("/WEB-INF/stockcode.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("Portfolio POST request received");
        String stockCode = req.getParameter("stockCode");
        AsyncContext asyncContext = req.startAsync();

        portfolioService.getPortfolioData(stockCode)
                .handle((data, ex) -> {
                    if (ex != null) {
                        handleException(asyncContext, ex);
                        return null;
                    }
                    return data;
                })
                .thenAccept(data -> {
                    if (data != null) {
                        processDataAndDispatch(asyncContext, data);
                    } else {
                        sendErrorResponse(asyncContext, 500, "데이터 처리 실패");
                    }
                });
    }

    private void handleException(AsyncContext ctx, Throwable ex) {
        Throwable rootCause = ex.getCause() != null ? ex.getCause() : ex;
        log.error("Error processing portfolio request", rootCause);

        if (rootCause instanceof RequestNotPermitted) {
            sendErrorResponse(ctx, 429, "초당 2회만 요청 가능합니다");
        } else {
            sendErrorResponse(ctx, 500, "서버 내부 오류: " + rootCause.getMessage());
        }
    }

    private void sendErrorResponse(AsyncContext ctx, int statusCode, String message) {
        try {
            HttpServletResponse response = (HttpServletResponse) ctx.getResponse();
            response.sendError(statusCode, message);
        } catch (IOException e) {
            log.error("Failed to send error response: {}", e.getMessage());
        } finally {
            ctx.complete();
        }
    }

    private void processDataAndDispatch(AsyncContext ctx, Map<String, String> data) {
        try {
            // 1. 주가 데이터 파싱 (output이 단일 객체)
            Map<String, Object> priceData = objectMapper.readValue(data.get("price"), Map.class);
            Map<String, Object> priceOutput = (Map<String, Object>) priceData.get("output");

            // 2. 잔고 데이터 파싱 (output1, output2는 리스트)
            Map<String, Object> balanceData = objectMapper.readValue(data.get("balance"), Map.class);
            List<Map<String, Object>> output1 = (List<Map<String, Object>>) balanceData.get("output1");
            List<Map<String, Object>> output2 = (List<Map<String, Object>>) balanceData.get("output2");

//            // 3. 체결 데이터 파싱 (output은 리스트)
//            Map<String, Object> tradesData = objectMapper.readValue(data.get("trades"), Map.class);
//            List<Map<String, Object>> tradesOutput = (List<Map<String, Object>>) tradesData.get("output");


            // JSP로 전달
            HttpServletRequest req = (HttpServletRequest) ctx.getRequest();
            req.setAttribute("priceData", priceOutput); // 주가 정보
            req.setAttribute("output1", output1); // 보유 종목
            req.setAttribute("output2", output2); // 계좌 잔고
//            req.setAttribute("tradesData", tradesOutput); // 체결 내역


            ctx.dispatch("/WEB-INF/portfolio.jsp");

        } catch (Exception e) {
            log.error("Data parsing error", e);
            sendErrorResponse(ctx, 500, "데이터 파싱 실패: " + e.getMessage());
        }
    }
}




//주식 잔고 조회 ok
//주식 현재가 Ok
package org.zerock.stocktrading.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.extern.log4j.Log4j2;
import org.zerock.stocktrading.dto.BalanceDTO;
import org.zerock.stocktrading.dto.InquireCcnlDTO;
import org.zerock.stocktrading.dto.InquirePriceDTO;
import org.zerock.stocktrading.dto.PsblOrderDTO;
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

    private void processDataAndDispatch(AsyncContext ctx, Map<String, Object> data) {
        try {
            InquirePriceDTO priceData = (InquirePriceDTO) data.get("price");
            BalanceDTO balanceData = (BalanceDTO) data.get("balance");
            InquireCcnlDTO tradesData = (InquireCcnlDTO) data.get("trades");
            PsblOrderDTO orderData = (PsblOrderDTO) data.get("order");

            HttpServletRequest req = (HttpServletRequest) ctx.getRequest();
            req.setAttribute("priceData", priceData.getOutput()); // 주가 정보
            req.setAttribute("balanceData", balanceData.getOutput1()); // 잔고 정보
            req.setAttribute("tradesData", tradesData.getOutput()); // 체결 내역
            req.setAttribute("orderData", orderData.getOutput()); // 주문 가능 정보


            ctx.dispatch("/WEB-INF/portfolio.jsp");
        } catch (Exception e) {
            log.error("Data 처리 오류", e);
            sendErrorResponse(ctx, 500, "데이터 처리 실패: " + e.getMessage());
        }
    }

}

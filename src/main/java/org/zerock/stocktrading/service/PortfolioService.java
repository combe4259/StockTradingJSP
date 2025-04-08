package org.zerock.stocktrading.service;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.extern.log4j.Log4j2;
import org.zerock.stocktrading.manager.ConfigManager;
import org.zerock.stocktrading.util.HttpClientUtil;
import org.zerock.stocktrading.util.QueryStringBuilder;
import org.zerock.stocktrading.util.RateLimiterConfigFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Log4j2
public class PortfolioService {

    private final RateLimiter rateLimiter = RateLimiterConfigFactory.create();
    // 추후 계좌 동적으로 처리해야 함
    private static final String ACCOUNT_NUMBER = "50124248";
    private static final String PRODUCT_CODE = "01";

    private final String domain;

    // 후속 API 호출 타이밍 조절을 위한 스케줄러 (재사용)
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public PortfolioService() {
        this.domain = ConfigManager.getProperty("domain.mock");
        if (this.domain == null) {
            throw new IllegalStateException("Domain is missing");
        }
    }

    // 잔고조회
    public String getBalanceInquiry() throws Exception {
        try {
            return RateLimiter.decorateSupplier(rateLimiter, () -> {
                try {
                    String endpoint = domain + "/uapi/domestic-stock/v1/trading/inquire-balance";
                    String queryString = new QueryStringBuilder()
                            .addParam("CANO", ACCOUNT_NUMBER)
                            .addParam("ACNT_PRDT_CD", PRODUCT_CODE)
                            .addParam("AFHR_FLPR_YN", "N")
                            .addParam("OFL_YN", "")
                            .addParam("INQR_DVSN", "01")
                            .addParam("UNPR_DVSN", "01")
                            .addParam("FUND_STTL_ICLD_YN", "N")
                            .addParam("FNCG_AMT_AUTO_RDPT_YN", "N")
                            .addParam("PRCS_DVSN", "00")
                            .addParam("CTX_AREA_FK100", "")
                            .addParam("CTX_AREA_NK100", "")
                            .build();
                    String tr_id = "VTTC8434R";
                    log.info("잔고조회 시작: {}", endpoint);

                    String response = HttpClientUtil.sendGetRequest(endpoint, queryString, tr_id);
                    return response;
                } catch (Exception e) {
                    log.error("잔고조회 실패", e);
                    throw new RuntimeException("잔고조회 중 오류 발생", e);
                }
            }).get();
        } catch (RequestNotPermitted e) {
            log.error("API 호출 제한 초과: {}", e.getMessage());
            throw new Exception("API 호출 한도 초과");
        }
    }

    // 주식현재가 체결
    public String getInquireCcnl(String stockCode) throws Exception {
        try {
            return RateLimiter.decorateSupplier(rateLimiter, () -> {
                try {
                    String endpoint = domain + "/uapi/domestic-stock/v1/quotations/inquire-ccnl";
                    String queryString = new QueryStringBuilder()
                            .addParam("fid_cond_mrkt_div_code", "J")
                            .addParam("fid_input_iscd", stockCode)
                            .build();
                    String response = HttpClientUtil.sendGetRequest(endpoint, queryString, "FHKST01010100");
                    log.debug("주식 현재가 조회 응답: {}", response);
                    return response;
                } catch (Exception e) {
                    log.error("주식 현재가 조회 실패: {}", e.getMessage());
                    throw new RuntimeException("주식 현재가 조회 중 오류 발생", e);
                }
            }).get();
        } catch (RequestNotPermitted e) {
            log.error("API 호출 제한 초과: {}", e.getMessage());
            throw new Exception("API 호출 한도 초과");
        }
    }

    // 주식현재가 조회
    public String getPriceInquiry(String stockCode) throws Exception {
        try {
            return RateLimiter.decorateSupplier(rateLimiter, () -> {
                try {
                    String endpoint = domain + "/uapi/domestic-stock/v1/quotations/inquire-price";
                    String queryString = new QueryStringBuilder()
                            .addParam("fid_cond_mrkt_div_code", "J")
                            .addParam("fid_input_iscd", stockCode)
                            .build();
                    String response = HttpClientUtil.sendGetRequest(endpoint, queryString, "FHKST01010100");
                    log.debug("현재가 체결 조회 응답: {}", response);
                    return response;
                } catch (Exception e) {
                    log.error("현재가 체결 조회 실패: {}", e.getMessage());
                    throw new RuntimeException("현재가 체결 조회 중 오류 발생", e);
                }
            }).get();
        } catch (RequestNotPermitted e) {
            log.error("API 호출 제한 초과: {}", e.getMessage());
            throw new Exception("API 호출 한도 초과");
        }
    }

    // 매수가능 조회
    public String getPsblOrder(String stockCode) throws Exception {
        try {
            return RateLimiter.decorateSupplier(rateLimiter, () -> {
                try {
                    String endpoint = domain + "/uapi/domestic-stock/v1/trading/inquire-psbl-order";
                    String queryString = new QueryStringBuilder()
                            .addParam("CANO", ACCOUNT_NUMBER)
                            .addParam("ACNT_PRDT_CD", PRODUCT_CODE)
                            .addParam("PDNO", stockCode)
                            .addParam("ORD_UNPR", "0")
                            .addParam("ORD_DVSN", "01")
                            .addParam("CMA_EVLU_AMT_ICLD_YN", "N")
                            .addParam("OVRS_ICLD_YN", "N")
                            .build();
                    String tr_id = "VTTC8908R";

                    log.info("매수가능 조회 시작");
                    String response = HttpClientUtil.sendGetRequest(endpoint, queryString, tr_id);
                    log.debug("매수 가능 조회 응답: {}", response);
                    return response;
                } catch (IOException e) {
                    log.error("매수 가능 조회 실패: {}", e.getMessage());
                    throw new RuntimeException("매수 가능 조회 중 오류 발생", e);
                }
            }).get();
        } catch (RequestNotPermitted e) {
            log.error("API 호출 제한 초과: {}", e.getMessage());
            throw new Exception("API 호출 한도 초과");
        }
    }

    // 개선된 getPortfolioData() - 후속 호출을 ScheduledExecutorService를 통해 지연 실행
    public CompletableFuture<Map<String, String>> getPortfolioData(String stockCode) {
        CompletableFuture<String> priceFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return getPriceInquiry(stockCode);
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });

        CompletableFuture<String> balanceFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return getBalanceInquiry();
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });

        CompletableFuture<Map<String, String>> initialData = priceFuture.thenCombine(balanceFuture, (price, balance) -> {
            Map<String, String> data = new HashMap<>();
            data.put("price", price);
            data.put("balance", balance);
            return data;
        });

        return initialData.thenCompose(data -> CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000); // 1초 지연
                data.put("trades", getInquireCcnl(stockCode));
                data.put("order", getPsblOrder(stockCode));
                return data;
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }));
    }

}

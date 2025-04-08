package org.zerock.stocktrading.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.zerock.stocktrading.dto.BalanceDTO;
import org.zerock.stocktrading.dto.InquireCcnlDTO;
import org.zerock.stocktrading.dto.InquirePriceDTO;
import org.zerock.stocktrading.dto.PsblOrderDTO;
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

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RateLimiter rateLimiter = RateLimiterConfigFactory.create();
    // 추후 계좌 동적으로 처리해야 함
    private static final String ACCOUNT_NUMBER = "50124248";
    private static final String PRODUCT_CODE = "01";

    private final String domain;

    public PortfolioService() {
        this.domain = ConfigManager.getProperty("domain.mock");
        if (this.domain == null) {
            throw new IllegalStateException("Domain is missing");
        }
    }

    // 잔고조회
    public BalanceDTO getBalanceInquiry() throws Exception {
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

                    String responseJson = HttpClientUtil.sendGetRequest(endpoint, queryString, tr_id);
                    BalanceDTO response = objectMapper.readValue(responseJson, BalanceDTO.class);
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
    public InquireCcnlDTO getInquireCcnl(String stockCode) throws Exception {
        try {
            return RateLimiter.decorateSupplier(rateLimiter, () -> {
                try {
                    String endpoint = domain + "/uapi/domestic-stock/v1/quotations/inquire-ccnl";
                    String queryString = new QueryStringBuilder()
                            .addParam("fid_cond_mrkt_div_code", "J")
                            .addParam("fid_input_iscd", stockCode)
                            .build();
                    String responseJson = HttpClientUtil.sendGetRequest(endpoint, queryString, "FHKST01010300");
                    log.info("주식 현재가 조회 응답: {}", responseJson);
                    InquireCcnlDTO response = objectMapper.readValue(responseJson, InquireCcnlDTO.class);
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
    public InquirePriceDTO getPriceInquiry(String stockCode) throws Exception {
        try {
            return RateLimiter.decorateSupplier(rateLimiter, () -> {
                try {
                    String endpoint = domain + "/uapi/domestic-stock/v1/quotations/inquire-price";
                    String queryString = new QueryStringBuilder()
                            .addParam("fid_cond_mrkt_div_code", "J")
                            .addParam("fid_input_iscd", stockCode)
                            .build();
                    String responseJson = HttpClientUtil.sendGetRequest(endpoint, queryString, "FHKST01010100");
                    log.debug("현재가 체결 조회 응답: {}", responseJson);
                    InquirePriceDTO response = objectMapper.readValue(responseJson, InquirePriceDTO.class);
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
    public PsblOrderDTO getPsblOrder(String stockCode) throws Exception {
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
                    String responseJson = HttpClientUtil.sendGetRequest(endpoint, queryString, tr_id);
                    log.debug("매수 가능 조회 응답: {}", responseJson);
                    PsblOrderDTO response = objectMapper.readValue(responseJson, PsblOrderDTO.class);
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


    public CompletableFuture<Map<String, Object>> getPortfolioData(String stockCode) {
        CompletableFuture<InquirePriceDTO> priceFuture = CompletableFuture.supplyAsync(() -> {
            try {
                log.info("성공 1");
                return getPriceInquiry(stockCode);
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });

        CompletableFuture<BalanceDTO> balanceFuture = CompletableFuture.supplyAsync(() -> {
            try {
                log.info("성공 2");
                return getBalanceInquiry();
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });

        return priceFuture.thenCombine(balanceFuture, (price, balance) -> {
            Map<String, Object> data = new HashMap<>();
            data.put("price", price);
            data.put("balance", balance);
            return data;
        }).thenCompose(data -> CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000); // 1초 지연
                log.info("성공 3");
                data.put("trades", getInquireCcnl(stockCode));
                log.info("성공 4");
                data.put("order", getPsblOrder(stockCode));
                return data;
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }));
    }


}

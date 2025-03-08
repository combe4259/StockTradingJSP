package org.zerock.stocktrading.service;

import lombok.extern.log4j.Log4j2;
import org.zerock.stocktrading.manager.ConfigManager;
import org.zerock.stocktrading.util.HttpClientUtil;
import org.zerock.stocktrading.util.QueryStringBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

@Log4j2
public class PortfolioService {
    //추후 계좌 동적으로 처리해야함
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
    public String getBalanceInquiry() throws Exception {
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
    }

    // 주식현재가 체결
    public String getInquireCcnl(String stockCode) throws Exception {
        try {
            String endpoint = domain + "/uapi/domestic-stock/v1/quotations/inquire-ccnl";
            String queryString = new QueryStringBuilder()
                    .addParam("fid_cond_mrkt_div_code","J")
                    .addParam("fid_input_iscd",stockCode)
                    .build();
            String tr_id = "FHKST01010300";

            log.info("현재가 체결 조회 시작");
            return HttpClientUtil.sendGetRequest(endpoint, queryString, tr_id);
        } catch (Exception e) {
            log.error("현재가 체결 조회 실패", e);
            throw new RuntimeException("현재가 체결 조회 중 오류 발생", e);
        }
    }

    // 주식현재가 조회
    public String getPriceInquiry(String stockCode) throws Exception {
        try {
            String endpoint = domain + "/uapi/domestic-stock/v1/quotations/inquire-price";
            String queryString = new QueryStringBuilder()
                    .addParam("fid_cond_mrkt_div_code","J")
                    .addParam("fid_input_iscd",stockCode)
                    .build();
            String tr_id = "FHKST01010100";


            log.info("현재가 조회 시작");
            String response = HttpClientUtil.sendGetRequest(endpoint, queryString, tr_id);
            log.debug("현재가 체결 조회 응답: {}", response);
            return response;
        } catch (Exception e) {
            log.error("현재가 체결 조회 실패: {}", e.getMessage());
            e.printStackTrace();  // 추가적으로 스택 트레이스 확인
            throw new RuntimeException("현재가 체결 조회 중 오류 발생", e);
        }
    }
//        매수가능 조회
    public String getPsblOrder (String stockCode) throws Exception {
        try {
            String endpoint = domain + "/uapi/domestic-stock/v1/trading/inquire-psbl-order";
            String queryString = new QueryStringBuilder()
                    .addParam("CANO",ACCOUNT_NUMBER)
                    .addParam("ACNT_PRDT_CD",PRODUCT_CODE)
                    .addParam("PDNO",stockCode)
                    .addParam("ORD_UNPR","0")
                    .addParam("ORD_DVSN","01")
                    .addParam("CMA_EVLU_AMT_ICLD_YN","N")
                    .addParam("OVRS_ICLD_YN","N")
                    .build();
            String tr_id = "VTTC8908R";

            log.info("매수가능 조회 시작");
            return HttpClientUtil.sendGetRequest(endpoint, queryString, tr_id);
            } catch (Exception e) {
                log.error("매수가능 조회 실패", e);
                throw new RuntimeException("매수가능 조회 중 오류 발생", e);
            }
        }

//        public Map<String, String> getPortfolioData (String stockCode) {

//
//            try {
//                data.put("price", getPriceInquiry(stockCode));
//                data.put("balance", getBalanceInquiry());
//                Thread.sleep(1000);
//                data.put("trades", getInquireCcnl(stockCode));
//                data.put("order", getPsblOrder(stockCode));
//
//                log.info("포트폴리오 데이터 조회 완료");
//                return data;
//            } catch (Exception e) {
//                log.error("포트폴리오 데이터 조회 실패", e);
//                throw new RuntimeException("포트폴리오 데이터 조회 중 오류 발생", e);
//            }
//        }
        public CompletableFuture<Map<String, String>> getPortfolioData(String stockCode){
            CompletableFuture<String> priceFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    return getPriceInquiry(stockCode);
                }catch (Exception e){
                    throw new CompletionException(e);
                }
            });

            CompletableFuture<String> balanceFuture = CompletableFuture.supplyAsync(()->{
                try {
                    return getBalanceInquiry();
                }catch (Exception e){
                    throw new CompletionException(e);
                }
            });

            CompletableFuture<String> tradesFuture = CompletableFuture.supplyAsync(()->{
                try {
                    return getInquireCcnl(stockCode);
                }catch (Exception e){
                    throw new CompletionException(e);
                }
            });

            CompletableFuture<String> orderFuture = CompletableFuture.supplyAsync(()->{
                try {
                    return getPsblOrder(stockCode);
                }catch (Exception e){
                    throw new CompletionException(e);
                }
            });
            return CompletableFuture.allOf(priceFuture, balanceFuture, tradesFuture, orderFuture)
                    .thenApply(v -> {
                        Map<String, String> data = new HashMap<>();
                        try {
                            data.put("price", priceFuture.get());
                            data.put("balance", balanceFuture.get());
                            data.put("trades", tradesFuture.get());
                            data.put("order", orderFuture.get());
                            log.info("포트폴리오 데이터 조회 완료");
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                        return data;
                    });


        }
    }

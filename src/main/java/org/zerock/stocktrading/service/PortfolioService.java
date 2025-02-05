package org.zerock.stocktrading.service;

import lombok.extern.log4j.Log4j2;
import org.zerock.stocktrading.ConfigManager;
import org.zerock.stocktrading.HttpClientUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class PortfolioService {
    private static final String ACCOUNT_NUMBER = "50124248";
    private static final String PRODUCT_CODE = "01";
    private static final String STOCK_CODE = "005930";

    private final String domain;

    public PortfolioService() {
        this.domain = ConfigManager.getProperty("domain.mock");
        if (this.domain == null) {
            throw new IllegalStateException("Domain configuration is missing");
        }
    }

    // 잔고조회
    public String getBalanceInquiry() throws Exception {
        try {
            String endpoint = domain + "/uapi/domestic-stock/v1/trading/inquire-balance";
            String queryString = String.format(
                    "?CANO=%s&ACNT_PRDT_CD=%s&AFHR_FLPR_YN=N&OFL_YN=&INQR_DVSN=01&UNPR_DVSN=01&" +
                            "FUND_STTL_ICLD_YN=N&FNCG_AMT_AUTO_RDPT_YN=N&PRCS_DVSN=00&CTX_AREA_FK100=&CTX_AREA_NK100=",
                    ACCOUNT_NUMBER, PRODUCT_CODE
            );
            String tr_id = "VTTC8434R";
            log.info("잔고조회 시작: {}", endpoint);

            String response = HttpClientUtil.sendGetRequest(endpoint, queryString, tr_id);
            log.debug("잔고조회 응답: {}", response);
            return response;
        } catch (Exception e) {
            log.error("잔고조회 실패", e);
            throw new RuntimeException("잔고조회 중 오류 발생", e);
        }
    }

    // 주식현재가 체결
    public String getInquireCcnl() throws Exception {
        try {
            String endpoint = domain + "/uapi/domestic-stock/v1/quotations/inquire-ccnl";
            String queryString = String.format(
                    "?fid_cond_mrkt_div_code=J&fid_input_iscd=%s",
                    STOCK_CODE
            );
            String tr_id = "FHKST01010300";

            log.info("현재가 체결 조회 시작");
            return HttpClientUtil.sendGetRequest(endpoint, queryString, tr_id);
        } catch (Exception e) {
            log.error("현재가 체결 조회 실패", e);
            throw new RuntimeException("현재가 체결 조회 중 오류 발생", e);
        }
    }

    // 주식현재가 조회
    public String getPriceInquiry() throws Exception {
        try {
            String endpoint = domain + "/uapi/domestic-stock/v1/quotations/inquire-price";
            String queryString = "?fid_cond_mrkt_div_code=J&fid_input_iscd=005930";
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
    public String getPsblOrder () throws Exception {
        try {
            String endpoint = domain + "/uapi/domestic-stock/v1/trading/inquire-psbl-order";
            String queryString = String.format(
                    "?CANO=%s&ACNT_PRDT_CD=%s&PDNO=%s&ORD_UNPR=0&ORD_DVSN=01&CMA_EVLU_AMT_ICLD_YN=N&OVRS_ICLD_YN=N",
                    ACCOUNT_NUMBER, PRODUCT_CODE, STOCK_CODE
            );
            String tr_id = "VTTC8908R";

            log.info("매수가능 조회 시작");
            return HttpClientUtil.sendGetRequest(endpoint, queryString, tr_id);
            } catch (Exception e) {
                log.error("매수가능 조회 실패", e);
                throw new RuntimeException("매수가능 조회 중 오류 발생", e);
            }
        }




        public Map<String, String> getPortfolioData () {
            Map<String, String> data = new HashMap<>();

            try {
                data.put("price", getPriceInquiry());
                Thread.sleep(1000);
                data.put("balance", getBalanceInquiry());
                Thread.sleep(1000);
                data.put("trades", getInquireCcnl());
                Thread.sleep(1000);
                data.put("order", getPsblOrder());

                log.info("포트폴리오 데이터 조회 완료");
                return data;
            } catch (Exception e) {
                log.error("포트폴리오 데이터 조회 실패", e);
                throw new RuntimeException("포트폴리오 데이터 조회 중 오류 발생", e);
            }
        }
    }

package org.zerock.stocktrading;


import lombok.extern.log4j.Log4j2;

@Log4j2
public class tempPortfolio {

    private final String domain;

    public tempPortfolio() {
        this.domain = ConfigManager.getProperty("domain.mock");
        if (this.domain == null) {
            throw new IllegalStateException("Domain configuration is missing");
        }
    }
//    public Map<String, String> getPortfolioData() throws Exception {
//        Map<String, String> data = new HashMap<>();
//
//        data.put("balance", getBalanceInquiry());
//        data.put("trades", getInquireCcnl());
//        data.put("price", getPriceInquiry());
//        data.put("order", getPsblOrder());
//        log.info("data");
//        return data;
//    }
    //잔고조회
    public String getBalanceInquiry() throws Exception{
        String endpoint = domain + "/uapi/domestic-stock/v1/trading/inquire-balance";
        String queryString = "?CANO=50124248&ACNT_PRDT_CD=01&AFHR_FLPR_YN=N&OFL_YN=&INQR_DVSN=01&UNPR_DVSN=01&FUND_STTL_ICLD_YN=N&FNCG_AMT_AUTO_RDPT_YN=N&PRCS_DVSN=00&CTX_AREA_FK100=&CTX_AREA_NK100=";
        String tr_id="VTTC8434R";
        log.info("잔고조회");
        return HttpClientUtil.sendGetRequest(endpoint, queryString, tr_id);
    }   //{{VTS}}/uapi/domestic-stock/v1/trading/inquire-balance?CANO={{CANO}}&ACNT_PRDT_CD=01&AFHR_FLPR_YN=N&OFL_YN=&INQR_DVSN=01&UNPR_DVSN=01&FUND_STTL_ICLD_YN=N&FNCG_AMT_AUTO_RDPT_YN=N&PRCS_DVSN=00&CTX_AREA_FK100=&CTX_AREA_NK100=
    //주식현재가 체결 (Conclusion 체결)


    public String getInquireCcnl() throws Exception{
        String endpoint = domain + "/uapi/domestic-stock/v1/quotations/inquire-ccnl";
        //나중에 동적입력이나 DTO로 바꾸기
        String fid_cond_mrkt_div_code ="J";
        String fid_input_iscd="005930";
        String queryString = "?fid_cond_mrkt_div_code="+fid_cond_mrkt_div_code+"&fid_input_iscd="+fid_input_iscd;
        String tr_id ="FHKST01010300";
        return HttpClientUtil.sendGetRequest(endpoint, queryString, tr_id);
    }

    //주식현재가 조회
    public String getPriceInquiry() throws Exception{
        //{{VTS}}/uapi/domestic-stock/v1/quotations/inquire-price?fid_cond_mrkt_div_code=J&fid_input_iscd=005930
        String endpoint = domain + "/uapi/domestic-stock/v1/quotations/inquire-price";
        String fid_cond_mrkt_div_code ="J";//fid_cond_mrkt_div_code //fid_input_iscd
        String fid_input_iscd = "005930";
        String queryString = "?fid_cond_mrkt_div_code="+fid_cond_mrkt_div_code+"&fid_input_iscd="+fid_input_iscd;
        String tr_id ="FHKST01010100";
        return HttpClientUtil.sendGetRequest(endpoint, queryString, tr_id);
    }

    //매수가능 조회
    public String getPsblOrder() throws Exception{
        String endpoint = domain + "/uapi/domestic-stock/v1/trading/inquire-psbl-order";
        String cano = "50124248"; // 계좌번호 (실제 값으로 변경)
        String acntPrdtCd = "01"; // 계좌상품코드
        String pdno = "005930"; // 삼성전자 종목 코드
        String ordUnpr = "0"; // 시장가 주문
        String ordDvsn = "01"; // 주문 구분 (시장가 주문)
        String cmaEvluAmtIcldYn = "N";
        String ovrsIcldYn = "N";

        // URL에 Query String 추가
        String queryString = String.format(
                "?CANO=%s&ACNT_PRDT_CD=%s&PDNO=%s&ORD_UNPR=%s&ORD_DVSN=%s&CMA_EVLU_AMT_ICLD_YN=%s&OVRS_ICLD_YN=%s",
                cano, acntPrdtCd, pdno, ordUnpr, ordDvsn, cmaEvluAmtIcldYn, ovrsIcldYn
        );
        String tr_id = "VTTC8908R";
        return HttpClientUtil.sendGetRequest(endpoint, queryString, tr_id);
    }









}
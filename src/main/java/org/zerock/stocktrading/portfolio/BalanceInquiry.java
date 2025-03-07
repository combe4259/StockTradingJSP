package org.zerock.stocktrading.portfolio;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.zerock.stocktrading.ConfigManager;
import org.zerock.stocktrading.HttpClientUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//주식 잔고 조회
@WebServlet(urlPatterns = "/balanceInquiry")
@Log4j2
public class BalanceInquiry extends HttpServlet {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException{
        log.info("balanceInqury get...");


        String domain = ConfigManager.getProperty("domain.mock");
        String endpoint = domain + "/uapi/domestic-stock/v1/trading/inquire-balance";
        String queryString = "?CANO=50124248&ACNT_PRDT_CD=01&AFHR_FLPR_YN=N&OFL_YN=&INQR_DVSN=01&UNPR_DVSN=01&FUND_STTL_ICLD_YN=N&FNCG_AMT_AUTO_RDPT_YN=N&PRCS_DVSN=00&CTX_AREA_FK100=&CTX_AREA_NK100=";
        String tr_id="VTTC8434R";

//        String cano = "50124248";
//        String acnt_prdt_cd = "01";
//        String afhr_flpr_yn ="N";
//        String ofl_yn="";
//        String inqr_dvsn ="01";
//        String unpr_dvsn="01";
//        String fund_sttl_icld_yn="N";
//        String fncg_amt_auto_rdpt_yn="N";
//        String prcs_dvsn="00";
//        String ctx_area_fk100 = "";                // 연속조회검색조건 (공란: 최초조회시)
//        String ctx_area_nk100 = "";

        try {
            String result = HttpClientUtil.sendGetRequest(endpoint, queryString, tr_id);
            req.setAttribute("balanceData", result);
            req.getRequestDispatcher("/WEB-INF/balanceInquiry.jsp").forward(req, resp);
        } catch (Exception e) {
            log.error("잔고 조회 실패", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "잔고 조회 실패");
        }




    }


}

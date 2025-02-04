package org.zerock.stocktrading;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@WebServlet(urlPatterns = "/balanceInquiry")
@Log4j2
public class BalanceInquiry extends HttpServlet {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException{

        log.info("balanceInqury get...");
        String accesstoken = ConfigManager.getProperty("accesstoken");
        if(accesstoken == null || accesstoken.isEmpty()){
            log.error("토큰 없음");
            return;
        }
        log.info("액세스 토큰 get" + accesstoken);
        String domain = ConfigManager.getProperty("domain.mock");
        String apiUrl = domain + "/uapi/domestic-stock/v1/trading/inquire-balance";

        String cano = "50124248";
        String acnt_prdt_cd = "01";
        String afhr_flpr_yn ="N";
        String ofl_yn="";
        String inqr_dvsn ="01";
        String unpr_dvsn="01";
        String fund_sttl_icld_yn="N";
        String fncg_amt_auto_rdpt_yn="N";
        String prcs_dvsn="00";
        String ctx_area_fk100 = "";                // 연속조회검색조건 (공란: 최초조회시)
        String ctx_area_nk100 = "";


//        String queryString = String.format(
//                "?CANO=%s&ACNT_PRDT_CD=%s&AFHR_FLPR_YN=%s&OFL_YN=%s&INQR_DVSN=%s&UNPR_DVSN=%s&FUND_STTL_ICLD_YN=%s&FNCG_AMT_AUTO_RDPT_YN=%s&PRCS_DVSN=%s&CTX_AREA_FK100=%s&CTX_AREA_NK100=%s",
//                cano, acnt_prdt_cd, afhr_flpr_yn, ofl_yn, inqr_dvsn, unpr_dvsn, fund_sttl_icld_yn, fncg_amt_auto_rdpt_yn, prcs_dvsn, ctx_area_fk100, ctx_area_nk100
//        );
        String queryString = "?CANO=50124248&ACNT_PRDT_CD=01&AFHR_FLPR_YN=N&OFL_YN=&INQR_DVSN=01&UNPR_DVSN=01&FUND_STTL_ICLD_YN=N&FNCG_AMT_AUTO_RDPT_YN=N&PRCS_DVSN=00&CTX_AREA_FK100=&CTX_AREA_NK100=";
        URL url = new URL(apiUrl + queryString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        conn.setRequestProperty("authorization", "Bearer " + accesstoken);
        conn.setRequestProperty("appkey", ConfigManager.getProperty("appkey"));
        conn.setRequestProperty("appsecret", ConfigManager.getProperty("appsecret"));
        conn.setRequestProperty("tr_id", "VTTC8434R");






        int responseCode = conn.getResponseCode();
        log.info(responseCode);

        //error 코드 확인
        if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {




        }

        StringBuilder responseStr = new StringBuilder();
        if(responseCode == HttpURLConnection.HTTP_OK){
            try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))){
                String line;
                while ((line = br.readLine()) !=null ){
                    responseStr.append(line);
                }
                log.info(responseStr);
            }

        }else {//에러 코드 생성하는법 배우기
            log.error("API 요청 실패. 응답 코드: " + responseCode);
            resp.getWriter().write("API 요청 실패. 응답 코드: " + responseCode);
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"));
            StringBuilder errorResponse = new StringBuilder();
            String line;
            while ((line = errorReader.readLine()) != null) {
                errorResponse.append(line);
            }
            log.error("API실패: " + errorResponse);
            return;
        }
        String response = responseStr.toString();
//        JsonNode rootNode = objectMapper.readTree(response);
//        String balancedata = rootNode.path("")
        req.setAttribute("balance", response);
        req.getRequestDispatcher("WEB-INF/balanceInqury.jsp").forward(req, resp);

    }


}

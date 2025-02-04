package org.zerock.stocktrading;

import lombok.extern.log4j.Log4j2;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
        import java.io.*;
        import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@WebServlet("/getOrder")
@Log4j2
public class ss extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("PsblOrder 조회 요청...");

        // 1. 액세스 토큰 발급
        String accessToken = "";
        try {
            accessToken = TokenManager.getAccessToken();
            log.info("발급된 액세스 토큰: " + accessToken);
        } catch (IOException e) {
            log.error("토큰 발급 실패", e);
            response.getWriter().write("토큰 발급 실패: " + e.getMessage());
            return;
        }

        // 2. 매수 가능 조회 API URL 구성 (모의 도메인 사용)
        String domain = ConfigManager.getProperty("domain.mock");
        String apiUrl = domain + "/uapi/domestic-stock/v1/trading/inquire-psbl-order";

        // 3. 요청 파라미터 (Query String 방식)
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

        URL url = new URL(apiUrl + queryString);
        log.info("API 요청 URL: " + url);

        // 4. HTTP 요청 설정
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("authorization", "Bearer " + accessToken);
        conn.setRequestProperty("appkey", ConfigManager.getProperty("appkey"));
        conn.setRequestProperty("appsecret", ConfigManager.getProperty("appsecret"));
        conn.setRequestProperty("tr_id", "VTTC8908R"); // 모의투자용 거래ID

        // 5. 응답 처리
        int responseCode = conn.getResponseCode();
        StringBuilder responseStr = new StringBuilder();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    responseStr.append(line);
                }
            }
            log.info("API 응답: " + responseStr.toString());
        } else {
            log.error("매수가능 조회 실패. 응답 코드: " + responseCode);
            response.getWriter().write("매수가능 조회 실패. 응답 코드: " + responseCode);
            return;
        }

        // 6. 결과를 JSP에 전달
        request.setAttribute("psblOrderResult", responseStr.toString());
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/displayPsblOrder.jsp");
        dispatcher.forward(request, response);
    }
}


package org.zerock.stocktrading;

import lombok.extern.log4j.Log4j2;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ParameterMetaData;

@WebServlet("/getPsblOrder")
@Log4j2
public class PsblOrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        log.info("PsblOrder.....");
        // 1. 액세스 토큰 발급 (TokenManager 사용)
        String accessToken = "";
        try {
            accessToken = TokenManager.getAccessToken();
            System.out.println(accessToken);
        } catch (IOException e) {
            response.getWriter().write("토큰 발급 실패: " + e.getMessage());
            return;
        }

        // 2. 매수가능 조회 API URL 구성 (모의 도메인 사용)
        String domain = ConfigManager.getProperty("domain.mock");
        String apiUrl = domain + "/uapi/domestic-stock/v1/trading/inquire-psbl-order";

        // 3. 요청 파라미터 (예시 값, 실제 계좌, 종목 정보에 맞게 수정)
        // Query Parameter는 보통 GET 요청에 URL에 포함되지만, 이 API는 JSON 요청 본문을 사용할 수 있습니다.
        String jsonInputString = "{"
                + "\"CANO\": \"50124248\","
                + "\"ACNT_PRDT_CD\": \"01\","
                + "\"PDNO\": \"005930\","
                + "\"ORD_UNPR\": \"55000\","
                + "\"ORD_DVSN\": \"00\","
                + "\"OVRS_ICLD_YN\": \"N\""
                + "\"CMA_EVLU_AMT_ICLD_YN\": \"N\","
                + "}";

        log.info(jsonInputString);
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("authorization", "Bearer " + accessToken);
        conn.setRequestProperty("appkey", ConfigManager.getProperty("appkey"));
        conn.setRequestProperty("appsecret", ConfigManager.getProperty("appsecret"));
        conn.setRequestProperty("tr_id", "VTTC8908R"); // 모의투자용 거래ID
        conn.setDoOutput(true);

        try(OutputStream os = conn.getOutputStream()){
            byte request_data[] = jsonInputString.getBytes("utf-8");
            os.write(request_data, 0, request_data.length);

        }



        // GET 요청 시에도 Body를 사용할 수 있는 경우가 있으나, HttpURLConnection은 GET에서 Body 사용을 권장하지 않습니다.
        // 만약 Body를 전송해야 한다면 POST 방식으로 전환하거나, Query String에 포함시켜야 합니다.
        // 여기서는 간단히 Query Parameter 없이 Header와 URL만 사용한다고 가정합니다.

        // 5. 응답 처리
        int responseCode = conn.getResponseCode();
        StringBuilder responseStr = new StringBuilder();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    responseStr.append(line);
                }
            }
        } else {
            response.getWriter().write("매수가능 조회 실패. 응답 코드: " + responseCode);
            return;
        }

        // 6. 결과를 JSP에 전달
        request.setAttribute("psblOrderResult", responseStr.toString());
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/displayPsblOrder.jsp");
        dispatcher.forward(request, response);
    }
}

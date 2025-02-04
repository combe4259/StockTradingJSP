package org.zerock.stocktrading;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.net.*;

@Log4j2
public class TokenManager {

    private static final String TOKEN_URL = ConfigManager.getProperty("domain.mock") + "/oauth2/tokenP";
    private static final String appKey = ConfigManager.getProperty("appkey");
    private static final String appSecret = ConfigManager.getProperty("appsecret");

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String getAccessToken() throws IOException {



        String jsonInputString = "{" +
                "\"grant_type\": \"client_credentials\"," +
                "\"appkey\": \"" + appKey + "\"," +
                "\"appsecret\": \"" + appSecret + "\"" +
                "}";

        URL url = new URL(TOKEN_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"));
            StringBuilder errorResponse = new StringBuilder();
            String line;
            while ((line = errorReader.readLine()) != null) {
                errorResponse.append(line);
            }
            log.error("토큰 발급 실패: " + errorResponse);
            //java.io.IOException: 토큰 발급 실패: {"error_description":"접근토큰 발급 잠시 후 다시 시도하세요(1분당 1회)","error_code":"EGW00133"}

            throw new IOException("토큰 발급 실패: " + errorResponse);
        }


        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("토큰 발급 실패, 응답 코드: " + responseCode);
        }

        StringBuilder responseStr = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                responseStr.append(responseLine.trim());
            }
        }
        log.info(responseStr);

        String response = responseStr.toString();
        // 토큰 추출: 예시 코드 (문자열 파싱, 실제로는 JSON 파싱 라이브러리 권장)
//        String token = response.split("\"access_token\"\\s*:\\s*\"")[1].split("\"")[0];
        JsonNode rootNode = objectMapper.readTree(response);
        String token = rootNode.path("access_token").asText();

        log.info("토큰 발급 성공: " + token);


        return token;

    }
}
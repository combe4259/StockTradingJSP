package org.zerock.stocktrading.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.zerock.stocktrading.manager.ConfigManager;
import org.zerock.stocktrading.manager.TokenManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
public class HttpClientUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String sendGetRequest(String endpoint, String queryString, String tr_id) throws IOException {
        String accesstoken = ConfigManager.getProperty("accesstoken");
        if (accesstoken == null || accesstoken.isEmpty()) {
            throw new IOException("액세스 토큰 없음");
        }

        URL url = new URL(endpoint + queryString);
        log.info("URL 출력 ******:" + url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("authorization", "Bearer " + accesstoken);
            conn.setRequestProperty("appkey", ConfigManager.getProperty("appkey"));
            conn.setRequestProperty("appsecret", ConfigManager.getProperty("appsecret"));
            conn.setRequestProperty("tr_id", tr_id);

            int responseCode = conn.getResponseCode();

            // 에러 응답 처리
            if (responseCode >= 400) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                    String errorResponse = br.lines().collect(Collectors.joining(System.lineSeparator()));
                    log.error("API 에러 응답: {}", errorResponse);
                }
                throw new IOException("Server returned HTTP response code: " + responseCode);
            }

            StringBuilder responseStr = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    responseStr.append(line);
                }
            }
            return responseStr.toString();
        } finally {
            if (conn != null) {
                log.info("연결 해제");
                conn.disconnect();
            }
        }
    }

}

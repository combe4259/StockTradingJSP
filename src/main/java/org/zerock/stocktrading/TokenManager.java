package org.zerock.stocktrading;
import java.io.*;
import java.net.*;


public class TokenManager {

    private static final String TOKEN_URL = ConfigManager.getProperty("domain.mock") + "/oauth2/tokenP";
    static String appKey = ConfigManager.getProperty("appkey");
    static String appSecret = ConfigManager.getProperty("appsecret");

    public static String getAccessToken() throws IOException {

        String jsonInputString = "{\r\n" +
                "    \"grant_type\": \"client_credentials\",\r\n" +
                "    \"appkey\": \"" + appKey + "\",\r\n" +
                "    \"appsecret\": \"" + appSecret + "\"\r\n" +
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

        // 응답 JSON 파싱 (단순 문자열 처리, 실제 개발 시 JSON 라이브러리 사용 권장)
        // 예: {"access_token": "abcd1234", "expires_in":3600, ...}
        // 여기서는 간단히 "access_token"을 추출하는 방법을 사용 (실제는 Gson, Jackson 등 사용)
        String response = responseStr.toString();
        // 토큰 추출: 예시 코드 (문자열 파싱, 실제로는 JSON 파싱 라이브러리 권장)
        String token = response.split("\"access_token\"\\s*:\\s*\"")[1].split("\"")[0];
        return token;

    }
}
package org.zerock.stocktrading.KISApi;

public class KIS {
    public static void main(String[] args) {
        try {
            // API 클라이언트 생성 (테스트 환경 사용)
            KISApiClient client = new KISApiClient(
                    "your-app-key",
                    "your-app-secret",
                    true  // 테스트 환경 사용
            );

            // 접근토큰 발급
            String tokenResponse = client.getAccessToken();
            System.out.println("토큰 응답: " + tokenResponse);

            // JSON 파싱하여 access_token 추출 (org.json 라이브러리 사용)
            JSONObject tokenJson = new JSONObject(tokenResponse);
            String accessToken = tokenJson.getString("access_token");

            // 삼성전자(005930) 현재가 조회
            String priceResponse = client.getStockPrice("005930", accessToken);
            System.out.println("시세 응답: " + priceResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
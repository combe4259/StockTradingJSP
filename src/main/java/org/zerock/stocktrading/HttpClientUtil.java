package org.zerock.stocktrading;

import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Log4j2
public class HttpClientUtil {

    public static String sendGetRequest(String endpoint, String queryString, String tr_id) throws IOException {

        String accesstoken = ConfigManager.getProperty("accesstoken");
        if(accesstoken == null || accesstoken.isEmpty()){
            throw new IOException("액세스 토큰 없음");
        }

        URL url = new URL(endpoint + queryString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("authorization", "Bearer " + accesstoken);
        conn.setRequestProperty("appkey", ConfigManager.getProperty("appkey"));
        conn.setRequestProperty("appsecret", ConfigManager.getProperty("appsecret"));
        conn.setRequestProperty("tr_id", tr_id);//동적 처리 해줘야할듯

        int responseCode = conn.getResponseCode();

        if(responseCode == HttpURLConnection.HTTP_FORBIDDEN){
            try(BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while((line = errorReader.readLine())!=null){
                    errorResponse.append(line);
                }
                log.info("Error:" + errorResponse);
                return errorResponse.toString();
            }
        }//close()해줘야하는거 아닌가

        if(responseCode != HttpURLConnection.HTTP_OK){
            throw new IOException("API 요청 실패, 응답 코드:" + responseCode);
        }

        StringBuilder responseStr = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
            String line;
            while ((line=br.readLine())!=null){
                responseStr.append(line);
            }
        }
        return responseStr.toString();


    }
}

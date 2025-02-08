package org.zerock.stocktrading.manager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


@Log4j2
public class WebSocketManager {
    private static final String appkey = ConfigManager.getProperty("appkey");
    private static final String appsecret = ConfigManager.getProperty("appsecret");
    private static final String domain = ConfigManager.getProperty("domain.mock");
    private static final String apiURL = domain + "/oauth2/Approval";
    private static final String JSONBody = "{"+
            "\"grant_type\": \"client_credentials\","+
            "\"appkey\": \"" +appkey+"\","+
            "\"secretkey\": \""+appsecret+"\""+
            "}";

    public static ObjectMapper objectMapper = new ObjectMapper();

    public static String getWS() throws IOException {

        URL url = new URL(apiURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("content-type", "application/json; utf-8");
        conn.setDoOutput(true);

        try(OutputStream os = conn.getOutputStream()){
            byte[] input = JSONBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if(responseCode >= 400){
            throw new IOException("웹소켓키 발급 실패");
        }

        StringBuilder responseStr= new StringBuilder();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"))){
            String line;
            while((line = br.readLine())!=null){
                responseStr.append(line);
            }

        }

        String response =responseStr.toString();
        JsonNode rootNode = objectMapper.readTree(response);
        String token = rootNode.path("approval_key").asText();

        log.info("웹소켓키발급 성공 " + token);

        return token;



    }



}

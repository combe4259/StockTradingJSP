<%--
  Created by IntelliJ IDEA.
  User: inter4259
  Date: 2025. 2. 5.
  Time: 오후 3:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="org.json.JSONArray, org.json.JSONObject" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%
    // JSON 데이터 받아오기
    String jsonData = (String) request.getAttribute("inquireccnl");

    // JSON 파싱
    JSONObject jsonObject = new JSONObject(jsonData);
    JSONArray outputArray = jsonObject.getJSONArray("output");
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>주식 체결 내역</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            padding: 20px;
            background-color: #f8f9fa;
        }
        h2 {
            text-align: center;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            background: white;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        th, td {
            padding: 10px;
            text-align: center;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #007bff;
            color: white;
        }
        tr:hover {
            background-color: #f1f1f1;
        }
    </style>
</head>
<body>

<h2>📈 주식 체결 내역</h2>

<table>
    <thead>
    <tr>
        <th>체결 시간</th>
        <th>현재가</th>
        <th>전일 대비</th>
        <th>전일 대비 기호</th>
        <th>체결 수량</th>
        <th>당일 상대 거래량</th>
        <th>전일 대비 등락률</th>
    </tr>
    </thead>
    <tbody>
    <%
        for (int i = 0; i < outputArray.length(); i++) {
            JSONObject stock = outputArray.getJSONObject(i);
    %>
    <tr>
        <td><%= stock.getString("stck_cntg_hour") %></td>
        <td><%= stock.getString("stck_prpr") %></td>
        <td><%= stock.getString("prdy_vrss") %></td>
        <td><%= stock.getString("prdy_vrss_sign") %></td>
        <td><%= stock.getString("cntg_vol") %></td>
        <td><%= stock.getString("tday_rltv") %></td>
        <td><%= stock.getString("prdy_ctrt") %>%</td>
    </tr>
    <% } %>
    </tbody>
</table>

</body>
</html>

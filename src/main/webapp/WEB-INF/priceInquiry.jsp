<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="org.json.JSONObject" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>주식 현재가 조회</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 20px;
            padding: 20px;
        }
        .container {
            width: 60%;
            margin: 0 auto;
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
        }
        h2 {
            text-align: center;
            color: #333;
        }
        .stock-info {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        .stock-info th, .stock-info td {
            border: 1px solid #ddd;
            padding: 10px;
            text-align: left;
        }
        .stock-info th {
            background-color: #007BFF;
            color: white;
        }
        .highlight {
            font-weight: bold;
            color: #d9534f;
        }
        .positive {
            color: #28a745;
        }
        .negative {
            color: #d9534f;
        }
    </style>
</head>
<body>

<div class="container">
    <h2>주식 현재가 조회 결과</h2>

    <%
        String jsonData = (String) request.getAttribute("priceData");
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONObject output = jsonObject.getJSONObject("output");

        String stockName = output.getString("rprs_mrkt_kor_name");
        String stockCode = output.getString("stck_shrn_iscd");
        String currentPrice = output.getString("stck_prpr");
        String changePrice = output.getString("prdy_vrss");
        String changeRate = output.getString("prdy_ctrt");
        String highestPrice = output.getString("stck_hgpr");
        String lowestPrice = output.getString("stck_lwpr");
        String volume = output.getString("acml_vol");
    %>

    <table class="stock-info">
        <tr>
            <th>종목명</th>
            <td><%= stockName %> (<%= stockCode %>)</td>
        </tr>
        <tr>
            <th>현재가</th>
            <td class="highlight"><%= currentPrice %> 원</td>
        </tr>
        <tr>
            <th>전일 대비</th>
            <td class="<%= changePrice.startsWith("-") ? "negative" : "positive" %>">
                <%= changePrice %> 원 ( <%= changeRate %> % )
            </td>
        </tr>
        <tr>
            <th>최고가</th>
            <td><%= highestPrice %> 원</td>
        </tr>
        <tr>
            <th>최저가</th>
            <td><%= lowestPrice %> 원</td>
        </tr>
        <tr>
            <th>거래량</th>
            <td><%= volume %> 주</td>
        </tr>
    </table>
</div>

</body>
</html>

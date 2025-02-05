<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="org.json.JSONArray, org.json.JSONObject" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%
    Map<String, String> portfolioData = (Map<String, String>)request.getAttribute("portfolioData");

    // JSON 파싱
    JSONObject priceJson = new JSONObject(portfolioData.get("price"));
    JSONObject balanceJson = new JSONObject(portfolioData.get("balance"));
    JSONObject tradesJson = new JSONObject(portfolioData.get("trades"));
    JSONObject orderJson = new JSONObject(portfolioData.get("order"));
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>포트폴리오 현황</title>
    <style>
        body {
            font-family: 'Noto Sans KR', sans-serif;
            margin: 20px;
            background-color: #f8f9fa;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }
        .card {
            background: white;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 12px;
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
<div class="container">
    <!-- 주가 정보 -->
    <div class="card">
        <h2>📊 주가 정보</h2>
        <table>
            <tr>
                <th>현재가</th>
                <th>전일대비</th>
                <th>등락률</th>
                <th>거래량</th>
            </tr>
            <tr>
                <td><%= priceJson.getJSONObject("output").getString("stck_prpr") %></td>
                <td><%= priceJson.getJSONObject("output").getString("prdy_vrss") %></td>
                <td><%= priceJson.getJSONObject("output").getString("prdy_ctrt") %>%</td>
                <td><%= priceJson.getJSONObject("output").getString("acml_vol") %></td>
            </tr>
        </table>
    </div>

    <!-- 잔고 정보 -->
    <div class="card">
        <h2>💰 잔고 정보</h2>
        <table>
            <tr>
                <th>보유수량</th>
                <th>매입단가</th>
                <th>평가금액</th>
                <th>평가손익</th>
            </tr>
            <tr>
                <td><%= balanceJson.getJSONArray("output1").getJSONObject(0).getString("hldg_qty") %></td>
                <td><%= balanceJson.getJSONArray("output1").getJSONObject(0).getString("pchs_avg_pric") %></td>
                <td><%= balanceJson.getJSONArray("output1").getJSONObject(0).getString("evlu_amt") %></td>
                <td><%= balanceJson.getJSONArray("output1").getJSONObject(0).getString("evlu_pfls_amt") %></td>
            </tr>
        </table>
    </div>

    <!-- 체결 내역 -->
    <div class="card">
        <h2>📈 체결 내역</h2>
        <table>
            <tr>
                <th>체결시간</th>
                <th>체결가</th>
                <th>전일대비</th>
                <th>체결량</th>
            </tr>
            <%
                JSONArray tradesArray = tradesJson.getJSONArray("output");
                for(int i = 0; i < Math.min(10, tradesArray.length()); i++) {
                    JSONObject trade = tradesArray.getJSONObject(i);
            %>
            <tr>
                <td><%= trade.getString("stck_cntg_hour") %></td>
                <td><%= trade.getString("stck_prpr") %></td>
                <td><%= trade.getString("prdy_vrss") %></td>
                <td><%= trade.getString("cntg_vol") %></td>
            </tr>
            <% } %>
        </table>
    </div>

    <!-- 주문 가능 정보 -->
    <div class="card">
        <h2>📝 주문 가능 정보</h2>
        <table>
            <tr>
                <th>주문가능현금</th>
                <th>최대매수금액</th>
                <th>최대매수수량</th>
            </tr>
            <tr>
                <td><%= orderJson.getJSONObject("output").getString("ord_psbl_cash") %></td>
                <td><%= orderJson.getJSONObject("output").getString("max_buy_amt") %></td>
                <td><%= orderJson.getJSONObject("output").getString("max_buy_qty") %></td>
            </tr>
        </table>
    </div>
</div>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>포트폴리오 정보</title>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Noto Sans KR', sans-serif;
            background-color: #f5f7fa;
            padding: 40px;
            color: #333;
        }

        h2 {
            color: #2c3e50;
            margin-bottom: 20px;
        }

        .section {
            background-color: #fff;
            padding: 25px 30px;
            margin-bottom: 40px;
            border-radius: 12px;
            box-shadow: 0 3px 10px rgba(0,0,0,0.06);
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
        }

        th, td {
            padding: 12px 18px;
            border: 1px solid #e1e4e8;
            text-align: center;
            font-size: 15px;
        }

        th {
            background-color: #f0f3f7;
            color: #2c3e50;
        }

        tr:nth-child(even) {
            background-color: #f9fbfd;
        }

        .flex-container {
            display: flex;
            gap: 20px;
            flex-wrap: wrap;
        }

        .half {
            flex: 1 1 45%;
        }

        .full {
            flex: 1 1 100%;
        }

        @media (max-width: 768px) {
            .half {
                flex: 1 1 100%;
            }
        }
    </style>
</head>
<body>

<div class="section">
    <h2>📈 주식 현재가 정보</h2>
    <table>
        <tr><th>현재가</th><td>${priceData.stck_prpr}</td></tr>
        <tr><th>전일대비</th><td>${priceData.prdy_vrss}</td></tr>
        <tr><th>전일대비율</th><td>${priceData.prdy_ctrt}</td></tr>
        <tr><th>시가</th><td>${priceData.stck_oprc}</td></tr>
        <tr><th>고가</th><td>${priceData.stck_hgpr}</td></tr>
        <tr><th>저가</th><td>${priceData.stck_lwpr}</td></tr>
    </table>
</div>

<div class="section">
    <h2>💰 보유 잔고 정보</h2>
    <c:forEach var="item" items="${balanceData}">
        <table>
            <tr><th>상품명</th><td>${item.prdt_name}</td></tr>
            <tr><th>보유수량</th><td>${item.hldg_qty}</td></tr>
            <tr><th>주문가능수량</th><td>${item.ord_psbl_qty}</td></tr>
            <tr><th>매입평균가</th><td>${item.pchs_avg_pric}</td></tr>
            <tr><th>현재가</th><td>${item.prpr}</td></tr>
            <tr><th>평가손익</th><td>${item.evlu_pfls_amt}</td></tr>
            <tr><th>수익률</th><td>${item.evlu_pfls_rt}%</td></tr>
        </table>
    </c:forEach>
</div>

<div class="section">
    <h2>🧾 체결 내역</h2>
    <table>
        <tr>
            <th>체결시간</th>
            <th>체결가</th>
            <th>체결량</th>
            <th>체결강도</th>
        </tr>
        <c:forEach var="trade" items="${tradesData}">
            <tr>
                <td>${trade.stck_cntg_hour}</td>
                <td>${trade.stck_prpr}</td>
                <td>${trade.cntg_vol}</td>
                <td>${trade.tday_rltv}</td>
            </tr>
        </c:forEach>
    </table>
</div>

<div class="section">
    <h2>📝 주문 가능 정보</h2>
    <table>
        <tr>
            <th>주문가능현금</th>
            <th>최대매수금액</th>
            <th>최대매수수량</th>
        </tr>
        <tr>
            <td>${orderData.ord_psbl_cash}</td>
            <td>${orderData.max_buy_amt}</td>
            <td>${orderData.max_buy_qty}</td>
        </tr>
    </table>
</div>

</body>
</html>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <!-- 스타일 부분은 동일하게 유지 -->
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
                <td>${priceData.output.stck_prpr}</td>
                <td>${priceData.output.prdy_vrss}</td>
                <td>${priceData.output.prdy_ctrt}%</td>
                <td>${priceData.output.acml_vol}</td>
            </tr>
        </table>
    </div>
    <!-- 잔고 정보 -->
    <div class="card">
        <h2>💰 잔고 정보</h2>
        <table>
            <tr>
                <th>종목이름</th>
                <th>보유수량</th>
                <th>매입단가</th>
                <th>평가금액</th>
                <th>평가손익</th>
            </tr>
            <tr>
                <td>${balanceData.output1[0].prdt_name}</td>
                <td>${balanceData.output1[0].hldg_qty}</td>
                <td>${balanceData.output1[0].pchs_avg_pric}</td>
                <td>${balanceData.output1[0].evlu_amt}</td>
                <td>${balanceData.output1[0].evlu_pfls_amt}</td>
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
            <c:forEach var="trade" items="${tradesData.output}" varStatus="status">
                <c:if test="${status.index < 10}">
                    <tr>
                        <td>${trade.stck_cntg_hour}</td>
                        <td>${trade.stck_prpr}</td>
                        <td>${trade.prdy_vrss}</td>
                        <td>${trade.cntg_vol}</td>
                    </tr>
                </c:if>
            </c:forEach>
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
                <td>${orderData.output.ord_psbl_cash}</td>
                <td>${orderData.output.max_buy_amt}</td>
                <td>${orderData.output.max_buy_qty}</td>
            </tr>
        </table>
    </div>
</div>
</body>
</html>
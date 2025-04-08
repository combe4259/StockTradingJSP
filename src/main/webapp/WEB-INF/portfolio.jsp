<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>포트폴리오 조회</title>
    <style>
        .card { margin: 20px; padding: 20px; border: 1px solid #ddd; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 8px; border: 1px solid #ddd; text-align: left; }
    </style>
</head>
<body>
<div class="container">
    <!-- 주가 정보 -->
    <div class="card">
        <h2>📈 주가 정보</h2>
        <table>
            <tr>
                <th>현재가</th>
                <th>전일대비</th>
                <th>등락률</th>
                <th>거래량</th>
            </tr>
            <tr>
                <td><fmt:formatNumber value="${priceData.stck_prpr}" pattern="#,###"/></td>
                <td>
                    <c:choose>
                        <c:when test="${priceData.prdy_vrss_sign == '2'}">▲</c:when>
                        <c:when test="${priceData.prdy_vrss_sign == '5'}">▼</c:when>
                        <c:otherwise>-</c:otherwise>
                    </c:choose>
                    <fmt:formatNumber value="${priceData.prdy_vrss}" pattern="#,###"/>
                </td>
                <td>${priceData.prdy_ctrt}%</td>
                <td><fmt:formatNumber value="${priceData.acml_vol}" pattern="#,###"/></td>
            </tr>
        </table>
    </div>

    <!-- 주식 보유 정보 -->
    <div class="card">
        <h2>📦 보유 종목</h2>
        <table>
            <tr>
                <th>종목명</th>
                <th>보유수량</th>
                <th>평균매입가</th>
                <th>평가금액</th>
                <th>평가손익</th>
            </tr>
            <c:forEach var="item" items="${output1}">
                <tr>
                    <td>${item.prdt_name}</td>
                    <td><fmt:formatNumber value="${item.hldg_qty}" pattern="#,###"/></td>
                    <td><fmt:formatNumber value="${item.pchs_avg_pric}" pattern="#,###.0000"/></td>
                    <td><fmt:formatNumber value="${item.evlu_amt}" pattern="#,###"/></td>
                    <td class="${item.evlu_pfls_amt >= 0 ? 'positive' : 'negative'}">
                        <fmt:formatNumber value="${item.evlu_pfls_amt}" pattern="#,###"/>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>

    <!-- 계좌 잔고 정보 -->
    <div class="card">
        <h2>💰 계좌 현황</h2>
        <table>
            <tr>
                <th>총 평가자산</th>
                <th>예수금</th>
                <th>총 자산</th>
            </tr>
            <c:forEach var="balance" items="${output2}">
                <tr>
                    <td><fmt:formatNumber value="${balance.tot_evlu_amt}" pattern="#,###"/></td>
                    <td><fmt:formatNumber value="${balance.dnca_tot_amt}" pattern="#,###"/></td>
                    <td><fmt:formatNumber value="${balance.nass_amt}" pattern="#,###"/></td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>
</body>
</html>

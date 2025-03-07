package org.zerock.stocktrading.portfolio;

import lombok.extern.log4j.Log4j2;
import org.zerock.stocktrading.ConfigManager;
import org.zerock.stocktrading.HttpClientUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
        import java.io.*;

//매수 가능 조회(예수금 등)
@WebServlet("/getOrder")
@Log4j2
public class psblOrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("PsblOrder 조회 요청...");

        // 2. 매수 가능 조회 API URL 구성 (모의 도메인 사용)
        String domain = ConfigManager.getProperty("domain.mock");
        String endpoint = domain + "/uapi/domestic-stock/v1/trading/inquire-psbl-order";
        String tr_id = "VTTC8908R";

        // 3. 요청 파라미터 (Query String 방식)
        String cano = "50124248"; // 계좌번호 (실제 값으로 변경)
        String acntPrdtCd = "01"; // 계좌상품코드
        String pdno = "005930"; // 삼성전자 종목 코드
        String ordUnpr = "0"; // 시장가 주문
        String ordDvsn = "01"; // 주문 구분 (시장가 주문)
        String cmaEvluAmtIcldYn = "N";
        String ovrsIcldYn = "N";

        // URL에 Query String 추가
        String queryString = String.format(
                "?CANO=%s&ACNT_PRDT_CD=%s&PDNO=%s&ORD_UNPR=%s&ORD_DVSN=%s&CMA_EVLU_AMT_ICLD_YN=%s&OVRS_ICLD_YN=%s",
                cano, acntPrdtCd, pdno, ordUnpr, ordDvsn, cmaEvluAmtIcldYn, ovrsIcldYn
        );

        try{
            String result = HttpClientUtil.sendGetRequest(endpoint, queryString, tr_id);
            req.setAttribute("psblOrder", result);
            req.getRequestDispatcher("WEB-INF/displayPsblOrder.jsp").forward(req,resp);
        }catch (Exception e){
            e.printStackTrace();

        }
    }
}


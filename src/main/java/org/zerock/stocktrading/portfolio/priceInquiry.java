package org.zerock.stocktrading.portfolio;

import lombok.extern.log4j.Log4j2;
import org.zerock.stocktrading.ConfigManager;
import org.zerock.stocktrading.HttpClientUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//주식현재가 시세
@WebServlet(urlPatterns = "/priceInquiry")
@Log4j2
public class priceInquiry extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        log.info("priceInquiry get...");

        String domain = ConfigManager.getProperty("domain.mock");
        String endpoint = domain + "/uapi/domestic-stock/v1/quotations/inquire-price";
        String tr_id ="FHKST01010100";
            //추후 동적 입력으로 변경
        //시장 분류 코드
        String fid_cond_mrkt_div_code ="J";
        //종목코드
        String fid_input_iscd = "005930";

        String queryString = "?fid_cond_mrkt_div_code="+fid_cond_mrkt_div_code+"&fid_input_iscd="+fid_input_iscd;

        try {
            String result = HttpClientUtil.sendGetRequest(endpoint, queryString, tr_id);
            req.setAttribute("priceData", result);
            req.getRequestDispatcher("WEB-INF/priceInquiry.jsp").forward(req,resp);
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}

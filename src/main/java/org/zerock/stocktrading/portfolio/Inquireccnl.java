package org.zerock.stocktrading.portfolio;

import org.zerock.stocktrading.ConfigManager;
import org.zerock.stocktrading.HttpClientUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//주식 체결 내역(그 날 거래정보 내가 산게 아님)
@WebServlet(urlPatterns = "/inquireccnl")
public class Inquireccnl extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{

        String domain = ConfigManager.getProperty("domain.mock");
        String endpoint = domain + "/uapi/domestic-stock/v1/quotations/inquire-ccnl";
        String tr_id ="FHKST01010300";

        String fid_cond_mrkt_div_code ="J";
        String fid_input_iscd="005930";

        String queryString = "?fid_cond_mrkt_div_code="+fid_cond_mrkt_div_code+"&fid_input_iscd="+fid_input_iscd;
        try{
            String result = HttpClientUtil.sendGetRequest(endpoint, queryString, tr_id);
            req.setAttribute("inquireccnl", result);
            req.getRequestDispatcher("WEB-INF/inquireccnl.jsp").forward(req,resp);
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}

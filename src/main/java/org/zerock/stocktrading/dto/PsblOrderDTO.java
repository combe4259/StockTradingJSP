package org.zerock.stocktrading.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PsblOrderDTO {
    private String rt_cd;//	성공 실패 여부
    private String msg_cd; //	응답코드
    private String msg1; //	응답메세지

    private Output output;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Output{
        private String ord_psbl_cash; //	주문가능현금
        private String ord_psbl_sbst; //	주문가능대용
        private String ruse_psbl_amt; //	재사용가능금액
        private String fund_rpch_chgs; //	펀드환매대금
        private String psbl_qty_calc_unpr; //	가능수량계산단가
        private String nrcvb_buy_amt; //	미수없는매수금액
        private String nrcvb_buy_qty; //	미수없는매수수량
        private String max_buy_amt; //	최대매수금액
        private String max_buy_qty; //	최대매수수량
        private String cma_evlu_amt; //	CMA평가금액
        private String ovrs_re_use_amt_wcrc; //	해외재사용금액원화
        private String ord_psbl_frcr_amt_wcrc; //	주문가능외화금액원화
    }

}

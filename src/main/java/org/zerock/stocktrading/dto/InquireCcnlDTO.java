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
public class InquireCcnlDTO {
    private String rt_cd;
    private String msg_cd;
    private String msg1;
    @JsonProperty("output")
    private List<Output> output; // 체결 데이터 배열

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Output {
        private String stck_cntg_hour;//	주식 체결 시간
        private String stck_prpr;//	주식 현재가
        private String prdy_vrss;//	전일 대비
        private String prdy_vrss_sign;//	전일 대비 부호
        private String cntg_vol; //	체결 거래량
        private String tday_rltv; //	당일 체결강도
        private String prdy_ctrt; //	전일 대비율

    }
}

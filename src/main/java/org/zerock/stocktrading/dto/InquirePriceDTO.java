package org.zerock.stocktrading.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InquirePriceDTO {
    private String rt_cd; // 성공 실패 여부
    private String msg_cd; // 응답코드
    private String msg1; // 응답메세지
    private Output output; // 응답상세

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Output{
        private String iscd_stat_cls_code; // 종목 상태 구분 코드
        private String marg_rate; // 증거금 비율
        private String rprs_mrkt_kor_name; // 대표 시장 한글 명
        private String new_hgpr_lwpr_cls_code; // 신 고가 저가 구분 코드
        private String bstp_kor_isnm; // 업종 한글 종목명
        private String temp_stop_yn; // 임시 정지 여부
        private String oprc_rang_cont_yn; // 시가 범위 연장 여부
        private String clpr_rang_cont_yn; // 종가 범위 연장 여부
        private String crdt_able_yn; // 신용 가능 여부
        private String grmn_rate_cls_code; // 보증금 비율 구분 코드
        private String elw_pblc_yn; // ELW 발행 여부
        private String stck_prpr; // 주식 현재가
        private String prdy_vrss; // 전일 대비
        private String prdy_vrss_sign; // 전일 대비 부호
        private String prdy_ctrt; // 전일 대비율
        private String acml_tr_pbmn; // 누적 거래 대금
        private String acml_vol; // 누적 거래량
        private String prdy_vrss_vol_rate; // 전일 대비 거래량 비율
        private String stck_oprc; // 주식 시가2
        private String stck_hgpr; // 주식 최고가
        private String stck_lwpr; // 주식 최저가
        private String stck_mxpr; // 주식 상한가
        private String stck_llam; // 주식 하한가
        private String stck_sdpr; // 주식 기준가
        private String wghn_avrg_stck_prc; // 가중 평균 주식 가격
        private String hts_frgn_ehrt; // HTS 외국인 소진율
        private String frgn_ntby_qty; // 외국인 순매수 수량
        private String pgtr_ntby_qty; // 프로그램매매 순매수 수량
        private String pvt_scnd_dmrs_prc; // 피벗 2차 디저항 가격
        private String pvt_frst_dmrs_prc; // 피벗 1차 디저항 가격
        private String pvt_pont_val; // 피벗 포인트 값
        private String pvt_frst_dmsp_prc; // 피벗 1차 디지지 가격
        private String pvt_scnd_dmsp_prc; // 피벗 2차 디지지 가격
        private String dmrs_val; // 디저항 값
        private String dmsp_val; // 디지지 값
        private String cpfn; // 자본금
        private String rstc_wdth_prc; // 제한 폭 가격
        private String stck_fcam; // 주식 액면가
        private String stck_sspr; // 주식 대용가
        private String aspr_unit; // 호가단위
        private String hts_deal_qty_unit_val; // HTS 매매 수량 단위 값
        private String lstn_stcn; // 상장 주수
        private String hts_avls; // HTS 시가총액
        private String per; // PER
        private String pbr; // PBR
        private String stac_month; // 결산 월
        private String vol_tnrt; // 거래량 회전율
        private String eps; // EPS
        private String bps; // BPS
        private String d250_hgpr; // 250일 최고가
        private String d250_hgpr_date; // 250일 최고가 일자
        private String d250_hgpr_vrss_prpr_rate; // 250일 최고가 대비 현재가 비율
        private String d250_lwpr; // 250일 최저가
        private String d250_lwpr_date; // 250일 최저가 일자
        private String d250_lwpr_vrss_prpr_rate; // 250일 최저가 대비 현재가 비율
        private String stck_dryy_hgpr; // 주식 연중 최고가
        private String dryy_hgpr_vrss_prpr_rate; // 연중 최고가 대비 현재가 비율
        private String dryy_hgpr_date; // 연중 최고가 일자
        private String stck_dryy_lwpr; // 주식 연중 최저가
        private String dryy_lwpr_vrss_prpr_rate; // 연중 최저가 대비 현재가 비율
        private String dryy_lwpr_date; // 연중 최저가 일자
        private String w52_hgpr; // 52주일 최고가
        private String w52_hgpr_vrss_prpr_ctrt; // 52주일 최고가 대비 현재가 대비
        private String w52_hgpr_date; // 52주일 최고가 일자
        private String w52_lwpr; // 52주일 최저가
        private String w52_lwpr_vrss_prpr_ctrt; // 52주일 최저가 대비 현재가 대비
        private String w52_lwpr_date; // 52주일 최저가 일자
        private String whol_loan_rmnd_rate; // 전체 융자 잔고 비율
        private String ssts_yn; // 공매도가능여부
        private String stck_shrn_iscd; // 주식 단축 종목코드
        private String fcam_cnnm; // 액면가 통화명
        private String cpfn_cnnm; // 자본금 통화명
        private String apprch_rate; // 접근도
        private String frgn_hldn_qty; // 외국인 보유 수량
        private String vi_cls_code; // VI적용구분코드
        private String ovtm_vi_cls_code; // 시간외단일가VI적용구분코드
        private String last_ssts_cntg_qty; // 최종 공매도 체결 수량
        private String invt_caful_yn; // 투자유의여부
        private String mrkt_warn_cls_code; // 시장경고코드
        private String short_over_yn; // 단기과열여부
        private String sltr_yn; // 정리매매여부
        private String mang_issu_cls_code; // 관리종목여부성공 실패 여부

    }
}

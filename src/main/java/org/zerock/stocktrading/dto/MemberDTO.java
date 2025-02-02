package org.zerock.stocktrading.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    private int member_id;
    private String user_id;
    private String password;
    private String username;
}


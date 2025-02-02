package org.zerock.stocktrading.domain;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberVO {
    private int member_id;
    private String user_id;
    private String password;
    private String username;
}

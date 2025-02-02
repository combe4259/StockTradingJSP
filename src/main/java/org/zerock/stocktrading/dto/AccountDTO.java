package org.zerock.stocktrading.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private int account_id;
    private int member_id;
    private String account_number;
    private BigDecimal balance;

}

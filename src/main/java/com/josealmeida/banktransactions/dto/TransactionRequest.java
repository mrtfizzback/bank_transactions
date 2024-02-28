package com.josealmeida.banktransactions.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest {
    private LocalDate schedulingDate;
    private BigDecimal amount;
}

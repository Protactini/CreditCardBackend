package com.alex.zhu.creditcardbackend.dto;


import lombok.Data;

import java.util.List;

//@Data
public record CardWithCashBackDTO(
        Long id,
        String name,
        String paymentMethod,
        List<CashBackDTO> cashBackList
) { }
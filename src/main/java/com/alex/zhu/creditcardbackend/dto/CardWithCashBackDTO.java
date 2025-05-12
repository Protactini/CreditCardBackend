package com.alex.zhu.creditcardbackend.dto;


import java.util.List;

public record CardWithCashBackDTO(
        Long id,
        String name,
        String paymentMethod,
        List<CashBackDTO> cashBackList
) { }
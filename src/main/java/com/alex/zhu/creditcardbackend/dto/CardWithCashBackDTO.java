package com.alex.zhu.creditcardbackend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CardWithCashBackDTO{
    Long id;
    String name;
    String paymentMethod;
    List<CashBackDTO> cashBackList;
}
package com.alex.zhu.creditcardbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardDTO {
    Long id;
    String name;
    String paymentMethod;
}

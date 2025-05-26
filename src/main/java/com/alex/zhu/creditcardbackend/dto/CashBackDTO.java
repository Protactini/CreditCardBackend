package com.alex.zhu.creditcardbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CashBackDTO {
    String area;
    double percentage;
}
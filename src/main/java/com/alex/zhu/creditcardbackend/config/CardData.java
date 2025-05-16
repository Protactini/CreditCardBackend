// src/main/java/com/alex/zhu/creditcardbackend/config/CardData.java
package com.alex.zhu.creditcardbackend.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class CardData {
    public String name;
    public String paymentMethod;

    @JsonProperty("cashBack")
    public List<CashBackData> cashBack;

    public static class CashBackData {
        public String area;
        public String productIcon;
        public double percentage;
    }
}

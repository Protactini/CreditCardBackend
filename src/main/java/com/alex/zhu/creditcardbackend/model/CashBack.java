// src/main/java/com/alex/zhu/creditcardbackend/model/CashBack.java
package com.alex.zhu.creditcardbackend.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
public class CashBack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String area;
    private String productIcon;
    private double percentage;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    public CashBack() {}

    // Convenience constructor
    public CashBack(String area, String productIcon, double percentage, Card card) {
        this.area = area;
        this.productIcon = productIcon;
        this.percentage = percentage;
        this.card = card;
    }

    // getters & setters...
    public Long getId() { return id; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public String getProductIcon() { return productIcon; }
    public void setProductIcon(String productIcon) { this.productIcon = productIcon; }
    public double getPercentage() { return percentage; }
    public void setPercentage(double percentage) { this.percentage = percentage; }
    public Card getCard() { return card; }
    public void setCard(Card card) { this.card = card; }
}

package com.alex.zhu.creditcardbackend.model;


import jakarta.persistence.*;

@Entity
public class CashBack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String area;
    private double percentage;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    // getters and setters
}
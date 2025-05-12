package com.alex.zhu.creditcardbackend.controller;

import com.alex.zhu.creditcardbackend.dto.*;
import com.alex.zhu.creditcardbackend.CardService;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api")
public class CardController {
    private final CardService service;

    public CardController(CardService service) {
        this.service = service;
    }

    @GetMapping("/cards")
    public List<CardDTO> getAllCards() {
        return service.getAllCards();
    }

    @GetMapping("/cards/{id}/cashback")
    public List<CashBackDTO> getCashBackForCard(@PathVariable Long id) {
        return service.getCashBackByCardId(id);
    }

    @GetMapping("/cards-with-cashback")
    public List<CardWithCashBackDTO> getAllCardsWithCashBack() {
        return service.getAllCardsWithCashBack();
    }
}
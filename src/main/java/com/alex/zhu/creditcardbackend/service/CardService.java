package com.alex.zhu.creditcardbackend.service;

import com.alex.zhu.creditcardbackend.dto.*;
import com.alex.zhu.creditcardbackend.model.Card;
import com.alex.zhu.creditcardbackend.repository.CardRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {
    private final CardRepository cardRepo;
    private final ModelMapper mapper = new ModelMapper();

    public CardService(CardRepository cardRepo) {
        this.cardRepo = cardRepo;
    }

    public List<CardDTO> getAllCards() {
        return cardRepo.findAll().stream()
                .map(c -> mapper.map(c, CardDTO.class))
                .collect(Collectors.toList());
    }

    public List<CashBackDTO> getCashBackByCardId(Long cardId) {
        return cardRepo.findById(cardId)
                .map(Card::getCashBack)
                .orElse(List.of())
                .stream()
                .map(cb -> mapper.map(cb, CashBackDTO.class))
                .collect(Collectors.toList());
    }

    public List<CardWithCashBackDTO> getAllCardsWithCashBack() {
        return cardRepo.findAll().stream()
                .map(c -> {
                    var cashList = c.getCashBack().stream()
                            .map(cb -> mapper.map(cb, CashBackDTO.class))
                            .collect(Collectors.toList());
                    return new CardWithCashBackDTO(
                            c.getId(),
                            c.getName(),
                            c.getPaymentMethod().name(),
                            cashList
                    );
                })
                .collect(Collectors.toList());
    }
}
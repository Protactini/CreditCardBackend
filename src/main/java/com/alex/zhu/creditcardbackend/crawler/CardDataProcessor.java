// src/main/java/com/alex/zhu/creditcardbackend/crawler/CardDataProcessor.java
package com.alex.zhu.creditcardbackend.crawler;

import com.alex.zhu.creditcardbackend.dto.CardWithCashBackDTO;
import com.alex.zhu.creditcardbackend.dto.CashBackDTO;
import com.alex.zhu.creditcardbackend.model.CashBack;
import com.alex.zhu.creditcardbackend.model.Card;
import com.alex.zhu.creditcardbackend.repository.CardRepository;
import com.alex.zhu.creditcardbackend.repository.CashBackRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Utility class for normalizing DTO area names and upserting
 * card & cashback data into the database.
 */
public class CardDataProcessor {

    /**
     * Normalize area names in the DTO to match database conventions.
     *
     * @param dto the raw CardWithCashBackDTO from the crawler
     * @return a new DTO with area names adjusted for the DB
     */
    public static CardWithCashBackDTO regulateAreas(CardWithCashBackDTO dto) {
        List<CashBackDTO> normalized = new ArrayList<>(dto.getCashBackList().size());

        for (CashBackDTO cb : dto.getCashBackList()) {
            String area = cb.getArea().trim();
            String dbName;
            switch (area) {
                case "Restaurants":
                    dbName = "Dining & Restaurants";
                    break;
                case "Select Streaming Services":
                    dbName = "Streaming Services";
                    break;
                default:
                    dbName = area;
            }
            normalized.add(new CashBackDTO(dbName, cb.getPercentage()));
        }

        return new CardWithCashBackDTO(
                dto.getId(),
                dto.getName(),
                dto.getPaymentMethod(),
                normalized
        );
    }

    /**
     * Upsert card entity and update only cashback percentages.
     *
     * @param dto      the normalized CardWithCashBackDTO
     * @param cardRepo the CardRepository
     * @param cashRepo the CashBackRepository
     */
    public static void upsertCardData(CardWithCashBackDTO dto,
                                      CardRepository cardRepo,
                                      CashBackRepository cashRepo) {
        // 1) Find existing card by name or create a new one
        Card card = cardRepo.findByName(dto.getName())
                .orElseGet(() -> {
                    Card c = new Card();
                    c.setName(dto.getName());
                    return c;
                });
        card = cardRepo.save(card);

        // 2) For each cashback entry: update if exists, otherwise insert new
        for (CashBackDTO cbDto : dto.getCashBackList()) {
            Optional<CashBack> existingOpt =
                    cashRepo.findByCardIdAndArea(card.getId(), cbDto.getArea());

            if (existingOpt.isPresent()) {
                // Update percentage on existing record
                CashBack existing = existingOpt.get();
                existing.setPercentage(cbDto.getPercentage());
                cashRepo.save(existing);
            } else {
                // Create new CashBack row
                CashBack newCb = new CashBack();
                newCb.setCard(card);
                newCb.setArea(cbDto.getArea());
                newCb.setPercentage(cbDto.getPercentage());
                cashRepo.save(newCb);
            }
        }
    }
}

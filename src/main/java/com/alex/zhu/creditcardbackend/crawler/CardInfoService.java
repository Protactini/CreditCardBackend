// src/main/java/com/alex/zhu/creditcardbackend/crawler/CardInfoService.java
package com.alex.zhu.creditcardbackend.crawler;

import com.alex.zhu.creditcardbackend.dto.*;
import com.alex.zhu.creditcardbackend.model.*;
import com.alex.zhu.creditcardbackend.repository.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CardInfoService {

    private static final Logger logger = LoggerFactory.getLogger(CardInfoService.class);

    private final List<String> companyUrls = List.of(
            "https://card.discover.com/cardissuer/public/rewards/offer/v1/offer-categories?_=1747952418206"  // your JSON endpoints
            // add more issuer URLs here
    );

    private final WebCrawlerRepo webCrawlerRepo;
    private final CardRepository     cardRepo;
    private final CashBackRepository cashRepo;

    public CardInfoService(WebCrawlerRepo webCrawlerRepo,
                           CardRepository cardRepo,
                           CashBackRepository cashRepo) {
        this.webCrawlerRepo = webCrawlerRepo;
        this.cardRepo       = cardRepo;
        this.cashRepo       = cashRepo;
    }

    @PostConstruct
    public void init() {
        refreshCache();
    }

    @Scheduled(cron = "0 0 2 * * MON")
    public void refreshWeekly() {
        refreshCache();
    }

    /**
     * 1) Crawl each company → upsert DB + collect DTO
     * 2) On any IOException → fall back to DB load
     * 3) Update in-memory cache
     */
    public void refreshCache() {
        List<CardWithCashBackDTO> dtos = new ArrayList<>();
        try {
            for (String url : companyUrls) {
                CardWithCashBackDTO dto = webCrawlerRepo.parseCardsFromForDiscover(url);
                upsertCardData(dto);
                dtos.add(dto);
            }
            logger.info("Cache refreshed via crawl: {} entries", dtos.size());
        } catch (IOException ex) {
            logger.error("Crawl failed—falling back to DB", ex);
//            List<CardWithCashBackDTO> fallback = loadFromDatabase();
//            logger.info("Cache populated from DB: {} entries", fallback.size());
        }
    }

    /**
     * Map DB rows to DTOs.
     */
    @Transactional(readOnly = true)
    protected List<CardWithCashBackDTO> loadFromDatabase() {
        return cardRepo.findAll().stream().map(card -> {
            List<CashBackDTO> list = card.getCashBack().stream()
                    .map(cb -> new CashBackDTO(cb.getArea(), cb.getPercentage()))
                    .collect(Collectors.toList());
            return new CardWithCashBackDTO(
                    card.getId(),
                    card.getName(),
                    card.getPaymentMethod().name(),
                    list
            );
        }).collect(Collectors.toList());
    }

    /**
     * Upsert a single card and its cashback rows:
     * - findByName or create new
     * - delete old CashBack by cardId
     * - save new CashBack entries
     */
    private void upsertCardData(CardWithCashBackDTO dto) {
        // 1) find or create
        Card card = cardRepo.findByName(dto.getName())
                .orElseGet(() -> {
                    Card c = new Card();
                    c.setName(dto.getName());
                    return c;
                });

        // 2) clear old cashback rows
        cashRepo.deleteByCardId(card.getId());

        // 3) insert fresh rows
        dto.getCashBackList().forEach(cbDto -> {
            var cb = new CashBack();
            cb.setArea(cbDto.getArea());
            cb.setPercentage(cbDto.getPercentage());
            cb.setCard(card);
            cashRepo.save(cb);
        });
    }
}

// src/main/java/com/alex/zhu/creditcardbackend/crawler/CardInfoService.java
package com.alex.zhu.creditcardbackend.crawler;

import com.alex.zhu.creditcardbackend.dto.*;
import com.alex.zhu.creditcardbackend.repository.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.util.*;


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
    /** Crawl, normalize, upsert, and log results */
    public void refreshCache() {
        List<CardWithCashBackDTO> dtos = new ArrayList<>();
        for (String url : companyUrls) {
            try {
                // 1) Fetch raw DTO
                CardWithCashBackDTO rawDto = webCrawlerRepo.parseCardsFromForDiscover(url);
                // 2) Normalize area names
                CardWithCashBackDTO normalized = CardDataProcessor.regulateAreas(rawDto);
                // 3) Upsert into DB
                CardDataProcessor.upsertCardData(normalized, cardRepo, cashRepo);
                dtos.add(normalized);
            } catch (IOException e) {
                logger.error("Failed to crawl {}: {}", url, e.getMessage());
            }
        }
        logger.info("Cache refreshed via crawl: {} entries", dtos.size());
    }
}

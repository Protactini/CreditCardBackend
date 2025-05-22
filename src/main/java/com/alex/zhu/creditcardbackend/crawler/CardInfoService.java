// src/main/java/com/alex/zhu/creditcardbackend/crawler/WebCrawlerService.java
package com.alex.zhu.creditcardbackend.crawler;

import com.alex.zhu.creditcardbackend.dto.CardWithCashBackDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Service that orchestrates crawling of external pages for card data,
 * caches the result in memory, and refreshes on startup + every Monday at 2 AM.
 */
@Service
public class CardInfoService {

    private static final Logger logger = LoggerFactory.getLogger(CardInfoService.class);

    /** URLs to crawl. One per issuer/page that lists multiple cards. */
    private final List<String> cardUrls = List.of(
            "https://card.discover.com/cardissuer/public/rewards/offer/v1/offer-categories?_=1747952418206" // Discover Card quarter cashback
            // add other issuer pages here
    );

    /** In-memory cache holding the last successful crawl result. */
    private final AtomicReference<List<CardWithCashBackDTO>> cache = new AtomicReference<>(List.of());

    private final WebCrawlerRepo webCrawlerRepo;

    /**
     * Injects the repo that knows how to parse each site’s HTML.
     */
    public CardInfoService(WebCrawlerRepo webCrawlerRepo) {
        this.webCrawlerRepo = webCrawlerRepo;
    }

    /**
     * Prime the cache once on startup.
     */
    @PostConstruct
    public void refreshOnStartup() {
        refreshCache();
    }

    /**
     * Every Monday at 02:00 AM, re-crawl and refresh the cache.
     *
     * Cron format: second minute hour day-of-month month day-of-week
     */
    @Scheduled(cron = "0 0 2 * * MON")
    public void refreshEveryMonday() {
        refreshCache();
    }

    /**
     * Returns the most recently cached cards for controllers to serve.
     */
    public List<CardWithCashBackDTO> getAllCardsFromCache() {
        return cache.get();
    }

    /**
     * Internal: crawl all URLs and update the in-memory cache.
     * On failure, logs the error but retains the last known good cache.
     */
    private void refreshCache() {
        try {
            List<CardWithCashBackDTO> result = new ArrayList<>();
            for (String url : cardUrls) {
                result.add(webCrawlerRepo.parseCardsFromForDiscover(url));
            }
            cache.set(result);
            logger.info("Cache refreshed: {} cards loaded", result.size());
        } catch (IOException e) {
            logger.error("Failed to refresh cache, keeping previous data", e);
        }
    }
}

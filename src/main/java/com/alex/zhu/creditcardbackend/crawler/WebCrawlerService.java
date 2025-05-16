// src/main/java/com/alex/zhu/creditcardbackend/crawler/WebCrawlerService.java
package com.alex.zhu.creditcardbackend.crawler;

import com.alex.zhu.creditcardbackend.dto.CardWithCashBackDTO;
import com.alex.zhu.creditcardbackend.dto.CashBackDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Crawls external pages for card data, caches the result in memory,
 * and refreshes on startup + every Monday at 2:00 AM.
 */
@Service
public class WebCrawlerService {

    /**
     * URLs to crawl. One per issuer or per page that lists multiple cards.
     */
    private final List<String> cardUrls = List.of(
            "https://www.discover.com/credit-cards/cash-back/cashback-calendar.html"
            // add other issuer pages here
    );

    /**
     * In‑memory cache of the last crawl.
     */
    private final AtomicReference<List<CardWithCashBackDTO>> cache =
            new AtomicReference<>(List.of());

    /**
     * Called once after the bean is constructed (i.e. on startup).
     */
    @PostConstruct
    public void refreshOnStartup() {
        refreshCache();
    }

    /**
     * Cron expression: every Monday at 02:00.
     * ──────────────────────────────────────────
     *   second minute hour day-of-month month day-of-week
     */
    @Scheduled(cron = "0 0 2 * * MON")
    public void refreshEveryMonday() {
        refreshCache();
    }

    /**
     * Public method for controllers to get the latest cached cards.
     */
    public List<CardWithCashBackDTO> getAllCardsFromCache() {
        return cache.get();
    }

    // ──────────────── internal ─────────────────

    /**
     * Crawl all URLs, parse, and update the in‑memory cache.
     */
    private void refreshCache() {
        try {
            List<CardWithCashBackDTO> result = new ArrayList<>();
            for (String url : cardUrls) {
                result.addAll(parseCardsFrom(url));
            }
            cache.set(result);
            System.out.println("WebCrawlerService: cache refreshed with " + result.size() + " cards");
        } catch (IOException e) {
            // handle or log the error (don’t overwrite cache on failure)
            System.err.println("WebCrawlerService: failed to refresh cache: " + e.getMessage());
        }
    }

    /**
     * Crawl a single page, parse its HTML, and return DTOs.
     */
    private List<CardWithCashBackDTO> parseCardsFrom(String url) throws IOException {
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .timeout(10_000)
                .get();

        List<CardWithCashBackDTO> cards = new ArrayList<>();
        Elements items = doc.select("div.card-item");
        for (Element item : items) {
            String name = item.selectFirst("h3.card-name").text();
            String paymentMethod = item.selectFirst("span.payment-method").text();
            List<CashBackDTO> cashList = new ArrayList<>();
            Elements rows = item.select("ul.cashback-list li");
            for (Element row : rows) {
                String area = row.selectFirst("span.area").text();
                double pct = Double.parseDouble(
                        row.selectFirst("span.percent").text().replace("%", "")
                );
                cashList.add(new CashBackDTO(area, pct));
            }
            cards.add(new CardWithCashBackDTO(null, name, paymentMethod, cashList));
        }
        return cards;
    }
}

// src/main/java/com/alex/zhu/creditcardbackend/crawler/WebCrawlerService.java
package com.alex.zhu.creditcardbackend.crawler;

import com.alex.zhu.creditcardbackend.dto.CardWithCashBackDTO;
import com.alex.zhu.creditcardbackend.dto.CashBackDTO;
import org.htmlunit.BrowserVersion;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
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
    private List<CardWithCashBackDTO> parseCardsFrom(String url) throws IOException{
        List<CardWithCashBackDTO> cards = new ArrayList<>();

        // 1) Spin up a headless, JS‑enabled client
        try (WebClient client = new WebClient(BrowserVersion.CHROME)) {
            client.getOptions().setCssEnabled(false);
            client.getOptions().setJavaScriptEnabled(true);
            client.getOptions().setThrowExceptionOnScriptError(false);
            client.getOptions().setTimeout(15_000);

            // 2) Fetch & render the page
            HtmlPage page = client.getPage(url);
            // wait for the calendar JS to finish loading (adjust as needed)
            client.waitForBackgroundJavaScript(5_000);

            // 3) Grab the fully rendered HTML
            String renderedHtml = page.asXml();
            Document doc = Jsoup.parse(renderedHtml);

            // 4) Now select the calendar quarters and their categories.
            //    You will need to inspect the rendered DOM to pick the right selectors.
            //    Here’s a hypothetical example:
            Elements quarters = doc.select("div.CalendarQuarter"); // e.g. one per Q1/Q2/Q3/Q4
            for (Element quarter : quarters) {
                // e.g. <h3 class="quarter-title">Q2 2025 – Grocery Stores & Wholesale Clubs</h3>
                String quarterTitle = quarter.selectFirst("h3.quarter-title").text();
                // Assume the category text comes immediately after “– ”
                String categoryNames = quarterTitle.substring(quarterTitle.indexOf("–") + 1).trim();

                // Split multiple categories by “ & ” or comma
                String[] areas = categoryNames.split("\\s*(&|,)\\s*");
                for (String area : areas) {
                    double pct = 5.0;  // always 5% on those categories
                    cards.add(new CardWithCashBackDTO(
                            null,
                            "Discover it Cash Back",          // hard‑coded card name
                            "DISCOVER",
                            List.of(new CashBackDTO(area, pct))
                    ));
                }
            }
        } catch (Exception e) {
            // Log & recover
            System.err.println("Failed to parse Discover calendar: " + e.getMessage());
        }

        return cards;
    }
}

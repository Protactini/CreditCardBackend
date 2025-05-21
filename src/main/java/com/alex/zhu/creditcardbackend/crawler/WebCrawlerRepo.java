// src/main/java/com/alex/zhu/creditcardbackend/crawler/WebCrawlerRepo.java
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository-style component that knows how to fetch and parse
 * pages for each issuer’s HTML format.
 */
@Repository
public class WebCrawlerRepo {

    private static final Logger logger = LoggerFactory.getLogger(WebCrawlerRepo.class);

    /**
     * Crawl the Discover cashback-calendar page, render its JS,
     * extract each quarter’s categories, and return DTOs.
     *
     * @param url the Discover calendar URL
     * @return list of CardWithCashBackDTO entries
     * @throws IOException on network or parsing errors
     */
    public List<CardWithCashBackDTO> parseCardsFromForDiscover(String url) throws IOException {
        List<CardWithCashBackDTO> cards = new ArrayList<>();

        // 1) Headless, JS-enabled HtmlUnit client
        try (WebClient client = new WebClient(BrowserVersion.CHROME)) {
            client.getOptions().setCssEnabled(false);
            client.getOptions().setJavaScriptEnabled(true);
            client.getOptions().setThrowExceptionOnScriptError(false);
            client.getOptions().setTimeout(15_000);

            // 2) Fetch & render the page
            HtmlPage page = client.getPage(url);
            client.waitForBackgroundJavaScript(5_000);

            // 3) Parse the fully rendered HTML with Jsoup
            String renderedHtml = page.asXml();
            Document doc = Jsoup.parse(renderedHtml);

            // 4) Select each quarter container (adjust selector to match real DOM)
            Elements quarters = doc.select("div.CalendarQuarter");
            for (Element quarter : quarters) {
                // e.g. <h3 class="quarter-title">Q2 2025 – Grocery Stores & Wholesale Clubs</h3>
                Element titleEl = quarter.selectFirst("h3.quarter-title");
                if (titleEl == null) continue;

                String title = titleEl.text();
                int dash = title.indexOf("–");
                if (dash < 0) continue;

                // Extract categories after the “–”
                String categoryNames = title.substring(dash + 1).trim();
                String[] areas = categoryNames.split("\\s*(&|,)\\s*");

                // Each area always gets 5% on Discover
                for (String area : areas) {
                    cards.add(new CardWithCashBackDTO(
                            null,
                            "Discover it Cash Back",     // card name
                            "DISCOVER",                   // issuer
                            List.of(new CashBackDTO(area, 5.0))
                    ));
                }
            }
        } catch (Exception e) {
            // Log but don’t rethrow to allow other URLs to be processed
            logger.error("Error parsing Discover page at {}: {}", url, e.getMessage());
        }

        return cards;
    }
}

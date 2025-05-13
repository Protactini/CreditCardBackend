package com.alex.zhu.creditcardbackend.crawler;

import com.alex.zhu.creditcardbackend.dto.CardWithCashBackDTO;
import com.alex.zhu.creditcardbackend.dto.CashBackDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service that crawls external websites to extract credit card
 * and cashback information, converting them into DTOs.
 */
@Service
public class WebCrawlerService {

    /**
     * List of external pages to crawl. Each URL should
     * list credit cards with a consistent HTML structure.
     */
    private final List<String> cardUrls = List.of(
            "https://site1.example.com/cards",
            "https://site2.example.com/offers"
            // Add additional sources here
    );

    /**
     * Public method called by controllers. It aggregates
     * results from all configured URLs.
     *
     * @return List of CardWithCashBackDTO for every card found
     * @throws IOException if network or parsing fails
     */
    public List<CardWithCashBackDTO> crawlAllCards() throws IOException {
        List<CardWithCashBackDTO> result = new ArrayList<>();

        // Iterate each source URL and parse its cards
        for (String url : cardUrls) {
            result.addAll(parseCardsFrom(url));
        }
        return result;
    }

    /**
     * Connects to a single URL, parses the HTML, and extracts
     * card data into DTOs.
     *
     * @param url the webpage to fetch
     * @return List of CardWithCashBackDTO parsed from this page
     * @throws IOException if request or parsing fails
     */
    private List<CardWithCashBackDTO> parseCardsFrom(String url) throws IOException {
        // Fetch and parse the HTML document
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")   // Mimic a real browser
                .timeout(10_000)            // 10 second timeout
                .get();

        List<CardWithCashBackDTO> cards = new ArrayList<>();

        // Select each card container element by its CSS class
        Elements items = doc.select("div.card-item");
        for (Element item : items) {
            // Extract the card name from a <h3 class="card-name"> element
            String name = item.selectFirst("h3.card-name").text();

            // Extract payment method from a <span class="payment-method">
            String paymentMethod = item.selectFirst("span.payment-method").text();

            // Prepare a list to hold cashback entries for this card
            List<CashBackDTO> cashList = new ArrayList<>();

            // Cashback info is assumed in a <ul class="cashback-list"> list
            Elements rows = item.select("ul.cashback-list li");
            for (Element row : rows) {
                // Each row has an <span class="area"> and <span class="percent">
                String area = row.selectFirst("span.area").text();
                String pctText = row.selectFirst("span.percent")
                        .text()
                        .replace("%", "");        // remove percent sign
                double pct = Double.parseDouble(pctText);

                // Add a new DTO for this cashback area
                cashList.add(new CashBackDTO(area, pct));
            }

            // Assemble the full card DTO (id is null since this is a crawl)
            cards.add(new CardWithCashBackDTO(null, name, paymentMethod, cashList));
        }

        return cards;
    }
}

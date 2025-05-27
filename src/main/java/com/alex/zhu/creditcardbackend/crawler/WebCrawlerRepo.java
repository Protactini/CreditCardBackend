// src/main/java/com/alex/zhu/creditcardbackend/crawler/WebCrawlerRepo.java
package com.alex.zhu.creditcardbackend.crawler;

import com.alex.zhu.creditcardbackend.dto.CardWithCashBackDTO;
import com.alex.zhu.creditcardbackend.dto.CashBackDTO;
import com.alex.zhu.creditcardbackend.crawler.response.*;

/**
 * Web page scraper import classes
 */
import org.htmlunit.BrowserVersion;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Jason response import classes
 */
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.databind.ObjectMapper;

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
     * Sample reply:
     * {
     *     "quarters": [
     *         {
     *             "offerId": 33931,
     *             "offerStatus": "expired",
     *             "title": "Restaurants, Home Improvement Stores, and Select Streaming Services",
     *             "offerDescUnenrolled": "Earn <b>5% <i>Cashback Bonus</i></b> at <b>Restaurants, Home Improvement Stores, and Select Streaming Services,</b> January 1 to March 31, 2025, on up to $1,500 in purchases when you activate.",
     *             "offerDescEnrolled": "You activated to earn <b>5% <i>Cashback Bonus</i></b> at <b>Restaurants, Home Improvement Stores, and Select Streaming Services,</b> PARENT_ENROLLMENT_DATE to March 31, 2025, on up to $1,500 in purchases.",
     *             "imageBase": "33931",
     *             "disclosureInd": "33931",
     *             "quarterLabelStartDate": "January 01, 2025",
     *             "quarterLabelEndDate": "March 31, 2025",
     *             "preEnrollmentStartDate": "December 01, 2024",
     *             "qualificationStartDate": "January 01, 2025",
     *             "exclusionCriteria": "",
     *             "inclusionCriteria": "",
     *             "hasAdditionalCategories": false,
     *             "additionalCategories": [],
     *             "disclosureUnenrolled": "Activate to earn 5% <em>Cashback Bonus</em> at <strong>Restaurants, Home Improvement Stores, and Select Streaming Services</strong> from 1/1/25 (or the date on which you activate 5%, whichever is later) through 3/31/25, on up to $1,500 in purchases.<ul><li><strong>Restaurant</strong> purchases include those made at merchants classified as full-service restaurants, cafes, cafeterias, fast-food locations, caterers, and restaurant delivery services. Restaurants located inside of or affiliated with another business, such as hotels or retail stores and establishments classified as a bakery, may not qualify.</li><li><strong>Home Improvement Store</strong> purchases include online and in store purchases at home improvement retail stores, building supply stores (e.g. lumber, paint, and hardware stores), as well as lawn, nursery, and garden supply stores. Purchases made through third parties, such as contractors, may not qualify. Purchases from home furnishings, home appliance, and floor covering stores are not included.</li><li>Examples of <strong>Select Streaming Service</strong> purchases include Amazon Music, Amazon Prime Video, AMC+, Apple Music, Apple TV, Audible, DirecTV Stream, FUBO, Google Play Music and Video, Max, iHeartRadio, MLB.TV, Netflix, Pandora, Paramount Plus, Peacock, Showtime, SiriusXM, Sling TV, Spotify, Starz, Vudu, YouTube Music, YouTube Premium, and YouTube TV. Other popular streaming services may be included. If your subscription is bundled with another product or service or billed by a third party (such as a digital platform, a cable or satellite provider, telecommunications, internet provider, or a car manufacturer), the purchase may not be eligible in this category. Add-ons associated with Select Streaming Services may not qualify for this promotion if they are not listed, billed in a bundle, billed separately, or billed through a third party. Listed merchants are in no way sponsoring or affiliated with this program.</li></ul>",
     *             "subTitle": "",
     *             "defaultOffer": false,
     *             "imageExt": "gif",
     *             "imagePath": "/content/dam/discover/en_us/omnichannel/rewards/5percent/images/",
     *             "earnDescriptionDuringQualification": "You&#8217;re earning 5% cash back",
     *             "earnDescriptionPreEnrollment": "You activated to earn 5% cash back",
     *             "middleSectionText": null,
     *             "middleSectionButtonText": "",
     *             "middleSectionButtonUrl": ""
     *         },
     *         ...
     *     ],
     *     "disclosureApply": "Applies to all categories: ",
     *     "disclosureCommon": "<span>Purchases must be made with merchants in the U.S. To qualify for 5%, the purchase transaction date must be before or on the last day of the offer or promotion. For online purchases, the transaction date from the merchant may be the date when the item ships. Rewards are added to your account within two billing periods. Even if a purchase appears to fit in a 5% category, the merchant may not have a merchant category code (MCC) in that category. Merchants and payment processors are assigned an MCC based on their typical products and services. Discover Card does not assign MCCs to merchants. Certain third-party payment accounts and digital wallet transactions may not earn 5% if the technology does not provide sufficient transaction details or a qualifying MCC. Learn more at <a href='https://www.discover.com/credit-cards/digital-wallets/?vcmpgn=discover_digitalwallets'>Discover.com/digitalwallets</a>. See <i>Cashback Bonus</i> Program Terms and Conditions for more information.</span>"
     * }
     */
    public CardWithCashBackDTO parseCardsFromForDiscover(String url) throws IOException {
        // 1) GET JSON
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET().build();

        String body;
        try {
            body = client.send(req, HttpResponse.BodyHandlers.ofString()).body();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Interrupted fetching Discover JSON", e);
        }

        // 2) Deserialize
        ObjectMapper mapper = new ObjectMapper();
        DiscoverResponse resp = mapper.readValue(body, DiscoverResponse.class);

        // 3) Find current quarter
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.ENGLISH);
        LocalDate today = LocalDate.now();
        for (DiscoverResponse.Quarter q : resp.getQuarters()) {
            if (!q.getOfferStatus().equals("expired")) {
                LocalDate start = LocalDate.parse(q.getQuarterLabelStartDate(), fmt);
                LocalDate end   = LocalDate.parse(q.getQuarterLabelEndDate(),   fmt);
                if (!today.isBefore(start) && !today.isAfter(end)) {
                    // 4) Split title into areas
                    String[] areas = q.getTitle().split("\\s*,\\s*|\\s+and\\s+");
                    List<CashBackDTO> cashList = new ArrayList<>();
                    for (String area : areas) {
                        cashList.add(new CashBackDTO(area.trim(), 5.0));
                    }
                    return new CardWithCashBackDTO(
                            null,
                            "Discover it Cash Back",
                            "DISCOVER",
                            cashList
                    );
                }
            }
        }

        // none matched → return empty DTO
        logger.warn("No active Discover quarter found for date {}", today);
        return new CardWithCashBackDTO(null, "Discover it Cash Back", "DISCOVER", List.of());
    }
}

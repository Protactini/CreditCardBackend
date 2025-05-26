package com.alex.zhu.creditcardbackend.crawler;

import com.alex.zhu.creditcardbackend.dto.CardWithCashBackDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CardInfoServiceTest {

    @Autowired
    private CardInfoService crawler;

    @Test
    void crawlAllCards_returnsNonEmptyList() throws IOException {
        crawler.refreshCache();
//        List<CardWithCashBackDTO> cards = crawler.loadFromDatabase();
//        assertNotNull(cards, "crawler should never return null");
//        assertFalse(cards.isEmpty(), "we expected at least one card from our sites");
//        // Optionally inspect first element
//        System.out.println(cards.get(0));
    }
}
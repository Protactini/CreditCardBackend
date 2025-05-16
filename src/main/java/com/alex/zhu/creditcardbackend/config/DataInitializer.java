// src/main/java/com/alex/zhu/creditcardbackend/config/DataInitializer.java
package com.alex.zhu.creditcardbackend.config;

import com.alex.zhu.creditcardbackend.model.Card;
import com.alex.zhu.creditcardbackend.model.CashBack;
import com.alex.zhu.creditcardbackend.model.PaymentMethod;
import com.alex.zhu.creditcardbackend.repository.CardRepository;
import com.alex.zhu.creditcardbackend.repository.CashBackRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner loadDefaultData(
            CardRepository cardRepo,
            CashBackRepository cashRepo
    ) {
        return args -> {
            // 1) Load JSON file from classpath
            var resource = new ClassPathResource("cards-data.json");
            try (InputStream in = resource.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                List<CardData> cards = mapper.readValue(
                        in,
                        new TypeReference<List<CardData>>() {}
                );

                // 2) For each CardData, create Card + its CashBack entries
                for (CardData cd : cards) {
                    Card card = new Card();
                    card.setName(cd.name);
                    card.setPaymentMethod(PaymentMethod.valueOf(cd.paymentMethod));
                    cardRepo.save(card);

                    // map and save cashback rows
                    cd.cashBack.forEach(cbData -> {
                        CashBack cb = new CashBack(
                                cbData.area,
                                cbData.productIcon,
                                cbData.percentage,
                                card
                        );
                        cashRepo.save(cb);
                    });
                }
            }
        };
    }
}

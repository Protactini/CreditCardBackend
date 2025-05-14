// src/main/java/com/alex/zhu/creditcardbackend/config/DataInitializer.java
package com.alex.zhu.creditcardbackend.config;

import com.alex.zhu.creditcardbackend.model.*;
import com.alex.zhu.creditcardbackend.repository.CardRepository;
import com.alex.zhu.creditcardbackend.repository.CashBackRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner loadDefaultData(
            CardRepository cardRepo,
            CashBackRepository cashRepo
    ) {
        return args -> {
            // ─── Chase Freedom Unlimited ───────────────────────────────────────
            Card chase = new Card();
            chase.setName("Chase Freedom Unlimited");
            chase.setPaymentMethod(PaymentMethod.VISA);
            cardRepo.save(chase);

            List<CashBack> chaseRates = List.of(
                    new CashBack("Grocery Stores",     "icon1", 1.5, chase),
                    new CashBack("Gas Stations",       "icon2", 1.5, chase),
                    new CashBack("EV Charging",        "icon3", 1.5, chase),
                    new CashBack("Dining & Restaurants","icon4", 3.0, chase),
                    new CashBack("Online Shopping",    "icon5", 1.5, chase),
                    new CashBack("Travel",             "icon6", 5.0, chase),
                    new CashBack("Streaming Services", "icon7", 1.5, chase),
                    new CashBack("Drugstores",         "icon8", 3.0, chase),
                    new CashBack("Home Improvement Stores","icon9",1.5,chase),
                    new CashBack("Transit & Commuting","icon10",1.5, chase),
                    new CashBack("Wholesale Clubs",    "icon11",1.5, chase)
            );
            cashRepo.saveAll(chaseRates);

            // ─── Citi Double Cash ─────────────────────────────────────────────
            Card citi = new Card();
            citi.setName("Citi Double Cash");
            citi.setPaymentMethod(PaymentMethod.MASTERCARD);
            cardRepo.save(citi);

            List<CashBack> citiRates = List.of(
                    new CashBack("Grocery Stores",     "icon1", 2.0, citi),
                    new CashBack("Gas Stations",       "icon2", 2.0, citi),
                    new CashBack("EV Charging",        "icon3", 2.0, citi),
                    new CashBack("Dining & Restaurants","icon4",2.0, citi),
                    new CashBack("Online Shopping",    "icon5", 2.0, citi),
                    new CashBack("Travel",             "icon6", 2.0, citi),
                    new CashBack("Streaming Services", "icon7", 2.0, citi),
                    new CashBack("Drugstores",         "icon8", 2.0, citi),
                    new CashBack("Home Improvement Stores","icon9",2.0,citi),
                    new CashBack("Transit & Commuting","icon10",2.0, citi),
                    new CashBack("Wholesale Clubs",    "icon11",2.0, citi)
            );
            cashRepo.saveAll(citiRates);

            // ─── BofA Customized Cash Rewards ─────────────────────────────────
            Card boa = new Card();
            boa.setName("BofA Customized Cash Rewards");
            boa.setPaymentMethod(PaymentMethod.VISA);
            cardRepo.save(boa);

            List<CashBack> boaRates = List.of(
                    new CashBack("Grocery Stores",     "icon1", 2.0, boa),
                    new CashBack("Gas Stations",       "icon2", 1.0, boa),
                    new CashBack("EV Charging",        "icon3", 3.0, boa),
                    new CashBack("Dining & Restaurants","icon4",3.0, boa),
                    new CashBack("Online Shopping",    "icon5", 1.0, boa),
                    new CashBack("Travel",             "icon6", 1.0, boa),
                    new CashBack("Streaming Services", "icon7", 3.0, boa),
                    new CashBack("Drugstores",         "icon8", 1.0, boa),
                    new CashBack("Home Improvement Stores","icon9",2.0,boa),
                    new CashBack("Transit & Commuting","icon10",1.0, boa),
                    new CashBack("Wholesale Clubs",    "icon11",2.0, boa)
            );
            cashRepo.saveAll(boaRates);

            // ─── Capital One Venture Rewards ──────────────────────────────────
            Card capone = new Card();
            capone.setName("Capital One Venture Rewards");
            capone.setPaymentMethod(PaymentMethod.VISA);
            cardRepo.save(capone);

            List<CashBack> caponeRates = List.of(
                    new CashBack("Grocery Stores",     "icon1", 2.0, capone),
                    new CashBack("Gas Stations",       "icon2", 2.0, capone),
                    new CashBack("EV Charging",        "icon3", 2.0, capone),
                    new CashBack("Dining & Restaurants","icon4",2.0, capone),
                    new CashBack("Online Shopping",    "icon5", 2.0, capone),
                    new CashBack("Travel",             "icon6", 5.0, capone),
                    new CashBack("Streaming Services", "icon7", 2.0, capone),
                    new CashBack("Drugstores",         "icon8", 2.0, capone),
                    new CashBack("Home Improvement Stores","icon9",2.0,capone),
                    new CashBack("Transit & Commuting","icon10",5.0, capone),
                    new CashBack("Wholesale Clubs",    "icon11",2.0, capone)
            );
            cashRepo.saveAll(caponeRates);

            // ─── Apple Card ──────────────────────────────────────────────────
            Card apple = new Card();
            apple.setName("Apple Card");
            apple.setPaymentMethod(PaymentMethod.MASTERCARD);
            cardRepo.save(apple);

            List<CashBack> appleRates = List.of(
                    new CashBack("Grocery Stores",     "icon1", 2.0, apple),
                    new CashBack("Gas Stations",       "icon2", 2.0, apple),
                    new CashBack("EV Charging",        "icon3", 2.0, apple),
                    new CashBack("Dining & Restaurants","icon4",2.0, apple),
                    new CashBack("Online Shopping",    "icon5", 2.0, apple),
                    new CashBack("Travel",             "icon6", 2.0, apple),
                    new CashBack("Streaming Services", "icon7", 2.0, apple),
                    new CashBack("Drugstores",         "icon8", 2.0, apple),
                    new CashBack("Home Improvement Stores","icon9",2.0,apple),
                    new CashBack("Transit & Commuting","icon10",2.0, apple),
                    new CashBack("Wholesale Clubs",    "icon11",2.0, apple)
            );
            cashRepo.saveAll(appleRates);

            // ─── Discover it Cash Back ───────────────────────────────────────
            Card discover = new Card();
            discover.setName("Discover it Cash Back");
            discover.setPaymentMethod(PaymentMethod.DISCOVER);
            cardRepo.save(discover);

            List<CashBack> discoverRates = List.of(
                    new CashBack("Grocery Stores",     "icon1", 5.0, discover),
                    new CashBack("Gas Stations",       "icon2", 5.0, discover),
                    new CashBack("EV Charging",        "icon3", 1.0, discover),
                    new CashBack("Dining & Restaurants","icon4",1.0, discover),
                    new CashBack("Online Shopping",    "icon5", 1.0, discover),
                    new CashBack("Travel",             "icon6", 1.0, discover),
                    new CashBack("Streaming Services", "icon7", 1.0, discover),
                    new CashBack("Drugstores",         "icon8", 1.0, discover),
                    new CashBack("Home Improvement Stores","icon9",1.0,discover),
                    new CashBack("Transit & Commuting","icon10",1.0, discover),
                    new CashBack("Wholesale Clubs",    "icon11",1.0, discover)
            );
            cashRepo.saveAll(discoverRates);

            // ─── Amex Blue Cash Preferred ────────────────────────────────────
            Card amex = new Card();
            amex.setName("Amex Blue Cash Preferred");
            amex.setPaymentMethod(PaymentMethod.AMEX);
            cardRepo.save(amex);

            List<CashBack> amexRates = List.of(
                    new CashBack("Grocery Stores",     "icon1", 6.0, amex),
                    new CashBack("Gas Stations",       "icon2", 3.0, amex),
                    new CashBack("EV Charging",        "icon3", 1.0, amex),
                    new CashBack("Dining & Restaurants","icon4",1.0, amex),
                    new CashBack("Online Shopping",    "icon5", 1.0, amex),
                    new CashBack("Travel",             "icon6", 1.0, amex),
                    new CashBack("Streaming Services", "icon7", 1.0, amex),
                    new CashBack("Drugstores",         "icon8", 1.0, amex),
                    new CashBack("Home Improvement Stores","icon9",1.0,amex),
                    new CashBack("Transit & Commuting","icon10",3.0, amex),
                    new CashBack("Wholesale Clubs",    "icon11",1.0, amex)
            );
            cashRepo.saveAll(amexRates);

            // ─── Wells Fargo Active Cash ─────────────────────────────────────
            Card wells = new Card();
            wells.setName("Wells Fargo Active Cash");
            wells.setPaymentMethod(PaymentMethod.VISA);
            cardRepo.save(wells);

            List<CashBack> wellsRates = List.of(
                    new CashBack("Grocery Stores",     "icon1", 2.0, wells),
                    new CashBack("Gas Stations",       "icon2", 2.0, wells),
                    new CashBack("EV Charging",        "icon3", 2.0, wells),
                    new CashBack("Dining & Restaurants","icon4",2.0, wells),
                    new CashBack("Online Shopping",    "icon5", 2.0, wells),
                    new CashBack("Travel",             "icon6", 2.0, wells),
                    new CashBack("Streaming Services", "icon7", 2.0, wells),
                    new CashBack("Drugstores",         "icon8", 2.0, wells),
                    new CashBack("Home Improvement Stores","icon9",2.0,wells),
                    new CashBack("Transit & Commuting","icon10",2.0, wells),
                    new CashBack("Wholesale Clubs",    "icon11",2.0, wells)
            );
            cashRepo.saveAll(wellsRates);
        };
    }
}

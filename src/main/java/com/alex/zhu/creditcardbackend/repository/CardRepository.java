package com.alex.zhu.creditcardbackend.repository;

import com.alex.zhu.creditcardbackend.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByName(String name);
}
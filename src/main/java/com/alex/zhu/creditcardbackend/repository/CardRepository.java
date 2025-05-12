package com.alex.zhu.creditcardbackend.repository;

import com.alex.zhu.creditcardbackend.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> { }
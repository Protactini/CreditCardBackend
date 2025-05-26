// src/main/java/com/alex/zhu/creditcardbackend/repository/CashBackRepository.java
package com.alex.zhu.creditcardbackend.repository;

import com.alex.zhu.creditcardbackend.model.CashBack;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CashBackRepository extends CrudRepository<CashBack, Long> {

    @Modifying
    @Query("DELETE FROM CashBack cb WHERE cb.card.id = :cardId")
    void deleteByCardId(Long cardId);
}

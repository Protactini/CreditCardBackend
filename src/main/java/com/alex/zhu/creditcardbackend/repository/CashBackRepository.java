// src/main/java/com/alex/zhu/creditcardbackend/repository/CashBackRepository.java
package com.alex.zhu.creditcardbackend.repository;

import com.alex.zhu.creditcardbackend.model.CashBack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CashBackRepository extends JpaRepository<CashBack, Long> {

    Optional<CashBack> findByCardIdAndArea(Long cardId, String area);


    /**
     * Deletes all CashBack entries belonging to the given Card ID.
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM CashBack cb WHERE cb.card.id = :cardId")
    void deleteByCardId(Long cardId);
}

package ru.sviridov.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sviridov.spring.entity.Card;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByUserId(Long id);

    @Query("select c from Card c where c.user.id = ?1 and c.id = ?2")
    Optional<Card> findCardByUserId(Long userId, Long cardId);

    Optional<Card> findByTitle(String title);
}
package org.example.cardshop.service;

import org.example.cardshop.model.Card;

import java.util.List;
import java.util.Optional;

public interface ICardService {
    List<Card> findAll();
    Optional<Card> findById(Long id);
    Card save(Card card);
    void deleteById(Long id);
}


package org.example.cardshop.dto;

import org.example.cardshop.model.Card;

public class CardMapper {

    public static CardDto toDto(Card card) {
        CardDto dto = new CardDto();
        dto.setId(card.getId());
        dto.setName(card.getName());
        dto.setDescription(card.getDescription());
        dto.setPrice(card.getPrice());
        return dto;
    }

    public static Card toEntity(CardDto dto) {
        Card card = new Card();
        card.setId(dto.getId());
        card.setName(dto.getName());
        card.setDescription(dto.getDescription());
        card.setPrice(dto.getPrice());
        return card;
    }
}


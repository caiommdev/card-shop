package org.example.cardshop.dto;

import org.example.cardshop.model.Card;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardMapperTest {

    @Test
    void testToDto() {
        Card card = new Card();
        card.setId(1L);
        card.setName("Test Card");
        card.setDescription("Test Description");
        card.setPrice(10.0);

        CardDto dto = CardMapper.toDto(card);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test Card", dto.getName());
    }

    @Test
    void testToEntity() {
        CardDto dto = new CardDto();
        dto.setId(1L);
        dto.setName("Test Card");
        dto.setDescription("Test Description");
        dto.setPrice(10.0);

        Card card = CardMapper.toEntity(dto);

        assertNotNull(card);
        assertEquals(1L, card.getId());
        assertEquals("Test Card", card.getName());
    }
}


package org.example.cardshop.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardDtoTest {

    @Test
    void testCardDtoGettersAndSetters() {
        CardDto dto = new CardDto();
        dto.setId(1L);
        dto.setName("Test Card");
        dto.setDescription("Test Description");
        dto.setPrice(99.99);

        assertEquals(1L, dto.getId());
        assertEquals("Test Card", dto.getName());
        assertEquals("Test Description", dto.getDescription());
        assertEquals(99.99, dto.getPrice());
    }

    @Test
    void testCardDtoCreation() {
        CardDto dto = new CardDto();
        dto.setName("Magic Card");
        dto.setDescription("A powerful card");
        dto.setPrice(50.0);

        assertNotNull(dto);
        assertEquals("Magic Card", dto.getName());
        assertEquals("A powerful card", dto.getDescription());
        assertEquals(50.0, dto.getPrice());
    }
}


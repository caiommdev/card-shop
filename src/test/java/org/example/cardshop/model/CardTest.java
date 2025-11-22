package org.example.cardshop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void testCardGettersAndSetters() {
        Card card = new Card();
        card.setId(1L);
        card.setName("Test Card");
        card.setDescription("Test Description");
        card.setPrice(99.99);

        assertEquals(1L, card.getId());
        assertEquals("Test Card", card.getName());
        assertEquals("Test Description", card.getDescription());
        assertEquals(99.99, card.getPrice());
    }

    @Test
    void testCardCreation() {
        Card card = new Card();
        card.setName("Magic Card");
        card.setDescription("A powerful card");
        card.setPrice(50.0);

        assertNotNull(card);
        assertEquals("Magic Card", card.getName());
        assertEquals("A powerful card", card.getDescription());
        assertEquals(50.0, card.getPrice());
    }
}


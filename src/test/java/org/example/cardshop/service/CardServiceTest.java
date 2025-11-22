package org.example.cardshop.service;

import org.example.cardshop.model.Card;
import org.example.cardshop.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardService cardService;

    private Card card;

    @BeforeEach
    void setUp() {
        card = new Card();
        card.setId(1L);
        card.setName("Test Card");
        card.setDescription("Test Description");
        card.setPrice(10.0);
    }

    @Test
    void testFindAll() {
        when(cardRepository.findAll()).thenReturn(Collections.singletonList(card));

        List<Card> result = cardService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(cardRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        Optional<Card> result = cardService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Card", result.get().getName());
        verify(cardRepository, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        when(cardRepository.save(any(Card.class))).thenReturn(card);

        Card result = cardService.save(card);

        assertNotNull(result);
        assertEquals("Test Card", result.getName());
        verify(cardRepository, times(1)).save(card);
    }
}


package org.example.cardshop.controller;

import org.example.cardshop.model.Card;
import org.example.cardshop.service.ICardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CardController.class)
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICardService cardService;

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
    void testIndex() throws Exception {
        when(cardService.findAll()).thenReturn(Collections.singletonList(card));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));

        verify(cardService, times(1)).findAll();
    }

    @Test
    void testAddCardForm() throws Exception {
        mockMvc.perform(get("/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-card"));
    }

    @Test
    void testAddCardSuccess() throws Exception {
        when(cardService.save(any(Card.class))).thenReturn(card);

        mockMvc.perform(post("/add")
                        .param("name", "Test Card")
                        .param("description", "Test Description")
                        .param("price", "10.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(cardService, times(1)).save(any(Card.class));
    }

    @Test
    void testEditCardForm() throws Exception {
        when(cardService.findById(1L)).thenReturn(Optional.of(card));

        mockMvc.perform(get("/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-card"));

        verify(cardService, times(1)).findById(1L);
    }

    @Test
    void testDeleteCard() throws Exception {
        doNothing().when(cardService).deleteById(1L);

        mockMvc.perform(get("/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(cardService, times(1)).deleteById(1L);
    }
}


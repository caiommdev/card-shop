package org.example.cardshop.controller;

import jakarta.validation.Valid;
import org.example.cardshop.dto.CardDto;
import org.example.cardshop.dto.CardMapper;
import org.example.cardshop.model.Card;
import org.example.cardshop.service.ICardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.stream.Collectors;

@Controller
public class CardController {

    @Autowired
    private ICardService cardService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("cards", cardService.findAll().stream()
                .map(CardMapper::toDto)
                .collect(Collectors.toList()));
        return "index";
    }

    @GetMapping("/add")
    public String addCardForm(Model model) {
        model.addAttribute("card", new CardDto());
        return "add-card";
    }

    @PostMapping("/add")
    public String addCard(@Valid CardDto cardDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("card", cardDto);
            return "add-card";
        }
        cardService.save(CardMapper.toEntity(cardDto));
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editCardForm(@PathVariable("id") long id, Model model) {
        Card card = cardService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid card Id:" + id));
        model.addAttribute("card", CardMapper.toDto(card));
        return "edit-card";
    }

    @PostMapping("/edit/{id}")
    public String editCard(@PathVariable("id") long id, @Valid CardDto cardDto, BindingResult result, Model model) {
            cardDto.setId(id);
            model.addAttribute("card", cardDto);
        if (result.hasErrors()) {
            return "edit-card";
        }
        cardService.save(CardMapper.toEntity(cardDto));
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteCard(@PathVariable("id") long id) {
        cardService.deleteById(id);
        return "redirect:/";
    }
}

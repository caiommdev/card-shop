package org.example.cardshop.controller;

import jakarta.validation.Valid;
import org.example.cardshop.model.Card;
import org.example.cardshop.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CardController {

    @Autowired
    private CardService cardService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("cards", cardService.findAll());
        return "index";
    }

    @GetMapping("/add")
    public String addCardForm(Model model) {
        model.addAttribute("card", new Card());
        return "add-card";
    }

    @PostMapping("/add")
    public String addCard(@Valid Card card, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-card";
        }
        cardService.save(card);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editCardForm(@PathVariable("id") long id, Model model) {
        Card card = cardService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid card Id:" + id));
        model.addAttribute("card", card);
        return "edit-card";
    }

    @PostMapping("/edit/{id}")
    public String editCard(@PathVariable("id") long id, @Valid Card card, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "edit-card";
        }
        cardService.save(card);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteCard(@PathVariable("id") long id) {
        cardService.deleteById(id);
        return "redirect:/";
    }
}

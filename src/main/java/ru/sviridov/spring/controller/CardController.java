package ru.sviridov.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sviridov.spring.dto.CardDto;
import ru.sviridov.spring.service.CardService;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {

    CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/")
    public List<CardDto> getAllCards() {
        return cardService.getAll();
    }

    @GetMapping("/{id}")
    public CardDto getCardById(@PathVariable(value = "id") Long id) {
        return cardService.getById(id);
    }

    @PostMapping
    public CardDto addCard(@RequestBody CardDto cardDto) {
        return cardService.saveOrUpdate(cardDto);
    }

    @PutMapping({"/{id}"})
    public CardDto updateCard(@PathVariable(value = "id") Long id, @RequestBody CardDto cardDto) {
        return cardService.updateById(id, cardDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCard(@PathVariable(value = "id") Long id) {
        cardService.deleteById(id);
    }
}

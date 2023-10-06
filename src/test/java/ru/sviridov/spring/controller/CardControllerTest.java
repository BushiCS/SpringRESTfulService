package ru.sviridov.spring.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sviridov.spring.dto.CardDto;
import ru.sviridov.spring.entity.Card;
import ru.sviridov.spring.mapper.CardMapper;
import ru.sviridov.spring.service.CardService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardControllerTest {

    @Mock
    CardService cardService;

    @InjectMocks
    CardController cardController;






    @Test
    void getAllCards() {
        List<Card> cards = new ArrayList<>();
        Card card1 = new Card();
        card1.setId(1L);
        card1.setTitle("VTB");
        Card card2 = new Card();
        card2.setId(2L);
        card2.setTitle("SBER");
        cards.add(card1);
        cards.add(card2);
        List<CardDto> dtoList = CardMapper.INSTANCE.toDtoList(cards);
        when(cardService.getAll()).thenReturn(dtoList);
        List<CardDto> actualCards = cardController.getAllCards();
        int expectedSize = dtoList.size();
        int actualSize = actualCards.size();
        assertEquals(expectedSize, actualSize);
        assertEquals(card1.getTitle(), actualCards.get(0).getTitle());
        assertEquals(card2.getTitle(), actualCards.get(1).getTitle());
    }

    @Test
    void getCardById() {
        Long id = 1L;
        Card card = new Card();
        card.setId(id);
        card.setTitle("VTB");
        CardDto dto = CardMapper.INSTANCE.toDto(card);
        when(cardService.getById(id)).thenReturn(dto);
        CardDto actualCard = cardController.getCardById(id);
        String expectedName = dto.getTitle();
        String actualName = actualCard.getTitle();
        assertEquals(expectedName, actualName);
        verify(cardService).getById(id);
    }

    @Test
    void addCard() {
        CardDto cardDto = new CardDto();
        cardDto.setTitle("VTB");
        when(cardService.saveOrUpdate(cardDto)).thenReturn(cardDto);
        CardDto actualDto = cardController.addCard(cardDto);
        String expectedName = cardDto.getTitle();
        String actualName = actualDto.getTitle();
        assertEquals(expectedName, actualName);
        verify(cardService).saveOrUpdate(cardDto);
    }

    @Test
    void updateCard() {
        CardDto cardDto = new CardDto();
        Long id = 1L;
        cardDto.setTitle("VTB");
        when(cardService.updateById(id, cardDto)).thenReturn(cardDto);
        CardDto actualDto = cardController.updateCard(id, cardDto);
        String expectedName = cardDto.getTitle();
        String actualName = actualDto.getTitle();
        assertEquals(expectedName, actualName);
        verify(cardService).updateById(id, cardDto);
    }

    @Test
    void deleteCard() {
        Long id = 1L;
        cardController.deleteCard(id);
        verify(cardService, times(1)).deleteById(id);
    }
}
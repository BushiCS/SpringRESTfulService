package ru.sviridov.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sviridov.spring.dto.CardDto;
import ru.sviridov.spring.entity.Card;
import ru.sviridov.spring.entity.User;
import ru.sviridov.spring.mapper.CardMapper;
import ru.sviridov.spring.repository.CardRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceImplTest {
    @Mock
    CardRepository cardRepository;
    @InjectMocks
    CardServiceImpl cardService;
    @Captor
    ArgumentCaptor<Card> cardCaptor;

    @Test
    void getAll() {
        Card card1 = new Card();
        Card card2 = new Card();

        card1.setTitle("VTB");
        card2.setTitle("SBER");

        List<Card> cards = List.of(card1, card2);

        when(cardRepository.findAll()).thenReturn(cards);
        List<CardDto> actualCards = cardService.getAll();
        int expectedSize = cards.size();
        int actualSize = actualCards.size();

        assertEquals(expectedSize, actualSize);

        for (int i = 0; i < cards.size(); i++) {
            assertEquals(cards.get(i).getTitle(), actualCards.get(i).getTitle());
        }

    }

    @Test
    void getById() {
        Long id = 1L;
        Card card = new Card();
        card.setTitle("VTB");
        User user = new User();
        user.setName("Bill");
        card.setUser(user);
        when(cardRepository.findById(id)).thenReturn(Optional.of(card));
        CardDto cardDto = cardService.getById(id);
        String expectedName = card.getTitle();
        String actualName = cardDto.getTitle();
        assertEquals(expectedName, actualName);
        verify(cardRepository).findById(id);
    }

    @Test
    void getCardsByUserId() {
        Long id = 1L;
        Card card1 = new Card();
        Card card2 = new Card();
        card1.setTitle("VTB");
        card2.setTitle("SBER");
        List<Card> expectedCards = List.of(card1, card2);
        when(cardRepository.findByUserId(id)).thenReturn(expectedCards);
        List<CardDto> actualCards = cardService.getCardsByUserId(id);
        int expectedSize = expectedCards.size();
        int actualSize = actualCards.size();
        assertEquals(expectedSize, actualSize);
        verify(cardRepository).findByUserId(id);
    }

    @Test
    void getCardByUserId() {
        Long userId = 1L;
        Long cardId = 1L;
        Card card = new Card();
        card.setTitle("VTB");
        when(cardRepository.findCardByUserId(userId, cardId)).thenReturn(Optional.of(card));
        CardDto actualCardDto = cardService.getCardByUserId(userId, cardId);
        String expectedName = card.getTitle();

        String actualName = actualCardDto.getTitle();
        assertEquals(expectedName, actualName);
        verify(cardRepository).findCardByUserId(userId, cardId);
    }

    @Test
    void saveOrUpdate() {
        CardDto cardDto = new CardDto();
        cardDto.setTitle("VTB");
        Long id = 1L;
        Card card = CardMapper.INSTANCE.toEntity(cardDto);
        card.setId(id);
        when(cardRepository.save(any(Card.class))).thenReturn(card);
        CardDto savedCardDto = cardService.saveOrUpdate(cardDto);
        String expectedName = cardDto.getTitle();
        String actualName = savedCardDto.getTitle();
        assertEquals(expectedName, actualName);
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    void updateById_ExistingCard() {
        Long id = 1L;
        CardDto cardDto = new CardDto();
        cardDto.setTitle("VTB");
        Card card = CardMapper.INSTANCE.toEntity(cardDto);
        card.setId(id);

        when(cardRepository.findById(id)).thenReturn(Optional.of(card));
        when(cardRepository.save(any(Card.class))).thenReturn(card);

        CardDto savedCardDto = cardService.updateById(id, cardDto);
        String expectedName = cardDto.getTitle();
        String acutalName = savedCardDto.getTitle();

        assertEquals(expectedName, acutalName);
        verify(cardRepository).findById(id);
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    void UpdateById_NewCard() {
        Long id = 1L;
        CardDto cardDto = new CardDto();
        cardDto.setTitle("VTB");
        Card card = CardMapper.INSTANCE.toEntity(cardDto);
        when(cardRepository.findById(id)).thenReturn(Optional.empty());
        when(cardRepository.save(any(Card.class))).thenReturn(card);

        CardDto savedCardDto = cardService.updateById(id, cardDto);
        String expectedName = cardDto.getTitle();
        String actualName = savedCardDto.getTitle();

        assertEquals(expectedName, actualName);
        verify(cardRepository).findById(id);
        verify(cardRepository).save(cardCaptor.capture());

        Card updatedCard = cardCaptor.getValue();
        assertEquals(id, updatedCard.getId());
        assertEquals(expectedName, updatedCard.getTitle());
    }

    @Test
    void deleteById() {
        Long id = 1L;
        cardService.deleteById(id);
        verify(cardRepository, times(1)).deleteById(id);
    }
}

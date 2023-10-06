package ru.sviridov.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sviridov.spring.dto.CardDto;
import ru.sviridov.spring.entity.Card;
import ru.sviridov.spring.mapper.CardMapper;
import ru.sviridov.spring.repository.CardRepository;

import java.util.List;

@Service
@Transactional
public class CardServiceImpl implements CardService {

    CardRepository cardRepository;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public List<CardDto> getAll() {
        return CardMapper.INSTANCE.toDtoList(cardRepository.findAll());
    }

    @Override
    public CardDto getById(Long id) {
        return CardMapper.INSTANCE.toDto(cardRepository.findById(id).orElseThrow());
    }

    @Override
    public List<CardDto> getCardsByUserId(Long id) {
        return CardMapper.INSTANCE.toDtoList(cardRepository.findByUserId(id));
    }

    @Override
    public CardDto getCardByUserId(Long userId, Long cardId) {
        return CardMapper.INSTANCE.toDto(cardRepository.findCardByUserId(userId, cardId).orElseThrow());
    }

    @Override
    public CardDto saveOrUpdate(CardDto cardDto) {
        Card entity = CardMapper.INSTANCE.toEntity(cardDto);
        Card saved = cardRepository.save(entity);
        return CardMapper.INSTANCE.toDto(saved);
    }

    @Override
    public CardDto updateById(Long id, CardDto cardDto) {
        Card entity = CardMapper.INSTANCE.toEntity(cardDto);
        return CardMapper.INSTANCE.toDto(cardRepository.findById(id).map(card -> {
            card.setTitle(entity.getTitle());
            return cardRepository.save(card);
        }).orElseGet(() -> {
            entity.setId(id);
            return cardRepository.save(entity);
        }));
    }

    @Override
    public void deleteById(Long id) {
        cardRepository.deleteById(id);
    }


}

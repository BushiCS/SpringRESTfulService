package ru.sviridov.spring.service;

import ru.sviridov.spring.dto.CardDto;

import java.util.List;

public interface CardService {

    List<CardDto> getAll();

    List<CardDto> getCardsByUserId(Long id);


    CardDto getCardByUserId(Long userId, Long cardId);

    CardDto getById(Long id);


    CardDto saveOrUpdate(CardDto cardDto);

    CardDto updateById(Long id, CardDto cardDto);

    void deleteById(Long id);

}

package ru.sviridov.spring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.sviridov.spring.dto.CardDto;
import ru.sviridov.spring.entity.Card;

import java.util.List;

@Mapper
public interface CardMapper {
    CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);
    CardDto toDto(Card card);

    List<CardDto> toDtoList (List<Card> cards);

    Card toEntity (CardDto dto);

}

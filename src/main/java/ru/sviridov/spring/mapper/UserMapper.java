package ru.sviridov.spring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.sviridov.spring.dto.UserDto;
import ru.sviridov.spring.dto.UserWithCardsAndProductsDto;
import ru.sviridov.spring.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserDto toDto(User user);
    List<UserDto>toDtoList(List<User> users);

    User toEntity (UserDto userDto);

    List<UserWithCardsAndProductsDto> toDtoWithCardsAndProducts(List<User> users);

    UserWithCardsAndProductsDto toDtoWithCardAndProduct (User user);

}

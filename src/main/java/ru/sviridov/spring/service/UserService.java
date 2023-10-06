package ru.sviridov.spring.service;

import ru.sviridov.spring.dto.CardDto;
import ru.sviridov.spring.dto.ProductDto;
import ru.sviridov.spring.dto.UserDto;
import ru.sviridov.spring.dto.UserWithCardsAndProductsDto;

import java.util.List;

public interface UserService {

    List<UserWithCardsAndProductsDto> getAll();


    UserWithCardsAndProductsDto getById(Long id);

    UserDto saveOrUpdate(UserDto userDto);


    UserDto updateById(Long id, UserDto userDto);

    void deleteById(Long id);


    CardDto addCardToUser(Long userId, CardDto cardDto);

    ProductDto addProductToUser(Long id, ProductDto productDto);
}

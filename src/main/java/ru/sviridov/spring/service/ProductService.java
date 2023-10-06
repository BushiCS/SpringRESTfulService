package ru.sviridov.spring.service;

import ru.sviridov.spring.dto.ProductDto;
import ru.sviridov.spring.dto.ProductDtoWithUsers;
import ru.sviridov.spring.dto.UserDto;

import java.util.List;

public interface ProductService {

    List<ProductDtoWithUsers> getAll();

    List<ProductDto> getProductsByUserId(Long id);


    ProductDto getProductByUserId(Long userId, Long productId);

    ProductDtoWithUsers getById(Long id);

    List<UserDto> getProductUsers(Long id);

    UserDto getProductUser(Long productId, Long userId);

    ProductDto saveOrUpdate(ProductDto productDto);

    ProductDto updateById(Long id, ProductDto productDto);

    void deleteById(Long id);

}

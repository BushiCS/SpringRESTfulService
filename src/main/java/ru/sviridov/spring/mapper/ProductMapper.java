package ru.sviridov.spring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.sviridov.spring.dto.ProductDto;
import ru.sviridov.spring.dto.ProductDtoWithUsers;
import ru.sviridov.spring.entity.Product;

import java.util.List;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDto toDto(Product product);
    List<ProductDto> toDtoList(List<Product> product);
    ProductDtoWithUsers toDtoWithUsers(Product product);

    List<ProductDtoWithUsers> toDtoWithUsersList(List<Product> products);

    Product toEntity (ProductDto productDto);


}

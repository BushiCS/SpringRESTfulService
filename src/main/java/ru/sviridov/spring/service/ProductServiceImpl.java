package ru.sviridov.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sviridov.spring.dto.ProductDto;
import ru.sviridov.spring.dto.ProductDtoWithUsers;
import ru.sviridov.spring.dto.UserDto;
import ru.sviridov.spring.entity.Product;
import ru.sviridov.spring.mapper.ProductMapper;
import ru.sviridov.spring.mapper.UserMapper;
import ru.sviridov.spring.repository.ProductRepository;
import ru.sviridov.spring.repository.UserRepository;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ProductDtoWithUsers> getAll() {

        return ProductMapper.INSTANCE.toDtoWithUsersList(productRepository.findAll());
    }

    @Override
    public List<ProductDto> getProductsByUserId(Long id) {
        return ProductMapper.INSTANCE.toDtoList(productRepository.findListByUserId(id));
    }

    @Override
    public ProductDto getProductByUserId(Long userId, Long productId) {
        return ProductMapper.INSTANCE.toDto(productRepository.findByUserId(userId, productId).orElseThrow());
    }

    @Override
    public ProductDtoWithUsers getById(Long id) {
        return ProductMapper.INSTANCE.toDtoWithUsers(productRepository.findById(id).orElseThrow());
    }

    @Override
    public List<UserDto> getProductUsers(Long id) {
        return UserMapper.INSTANCE.toDtoList(userRepository.findListByProductId(id));
    }

    @Override
    public UserDto getProductUser(Long productId, Long userId) {
        return UserMapper.INSTANCE.toDto(userRepository.findByProductId(productId, userId));
    }

    @Override
    public ProductDto saveOrUpdate(ProductDto productDto) {
        Product productToUpdate = ProductMapper.INSTANCE.toEntity(productDto);
        Product saved = productRepository.save(productToUpdate);
        return ProductMapper.INSTANCE.toDto(saved);
    }

    @Override
    public ProductDto updateById(Long id, ProductDto productDto) {
        Product entity = ProductMapper.INSTANCE.toEntity(productDto);
        return ProductMapper.INSTANCE.toDto(productRepository.findById(id).map(product -> {
            product.setTitle(entity.getTitle());
            return productRepository.save(product);
        }).orElseGet(() -> {
            entity.setId(id);
            return productRepository.save(entity);
        }));
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}

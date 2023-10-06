package ru.sviridov.spring.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sviridov.spring.dto.ProductDto;
import ru.sviridov.spring.dto.ProductDtoWithUsers;
import ru.sviridov.spring.dto.UserDto;
import ru.sviridov.spring.entity.Product;
import ru.sviridov.spring.entity.User;
import ru.sviridov.spring.mapper.ProductMapper;
import ru.sviridov.spring.mapper.UserMapper;
import ru.sviridov.spring.repository.ProductRepository;
import ru.sviridov.spring.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    ProductServiceImpl productService;

    @Captor
    ArgumentCaptor<Product> productCaptor;

    @Test
    void getAll() {
        Product product1 = new Product();
        Product product2 = new Product();

        User user1 = new User();
        User user2 = new User();

        user1.setName("John");

        user2.setName("Bill");
        product1.setId(1L);
        product1.setTitle("Milk");
        product1.setUsers(Set.of(user1, user2));

        product2.setId(2L);
        product2.setTitle("Cheese");
        List<Product> products = List.of(product1, product2);

        when(productRepository.findAll()).thenReturn(products);
        List<ProductDtoWithUsers> productDtoList = productService.getAll();
        int expectedSize = products.size();
        int actualSize = productDtoList.size();
        assertEquals(expectedSize, actualSize);
        verify(productRepository).findAll();
    }

    @Test
    void getProductsByUserId() {
        Long id = 1L;
        Product product1 = new Product();
        Product product2 = new Product();
        product1.setTitle("Sausage");
        product2.setTitle("Cheese");
        List<Product> products = List.of(product1, product2);
        when(productRepository.findListByUserId(id)).thenReturn(products);
        List<ProductDto> dtoProductByUserId = productService.getProductsByUserId(id);
        int expectedSize = products.size();
        int actualSize = dtoProductByUserId.size();

        assertEquals(expectedSize, actualSize);
        verify(productRepository).findListByUserId(id);
    }

    @Test
    void getProductByUserId() {
        Long userId = 1L;
        Long productId = 1L;
        Product product = new Product();
        product.setTitle("Cheese");
        when(productRepository.findByUserId(userId, productId)).thenReturn(Optional.of(product));
        ProductDto ProductDto = productService.getProductByUserId(userId, productId);
        String expectedName = product.getTitle();
        String actualName = ProductDto.getTitle();
        assertEquals(expectedName, actualName);
        verify(productRepository).findByUserId(userId, productId);

    }

    @Test
    void getById() {
        Long id = 1L;
        User user = new User();
        user.setName("John");
        Set<User> users = Set.of(user);
        Product product = new Product();
        product.setId(id);
        product.setUsers(users);
        product.setTitle("Cheese");
        ProductDtoWithUsers dtoWithUsers = ProductMapper.INSTANCE.toDtoWithUsers(product);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        ProductDtoWithUsers actualProductDto = productService.getById(id);
        String expectedName = product.getTitle();
        String actualName = actualProductDto.getTitle();
        int expectedSize = product.getUsers().size();
        int actualSize = actualProductDto.getUsers().size();
        Assertions.assertEquals(expectedSize, actualSize);
        Assertions.assertEquals(expectedName, actualName);
        verify(productRepository).findById(id);
    }

    @Test
    void getProductUsers() {
        Long id = 1L;
        User user1 = new User();
        User user2 = new User();

        user1.setName("Michael");
        user2.setName("Jack");

        List<User> users = List.of(user1, user2);
        when(userRepository.findListByProductId(id)).thenReturn(users);
        List<UserDto> productUsers = productService.getProductUsers(id);
        int expectedSize = users.size();
        int actualSize = productUsers.size();

        assertEquals(expectedSize, actualSize);
        verify(userRepository).findListByProductId(id);
    }

    @Test
    void getProductUser() {
        User user = new User();
        user.setName("Jack");
        Long userId = 1L;
        Long productId = 1L;

        when(userRepository.findByProductId(productId, userId)).thenReturn(user);
        UserDto userDto = productService.getProductUser(productId, userId);
        String expectedName = user.getName();
        String actualName = userDto.getName();
        assertEquals(expectedName, actualName);
        verify(userRepository).findByProductId(productId, userId);
    }

    @Test
    void saveOrUpdate() {
        ProductDto productDto = new ProductDto();
        productDto.setTitle("Juice");
        Long id = 1L;
        Product product = ProductMapper.INSTANCE.toEntity(productDto);
        product.setId(id);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        ProductDto savedProductDto = productService.saveOrUpdate(productDto);
        String expectedName = productDto.getTitle();
        String actualName = savedProductDto.getTitle();
        assertEquals(expectedName, actualName);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateById_ExistingProduct() {
        Long id = 1L;
        Product product = new Product();
        product.setId(id);
        product.setTitle("Juice");
        ProductDto productDto = ProductMapper.INSTANCE.toDto(product);
        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDto savedProductDto = productService.updateById(id, productDto);
        String expectedName = product.getTitle();
        String actualName = savedProductDto.getTitle();
        verify(productRepository).findById(id);
        verify(productRepository).save(product);


    }

    @Test
    void updateById_newProduct() {
        Long id = 1L;
        ProductDto productDto = new ProductDto();
        productDto.setTitle("Juice");
        Product product = ProductMapper.INSTANCE.toEntity(productDto);
        when(productRepository.findById(id)).thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDto savedProductDto = productService.updateById(id, productDto);
        String expectedName = productDto.getTitle();
        String actualName = savedProductDto.getTitle();

        assertEquals(expectedName, actualName);
        verify(productRepository).findById(id);
        verify(productRepository).save(productCaptor.capture());
        Product updatedProduct = productCaptor.getValue();
        assertEquals(id, updatedProduct.getId());
        assertEquals(expectedName, updatedProduct.getTitle());
    }
    @Test
    void deleteById() {
        Long id = 1L;
        productService.deleteById(id);
        verify(productRepository, times(1)).deleteById(id);
    }
}
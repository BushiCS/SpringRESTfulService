package ru.sviridov.spring.controller;

import jakarta.persistence.Entity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sviridov.spring.dto.ProductDto;
import ru.sviridov.spring.dto.ProductDtoWithUsers;
import ru.sviridov.spring.dto.UserDto;
import ru.sviridov.spring.service.ProductService;
import ru.sviridov.spring.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {
    @Mock
    ProductService productService;

    @Mock
    UserService userService;

    @InjectMocks
    ProductController productController;

    @Test
    void getAllProduct() {
        ProductDtoWithUsers productDto1 = new ProductDtoWithUsers();
        ProductDtoWithUsers productDto2 = new ProductDtoWithUsers();
        productDto1.setTitle("Cheese");
        productDto2.setTitle("Milk");
        UserDto userDto1 = new UserDto();
        UserDto userDto2 = new UserDto();
        userDto1.setName("Bill");
        userDto2.setName("John");
        List<UserDto> users = List.of(userDto1, userDto2);
        productDto1.setUsers(users);
        List<ProductDtoWithUsers> products = List.of(productDto1, productDto2);
        when(productService.getAll()).thenReturn(products);
        List<ProductDtoWithUsers> allProduct = productController.getAllProduct();
        int expectedSize = products.size();
        int actualSize = allProduct.size();
        String expectedName = products.get(0).getTitle();
        String actualName = allProduct.get(0).getTitle();
        int expectedUsersSize = products.get(0).getUsers().size();
        int actualUsersSize = allProduct.get(0).getUsers().size();
        assertEquals(expectedName, actualName);
        assertEquals(expectedSize, actualSize);
        assertEquals(expectedUsersSize, actualUsersSize);
    }

    @Test
    void getProduct() {
        Long id = 1L;
        ProductDtoWithUsers productDto = new ProductDtoWithUsers();
        productDto.setTitle("Sausage");
        UserDto userDto = new UserDto();
        productDto.setUsers(List.of(userDto));
        when(productService.getById(id)).thenReturn(productDto);
        ProductDtoWithUsers actualProduct = productController.getProduct(id);
        int expectedUsersSize = productDto.getUsers().size();
        int actualUsersSize = actualProduct.getUsers().size();
        String expectedName = productDto.getTitle();
        String actualName = actualProduct.getTitle();
        assertEquals(expectedName, actualName);
        assertEquals(expectedUsersSize, actualUsersSize);
    }

    @Test
    void getProductUsers() {
        UserDto userDto1 = new UserDto();
        UserDto userDto2 = new UserDto();
        userDto1.setName("Bill");
        userDto2.setName("Jack");
        Long id = 1L;
        List<UserDto> users = List.of(userDto1, userDto2);
        when(productService.getProductUsers(id)).thenReturn(users);
        List<UserDto> productUsers = productController.getProductUsers(id);
        int expectedSize = users.size();
        int actualSize = productUsers.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    void getProductUser() {
        UserDto userDto = new UserDto();
        userDto.setName("Bill");
        Long productId = 1L;
        Long userId = 1L;
        when(productService.getProductUser(productId, userId)).thenReturn(userDto);
        UserDto actualDto = productController.getProductUser(productId, userId);
        String expectedDtoName = userDto.getName();
        String actualDtoName = actualDto.getName();
        assertEquals(expectedDtoName, actualDtoName);
    }

    @Test
    void addProduct() {
        ProductDto productDto = new ProductDto();
        productDto.setTitle("VTB");
        when(productService.saveOrUpdate(productDto)).thenReturn(productDto);
        ProductDto actualProductDto = productController.addProduct(productDto);
String expectedProductDtoTitle = productDto.getTitle();
        String actualProductDtoTitle = actualProductDto.getTitle();
 assertEquals(expectedProductDtoTitle, actualProductDtoTitle);
    }

    @Test
    void updateProduct() {
        ProductDto productDto = new ProductDto();
        Long id = 1L;
        productDto.setTitle("SBER");
        when(productService.updateById(id, productDto)).thenReturn(productDto);
        ProductDto actualProductDto = productController.updateProduct(id, productDto);
        String expectedProductDtoTitle = productDto.getTitle();
        String actualProductDtoTitle = actualProductDto.getTitle();
        assertEquals(expectedProductDtoTitle, actualProductDtoTitle);
    }

    @Test
    void deleteProduct() {
        Long id = 1L;
        productController.deleteProduct(id);
        verify(productService, times(1)).deleteById(id);
    }
}
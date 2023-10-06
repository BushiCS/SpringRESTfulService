package ru.sviridov.spring.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sviridov.spring.dto.CardDto;
import ru.sviridov.spring.dto.ProductDto;
import ru.sviridov.spring.dto.ProductDtoWithUsers;
import ru.sviridov.spring.dto.UserDto;
import ru.sviridov.spring.dto.UserWithCardsAndProductsDto;
import ru.sviridov.spring.entity.Card;
import ru.sviridov.spring.entity.Product;
import ru.sviridov.spring.mapper.CardMapper;
import ru.sviridov.spring.mapper.ProductMapper;
import ru.sviridov.spring.service.CardService;
import ru.sviridov.spring.service.ProductService;
import ru.sviridov.spring.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    UserService userService;

    @Mock
    CardService cardService;

    @Mock
    ProductService productService;
    @InjectMocks
    UserController userController;

    @Test
    void getAllUsers() {
        UserWithCardsAndProductsDto userDto1 = new UserWithCardsAndProductsDto();
        UserWithCardsAndProductsDto userDto2 = new UserWithCardsAndProductsDto();
        Product product1 = new Product();
        Product product2 = new Product();
        Card card1 = new Card();
        Card card2 = new Card();
        List<Product> products = List.of(product1, product2);
        List<ProductDto> dtoProducts = ProductMapper.INSTANCE.toDtoList(products);
        List<Card> cards = List.of(card1, card2);
        List<CardDto> dtoCards = CardMapper.INSTANCE.toDtoList(cards);
        userDto1.setProducts(dtoProducts);
        userDto1.setCards(dtoCards);
        List<UserWithCardsAndProductsDto> users = List.of(userDto1, userDto2);
        when(userService.getAll()).thenReturn(users);
        List<UserWithCardsAndProductsDto> actualUsers = userController.getAllUsers();
        int expectedSize = users.size();
        int actualSize = actualUsers.size();
        int expectedProductsSize = users.get(0).getProducts().size();
        int actualProductsSize = actualUsers.get(0).getProducts().size();
        assertEquals(expectedSize, actualSize);
        assertEquals(expectedProductsSize, actualProductsSize);
    }

    @Test
    void getUserById() {
        Long id = 1L;
        UserWithCardsAndProductsDto userDto = new UserWithCardsAndProductsDto();
        CardDto card1 = new CardDto();
        CardDto card2 = new CardDto();
        card1.setTitle("Milk");
        card2.setTitle("Cheese");
        ProductDto product1 = new ProductDto();
        ProductDto product2 = new ProductDto();
        List<CardDto> cards = List.of(card1, card2);
        List<ProductDto> products = List.of(product1, product2);
        userDto.setCards(cards);
        userDto.setProducts(products);
        when(userService.getById(id)).thenReturn(userDto);
        UserWithCardsAndProductsDto actualUserDto = userController.getUserById(id);
        String expectedUserDtoName = userDto.getName();
        String actualUserDtoName = actualUserDto.getName();
        verify(userService).getById(id);
        assertEquals(expectedUserDtoName, actualUserDtoName);
        assertEquals(userDto.getProducts(), actualUserDto.getProducts());
        assertEquals(userDto.getCards(), actualUserDto.getCards());
    }

    @Test
    void getUserCards() {
        CardDto cardDto1 = new CardDto();
        CardDto cardDto2 = new CardDto();
        cardDto1.setTitle("VTB");
        cardDto2.setTitle("SBER");
        Long id = 1L;
        List<CardDto> cards = List.of(cardDto1, cardDto2);
        when(cardService.getCardsByUserId(id)).thenReturn(cards);
        List<CardDto> userCards = userController.getUserCards(id);
        int expectedSize = cards.size();
        int actualSize = userCards.size();
        assertEquals(expectedSize, actualSize);

    }

    @Test
    void getUserCardByCardId() {
        CardDto cardDto1 = new CardDto();
        cardDto1.setTitle("VTB");
        Long userId = 1L;
        Long cardId = 1L;
        when(cardService.getCardByUserId(userId, cardId)).thenReturn(cardDto1);
        CardDto userCardByCardId = userController.getUserCardByCardId(userId, cardId);
        String expectedName = cardDto1.getTitle();
        String actualName = userCardByCardId.getTitle();
        assertEquals(expectedName, actualName);

    }

    @Test
    void getUserProducts() {
        ProductDto productDto1 = new ProductDto();
        ProductDto productDto2 = new ProductDto();
        productDto1.setTitle("Sausage");
        productDto2.setTitle("Milk");
        Long id = 1L;
        List<ProductDto> products = List.of(productDto1, productDto2);
        when(productService.getProductsByUserId(id)).thenReturn(products);
        List<ProductDto> userProducts = userController.getUserProducts(id);
        int expectedSize = products.size();
        int actualSize = userProducts.size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    void getUserProduct() {
        ProductDto productDto = new ProductDto();
        productDto.setTitle("Milk");
        Long userId = 1L;
        Long productId = 1L;
        when(productService.getProductByUserId(userId, productId)).thenReturn(productDto);
        ProductDto userProduct = userController.getUserProduct(userId, productId);
        String expectedName = productDto.getTitle();
        String actualName = userProduct.getTitle();
        assertEquals(expectedName, actualName);

    }

    @Test
    void addUser() {
        UserDto userDto = new UserDto();
        when(userService.saveOrUpdate(userDto)).thenReturn(userDto);
        UserDto userDto1 = userController.addUser(userDto);
        String expectedName = userDto.getName();
        String actualName = userDto1.getName();

        assertEquals(expectedName, actualName);


    }

    @Test
    void addCardToUser() {
        CardDto cardDto = new CardDto();
        cardDto.setTitle("VTB");
        Long id = 1L;
        when(userService.addCardToUser(id, cardDto)).thenReturn(cardDto);
        CardDto actualDto = userController.addCardToUser(id, cardDto);
        String expectedName = cardDto.getTitle();
        String actualName = actualDto.getTitle();
        assertEquals(expectedName, actualName);
    }

    @Test
    void addProductToUser() {
        ProductDto productDto = new ProductDto();
        productDto.setTitle("Milk");
        Long id = 1L;
        when(userService.addProductToUser(id, productDto)).thenReturn(productDto);
        ProductDto actualDto = userController.addProductToUser(id, productDto);
        String expectedName = productDto.getTitle();
        String actualName = actualDto.getTitle();
        assertEquals(expectedName, actualName);
    }

    @Test
    void updateUser() {
        UserDto userDto = new UserDto();
        userDto.setName("Bill");
        Long id = 1L;
        when(userService.updateById(id, userDto)).thenReturn(userDto);
        UserDto actualDto = userController.updateUser(id, userDto);
        String expectedName = userDto.getName();
        String actualName = actualDto.getName();
        assertEquals(expectedName, actualName);
    }

    @Test
    void deleteUser() {
        Long id = 1L;
        userController.deleteUser(id);
        verify(userService, times(1)).deleteById(id);
    }
}
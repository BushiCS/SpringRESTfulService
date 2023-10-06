package ru.sviridov.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.sviridov.spring.dto.CardDto;
import ru.sviridov.spring.dto.ProductDto;
import ru.sviridov.spring.dto.UserDto;
import ru.sviridov.spring.dto.UserWithCardsAndProductsDto;
import ru.sviridov.spring.service.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final ProductService productService;
    private final CardService cardService;
    private final UserService userService;


    @Autowired
    public UserController(UserService userService, CardService cardService, ProductService productService) {
        this.userService = userService;
        this.cardService = cardService;
        this.productService = productService;
    }

    @GetMapping("/")
    public List<UserWithCardsAndProductsDto> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public UserWithCardsAndProductsDto getUserById(@PathVariable(value = "id") Long id) {
        return userService.getById(id);
    }

    @GetMapping("/{id}/cards")
    public List<CardDto> getUserCards(@PathVariable(value = "id") Long id) {
        return cardService.getCardsByUserId(id);
    }

    @GetMapping("/{userId}/cards/{cardId}")
    public CardDto getUserCardByCardId(@PathVariable(value = "userId") Long userId, @PathVariable(value = "cardId") Long cardId) {
        return cardService.getCardByUserId(userId, cardId);
    }

    @GetMapping("/{id}/products")
    public List<ProductDto> getUserProducts(@PathVariable(value = "id") Long id) {
        return productService.getProductsByUserId(id);
    }

    @GetMapping("/{userId}/products/{productId}")
    public ProductDto getUserProduct(@PathVariable(value = "userId") Long userId, @PathVariable(value = "productId") Long productId) {
        return productService.getProductByUserId(userId, productId);
    }

    @PostMapping("/add")
    public UserDto addUser(@RequestBody UserDto userDto) {
        return userService.saveOrUpdate(userDto);
    }

    @PostMapping("/{id}/add/card")
    public CardDto addCardToUser(@PathVariable(value = "id") Long id, @RequestBody CardDto cardDto) {
        return userService.addCardToUser(id, cardDto);
    }

    @PostMapping("/{id}/add/product")
    public ProductDto addProductToUser(@PathVariable(value = "id") Long id, @RequestBody ProductDto productDto) {
        return userService.addProductToUser(id, productDto);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable(value = "id") Long id, @RequestBody UserDto userDto) {
        return userService.updateById(id, userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable(value = "id") Long id) {
        userService.deleteById(id);
    }

}

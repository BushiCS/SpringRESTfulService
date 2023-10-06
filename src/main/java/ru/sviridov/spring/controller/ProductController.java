package ru.sviridov.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.sviridov.spring.dto.ProductDto;
import ru.sviridov.spring.dto.ProductDtoWithUsers;
import ru.sviridov.spring.dto.UserDto;
import ru.sviridov.spring.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public List<ProductDtoWithUsers> getAllProduct() {
        return productService.getAll();
    }

    @GetMapping("/{id}")
    public ProductDtoWithUsers getProduct(@PathVariable(value = "id") Long id) {
        return productService.getById(id);
    }

    @GetMapping("/{id}/users")
    public List<UserDto> getProductUsers(@PathVariable(value = "id") Long id) {
        return productService.getProductUsers(id);
    }

    @GetMapping("/{productId}/users/{userId}")
    public UserDto getProductUser(@PathVariable(value = "productId") Long productId, @PathVariable(value = "userId") Long userId) {
        return productService.getProductUser(productId, userId);
    }

    @PostMapping("/add")
    public ProductDto addProduct(@RequestBody ProductDto productDto) {
        return productService.saveOrUpdate(productDto);
    }

    @PutMapping("/{id}")
    public ProductDto updateProduct(@PathVariable(value = "id") Long id, @RequestBody ProductDto productDto) {
        return productService.updateById(id, productDto);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable(value = "id") Long id) {
        productService.deleteById(id);
    }
}

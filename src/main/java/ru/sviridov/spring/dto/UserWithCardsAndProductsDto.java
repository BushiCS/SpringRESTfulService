package ru.sviridov.spring.dto;

import java.util.List;

public class UserWithCardsAndProductsDto {

    private String name;

    private List<CardDto> cards;

    private List<ProductDto> products;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CardDto> getCards() {
        return cards;
    }

    public void setCards(List<CardDto> cards) {
        this.cards = cards;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }

}

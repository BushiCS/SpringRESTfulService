package ru.sviridov.spring.dto;

import java.util.List;

public class ProductDtoWithUsers {

    private String title;

    private List<UserDto> users;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<UserDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }


}

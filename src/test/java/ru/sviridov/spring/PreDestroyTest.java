package ru.sviridov.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sviridov.spring.entity.Product;
import ru.sviridov.spring.entity.User;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PreDestroyTest {

    private Product product;

    @Mock
    private User user1, user2;

    @BeforeEach
    public void setUp() {
        product = new Product();
        product.getUsers().add(user1);
        product.getUsers().add(user2);
    }

    @Test
    void testRemoveUsersFromProducts() {
        product.removeUsersFromProducts();
        if (user2.getProducts() != null) {
            verify(user2, times(2)).getProducts();
            Assertions.assertFalse(user2.getProducts().contains(product));
        }
    }
}

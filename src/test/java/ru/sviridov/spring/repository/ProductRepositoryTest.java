package ru.sviridov.spring.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.sviridov.DataTestConfig;
import ru.sviridov.spring.entity.Product;
import ru.sviridov.spring.entity.User;
import ru.sviridov.spring.repository.CardRepository;
import ru.sviridov.spring.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringJUnitConfig(DataTestConfig.class)
@Testcontainers
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@TestPropertySource({"classpath:test_db.properties"})
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    private static final PostgreSQLContainer<?> container =
            new PostgreSQLContainer<>("postgres:latest").withDatabaseName("test")
                    .withUsername("test")
                    .withPassword("test")
                    .withInitScript("sql/create-test-table.sql");

    @DynamicPropertySource
    static void addProperties(DynamicPropertyRegistry registry) {
        registry.add("db.url", container::getJdbcUrl);
        registry.add("db.username", container::getUsername);
        registry.add("db.password", container::getPassword);
        registry.add("db.driver", container::getDriverClassName);
    }

    @BeforeAll
    static void runContainer() {
        container.start();
    }

    @Test
    void findAll() {
        List<Product> products = productRepository.findAll();
        Set<User> users = products.get(0).getUsers();
        int actual = products.size();
        int expected = 5;
        int expectedUsersSize = 2;
        int actualUsersSize = users.size();
        assertEquals(expectedUsersSize, actualUsersSize);
        assertEquals(expected, actual);
    }

    @Test
    void findById() {
        Product byId = productRepository.findById(1L).orElseThrow();
        String actualName = "Milk";
        String expectedName = byId.getTitle();
        assertEquals(expectedName, actualName);
    }

    @Test
    void saveProduct() {
        Product product = new Product();
        product.setTitle("Ice cream");
        User user = new User();
        user.setName("Moe");
        product.setUsers(Set.of(user));
        Product saved = productRepository.save(product);
        String expectedName = product.getTitle();
        String actualName = saved.getTitle();
        Set<User> expectedUsers = product.getUsers();
        Set<User> actualUsers = saved.getUsers();
        assertEquals(expectedName, actualName);
        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void updateById() {
        Long id = 1L;
        String expectedName = "Coconut Milk";
        Product product = new Product();
        product.setId(id);
        product.setTitle(expectedName);
        Product save = productRepository.save(product);
        String actualName = save.getTitle();
        assertEquals(expectedName, actualName);
    }

    @Test
    void deleteById() {
        Product product = new Product();
        product.setTitle("Sugar");
        Product saved = productRepository.save(product);
        Long id = saved.getId();
        productRepository.deleteById(id);
        boolean actual = productRepository.findById(id).isPresent();
        assertFalse(actual);
    }

    @Test
    void findListBy_UserId() {
        Long id = 3L;
        List<Product> listByUserId = productRepository.findListByUserId(id);
        int expectedProductsSize = 2;
        int actualProductsSize = listByUserId.size();
        assertEquals(expectedProductsSize, actualProductsSize);
    }

    @Test
    void findProductBy_UserId() {
        Long userId = 3L;
        Long productId = 1L;
        Product product = productRepository.findByUserId(userId, productId).orElseThrow();
        String expectedName = "Milk";
        String actualName = product.getTitle();
        assertEquals(expectedName, actualName);
    }

    @Test
    void findByTitle() {
        String expectedName = "Milk";
        Product product = productRepository.findByTitle(expectedName).orElseThrow();
        String actualName = product.getTitle();
        assertEquals(expectedName, actualName);
    }

}

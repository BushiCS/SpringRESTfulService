package ru.sviridov.spring.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
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
import ru.sviridov.spring.entity.Card;
import ru.sviridov.spring.entity.Product;
import ru.sviridov.spring.entity.User;
import ru.sviridov.spring.repository.UserRepository;

import java.util.List;
import java.util.Set;

@Transactional
@SpringJUnitConfig(DataTestConfig.class)
@Testcontainers
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@TestPropertySource({"classpath:test_db.properties"})
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;


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
        List<User> users = userRepository.findAll();
        int actual = users.size();
        int expected = 5;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findById() {
        User byId = userRepository.findById(1L).orElseThrow();
        String actualName = byId.getName();
        String expectedName = "Bill";
        Assertions.assertEquals(expectedName, actualName);
    }

    @Test
    void saveUserWith_CardsAndProducts() {
        User user = new User();
        user.setName("John");
        Card card = new Card();
        card.setUser(user);
        card.setId(10L);
        card.setTitle("VTB");
        user.setCards(Set.of(card));
        Product product = new Product();
        product.setTitle("Kolbasa");
        product.setUsers(Set.of(user));
        user.setProducts(List.of(product));
        User saved = userRepository.save(user);
        String expected = "John";
        String actual = saved.getName();
        Set<Card> expectedCards = user.getCards();
        Set<Card> actualCards = saved.getCards();
        List<Product> expectedProducts = user.getProducts();
        List<Product> actualProducts = saved.getProducts();
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expectedCards, actualCards);
        Assertions.assertEquals(expectedProducts, actualProducts);
    }

    @Test
    void updateById() {
        Long id = 1L;
        String expectedName = "Carl";
        User user = new User();
        user.setId(id);
        user.setName(expectedName);
        User saved = userRepository.save(user);
        String actualName = saved.getName();
        Assertions.assertEquals(expectedName, actualName);
    }

    @Test
    void deleteById() {
        User user = new User();
        user.setName("Bill");
        User saved = userRepository.save(user);
        Long id = saved.getId();
        userRepository.deleteById(id);
        boolean actual = userRepository.findById(id).isPresent();
        Assertions.assertFalse(actual);
    }

    @Test
    void findAllWith_CardsAndProducts() {
        List<User> users = userRepository.findAllWithCardsAndProducts();
        int expectedSize = 5;
        int actualSize = users.size();
        Assertions.assertEquals(expectedSize, actualSize);
        Set<Card> cards = users.get(0).getCards();
        int expectedUserCardsSize = 2;
        int actualUserCardsSize = cards.size();
        Assertions.assertEquals(expectedUserCardsSize, actualUserCardsSize);
        List<Product> products = users.get(1).getProducts();
        int expectedUserProductsSize = 2;
        int actualUserProductsSize = products.size();
        Assertions.assertEquals(expectedUserProductsSize, actualUserProductsSize);
    }

    @Test
    void findUserWith_CardsAndProducts() {
        Long id = 2L;
        User user = userRepository.findWithCardsAndProducts(id).orElseThrow();
        String expectedName = "Jack";
        String actualName = user.getName();

        List<Product> products = user.getProducts();
        Set<Card> cards = user.getCards();
        int expectedCardsSize = 1;
        int expectedProductsSize = 2;
        int actualCardsSize = cards.size();
        int actualProductsSize = products.size();
        Assertions.assertEquals(expectedName, actualName);
        Assertions.assertEquals(expectedCardsSize, actualCardsSize);
        Assertions.assertEquals(expectedProductsSize, actualProductsSize);
    }

    @Test
    void findUsersBy_ProductId() {
        Long id = 1L;
        List<User> listByProductId = userRepository.findListByProductId(id);
        int expectedSize = 2;
        int actualSize = listByProductId.size();
        Assertions.assertEquals(expectedSize, actualSize);
    }

    @Test
    void findUserBy_ProductId(){
        Long userId = 2L;
        Long productId = 1L;
        User byProductId = userRepository.findByProductId(productId, userId);
        String expectedName = "Jack";
        String actualName = byProductId.getName();
        Assertions.assertEquals(expectedName, actualName);
    }
}

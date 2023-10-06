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
import ru.sviridov.spring.entity.User;
import ru.sviridov.spring.repository.CardRepository;

import java.util.List;

@Transactional
@SpringJUnitConfig(DataTestConfig.class)
@Testcontainers
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@TestPropertySource({"classpath:test_db.properties"})
public class CardRepositoryTest {

    @Autowired
    private CardRepository cardRepository;

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
        List<Card> all = cardRepository.findAll();
        int expectedSize = 4;
        int actualSize = all.size();
        Assertions.assertEquals(expectedSize, actualSize);
    }

    @Test
    void findById() {
        Card card = cardRepository.findById(1L).orElseThrow();
        String expectedName = "VTB";
        String actualName = card.getTitle();
        Assertions.assertEquals(expectedName, actualName);
    }

    @Test
    void save() {
        Card card = new Card();
        User user = new User();
        user.setId(1L);
        user.setName("Anna");
        card.setTitle("SBERBANK");
        card.setUser(user);
        Card saved = cardRepository.save(card);
        String expectedName = card.getTitle();
        String actualName = saved.getTitle();
        User expectedUser = card.getUser();
        User actualUser = saved.getUser();
        Assertions.assertEquals(expectedName, actualName);
        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    void updateById() {
        Long id = 1L;
        Card card = new Card();
        card.setTitle("MyBank");
        card.setId(id);
        Card saved = cardRepository.save(card);
        String expectedName = card.getTitle();
        String actualName = saved.getTitle();
        Assertions.assertEquals(expectedName, actualName);
    }

    @Test
    void findByUser(){
        Long id = 1L;
        List<Card> cards = cardRepository.findByUserId(id);
        int expectedSize = 2;
        int actualSize = cards.size();
        Assertions.assertEquals(expectedSize, actualSize);
    }

    @Test
    void findCardByUser_Id(){
        Long userId = 1L;
        Long cardId = 1L;
        Card card = cardRepository.findCardByUserId(userId, cardId).orElseThrow();
        String actualName = card.getTitle();
        String expectedName = "VTB";
        Assertions.assertEquals(expectedName, actualName);
    }

    @Test
    void findByTitle(){
        String expectedName = "VTB";
        Card card = cardRepository.findByTitle(expectedName).orElseThrow();
        String actualName = card.getTitle();
        Assertions.assertEquals(expectedName, actualName);
    }
}

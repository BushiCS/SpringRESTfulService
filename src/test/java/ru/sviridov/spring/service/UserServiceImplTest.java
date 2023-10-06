package ru.sviridov.spring.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sviridov.spring.dto.CardDto;
import ru.sviridov.spring.dto.ProductDto;
import ru.sviridov.spring.dto.UserDto;
import ru.sviridov.spring.dto.UserWithCardsAndProductsDto;
import ru.sviridov.spring.entity.Card;
import ru.sviridov.spring.entity.Product;
import ru.sviridov.spring.entity.User;
import ru.sviridov.spring.mapper.CardMapper;
import ru.sviridov.spring.mapper.ProductMapper;
import ru.sviridov.spring.mapper.UserMapper;
import ru.sviridov.spring.repository.CardRepository;
import ru.sviridov.spring.repository.ProductRepository;
import ru.sviridov.spring.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    CardRepository cardRepository;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    UserServiceImpl userService;
    @Captor
    ArgumentCaptor<User> userCaptor;

    @Test
    void getAll() {
        User user1 = new User();
        User user2 = new User();
        List<User> users = new ArrayList<>(List.of(user1, user2));

        UserWithCardsAndProductsDto userDto1 = new UserWithCardsAndProductsDto();
        UserWithCardsAndProductsDto userDto2 = new UserWithCardsAndProductsDto();
        List<UserWithCardsAndProductsDto> usersDto = new ArrayList<>(List.of(userDto1, userDto2));

        when(userRepository.findAllWithCardsAndProducts()).thenReturn(users);
        List<UserWithCardsAndProductsDto> actualUsersDto = userService.getAll();

        int actualSize = actualUsersDto.size();
        int expectedSize = usersDto.size();

        assertEquals(expectedSize, actualSize);
        verify(userRepository).findAllWithCardsAndProducts();

    }

    @Test
    void getById() {
        long id = 1L;
        Product product1 = new Product();
        product1.setTitle("Sausage");
        Product product2 = new Product();
        product2.setTitle("Cheese");

        Card card1 = new Card();
        card1.setTitle("VTB");
        Card card2 = new Card();
        card2.setTitle("SBER");

        User user = new User();
        user.setId(id);
        user.setName("Jack");
        user.setProducts(List.of(product1, product2));
        user.setCards(Set.of(card1, card2));

        ProductDto productDto1 = ProductMapper.INSTANCE.toDto(product1);
        ProductDto productDto2 = ProductMapper.INSTANCE.toDto(product2);

        CardDto cardDto1 = CardMapper.INSTANCE.toDto(card1);
        CardDto cardDto2 = CardMapper.INSTANCE.toDto(card2);

        UserWithCardsAndProductsDto userDto = new UserWithCardsAndProductsDto();
        userDto.setCards(List.of(cardDto1, cardDto2));
        userDto.setProducts(List.of(productDto1, productDto2));
        when(userRepository.findWithCardsAndProducts(id)).thenReturn(Optional.of(user));
        UserWithCardsAndProductsDto actualDtoUser = userService.getById(id);

        int expectedProductsSize = user.getProducts().size();
        int actualProductsSize = actualDtoUser.getProducts().size();
        int expectedCardSize = user.getCards().size();
        int actualCardSize = actualDtoUser.getCards().size();
        String expectedName = user.getName();
        String actualName = actualDtoUser.getName();

        assertEquals(expectedProductsSize, actualProductsSize);
        assertEquals(expectedCardSize, actualCardSize);
        assertEquals(expectedName, actualName);
        verify(userRepository).findWithCardsAndProducts(id);
    }

    @Test
    void testGetById_ThrowsNoSuchElementException() {
        Long id = 1L;
        when(userRepository.findWithCardsAndProducts(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, ()  -> userService.getById(id));
    }

    @Test
    void saveOrUpdate() {
        UserDto userDto = new UserDto();
        userDto.setName("Bill");
        User user = new User();
        user.setId(1L);
        user.setName("Bill");

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto savedUserDto = userService.saveOrUpdate(userDto);

        String expectedName = userDto.getName();
        String actualName = savedUserDto.getName();

        assertEquals(expectedName, actualName);
    }

    @Test
    void testUpdateById_existingUser() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setName("Bill");
        UserDto userDto = new UserDto();
        userDto.setName("John");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto savedDto = userService.updateById(id, userDto);
        String expectedName = userDto.getName();
        String actualName = savedDto.getName();

        assertEquals(expectedName, actualName);
        verify(userRepository).findById(id);
        verify(userRepository).save(user);
    }

    @Test
    void testUpdateById_newUser() {

        Long id = 1L;
        UserDto userDto = new UserDto();
        userDto.setName("Bill");
        User user = UserMapper.INSTANCE.toEntity(userDto);
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto savedUserDto = userService.updateById(id, userDto);
        String expectedName = userDto.getName();
        String actualName = savedUserDto.getName();
        assertEquals(expectedName, actualName);
        verify(userRepository).findById(id);
        verify(userRepository).save(userCaptor.capture());

        User updatedUser = userCaptor.getValue();
        assertEquals(id, updatedUser.getId());
        assertEquals(expectedName, updatedUser.getName());
    }

    @Test
    void deleteById() {
        Long id = 1L;
        userService.deleteById(id);
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void addCardToUser() {
        Long id = 1L;

        CardDto cardDto = new CardDto();
        cardDto.setTitle("VTB");
        Card card = CardMapper.INSTANCE.toEntity(cardDto);

        User user = new User();
        user.setId(id);

        when(cardRepository.findByTitle(card.getTitle())).thenReturn(Optional.of(card));
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        CardDto savedCardDto = userService.addCardToUser(id, cardDto);
        String expectedTitle = cardDto.getTitle();
        String actualTitle = savedCardDto.getTitle();

        assertNotNull(savedCardDto);
        assertEquals(expectedTitle, actualTitle);
        assertEquals(user, card.getUser());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void addProductToUser() {
        Long id = 1L;
        ProductDto productDto = new ProductDto();
        productDto.setTitle("Sausage");

        Product product = ProductMapper.INSTANCE.toEntity(productDto);
        User user = new User();
        user.setId(id);
        user.setProducts(new ArrayList<>());
        product.setUsers(Set.of(user));
        when(productRepository.findByTitle(product.getTitle())).thenReturn(Optional.of(product));
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        ProductDto savedProductDto = userService.addProductToUser(id, productDto);

        String expectedTitle = productDto.getTitle();
        String actualTitle = savedProductDto.getTitle();
        assertNotNull(savedProductDto);
        assertEquals(expectedTitle, actualTitle);
        assertEquals(Set.of(user), product.getUsers());
        assertEquals(product.getTitle(), user.getProducts().get(0).getTitle());

        verify(productRepository, times(1)).findByTitle(productDto.getTitle());
        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).save(user);
    }


}

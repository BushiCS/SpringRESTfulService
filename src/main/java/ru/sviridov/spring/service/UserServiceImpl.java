package ru.sviridov.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final ProductRepository productRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           CardRepository cardRepository,
                           ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<UserWithCardsAndProductsDto> getAll() {
        List<User> users = userRepository.findAllWithCardsAndProducts();
        return UserMapper.INSTANCE.toDtoWithCardsAndProducts(users);
    }

    @Override
    public UserWithCardsAndProductsDto getById(Long id) {
        User user = userRepository.findWithCardsAndProducts(id).orElseThrow();
        return UserMapper.INSTANCE.toDtoWithCardAndProduct(user);
    }

    @Override
    public UserDto saveOrUpdate(UserDto userDto) {
        User entity = UserMapper.INSTANCE.toEntity(userDto);
        User saved = userRepository.save(entity);
        return UserMapper.INSTANCE.toDto(saved);
    }

    @Override
    public UserDto updateById(Long id, UserDto userDto) {
        User usertoUpdate = UserMapper.INSTANCE.toEntity(userDto);
        return UserMapper.INSTANCE.toDto(userRepository.findById(id).map(user -> {
            user.setName(usertoUpdate.getName());
            return userRepository.save(user);
        }).orElseGet(() -> {
            usertoUpdate.setId(id);
            return userRepository.save(usertoUpdate);
        }));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public CardDto addCardToUser(Long userId, CardDto cardDto) {
        Card card = cardRepository.findByTitle(cardDto.getTitle()).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        card.setUser(user);
        user.getCards().add(card);
        userRepository.save(user);
        return CardMapper.INSTANCE.toDto(card);
    }

    @Override
    public ProductDto addProductToUser(Long id, ProductDto productDto) {
        Product product = productRepository.findByTitle(productDto.getTitle()).orElseThrow();
        User user = userRepository.findById(id).orElseThrow();
        user.getProducts().add(product);
        userRepository.save(user);
        return ProductMapper.INSTANCE.toDto(product);
    }
}

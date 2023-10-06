package ru.sviridov.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sviridov.spring.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT distinct u from User u left join fetch u.products left join fetch u.cards")
    List<User> findAllWithCardsAndProducts();

    @Query("SELECT u from User u left join fetch u.products left join fetch u.cards where  u.id = ?1")
    Optional<User> findWithCardsAndProducts(Long id);

    @Query("select u from User u join u.products up where up.id = ?1 ")
    List<User> findListByProductId(Long id);

    @Query("select u from User u join u.products up where up.id = ?1 and u.id = ?2")
    User findByProductId(Long productId, Long userId);
}

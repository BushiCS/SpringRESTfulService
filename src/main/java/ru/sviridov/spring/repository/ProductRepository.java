package ru.sviridov.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sviridov.spring.entity.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p join p.users up where up.id = ?1")
    List<Product> findListByUserId(Long id);

    @Query("select p from Product p join p.users up where  up.id = ?1 and p.id = ?2")
    Optional<Product> findByUserId(Long userId, Long productId);

    Optional<Product> findByTitle(String title);
}
package ru.egor.tgBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.egor.tgBot.entity.Product;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "products", path = "products")
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p from Product p where p.category.id = :categoryId")
    List<Product> findAllByCategoryName(@Param("categoryId")Long categoryId);

    List<Product> findByNameContainingIgnoreCase(String name);
}

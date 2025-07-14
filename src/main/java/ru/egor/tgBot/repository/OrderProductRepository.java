package ru.egor.tgBot.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.egor.tgBot.entity.OrderProduct;
import ru.egor.tgBot.entity.Product;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "order-products",path = "order-products")
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    @Query("SELECT op.product FROM OrderProduct op " +
            "JOIN op.clientOrder co " +
            "JOIN co.client c " +
            "WHERE c.id = :clientId")
    List<Product> findAllProductsByClientId(@Param("clientId") Long clientId);

    @Query("SELECT op.product, SUM(op.countProduct) as totalOrdered " +
            "FROM OrderProduct op " +
            "GROUP BY op.product " +
            "ORDER BY totalOrdered DESC")
    List<Object[]> findTopPopularProducts(Pageable pageable);
}

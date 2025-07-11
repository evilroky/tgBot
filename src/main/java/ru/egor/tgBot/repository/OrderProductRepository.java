package ru.egor.tgBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.egor.tgBot.entity.OrderProduct;

@RepositoryRestResource(collectionResourceRel = "order-products",path = "order-products")
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

}

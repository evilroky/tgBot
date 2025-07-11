package ru.egor.tgBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.egor.tgBot.entity.ClientOrder;

@RepositoryRestResource(collectionResourceRel = "client-orders",path = "client-orders")
public interface ClientOrderRepository extends JpaRepository<ClientOrder, Long> {

}

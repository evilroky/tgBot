package ru.egor.tgBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.egor.tgBot.entity.ClientOrder;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "client-orders",path = "client-orders")
public interface ClientOrderRepository extends JpaRepository<ClientOrder, Long> {

    @Query("select c from ClientOrder c where c.client.id = :clientId")
    List<ClientOrder> findByClientId(@Param("clientId")Long clientId);

    @Query("select c from ClientOrder c where c.client.id = :clientId and c.status = 1")
    ClientOrder getClientOrderByClientId(@Param("clientId")Long clientId);

}

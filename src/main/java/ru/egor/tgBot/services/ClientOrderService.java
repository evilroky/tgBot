package ru.egor.tgBot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import ru.egor.tgBot.entity.Client;
import ru.egor.tgBot.entity.ClientOrder;
import ru.egor.tgBot.repository.ClientOrderRepository;

import java.util.List;

@Service
public class ClientOrderService {
    @Autowired
    private ClientOrderRepository clientOrderRepository;

    public List<ClientOrder> findByClientId(Long clientId) {
        return clientOrderRepository.findByClientId(clientId);
    }

    public void saveNewClientOrder(Client client, Integer status, Double total) {
        ClientOrder clientOrder = new ClientOrder();
        clientOrder.setClient(client);
        clientOrder.setStatus(status);
        clientOrder.setTotal(total);
        clientOrderRepository.save(clientOrder);
    }

    public ClientOrder getClientOrder(Long id){
        return clientOrderRepository.getClientOrderByClientId(id);
    }

    public void updateClientOrder(ClientOrder clientOrder, Double total){
        ClientOrder clientOrder1 = clientOrder;
        clientOrder1.setStatus(2);
        clientOrder1.setStatus(clientOrder.getStatus());
        clientOrder1.setTotal(total);
        clientOrderRepository.save(clientOrder);
    }
}

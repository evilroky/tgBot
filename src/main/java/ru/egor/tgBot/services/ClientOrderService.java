package ru.egor.tgBot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
}

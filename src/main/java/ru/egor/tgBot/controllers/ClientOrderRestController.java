package ru.egor.tgBot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.egor.tgBot.entity.ClientOrder;
import ru.egor.tgBot.services.ClientOrderService;

import java.util.List;

@RestController
public class ClientOrderRestController {
    @Autowired
    private ClientOrderService clientOrderService;

    @GetMapping("/rest/clients/{id}/orders")
    public List<ClientOrder> getClientOrdersById(@PathVariable Long id) {
        return clientOrderService.findByClientId(id);
    }
}

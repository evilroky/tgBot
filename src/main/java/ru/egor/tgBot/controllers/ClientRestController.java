package ru.egor.tgBot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.egor.tgBot.entity.Client;
import ru.egor.tgBot.services.ClientService;

import java.util.List;

@RestController
public class ClientRestController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/rest/clients/search")
    public List<Client> findByFullNameContainingIgnoreCase(@RequestParam String name) {
        return clientService.findByFullNameContainingIgnoreCase(name);
    }
}

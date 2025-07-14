package ru.egor.tgBot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.egor.tgBot.entity.Client;
import ru.egor.tgBot.repository.ClientRepository;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> findByFullNameContainingIgnoreCase(String fullName) {
        return clientRepository.findByFullNameContainingIgnoreCase(fullName);
    }
}

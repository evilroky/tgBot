package ru.egor.tgBot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.egor.tgBot.entity.Product;
import ru.egor.tgBot.repository.OrderProductRepository;

import java.util.List;

@Service
public class OrderProductService {

    @Autowired
    private OrderProductRepository orderProductRepository;

    public List<Product> findAllProductsByClientId(Long clientId) {
        return orderProductRepository.findAllProductsByClientId(clientId);
    }

    public List<Product> getTopPopularProducts(int limit) {
        return orderProductRepository.findTopPopularProducts(limit);
    }
}

package ru.egor.tgBot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.egor.tgBot.entity.Product;
import ru.egor.tgBot.repository.OrderProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderProductService {

    @Autowired
    private OrderProductRepository orderProductRepository;

    public List<Product> findAllProductsByClientId(Long clientId) {
        return orderProductRepository.findAllProductsByClientId(clientId);
    }

    public List<Product> getTopPopularProducts(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return orderProductRepository.findTopPopularProducts(pageable)
                .stream()
                .map(arr -> (Product) arr[0])
                .collect(Collectors.toList());
    }
}

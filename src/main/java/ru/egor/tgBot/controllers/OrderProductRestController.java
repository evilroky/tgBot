package ru.egor.tgBot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.egor.tgBot.entity.Product;
import ru.egor.tgBot.services.OrderProductService;

import java.util.List;

@RestController
public class OrderProductRestController {

    @Autowired
    private OrderProductService orderProductService;

    @GetMapping("/rest/clients/{id}/products")
    public List<Product> findAllProductsByClientId(@PathVariable Long id) {
        return orderProductService.findAllProductsByClientId(id);
    }

    @GetMapping("/rest/products/popular")
    public List<Product> getTopPopularProducts(@RequestParam int limit){
        return orderProductService.getTopPopularProducts(limit);
    }
}

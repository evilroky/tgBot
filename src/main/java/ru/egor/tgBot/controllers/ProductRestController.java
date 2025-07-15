package ru.egor.tgBot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.egor.tgBot.entity.Product;
import ru.egor.tgBot.services.OrderProductService;
import ru.egor.tgBot.services.ProductService;

import java.util.List;

@RestController
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderProductService orderProductService;

    @GetMapping("/rest/products/search")
    public List<Product> searchProduct(@RequestParam(required = false) Long categoryId,
                                       @RequestParam(required = false) String name) {
        if (name != null) {
            return productService.findByNameContainingIgnoreCase(name);
        } else
            return productService.getProductsByCategoryName(categoryId);

    }

    @GetMapping("/rest/clients/{id}/products")
    public List<Product> findAllProductsByClientId(@PathVariable Long id) {
        return orderProductService.findAllProductsByClientId(id);
    }

    @GetMapping("/rest/products/popular")
    public List<Product> getTopPopularProducts(@RequestParam int limit){
        return orderProductService.getTopPopularProducts(limit);
    }

}

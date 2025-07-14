package ru.egor.tgBot.controllers;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.egor.tgBot.entity.Product;
import ru.egor.tgBot.services.ProductService;

import java.util.List;

@RestController
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @GetMapping("/rest/products/search")
    public List<Product> searchProduct(@RequestParam(required = false) Long categoryId,
                                       @RequestParam(required = false) String name) {
        if (name != null) {
            return productService.findByNameContainingIgnoreCase(name);
        } else if (categoryId != null) {
            return productService.getProductsByCategoryName(categoryId);
        }
        return null;
    }

}

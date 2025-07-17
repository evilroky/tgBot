package ru.egor.tgBot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.egor.tgBot.entity.ClientOrder;
import ru.egor.tgBot.entity.OrderProduct;
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

    public void addProductToOrder(ClientOrder clientOrder, Product product, long count) {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setClientOrder(clientOrder);
        orderProduct.setProduct(product);
        orderProduct.setCountProduct(count);

        orderProductRepository.save(orderProduct);
    }

    public List<OrderProduct> findOrderProductsByClientOrderId(Long clientOrderId){
        return orderProductRepository.findOrderProductsByClientOrderId(clientOrderId);
    }
}

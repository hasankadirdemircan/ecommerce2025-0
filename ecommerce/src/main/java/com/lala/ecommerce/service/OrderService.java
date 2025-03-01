package com.lala.ecommerce.service;

import com.lala.ecommerce.dto.OrderProductInfo;
import com.lala.ecommerce.dto.OrderRequest;
import com.lala.ecommerce.exception.InsufficientProductUnitException;
import com.lala.ecommerce.exception.ProductNotFoundException;
import com.lala.ecommerce.model.Order;
import com.lala.ecommerce.model.Product;
import com.lala.ecommerce.repository.OrderRepository;
import com.lala.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final JavaMailSender javaMailSender;


    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public Boolean doOrder(OrderRequest orderRequest) {
        SimpleMailMessage mailMessage
                = new SimpleMailMessage();

        // Setting up necessary details
        mailMessage.setFrom("emailecommerce005@gmail.com");
        mailMessage.setTo("hkdemircan06@gmail.com");
        mailMessage.setText("test");
        mailMessage.setSubject("est");

        // Sending the mail
        javaMailSender.send(mailMessage);
        productUnitStockCheck(orderRequest.getOrderList());
        List<Double> orderTotalCostList = new ArrayList<>();
        orderRequest.getOrderList().forEach(orderRequestInfo -> {
            Order order = new Order();
            Product product = productRepository.findById(orderRequestInfo.getProductId()).orElseThrow(() -> new ProductNotFoundException("product not found, id : " + orderRequestInfo.getProductId()));
            Double totalPrice = product.getPrice() * orderRequestInfo.getQuantity();
            orderTotalCostList.add(totalPrice);

            order.setQuantity(orderRequestInfo.getQuantity());
            order.setProductId(orderRequestInfo.getProductId());
            order.setCustomerId(orderRequest.getCustomerId());
            order.setPrice(product.getPrice());
            order.setPurchaseDate(LocalDateTime.parse(formatter.format(LocalDateTime.now()), formatter));
            order.setTotalPrice(totalPrice);

            int remainedUnitInStock = product.getUnitsInStock() - orderRequestInfo.getQuantity();
            if(remainedUnitInStock == 0) {
                product.setActive(false);
            }
            product.setUnitsInStock(remainedUnitInStock);

            orderRepository.save(order);
            productRepository.save(product);
        });

        //mail entegrasyonu yap

        Double orderTotalCost = orderTotalCostList.stream().mapToDouble(Double::doubleValue).sum();
        return true;
    }

    private void productUnitStockCheck(List<OrderProductInfo> orderList) {
        orderList.forEach(productInfo -> {
            Product product = productRepository.findById(productInfo.getProductId()).orElseThrow(() -> new ProductNotFoundException("product not found, id : " + productInfo.getProductId()));

            if(product.getUnitsInStock() - productInfo.getQuantity() < 0) {
                throw new InsufficientProductUnitException("the product stock unsufficient, productName : " + product.getName() + " productId : " + product.getId());
            }
        });
    }

    public List<Order> getCustomerOrderHistory(Long id) {
        return orderRepository.getCustomerOrderHistory(id);
    }
}

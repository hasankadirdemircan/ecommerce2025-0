package com.lala.ecommerce.service.impl;

import com.lala.ecommerce.dto.OrderProductInfo;
import com.lala.ecommerce.dto.OrderRequest;
import com.lala.ecommerce.exception.CustomerNotFoundException;
import com.lala.ecommerce.exception.InsufficientProductUnitException;
import com.lala.ecommerce.exception.ProductNotFoundException;
import com.lala.ecommerce.model.Customer;
import com.lala.ecommerce.model.Order;
import com.lala.ecommerce.model.Product;
import com.lala.ecommerce.repository.CustomerRepository;
import com.lala.ecommerce.repository.OrderRepository;
import com.lala.ecommerce.repository.ProductRepository;
import com.lala.ecommerce.service.OrderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final JavaMailSender javaMailSender;
    private final CustomerRepository customerRepository;


    @Value("${spring.mail.username}")
    private String emailFrom;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @Override
    public Boolean doOrder(OrderRequest orderRequest) {

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
        Customer customer = customerRepository.findById(orderRequest.getCustomerId()).orElseThrow(() -> new CustomerNotFoundException(orderRequest.getCustomerId() + " customer not found"));

        Double orderTotalCost = orderTotalCostList.stream().mapToDouble(Double::doubleValue).sum();
      //  sendMail(customer.getEmail(), customer.getFirstName(), orderTotalCost);
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

    @Override
    public List<Order> getCustomerOrderHistory(Long id) {
        return orderRepository.getCustomerOrderHistory(id);
    }

    private void sendMail(String emailTo, String firstName, double totalCost) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom(emailFrom, "Lala");
            helper.setTo(emailTo);
            helper.setSubject("Hello " + firstName + " Your Order is in progress");
            String content = "<p>" + "Hello " + firstName + "</p><p>The total cost is "+ totalCost + "</p>";

            helper.setText(content, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

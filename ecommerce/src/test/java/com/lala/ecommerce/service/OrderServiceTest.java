package com.lala.ecommerce.service;

import com.lala.ecommerce.dto.OrderRequest;
import com.lala.ecommerce.exception.InsufficientProductUnitException;
import com.lala.ecommerce.helper.CustomerDoFactory;
import com.lala.ecommerce.helper.OrderDoFactory;
import com.lala.ecommerce.helper.OrderDtoFactory;
import com.lala.ecommerce.helper.ProductDoFactory;
import com.lala.ecommerce.model.Customer;
import com.lala.ecommerce.model.Order;
import com.lala.ecommerce.model.Product;
import com.lala.ecommerce.repository.CustomerRepository;
import com.lala.ecommerce.repository.OrderRepository;
import com.lala.ecommerce.repository.ProductRepository;
import com.lala.ecommerce.service.impl.OrderServiceImpl;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private  ProductRepository productRepository;

    @Mock
    private  OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;


    @Mock
    private  JavaMailSender javaMailSender;

    @Mock
    private MimeMessageHelper mimeMessageHelper;

    @Mock
    private MimeMessage mimeMessage;

    private OrderDtoFactory orderDtoFactory;

    private ProductDoFactory productDoFactory;

    private CustomerDoFactory customerDoFactory;

    private OrderDoFactory orderDoFactory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.orderDtoFactory = new OrderDtoFactory();
        this.productDoFactory = new ProductDoFactory();
        this.customerDoFactory = new CustomerDoFactory();
        this.orderDoFactory = new OrderDoFactory();
    }

    @Test
    void shouldThrowInsufficientProductUnitException_failed() {
        //given
        OrderRequest orderRequest = orderDtoFactory.generateOrderRequest();
        orderRequest.getOrderList().get(0).setQuantity(7);
        Product product = productDoFactory.generateProduct(3L, 1L);

        //when
        when(productRepository.findById(orderRequest.getOrderList().get(0).getProductId())).thenReturn(Optional.of(product));

        //then
        InsufficientProductUnitException thrown = Assertions.assertThrows(InsufficientProductUnitException.class,
                () -> orderService.doOrder(orderRequest));

        //assert
        assertEquals("the product stock unsufficient, productName : " + product.getName() + " productId : " + product.getId(), thrown.getMessage());

    }

    @Test
    void shouldDoOrder_success() {
        //given
        OrderRequest orderRequest = orderDtoFactory.generateOrderRequest();
        Product product1 = productDoFactory.generateProduct(3L, 1L);
        Product product2 = productDoFactory.generateProduct(3L, 2L);
        mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        ReflectionTestUtils.setField(orderService, "emailFrom", "test");
        Customer customer = customerDoFactory.generateCustomerById(orderRequest.getCustomerId(), "ROLE_USER");

        //when
        when(productRepository.findById(orderRequest.getOrderList().get(0).getProductId())).thenReturn(Optional.of(product1));
        when(productRepository.findById(orderRequest.getOrderList().get(1).getProductId())).thenReturn(Optional.of(product2));
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(customerRepository.findById(orderRequest.getCustomerId())).thenReturn(Optional.of(customer));

        //then
        Boolean response = orderService.doOrder(orderRequest);

        //assert
        assertTrue(response);
        verify(productRepository, times(2)).findById(orderRequest.getOrderList().get(0).getProductId());
        verify(productRepository, times(2)).findById(orderRequest.getOrderList().get(1).getProductId());
        verify(javaMailSender, times(1)).send(mimeMessage);
        verify(productRepository, times(2)).save(any());
    }

    @Test
    void shouldThrowMessageException_failed() {
        //given
        OrderRequest orderRequest = orderDtoFactory.generateOrderRequest();
        Product product1 = productDoFactory.generateProduct(3L, 1L);
        Product product2 = productDoFactory.generateProduct(3L, 2L);
        mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        ReflectionTestUtils.setField(orderService, "emailFrom", "test");
        Customer customer = customerDoFactory.generateCustomerById(orderRequest.getCustomerId(), "ROLE_USER");

        //when
        when(productRepository.findById(orderRequest.getOrderList().get(0).getProductId())).thenReturn(Optional.of(product1));
        when(productRepository.findById(orderRequest.getOrderList().get(1).getProductId())).thenReturn(Optional.of(product2));
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(customerRepository.findById(orderRequest.getCustomerId())).thenReturn(Optional.of(customer));
        Mockito.doThrow(new RuntimeException()).when(javaMailSender).send((MimeMessage) any());

        //then
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> orderService.doOrder(orderRequest));


        //assert
        assertEquals(RuntimeException.class, thrown.getClass());
        verify(productRepository, times(2)).findById(orderRequest.getOrderList().get(0).getProductId());
        verify(productRepository, times(2)).findById(orderRequest.getOrderList().get(1).getProductId());
        verify(javaMailSender, times(1)).send(mimeMessage);
        verify(productRepository, times(2)).save(any());
    }

    @Test
    void shouldGetCustomerOrderHistory_success() {
        //given
        Long customerId = 1L;
        List<Order> orderList = orderDoFactory.generateOrderList();

        //when
        when(orderRepository.getCustomerOrderHistory(customerId)).thenReturn(orderList);

        //then
        List<Order> response = orderService.getCustomerOrderHistory(customerId);

        //assert
        assertEquals(orderList, response);
        verify(orderRepository, times(1)).getCustomerOrderHistory(customerId);
    }
}

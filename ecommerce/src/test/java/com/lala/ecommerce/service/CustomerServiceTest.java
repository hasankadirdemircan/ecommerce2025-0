package com.lala.ecommerce.service;

import com.lala.ecommerce.exception.CustomerAlreadyExistsException;
import com.lala.ecommerce.helper.CustomerDoFactory;
import com.lala.ecommerce.helper.CustomerDtoFactory;
import com.lala.ecommerce.mapper.CustomerMapper;
import com.lala.ecommerce.model.Customer;
import com.lala.ecommerce.repository.CustomerRepository;
import com.lala.ecommerce.service.impl.CustomerServiceImpl;
import com.lala.ecommerce.service.impl.JwtServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtServiceImpl jwtServiceImpl;

    private CustomerDtoFactory customerDtoFactory;
    private CustomerDoFactory customerDoFactory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.customerDtoFactory = new CustomerDtoFactory();
        this.customerDoFactory = new CustomerDoFactory();
    }

    @Test
    void createCustomer_shouldThrowCustomerAlreadyExistsException() {
        //given
        Customer customer = customerDoFactory.generateCustomerById(1L);

        //when
        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));

        //then
        CustomerAlreadyExistsException thrown = Assertions.assertThrows(CustomerAlreadyExistsException.class,
                () -> customerService.createCustomer(customer));

        //assert
        assertEquals(customer.getEmail() + " is already exists, please use another email", thrown.getMessage());
        verify(customerRepository, times(1)).findByEmail(customer.getEmail());
        verify(customerRepository, times(0)).save(customer);
        verify(passwordEncoder, times(0)).encode(customer.getPassword());
        verify(customerMapper, times(0)).customerToCustomerDto(customer);
    }
}

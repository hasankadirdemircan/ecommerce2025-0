package com.lala.ecommerce.service;

import com.lala.ecommerce.helper.CustomerDoFactory;
import com.lala.ecommerce.model.Customer;
import com.lala.ecommerce.repository.CustomerRepository;
import com.lala.ecommerce.service.impl.UserInfoDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserInfoDetailsServiceTest {

    @InjectMocks
    private UserInfoDetailsService userInfoDetailsService;

    @Mock
    private CustomerRepository customerRepository;

    private CustomerDoFactory customerDoFactory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.customerDoFactory = new CustomerDoFactory();

    }

    @Test
    void shouldReturnUserDetails_success() {
        //given
        Customer customer = customerDoFactory.generateCustomerById(1L, "ROLE_USER");

        //when
        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));

        //then
        UserDetails response = userInfoDetailsService.loadUserByUsername(customer.getEmail());

        //assert
        assertEquals(customer.getEmail(), response.getUsername());
        verify(customerRepository, times(1)).findByEmail(customer.getEmail());
    }
}

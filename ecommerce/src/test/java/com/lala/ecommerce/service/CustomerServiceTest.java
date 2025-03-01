package com.lala.ecommerce.service;

import com.lala.ecommerce.helper.CategoryDOFactory;
import com.lala.ecommerce.mapper.CustomerMapper;
import com.lala.ecommerce.repository.CustomerRepository;
import com.lala.ecommerce.service.impl.CustomerServiceImpl;
import com.lala.ecommerce.service.impl.JwtServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
       //this.categoryDOFactory = new CategoryDOFactory();
    }

}

package com.lala.ecommerce.service;

import com.lala.ecommerce.dto.AuthDto;
import com.lala.ecommerce.dto.CustomerDto;
import com.lala.ecommerce.dto.LoginDto;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Mock
    private Authentication authentication;

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
        Customer customer = customerDoFactory.generateCustomerById(1L, "ROLE_USER");

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

    @Test
    void createCustomer_successWithAdminRole() {
        //given
        Customer customer = customerDoFactory.generateCustomerById(1L, "ROLE_ADMIN");
        String originalPassw = customer.getPassword();
        CustomerDto customerDto = customerDtoFactory.generateCustomerDtoById(1L, "ROLE_ADMIN");

        //when
        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(customer.getPassword())).thenReturn("123");
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.customerToCustomerDto(any(Customer.class))).thenReturn(customerDto);

        //then
        CustomerDto response = customerService.createCustomer(customer);

        //assert
        assertEquals(customerDto, response);
        verify(customerRepository, times(1)).findByEmail(customer.getEmail());
        verify(passwordEncoder, times(1)).encode(originalPassw);
        verify(customerRepository, times(1)).save(any(Customer.class));
        verify(customerMapper, times(1)).customerToCustomerDto(any(Customer.class));
    }

    @Test
    void login_success() {
        //given
        AuthDto authDto = customerDtoFactory.generateAuthDto();
        LoginDto loginDto = customerDtoFactory.generateLoginDto();

        //when
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(jwtServiceImpl.generateToken(authentication)).thenReturn(loginDto);

        //then
        LoginDto response = customerService.login(authDto);

        //assert
        assertEquals(loginDto, response);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtServiceImpl, times(1)).generateToken(authentication);
    }

    @Test
    void login_shouldThrowUsernameNotFoundException() {
        //given
        AuthDto authDto = customerDtoFactory.generateAuthDto();

        //when
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        //then
        UsernameNotFoundException thrown = Assertions.assertThrows(UsernameNotFoundException.class,
                () -> customerService.login(authDto));

        //assert
        assertEquals("Invalid user details.", thrown.getMessage());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtServiceImpl, times(0)).generateToken(authentication);
    }
}

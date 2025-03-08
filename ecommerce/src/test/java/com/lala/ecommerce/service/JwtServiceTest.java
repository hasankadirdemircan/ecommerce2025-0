package com.lala.ecommerce.service;

import com.lala.ecommerce.config.UserInfoDetails;
import com.lala.ecommerce.dto.LoginDto;
import com.lala.ecommerce.helper.CustomerDoFactory;
import com.lala.ecommerce.helper.CustomerDtoFactory;
import com.lala.ecommerce.model.Customer;
import com.lala.ecommerce.repository.CustomerRepository;
import com.lala.ecommerce.service.impl.JwtServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class JwtServiceTest {

    @InjectMocks
    private JwtServiceImpl jwtService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private Authentication authentication;

    private CustomerDoFactory customerDoFactory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.customerDoFactory = new CustomerDoFactory();
    }

    @Test
    void validateToken_expiredTrue() {
        //given
        Customer customer = customerDoFactory.generateCustomerById(1L, "ROLE_USER");
        UserInfoDetails userInfoDetails = Optional.of(customer).map(UserInfoDetails::new).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        Collection<SimpleGrantedAuthority> authCollection = Collections.singleton(simpleGrantedAuthority);


        //when
        //any()
        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));
        when(authentication.getAuthorities()).thenReturn((Collection)authCollection);
        when(authentication.getName()).thenReturn(customer.getEmail());

        //then
        LoginDto response = jwtService.generateToken(authentication);
        System.out.println();

        boolean isValidateResponse = jwtService.validateToken(response.getToken(), userInfoDetails);

        assertEquals(true, isValidateResponse);
        verify(authentication, times(1)).getAuthorities();
    }
}

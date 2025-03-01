package com.lala.ecommerce.service.impl;

import com.lala.ecommerce.config.UserInfoDetails;
import com.lala.ecommerce.model.Customer;
import com.lala.ecommerce.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInfoDetailsService implements UserDetailsService {

    @Autowired
    private  CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> customerInfo = customerRepository.findByEmail(username);
        return customerInfo.map(UserInfoDetails::new).orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }
}

package com.lala.ecommerce.service;

import com.lala.ecommerce.dto.AuthDto;
import com.lala.ecommerce.dto.LoginDto;
import com.lala.ecommerce.enums.RoleEnum;
import com.lala.ecommerce.dto.CustomerDto;
import com.lala.ecommerce.exception.CustomerAlreadyExistsException;
import com.lala.ecommerce.mapper.CustomerMapper;
import com.lala.ecommerce.model.Customer;
import com.lala.ecommerce.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public CustomerDto createCustomer(Customer customer) {
        Optional<Customer> customerOptional = customerRepository.findByEmail(customer.getEmail());
        if(customerOptional.isPresent()) {
            throw new CustomerAlreadyExistsException(customer.getEmail() + " is already exists, please use another email");
        }

        if(Objects.isNull(customer.getRoles())) {
            customer.setRoles(RoleEnum.ROLE_USER.name());
        }
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));

        return customerMapper.customerToCustomerDto(customerRepository.save(customer));

    }

    public LoginDto login(AuthDto authDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDto.getEmail(), authDto.getPassword()));
        if(authentication.isAuthenticated()) {
            return jwtService.generateToken(authentication);
        }

        throw new UsernameNotFoundException("Invalid user details.");
    }
}

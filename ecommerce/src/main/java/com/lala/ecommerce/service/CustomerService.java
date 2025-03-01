package com.lala.ecommerce.service;

import com.lala.ecommerce.dto.AuthDto;
import com.lala.ecommerce.dto.CustomerDto;
import com.lala.ecommerce.dto.LoginDto;
import com.lala.ecommerce.model.Customer;

public interface CustomerService {
    CustomerDto createCustomer(Customer customer);
    LoginDto login(AuthDto authDto);
}

package com.lala.ecommerce.helper;

import com.lala.ecommerce.dto.AuthDto;
import com.lala.ecommerce.dto.CustomerDto;
import com.lala.ecommerce.dto.LoginDto;
import com.lala.ecommerce.model.Address;

public class CustomerDtoFactory {


    public CustomerDto generateCustomerDtoById(Long id, String role) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(id);
        customerDto.setEmail("test");
        customerDto.setRoles(role);
        customerDto.setFirstName("firstname");
        customerDto.setLastName("lastname");

        Address address = new Address();
        address.setAddressLine("addess line");
        address.setCity("city");
        address.setDistrict("district");
        address.setPostCode("1234");
        address.setCountry("country");

        customerDto.setAddress(address);

        return customerDto;
    }

    public AuthDto generateAuthDto() {
        AuthDto authDto = new AuthDto();
        authDto.setEmail("email");
        authDto.setPassword("password");

        return authDto;
    }

    public LoginDto generateLoginDto() {
        LoginDto loginDto = new LoginDto();
        loginDto.setToken("auth-token");
        loginDto.setCustomerId(1L);

        return loginDto;
    }
}

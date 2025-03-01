package com.lala.ecommerce.helper;

import com.lala.ecommerce.dto.CustomerDto;
import com.lala.ecommerce.model.Address;
import com.lala.ecommerce.model.Customer;

public class CustomerDoFactory {

    public Customer generateCustomerById(Long id) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setEmail("test");
        customer.setRoles("ROLE_USER");
        customer.setFirstName("firstname");
        customer.setLastName("lastname");
        customer.setPassword("password");

        Address address = new Address();
        address.setAddressLine("addess line");
        address.setCity("city");
        address.setDistrict("district");
        address.setPostCode("1234");
        address.setCountry("country");

        customer.setAddress(address);

        return customer;
    }
}

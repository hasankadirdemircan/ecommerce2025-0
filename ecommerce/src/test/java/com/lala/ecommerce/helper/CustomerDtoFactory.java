package com.lala.ecommerce.helper;

import com.lala.ecommerce.dto.CustomerDto;
import com.lala.ecommerce.model.Address;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;

public class CustomerDtoFactory {


    public CustomerDto generateCustomerDtoById(Long id) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(id);
        customerDto.setEmail("test");
        customerDto.setRoles("ROLE_USER");
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
}

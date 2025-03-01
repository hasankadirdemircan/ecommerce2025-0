package com.lala.ecommerce.mapper;

import com.lala.ecommerce.dto.CustomerDto;
import com.lala.ecommerce.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring" , unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    CustomerDto customerToCustomerDto(Customer customer);
}

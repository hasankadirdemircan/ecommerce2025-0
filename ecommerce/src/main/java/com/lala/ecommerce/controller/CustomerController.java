package com.lala.ecommerce.controller;

import com.lala.ecommerce.dto.AuthDto;
import com.lala.ecommerce.dto.CustomerDto;
import com.lala.ecommerce.dto.LoginDto;
import com.lala.ecommerce.model.Customer;
import com.lala.ecommerce.model.Order;
import com.lala.ecommerce.service.CustomerService;
import com.lala.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final OrderService orderService;

  @PostMapping("/register")
  public ResponseEntity<CustomerDto> createCustomer(@RequestBody Customer customer) {
        return new ResponseEntity<>(customerService.createCustomer(customer), HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginDto> login(@RequestBody AuthDto authDto) {
        return new ResponseEntity<>(customerService.login(authDto), HttpStatus.OK);
  }

  @GetMapping("/test")
  public ResponseEntity<String> test() {
      return new ResponseEntity<>("test", HttpStatus.OK);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
  public ResponseEntity<List<Order>> getCustomerOrderHistory(@PathVariable("id") Long id) {
      return new ResponseEntity<>(orderService.getCustomerOrderHistory(id), HttpStatus.OK);
  }
}

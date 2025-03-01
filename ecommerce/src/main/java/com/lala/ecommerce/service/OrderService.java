package com.lala.ecommerce.service;

import com.lala.ecommerce.dto.OrderRequest;
import com.lala.ecommerce.model.Order;

import java.util.List;

public interface OrderService {
    Boolean doOrder(OrderRequest orderRequest);
    List<Order> getCustomerOrderHistory(Long id);
}

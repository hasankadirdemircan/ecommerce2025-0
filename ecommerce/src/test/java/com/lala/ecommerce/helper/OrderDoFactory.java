package com.lala.ecommerce.helper;

import com.lala.ecommerce.model.Order;

import java.util.List;

public class OrderDoFactory {

    public List<Order> generateOrderList() {
        Order order1 = new Order();
        order1.setTotalPrice(100d);
        order1.setQuantity(4);
        order1.setPrice(25d);
        order1.setProductId(1L);

        Order order2 = new Order();
        order2.setTotalPrice(200d);
        order2.setQuantity(5);
        order2.setPrice(40d);
        order2.setProductId(2L);

        return List.of(order1, order2);
    }
}

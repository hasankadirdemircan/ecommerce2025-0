package com.lala.ecommerce.helper;

import com.lala.ecommerce.dto.OrderProductInfo;
import com.lala.ecommerce.dto.OrderRequest;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class OrderDtoFactory {

    public OrderRequest generateOrderRequest() {
        OrderRequest orderRequest = new OrderRequest();
        List<OrderProductInfo> orderProductInfoList = new ArrayList<>();
        OrderProductInfo orderProductInfo1 = new OrderProductInfo();
        orderProductInfo1.setProductId(1L);
        orderProductInfo1.setQuantity(5);

        OrderProductInfo orderProductInfo2 = new OrderProductInfo();
        orderProductInfo2.setProductId(2L);
        orderProductInfo2.setQuantity(2);

        orderProductInfoList.add(orderProductInfo1);
        orderProductInfoList.add(orderProductInfo2);

        orderRequest.setOrderList(orderProductInfoList);
        orderRequest.setCustomerId(1L);

        return orderRequest;
    }
}

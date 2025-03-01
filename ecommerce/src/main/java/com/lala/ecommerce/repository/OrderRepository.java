package com.lala.ecommerce.repository;

import com.lala.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.customerId = :customerId")
    List<Order> getCustomerOrderHistory(@Param("customerId") Long customerId);
}

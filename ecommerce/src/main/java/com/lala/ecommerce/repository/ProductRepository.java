package com.lala.ecommerce.repository;

import com.lala.ecommerce.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.categoryId =:categoryId AND p.active = true")
    List<Product> findProductListByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT p FROM Product p WHERE p.active = false")
    List<Product> getDeActiveProductList();

    @Query("SELECT p FROM Product p")
    List<Product> getProductList(); // findAll() ile aynı isi yapiyor.

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.active = :isActive WHERE p.id = :id")
    void updateProductActive(@Param("isActive") boolean isActive, @Param("id") Long id);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.categoryId = :categoryId")
    Long getProductCountByCategoryId(@Param("categoryId") Long categoryId);

}

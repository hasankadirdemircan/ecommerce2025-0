package com.lala.ecommerce.helper;

import com.lala.ecommerce.model.Product;

import java.util.List;

public class ProductDoFactory {

    public Product generateProduct(Long categoryId, Long productId) {
        Product product = new Product();
        product.setActive(true);
        product.setId(productId);
        product.setUnitsInStock(5);
        product.setName("product");
        product.setCategoryId(categoryId);
        product.setPrice(2.0);

        return product;
    }

    public List<Product> generateProductList(Long categoryId) {
        Product product1 = new Product();
        product1.setActive(true);
        product1.setId(1L);
        product1.setUnitsInStock(5);
        product1.setName("product1");
        product1.setCategoryId(categoryId);

        Product product2 = new Product();
        product2.setActive(true);
        product2.setId(2L);
        product2.setUnitsInStock(5);
        product2.setName("product2");
        product2.setCategoryId(categoryId);

        Product product3 = new Product();
        product3.setActive(true);
        product3.setId(3L);
        product3.setUnitsInStock(5);
        product3.setName("product3");
        product3.setCategoryId(categoryId);

        return List.of(product1, product2, product3);
    }
}

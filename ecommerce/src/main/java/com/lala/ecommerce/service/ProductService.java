package com.lala.ecommerce.service;

import com.lala.ecommerce.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    Product createProduct(MultipartFile file, Product product);
    List<Product> getProductListByCategoryId(Long categoryId);
    Product getProduct(Long id);
    void activeOrDeActiveProduct(Long id, boolean isActive);
    void deleteProduct(Long id);
    List<Product> getAllProductList();
}

package com.lala.ecommerce.service;

import com.lala.ecommerce.exception.ProductNotFoundException;
import com.lala.ecommerce.helper.CustomerDoFactory;
import com.lala.ecommerce.helper.ProductDoFactory;
import com.lala.ecommerce.model.Product;
import com.lala.ecommerce.repository.ProductRepository;
import com.lala.ecommerce.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    private ProductDoFactory productDoFactory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.productDoFactory = new ProductDoFactory();
    }

    @Test
    void shouldReturnProductList_success() {
        //given
        Long categoryId = 3L;
        List<Product> productList = productDoFactory.generateProductList(categoryId);

        //when
        when(productRepository.findProductListByCategoryId(categoryId)).thenReturn(productList);

        //then
        List<Product> response = productService.getProductListByCategoryId(categoryId);

        //assert
        assertEquals(productList, response);
        verify(productRepository, times(1)).findProductListByCategoryId(categoryId);
    }

    @Test
    void shouldReturnProduct_success() {
        //given
        Long productId = 1L;
        Product product = productDoFactory.generateProduct(3L, productId);

        //when
        when(productRepository.findById(productId)).thenReturn( Optional.of(product));

        //then
        Product response = productService.getProduct(productId);

        //assert
        assertEquals(product, response);
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void shouldThrowProductNotFoundException_failed() {
        //given
        Long productId = 1L;

        //when
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        //then
        ProductNotFoundException thrown = Assertions.assertThrows(ProductNotFoundException.class,
                () -> productService.getProduct(productId));

        //assert
        assertEquals("Product not Found, id : " + productId, thrown.getMessage());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void shouldUpdateActiveProduct_success() {
        //given
        Long productId = 1L;
        boolean isActive = true;

        //when
        Mockito.doNothing().when(productRepository).updateProductActive(isActive, productId);

        //then
        productService.activeOrDeActiveProduct(productId, isActive);

        //assert
        verify(productRepository, times(1)).updateProductActive(isActive, productId);
    }

    @Test
    void shouldReturnAllProductList_success() {
        //given
        List<Product> productList = productDoFactory.generateProductList(3L);

        //when
        when(productRepository.findAll()).thenReturn(productList);

        //then
        List<Product> response = productService.getAllProductList();

        //assert
        assertEquals(productList, response);
        verify(productRepository, times(1)).findAll();
    }
}

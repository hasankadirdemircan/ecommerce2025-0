package com.lala.ecommerce.service;

import com.lala.ecommerce.exception.CategoryDeleteException;
import com.lala.ecommerce.exception.CategoryDuplicateException;
import com.lala.ecommerce.helper.CategoryDOFactory;
import com.lala.ecommerce.model.Category;
import com.lala.ecommerce.repository.CategoryRepository;
import com.lala.ecommerce.repository.ProductRepository;
import com.lala.ecommerce.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    private CategoryDOFactory categoryDOFactory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.categoryDOFactory = new CategoryDOFactory();
    }

    @Test
    void createCategory_successful() {
        //given
        Category category = categoryDOFactory.getCategoryWithoutId();
        Category savedCategory = categoryDOFactory.getCategoryById(1L);

        //when
        when(categoryRepository.findCategoryByName(category.getName())).thenReturn(Optional.empty());
        when(categoryRepository.save(category)).thenReturn(savedCategory);

        //then
        Category response = categoryService.createCategory(category);

        //assert
        assertEquals(category.getName(), response.getName());
        assertEquals(savedCategory.getId(), response.getId());
        verify(categoryRepository, times(1)).findCategoryByName(category.getName());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void createCategory_shouldThrowCategoryDuplicateException() {
        //given
        Category savedCategory = categoryDOFactory.getCategoryById(1L);

        //when
        when(categoryRepository.findCategoryByName(savedCategory.getName())).thenReturn(Optional.of(savedCategory));

        //then
        CategoryDuplicateException thrown = Assertions.assertThrows(CategoryDuplicateException.class,
                () -> categoryService.createCategory(savedCategory));

        //assert
        assertEquals("Category is already defined : " + savedCategory.getName(), thrown.getMessage());
        verify(categoryRepository, times(1)).findCategoryByName(savedCategory.getName());
        verify(categoryRepository, times(0)).save(savedCategory);

    }

    @Test
    void deleteCategory_successful() {
        //given
        Long categoryId = 1L;

        //when
        when(productRepository.getProductCountByCategoryId(categoryId)).thenReturn(0L);
        Mockito.doNothing().when(categoryRepository).deleteById(categoryId);

        //then
        categoryService.deleteCategory(categoryId);

        //assert
        verify(productRepository, times(1)).getProductCountByCategoryId(categoryId);
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    void deleteCategory_shouldThrowCategoryDeleteException() {
        //given
        Long categoryId = 1L;
        Long productCountOfCategory = 2L;

        //when
        when(productRepository.getProductCountByCategoryId(categoryId)).thenReturn(productCountOfCategory);

        //then
        CategoryDeleteException thrown = Assertions.assertThrows(CategoryDeleteException.class,
                () -> categoryService.deleteCategory(categoryId));

        //assert
        assertEquals("you can not delete this category because category has " + productCountOfCategory + " product", thrown.getMessage());
        verify(productRepository, times(1)).getProductCountByCategoryId(categoryId);
        verify(categoryRepository, times(0)).deleteById(categoryId);
    }


}

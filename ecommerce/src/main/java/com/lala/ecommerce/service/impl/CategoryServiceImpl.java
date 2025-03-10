package com.lala.ecommerce.service.impl;

import com.lala.ecommerce.exception.CategoryDeleteException;
import com.lala.ecommerce.exception.CategoryDuplicateException;
import com.lala.ecommerce.exception.CategoryNotFoundException;
import com.lala.ecommerce.model.Category;
import com.lala.ecommerce.repository.CategoryRepository;
import com.lala.ecommerce.repository.ProductRepository;
import com.lala.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public Category createCategory(Category category) {
        Optional<Category> optionalCategory = categoryRepository.findCategoryByName(category.getName());
        if(optionalCategory.isPresent()) {
            throw new CategoryDuplicateException("Category is already defined : " + category.getName());
        }

        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        Long productCountOfCategory = productRepository.getProductCountByCategoryId(id);
        if(productCountOfCategory > 0) {
            throw new CategoryDeleteException("you can not delete this category because category has " + productCountOfCategory + " product");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public Category getCategory(Long id) {
      return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category not found, id : " + id));
    }

    @Override
    public List<Category> getCategoryList() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(Category category) {
        return categoryRepository.save(category);
    }
}

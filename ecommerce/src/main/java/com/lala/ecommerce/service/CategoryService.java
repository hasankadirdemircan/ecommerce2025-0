package com.lala.ecommerce.service;

import com.lala.ecommerce.exception.CategoryDuplicateException;
import com.lala.ecommerce.exception.CategoryNotFoundException;
import com.lala.ecommerce.model.Category;
import com.lala.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category createCategory(Category category) {
        Optional<Category> optionalCategory = categoryRepository.findCategoryByName(category.getName());
        if(optionalCategory.isPresent()) {
            throw new CategoryDuplicateException("Category is already defined : " + category.getName());
        }

        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public Category getCategory(Long id) {
      return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category not found, id : " + id));
    }

    public List<Category> getCategoryList() {
        return categoryRepository.findAll();
    }

    public Category updateCategory(Category category) {
        return categoryRepository.save(category);
    }
}

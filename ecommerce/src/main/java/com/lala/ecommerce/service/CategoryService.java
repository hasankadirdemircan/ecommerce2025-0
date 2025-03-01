package com.lala.ecommerce.service;

import com.lala.ecommerce.model.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(Category category);

    void deleteCategory(Long id);

    Category getCategory(Long id);

    List<Category> getCategoryList();

    Category updateCategory(Category category);
}

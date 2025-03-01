package com.lala.ecommerce.helper;

import com.lala.ecommerce.model.Category;

import java.util.List;

public class CategoryDOFactory {

    public Category getCategoryById(Long id) {
        Category category = new Category();
        category.setId(id);
        category.setName("category1");

        return category;
    }

    public Category getCategoryWithoutId() {
        Category category = new Category();
        category.setName("category1");

        return category;
    }

    public List<Category> getCategoryListWithId() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("category1");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("category2");

        Category category3 = new Category();
        category3.setId(3L);
        category3.setName("category3");

        return List.of(category1, category2, category3);
    }
}

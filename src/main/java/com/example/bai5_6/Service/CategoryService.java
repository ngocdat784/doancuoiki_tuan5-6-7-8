package com.example.bai5_6.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bai5_6.Repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<com.example.bai5_6.Model.Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void saveCategory(com.example.bai5_6.Model.Category category) {
        categoryRepository.save(category);
    }

    public com.example.bai5_6.Model.Category getCategoryById(Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }
}
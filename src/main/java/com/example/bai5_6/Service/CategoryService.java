package com.example.bai5_6.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.bai5_6.Model.Category;
import com.example.bai5_6.Repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // ================== GET ALL ==================
    public java.util.List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // ================== PAGINATION ==================
    public Page<Category> getCategories(int page) {
        Pageable pageable = PageRequest.of(page, 5); // 5 item / page
        return categoryRepository.findAll(pageable);
    }

    // ================== SAVE ==================
    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    // ================== GET BY ID ==================
    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }

    // ================== DELETE ==================
    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }

    // ================== SEARCH ==================
    public Page<Category> searchCategory(String keyword, int page) {

        Pageable pageable = PageRequest.of(page, 5);

        if (keyword != null && !keyword.trim().isEmpty()) {
            return categoryRepository
                    .findByNameContainingIgnoreCase(keyword, pageable);
        }

        return categoryRepository.findAll(pageable);
    }
}
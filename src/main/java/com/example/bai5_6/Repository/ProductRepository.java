package com.example.bai5_6.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bai5_6.Model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
    Page<Product> findByCategoryId(Integer categoryId, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCaseAndCategoryId(
        String keyword, Integer categoryId, Pageable pageable);
}
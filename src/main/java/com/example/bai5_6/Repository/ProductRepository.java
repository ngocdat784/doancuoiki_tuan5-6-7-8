package com.example.bai5_6.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bai5_6.Model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
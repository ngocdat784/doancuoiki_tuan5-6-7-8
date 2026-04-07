package com.example.bai5_6.Repository;

import com.example.bai5_6.Model.ProductClick;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductClickRepository extends JpaRepository<ProductClick, Integer> {

    ProductClick findByProductIdAndUsername(Integer productId, String username);

    List<ProductClick> findTop3ByUsernameOrderByClickCountDesc(String username);

    List<ProductClick> findTop3ByUsernameIsNullOrderByClickCountDesc();
}
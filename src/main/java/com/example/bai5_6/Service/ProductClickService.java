package com.example.bai5_6.Service;

import java.util.List;

import com.example.bai5_6.Model.ProductClick;

public interface ProductClickService {

    void increaseClick(Integer productId, String username);

    List<ProductClick> getTop3Global();

    List<ProductClick> getTop3ByUser(String username);
}

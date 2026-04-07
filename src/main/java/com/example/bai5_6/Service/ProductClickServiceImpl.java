package com.example.bai5_6.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bai5_6.Model.Product;
import com.example.bai5_6.Model.ProductClick;
import com.example.bai5_6.Repository.ProductClickRepository;
import com.example.bai5_6.Repository.ProductRepository;

@Service
public class ProductClickServiceImpl implements ProductClickService {

    @Autowired
    private ProductClickRepository repo;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void increaseClick(Integer productId, String username) {

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) return;

        // ===== GLOBAL =====
        ProductClick global = repo.findByProductIdAndUsername(productId, null);

        if (global == null) {
            global = new ProductClick();
            global.setProduct(product);
            global.setClickCount(1);
            global.setUsername(null);
        } else {
            global.setClickCount(global.getClickCount() + 1);
        }
        repo.save(global);

        // ===== USER =====
        if (username != null) {

            ProductClick userClick =
                    repo.findByProductIdAndUsername(productId, username);

            if (userClick == null) {
                userClick = new ProductClick();
                userClick.setProduct(product);
                userClick.setUsername(username);
                userClick.setClickCount(1);
            } else {
                userClick.setClickCount(userClick.getClickCount() + 1);
            }

            repo.save(userClick);
        }
    }

    @Override
    public List<ProductClick> getTop3Global() {
        return repo.findTop3ByUsernameIsNullOrderByClickCountDesc();
    }

    @Override
    public List<ProductClick> getTop3ByUser(String username) {
        return repo.findTop3ByUsernameOrderByClickCountDesc(username);
    }
}

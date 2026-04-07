package com.example.bai5_6.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.bai5_6.Model.Product;
import com.example.bai5_6.Repository.CategoryRepository;
import com.example.bai5_6.Repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // ================== GET ALL ==================
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // ================== PAGINATION + SORT ==================
    public Page<Product> getProducts(int page, String sortDir) {

        Sort sort = sortDir.equals("asc")
                ? Sort.by("price").ascending()
                : Sort.by("price").descending();

        Pageable pageable = PageRequest.of(page, 5, sort);

        return productRepository.findAll(pageable);
    }

    // ================== SAVE ==================
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    // ================== GET BY ID ==================
    public Product getProductById(int id) {
        return productRepository.findById(id).orElse(null);
    }

    // ================== DELETE ==================
    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }

    // ================== SEARCH + PAGINATION + SORT ==================
    public Page<Product> searchByName(String keyword, int page, String sortDir) {

        Sort sort = sortDir.equals("asc")
                ? Sort.by("price").ascending()
                : Sort.by("price").descending();

        Pageable pageable = PageRequest.of(page, 5, sort);

        return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }
    public Page<Product> filterByCategory(Integer categoryId, int page, String sortDir) {

    Sort sort = sortDir.equals("asc")
            ? Sort.by("price").ascending()
            : Sort.by("price").descending();

    Pageable pageable = PageRequest.of(page, 5, sort);

    return productRepository.findByCategoryId(categoryId, pageable);
}
public Page<Product> searchAndFilter(String keyword, Integer categoryId, int page, String sortDir) {

    Sort sort = sortDir.equals("asc")
            ? Sort.by("price").ascending()
            : Sort.by("price").descending();

    Pageable pageable = PageRequest.of(page, 5, sort);

    if (keyword != null && !keyword.isEmpty() && categoryId != null) {
        return productRepository
                .findByNameContainingIgnoreCaseAndCategoryId(keyword, categoryId, pageable);
    } else if (keyword != null && !keyword.isEmpty()) {
        return productRepository
                .findByNameContainingIgnoreCase(keyword, pageable);
    } else if (categoryId != null) {
        return productRepository
                .findByCategoryId(categoryId, pageable);
    } else {
        return productRepository.findAll(pageable);
    }
}

}
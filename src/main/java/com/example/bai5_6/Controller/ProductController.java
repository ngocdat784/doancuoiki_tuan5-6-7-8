package com.example.bai5_6.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.bai5_6.Model.Product;
import com.example.bai5_6.Service.CategoryService;
import com.example.bai5_6.Service.ProductService;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
public String listProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "asc") String sortDir,
        @RequestParam(required = false) Integer categoryId,
        Model model) {

    Page<Product> productPage;

    if (categoryId != null) {
        productPage = productService.filterByCategory(categoryId, page, sortDir);
    } else {
        productPage = productService.getProducts(page, sortDir);
    }

    model.addAttribute("products", productPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", productPage.getTotalPages());
    model.addAttribute("sortDir", sortDir);
    model.addAttribute("categoryId", categoryId);
    model.addAttribute("categories", categoryService.getAllCategories());

    return "product/list";
}

    // ================== ADD ==================
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/add";
    }

    // ================== SAVE ==================
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product) {
        productService.saveProduct(product);
        return "redirect:/products";
    }

    // ================== EDIT ==================
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/edit";
    }

    // ================== DELETE ==================
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }

   @GetMapping("/search")
public String searchProduct(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Integer categoryId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "asc") String sortDir,
        Model model) {

    Page<Product> productPage =
            productService.searchAndFilter(keyword, categoryId, page, sortDir);

    model.addAttribute("products", productPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", productPage.getTotalPages());
    model.addAttribute("keyword", keyword);
    model.addAttribute("sortDir", sortDir);
    model.addAttribute("categoryId", categoryId);
    model.addAttribute("categories", categoryService.getAllCategories());

    return "product/list";
}

}
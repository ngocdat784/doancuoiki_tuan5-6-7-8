package com.example.bai5_6.Controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.bai5_6.Model.Product;
import com.example.bai5_6.Service.CategoryService;
import com.example.bai5_6.Service.ProductClickService;
import com.example.bai5_6.Service.ProductService;
import com.example.bai5_6.Service.ReviewService;
import com.example.bai5_6.Model.Review;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
private ReviewService reviewService;
@Autowired
private ProductClickService productClickService;
    @GetMapping
public String listProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "asc") String sortDir,
        @RequestParam(required = false) Integer categoryId,
        Model model,
        Principal principal) {

    Page<Product> productPage;

    if (categoryId != null) {
        productPage = productService.filterByCategory(categoryId, page, sortDir);
    } else {
        productPage = productService.getProducts(page, sortDir);
    }

    Map<Integer, List<Product>> productsByCategory =
        productPage.getContent().stream()
        .collect(Collectors.groupingBy(p ->
            p.getCategory() != null ? p.getCategory().getId() : 0
        ));

    model.addAttribute("products", productPage.getContent());
    model.addAttribute("productsByCategory", productsByCategory);

    // ✅ REVIEW
    model.addAttribute("reviewService", reviewService);

    // 🔥 BÁN CHẠY (GLOBAL)
    model.addAttribute("topGlobal",
            productClickService.getTop3Global());

    // 🔥 ĐỀ XUẤT (USER)
    if (principal != null) {
        model.addAttribute("topUser",
                productClickService.getTop3ByUser(principal.getName()));
    }

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
@GetMapping("/{id}")
public String productDetail(@PathVariable Integer id, Model model, Principal principal) {

    Product product = productService.getProductById(id);
    List<Review> reviews = reviewService.getReviewsByProduct(id);

    double avgRating = reviewService.getAverageRating(id);

    model.addAttribute("product", product);
    model.addAttribute("reviews", reviews);
    model.addAttribute("avgRating", avgRating);

    // ✅ kiểm tra user đã đánh giá chưa
    if (principal != null) {
        Review userReview = reviewService.getUserReview(id, principal.getName());
        model.addAttribute("userReview", userReview);
    }

    return "product/detail";
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

    // ✅ THÊM
    model.addAttribute("reviewService", reviewService);

    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", productPage.getTotalPages());
    model.addAttribute("keyword", keyword);
    model.addAttribute("sortDir", sortDir);
    model.addAttribute("categoryId", categoryId);
    model.addAttribute("categories", categoryService.getAllCategories());

    return "product/list";
}
@PostMapping("/review")
public String addReview(@RequestParam Integer productId,
                        @RequestParam int rating,
                        @RequestParam String comment,
                        Principal principal) {

    reviewService.saveReview(productId, rating, comment, principal.getName());

    return "redirect:/products/" + productId;
}
}
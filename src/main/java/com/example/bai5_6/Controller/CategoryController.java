package com.example.bai5_6.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.bai5_6.Model.Category;
import com.example.bai5_6.Service.CategoryService;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // ================== LIST ==================
    @GetMapping
    public String listCategories(
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Page<Category> categoryPage = categoryService.getCategories(page);

        model.addAttribute("categories", categoryPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categoryPage.getTotalPages());

        return "category/list";
    }

    // ================== ADD ==================
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        return "category/add";
    }

    // ================== SAVE ==================
    @PostMapping("/save")
    public String saveCategory(@ModelAttribute("category") Category category) {
        categoryService.saveCategory(category);
        return "redirect:/categories";
    }

    // ================== EDIT ==================
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {

        Category category = categoryService.getCategoryById(id);

        model.addAttribute("category", category);
        return "category/edit";
    }

    // ================== DELETE ==================
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer id) {
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }

    // ================== SEARCH ==================
    @GetMapping("/search")
    public String searchCategory(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Page<Category> categoryPage =
                categoryService.searchCategory(keyword, page);

        model.addAttribute("categories", categoryPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categoryPage.getTotalPages());
        model.addAttribute("keyword", keyword);

        return "category/list";
    }
}
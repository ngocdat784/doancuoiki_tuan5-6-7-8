package com.example.bai5_6.Controller;

import com.example.bai5_6.Model.CartItem;
import com.example.bai5_6.Model.Product;
import com.example.bai5_6.Service.ProductService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductService productService;

  @GetMapping("/add/{id}")
public String addToCart(@PathVariable("id") Integer id, HttpSession session) {

    Product product = productService.getProductById(id);

    List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

    if (cart == null) {
        cart = new ArrayList<>();
    }

    boolean found = false;

    for (CartItem item : cart) {
        if (item.getProductId().equals(product.getId())) {
            // ❌ KHÔNG tăng số lượng nữa
            found = true;
            break;
        }
    }

    if (!found) {
        CartItem newItem = new CartItem(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImage(),
                1
        );
        cart.add(newItem);
    }

    session.setAttribute("cart", cart);

    return "redirect:/cart";
}

    // ================== VIEW CART ==================
    @GetMapping("")
    public String viewCart(HttpSession session, Model model) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
        }

        model.addAttribute("cart", cart);
        return "cart/view";
    }
    @GetMapping("/increase/{id}")
public String increase(@PathVariable("id") Long id, HttpSession session) {

    List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

    if (cart != null) {
        for (CartItem item : cart) {
            if (item.getProductId().equals(id)) {
                item.setQuantity(item.getQuantity() + 1);
                break;
            }
        }
    }

    return "redirect:/cart";
}
@GetMapping("/decrease/{id}")
public String decrease(@PathVariable("id") Long id, HttpSession session) {

    List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

    if (cart != null) {
        cart.removeIf(item -> {
            if (item.getProductId().equals(id)) {
                item.setQuantity(item.getQuantity() - 1);
                return item.getQuantity() <= 0;
            }
            return false;
        });
    }

    return "redirect:/cart";
}
}
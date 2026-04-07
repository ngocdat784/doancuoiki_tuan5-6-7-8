package com.example.bai5_6.Controller;

import com.example.bai5_6.Model.CartItem;
import com.example.bai5_6.Model.Order;
import com.example.bai5_6.Model.OrderDetail;
import com.example.bai5_6.Model.Product;
import com.example.bai5_6.Repository.OrderDetailRepository;
import com.example.bai5_6.Repository.OrderRepository;
import com.example.bai5_6.Service.ProductClickService;
import com.example.bai5_6.Service.ProductService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.bai5_6.Model.OrderStatus;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductService productService;
    @Autowired
private OrderRepository orderRepository;

@Autowired
private OrderDetailRepository orderDetailRepository;
@Autowired
private ProductClickService productClickService;

 @GetMapping("/add/{id}")
public String addToCart(@PathVariable("id") Integer id,
                        HttpSession session,
                        Principal principal) {

    // ================= CLICK TRACKING =================
    String username = (principal != null) ? principal.getName() : null;
    productClickService.increaseClick(id, username);

    // ================= CART LOGIC (code cũ) =================
    Product product = productService.getProductById(id);

    List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

    if (cart == null) {
        cart = new ArrayList<>();
    }

    boolean found = false;

    for (CartItem item : cart) {
        if (item.getProductId().equals(product.getId())) {
            // ❌ không tăng số lượng
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
@PostMapping("/checkout")
public String checkout(HttpSession session, Principal principal) {

    List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

    if (cart == null || cart.isEmpty()) {
        return "redirect:/cart";
    }

    Order order = new Order();

    // ✅ SET USERNAME
    order.setUsername(principal.getName());

    // ✅ STATUS
    order.setStatus(OrderStatus.PENDING);

    double total = 0;

    order = orderRepository.save(order);

    for (CartItem item : cart) {

        Product product = productService.getProductById(item.getProductId().intValue());

        OrderDetail detail = new OrderDetail(
                order,
                product,
                item.getQuantity(),
                item.getPrice()
        );

        total += item.getQuantity() * item.getPrice();

        orderDetailRepository.save(detail);
    }

    order.setTotalAmount(total);
    orderRepository.save(order);

    session.removeAttribute("cart");

    return "redirect:/cart/success";
}
@GetMapping("/my-orders")
public String myOrders(Model model, Principal principal) {

    List<Order> orders = orderRepository.findByUsername(principal.getName());

    model.addAttribute("orders", orders);
    return "order/my-orders";
}
@GetMapping("/admin/orders")
public String allOrders(Model model) {
    model.addAttribute("orders", orderRepository.findAll());
    return "order/admin-orders";
}
@PostMapping("/admin/orders/update")
public String updateStatus(@RequestParam int orderId,
                           @RequestParam OrderStatus status,
                           RedirectAttributes redirectAttributes) {

    Order order = orderRepository.findById(orderId).orElse(null);

    if (order != null) {
        order.setStatus(status);
        orderRepository.save(order);

        // ✅ thêm dòng này
        redirectAttributes.addFlashAttribute("success", "Cập nhật trạng thái thành công!");
    }

    return "redirect:/cart/admin/orders";
}
@GetMapping("/success")
public String checkoutSuccess(Model model) {
    model.addAttribute("message", "Đặt hàng thành công!");
    return "cart/success";
}
}
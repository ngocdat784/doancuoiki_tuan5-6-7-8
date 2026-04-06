package com.example.bai5_6.Model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date orderDate;

    private double totalAmount;
    private String username;

    // ✅ THÊM TRẠNG THÁI
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    public Order() {
        this.orderDate = new Date();
        this.status = OrderStatus.PENDING; // ✅ mặc định
    }

    public Long getId() { return id; }

    public Date getOrderDate() { return orderDate; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public List<OrderDetail> getOrderDetails() { return orderDetails; }
    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    // ✅ Getter / Setter cho status
    public OrderStatus getStatus() { return status; }

    public void setStatus(OrderStatus status) { this.status = status; }
    // ✅ Getter / Setter cho username
public String getUsername() {
    return username;
}

public void setUsername(String username) {
    this.username = username;
}
}
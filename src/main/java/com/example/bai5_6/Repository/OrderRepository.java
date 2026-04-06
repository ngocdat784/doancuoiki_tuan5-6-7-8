package com.example.bai5_6.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bai5_6.Model.Order;
import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUsername(String username);
}
package com.example.bai5_6.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bai5_6.Model.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
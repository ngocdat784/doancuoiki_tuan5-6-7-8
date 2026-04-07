package com.example.bai5_6.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bai5_6.Model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
package com.example.bai5_6.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bai5_6.Model.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByUsername(String username);
}

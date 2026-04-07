package com.example.bai5_6.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.example.bai5_6.Model.Account;
import com.example.bai5_6.Model.Role;
import com.example.bai5_6.Repository.AccountRepository;
import com.example.bai5_6.Repository.RoleRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    public AccountService(AccountRepository accountRepository,
                          RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
    }

    // ================= LOGIN =================
    @Override
    public UserDetails loadUserByUsername(String username) {

        Account account = accountRepository.findByUsername(username);

        if (account == null) {
            throw new UsernameNotFoundException("User not found");
        }

        List<GrantedAuthority> authorities = account.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new User(
                account.getUsername(),
                account.getPassword(),
                authorities
        );
    }

    // ================= REGISTER =================
    public void register(String username, String password) {

        // check trùng username
        if (accountRepository.findByUsername(username) != null) {
            throw new RuntimeException("Username already exists");
        }

        // encode password (KHÔNG inject nữa)
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Account account = new Account();
        account.setUsername(username);
        account.setPassword(encoder.encode(password));

        // role USER
        Role roleUser = roleRepository.findByName("ROLE_USER");

        if (roleUser == null) {
            roleUser = new Role();
            roleUser.setName("ROLE_USER");
            roleUser = roleRepository.save(roleUser);
        }

        account.setRoles(List.of(roleUser));

        accountRepository.save(account);
    }
}
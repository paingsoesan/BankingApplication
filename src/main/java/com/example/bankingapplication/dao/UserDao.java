package com.example.bankingapplication.dao;

import com.example.bankingapplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User,Long> {

    Boolean existsByEmail(String email);
}

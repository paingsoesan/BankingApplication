package com.example.bankingapplication.dao;

import com.example.bankingapplication.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionDao extends JpaRepository<Transaction, String> {
}

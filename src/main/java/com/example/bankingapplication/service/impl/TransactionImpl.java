package com.example.bankingapplication.service.impl;

import com.example.bankingapplication.dao.TransactionDao;
import com.example.bankingapplication.dto.TransactionDto;
import com.example.bankingapplication.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionImpl implements TransactionService{

    @Autowired
    private TransactionDao transactionDao;

    @Override
    public List<TransactionDto> saveTransaction(TransactionDto transactionDto) {
        Transaction transaction = Transaction.builder()
                .transactionType(transactionDto.getTransactionType())
                .accountNumber(transactionDto.getAccountNumber())
                .amount(transactionDto.getAmount())
                .status("Success")
                .createdAt(LocalDate.now())
                .build();
        transactionDao.save(transaction);
        System.out.println("Transaction saved successfully");
        return null;
    }
}

package com.example.bankingapplication.service.impl;

import com.example.bankingapplication.dto.TransactionDto;

import java.util.List;

public interface TransactionService {

    List<TransactionDto> saveTransaction(TransactionDto transactionDto);
}

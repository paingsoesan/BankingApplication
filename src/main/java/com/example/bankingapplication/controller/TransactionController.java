package com.example.bankingapplication.controller;

import com.example.bankingapplication.dto.TransactionDto;
import com.example.bankingapplication.entity.Transaction;
import com.example.bankingapplication.service.impl.BankStatement;
import com.example.bankingapplication.service.impl.TransactionImpl;
import com.example.bankingapplication.service.impl.TransactionService;
import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/bankStatement")
@AllArgsConstructor
public class TransactionController {

    @Autowired
    private BankStatement bankStatement;

    @GetMapping("/transaction")
    public List<Transaction> generateBankStatement(@RequestParam String accountNumber,
                                                   @RequestParam String startDate,
                                                   @RequestParam String endDate) throws DocumentException, FileNotFoundException {
            return bankStatement.generateStatement(accountNumber,startDate,endDate);
    }

}

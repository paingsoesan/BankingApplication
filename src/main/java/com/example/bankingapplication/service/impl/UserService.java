package com.example.bankingapplication.service.impl;

import com.example.bankingapplication.dto.BankResponse;
import com.example.bankingapplication.dto.CreditDebitRequest;
import com.example.bankingapplication.dto.EnquiryRequest;
import com.example.bankingapplication.dto.UserRequest;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
    String nameEnquiry(EnquiryRequest enquiryRequest);
    BankResponse creditAccount(CreditDebitRequest creditDebitRequest);

    BankResponse debitAccount(CreditDebitRequest creditDebitRequest);
}

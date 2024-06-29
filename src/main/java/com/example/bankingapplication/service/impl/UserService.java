package com.example.bankingapplication.service.impl;

import com.example.bankingapplication.dto.BankResponse;
import com.example.bankingapplication.dto.UserRequest;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);
}

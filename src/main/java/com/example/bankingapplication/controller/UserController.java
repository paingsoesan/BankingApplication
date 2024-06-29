package com.example.bankingapplication.controller;

import com.example.bankingapplication.dto.BankResponse;
import com.example.bankingapplication.dto.UserRequest;

import com.example.bankingapplication.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    //testing git

    @Autowired
    private UserService userService;


    @PostMapping("/create")
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
        return userService.createAccount(userRequest);
    }

}

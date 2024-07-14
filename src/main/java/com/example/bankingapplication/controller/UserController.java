package com.example.bankingapplication.controller;

import com.example.bankingapplication.dto.*;

import com.example.bankingapplication.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management APIs")
public class UserController {
    //testing git
    //user email reply

    @Autowired
    private UserService userService;
    @Operation(
            summary = "Create New User Account",
            description = "Creating a new user and assigning an accout ID"
    )

    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )


    @PostMapping("/create")
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
        return userService.createAccount(userRequest);
    }

    @Operation(
            summary = "Balance Enquiry",
            description = "Given account number,check how much the user has"
    )

    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )


    @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.balanceEnquiry(enquiryRequest);
    }

    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.nameEnquiry(enquiryRequest);
    }

    @PostMapping("/credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest creditDebitRequest){
        return userService.creditAccount(creditDebitRequest);
    }

    @PostMapping("/debit")
    public  BankResponse debitAccount(@RequestBody CreditDebitRequest creditDebitRequest){
        return userService.debitAccount(creditDebitRequest);
    }

    @PostMapping("/transfer")
    public BankResponse transfer(@RequestBody TransferRequest transferRequest){
        return userService.transfer(transferRequest);
    }


}

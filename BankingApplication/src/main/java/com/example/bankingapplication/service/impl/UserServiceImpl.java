package com.example.bankingapplication.service.impl;

import com.example.bankingapplication.dao.UserDao;
import com.example.bankingapplication.dto.AccountInfo;
import com.example.bankingapplication.dto.BankResponse;
import com.example.bankingapplication.dto.EmailDetails;
import com.example.bankingapplication.dto.UserRequest;
import com.example.bankingapplication.entity.User;
import com.example.bankingapplication.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private EmailService emailService;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {

        if (userDao.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                                    .accountInfo(null)
                                        .build();

        }

        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                    .build();

        User savedUser = userDao.save(newUser);
        //send email alert
        EmailDetails emailDetails = EmailDetails.builder()
                                    .recipient(savedUser.getEmail())
                                    .subject("Account Creation")
                                    .messageBody("Congratulation! Your account has been successfully created.\nYour Account Details: \n" + "\nAccount Name: " + savedUser.getFirstName() + " " + savedUser.getLastName()
                                                + " " + savedUser.getOtherName() + "\nAccount Number: " + savedUser.getAccountNumber())
                                        .build();
        emailService.sendEmailAlert(emailDetails);

        
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
                                        .build())
                            .build();


    }
}

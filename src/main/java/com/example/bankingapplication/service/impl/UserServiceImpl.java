package com.example.bankingapplication.service.impl;

import com.example.bankingapplication.dao.UserDao;
import com.example.bankingapplication.dto.*;
import com.example.bankingapplication.entity.User;
import com.example.bankingapplication.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

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

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        //check if the provided account number exists in db
        Boolean isAccountExists = userDao.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!isAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                                .build();
        }

        User foundUser = userDao.findByAccountNumber(enquiryRequest.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNumber(enquiryRequest.getAccountNumber())
                        .accountName(foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName())
                        .build())
                .build();

    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        Boolean isAccountExist = userDao.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!isAccountExist){
            return AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE;
        }
        User foundUser = userDao.findByAccountNumber(enquiryRequest.getAccountNumber());
        return foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName();

        //balance Enquiry,name Enquiry,credit,debit,transfer
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest creditDebitRequest) {
        //chacking if the account exists
        Boolean isAccountExist = userDao.existsByAccountNumber(creditDebitRequest.getAccountNumber());
                if(!isAccountExist){
                    return BankResponse.builder()
                            .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                            .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                            .build();
                }
                User userToCredit= userDao.findByAccountNumber(creditDebitRequest.getAccountNumber());
                userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditDebitRequest.getAmount()));

                //need to save first to db to get latest updated balance
                userDao.save(userToCredit);

                return BankResponse.builder()
                        .responseCode(AccountUtils.TRANSACTION_SUCCESS_CODE)
                        .responseMessage(String.format(AccountUtils.TRANSACTION_SUCCESS_MESSAGE, userToCredit.getAccountBalance()))
                        .accountInfo(AccountInfo.builder()
                                .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName() + " " + userToCredit.getOtherName())
                                .accountBalance(userToCredit.getAccountBalance())
                                .accountNumber(userToCredit.getAccountNumber())
                                    .build())
                        .build();

    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest creditDebitRequest) {
        //check if the account exists
        //check if the amount you intend to withdraw is not more than the current account balance
        boolean isAccountExist = userDao.existsByAccountNumber(creditDebitRequest.getAccountNumber());
                if(!isAccountExist){
                    return BankResponse.builder()
                            .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                            .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                            .accountInfo(null)
                            .build();
                }

                User userToDebit = userDao.findByAccountNumber(creditDebitRequest.getAccountNumber());
                BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = creditDebitRequest.getAmount().toBigInteger();
                if(availableBalance.intValue() < debitAmount.intValue()){
                    return BankResponse.builder()
                            .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                            .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                            .accountInfo(null)
                            .build();
                }else {
                    userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(creditDebitRequest.getAmount()));
                    userDao.save(userToDebit);
                    return BankResponse.builder()
                            .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
                            .responseMessage(AccountUtils.ACCOUNT_DEBITED_MESSAGE)
                            .accountInfo(AccountInfo.builder()
                                    .accountNumber(creditDebitRequest.getAccountNumber())
                                    .accountBalance(userToDebit.getAccountBalance())
                                    .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName() + " " + userToDebit.getOtherName())
                                    .build())
                            .build();
                }


    }

    //balanceEnquiry, name Enquiry, credit, debit,transfer


}

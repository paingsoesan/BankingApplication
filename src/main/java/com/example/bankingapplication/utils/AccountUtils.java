package com.example.bankingapplication.utils;

import com.example.bankingapplication.dao.UserDao;
import com.example.bankingapplication.dto.CreditDebitRequest;
import com.example.bankingapplication.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Year;

public class AccountUtils {

    private UserService userService;

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account";

    public static final String ACCOUNT_CREATION_SUCCESS = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account has been successfully created";

    public static final String ACCOUNT_NOT_EXIST_CODE = "003";
    public static final String ACCOUNT_NOT_EXISTS_MESSAGE = "Account is not exists";

    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_FOUND_MESSAGE = "Account is found";

    public static  final String TRANSACTION_SUCCESS_CODE = "005";
    public static final String TRANSACTION_SUCCESS_MESSAGE = "Your balance is now %s";

    public static final String INSUFFICIENT_BALANCE_CODE = "006";
    public static  final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient balance";

    public static final String ACCOUNT_DEBITED_SUCCESS = "007";
    public static final String ACCOUNT_DEBITED_MESSAGE = "Account has been successfully debited";

    public static final String TRANSFER_SUCCESSFUL_CODE = "008";
    public static final String TRANSFER_SECCESSFUL_MESSAGE  = "Transfer Successful";

    public static String generateAccountNumber(){

        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;

        //generate ramdom number between min and max
        int randomNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);

        //convert the current randomNumber to String, then concat

        String year = String.valueOf(currentYear);
        String ranNumber = String.valueOf(randomNumber);
        StringBuilder accountNumber = new StringBuilder();

        accountNumber.append(year).append(ranNumber);
        return accountNumber.toString();

    }
}

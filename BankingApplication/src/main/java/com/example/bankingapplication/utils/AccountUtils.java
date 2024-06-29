package com.example.bankingapplication.utils;

import java.time.Year;

public class AccountUtils {
    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account";

    public static final String ACCOUNT_CREATION_SUCCESS = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account has been successfully created";

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

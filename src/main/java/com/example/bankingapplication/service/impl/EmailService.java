package com.example.bankingapplication.service.impl;

import com.example.bankingapplication.dto.EmailDetails;

public interface EmailService {

    void sendEmailAlert(EmailDetails emailDetails);

    void sendEmailWithAttachment(EmailDetails emailDetails);
}

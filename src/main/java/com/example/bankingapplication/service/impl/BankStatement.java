package com.example.bankingapplication.service.impl;

import com.example.bankingapplication.dao.TransactionDao;
import com.example.bankingapplication.dao.UserDao;
import com.example.bankingapplication.dto.EmailDetails;
import com.example.bankingapplication.entity.Transaction;
import com.example.bankingapplication.entity.User;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {

    @Autowired
    private TransactionDao transactionDao;
    @Autowired
    private UserDao userDao;

    @Autowired
    private EmailService emailService;

    private static final String FILE = "C:\\Users\\Public\\Documents\\soesan.pdf";

    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) throws FileNotFoundException, DocumentException {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
        List<Transaction> transactionList = transactionDao.findAll().stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> transaction.getCreatedAt() != null && !transaction.getCreatedAt().isBefore(start) && !transaction.getCreatedAt().isAfter(end))
                .collect(Collectors.toList());
        User user = userDao.findByAccountNumber(accountNumber);
        String customerName = user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName();

        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);
        log.info("Setting size of document");
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        PdfPTable bankInfoTable = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell(new Phrase("Java Developer Bank"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.BLACK);
        bankName.setPadding(20f);

        PdfPCell bankAddress = new PdfPCell(new Phrase("72, Some address, Koh Samui"));
        bankAddress.setBorder(0);
        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);

        PdfPTable statementInfo = new PdfPTable(2);
        PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date: " + startDate));
        customerInfo.setBorder(0);
        PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
        statement.setBorder(0);
        PdfPCell stopDate = new PdfPCell(new Phrase("End Date: " + endDate));
        stopDate.setBorder(0);
        PdfPCell name = new PdfPCell(new Phrase("Customer Name: " + customerName));
        name.setBorder(0);
        PdfPCell space = new PdfPCell();
        PdfPCell address = new PdfPCell(new Phrase("Customer Address: " + user.getAddress()));
        address.setBorder(0);

        // Create a table with 4 columns
        PdfPTable transactionTable = new PdfPTable(4);

        // Create and style the header cells
        PdfPCell dateHeader = new PdfPCell(new Phrase("DATE"));
        dateHeader.setBackgroundColor(BaseColor.GREEN);
        dateHeader.setBorder(Rectangle.BOX);

        PdfPCell transactionTypeHeader = new PdfPCell(new Phrase("TRANSACTION TYPE"));
        transactionTypeHeader.setBackgroundColor(BaseColor.BLUE);
        transactionTypeHeader.setBorder(Rectangle.BOX);

        PdfPCell transactionAmountHeader = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
        transactionAmountHeader.setBackgroundColor(BaseColor.BLUE);
        transactionAmountHeader.setBorder(Rectangle.BOX);

        PdfPCell statusHeader = new PdfPCell(new Phrase("STATUS"));
        statusHeader.setBackgroundColor(BaseColor.GREEN);
        statusHeader.setBorder(Rectangle.BOX);

        // Add the header cells to the table
        transactionTable.addCell(dateHeader);
        transactionTable.addCell(transactionTypeHeader);
        transactionTable.addCell(transactionAmountHeader);
        transactionTable.addCell(statusHeader);

        // Add the transaction data rows
        transactionList.forEach(transaction -> {
            // Ensure transaction.getCreatedAt() is not null
            if (transaction.getCreatedAt() != null) {
                PdfPCell dateCell = new PdfPCell(new Phrase(transaction.getCreatedAt().toString()));
                dateCell.setBorder(Rectangle.BOX);
                transactionTable.addCell(dateCell);
            } else {
                PdfPCell dateCell = new PdfPCell(new Phrase("N/A"));
                dateCell.setBorder(Rectangle.BOX);
                transactionTable.addCell(dateCell);
            }

            PdfPCell transactionTypeCell = new PdfPCell(new Phrase(transaction.getTransactionType()));
            transactionTypeCell.setBorder(Rectangle.BOX);
            transactionTable.addCell(transactionTypeCell);

            PdfPCell transactionAmountCell = new PdfPCell(new Phrase(transaction.getAmount().toString()));
            transactionAmountCell.setBorder(Rectangle.BOX);
            transactionTable.addCell(transactionAmountCell);

            PdfPCell statusCell = new PdfPCell(new Phrase(transaction.getStatus()));
            statusCell.setBorder(Rectangle.BOX);
            transactionTable.addCell(statusCell);
        });

        statementInfo.addCell(customerInfo);
        statementInfo.addCell(statement);
        statementInfo.addCell(stopDate);
        statementInfo.addCell(name);
        statementInfo.addCell(space);
        statementInfo.addCell(address);

        document.add(bankInfoTable);
        document.add(statementInfo);
        document.add(transactionTable);

        document.close();

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("STATEMENT OF ACCOUNT")
                .messageBody("Kindly find your requested statement attached!")
                .attachment(FILE)
                .build();

        emailService.sendEmailWithAttachment(emailDetails);

        return transactionList;
    }
}

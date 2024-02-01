package com.kirana.kiranaApplication.controller;

import com.kirana.kiranaApplication.model.Transaction;
import com.kirana.kiranaApplication.service.TransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "Transaction Management")
@Slf4j
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transactions/record")
    @ApiOperation(value = "Get all transactions")
    public ResponseEntity<Transaction> recordTransaction(@RequestBody Transaction transaction) {
        log.info("Process The Transaction::{}",transaction);
        Transaction savedTransaction = transactionService.saveTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction);
    }

    @GetMapping("/transactions/daily-reports")
    @ApiOperation(value = "Get daily reports", notes = "Group transactions for daily reports")
    public ResponseEntity<Map<LocalDate, List<Transaction>>> getDailyReports(@RequestParam String targetCurrency) {
        log.info("Details of The Transaction based upon the target currency::{}",targetCurrency);
        Map<LocalDate, List<Transaction>> dailyReports = transactionService.getDailyReports(targetCurrency);
        return ResponseEntity.ok(dailyReports);
    }
}

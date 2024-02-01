package com.kirana.kiranaApplication.service;

import com.kirana.kiranaApplication.model.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public interface TransactionService {
    Transaction saveTransaction(Transaction transaction);
    Map<LocalDate, List<Transaction>> getDailyReports(String targetCurrency);
    double currentCurrency(double amount, String fromCurrency, String toCurrency);
}

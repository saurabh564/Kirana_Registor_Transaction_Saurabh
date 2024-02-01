package com.kirana.kiranaApplication.impl;

import com.kirana.kiranaApplication.model.CurrencyConversionResponse;
import com.kirana.kiranaApplication.model.Transaction;
import com.kirana.kiranaApplication.repository.TransactionRepository;
import com.kirana.kiranaApplication.service.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ResponseEntity<CurrencyConversionResponse> responseEntity;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveTransaction_WithINRCurrency_ShouldSaveOriginalTransaction() {
        Transaction transaction = new Transaction();
        transaction.setCurrency("INR");
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        Transaction savedTransaction = transactionService.saveTransaction(transaction);
        verify(transactionRepository, times(1)).save(transaction);
        assertEquals(transaction, savedTransaction);
    }

    @Test
    public void saveTransaction_WithUSDCurrency_ShouldSaveOriginalTransaction() {
        Transaction transaction = new Transaction();
        transaction.setCurrency("USD");
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        Transaction savedTransaction = transactionService.saveTransaction(transaction);
        verify(transactionRepository, times(1)).save(transaction);
        assertEquals(transaction, savedTransaction);
    }

    @Test
    public void saveTransaction_WithNonINRAndNonUSDCurrency_ShouldSaveInINRAndUSD() {
        Transaction transaction = new Transaction();
        transaction.setAmount(100);
        transaction.setCurrency("EUR");

        when(restTemplate.getForEntity(anyString(), any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction savedTransaction = invocation.getArgument(0);
            savedTransaction.setId(1L); // Assuming INR transaction has ID 1
            return savedTransaction;
        });
        Transaction savedTransaction = transactionService.saveTransaction(transaction);
        verify(transactionRepository, times(2)).save(any(Transaction.class));
        assertEquals(2, savedTransaction.getId()); // Assuming USD transaction has ID 2
    }

    @Test
    public void getDailyReports_WithEmptyResult() {
        when(transactionRepository.findAll()).thenReturn(Collections.emptyList());
        Map<LocalDate, List<Transaction>> dailyReports = transactionService.getDailyReports("INR");
        verify(transactionRepository, times(1)).findAll();
        assertEquals(0, dailyReports.size());
    }
}
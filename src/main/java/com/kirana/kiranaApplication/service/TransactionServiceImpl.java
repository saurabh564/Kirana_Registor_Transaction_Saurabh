package com.kirana.kiranaApplication.service;

import com.kirana.kiranaApplication.model.CurrencyConversionResponse;
import com.kirana.kiranaApplication.model.Transaction;
import com.kirana.kiranaApplication.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService{
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RestTemplate restTemplate;
    private static final String CURRENCY_API_BASE_URL = "https://api.fxratesapi.com/latest";

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        log.info("Save the Record ::{}::{}",transaction.getCurrency(),transaction.getAmount());
        if (!transaction.getCurrency().equals("INR") && !transaction.getCurrency().equals("USD")) {
            log.info("Convert the Currency into INR ::{}::{}",transaction.getCurrency(),transaction.getAmount());
            double inrAmount = currentCurrency(transaction.getAmount(), transaction.getCurrency(), "INR");
            Transaction inrTransaction = new Transaction(transaction.getId(),transaction.getDateTime(),transaction.getAmount(),transaction.getCurrency(),transaction.getType());
            inrTransaction.setAmount(inrAmount);
            inrTransaction.setCurrency("INR");
            transactionRepository.save(inrTransaction);

            log.info("Convert the Currency into USD ::{}::{}",transaction.getCurrency(),transaction.getAmount());
            double usdAmount = currentCurrency(transaction.getAmount(), transaction.getCurrency(), "USD");
            Transaction usdTransaction = new Transaction(transaction.getId(),transaction.getDateTime(),transaction.getAmount(),transaction.getCurrency(),transaction.getType());
            usdTransaction.setAmount(usdAmount);
            usdTransaction.setCurrency("USD");
            transactionRepository.save(usdTransaction);
        }else{
            transactionRepository.save(transaction);
        }
        return transaction;
    }

    @Override
    public Map<LocalDate, List<Transaction>> getDailyReports(String targetCurrency) {
        log.info("Show Details Based Upon the Group Dates");
        List<Transaction> transactions = transactionRepository.findAll();
        Map<LocalDate, List<Transaction>> dailyReports = new HashMap<>();
        for (Transaction transaction : transactions) {
            LocalDate date = transaction.getDateTime().toLocalDate();

            if (!transaction.getCurrency().equals(targetCurrency)) {
                double convertedAmount = currentCurrency(transaction.getAmount(), transaction.getCurrency(), targetCurrency);
                transaction.setAmount(convertedAmount);
                transaction.setCurrency(targetCurrency);
            }
            dailyReports.computeIfAbsent(date, k -> new ArrayList<>()).add(transaction);
        }
        return dailyReports;
    }

    @Override
    public double currentCurrency(double amount, String fromCurrency, String toCurrency) {
        String apiUrl = String.format("%s?from=%s&to=%s&apikey=<your_api_key>", CURRENCY_API_BASE_URL, fromCurrency, toCurrency);
        log.info("Convert the Currency from the From ::{}::To::{}",fromCurrency,toCurrency);

        ResponseEntity<CurrencyConversionResponse> responseEntity = restTemplate.getForEntity(apiUrl, CurrencyConversionResponse.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            CurrencyConversionResponse response = responseEntity.getBody();
            if (response != null && response.isSuccess()) {
                double exchangeRate = response.getRates().get(toCurrency);
                return amount * exchangeRate;
            } else {
                throw new RuntimeException("Failed to fetch currency conversion rates.");
            }
        } else {
            throw new RuntimeException("Failed to fetch currency conversion rates. HTTP status: " + responseEntity.getStatusCodeValue());
        }
    }
}

package com.kirana.kiranaApplication.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyConversionResponse {
    private boolean success;

    @JsonProperty("rates")
    private Map<String, Double> rates;

}

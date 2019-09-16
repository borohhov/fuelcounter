package com.fuelcounter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface AmountDTO {

    double getAmount();

    @JsonProperty("month")
    String getYearAndMonth();
}

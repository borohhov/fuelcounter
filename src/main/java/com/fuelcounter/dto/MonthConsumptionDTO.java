package com.fuelcounter.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuelcounter.entity.FuelType;

import java.sql.Timestamp;

public interface MonthConsumptionDTO {
    // list fuel consumption records for specified month (each row should contain: fuel type, volume, date, price, total price, driver ID)

    FuelType getFuelType();

    float getVolume();

    @JsonFormat(pattern="yyyy-MM-dd")
    Timestamp getDate();

    float getPrice();

    @JsonProperty("totalPrice")
    float getAmount();

    int getDriverId();


}

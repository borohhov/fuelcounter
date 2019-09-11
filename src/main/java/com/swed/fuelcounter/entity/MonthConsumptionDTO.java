package com.swed.fuelcounter.entity;

import java.sql.Timestamp;

public interface MonthConsumptionDTO {
    // list fuel consumption records for specified month (each row should contain: fuel type, volume, date, price, total price, driver ID)

    FuelType getFuelType();

    float getVolume();

    float getPrice();

    float getAmount();

    Timestamp getDate();

    int getDriverId();


}

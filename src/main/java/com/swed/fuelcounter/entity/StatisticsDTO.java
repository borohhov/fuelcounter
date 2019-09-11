package com.swed.fuelcounter.entity;

public interface StatisticsDTO {
  /* statistics for each month,
   * list fuel consumption records grouped by fuel type
   * (each row should contain: fuel type, volume, average price, total price) */

  FuelType getFuelType();

  float getVolume();

  float getAveragePrice();

  float getAmount();
}

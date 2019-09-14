package com.swed.fuelcounter.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swed.fuelcounter.entity.FuelType;

public interface StatisticsDTO {
  /* statistics for each month,
   * list fuel consumption records grouped by fuel type
   * (each row should contain: fuel type, volume, average price, total price) */

  FuelType getFuelType();

  float getVolume();

  float getAveragePrice();

  float getAmount();

  @JsonIgnore
  String getYearAndMonth();
}

package com.fuelcounter.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class FuelRecordsList {

    @Getter
    @Setter
    @JsonProperty("records")
    List<FuelRecord> records;
}

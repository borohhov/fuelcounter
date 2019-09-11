package com.swed.fuelcounter.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Accessors(chain = true)
@Table(name = "fuel_record")
public class FuelRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private String recordId;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    @Getter
    @Setter
    @NotNull
    private float price;

    @Getter
    @Setter
    @NotNull
    private float volume;

    @Getter
    @Setter
    private Timestamp date;

    @Getter
    @Setter
    private int driverId;

    @Transient // needed for AmountDTO grouping
    @Getter
    @Setter
    @JsonIgnore(true)
    private String yearAndMonth;

    @Transient // needed for StatisticsDTO grouping
    @Getter
    @Setter
    @JsonIgnore(true)
    private float averagePrice;
}

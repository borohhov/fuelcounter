package com.fuelcounter.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Objects;

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
    @JsonFormat(pattern="yyyy-MM-dd")
    private Timestamp date;

    @Getter
    @Setter
    private int driverId;

    @Transient // needed for AmountDTO grouping
    @Getter
    @Setter
    @JsonIgnore
    private String yearAndMonth;

    @Transient // needed for StatisticsDTO grouping
    @Getter
    @Setter
    @JsonIgnore
    private float averagePrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FuelRecord that = (FuelRecord) o;
        return Float.compare(that.price, price) == 0 &&
                Float.compare(that.volume, volume) == 0 &&
                driverId == that.driverId &&
                fuelType == that.fuelType &&
                date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fuelType, price, volume, date, driverId);
    }
}

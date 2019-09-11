package com.swed.fuelcounter.entity;



import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.sql.Date;

// Transient entity to allow custom query generation with JPA
//@Entity
public class Amount {

    public Amount(double amount, Date date){
        this.amount = amount;
        this.date = date;
    }

    @Id
    @Getter
    @SuppressWarnings("unused")
    private String id;


    @Getter
    @Setter
    private double amount;

    @Getter
    @Setter
    private Date date;
}

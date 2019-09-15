package com.fuelcounter.helper;

import org.junit.Test;

import java.sql.Date;
import java.sql.Timestamp;

import static org.junit.Assert.*;

public class DateConversionTest {

    @Test
    public void convertDateFormat() {
        assertEquals(Timestamp.valueOf("2019-01-02 12:00:00"), DateConversion.convertDateFormat("01.02.2019"));
    }

    @Test
    public void stringToDate() {
        assertEquals(Date.valueOf("2019-01-02"), DateConversion.stringToDate("2019-01-02"));
    }
}
package com.fuelcounter.entity;

import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.*;

public class FuelRecordTest {

    private FuelRecord record;
    public FuelRecordTest(){
        record = new FuelRecord();
        record.setFuelType(FuelType.DIESEL).setDate(Timestamp.valueOf("2019-09-15 00:00:00.553955473")).setPrice(10.1f).setVolume(10.2f).setDriverId(122);
    }


    @Test
    public void getFuelType() {
        assertEquals(FuelType.DIESEL, record.getFuelType());
        assertNotEquals(FuelType.PETROL_95, record.getFuelType());
    }

    @Test
    public void getPrice() {
        assertEquals(10.1f, record.getPrice(), 0.0);
    }

    @Test
    public void getVolume() {
        assertEquals(10.2f, record.getVolume(), 0.0);
    }

    @Test
    public void getDate() {
        assertEquals(Timestamp.valueOf("2019-09-15 00:00:00.553955473"), record.getDate());
    }

    @Test
    public void getDriverId() {
        assertEquals(122, record.getDriverId());
    }

}
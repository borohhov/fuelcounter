package com.fuelcounter.persistence;


import com.fuelcounter.dto.AmountDTO;
import com.fuelcounter.entity.FuelRecord;
import com.fuelcounter.entity.FuelType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@DataJpaTest
public class FuelRecordJpaRepositoryTest {

    @Autowired
    FuelRecordJpaRepository repository;

    private static final int driverId = 123103515;


    private List<FuelRecord> initialData(){
        List<FuelRecord> records = new ArrayList<>(2);
        records.add(new FuelRecord().setDate(Timestamp.valueOf("2019-01-02 12:00:00")).setPrice(10.0f).setVolume(20.5f).setFuelType(FuelType.DIESEL).setDriverId(driverId));
        records.add(new FuelRecord().setDate(Timestamp.valueOf("2019-01-06 12:00:00")).setPrice(10.0f).setVolume(20.5f).setFuelType(FuelType.PETROL_95).setDriverId(driverId));
        return records;
    }

    @Test
    public void findByRecordId() {
        List<FuelRecord> records = initialData();
        List<FuelRecord> databaseRecords = repository.saveAll(records);

        assertEquals(records.get(1), repository.findByRecordId(databaseRecords.get(1).getRecordId()));


    }


    @Test
    public void deleteByRecordId() {
        repository.saveAll(initialData());
        repository.deleteByRecordId("1");
        assertEquals(2, repository.findAll().size());
    }

    @Test
    public void sumAmountByMonth() {
        repository.saveAll(initialData());
        List<AmountDTO> amounts = repository.sumAmountByMonth();
        assertEquals(1, amounts.size());
        assertEquals("2019-01", amounts.get(0).getYearAndMonth());
        assertEquals(410f, amounts.get(0).getAmount(),0.0f);
    }

    @Test
    public void sumAmountByMonthAndDriver() {
        repository.saveAll(initialData());
        List<AmountDTO> amounts = repository.sumAmountByMonthAndDriver(driverId);
        assertEquals(1, amounts.size());
        assertEquals("2019-01", amounts.get(0).getYearAndMonth());
        assertEquals(410f, amounts.get(0).getAmount(),0.0f);

    }

    @Test
    public void getConsumptionRecordsByMonth() {
        repository.saveAll(initialData());
        assertEquals(2, repository.getConsumptionRecordsByMonth(new Date(Timestamp.valueOf("2019-01-06 12:00:00").getTime())).size());

    }

    @Test
    public void statsByMonth() {
        repository.saveAll(initialData());
        assertEquals(2, repository.statsByMonth().size());
    }
}
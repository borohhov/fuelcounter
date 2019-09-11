package com.swed.fuelcounter.service;

import com.swed.fuelcounter.entity.*;
import com.swed.fuelcounter.helper.DateConversion;
import com.swed.fuelcounter.persistence.FuelRecordJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.sql.Timestamp;
import java.util.List;


@RestController
@RequestMapping(value = "/rest/")
class FuelRecordsService {
    private final FuelRecordJpaRepository repository;

    public FuelRecordsService(FuelRecordJpaRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/{recordId}", method = RequestMethod.GET)
    public ResponseEntity<FuelRecord> getRecordById(@PathVariable("recordId") String recordId) {
        FuelRecord rec = repository.findByRecordId(recordId);
        return new ResponseEntity<>(rec, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/{recordId}", method = RequestMethod.DELETE)
    public void deleteByRecordId(@PathVariable("recordId") String recordId) {
        repository.deleteByRecordId(recordId);
    }

    @RequestMapping(value = "/{driverId}/records", method = RequestMethod.GET)
    public ResponseEntity<List<FuelRecord>> getRecordsByDriverId(@PathVariable("driverId") int driverId) {
        List<FuelRecord> recs = repository.findAllByDriverId(driverId);
        return new ResponseEntity<>(recs, HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public FuelRecord addFuelRecord(
            @RequestParam(value = "driverId") int driverId,
            @RequestParam(value = "fuelType") FuelType fuelType,
            @RequestParam(value = "price") float price,
            @RequestParam(value = "volume") float volume,
            @RequestParam(value = "date") String date) {


        FuelRecord fuelRecord = new FuelRecord().setDriverId(driverId).setFuelType(fuelType).setPrice(price).setVolume(volume).setDate(DateConversion.convertDate(date));
        repository.save(fuelRecord);
        return fuelRecord;
    }

    @RequestMapping(value = "/amount-by-month", method = RequestMethod.GET)
    public ResponseEntity<List<AmountDTO>> getAmountByMonth() {
        List<AmountDTO> amounts = repository.sumAmountByMonth();
        return new ResponseEntity<>(amounts, HttpStatus.OK);
    }

    @RequestMapping(value = "/{driverId}/amount-by-month", method = RequestMethod.GET)
    public ResponseEntity<List<AmountDTO>> getAmountByMonth(@PathVariable("driverId") int driverId) {
        List<AmountDTO> amounts = repository.sumAmountByMonthAndDriver(driverId);
        return new ResponseEntity<>(amounts, HttpStatus.OK);
    }

    @RequestMapping(value = "/records-by-month/{month}", method = RequestMethod.GET)
    public ResponseEntity<List<MonthConsumptionDTO>> getConsumptionRecordsByMonth(@PathVariable("month") String yearAndMonth) {
        String ym = yearAndMonth + "-02";
        List<MonthConsumptionDTO> records = repository.getConsumptionRecordsByMonth(DateConversion.stringToDate(ym));
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @RequestMapping(value = "/{driverId}/records-by-month/{month}", method = RequestMethod.GET)
    public ResponseEntity<List<MonthConsumptionDTO>> getConsumptionRecordsByMonth(
            @PathVariable("driverId") int driverId,
            @PathVariable("month") String yearAndMonth) {

        String ym = yearAndMonth + "-02";
        List<MonthConsumptionDTO> records = repository.getConsumptionRecordsByMonth(driverId, DateConversion.stringToDate(ym));
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public ResponseEntity<List<StatisticsDTO>> getConsumptionRecordsByMonth() {
        List<StatisticsDTO> records = repository.statsByMonth();
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @RequestMapping(value = "/{driverId}/statistics", method = RequestMethod.GET)
    public ResponseEntity<List<StatisticsDTO>> getConsumptionRecordsByMonth(
            @PathVariable("driverId") int driverId) {
        List<StatisticsDTO> records = repository.statsByMonthAndDriver(driverId);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }
}
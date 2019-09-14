package com.swed.fuelcounter.controller;

import com.swed.fuelcounter.dto.AmountDTO;
import com.swed.fuelcounter.dto.MonthConsumptionDTO;
import com.swed.fuelcounter.dto.StatisticsDTO;
import com.swed.fuelcounter.entity.*;
import com.swed.fuelcounter.helper.DateConversion;
import com.swed.fuelcounter.helper.ResponseChecker;
import com.swed.fuelcounter.persistence.FuelRecordJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.security.InvalidParameterException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/rest/")
class FuelRecordsController {
    private final FuelRecordJpaRepository repository;

    public FuelRecordsController(FuelRecordJpaRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/{recordId}", method = RequestMethod.GET)
    public ResponseEntity<FuelRecord> getRecordById(@PathVariable("recordId") String recordId) {
        FuelRecord rec = repository.findByRecordId(recordId);
        if(rec == null) return new ResponseEntity<>(rec, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Transactional
    @RequestMapping(value = "/{recordId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteByRecordId(@PathVariable("recordId") String recordId) {
        ResponseEntity<String> response;
        if (repository.findByRecordId(recordId) != null) {
            repository.deleteByRecordId(recordId);
            response = new ResponseEntity<>("Deleted", HttpStatus.OK);
        } else {
            response = new ResponseEntity<>("Could not find record #" + recordId, HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @RequestMapping(value = "/{driverId}/records", method = RequestMethod.GET)
    public ResponseEntity getRecordsByDriverId(@PathVariable("driverId") int driverId) {
        List<FuelRecord> records = repository.findAllByDriverId(driverId);
        return ResponseChecker.validateResponse(String.valueOf(driverId), records);
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

    @RequestMapping(value = "/bulk", method = RequestMethod.POST)
    public ResponseEntity<String> bulkAddFuelRecord(
            @RequestBody FuelRecordsList records) {
        repository.saveAll(records.getRecords());
        return new ResponseEntity<>("Added " + records.getRecords().size() + " records", HttpStatus.OK);
    }

    @RequestMapping(value = "/amount-by-month", method = RequestMethod.GET)
    public ResponseEntity getAmountByMonth() {
        List<AmountDTO> amounts = repository.sumAmountByMonth();
        return ResponseChecker.validateResponse("", amounts);
    }

    @RequestMapping(value = "/{driverId}/amount-by-month", method = RequestMethod.GET)
    public ResponseEntity<List<AmountDTO>> getAmountByMonth(@PathVariable("driverId") int driverId) {
        List<AmountDTO> amounts = repository.sumAmountByMonthAndDriver(driverId);
        return new ResponseEntity<>(amounts, HttpStatus.OK);
    }

    @RequestMapping(value = "/records-by-month/{month}", method = RequestMethod.GET)
    public ResponseEntity getConsumptionRecordsByMonth(@PathVariable("month") String yearAndMonth) {
        //Convert YYYY-MM to YYYY-MM-DD so that it looks like a date to the database
        String ym = yearAndMonth + "-02";
        Date date;
        try {
            date = DateConversion.stringToDate(ym);
        } catch (DateTimeParseException ex) {
            return new ResponseEntity<>(
                    "Invalid parameter '" + yearAndMonth + "'. The parameter must be formatted 'YYYY-MM', example: 2019-09",
                    HttpStatus.BAD_REQUEST);
        }
        List<MonthConsumptionDTO> records = repository.getConsumptionRecordsByMonth(date);
        return ResponseChecker.validateResponse(yearAndMonth, records);
    }

    @RequestMapping(value = "/{driverId}/records-by-month/{month}", method = RequestMethod.GET)
    public ResponseEntity getConsumptionRecordsByMonth(
            @PathVariable("driverId") int driverId,
            @PathVariable("month") String yearAndMonth) {
        //Convert YYYY-MM to YYYY-MM-DD so that it looks like a date to the database
        String ym = yearAndMonth + "-02";
        Date date;
        try {
            date = DateConversion.stringToDate(ym);
        } catch (DateTimeParseException ex) {
            return new ResponseEntity<>(
                    "Invalid parameter '" + yearAndMonth + "'. The parameter must be formatted 'YYYY-MM', example: 2019-09",
                    HttpStatus.BAD_REQUEST);
        }
        List<MonthConsumptionDTO> records = repository.getConsumptionRecordsByMonth(driverId, date);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public ResponseEntity<Map<String, List<StatisticsDTO>>> getConsumptionRecordsByMonth() {
        Map<String, List<StatisticsDTO>> map = new LinkedHashMap<>();
        List<StatisticsDTO> records = repository.statsByMonth();
        List<String> yearAndMonths = records.stream().map(StatisticsDTO::getYearAndMonth).collect(Collectors.toList());
        yearAndMonths.forEach(ym -> map.put(ym, records.stream().filter(record -> record.getYearAndMonth().equals(ym)).collect(Collectors.toList())));
        LinkedHashMap<String, List<StatisticsDTO>> sortedMap = new LinkedHashMap<>();
        map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(row -> sortedMap.put(row.getKey(), row.getValue()));
        return new ResponseEntity<>(sortedMap, HttpStatus.OK);
    }

    @RequestMapping(value = "/{driverId}/statistics", method = RequestMethod.GET)
    public ResponseEntity<Map<String, StatisticsDTO>> getConsumptionRecordsByMonth(@PathVariable("driverId") int driverId) {
        Map<String, StatisticsDTO> map = new HashMap<>();
        List<StatisticsDTO> records = repository.statsByMonthAndDriver(driverId);
        records.forEach(record -> map.put(record.getYearAndMonth(), record));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
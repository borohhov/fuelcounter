package com.fuelcounter.controller;

import com.fuelcounter.dto.AmountDTO;
import com.fuelcounter.dto.MonthConsumptionDTO;
import com.fuelcounter.dto.StatisticsDTO;
import com.fuelcounter.entity.FuelRecord;
import com.fuelcounter.entity.FuelRecordsList;
import com.fuelcounter.entity.FuelType;
import com.fuelcounter.helper.DateConversion;
import com.fuelcounter.helper.ResponseChecker;
import com.fuelcounter.persistence.FuelRecordJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.sql.Date;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/")
class FuelRecordsController {

    @Autowired
    private FuelRecordJpaRepository repository;

    @Transactional
    @RequestMapping(value = "/records/{recordId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteByRecordId(@PathVariable("recordId") String recordId) {
        ResponseEntity<String> response;
        if (repository.findByRecordId(recordId) != null) {
            repository.deleteByRecordId(recordId);
            response = new ResponseEntity<>(HttpStatus.ACCEPTED);
        } else {
            response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return response;
    }

    @RequestMapping(value = "/records", method = RequestMethod.POST)
    public ResponseEntity<HashMap> addFuelRecord(
            @RequestParam(value = "driverId") int driverId,
            @RequestParam(value = "fuelType") FuelType fuelType,
            @RequestParam(value = "price") float price,
            @RequestParam(value = "volume") float volume,
            @RequestParam(value = "date") String date) {


        FuelRecord fuelRecord = new FuelRecord().setDriverId(driverId).setFuelType(fuelType).setPrice(price).setVolume(volume).setDate(DateConversion.convertDateFormat(date));
        repository.save(fuelRecord);
        HashMap<String, String> map = new HashMap<>();
        map.put("id", fuelRecord.getRecordId());
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/records/bulk", method = RequestMethod.POST)
    public ResponseEntity<HashMap> bulkAddFuelRecord(
            @RequestBody FuelRecordsList records) {
        repository.saveAll(records.getRecords());
        HashMap<String, List<String>> map = new HashMap<>();
        List<String> ids = new ArrayList<>();
        records.getRecords().forEach(record -> ids.add(record.getRecordId()));
        map.put("id", ids);

        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/amount-by-month", method = RequestMethod.GET)
    public ResponseEntity getAmountByMonth() {
        List<AmountDTO> amounts = repository.sumAmountByMonth();
        return ResponseChecker.validateResponse(amounts);
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
        return ResponseChecker.validateResponse(records);
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
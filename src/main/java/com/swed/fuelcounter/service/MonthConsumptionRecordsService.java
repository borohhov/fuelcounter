package com.swed.fuelcounter.service;

import com.swed.fuelcounter.entity.MonthConsumptionDTO;
import com.swed.fuelcounter.helper.DateConversion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
@RestController
@RequestMapping(value = "/rest")
class MonthConsumptionRecordsService {
    private final MonthConsumptionRecordRepository repository;

    public MonthConsumptionRecordsService(MonthConsumptionRecordRepository repository) {
        this.repository = repository;
    }





}**/
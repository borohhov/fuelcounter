package com.fuelcounter.helper;

import com.fuelcounter.entity.FuelRecord;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ResponseCheckerTest {

    @Test
    public void validateResponse() {
        List<FuelRecord> records = new ArrayList<>(1);

        assertEquals(HttpStatus.NO_CONTENT,ResponseChecker.validateResponse(records).getStatusCode());

        records.add(new FuelRecord());
        assertEquals(HttpStatus.OK,ResponseChecker.validateResponse(records).getStatusCode());
    }
}
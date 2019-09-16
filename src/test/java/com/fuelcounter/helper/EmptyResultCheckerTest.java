package com.fuelcounter.helper;

import com.fuelcounter.entity.FuelRecord;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class EmptyResultCheckerTest {

    @Test
    public void validateResponse() {
        List<FuelRecord> records = new ArrayList<>(1);

        assertEquals(HttpStatus.NO_CONTENT, EmptyResultChecker.validateResponse(records).getStatusCode());

        records.add(new FuelRecord());
        assertEquals(HttpStatus.OK, EmptyResultChecker.validateResponse(records).getStatusCode());
    }
}
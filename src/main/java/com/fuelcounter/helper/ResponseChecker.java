package com.fuelcounter.helper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseChecker {

    public static <T> ResponseEntity validateResponse(List<T> recordsList){
        ResponseEntity responseEntity;
        if(recordsList == null || recordsList.size() == 0){
            responseEntity = new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        }

        else {
            responseEntity = new ResponseEntity<>(recordsList, HttpStatus.OK);
        }
        return responseEntity;
    }
}

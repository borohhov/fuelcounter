package com.fuelcounter.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Controller
public class ExceptionController implements ErrorController {
    private final static String PATH = "/error";

    @Override
    public String getErrorPath() {
        return PATH;
    }

    @RequestMapping(value=PATH, produces="application/json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> handle(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Exception exception = (Exception) request.getAttribute("org.springframework.boot.web.servlet.error.DefaultErrorAttributes.ERROR");
        map.put("timestamp", LocalDateTime.now());
        map.put("status", request.getAttribute("javax.servlet.error.status_code"));
        map.put("reason", exception.getLocalizedMessage());
        // Validations for creating FuelRecord
        if(exception.getLocalizedMessage()
                .contains("Failed to convert value of type 'java.lang.String' to required type 'com.fuelcounter.entity.FuelType'")){
            map.put("analysis", "Valid FuelType: PETROL_95, PETROL_98, DIESEL");
        }
        map.put("help", "Single record POST /rest/record must have the following URI parameters: " +
                "/rest/records?driverId=INTEGER&price=FLOAT&volume=FLOAT&date=MM.DD.YYYY&fuelType=PETROL_98/PETROL_95/DIESEL " +
                "Example:" +
                "/rest/records?driverId=1234&price=15&volume=20&date=12.11.2010&fuelType=PETROL_98" +
                "Multiple entry POST /rest/records must have the JSON string in the request body like the example:" +
                "{\"records\":[{\"fuelType\": \"PETROL_95\",\"price\": 15,\"volume\": 22,\"date\": \"2015-12-10\",\"driverId\": 1234},{\"fuelType\": \"PETROL_95\",\"price\": 15.3,\"volume\": 20.9,\"date\": \"2012-12-0\",\"driverId\": 1234}]}");
        return map;
    }

}

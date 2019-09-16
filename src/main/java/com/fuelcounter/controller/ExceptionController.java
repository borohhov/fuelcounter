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
        return map;
    }

}

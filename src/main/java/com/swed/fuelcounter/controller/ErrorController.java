package com.swed.fuelcounter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ErrorController {

    @RequestMapping(value="/error", produces="application/json")
    @ResponseBody
    public Map<String, Object> handle(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", request.getAttribute("javax.servlet.error.status_code"));
        map.put("reason", request.getAttribute("javax.servlet.error.message"));
        // Validations for creating FuelRecord
        if(((String) request.getAttribute("javax.servlet.error.message"))
                .contains("JSON parse error: Cannot deserialize value of type `com.swed.fuelcounter.entity.FuelType`")){
            map.put("analysis", "One or more invalid records");
        }
        return map;
    }



}

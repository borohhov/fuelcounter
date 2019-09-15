package com.fuelcounter.controller;

import com.fuelcounter.FuelCounterApplication;
import com.fuelcounter.entity.FuelRecord;
import com.fuelcounter.persistence.FuelRecordJpaRepository;
import com.sun.jndi.toolkit.url.Uri;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.MalformedURLException;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FuelCounterApplication.class)
//@WebMvcTest(FuelRecordsController.class)
public class FuelRecordsControllerTest {

    private MockMvc mockMvc;


    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).dispatchOptions(true).build();
    }

    @Test
    public void addFuelRecord() {
        try {
            mockMvc.perform(post("/rest/records?driverId=1234&price=15&volume=20&date=12.11.2010&fuelType=PETROL_98")
                    .contextPath("/rest")
                    .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void deleteByRecordId() {
        try {
            mockMvc.perform(delete("/rest/records/1")
                    .contextPath("/rest")
                    .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                    .andExpect(status().isAccepted());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Test
    public void bulkAddFuelRecord() {
        String bulkAdd = "{\n" +
                "    \"records\": [\n" +
                "        {\n" +
                "            \"fuelType\": \"PETROL_95\",\n" +
                "            \"price\": 15,\n" +
                "            \"volume\": 20,\n" +
                "            \"date\": \"2015-12-10\",\n" +
                "            \"driverId\": 1234\n" +
                "        },\n" +
                "        {\n" +
                "            \"fuelType\": \"PETROL_95\",\n" +
                "            \"price\": 15.3,\n" +
                "            \"volume\": 20.9,\n" +
                "            \"date\": \"2012-12-11\",\n" +
                "            \"driverId\": 1234\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        try {
            mockMvc.perform(post("/rest/records/bulk")
                    .contextPath("/rest")
                    .contentType("application/json")
                    .content(bulkAdd)
                    .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAmountByMonth() {
        String expectedResponse = "[{\"yearAndMonth\":\"2020-11\",\"amount\":780.0},{\"yearAndMonth\":\"2021-12\",\"amount\":320.0}]";
        try {
            mockMvc.perform(post("/rest/records?driverId=12345&price=12&volume=20&date=11.11.2020&fuelType=DIESEL")
                    .contextPath("/rest")
                    .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                    .andExpect(status().isCreated());
            mockMvc.perform(post("/rest/records?driverId=123&price=12&volume=25&date=11.12.2020&fuelType=DIESEL")
                    .contextPath("/rest")
                    .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                    .andExpect(status().isCreated());
            mockMvc.perform(post("/rest/records?driverId=123&price=12&volume=20&date=11.14.2020&fuelType=PETROL_95")
                    .contextPath("/rest")
                    .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                    .andExpect(status().isCreated());
            mockMvc.perform(post("/rest/records?driverId=123&price=16&volume=20&date=12.11.2021&fuelType=DIESEL")
                    .contextPath("/rest")
                    .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                    .andExpect(status().isCreated());
            mockMvc.perform(get("/rest/amount-by-month")
                    .contextPath("/rest")
                    .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                    .andExpect(status().isOk())
                    .andExpect(content().string(expectedResponse));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Test
    public void getConsumptionRecordsByMonth() {
        String expectedResponse = "[{\"date\":\"2020-11-12\",\"driverId\":123,\"volume\":25.0,\"fuelType\":\"DIESEL\",\"price\":12.0,\"amount\":300.0},{\"date\":\"2020-11-14\",\"driverId\":123,\"volume\":20.0,\"fuelType\":\"PETROL_95\",\"price\":12.0,\"amount\":240.0},{\"date\":\"2020-11-11\",\"driverId\":12345,\"volume\":20.0,\"fuelType\":\"DIESEL\",\"price\":12.0,\"amount\":240.0},{\"date\":\"2020-11-12\",\"driverId\":123,\"volume\":25.0,\"fuelType\":\"DIESEL\",\"price\":12.0,\"amount\":300.0},{\"date\":\"2020-11-14\",\"driverId\":123,\"volume\":20.0,\"fuelType\":\"PETROL_95\",\"price\":12.0,\"amount\":240.0}]";
        try {
            mockMvc.perform(post("/rest/records?driverId=12345&price=12&volume=20&date=11.11.2020&fuelType=DIESEL")
                    .contextPath("/rest")
                    .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                    .andExpect(status().isCreated());
            mockMvc.perform(post("/rest/records?driverId=123&price=12&volume=25&date=11.12.2020&fuelType=DIESEL")
                    .contextPath("/rest")
                    .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                    .andExpect(status().isCreated());
            mockMvc.perform(post("/rest/records?driverId=123&price=12&volume=20&date=11.14.2020&fuelType=PETROL_95")
                    .contextPath("/rest")
                    .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                    .andExpect(status().isCreated());
            mockMvc.perform(post("/rest/records?driverId=123&price=16&volume=20&date=12.11.2021&fuelType=DIESEL")
                    .contextPath("/rest")
                    .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                    .andExpect(status().isCreated());
            mockMvc.perform(get("/rest/records-by-month/2020-11")
                    .contextPath("/rest")
                    .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                    .andExpect(status().isOk())
                    .andExpect(content().string(expectedResponse));
            mockMvc.perform(get("/rest/records-by-month/2020-05")
                    .contextPath("/rest")
                    .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                    .andExpect(status().isNoContent());
            mockMvc.perform(get("/rest/records-by-month/2020-3")
                    .contextPath("/rest")
                    .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
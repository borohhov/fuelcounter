package com.fuelcounter.helper;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class DateConversion {

    public static Timestamp convertDateFormat(String date) throws DateTimeParseException {
        //Incoming date is mm.dd.yyyy, we need to translate it for the Date class
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("MM.dd.uuuu");
        LocalDate dateNew = LocalDate.parse(date, formatter);
        // Need to use java.util.Date because JPA might not support LocalDate. Set time to 12pm to avoid issues with timezones
        return Timestamp.valueOf(dateNew.atTime(12,0));
    }

    public static Date stringToDate(String date) throws DateTimeParseException {
        //Incoming date is mm.dd.yyyy, we need to translate it for the Date class
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        LocalDate dateNew = LocalDate.parse(date, formatter);
        // Need to use java.util.Date because JPA might not support LocalDate. Set time to 12pm to avoid issues with timezones
        return Date.valueOf(dateNew);
    }
}

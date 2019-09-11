package com.swed.fuelcounter.persistence;


import com.swed.fuelcounter.entity.MonthConsumptionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.sql.Date;
import java.util.List;
/**
//@Component
public class MonthConsumptionRecordRepositoryImpl {

  //  @PersistenceContext
    private EntityManager entityManager;

    //@Autowired
    private MonthConsumptionRecordRepository repository;





    @SuppressWarnings("unused")
    public  List<MonthConsumptionDTO> getConsumptionRecordsByMonth(Date date){
        //FuelType fuelType, float volume, float price, Amount amount, int driverId
        int month = date.toLocalDate().getMonthValue();
        int year = date.toLocalDate().getYear();
        String hql = "SELECT NEW MonthConsumptionDTO(fuelType, volume, price, (price*volume) as amount, date, driverId) " +
                "FROM FuelRecord " +
                "WHERE Month(date) = :month AND Year(date) = :year" ;
        TypedQuery<MonthConsumptionDTO> query = entityManager.createQuery(hql, MonthConsumptionDTO.class);
        query.setParameter("month", month);
        query.setParameter("year", year);
        return query.getResultList();
    }

    @SuppressWarnings("unused")
    public  List<MonthConsumptionDTO> getConsumptionRecordsByMonth(int driverId, Date date){ //TODO: get yyyy-mm instead of yyyy-mm-dd
        //FuelType fuelType, float volume, float price, Amount amount, int driverId
        int month = date.toLocalDate().getMonthValue();
        int year = date.toLocalDate().getYear();
        String hql = "SELECT NEW MonthConsumptionDTO(fuelType, volume, price, (price*volume) as amount, date, driverId) " +
                "FROM FuelRecord " +
                "WHERE Month(date) = :month AND Year(date) = :year AND driverId = :driverId" ;
        TypedQuery<MonthConsumptionDTO> query = entityManager.createQuery(hql, MonthConsumptionDTO.class);
        query.setParameter("month", month);
        query.setParameter("year", year);
        query.setParameter("driverId", driverId);
        return query.getResultList();
    }





}**/
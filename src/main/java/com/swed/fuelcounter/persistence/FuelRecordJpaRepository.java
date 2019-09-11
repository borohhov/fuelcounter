package com.swed.fuelcounter.persistence;

import com.swed.fuelcounter.entity.AmountDTO;
import com.swed.fuelcounter.entity.FuelRecord;
import com.swed.fuelcounter.entity.MonthConsumptionDTO;
import com.swed.fuelcounter.entity.StatisticsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface FuelRecordJpaRepository extends JpaRepository<FuelRecord, Long> {

    FuelRecord findByRecordId(String recordId);

    List<FuelRecord> findAllByDriverId(int driverId);

    void deleteByRecordId(String recordId);

    // Using Projections with custom SQL for non-entity requests

    @Query(value = "SELECT SUM(amount) AS amount, yearAndMonth FROM (SELECT price*volume AS amount, CONCAT(YEAR(date),'-', LPAD(MONTH(date), 2, '0')) AS yearANdMonth FROM fuel_record) \n" +
            "GROUP BY yearAndMonth ORDER BY yearAndMonth ", nativeQuery = true)
    List<AmountDTO> sumAmountByMonth();

    @Query(value = "SELECT SUM(amount) AS amount, yearAndMonth FROM (SELECT price*volume AS amount, CONCAT(YEAR(date),'-', LPAD(MONTH(date), 2, '0')) AS yearANdMonth FROM fuel_record WHERE driver_id = :driverId ) \n" +
            "GROUP BY yearAndMonth ORDER BY yearAndMonth ", nativeQuery = true)
    List<AmountDTO> sumAmountByMonthAndDriver(@Param("driverId") int driverId);


    @Query(value = "SELECT fuel_type AS fuelType, volume, price, (price*volume) AS amount, date, driver_id AS driverId FROM fuel_record WHERE Month(date) = Month(:monthAndYear) AND Year(date) = YEAR(:monthAndYear)", nativeQuery = true)
    List<MonthConsumptionDTO> getConsumptionRecordsByMonth(Date monthAndYear);

    @Query(value = "SELECT fuel_type AS fuelType,  volume, price, (price*volume) AS amount, date, driver_id AS driverId FROM fuel_record WHERE Month(date) = Month(:monthAndYear) AND Year(date) = YEAR(:monthAndYear) AND driver_id = :driverId", nativeQuery = true)
    List<MonthConsumptionDTO> getConsumptionRecordsByMonth(@Param("driverId") int driverId, @Param("monthAndYear") Date monthAndYear);

    @Query(value = "SELECT fuelType, SUM(volume) AS volume, AVG(price) AS averagePrice,  SUM(amount) AS amount, yearAndMonth FROM (SELECT fuel_type as fuelType, price, volume, price*volume AS amount, driver_id as driverId, CONCAT(YEAR(date),'-', LPAD(MONTH(date), 2, '0')) AS yearANdMonth FROM fuel_record ) GROUP BY fuelType, yearAndMonth", nativeQuery = true)
    List<StatisticsDTO> statsByMonth();

    @Query(value = "SELECT fuelType, SUM(volume) AS volume, AVG(price) AS averagePrice,  SUM(amount) AS amount, yearAndMonth FROM (SELECT fuel_type as fuelType, price, volume, price*volume AS amount, driver_id as driverId, CONCAT(YEAR(date),'-', LPAD(MONTH(date), 2, '0')) AS yearANdMonth FROM fuel_record WHERE driver_id = :driverId) GROUP_BY fuelType, yearAndMonth", nativeQuery = true)
    List<StatisticsDTO> statsByMonthAndDriver(@Param("driverId") int driverId);
}

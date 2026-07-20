package com.store.sales_api.sale;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ISaleRepository extends JpaRepository<Sale, Long>{

    Optional<Sale> findByCode(String code);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.date = :date")
    Integer getDaySalesCount(@Param("date") LocalDate date);

    @Query("SELECT SUM(s.totalAmount) FROM Sale s WHERE s.date = :date")
    BigDecimal getDayTotalAmount(@Param("date") LocalDate date);

    Optional<Sale> findTopByOrderByTotalAmountDesc();
}

package com.store.sales_api.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.store.sales_api.model.Sale;


public interface ISaleRepository extends JpaRepository<Sale, Long>{

    Optional<Sale> findByCode(String code);

    Long countByDate(LocalDate date);
}

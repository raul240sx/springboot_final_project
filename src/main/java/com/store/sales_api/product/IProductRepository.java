package com.store.sales_api.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface IProductRepository extends JpaRepository<Product, Long>{

    Optional<Product> findByCode(String code);

    List<Product> findAllByCodeIn(List<String> codes);

    List<Product> findByStockLessThan(Integer stock);

}

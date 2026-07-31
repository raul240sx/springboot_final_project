package rrs.ms_sales.model;

import java.math.BigDecimal;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter @Setter
public class Detail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne  
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    private String productCode;

    private Integer quantity = 1;
    private BigDecimal partialAmount = new BigDecimal(0.00);

    
    
    public Detail() {
    }


    public Detail(Sale sale, String productCode, Integer quantity, BigDecimal partialAmount) {
        this.sale = sale;
        this.productCode = productCode;
        this.quantity = quantity;
        this.partialAmount = partialAmount;
    }

}

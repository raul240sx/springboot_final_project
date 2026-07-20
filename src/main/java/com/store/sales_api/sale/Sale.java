package com.store.sales_api.sale;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.store.sales_api.client.Client;
import com.store.sales_api.security.Vendor;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;


@Entity
@SQLDelete(sql = "UPDATE sale SET is_active=false WHERE id=?")
@SQLRestriction("is_active=true")
@Getter @Setter
public class Sale {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private LocalDate date;
    private BigDecimal totalAmount = new BigDecimal(0.00);
    private Boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Detail> details = new ArrayList<>();



    public Sale() {
    }


    public Sale(String code, LocalDate date, BigDecimal totalAmount, Client client, Vendor vendor) {
        this.code = code;
        this.date = date;
        this.totalAmount = totalAmount;
        this.client = client;
        this.vendor = vendor;
    }


    public void addDetail(Detail detail) {
        this.details.add(detail);
        detail.setSale(this);
    }

}

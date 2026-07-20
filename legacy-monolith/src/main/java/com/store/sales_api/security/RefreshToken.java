package com.store.sales_api.security;

import java.time.Instant;

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
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    private Instant expiryDate;



    public RefreshToken() {
    }

    public RefreshToken(String token, Vendor vendor, Instant expiryDate) {
        this.token = token;
        this.vendor = vendor;
        this.expiryDate = expiryDate;
    }
}



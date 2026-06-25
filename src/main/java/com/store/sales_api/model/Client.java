package com.store.sales_api.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;


@Entity
@SQLDelete(sql = "UPDATE sales SET is_active=false WHERE id=?")
@SQLRestriction("is_active=true")
@Getter @Setter
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;
    private String lastName;
    private String dni;
    private Boolean isActive = true;

    @OneToMany (mappedBy = "client")
    private List<Sale> purchases = new ArrayList<>();



    public Client() {
    }

    public Client(String name, String lastName, String dni) {
        this.name = name;
        this.lastName = lastName;
        this.dni = dni;
    }
    
}

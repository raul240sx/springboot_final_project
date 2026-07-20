package com.store.sales_api.security;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.store.sales_api.sale.Sale;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter @Setter
@SQLDelete(sql = "UPDATE vendor SET is_active=false WHERE id = ?")
@SQLRestriction("is_active=true")
public class Vendor{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;
    private String code;
    private String name;
    private String lastName;
    private String dni;
    private Boolean is_active = true;

    @OneToMany(mappedBy = "vendor")
    private List<Sale> sales = new ArrayList<>();
    
    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VendorRole> roles = new ArrayList<>();


    
    public Vendor() {
    }

    public Vendor(String name, String lastName, String dni, String password) {
        this.name = name;
        this.lastName = lastName;
        this.dni = dni;
        this.password = password;
    }


    public void addRole(VendorRole role){
        if (!roles.contains(role)) {
            this.roles.add(role);
            role.setVendor(this);
        }
    }

    public void removeRole(VendorRole role) {
        if (roles.remove(role)) {
            role.setVendor(null);
        }
    }
}

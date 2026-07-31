package rrs.ms_clients.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;


@Entity
@SQLDelete(sql = "UPDATE client SET is_active=false WHERE id=?")
@SQLRestriction("is_active=true")
@Getter @Setter
public class Client{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;
    private String lastName;
    private String dni;
    private Boolean is_active = true;


    public Client() {
    }

    public Client(String name, String lastName, String dni) {
        this.name = name;
        this.lastName = lastName;
        this.dni = dni;
    } 
}

package org.example.practic.domain;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name="Customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name="customer_id")
    private Long customerId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Column(name = "registration_date", nullable = false)
    private Date registrationDate;

    public Customer() {}
    public Customer(Long customerId, String name, String city, Date registrationDate) {
        this.customerId = customerId;
        this.name = name;
        this.city = city;
        this.registrationDate = registrationDate;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
}

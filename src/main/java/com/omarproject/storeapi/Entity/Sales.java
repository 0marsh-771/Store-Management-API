package com.omarproject.storeapi.Entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "sales")
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "cashier")
    private String cashier;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "total")
    private BigDecimal total;

    // define the constructors

    public Sales(){

    }

    public Sales(String cashier, LocalDate date, BigDecimal total) {
        this.cashier = cashier;
        this.date = date;
        this.total = total;
    }

    // define the getter and setter methods
    // getters


    public int getId() {
        return id;
    }

    public String getCashier() {
        return cashier;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getTotal() {
        return total;
    }

    // setters

    public void setId(int id) {
        this.id = id;
    }

    public void setCashier(String cashier) {
        this.cashier = cashier;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}

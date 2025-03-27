package com.example.demo;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Statistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Integer averageRate;

    public Statistic() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAverageRate() {
        return averageRate;
    }

    public void setAverageRate(Integer averageRate) {
        this.averageRate = averageRate;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "id=" + id +
                ", averageRate=" + averageRate +
                '}';
    }
}

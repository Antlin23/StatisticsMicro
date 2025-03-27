package com.example.demo;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Statistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Statistic() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "id=" + id +
                '}';
    }
}

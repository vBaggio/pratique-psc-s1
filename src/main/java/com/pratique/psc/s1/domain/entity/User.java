package com.pratique.psc.s1.domain.entity;

import java.time.LocalDateTime;

public class User {
    
    private final long id;
    private String name;
    private String email;
    private String city;
    private final LocalDateTime createdAt;

    public User(long id, String name, String email, String city, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.city = city;
        this.createdAt = createdAt;
    }

    public User(long id, String name, String email, String city) {
        this(id, name, email, city, LocalDateTime.now());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

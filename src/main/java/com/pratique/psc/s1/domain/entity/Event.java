package com.pratique.psc.s1.domain.entity;

import com.pratique.psc.s1.domain.enums.EventCategory;
import com.pratique.psc.s1.domain.enums.EventStatus;

import java.time.LocalDateTime;

public class Event {
    
    private final long id;
    private String name;
    private String city;
    private String address;
    private EventCategory category;
    private LocalDateTime dateTime;
    private int durationMinutes;
    private String description;
    private final LocalDateTime createdAt;

    public Event(
            long id,
            String name,
            String city,
            String address,
            EventCategory category,
            LocalDateTime dateTime,
            int durationMinutes,
            String description
    ) {
        this(id, name, city, address, category, dateTime, durationMinutes, description, LocalDateTime.now());
    }

    public Event(
            long id,
            String name,
            String city,
            String address,
            EventCategory category,
            LocalDateTime dateTime,
            int durationMinutes,
            String description,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.createdAt = createdAt;
        this.name = name;
        this.city = city;
        this.address = address;
        this.category = category;
        this.dateTime = dateTime;
        this.durationMinutes = durationMinutes;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public EventCategory getCategory() {
        return category;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCategory(EventCategory category) {
        this.category = category;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EventStatus getStatus(LocalDateTime now) {
        LocalDateTime current = now;
        LocalDateTime end = dateTime.plusMinutes(durationMinutes);

        if (current.isBefore(dateTime)) {
            return EventStatus.UPCOMING;
        }
        if (!current.isAfter(end)) {
            return EventStatus.ONGOING;
        }
        return EventStatus.PAST;
    }
}

package com.pratique.psc.s1.domain.entity;

import java.time.LocalDateTime;

public class Participation {
    
    private final long id;
    private final long userId;
    private final long eventId;
    private final LocalDateTime confirmedAt;

    public Participation(long id, long userId, long eventId, LocalDateTime confirmedAt) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.confirmedAt = confirmedAt;
    }

    public Participation(long id, long userId, long eventId) {
        this(id, userId, eventId, LocalDateTime.now());
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public long getEventId() {
        return eventId;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }
}

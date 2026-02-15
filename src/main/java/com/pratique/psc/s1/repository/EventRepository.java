package com.pratique.psc.s1.repository;

import com.pratique.psc.s1.domain.entity.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository {
    void save(Event event);

    List<Event> findAll();

    Optional<Event> findById(long id);

    boolean deleteById(long id);
}

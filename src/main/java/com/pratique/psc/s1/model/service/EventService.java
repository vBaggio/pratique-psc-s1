package com.pratique.psc.s1.model.service;

import com.pratique.psc.s1.model.entity.Event;
import com.pratique.psc.s1.model.enums.EventCategory;
import com.pratique.psc.s1.model.enums.EventStatus;
import com.pratique.psc.s1.model.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class EventService {
    
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event createEvent(
            String name,
            String city,
            String address,
            EventCategory category,
            LocalDateTime dateTime,
            int durationMinutes,
            String description
    ) {
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Nao e permitido cadastrar evento com data passada.");
        }

        long nextId = eventRepository.findAll().stream()
                .mapToLong(Event::getId)
                .max()
                .orElse(0L) + 1;

        Event event = new Event(nextId, name, city, address, category, dateTime, durationMinutes, description);
        eventRepository.save(event);
        return event;
    }

    public List<Event> listAllSortedByDateTime() {
        return eventRepository.findAll().stream()
                .sorted(Comparator.comparing(Event::getDateTime))
                .toList();
    }

    public List<Event> listByStatus(EventStatus status, LocalDateTime now) {
        return eventRepository.findAll().stream()
                .filter(event -> event.getStatus(now) == status)
                .sorted(Comparator.comparing(Event::getDateTime))
                .toList();
    }

    public List<Event> listByCitySortedByDateTime(String city) {
        return eventRepository.findAll().stream()
                .filter(event -> event.getCity() != null && event.getCity().equalsIgnoreCase(city))
                .sorted(Comparator.comparing(Event::getDateTime))
                .toList();
    }

    public List<Event> listUpcomingAndOngoingByCitySortedByDateTime(String city, LocalDateTime now) {
        return eventRepository.findAll().stream()
                .filter(event -> event.getCity() != null && event.getCity().equalsIgnoreCase(city))
                .filter(event -> event.getStatus(now) != EventStatus.PAST)
                .sorted(Comparator.comparing(Event::getDateTime))
                .toList();
    }

    public Optional<Event> findById(long id) {
        return eventRepository.findById(id);
    }

    public boolean deleteById(long id) {
        return eventRepository.deleteById(id);
    }
}

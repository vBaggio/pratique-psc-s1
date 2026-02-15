package com.pratique.psc.s1.service;

import com.pratique.psc.s1.domain.entity.Event;
import com.pratique.psc.s1.domain.entity.Participation;
import com.pratique.psc.s1.repository.EventRepository;
import com.pratique.psc.s1.repository.ParticipationRepository;
import com.pratique.psc.s1.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ParticipationService {
    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public ParticipationService(
            ParticipationRepository participationRepository,
            UserRepository userRepository,
            EventRepository eventRepository
    ) {
        this.participationRepository = participationRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    public Participation confirmParticipation(long userId, long eventId) {
        boolean userExists = userRepository.findById(userId).isPresent();
        if (!userExists) {
            throw new IllegalArgumentException("Usuario nao encontrado.");
        }

        boolean eventExists = eventRepository.findById(eventId).isPresent();
        if (!eventExists) {
            throw new IllegalArgumentException("Evento nao encontrado.");
        }

        Optional<Participation> existing = participationRepository.findByUserIdAndEventId(userId, eventId);
        if (existing.isPresent()) {
            return existing.get();
        }

        long nextId = participationRepository.findAll().stream()
                .mapToLong(Participation::getId)
                .max()
                .orElse(0L) + 1;

        Participation participation = new Participation(nextId, userId, eventId);
        participationRepository.save(participation);
        return participation;
    }

    public boolean cancelParticipation(long userId, long eventId) {
        Optional<Participation> existing = participationRepository.findByUserIdAndEventId(userId, eventId);
        return existing.filter(participation -> participationRepository.deleteById(participation.getId())).isPresent();
    }

    public List<Participation> listByUser(long userId) {
        return participationRepository.findByUserId(userId);
    }

    public List<Event> listConfirmedEventsByUser(long userId) {
        List<Long> eventIds = participationRepository.findByUserId(userId).stream()
                .map(Participation::getEventId)
                .toList();

        return eventIds.stream()
                .map(eventRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(Comparator.comparing(Event::getDateTime))
                .toList();
    }
}

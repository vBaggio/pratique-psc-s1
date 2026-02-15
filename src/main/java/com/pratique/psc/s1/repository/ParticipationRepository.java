package com.pratique.psc.s1.repository;

import com.pratique.psc.s1.domain.entity.Participation;

import java.util.List;
import java.util.Optional;

public interface ParticipationRepository {
    void save(Participation participation);

    List<Participation> findAll();

    List<Participation> findByUserId(long userId);

    List<Participation> findByEventId(long eventId);

    Optional<Participation> findByUserIdAndEventId(long userId, long eventId);

    boolean deleteById(long id);
}

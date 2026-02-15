package com.pratique.psc.s1.repository.impl.file;

import com.pratique.psc.s1.domain.entity.Participation;
import com.pratique.psc.s1.repository.ParticipationRepository;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParticipationFileRepository extends FileRepository<Participation> implements ParticipationRepository {

    public ParticipationFileRepository(Path filePath) {
        super(filePath);
    }

    @Override
    public void save(Participation participation) {
        findByUserIdAndEventId(participation.getUserId(), participation.getEventId())
                .ifPresent(items::remove);
        items.add(participation);
    }

    @Override
    public List<Participation> findAll() {
        return new ArrayList<>(items);
    }

    @Override
    public List<Participation> findByUserId(long userId) {
        return items.stream()
                .filter(item -> item.getUserId() == userId)
                .toList();
    }

    @Override
    public List<Participation> findByEventId(long eventId) {
        return items.stream()
                .filter(item -> item.getEventId() == eventId)
                .toList();
    }

    @Override
    public Optional<Participation> findByUserIdAndEventId(long userId, long eventId) {
        return items.stream()
                .filter(item -> item.getUserId() == userId && item.getEventId() == eventId)
                .findFirst();
    }

    @Override
    public boolean deleteById(long id) {
        return items.removeIf(item -> item.getId() == id);
    }

    @Override
    protected Participation parse(String line) {
        String[] parts = line.split(";", -1);
        return new Participation(
                Long.parseLong(parts[0]),
                Long.parseLong(parts[1]),
                Long.parseLong(parts[2]),
                LocalDateTime.parse(parts[3])
        );
    }

    @Override
    protected String format(Participation participation) {
        return String.join(";",
                String.valueOf(participation.getId()),
                String.valueOf(participation.getUserId()),
                String.valueOf(participation.getEventId()),
                participation.getConfirmedAt().toString()
        );
    }
}

package com.pratique.psc.s1.model.repository.impl.file;

import com.pratique.psc.s1.model.entity.Event;
import com.pratique.psc.s1.model.enums.EventCategory;
import com.pratique.psc.s1.model.repository.EventRepository;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EventFileRepository extends FileRepository<Event> implements EventRepository {

    public EventFileRepository(Path filePath) {
        super(filePath);
    }

    @Override
    public void save(Event event) {
        findById(event.getId()).ifPresent(items::remove);
        items.add(event);
    }

    @Override
    public List<Event> findAll() {
        return new ArrayList<>(items);
    }

    @Override
    public Optional<Event> findById(long id) {
        return items.stream().filter(item -> item.getId() == id).findFirst();
    }

    @Override
    public boolean deleteById(long id) {
        return items.removeIf(item -> item.getId() == id);
    }

    @Override
    protected Event parse(String line) {
        String[] parts = line.split(";", -1);
        return new Event(
                Long.parseLong(parts[0]),
                parts[1],
                parts[2],
                parts[3],
                EventCategory.valueOf(parts[4]),
                LocalDateTime.parse(parts[5]),
                Integer.parseInt(parts[6]),
                parts[7],
                LocalDateTime.parse(parts[8])
        );
    }

    @Override
    protected String format(Event event) {
        return String.join(";",
                String.valueOf(event.getId()),
                event.getName(),
                event.getCity(),
                event.getAddress(),
                event.getCategory().name(),
                event.getDateTime().toString(),
                String.valueOf(event.getDurationMinutes()),
                event.getDescription(),
                event.getCreatedAt().toString()
        );
    }
}

package com.pratique.psc.s1.repository.impl.file;

import com.pratique.psc.s1.domain.entity.User;
import com.pratique.psc.s1.repository.UserRepository;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserFileRepository extends FileRepository<User> implements UserRepository {

    public UserFileRepository(Path filePath) {
        super(filePath);
    }

    @Override
    public void save(User user) {
        findById(user.getId()).ifPresent(items::remove);
        items.add(user);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(items);
    }

    @Override
    public Optional<User> findById(long id) {
        return items.stream().filter(item -> item.getId() == id).findFirst();
    }

    @Override
    public boolean deleteById(long id) {
        return items.removeIf(item -> item.getId() == id);
    }

    @Override
    protected User parse(String line) {
        String[] parts = line.split(";", -1);
        return new User(
                Long.parseLong(parts[0]),
                parts[1],
                parts[2],
                parts[3],
                LocalDateTime.parse(parts[4])
        );
    }

    @Override
    protected String format(User user) {
        return String.join(";",
                String.valueOf(user.getId()),
                user.getName(),
                user.getEmail(),
                user.getCity(),
                user.getCreatedAt().toString()
        );
    }
}

package com.pratique.psc.s1.model.service;

import com.pratique.psc.s1.model.entity.User;
import com.pratique.psc.s1.model.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String name, String email, String city) {
        String normalizedEmail = normalizeEmail(email);
        boolean emailAlreadyExists = userRepository.findAll().stream()
                .map(User::getEmail)
                .filter(Objects::nonNull)
                .map(this::normalizeEmail)
                .anyMatch(normalizedEmail::equals);

        if (emailAlreadyExists) {
            throw new IllegalArgumentException("Ja existe usuario cadastrado com este e-mail.");
        }

        long nextId = userRepository.findAll().stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0L) + 1;

        User user = new User(nextId, name, email, city);
        userRepository.save(user);
        return user;
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    public boolean deleteById(long id) {
        return userRepository.deleteById(id);
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }
}

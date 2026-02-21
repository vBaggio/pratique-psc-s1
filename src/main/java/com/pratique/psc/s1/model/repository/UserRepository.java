package com.pratique.psc.s1.model.repository;

import com.pratique.psc.s1.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void save(User user);

    List<User> findAll();

    Optional<User> findById(long id);

    boolean deleteById(long id);
}

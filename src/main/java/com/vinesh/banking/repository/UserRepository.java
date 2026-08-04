package com.vinesh.banking.repository;

import com.vinesh.banking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Spring Data generates the queries — we just declare the method signatures.
// existsBy* is faster than findBy* when you only need a boolean check.
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}

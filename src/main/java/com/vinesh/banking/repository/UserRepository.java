package com.vinesh.banking.repository;

import com.vinesh.banking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

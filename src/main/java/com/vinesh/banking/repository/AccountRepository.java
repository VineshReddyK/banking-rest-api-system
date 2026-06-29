package com.vinesh.banking.repository;

import com.vinesh.banking.entity.Account;
import com.vinesh.banking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByUser(User user);
}

package org.example.repository;

import org.example.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface accountRepository extends JpaRepository<Account, String> {
}

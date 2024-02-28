package com.josealmeida.banktransactions.repository;

import com.josealmeida.banktransactions.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}

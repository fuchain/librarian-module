package com.fpt.edu.repositories;

import org.springframework.data.repository.CrudRepository;

import com.fpt.edu.entities.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long>{

}

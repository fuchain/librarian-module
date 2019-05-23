package com.fptedu.web.repository;

import org.springframework.data.repository.CrudRepository;

import com.fpt.edu.entities.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Long>{

}

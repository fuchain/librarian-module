package com.fpt.edu.services;

import com.fpt.edu.entities.Transaction;
import com.fpt.edu.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class TransactionServices {
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServices(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction getById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction id: " + id + " not found"));
    }

    public Transaction insertTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}

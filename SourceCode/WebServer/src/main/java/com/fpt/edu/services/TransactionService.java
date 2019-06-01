package com.fpt.edu.services;

import com.fpt.edu.entities.Transaction;
import com.fpt.edu.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction getById(Long id) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(id);

        Transaction transaction = null;

        if (optionalTransaction.isPresent()) {
            transaction = optionalTransaction.get();
        }

        return transaction;
    }

    public Transaction insertTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}

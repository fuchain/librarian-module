package com.fpt.edu.controller;

import com.fpt.edu.entities.Transaction;
import com.fpt.edu.services.TransactionServices;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("transaction")
public class TransactionController extends BaseController {
    private final TransactionServices transactionServices;

    @Autowired
    public TransactionController(TransactionServices transactionServices) {
        this.transactionServices = transactionServices;
    }

    @ApiOperation(value = "Get a transaction by its id", response = Transaction.class)
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getById(@PathVariable Long id) {
        return new ResponseEntity<>(transactionServices.getById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Create a transaction", response = Transaction.class)
    @PostMapping("")
    public ResponseEntity<Transaction> insertTransaction(@RequestBody Transaction transaction) {
        Transaction transactionResult = transactionServices.insertTransaction(transaction);

        return new ResponseEntity<>(transactionResult, HttpStatus.CREATED);
    }

}

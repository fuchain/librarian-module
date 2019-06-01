package com.fpt.edu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.Transaction;
import com.fpt.edu.exception.EntityNotFoundException;
import com.fpt.edu.services.TransactionService;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("transaction")
public class TransactionController extends BaseController {

    @Autowired
    private TransactionService transactionService;

    @ApiOperation(value = "Get a transaction by its id", response = String.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> getById(@PathVariable Long id) throws EntityNotFoundException, JsonProcessingException {
        Transaction transaction = transactionService.getById(id);

        if (transaction == null) {
            throw new EntityNotFoundException("Transaction id: " + id + " not found");
        }

        JSONObject jsonObject = utils.convertObjectToJSONObject(transaction);
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @ApiOperation(value = "Create a transaction", response = String.class)
    @RequestMapping(value = "", method = RequestMethod.POST, produces = Constant.APPLICATION_JSON)
    public ResponseEntity<String> insertTransaction(@RequestBody Transaction transaction) {
        Transaction transactionResult = transactionService.insertTransaction(transaction);
        JSONObject jsonObject = new JSONObject();
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.CREATED);
    }


}

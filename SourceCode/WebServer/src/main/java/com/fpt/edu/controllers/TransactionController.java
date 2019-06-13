package com.fpt.edu.controllers;

import com.amazonaws.services.s3.AmazonS3;
import com.fpt.edu.entities.Transaction;
import com.fpt.edu.services.TransactionServices;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

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

	@Autowired
	AmazonS3 s3Client;
	@ApiOperation(value = "Create a transaction", response = Transaction.class)
	@PostMapping("/upload")
	public ResponseEntity<String> testUploadFile(@RequestParam("file")MultipartFile file) throws URISyntaxException, IOException {
		String fileUrl = utils.uploadFile(file);

		JSONObject responseObj = new JSONObject();
		responseObj.put("url", fileUrl);
		return new ResponseEntity<>(responseObj.toString(), HttpStatus.CREATED);
	}

}

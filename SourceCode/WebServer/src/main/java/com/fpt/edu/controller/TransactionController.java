package com.fpt.edu.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.Transaction;
import com.fpt.edu.services.TransactionServices;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_PNG_VALUE,MediaType.IMAGE_GIF_VALUE})
	public ResponseEntity<byte[]> testUploadFile(@RequestParam("file")MultipartFile file) throws URISyntaxException, IOException {
		String fileUrl=utils.uploadFile(file);
		InputStreamResource resource= utils.downloadFileTos3bucket(fileUrl);
		return new ResponseEntity<>(IOUtils.toByteArray(resource.getInputStream()),HttpStatus.CREATED);
	}

}

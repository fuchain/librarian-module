package com.example.demo;

import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;

import com.example.demo.entities.Book;
import com.example.demo.helpers.*;
import com.example.demo.services.TransactionBuilderServices;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.bigchaindb.util.Base58;
import repository.IUserRepository;

/**
 * simple usage of BigchainDB Java driver (https://github.com/bigchaindb/java-bigchaindb-driver)
 * to create TXs on BigchainDB network
 *
 * @author dev@bigchaindb.com
 */

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = {IUserRepository.class})
@ComponentScan(basePackages = {"repository", "services", "controller"})
@EntityScan("entities")
public class BigchainDBJavaDriverUsageExample {

    /**
     * main method
     *
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String args[]) throws Exception {
        SpringApplication.run(BigchainDBJavaDriverUsageExample.class, args);
        // Prepare datas
        // http://testnet.bigchaindb.com
        // https://bigchain.fptu.tech/
        String host = "http://localhost:9984/"; // hostname of bigchaindb
        String returner = "linhph@fptu.tech"; // Linh is returner
        String borrower = "phong@fptu.tech"; // Phong is borrower
        int year = 2000; // metadata: year when asset was created
        String status = "In use"; // metadata: status of asset

        //set configuration
        ConfigHelper.setupConfig(host);

        // create new book
        Book book = new Book(
                1,
                "Machine Learning",
                "John Thanos",
                "Computer Science",
                "Start",
                "Machine learning for beginer",
                2000,
                borrower);
        System.out.println("(*) Book Prepared..");

        TransactionBuilderServices services = new TransactionBuilderServices();

        //execute CREATE transaction
//        String createTxIid = services.createTransaction(book.getAsset(), book.getMetadata(), returner);

        //create transfer metadata
        book.setStatus(Book.StatusType.IN_USE.value());
//        book.setStatus(Book.StatusType.DAMAGED.value());
        book.setKeeper(borrower);
//        book.setKeeper(returner);
//        bookMetadata.setKeeper(returner);
//        System.out.println("(*) Transfer Metadata Prepared..");

        //execute TRANSFER transaction on the CREATED asset
        String txId = "4efc8a69b257b8c70a7b03aa98eff0f2663b09b26f1a50e6afeaaecc169fc396";
        String assetId = "919fc997ce88a44629bbf577666f1bf53b599f1594d644728823444fb6729333";

        services.transferTransaction(txId, assetId, book.getMetadata(), returner, borrower);
//        services.transferTransaction(txId, assetId, book.getMetadata(), borrower, returner);
    }
}
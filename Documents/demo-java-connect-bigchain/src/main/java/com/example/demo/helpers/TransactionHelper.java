package com.example.demo.helpers;

import com.bigchaindb.builders.BigchainDbTransactionBuilder;
import com.bigchaindb.constants.Operations;
import com.bigchaindb.model.GenericCallback;
import com.bigchaindb.model.MetaData;
import com.bigchaindb.model.Transaction;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import okhttp3.Response;

import java.security.KeyPair;
import java.util.Map;
import java.util.TreeMap;

public final class TransactionHelper {

    public static String createTransaction(
        Map<String, String> assetData,
        MetaData metaData,
        KeyPair keyPair
    ) throws Exception {
        Transaction transaction = null;
         transaction = BigchainDbTransactionBuilder
            .init()
            .addAssets(assetData, TreeMap.class)
            .addMetaData(metaData)
            .operation(Operations.CREATE)
            .buildAndSign((EdDSAPublicKey) keyPair.getPublic(), (EdDSAPrivateKey) keyPair.getPrivate())
            .sendTransaction(transactionResultHandler());

        System.out.println("(*) CREATE Transaction sent..");
        System.out.println("transaciton: " + transaction);
        return transaction.getId();
    }

    private static GenericCallback transactionResultHandler() {
        GenericCallback callback = new GenericCallback() {
            @Override
            public void transactionMalformed(Response response) {
                System.out.println("Malformed: " + response.message());
                onFailure();
            }

            @Override
            public void pushedSuccessfully(Response response) {
                System.out.println("Success: " + response.message());
                onSuccess();
            }

            @Override
            public void otherError(Response response) {
                System.out.println("OtherError" + response.message());
                onFailure();
            }
        };
        return callback;
    }

    private static void onSuccess() {
        System.out.println("Transaction successfully!!");
    }

    private static void onFailure() {
        System.out.println("Transaction failed!!!");
    }
}

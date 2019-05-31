package com.example.demo.services;

import com.bigchaindb.api.TransactionsApi;
import com.bigchaindb.builders.BigchainDbTransactionBuilder;
import com.bigchaindb.constants.Operations;
import com.bigchaindb.cryptoconditions.types.Ed25519Sha256Condition;
import com.bigchaindb.cryptoconditions.types.Ed25519Sha256Fulfillment;
import com.bigchaindb.model.*;
import com.bigchaindb.util.Base58;
import com.bigchaindb.util.DriverUtils;
import com.bigchaindb.util.KeyPairUtils;
import com.example.demo.helpers.KeypairHelper;
import com.google.api.client.util.Base64;
import com.google.gson.JsonObject;
import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import okhttp3.Response;

import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class TransactionBuilderServices {

    private static final String SHA_512_HASH = "SHA-512";
    private static final String DEFAULT_ASSET_AMOUNT = "1";
    private static final String DEFAULT_TRANSACTION_VALIDATION_VERSION = "2.0";
    private static final String DEFAULT_DETAIL_TYPE = "ed25519-sha-256";
    private static final int DEFAULT_INPUT_INDEX = 0;
    private static final int DEFAULT_OUPUT_INDEX = 0;

    private Transaction transaction;

    /**
     * CREATE transaction
     *
     * @param asset
     * @param metadata
     * @param owner
     * @throws Exception
     */
    public String createTransaction(
            Object asset,
            Object metadata,
            String owner
    ) throws Exception {
        KeyPair ownerKeys = KeypairHelper.getKeyPairFromInput(owner);
        this.transaction = BigchainDbTransactionBuilder
                .init()
                .addAssets(asset, Object.class)
                .addMetaData(metadata)
                .operation(Operations.CREATE)
                .buildAndSign((EdDSAPublicKey) ownerKeys.getPublic(), (EdDSAPrivateKey) ownerKeys.getPrivate())
                .sendTransaction(transactionResultHandler());
        String transactionId = this.transaction.getId();

        System.out.println("(*) CREATE Transaction sent..");
        System.out.println("transaction id: " + transactionId);
        System.out.println("transaciton: " + this.transaction);
        return transactionId;
    }

    /**
     * TRANSFER transaction
     *
     * @param previousTransactionId
     * @param assetId
     * @param metaData
     * @param owner
     * @param target
     * @return String
     * @throws Exception
     */
    public String transferTransaction(
            String previousTransactionId,
            String assetId, Object metaData,
            String owner, String target
    ) throws Exception {
        KeyPair ownerKeys = KeypairHelper.getKeyPairFromInput(owner);
        KeyPair targetKeys = KeypairHelper.getKeyPairFromInput(target);
        this.transaction = this.initTransaction(
                assetId, metaData, Operations.TRANSFER.name(),
                DEFAULT_TRANSACTION_VALIDATION_VERSION);

        this.setInput(previousTransactionId, ownerKeys);
        this.setOutput(targetKeys);

        this.sign(this.transaction.toHashInput(), ownerKeys);

        String transactionId = DriverUtils.getSha3HashHex(DriverUtils.makeSelfSortingGson(this.transaction.toHashInput()).toString().getBytes());
        this.submitTransaction(transactionId);

        System.out.println("(*) TRANSFER Transaction sent..");
        System.out.println("transaction id: " + transactionId);
        System.out.println("transaciton: " + this.transaction);
        return transactionId;
    }

    /**
     * Init transaction with asset, metadata, operation, tx validation version
     *
     * @param asset
     * @param metadata
     * @param operaton
     * @param version
     * @return
     */
    private Transaction initTransaction(Object asset, Object metadata, String operaton, String version) {
        Transaction transaction = new Transaction();

        // Set asset
        transaction.setAsset(new Asset((String) asset));
        // Set metadata
        transaction.setMetaData(metadata);
        // Set operation
        transaction.setOperation(operaton);
        // Set version
        transaction.setVersion(version);

        return transaction;
    }

    /**
     * Set input of transaction
     *
     * @param previousTransactionId
     * @throws Exception
     */
    private void setInput(String previousTransactionId, KeyPair ownerKeys) throws Exception {
        List<String> owners = null;
        FulFill spendFrom = null;
        String operation = this.transaction.getOperation();
        owners = TransactionsApi
                .getTransactionById(previousTransactionId)
                .getInputs().get(DEFAULT_INPUT_INDEX).getOwnersBefore();
        spendFrom = new FulFill();
        spendFrom.setTransactionId(previousTransactionId);
        spendFrom.setOutputIndex(DEFAULT_OUPUT_INDEX);

        Input input = new Input();
        input.setFulFills(spendFrom);
        for (String owner : owners) {
            input.addOwner(owner);
        }
        this.transaction.getInputs().add(input);
    }

    /**
     * Set output of transaction
     *
     * @param targetKeys
     */
    private void setOutput(KeyPair targetKeys) {
        Output output = new Output();
        output.setAmount(DEFAULT_ASSET_AMOUNT);

        // Prepare target public key
        EdDSAPublicKey targetPublicKey = (EdDSAPublicKey) targetKeys.getPublic();
        String encodedTargetPublicKey = KeyPairUtils.encodePublicKeyInBase58(targetPublicKey);

        // Prepare condition (detail)
        Ed25519Sha256Condition sha256Condition = new Ed25519Sha256Condition(targetPublicKey);
        output.addPublicKey(encodedTargetPublicKey);
        Details details = new Details();
        details.setPublicKey(encodedTargetPublicKey);
        details.setType(DEFAULT_DETAIL_TYPE);
        output.setCondition(new Condition(details, sha256Condition.getUri().toString()));

        this.transaction.getOutputs().add(output);
    }

    /**
     * Sign transaction
     *
     * @param transactionHash
     * @param ownerKeys
     * @throws Exception
     */
    private void sign(String transactionHash, KeyPair ownerKeys) throws Exception {
        JsonObject transactionJObject = DriverUtils.makeSelfSortingGson(transactionHash);
        byte[] sha3Hash;
        StringBuilder preimage = new StringBuilder(transactionJObject.toString());
        for (Input in : this.transaction.getInputs()) {
            if (in.getFulFills() != null) {
                FulFill fulfill = in.getFulFills();
                String txBlock = fulfill.getTransactionId() + fulfill.getOutputIndex();
                preimage.append(txBlock);
            }
        }
        sha3Hash = DriverUtils.getSha3HashRaw(preimage.toString().getBytes());

        Signature edDsaSigner = new EdDSAEngine(MessageDigest.getInstance(SHA_512_HASH));
        edDsaSigner.initSign(ownerKeys.getPrivate());
        edDsaSigner.update(sha3Hash);
        byte[] signature = edDsaSigner.sign();
        Ed25519Sha256Fulfillment fulfillment = new Ed25519Sha256Fulfillment((EdDSAPublicKey) ownerKeys.getPublic(), signature);
        this.transaction.getInputs().get(DEFAULT_INPUT_INDEX)
                .setFullFillment(Base64.encodeBase64URLSafeString(fulfillment.getEncoded()));
    }

    /**
     * Submit transaction
     *
     * @param transactionId
     */
    private void submitTransaction(String transactionId) {
        this.transaction.setSigned(true);
        this.transaction.setId(transactionId);
        TransactionsApi.sendTransaction(this.transaction, transactionResultHandler());
    }

    /**
     * callback handler to handle result of transaction
     *
     * @return GenericCallback
     */
    private GenericCallback transactionResultHandler() {
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

    /**
     * execute when transaction is success
     */
    private void onSuccess() {
        System.out.println("Transaction successfully!!");
    }

    /**
     * execute when transaction is failure
     */
    private void onFailure() {
        System.out.println("Transaction failed!!!");
    }
}

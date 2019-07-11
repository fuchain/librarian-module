package com.fpt.edu.services;

import com.bigchaindb.api.TransactionsApi;
import com.bigchaindb.constants.Operations;
import com.bigchaindb.cryptoconditions.types.Ed25519Sha256Condition;
import com.bigchaindb.cryptoconditions.types.Ed25519Sha256Fulfillment;
import com.bigchaindb.model.*;
import com.bigchaindb.util.DriverUtils;
import com.bigchaindb.util.KeyPairUtils;
import com.fpt.edu.common.helpers.KeypairHelper;
import com.google.api.client.util.Base64;
import com.google.gson.JsonObject;
import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;

@Service
public final class BigchainTransactionServices {

	public Transaction doCreate(
		Object asset, Object metadata, String owner
	) throws Exception {
		return doCreate(
			asset, metadata, owner,
			(transaction, response) -> {
			},
			(transaction, response) -> {
			});
	}

	public Transaction doCreate(
		Object asset, Object metadata, String owner,
		TransactionresultHandler successCaller,
		TransactionresultHandler faildedCaller
	) throws Exception {
		TransactionBuilderFactory factory = new TransactionBuilderFactory();
		return factory.createTransaction(
			asset, metadata, owner,
			successCaller, faildedCaller);
	}

	public Transaction doTransfer(
		String previousTransactionId,
		String assetId, Object metadata,
		String owner, String target
	) throws Exception {
		return doTransfer(
			previousTransactionId,
			assetId, metadata,
			owner, target,
			(transaction, response) -> {
			},
			(transaction, response) -> {
			});
	}

	public Transaction doTransfer(
		String previousTransactionId,
		String assetId, Object metadata,
		String owner, String target,
		TransactionresultHandler successCaller,
		TransactionresultHandler faildedCaller
	) throws Exception {
		TransactionBuilderFactory factory = new TransactionBuilderFactory();
		return factory.transferTransaction(
			previousTransactionId,
			assetId, metadata,
			owner, target,
			successCaller, faildedCaller);
	}

	public Transactions getTransactionsByAssetId(String assetId, Operations operartion) throws Exception {
		return TransactionsApi.getTransactionsByAssetId(assetId, operartion);
	}

	public Transaction getTransactionById(String id) throws Exception {
		return TransactionsApi.getTransactionById(id);
	}

	public interface TransactionresultHandler {

		// This is  callback when transaction has result
		public void onTransactionResult(Transaction transaction, Response response);
	}
}

class TransactionBuilderFactory {

	private static final String SHA_512_HASH = "SHA-512";
	private static final String DEFAULT_ASSET_AMOUNT = "1";
	private static final String DEFAULT_TRANSACTION_VALIDATION_VERSION = "2.0";
	private static final String DEFAULT_DETAIL_TYPE = "ed25519-sha-256";
	private static final int DEFAULT_INPUT_INDEX = 0;
	private static final int DEFAULT_OUPUT_INDEX = 0;
	private final Logger LOGGER = LogManager.getLogger(getClass());
	private Transaction transaction;

	/**
	 * CREATE transaction with callback handler
	 *
	 * @param asset
	 * @param metadata
	 * @param owner
	 * @throws Exception
	 */
	public Transaction createTransaction(
		Object asset, Object metadata, String owner,
		BigchainTransactionServices.TransactionresultHandler successCaller,
		BigchainTransactionServices.TransactionresultHandler faildedCaller
	) throws Exception {
		KeyPair ownerKeys = KeypairHelper.getKeyPairFromInput(owner);
		this.transaction = this.initTransaction(
			asset, metadata, Operations.CREATE.name(),
			DEFAULT_TRANSACTION_VALIDATION_VERSION);

		this.setInput(null, ownerKeys);
		this.setOutput(ownerKeys);

		this.sign(this.transaction.toHashInput(), ownerKeys);

		String transactionId = DriverUtils.getSha3HashHex(DriverUtils.makeSelfSortingGson(this.transaction.toHashInput()).toString().getBytes());
		this.submitTransaction(transactionId, successCaller, faildedCaller);

		return this.transaction;
	}

	/**
	 * TRANSFER transaction
	 *
	 * @param previousTransactionId
	 * @param assetId
	 * @param metadata
	 * @param owner
	 * @param target
	 * @return String
	 * @throws Exception
	 */
	public Transaction transferTransaction(
		String previousTransactionId,
		String assetId, Object metadata,
		String owner, String target,
		BigchainTransactionServices.TransactionresultHandler successCaller,
		BigchainTransactionServices.TransactionresultHandler faildedCaller
	) throws Exception {
		KeyPair ownerKeys = KeypairHelper.getKeyPairFromInput(owner);
		KeyPair targetKeys = KeypairHelper.getKeyPairFromInput(target);
		this.transaction = this.initTransaction(
			assetId, metadata, Operations.TRANSFER.name(),
			DEFAULT_TRANSACTION_VALIDATION_VERSION);

		this.setInput(previousTransactionId, ownerKeys);
		this.setOutput(targetKeys);

		this.sign(this.transaction.toHashInput(), ownerKeys);

		String transactionId = DriverUtils.getSha3HashHex(DriverUtils.makeSelfSortingGson(this.transaction.toHashInput()).toString().getBytes());
		this.submitTransaction(transactionId, successCaller, faildedCaller);

		return this.transaction;
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
		if (String.class.isAssignableFrom(asset.getClass())) {
			transaction.setAsset(new Asset((String) asset));
		} else {
			transaction.setAsset(new Asset(asset, asset.getClass()));
		}
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

		if (previousTransactionId == null && operation.equals(Operations.CREATE.name())) {
			owners = new ArrayList<>();
			EdDSAPublicKey ownerPublicKey = (EdDSAPublicKey) ownerKeys.getPublic();
			String encodedTargetPublicKey = KeyPairUtils.encodePublicKeyInBase58(ownerPublicKey);
			owners.add(encodedTargetPublicKey);
		} else if (operation.equals(Operations.TRANSFER.name())) {
			owners = TransactionsApi
				.getTransactionById(previousTransactionId)
				.getInputs().get(DEFAULT_INPUT_INDEX).getOwnersBefore();
			spendFrom = new FulFill();
			spendFrom.setTransactionId(previousTransactionId);
			spendFrom.setOutputIndex(DEFAULT_OUPUT_INDEX);
		}

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
		if (Operations.TRANSFER.name().equals(this.transaction.getOperation())) {
			StringBuilder preimage = new StringBuilder(transactionJObject.toString());
			for (Input in : this.transaction.getInputs()) {
				if (in.getFulFills() != null) {
					FulFill fulfill = in.getFulFills();
					String txBlock = fulfill.getTransactionId() + fulfill.getOutputIndex();
					preimage.append(txBlock);
				}
			}
			sha3Hash = DriverUtils.getSha3HashRaw(preimage.toString().getBytes());
		} else {
			// otherwise, just get the message digest
			sha3Hash = DriverUtils.getSha3HashRaw(transactionJObject.toString().getBytes());
		}

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
	private void submitTransaction(
		String transactionId,
		BigchainTransactionServices.TransactionresultHandler successCaller,
		BigchainTransactionServices.TransactionresultHandler faildedCaller
	) throws Exception {
		this.transaction.setSigned(true);
		this.transaction.setId(transactionId);
		TransactionsApi.sendTransaction(this.transaction, transactionResultHandler(successCaller, faildedCaller));
	}

	/**
	 * callback handler to handle result of transaction
	 *
	 * @return GenericCallback
	 */
	private GenericCallback transactionResultHandler(
		BigchainTransactionServices.TransactionresultHandler successCaller,
		BigchainTransactionServices.TransactionresultHandler faildedCaller
	) throws Exception {
		Transaction transaction = this.transaction;
		Logger logger = this.LOGGER;
		GenericCallback callback = new GenericCallback() {
			@Override
			public void transactionMalformed(Response response) {
				faildedCaller.onTransactionResult(transaction, response);
				logError(response.message());
			}

			@Override
			public void pushedSuccessfully(Response response) {
				successCaller.onTransactionResult(transaction, response);
				logSuccess();
			}

			@Override
			public void otherError(Response response) {
				faildedCaller.onTransactionResult(transaction, response);
				logError(response.message());
			}
		};
		return callback;
	}

	private void logSuccess() {
		this.LOGGER.info("Transaction submit successfully \n" +
			"Transaction id: " + this.transaction.getId());
	}

	private void logError(String message) {
		this.LOGGER.error("Transaction error: " + message + "\n" +
			"Transaction details: " + this.transaction);
	}
}

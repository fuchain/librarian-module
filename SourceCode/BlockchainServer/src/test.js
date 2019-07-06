const driver = require("./core/bigchaindb-driver");

// BigchainDB server instance (e.g. https://example.com/api/v1/)
import env from "@core/env";
const API_PATH = env.bigchainHost;

// Create a new keypair.
const alice = new driver.Ed25519Keypair();

// Construct a transaction payload
const tx = driver.Transaction.makeCreateTransaction(
    // Define the asset to store, in this example it is the current temperature
    // (in Celsius) for the city of Berlin.
    { city: "Berlin, DE", temperature: 22, datetime: new Date().toString() },

    // Metadata contains information about the transaction itself
    // (can be `null` if not needed)
    { what: "My first BigchainDB transaction" },

    // A transaction needs an output
    [
        driver.Transaction.makeOutput(
            driver.Transaction.makeEd25519Condition(alice.publicKey)
        )
    ],
    alice.publicKey
);

// Sign the transaction with private keys
const txSigned = driver.Transaction.signTransaction(tx, alice.privateKey);

// Send the transaction off to BigchainDB
const conn = new driver.Connection(API_PATH);

conn.postTransactionCommit(txSigned).then(retrievedTx =>
    console.log("Transaction", retrievedTx.id, "successfully posted.")
);

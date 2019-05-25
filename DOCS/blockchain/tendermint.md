![](https://pbs.twimg.com/profile_images/909728874560233472/cb9rWojF_400x400.jpg)

## **Introduction**

  - Tendermint is software for securely and consistently replicating an application on many machines. By securely, Tendermint works even if up to 1/3 of machines fail in arbitrary ways. By consistently, every non-faulty machine sees the same transaction log and computes the same state. Secure and consistent replication is a fundamental problem in distributed systems; it plays a critical role in the fault tolerance of a broad range of applications, from currencies, to elections, to infrastructure orchestration, and beyond.

  - Tendermint consists of two chief technical components: a blockchain consensus engine and a generic application interface. The **consensus engine**, called `Tendermint Core`, ensures that the same transactions are recorded on every machine in the same order. The **application** interface, called the `Application BlockChain Interface (ABCI)`, enables the transactions to be processed in any programming language. Unlike other blockchain and consensus solutions, which come pre-packaged with built in state machines (like a fancy key-value store, or a quirky scripting language), developers can use Tendermint for BFT state machine replication of applications written in whatever programming language and development environment is right for them.

  - Tendermint is designed to be easy-to-use, simple-to-understand, highly performant, and useful for a wide variety of distributed applications.


## **Intro to ABCI**

  - `Tendermint Core` (the **consensus engine**) communicates with the application via a socket protocol that satisfies the ABCI.

  - The ABCI consists of 3 primary message types that get delivered from the core to the application. The application replies with corresponding response messages.

    + The **DeliverTx** message is the work horse of the application. Each transaction in the blockchain is delivered with this message. The application needs to validate each transaction received with the **DeliverTx** message against the current state, application protocol, and the cryptographic credentials of the transaction. A validated transaction then needs to update the application state — by binding a value into a key values store, or by updating the UTXO database, for instance.

    + The **CheckTx** message is similar to **DeliverTx**, but it's only for validating transactions. Tendermint Core's mempool first checks the validity of a transaction with **CheckTx**, and only relays valid transactions to its peers. For instance, an application may check an incrementing sequence number in the transaction and return an error upon **CheckTx** if the sequence number is old. Alternatively, they might use a capabilities based system that requires capabilities to be renewed with every transaction.

    + It's probably evident that applications designers need to very carefully design their message handlers to create a blockchain that does anything useful but this architecture provides a place to start. The diagram below illustrates the flow of messages via ABCI.

      ![](https://www.tendermint.com/docs/assets/img/abci.3542de28.png)


## **Consensus Overview**

  - Tendermint is an easy-to-understand, mostly asynchronous, BFT consensus protocol. The protocol follows a simple state machine that looks like this:

    ![](https://www.tendermint.com/docs/assets/img/consensus_logic.e9f4ca6f.png)

  - Participants in the protocol are called **validators**; they take turns proposing blocks of transactions and voting on them. Blocks are committed in a chain, with one block at each **height**. A block may fail to be committed, in which case the protocol moves to the next **round**, and a new validator gets to propose a block for that height. Two stages of voting are required to successfully commit a block; these are **pre-vote** and **pre-commit**. A block is committed when more than 2/3 of validators pre-commit for the same block in the same round.

  - Validators may fail to commit a block for a number of reasons; the current proposer may be offline, or the network may be slow. Tendermint allows them to establish that a validator should be skipped. Validators wait a small amount of time to receive a complete proposal block from the proposer before voting to move to the next round. This reliance on a timeout is what makes Tendermint a weakly synchronous protocol, rather than an asynchronous one. However, the rest of the protocol is asynchronous, and validators only make progress after hearing from more than two-thirds of the validator set. A simplifying element of Tendermint is that it uses the same mechanism to commit a block as it does to skip to the next round.

  - The following diagram is Tendermint in a (technical) nutshell:

    ![](https://www.tendermint.com/docs/assets/img/tm-transaction-flow.258ca020.png)

## **References**

  - https://www.tendermint.com

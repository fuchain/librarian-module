  ![](https://cdn-images-1.medium.com/max/1200/1*sSXgRKOQ5ARG6mPFY1UoYQ.jpeg)

## **Concepts**

- **Transaction Model**

    The first thing to understand about BigchainDB is how structuring data. Traditional SQL databases structure data in tables. NoSQL databases use other formats to structure data such as JSON and key-values, as well as tables. At BigchainDB, data is structured as assets. Anything can be represented as an asset. An asset can characterize any physical or digital object that you can think of like a car, a data set or an intellectual property right.

    These assets can be registered on BigchainDB in two ways:

    + By users in `CREATE` transactions

    + Transferred (or updated) to other users in `TRANSFER` transactions

    Visualization of our transaction model:

    ![](https://www.bigchaindb.com/developers/guide/diagram.png)

- **Asset**

    An asset can represent any physical or digital object. It can be a physical object like a car or a house. Or it can be a digital object like a customer order or an air mile. An asset can have one or multiple owners, but it can also be its own owner. 

    An asset always contains data that is immutable. In the example above, the color and the registration number of a bicycle is immutable data.

    Depending on the context, an asset can represent many different things.

    **An asset as a claim**

    + An asset can represent an ownership claim for a particular object, e.g. it represents a claim that User ABC owns the bicycle with the number XYZ. This can be valid for any type of ownership.

    **An asset as a token**

    + An asset can also represent a token. BigchainDB supports divisible assets. This means, multiple assets can be issued and attributed to one overarching asset. This can for instance be interesting for token launches.

    **An asset as a versioned document**

    + An asset can also be a versioned document with the version stated in the metadata field. The version of this document can be updated on a continuous basis. Every time there is a new version of the document, it could be reflected in the metadata.

    **An asset as a time series**

    + An asset can also represent a time series of data. For instance, an IoT sensor records its own data. The IoT sensor is the asset and every submission of its data (e.g. temperature) is represented as an update in the metadata with the latest temperature that the IoT sensor measured.

    **An asset as a state machine**

    + An asset can also be a state machine where the state transition is represented in the metadata. Each time the machine changes its state, a transaction is triggered to update the metadata to the new state (possibility to listen to it with the WebSocket).

    **An asset as a permission (RBAC)**

    + Assets could also be: roles, users, messages, (and anything which can have multiple instances in a scenario — vehicles, reports, and so on).

  **Input**
  
  - Conceptually, an input is a pointer to an output of a previous transaction. It specifies to whom an asset belonged before and it provides a proof that the conditions required to transfer the ownership of that asset (e.g. a person needs to sign) are fulfilled. In a `CREATE` transaction, there is no previous owner, so an input in a `CREATE` transaction simply specifies who the person is that is registering the object (this is usually the same as the initial owner of the asset). In a `TRANSFER` transaction, an input contains a proof that the user is authorized to spend (transfer or update) this particular output. In practical terms, this means that with the input, a user is stating which asset (e.g. the bike) should be transferred. He also demonstrates that he or she is authorized to do the transfer of that asset.

  **Output**
  
  - A transaction output specifies the conditions that need to be fulfilled to change the ownership of a specific asset. For instance: to transfer a bicycle, a person needs to sign the transaction with his or her private key. This also implicitly contains the information that the public key associated with that private key is the current owner of the asset.

  - Note that a transaction can also have multiple outputs. These are called divisible assets.

  - The output can also contain complex conditions (e.g. multiple signatures of multiple people) to acquire ownership.

  **Metadata**

  - The metadata field allows users to add additional data to a transaction. This can be any type of data, like the age of a bicycle or the kilometers driven. The good thing about the metadata is that it can be updated with every transaction. In contrast to the data in the asset field, the metadata field allows to add new information to every transaction.

  **Transaction ID**

  The ID of a transaction is a unique hash that identifies a transaction. It contains all the information about the transaction in a hashed way.


## **Consensus Engine**

  BigchainDB uses **Tendermint** for consensus and transaction replication, and Tendermint is **Byzantine Fault Tolerant** (`BFT`).

  Tendermint is software for securely and consistently replicating an application on many machines. By securely, Tendermint works even if up to 1/3 of machines fail in arbitrary ways. By consistently, every non-faulty machine sees the same transaction log and computes the same state. Secure and consistent replication is a fundamental problem in distributed systems; it plays a critical role in the fault tolerance of a broad range of applications, from currencies, to elections, to infrastructure orchestration, and beyond.

  The ability to tolerate machines failing in arbitrary ways, including becoming malicious, is known as Byzantine fault tolerance (BFT). The theory of BFT is decades old, but software implementations have only became popular recently, due largely to the success of "blockchain technology" like Bitcoin and Ethereum. Blockchain technology is just a re-formalization of BFT in a more modern setting, with emphasis on peer-to-peer networking and cryptographic authentication. The name derives from the way transactions are batched in blocks, where each block contains a cryptographic hash of the previous one, forming a chain. In practice, the blockchain data structure actually optimizes BFT design.

  Tendermint consists of two chief technical components: a blockchain consensus engine and a generic application interface. The consensus engine, called Tendermint Core, ensures that the same transactions are recorded on every machine in the same order. The application interface, called the Application BlockChain Interface (ABCI), enables the transactions to be processed in any programming language. Unlike other blockchain and consensus solutions, which come pre-packaged with built in state machines (like a fancy key-value store, or a quirky scripting language), developers can use Tendermint for BFT state machine replication of applications written in whatever programming language and development environment is right for them.

  Tendermint is designed to be easy-to-use, simple-to-understand, highly performant, and useful for a wide variety of distributed applications.


## **The life of BigchainDB Transaction**

  - **Sending a Transaction to a BigchainDB Network**
  
      A transaction can be sent to a BigchainDB network using the BigchainDB HTTP API. More specifically, one would use one of the following endpoints, with the transaction in the body of the HTTP request

      ```
        POST /api/v1/transactions

        POST /api/v1/transactions?mode=async

        POST /api/v1/transactions?mode=sync

        POST /api/v1/transactions?mode=commit
      ```

      A BigchainDB driver could also be used to post a transaction. The HTTP request (containing the trans- action) can be sent to **ANY** of the nodes in the BigchainDB network, or even more than one. Picture below illustrates the main components in a four-node BigchainDB 2.0 network, and how they communicate with each other.

      ![](https://docs.bigchaindb.com/en/latest/_images/schemaDB.png)

  - **Arrival of a Transaction at a Node**

      Assume that the HTTP request arrives successfully at the Gunicorn web server inside a BigchainDB node. There is a Python method for handling that endpoint. That method checks the validity of the transaction.
      
      - If it’s not valid, then that’s the end of the story for the transaction, the HTTP response status code is 400 (Error), and the response body gives some information about what was invalid.
      
      - If the transaction is valid, then it’s converted to Base64 and put into a new JSON string with some other information (such as the mode). BigchainDB then sends that string to the local Tendermint instance in the body of an HTTP POST request. That request uses the Tendermint Broadcast API (Tendermint has other APIs).

  - **Arrival of a Transaction at a Tendermint Instance**

      When a transaction is sent to a Tendermint node, it will run via CheckTx against the application. If it passes CheckTx, it will be included in the mempool, broadcast to other peers, and eventually included in a block.

      Since there are multiple phases to processing a transaction, they offer multiple endpoints to broadcast a transaction:

      `/broadcast.tx.async`: will return right away without waiting to hear if the transaction is even valid

      `/broadcast.tx.sync`: will return with the result of running the transaction through `CheckTx`

      `/broadcast.tx.commit`: will wait until the transaction is committed in a block or until some timeout is reached, but will return right away if the transaction does not pass CheckTx

      The benefit of using `broadcast.tx.commit` is that the request re- turns after the transaction is committed (i.e. included in a block), but that can take on the order of a second.

      For a quick result, use `broadcast,tx.sync`, but the transaction will not be committed until later, and by that point its effect on the state may change.

  - **The above text requires some explanation**

      `CheckTx` is an API that Tendermint expects BigchainDB to implement. It’s explained below.

      If someone uses BigchainDB’s POST `/api/v1/transactions?mode=async` endpoint to send the transaction, then the Tendermint `/broadcast.tx.async` endpoint will be used. Similar things can be said for the `sync` and `commit` modes. If no mode was specified, then the default is `async`

      Every Tendermint instance has a local mempool (memory pool) of transactions which have passed initial validation, but haven’t been included in a block yet.

      When Tendermint wants to determine if a transaction is valid, it sends the transaction to BigchainDB using a CheckTx request. It expects BigchainDB to implement CheckTx, and several other message types, all of which are explained in the ABCI pecification (ABCI stands for Application BlockChain Interface). In particular, BigchainDB implements:

      - `CheckTx`: executed before a new tx is stored to the current node's mempool
      - `DeliverTx`: executed before a new tx is broadcasted to other nodes' mempool
      - `Commit`: add new block to chain by storing collection of assets & collection of metadatas to mongoDB **(facilitate the text search)**


## **Basic local deployment of BigchainDB**

  Install `git`

  Install `docker`

  Setup `ssh`

  ```
  git clone git@github.com:bigchaindb/bigchaindb.git

  cd bigchaindb

  docker-compose build

  docker-compose up
  ```

  Open browser, and type `http://localhost:9984`
  

## **References**

  - https://www.bigchaindb.com

  - https://github.com/bigchaindb/BEPs/tree/master/13#transaction-validation

  - https://www.tendermint.com

  - https://github.com/tendermint/abci/blob/master/specification.rst

## **Deep Concern**

  - Transaction per second: https://blog.bigchaindb.com/and-were-off-to-the-races-1aff2b66567c

  - Transaction validation rule: https://github.com/bigchaindb/BEPs/tree/master/13
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

  ![](https://docs.bigchaindb.com/en/latest/_images/schemaDB.png)


## **Basic local development of BigchainDB**

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

  - https://www.tendermint.com


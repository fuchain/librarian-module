![](https://steemitimages.com/640x0/https://www.smartdatacollective.com/wp-content/uploads/2017/06/blockchain-explain-smartdata-collective-1024x614.jpg)

## **Concepts**
- **P2P Network**

    A Peer to Peer (abbreviated to `P2P`) network is a very important part of how blockchain technology works.
    

    Each peer (a “peer” being a computer system on the network) is considered equal and are commonly referred to as `nodes`. A peer makes a portion of computing resources such as disk storage, processing power or network bandwidth, directly available to other participants without the need for any central coordination by servers or stable hosts.

    ![](https://lisk.io/content/5-academy/2-blockchain-basics/4-how-does-blockchain-work/1-what-is-a-peer-to-peer-network/3-p2p-vs-centralised.jpg)


- **Cryptography**

    A lot of people use cryptography on a daily basis without realizing it as many popular messaging apps use encryption. It is also one of the core aspects of blockchain technology. 

    Cryptography is the method of disguising and revealing, otherwise known as encrypting and decrypting, information through complex mathematics. This means that the information can only be viewed by the intended recipients and nobody else. The method involves taking unencrypted data, such as a piece of text, and encrypting it using a mathematical algorithm, known as a `cipher`. This produces a ciphertext, a piece of information that is completely useless and nonsensical until it is decrypted. This method of encryption is known as symmetric-key cryptography.

    ![](https://lisk.io/content/5-academy/2-blockchain-basics/4-how-does-blockchain-work/2-blockchain-cryptography-explained/5-julius-caesar-encryption.gif)

    Blockchain technology utilizes `cryptography` as a means of  ensuring transactions are done safely, while securing all information and storages of value. Therefore, anyone using blockchain can have complete confidence that once something is recorded on a blockchain, it is done so legitimately and in a manner that preserves security.

    Despite being founded upon a similar framework, the type of cryptography used in blockchain, namely public-key cryptography, is considerably better suited to the functions associated with the technology than symmetric-key cryptography.

    ![](https://lisk.io/content/5-academy/2-blockchain-basics/4-how-does-blockchain-work/2-blockchain-cryptography-explained/4-symmetric-key-cryptography.jpg)

    Public-key cryptography, also known as asymmetric cryptography, represents an improvement on standard symmetric-key cryptography as it allows information to be transferred through a public key that can be shared with anyone.

    A combination of a users `public key` and private key encrypt the information, whereas the recipients `private key` and sender's public key decrypt it. It is impossible to work out what the private key is based on the public key. Therefore, a user can send their public key to anyone without worrying that someone will gain access to their private key. The sender can encrypt files that they can be sure will only be decrypted by the intended party.

    ![](https://lisk.io/content/5-academy/2-blockchain-basics/4-how-does-blockchain-work/2-blockchain-cryptography-explained/6-public-key-cryptography-1.jpg)


- **Digital Signature**

    Digital signatures provide three key advantages of storing and transferring information on a blockchain. First of all, they guarantee integrity. Theoretically, data that is being sent can be altered without necessarily even being seen by a hacker. However, if this does happen in the case of data that is accompanied by a digital signature, the signature  would become invalid. Therefore digitally signed data, that is encrypted, is not only safe from being seen but will also reveal if it has been tampered with, cementing its incorruptibility.

    Digital signatures not only secure data but also the identity of the individual sending it. Ownership of a digital signature is always bound to a certain user and as such, one can be sure that they are communicating with whom they intend to. 

    ![](https://lisk.io/content/5-academy/2-blockchain-basics/4-how-does-blockchain-work/3-digital-signatures/11-digital-signature.jpg)


- **Nodes**

    A node is a device on a blockchain network, that is in essence the foundation of the technology, allowing it to function and survive.  Nodes are distributed across a widespread network and carry out a variety of tasks.

    A node can be any active electronic device, including a computer, phone or even a printer, as long as it is connected to the internet and as such has an IP address. The role of a node is to support the network by maintaining a copy of a blockchain and, in some cases, to process transactions.

    ![](https://lisk.io/content/5-academy/2-blockchain-basics/4-how-does-blockchain-work/4-nodes/16-nodes.jpg)

    A node can either be a communication endpoint or a point of communication redistribution, linking to other nodes. Every node on the network is considered equal, however certain nodes have different roles in the manner in which they support the network. **Not all nodes will store a full copy of a blockchain or validate transactions**.

    All nodes use the same consensus protocol to remain compatible with each other. It is the nodes on the network that confirm and validate transactions, putting them into blocks. Nodes always come to their own conclusion on whether a transaction is valid and should be added to a `block` with other transactions, irrespective of how other nodes act.

    ![](https://lisk.io/content/5-academy/2-blockchain-basics/4-how-does-blockchain-work/4-nodes/23-nodes.jpg)

- **Hashing**

    The reliability and integrity of blockchain is rooted in there being no chance of any fraudulent data or transactions, such as a double spend, being accepted or recorded. A cornerstone of the technology as a whole and the key components in maintaining this reliability is hashing.

    Hashing drastically increases the `security` of the data. Anyone who may be trying to decrypt the data by looking at the hash will not be able to work out the length of the encrypted information based on the hash. A cryptographic hash function needs to have several crucial qualities to be considered useful, these include:

    + Impossible to produce the same hash value for differing inputs

    + The same message will always produce the same hash value

    + Quick to produce a hash for any given message

    + Impossible to determine input based on hash value

    + Even the slightest change to an input completely alters the hash

    Hashing secures data by providing certainty that it hasn’t been tampered with before being seen by the intended recipient. So, as an example, if you downloaded a file containing sensitive information, you could run it through a hashing algorithm, calculate the hash of that data and compare it to the one shown by whoever sent you the data. If the hashes don’t match, you can be certain that the file was altered before you received it.

    ![](https://lisk.io/content/5-academy/2-blockchain-basics/4-how-does-blockchain-work/5-what-is-hashing/11d_hashing_comparaison.gif)

- **Blockchain Hashing**

    In blockchain, hashes are used to represent the current state of the world, or to be more precise, the state of a blockchain. As such, the input represents everything that has happened on a blockchain, so every single transaction up to that point, combined with the new data that is being added. What this means is that the output is based on, and therefore shaped by, all previous transactions that have occurred on a blockchain.

    As mentioned, the slightest change to any part of the input results in a huge change to the output; in this lies the irrefutable security of blockchain technology. Changing any record that has previously happened on a blockchain would change all the hashes, making them false and obsolete. This becomes impossible when the `transparent` nature of blockchain is taken into account, as these changes would need to be done in plain sight of the whole network.

    The first `block` of a blockchain, known as a `genesis block`, contains its transactions that, when combined and validated, produce a `unique hash`. This hash and all the new transactions that are being processed are then used as input to create a brand new hash that is used in the next block in the chain. This means that each block links back to its previous block through its hash, forming a chain back to the genesis block, hence the name blockchain. In this way, transactions can be added securely as long as the nodes on the network are in consensus on what the hash should be.

    ![](https://steemitimages.com/DQmbN1vkCLMMY111CgV3fgpjNqJwu5QH4NDLDnzizRJQnQB/image.png)

    Data structures are a specialized way of storing data. The two foremost hashing objects carrying out this function are pointers and linked lists. Pointers store addresses as variables and as such point to the locations of other variables. Linked lists are a sequence of blocks connected to one another through pointers. As such, the variable in each pointer is the address of the next node, with the last node having no pointer and the pointer in the first block, the genesis block, actually lying outside of the block itself. At its simplest, a blockchain is simply a linked list of recorded transactions pointing back to one another through hash pointers.

    Hash pointers are where blockchain sets itself apart in terms of certainty as pointers not only contain the address of the previous block, but also the hash data of that block too. As described earlier, this is the foundation of the secure nature of blockchain. For example, if a hacker wanted to attack the ninth block in a chain and change its data, he would have to alter the data in all following blocks, as their hash would also change. In essence, this makes it impossible to alter any data that is recorded on a blockchain.

    ![](https://lisk.io/content/5-academy/2-blockchain-basics/4-how-does-blockchain-work/5-what-is-hashing/18-data-structure.jpg)

    Hashing is of the core fundamentals and foremost aspects of the immutable and defining potential of blockchain technology. It preserves the authenticity of the data that is recorded and viewed, and as such, the integrity of a blockchain as a whole. It is one of the more technical aspect of the technology, however understanding it is a solid step in understanding how blockchain functions and the immeasurable potential and value that it has.


- **Consensus Protocal**

    Blockchain consensus protocols are what keep all the `nodes` on a network synchronized with each other, while providing an answer to the question: how do we all make sure that we agree on what the truth is?

    As a term, ‘consensus’ means that the nodes on the network agree on the same state of a blockchain, in a sense making it a self-auditing ecosystem. This is an absolutely crucial aspect of the technology, carrying out two key functions. Firstly, consensus protocols allow a blockchain to be updated, while ensuring that every `block` in the chain is true as well as keeping participants incentivized. Secondly, it prevents any single entity from controlling or derailing the whole blockchain system. The aim of consensus rules is to guarantee a single chain is used and followed.

    Consensus rules are a specific set of rules that nodes on the network will ensure a block follows when validating that block and the transactions within it. The key requirement to achieve a consensus is a unanimous acceptance between nodes on the network for a single data value, even in the event of some of the nodes failing or being unreliable in any way.

    ![](https://lisk.io/content/5-academy/2-blockchain-basics/4-how-does-blockchain-work/6-consensus-protocols/7-consensus-protocol.gif)

    Consensus protocols are a key aspect in allowing a blockchain to function and exist. After all, as at its core a blockchain is a ledger of information it is paramount that there is absolute certainty that the information that is being stored is honest and accurate. 


## **Blockchain Evolution**

- **Blockchain 1.0: Currency**
  
    The implementation of distributed ledger technology (DLT) led to its first and obvious application: cryptocurrencies. This allows financial transactions based on blockchain technology or DLT (for the sake of simplicity often seen as synonyms) to be executed with `Bitcoin` being the most prominent example in this segment. It is being used as “cash for the Internet”, a digital payment system and can be seen as the enabler of an “Internet of Money”.

- **Blockchain 2.0: Smart Contracts**

    The new key concept are Smart Contracts, small computer programs that “live” in the blockchain. They are autonomous computer programs that execute automatically and conditions defined beforehand such as the facilitation, verification or enforcement of the performance of a contract. One big advantage this technlogy offers, is the blockchain making it impossible to tamper or hack Smart Contracts. So Smart Contracts reduce the cost of verification, exceution, arbitration and fraud prevention and allow transparent contract definition overcoming the moral hazard problem.

    Most prominent in this field is the **Ethereum Blockchain** — with its aim at allowing the implementation of Smart Contracts.

- **Blockchain 3.0: DApps**

    DApp is an abbreviated form for decentralized application avoiding centralized infrastructure. It uses decentralized storage and decentralized communication, so most DApps have their backend code running on a decentralized peer-to-peer network, a blockchain. In contrast, a traditional app has its backend code running on centralized servers. A DApp can have frontend code and user interfaces written in any language that can make calls to its backend, like a traditional App. But a Dapp can have its frontend hosted on decentralized storages such as Ethereums Swarm.

    `DApp = frontend + contracts` (running i.e. on Ethereum)

- **Blockchain 4.0: Making blockchain usable in industry (4.0)**

    With the foundations laid by the previous versions, for us Blockchain 4.0 describes solutions and approaches that make blockchain technology usable to business demands. Especially **Industry 4.0 demands**.

    Industry 4.0 meaning in short terms automation, enterprise resource planning, and integration of different execution systems. However, this industrial revolution demands an increasing degree of trust and privacy protection — this is where blockchain kicks in.

    When adding blockchain to IT systems one ends up with business integration, allowing Cross-System/Cross-Blockchain business processes, i.e. machines safely and autonomously placing an order for their replacement parts to arrive. Supply chain management, approval workflows, financial transactions and condition based payments, `IoT` data collection, health management and asset management are just a few examples of areas that can be empowered by blockchain technology.

    Blockchain 4.0 means, making Blockchain 3.0 usable in real-life business scenarios. Satisfying Industry 4.0 demands by making blockchain promises come to life.


## **References**

  - https://lisk.io/academy/blockchain-basics

  - https://steemit.com/blockchain/@thesumitbanik/how-does-a-blockchain-work-simply-explained

  - https://medium.com/@UnibrightIO/blockchain-evolution-from-1-0-to-4-0-3fbdbccfc666

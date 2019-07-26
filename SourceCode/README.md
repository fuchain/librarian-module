# Librarian Blockchain-based

Librarian Module is the book-share without warehouse based on Blockchain platform. This repository is for the whole system.

## Run and Test

Running and testing the latest version of Librarian Module cannot be easier. Make sure you have a recent version of [Docker Compose](https://docs.docker.com/compose/install/) installed. When you are ready, fire up a terminal and run:

```text
$ git clone git@github.com:huynhminhtufu/librarian-module.git
$ cd librarian-module
$ make run
```

You can you our BigchainDB testnet at `https://testnet.bigchain.fptu.tech` or deal with your own at `http://localhost:9984/`.

There are also other commands you can execute:

- `make start`: Run Librarian Module from source and daemonize it.
- `make stop`: Stop Librarian Module.
- `make build`: Build all services.
- `make reset`: Clear all services, volumns and networks.

# Deploy production

Easy deployment to production with Docker Swarm:

Make sure you have built images on Docker Hub and `docker-stack.yml` file, then run:

```
$ docker swarm init
$ docker stack deploy -c docker-stack.yml librarian
```

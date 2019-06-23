# Librarian Module

Librarian Module is the book-share without warehouse platform. This repository is for the whole services.

## Run and Test

Running and testing the latest version of Librarian Module cannot be easier. Make sure you have a recent version of [Docker Compose](https://docs.docker.com/compose/install/) installed. When you are ready, fire up a terminal and run:

```text
$ git clone git@github.com:huynhminhtufu/librarian-module.git
$ cd librarian-module
$ make run
```

You can you our BigchainDB testnet at `http://localhost:9984/`.

There are also other commands you can execute:

- `make start`: Run Librarian Module from source and daemonize it (stop it with `make stop`).
- `make stop`: Stop Librarian Module.

To view all commands available, run `make help`.

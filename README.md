# BSMW - Book Share Management without warehouse

Librarian Module is the book-share without warehouse based on **Blockchain database**.

FPT University Summer 2019 - Capstone Project

## Stacks:

- Javascript
- Golang
- Node.js, Express
- Vue.js
- React, Redux
- Socket.io
- MongoDB
- BigchainDB
- Redis
- Docker, Docker Compose, Docker Swarm
- Ansible script

# Fast development bootstrap (Ubuntu machine)

Setup full development environment (from fresh Ubuntu machine)

```bash
$ curl -s http://git.fptu.tech/fuchain/librarian-module/raw/branch/develop/scripts/reset.sh | sudo bash /dev/stdin
```

Setup a 4-node FUChain network on Docker Engine

```
curl -s http://git.fptu.tech/fuchain/librarian-module/raw/branch/develop/scripts/stack.sh | sudo bash /dev/stdin
```

## Development

Running and testing the latest version of Librarian Module cannot be easier. Make sure you have a recent version of [Docker Compose](https://docs.docker.com/compose/install/) installed. When you are ready, fire up a terminal and run:

```bash
$ git clone git@github.com:huynhminhtufu/librarian-module.git
$ cd librarian-module
$ make run
```

You can use our BigchainDB testnet at `https://testnet.bigchain.fptu.tech` or deal with your own at `http://localhost:9984/`.

There are also other commands you can execute:

- `make start`: Run Librarian Module from source and daemonize it.
- `make stop`: Stop Librarian Module.
- `make build`: Build all services.
- `make reset`: Clear all services, volumns and networks.

## Production

Easy deploy to production with **Docker Swarm**:

Make sure you have built images on your Docker Hub and `docker-stack.yml` file, then run:

```
$ docker swarm init
$ docker stack deploy -c docker-stack.yml librarian
```

## Contributors:

1. Huynh Minh Tu
2. Pham Hoang Linh
3. To Quoc Cuong
4. Doan Vu Phong

Supervisor: Mr. Kieu Trong Khanh

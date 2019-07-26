# Blockchain Server Node.js

A starter repository for scaffolding capstone projects.

## Materials

Some main dependencies of this repository:

-   Express.js Server
-   MongoDB Driver
-   ES6
-   CORS + Bcrypt + Authentication
-   Cryptos + BigchainDB Driver

## Installation

Prerequisites: Node.js (>=6.x, 8.x preferred), npm and Git.

Install dependencies via npm:

```
$ npm install
```

Config environment at `.env` file, example at `.env.example`.

### Development

Running in development mode with `nodemon`:

```
$ npm run serve
```

### Production

Build production dist files and run:

```
$ npm run build && npm run start
```

Or build production Docker image:

```
$ docker build -t ibrarian-blockchainserver . && docker run -d --name ibrarian-blockchainserver -p 5000:5000 ibrarian-blockchainserver:latest
```

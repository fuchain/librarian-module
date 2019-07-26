# I/O Server Node.js

A starter repository for scaffolding capstone projects.

## Materials

Some main dependencies of this repository:

-   Express.js Server
-   Mongoose ODM
-   ES6
-   CORS + Bcrypt + Authentication
-   WebSocket with Socket.io
-   Sendgrid for mailing service

## Installation

Prerequisites: Node.js (>=6.x, 8.x preferred), npm version 3+ and Git.

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
$ docker build -t ioserver . && docker run -d --name ioserver -p 5002:5002 ioserver:latest
```

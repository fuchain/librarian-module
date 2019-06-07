# Node.js RESTful API with Express

A starter repository for scaffolding capstone projects.

## Materials

Some main dependencies of this repository:

-   Express.js Server
-   Sequelize.js ORM
-   ES6
-   CORS + Bcrypt + Authentication

## Installation

Prerequisites: Node.js (>=6.x, 8.x preferred), npm version 3+ and Git.

If you don't have Yarn, install it by running:

```
$ npm install -g yarn
```

Install dependencies via yarn:

```
$ yarn
```

Config environment at `.env` file, example at `.env.example`.

### Development

Running in development mode with `nodemon`:

```
$ yarn serve
```

### Production

Build production dist files and run:

```
$ yarn build && yarn start
```

Or build production Docker image:

```
$ docker build -t nodejs-api . && docker run -d --name nodejs-api -p 5000:3000 nodejs-api:latest
```

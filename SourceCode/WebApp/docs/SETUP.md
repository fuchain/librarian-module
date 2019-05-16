# Setup FPTU Librarian Webapp for development

You can setup development environment by two ways: use `Docker` or `Node.js`, use Docker is fast and simpler.

## Docker

Install DockerCE from [https://docs.docker.com/install/](https://docs.docker.com/install/).

```
$ docker-compose up
```

### Node.js

Install Node.js version >= 8, and Yarn:

```
$ npm install -g yarn
```

Fetch dependency by yarn:

```
$ yarn
```

Start development server:

```
$ yarn serve
```

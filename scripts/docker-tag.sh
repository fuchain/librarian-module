#!/usr/bin/env bash

docker tag librarian-webapp-dev:latest fptudev/webapp:latest
docker tag librarian-visualizeapp-dev:latest fptudev/visualizeapp:latest
docker tag librarian-pairworker-dev:latest fptudev/pairworker:latest
docker tag librarian-bcserver-dev:latest fptudev/bcserver:latest
docker tag librarian-ioserver-dev:latest fptudev/ioserver:latest
docker tag bigchaindb_bigchaindb:latest fptudev/bigchaindb:latest

docker push fptudev/webapp:latest
docker push fptudev/visualizeapp:latest
docker push fptudev/pairworker:latest
docker push fptudev/bcserver:latest
docker push fptudev/ioserver:latest
docker push fptudev/bigchaindb:latest

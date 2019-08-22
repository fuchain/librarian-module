#!/usr/bin/env bash

docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)
docker system prune --volumes

cd /home
rm -rf fuchain
rm -rf librarian-module
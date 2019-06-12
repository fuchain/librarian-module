#!/bin/sh
ssh root@ssh.fptu.tech <<EOF
    cd /home/capstone/librarian-module/SourceCode/WebApp || exit 1
    git checkout . || exit 1
    git pull origin develop
    docker build -t librarian-webapp . || exit 1
    docker stop librarian-webapp || exit 1
    docker rm librarian-webapp || exit 1
    docker run -d --name librarian-webapp -p 3000:80 librarian-webapp:latest || exit 1
    exit
EOF
#!/bin/sh
ssh root@ssh.fptu.tech <<EOF
    cd /home/capstone/librarian-module/SourceCode/WebServer
    git checkout . || exit 1
    git pull origin develop
    docker build -t librarian-webserver . || exit 1
    docker stop librarian-webserver || exit 1
    docker rm librarian-webserver || exit 1
    docker run -d --name librarian-webserver -p 5000:8080 librarian-webserver:latest || exit 1
    exit
EOF
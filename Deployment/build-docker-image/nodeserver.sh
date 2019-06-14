#!/bin/sh
ssh root@ssh.fptu.tech <<EOF
    cd /home/capstone/librarian-module/SourceCode/NodeServer || exit 1
    git checkout . || exit 1
    git pull origin develop
    docker build -t librarian-nodeserver . || exit 1
    docker stop librarian-nodeserver || exit 1
    docker rm librarian-nodeserver || exit 1
    docker run -d --name librarian-nodeserver --network=host -p 5002:3000 librarian-nodeserver:latest || exit 1
    exit
EOF
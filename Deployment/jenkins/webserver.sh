ssh root@aws.fptu.tech <<EOF
    cd /home/capstone/librarian-module/SourceCode/WebServer
    git checkout .
    git pull origin develop
    docker build -t librarian-webserver . || exit 1
    docker tag librarian-webserver:latest fptu/librarian-webserver:latest
    docker push fptu/librarian-webserver:latest
    exit
EOF

ssh root@ssh.fptu.tech <<EOF
    docker service update --image=fptu/librarian-webserver:latest librarian_webserver
    exit
EOF
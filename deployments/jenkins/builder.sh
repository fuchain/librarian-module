SERVICE_NAME="webapp"

ssh root@builder.fptu.tech <<EOF
    cd /home/librarian-module/$SERVICE_NAME
    git checkout .
    git pull origin develop
    docker build -t fptu/$SERVICE_NAME:latest . || exit 1
    docker push fptu/$SERVICE_NAME:latest
    exit
EOF

ssh root@ssh.fptu.tech <<EOF
    docker service update --image=fptu/$SERVICE_NAME:latest librarian_$SERVICE_NAME
    exit
EOF
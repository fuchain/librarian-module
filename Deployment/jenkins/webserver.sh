ssh root@aws.fptu.tech <<EOF
    cd /home/capstone/librarian-module/SourceCode/WebServer
    git checkout .
    git pull origin develop
    docker build -t {service_name} . || exit 1
    docker tag {service_name}:latest fptu/{service_name}:latest
    docker push fptu/{service_name}:latest
    exit
EOF

ssh root@ssh.fptu.tech <<EOF
    docker service update --image=fptu/{service_name}:latest librarian_{service_name}
    exit
EOF
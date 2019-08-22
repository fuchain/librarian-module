#!/usr/bin/env bash

sudo apt update

sudo apt install make -y
sudo apt install wget -y
sudo apt install unzip -y

echo "Installing Docker and Docker Compose"
sudo apt install apt-transport-https ca-certificates curl software-properties-common -y
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu bionic stable"
sudo apt update
apt-cache policy docker-ce
sudo apt install docker-ce -y
sudo curl -L "https://github.com/docker/compose/releases/download/1.24.1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

echo "Installing FUChain"
cd /home
git clone https://git.fptu.tech/fuchain/fuchain.git
cd fuchain
wget http://builder.fptu.tech/dump1.zip
unzip -o dump1.zip
rm -rf dump1.zip
rm -rf __MACOSX
make start

echo "Installing Librarian Module"
cd /home
git clone https://git.fptu.tech/fuchain/librarian-module.git
cd librarian-module
sudo make start
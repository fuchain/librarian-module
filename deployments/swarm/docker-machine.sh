for i in 1 2 3 4; do docker-machine create --driver digitalocean \
--digitalocean-image ubuntu-18-04-x64 \
--digitalocean-size s-1vcpu-2gb \
--digitalocean-region sgp1 \
--digitalocean-ssh-key-fingerprint b7:67:42:eb:a5:cc:6e:6f:ae:e7:06:cf:eb:cc:1b:02 \
--digitalocean-access-token e3165e76f26e4fb40b8f26828f987a92009561864caced81a9ca458d18ff5bb4 node$i; done
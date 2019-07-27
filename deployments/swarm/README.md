# Deploy services to Docker Swarm

```
$ docker swarm init
$ docker stack deploy -c docker-stack.yml librarian
```

Or create production node in Digital Ocean by running script:

```
$ $DOTOKEN=[your_token]
$ sh docker-machine.sh
```

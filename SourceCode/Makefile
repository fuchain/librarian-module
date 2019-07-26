DOCKER := docker
DC := docker-compose
ECHO := /usr/bin/env echo

IS_DOCKER_COMPOSE_INSTALLED := $(shell command -v docker-compose 2> /dev/null)

build: check-deps ## Build services from source (stop it with ctrl+c)
	@$(DC) build

run: check-deps ## Run services from source (stop it with ctrl+c)
	@$(DC) up

start: check-deps ## Run services from source and daemonize it (stop with `make stop`)
	@$(DC) up -d

stop: check-deps ## Stop services
	@$(DC) stop

reset: check-deps ## Stop and REMOVE all containers. WARNING: you will LOSE all data stored in BigchainDB.
	@$(DC) down

check-deps:
ifndef IS_DOCKER_COMPOSE_INSTALLED
	@$(ECHO) "Error: docker-compose is not installed"
	@$(ECHO)
	@$(ECHO) "You need docker-compose to run this command. Check out the official docs on how to install it in your system:"
	@$(ECHO) "- https://docs.docker.com/compose/install/"
	@$(ECHO)
	@$(DC) # docker-compose is not installed, so we call it to generate an error and exit
endif
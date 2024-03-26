.PHONY: run stop build

run:
	docker compose -f docker/docker-compose.yml up -d
stop:
	docker compose -f docker/docker-compose.yml down
build:
	docker compose -f docker/docker-compose.yml build --no-cache
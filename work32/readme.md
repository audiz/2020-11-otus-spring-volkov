Wrap the app in a docker container

Goal: deploy the application in a modern DevOps stack Result: application wrapper in Docker

Attention! The task is performed on the basis of any Web application made

Wrap the application in a docker container. Dockerfile is usually located in the root of the repository.
The image must include JAR applications. Building in a container is recommended, but not required.
You don't need to wrap the database in your own container (unless you use custom plugins)
Configure communication between containers, using docker-compose
Optional: do this in a local cube.
It is desirable to implement the application using all the Best Practices of Docker (logging in stdout, etc.)
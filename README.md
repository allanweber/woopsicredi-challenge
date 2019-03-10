# Woopsicredi challenge

## Structure
It is composed for the:
* domain: basically the entities, dto, exceptions and services
* infrastructure: services injected and repositoriws
* config: spring, mongo, swagger and exception handlers configurations
* application.api: apis rest to consume the app

## Stack
* Java
* MongoDb + Spring Starter Mongo
* Spring Boot/Stater
* JUnit + Spring Boot Test
* swagger + swagger-ui
* Model Mapper
* Apache commons lang

## Tests
95% line coverage, could have more but I covered just what I think is important.

## Further improvements

* Create the interface to consume the APIs
* Logs
* Authentication
* Mutation coverage with Pitest for example

# Run the app

Must have Docker instaled

## Run All Together

* go to api directory and run: **gradlew build**
* In the root path run the docker compose file: **docker-compose up**
* access the url **localhost:8080** or swagger **http://localhost:8080/swagger-ui.html**

## Run Separately

* Create a docker instance for MongoDb in the properly port 27017, run de command: **docker run -p 27017:27017 mongo**
* go to api directory and run: **gradlew bootRun**
* access the url **localhost:8080** or swagger **http://localhost:8080/swagger-ui.html**

**To Test the app**

* go to api directory and run :  **gradlew test**

**To use the application**

* Manage ruling: **http://localhost:8080/ruling/**

* Manage Votes: **http://localhost:8080/voting/**

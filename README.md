# Point Poker
This repo is the backend service for the Point Poker application, written in Java. It does contain the ui build (written in angular) as well, though the ui project in angular is a seperate repo.

## Project structure
The application is a Spring Boot service with [Gradle](https://gradle.org/) as build-tool.


## Requirements
- JDK 8+ (The project includes a gradle wrapper so no need to install gradle)

### Recommended tools
- IntelliJ IDEA Community/Ultimate (though any Java/Gradle capable IDE will suffice)

## Architecture
The project conforms to Hexagonal Architecture, see [ARCHITECTURE.md](ARCHITECTURE.md)

## Testing
Integration test cases for 'Service Controller'

To run tests:
```shell
./gradlew test
```

## Running
The server can be started by executing gradle task `bootRun`
```shell
./gradlew bootRun
```
which will start the server listening on `http://localhost:8081/`

You can also start the server by running the `PointPokerApplication` main class in IDE.
# Architecture

This backend service is a [Spring Boot](https://spring.io/projects/spring-boot) [MVC](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html) service written in Java(v8). It is designed as a [Hexagonal Architecture](https://dzone.com/articles/hexagonal-architecture-what-is-it-and-how-does-it) application.
[Spring Security](https://spring.io/projects/spring-security) framework is also used handle the security aspects of the REST services. As of writing, Basic Auth strategy has been implemented.

## High-level overview

This repo is a mult-module project with three sub modules: pointpoker-app, pointpoker-domain and pointpoker-infra

###pointpoker-domain
This is the core of the application which contains the entire domain specific logic/flows. The domain objects and services exist in this module with interfaces to communicate with other modules.

###pointpoker-infra
This module handles the DB part of the application. As of writing, this app doesn't rely on any third party DB to be available. Rather an in-memory repository implementation handles the persistence part. (see `InMemoryPokerSessionRepositoryImpl` class).
This module has direct dependency on the domain module.

###pointpoker-app
This module contains the rest endpoints and the corresponding security configuration. It has direct dependency on the infra module.

## API Spec

Spring fox library is used to generate the Open API Spec. After starting the server hit  this url for spec: http://localhost:8081/v2/api-docs
For the Spec GUI hit this url: http://localhost:8081/swagger-ui/


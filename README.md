# desire-mgmt

:warning::warning::construction::construction:  Microservices in a multimodule Maven project with servers, REST API, and MVC system

**Pending**
- MVC Microservice 04

**Complete**
- Eureka-server
- Microservice ms-products with its own PostgreSQL database
- Cloud config server
- Microservice ms-orders with private PostgreSQL database
- Feign Communications between microservices ms-products and ms-orders with resilience4j circuit breaker
- Microservice ms-customer with a exclusive PostgreSQL database
- Feign Communications between microservices ms-customer and ms-orders with resilience4j circuit breaker
- API Gateway

## Table of contents

- [Installation](#installation)
- [Usage](#usage)
- [It's not a bug, it's a feature](#features)
- [Maintainers](#maintainers)
- [License](#license)


## Installation

1. First of all clone or download the project.

1. Inside the main folder, you could find the docker-compose.yml file.

1. From there use the command line to run these orders
    ```
    mvn clean package
    docker-compose up
    ```

1. You could finish with this command
    ```
    docker-compose down --rmi local
    ```

## Usage

Nowadays, main microservices are available at 7701,7702 and 7703 ports. You could visit its own swagger-ui on http://${hostname}:${port}/swagger-ui/index.html to know more about the API.
Api Gateway responds on 8080 port, so you could call them using the URL http://${hostname}:8080/api/${entity} as well. For instance:
    ```
    http://localhost:8080/api/addresses
    ```
 
More servers are available at 8761 and 8888 for eureka-server and config-server respectively


## Features

#### :large_orange_diamond: JUnit test in business logic classes

#### :large_orange_diamond: Multiple profiles system for dev, pro and test environment

#### :large_orange_diamond: Include docker-compose.yml and Dockerfile for easy containerization

#### :large_orange_diamond: Feign Communications between microservices with resilience4j circuit breaker fallback


## Maintainers

Just me, [Iván](https://github.com/Ivan-Montes) :sweat_smile:


## License

[GPLv3 license](https://choosealicense.com/licenses/gpl-3.0/)

---

[![Java](https://badgen.net/static/JavaSE/17/orange)](https://www.java.com/es/)
[![Maven](https://badgen.net/badge/icon/maven?icon=maven&label&color=red)](https://https://maven.apache.org/)
[![Spring](https://img.shields.io/badge/spring-blue?logo=Spring&logoColor=white)](https://spring.io)
[![GitHub](https://badgen.net/badge/icon/github?icon=github&label)](https://github.com)
[![Eclipse](https://badgen.net/badge/icon/eclipse?icon=eclipse&label)](https://https://eclipse.org/)
[![SonarQube](https://badgen.net/badge/icon/sonarqube?icon=sonarqube&label&color=purple)](https://www.sonarsource.com/products/sonarqube/downloads/)
[![GPLv3 license](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://choosealicense.com/licenses/gpl-3.0/)
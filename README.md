# desire-mgmt

:warning::warning::construction::construction:  Microservices in a multimodule Maven project with servers, REST API, and MVC system

**Pending**
- MVC ms-ui for presentation layer [8081]

**Complete**
- Eureka-server [8761]
- Microservice ms-products with its own PostgreSQL database
- Cloud config server [8888]
- Microservice ms-orders with private PostgreSQL database
- Feign Communications between microservices ms-products and ms-orders with resilience4j circuit breaker
- Microservice ms-customer with a exclusive PostgreSQL database
- Feign Communications between microservices ms-customer and ms-orders with resilience4j circuit breaker
- API Gateway [8080]
- Prometheus [9090] + Grafana [3000]

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
    docker-compose up -d
    ```
   The Config-server starts quickly in a Docker environment, but then takes some time to properly serve the configuration files, which allows the rest of the microservices to start.
   
1. You could stop all microservices and erase containers/volumes with this order
    ```
    docker-compose down --rmi local -v
    ```

But if you just want to run it from your IDE, please visit the project folder for the databases for a correct initialization


## Usage

Main microservices have dynamic ports, you could know more about the API in future releases from the MVC ms-ui. In DEV/IDE environment you could visit their own swagger-ui on http://${hostname}:${port}/swagger-ui/index.html to . For example:

   ```
    http://localhost:33333/swagger-ui/index.html
   ```

Api Gateway responds on 8080 port, so you could call them directly using the URL http://${hostname}:8080/api/${entity} as well. For instance:

   ```
    http://localhost:8080/api/addresses
   ```
 
Prometheus interface is reachable at port 9090 and Grafana is accessible at 3000 instead. Initial credentials are both "admin". Main datasource and two dashboards are already loaded thanks to docker-compose settings.

Other servers are available at 8761 and 8888 for eureka-server and config-server respectively.


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
[![Docker](https://badgen.net/badge/icon/docker?icon=docker&label)](https://www.docker.com/)
[![GPLv3 license](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://choosealicense.com/licenses/gpl-3.0/)
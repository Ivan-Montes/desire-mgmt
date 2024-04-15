# desire-mgmt

Microservices in a multimodule Maven project with servers, REST API, and MVC system

**Features**
- Eureka server [8761]
- Cloud config server [8888]
- API Gateway [8080] with centralized OpenApi Swagger
- Microservice ms-products with its own PostgreSQL database
- Microservice ms-orders with private PostgreSQL database
- Microservice ms-customer with a exclusive PostgreSQL database
- Prometheus [9090] + Grafana [3000]
- Feign Communications between microservices with resilience4j circuit breaker
- Dashboard on ms-ui [8081] for MVC presentation layer


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

- From the presentation layer

The microservice ms-ui operates as the presentation layer, displaying a dashboard on port 8081 to facilitate interaction. It is the recommended option and you could use it as the main access point to the infrastructure using your default browser. 
	
	```
	http://localhost:8081
	```
  
In other hand, Prometheus interface is reachable at port 9090 and Grafana is accessible at 3000 instead. Initial credentials are both "admin". Main datasource and two dashboards are already loaded thanks to docker-compose settings. More servers are available at 8761 and 8888 for eureka-server and config-server respectively.

	
- API Rest

Endpoints have dynamic ports but Api Gateway responds on 8080 port, so you could use a program like SoapUI or Postman and call them with the following nomenclature http://${hostname}:8080/api/${entity}. For instance:

   ```
    ** Get a List of products **
		http://localhost:8080/api/products

	**  Get a address according to an Id **
		http://localhost:8080/api/addresses/5
   ```

It`s recommended to visit swagger-ui to know more about microservices, however the best aproach to using the REST API is through the Api Gateway. The feature is centralized on the api-gateway server, so you could navigate to http://${hostname}:8080/swagger-ui/index.html and then change the selection on the superior dropdown menu. Some examples of urls are:

   ```
    http://localhost:8080/addresses/swagger-ui/index.html 
    http://localhost:8080/categories/swagger-ui/index.html
    http://localhost:8080/orderdetails/swagger-ui/index.html
   ```

In this context, unexpected behaviors may occur due to the different network settings when you use Swagger requests directly from the Swagger-ui, rather than going through the Api Gateway or using tools like SoapUI or Postman. For preventing some of them, CORS and CSRF are disabled in Spring Security settings.


## Features

#### :large_orange_diamond: JUnit test in business logic classes

#### :large_orange_diamond: Multiple profiles system for dev, pro and test environment

#### :large_orange_diamond: Include docker-compose.yml and Dockerfile for easy containerization

#### :large_orange_diamond: Feign Communications between microservices with resilience4j circuit breaker fallback

#### :large_orange_diamond: Dashboard on port 8081 and OpenApi Swagger included for API description

#### :large_orange_diamond: Dynamic web pages with the Thymeleaf template engine


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
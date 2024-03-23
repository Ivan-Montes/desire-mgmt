#### Docker commands

In order to test the project in you IDE, it's mandatory to have connection between the microservice and its own database. These commands create a database in a docker container for each one. Remember to stop all these containers if you run docker-compose 

- Create network cause the default net (bridge) do not resolve by name

```
docker network create desire-mgmt-net
```

- Database PostgreSQL for ms-products

```
docker run --name postgresdb01local --network desire-mgmt-net -e POSTGRES_PASSWORD=12345 -e POSTGRES_USER=root -e POSTGRES_DB=productsDb -p 5432:5432 -d postgres
```

- Database PostgreSQL for ms-orders

```
docker run --name postgresdb02local --network desire-mgmt-net -e POSTGRES_PASSWORD=12345 -e POSTGRES_USER=root -e POSTGRES_DB=ordersDb -p 5433:5432 -d postgres
```


- Database PostgreSQL for ms-customers

```
docker run --name postgresdb03local --network desire-mgmt-net -e POSTGRES_PASSWORD=12345 -e POSTGRES_USER=root -e POSTGRES_DB=customersDb -p 5434:5432 -d postgres
```

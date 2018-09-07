## About
This is an API for peer-to-peer money transfer. Transfers can be done between currencies with most recent average exchange rates.

## Architecture
- Project structure is built upon hexagonal architecture.
- Entity ```id```s from database will never be exposed but just theirs ```reference```s.
- There are three main package in abstract way:
    - <b>Application</b> package: Real world contact of the project with rest controllers, requests and responses.
    - <b>Domain</b> package: Main business logic holder with some set of layers.
    - <b>Infrastructure</b> package: Base configurations and controller implementations.
- There are 35 tests in project to be sure everything is working. Four main testing type is used:
    - <b>Rest Integration Tests</b>: For endpoint tests. The tests initialize vertx, hit endpoint and get response in json format.
    - <b>Functional Tests</b>: Service tests ensure service implementations and data persistence layer working properly.
    - <b>Concurrent Functional Tests</b>: Service tests ensure service and persistence layers working properly on concurrent requests.
    - <b>Unit Tests</b>: Component tests ensure component under-test is working properly.
  
## How to run
- Run command: ```git clone https://github.com/kahramani/p2p-transfer.git```
- Go to project's path
- Run command: ```./mvnw clean install```
- Run command: ```java -jar target/p2p-money-transfer-1.0.0-SNAPSHOT-fat.jar```
- Project will listen requests from port ```8080```
- Also if you want to specify a port you can give the port number by program argument e.g ```java -jar target/p2p-money-transfer-1.0.0-SNAPSHOT-fat.jar 9090``` 

## Endpoints
There are two endpoints in the project:
- ```POST api/v1/accounts/{ACCOUNT_REFERENCE}/transfers``` to create a transfer.
- ```GET api/v1/transfers/{TRANSFER_REFERENCE}``` to get transfer info.

## How to use
- You can use [postman collection](https://github.com/kahramani/p2p-transfer/blob/master/p2p.postman_collection.json) to hit endpoints.
- For create endpoint you can use username or mobile phone number to point receiver.
- You can find the pre-defined ```users```, ```accounts``` and ```transfers``` on [init script](https://github.com/kahramani/p2p-transfer/blob/master/src/main/resources/scripts/init.sql#L55) 

## Tech Stack
Tech|Version
---|---
JDK|1.8
Vertx|3.5.2
H2 in-memory db|1.4.197
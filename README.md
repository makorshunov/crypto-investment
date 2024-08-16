# Crypto Recommendation Service

## Overview

The *Crypto Recommendation Service* is a Java-based application designed to provide recommendations and information about various cryptocurrencies to users. 


## Key Features

* **Cryptocurrency Information**: Retrieve information about specific cryptocurrencies.
* **Normalized Range Calculations**: Sort and display cryptocurrencies based on their normalized price range.
* **Dockerized Deployment**: Deploy the application in a Docker container for easy portability and scalability.
* **Swagger API Documentation**: Easily explore and test the API using the integrated Swagger UI.

## Prerequisites

To build and run the Crypto Recommendation Service, ensure that you have the following installed:

* **Java**: Version 21
* **Maven**: Maven Wrapper (`mvnw`) is included in the project, so you don't need to install Maven globally.
* **Docker**: For containerizing the application.

## Building the Application

### Using Maven Wrapper

The project includes a Maven Wrapper (`mvnw`) that allows you to build the application without needing a global Maven installation. Follow these steps based on your operating system:

#### Mac/Linux

```console
./mvnw clean install
```

#### Windows

```console
mvnw.cmd clean install
```

## Running the Application

### Running Locally

After building the application, you can run it locally using:

```console
java -jar target/crypto-investment-1.0.0.jar
```

The application will start and will be available at http://localhost:8080.

### Running with Docker

To run the application in a Docker container, follow these steps:

1. Build the Docker image:

```console
docker build -t crypto-investment-service .
```

2. Run the Docker container:

```console
docker run -d -p 8080:8080 crypto-investment-service
```

This will start the service within a Docker container and expose it on port 8080.

## Swagger API Documentation

Swagger UI is integrated into the application for easy API exploration and testing. Once the application is running, you can access the Swagger UI at:

http://localhost:8080/swagger-ui/index.html

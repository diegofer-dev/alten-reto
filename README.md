# Starship Database API

## Overview

Welcome to the Starship Database API project! This project is a RESTful API built entirely in Java using Spring Boot. It provides CRUD (Create, Read, Update, Delete) operations for managing a starship's database. The project leverages Hibernate and JPA for database interactions, RabbitMQ for messaging, and Redis for caching.

The purpose of this project was to build a demo REST API complete with features commonly used in modern web applications.

## Technologies Used

- **Spring Boot**: Simplifies the development and deployment process.
- **Hibernate & JPA**: ORM tools for seamless database interactions.
- **RabbitMQ**: Messaging broker for reliable communication.
- **Redis**: In-memory data structure store for caching.
- **Flyway**: Tools to ease the database migration.

## Requirements

- Java 21 or higher
- Maven 3.6.3 or higher
- Spring Boot Maven Plugin 3.2.5 or higher
- Docker

## Installation

1. **Clone the repository:**
    ```sh
    git clone https://github.com/diegofer-dev/alten-reto.git
    cd alten-reto
    ```

2. **Compose local environment with Docker:**
    ```sh
    docker compose up
    ```

3. **Build and run the application:**
    ```sh
    mvn clean install
    mvn spring-boot:run
    ```

## Usage

The API endpoints allow you to manage starship data through standard HTTP methods. Below are the main endpoints:

### Create a Starship
- **URL**: `/api/starships`
- **Method**: `POST`
- **Body**:
    ```json
    {
        "starshipName": "USS Orinoco",
        "movieName": "Star Trek",
        "numberOfPassengers": 10
    }
    ```

### Get All Starships
- **URL**: `/api/starships`
- **Method**: `GET`
- **Params**:
  ```javascript
    {
        "starshipName": "USS Orin",  // The name (or part of) of the ship eg: starshipName=USS%20Orin
        "movieName": "Star Tre",     // The name (or part of) of the movie eg: movieName=Star%20Tre
        "numberOfPassengers": 10     // Exact number of passengers eg: numberOfPassengers=10
        "page": 0,                   // Page number eg: page=0
        "size": 1,                   // Size of the page eg: size=1
        "sort": [                    // How to sort the retrieved data. eg: sort=numberOfPassengers,asc&sort=movieName,desc
          "string"
        ]
    }
    ```

### Get a Starship by ID
- **URL**: `/api/starships/{id}`
- **Method**: `GET`

### Update a Starship
- **URL**: `/api/starships/{id}`
- **Method**: `PUT`
- **Body**:
    ```json
    {
        "starshipName": "Updated Starship",
        "movieName": "Updated Movie",
        "numberOfPassengers": 15
    }
    ```

### Delete a Starship
- **URL**: `/api/starships/{id}`
- **Method**: `DELETE`

### Important Notes
Once the application is running, Swagger Documentation can be found at [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Project Structure

- **src/main/java**: Contains the Java source files.
- **src/main/resources**: Contains configuration files and templates.
- **src/test/java**: Contains the test cases.


## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

## Contact

For any questions or feedback, please contact me at [13diegofernandez13@gmail.com].

Thank you for checking the Starship Database API!

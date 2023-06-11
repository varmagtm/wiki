# Wiki Application
The Wiki Application is a REST API that allows users to create, edit, and collaborate on documents in a wiki-style format.

Project's skeleton has been generated using [Spring Initializr](https://start.spring.io).
## Features
- Create document revisions
- Track document revisions
- View documents at specific timestamps
- Retrieve the latest version of a document
## Technologies Used
- Java
- Spring Boot
- Spring MVC
- Spring Data JPA
- Postgres
- Hikari connection pool (DB Connection Pool)
- Liquibase (For DB revisioning)
- Maven (Build tool)
- Docker (Container Runtime)

## Getting Started

### Prerequisites
Below applications need to be installed:
- Java Development Kit (JDK) 20
- Maven
- Docker
- Git
### Building and Running the application
1. Clone the repository:
```shell
git clone https://github.com/your-username/wiki-application.git
```
2. Navigate to the project directory:
```shell
cd wiki-application
```
3. Build the application using Maven:
```shell
mvn clean install
```
4. Run the application:
```shell
docker compose up
```

The application will start and available for access at http://localhost:8080.

## API Endpoints

The Wiki Application exposes the following RESTful API endpoints:

- GET `/documents`: Returns a list of available document titles.
- GET `/documents/{title}`: Returns a list of available revisions for a document.
- GET `/documents/{title}/{timestamp}`: Returns the document as it was at the given timestamp.
- GET `/documents/{title}/latest`: Returns the latest version of the document.
- PUT `/documents/{title}`: Allows users to post a new revision of a document.

## Usage
You can use tools like cURL or Postman to access the API endpoints.

If you use Postman tool, you can import the collection using the file `./wiki.postman_collection.json`.

Here are some example requests:

- GET Document titles
```shell
curl --location 'http://localhost:8080/documents'
```
- Get revisions for a document:
```shell
curl --location 'http://localhost:8080/documents/document1'
```
- Get document at a specific timestamp:
```shell
curl --location 'http://localhost:8080/documents/document1/2023-06-11T14:30:13.087869'
```
- Get the latest version of a document:
```shell
curl --location 'http://localhost:8080/documents/document1/latest'
```
- Create a new revision for a document:
```shell
curl --location --request PUT 'http://localhost:8080/documents/document1' \
--header 'Content-Type: application/json' \
--data '"new content"'
```

## API Documentation

Swagger UI has been configured for the API documentation. Once the application is up, access it using the URL - http://localhost:8080/swagger-ui/index.html.

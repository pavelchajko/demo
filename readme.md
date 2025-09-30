
A Spring Boot application that allows creating and fetching users from a SQL database.
## Setup
- make sure to run docker compose up -d to start the database
- configure your database connection in `src/main/resources/application.properties` if different than the default
- running the project will initialize the database schema automatically and initiate the users table
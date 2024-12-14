# Database Setup

This guide will help you set up and run a PostgreSQL database locally using Docker and Docker Compose.

## Prerequisites

Make sure you have the following installed on your system:

- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/install/)

## Getting Started

### 1. Configure Environment Variables

In the database directory, you'll need to create `.env` file for the PostgreSQL database configuration.

You can start by copying the provided .env.example file:
```
cp .env.example .env
```

Make sure to customize values in the `.env` file to suit your local setup.

### 2. Start the Database

Use Docker Compose to start the PostgreSQL database container:
```
docker-compose up -d
```

### 3. Accessing the Database
Once the database container is up and running, you can connect to it using a GUI tool (e.g., DBeaver, pgAdmin).

### 4. Stopping the Database
To stop the running PostgreSQL container, use the following command:
```
docker-compose down
```
This will stop and remove the container, but the data will be preserved thanks to the mounted volume.

### 5. Removing Volumes (Optional)
If you want to remove the database data completely (including the volume), you can run:
```
docker-compose down -v
```
This will delete the volume and its associated data.

### Additional Notes
* The PostgreSQL version used is defined in the docker-compose.yml file. You can change the version by updating the image property under the db service.
* For development, you can use database management tools like [pgAdmin](https://www.pgadmin.org) or [Dbeaver](https://dbeaver.io) to interact with the database.

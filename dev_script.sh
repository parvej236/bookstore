#!/bin/bash
# dev_script.sh - One command to run the Bookstore app locally with Docker

# Stop any existing containers
echo "Stopping any running containers..."
docker compose down

# Build the Spring Boot Docker image
echo "Building Spring Boot Docker image..."
docker build -t bookstore .

# Start containers
echo "Starting PostgreSQL and Spring Boot containers..."
docker compose up -d

# Wait for a few seconds to let DB start
echo "Waiting for PostgreSQL to initialize..."
sleep 10

# Show logs of the app container
echo "Showing logs of the bookstore app..."
docker compose logs -f app

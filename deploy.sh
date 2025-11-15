#!/bin/bash

# Bookstore Application Deployment Script
# This script helps deploy the application using Docker Compose

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Bookstore Application Deployment${NC}"
echo -e "${GREEN}========================================${NC}"

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo -e "${RED}Error: Docker is not installed. Please install Docker first.${NC}"
    exit 1
fi

# Check if Docker Compose is installed and determine which command to use
if docker compose version &> /dev/null; then
    COMPOSE_CMD="docker compose"
elif command -v docker-compose &> /dev/null; then
    COMPOSE_CMD="docker-compose"
else
    echo -e "${RED}Error: Docker Compose is not installed. Please install Docker Compose first.${NC}"
    exit 1
fi

echo -e "${GREEN}Using: $COMPOSE_CMD${NC}"

# Function to check if .env file exists
check_env_file() {
    if [ ! -f .env ]; then
        echo -e "${YELLOW}Warning: .env file not found.${NC}"
        read -p "Do you want to create .env from .env.example? (y/n): " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            if [ -f .env.example ]; then
                cp .env.example .env
                echo -e "${GREEN}.env file created from .env.example${NC}"
                echo -e "${YELLOW}Please update .env file with your actual values before continuing.${NC}"
                read -p "Press Enter to continue after updating .env file..."
            else
                echo -e "${RED}Error: .env.example file not found.${NC}"
                exit 1
            fi
        fi
    fi
}

# Parse command line arguments
COMMAND=${1:-up}

case $COMMAND in
    up|start)
        echo -e "${GREEN}Starting services...${NC}"
        check_env_file
        $COMPOSE_CMD up -d
        echo -e "${GREEN}Services started successfully!${NC}"
        echo -e "${GREEN}Application is available at: http://localhost:3333${NC}"
        echo -e "${YELLOW}View logs with: $COMPOSE_CMD logs -f${NC}"
        ;;
    down|stop)
        echo -e "${YELLOW}Stopping services...${NC}"
        $COMPOSE_CMD down
        echo -e "${GREEN}Services stopped successfully!${NC}"
        ;;
    restart)
        echo -e "${YELLOW}Restarting services...${NC}"
        $COMPOSE_CMD restart
        echo -e "${GREEN}Services restarted successfully!${NC}"
        ;;
    build)
        echo -e "${GREEN}Building application...${NC}"
        $COMPOSE_CMD build --no-cache
        echo -e "${GREEN}Build completed successfully!${NC}"
        ;;
    logs)
        $COMPOSE_CMD logs -f
        ;;
    ps|status)
        $COMPOSE_CMD ps
        ;;
    clean)
        echo -e "${YELLOW}Cleaning up containers, volumes, and images...${NC}"
        read -p "This will delete all data. Are you sure? (y/n): " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            $COMPOSE_CMD down -v --rmi all
            echo -e "${GREEN}Cleanup completed!${NC}"
        else
            echo -e "${YELLOW}Cleanup cancelled.${NC}"
        fi
        ;;
    backup)
        echo -e "${GREEN}Backing up database...${NC}"
        BACKUP_FILE="backup_$(date +%Y%m%d_%H%M%S).sql"
        $COMPOSE_CMD exec -T postgres pg_dump -U bookstore_user bookstore > "$BACKUP_FILE"
        echo -e "${GREEN}Backup created: $BACKUP_FILE${NC}"
        ;;
    prod)
        echo -e "${GREEN}Starting production services...${NC}"
        check_env_file
        $COMPOSE_CMD -f docker-compose.yml -f docker-compose.prod.yml up -d
        echo -e "${GREEN}Production services started successfully!${NC}"
        ;;
    *)
        echo -e "${RED}Unknown command: $COMMAND${NC}"
        echo ""
        echo "Usage: ./deploy.sh [command]"
        echo ""
        echo "Commands:"
        echo "  up, start    - Start all services (default)"
        echo "  down, stop   - Stop all services"
        echo "  restart      - Restart all services"
        echo "  build        - Build application image"
        echo "  logs         - View logs (follow mode)"
        echo "  ps, status   - Show service status"
        echo "  clean        - Remove all containers, volumes, and images"
        echo "  backup       - Backup database"
        echo "  prod         - Start in production mode"
        exit 1
        ;;
esac


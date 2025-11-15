# Quick Start Guide

## 🚀 Deploy in 3 Steps

### Step 1: Make deployment script executable
```bash
chmod +x deploy.sh
```

### Step 2: Start the application
```bash
./deploy.sh up
```

Or use Docker Compose directly:
```bash
docker-compose up -d
```

### Step 3: Access the application
Open your browser and navigate to: **http://localhost:3333**

## 📋 What Gets Deployed

- **Spring Boot Application** on port 3333
- **PostgreSQL Database** on port 5432
- **Automatic database initialization** from schema.sql
- **Health checks** for both services
- **Data persistence** via Docker volumes

## 🔧 Common Commands

```bash
# Start services
./deploy.sh up

# Stop services
./deploy.sh down

# View logs
./deploy.sh logs

# Check status
./deploy.sh ps

# Rebuild application
./deploy.sh build

# Production deployment
./deploy.sh prod
```

## 🔐 Default Database Credentials

- **Database**: bookstore
- **Username**: bookstore_user
- **Password**: bookstore_password

⚠️ **Change these in production!**

## 📝 Environment Variables

Create a `.env` file (optional) to customize:

```bash
cp .env.example .env
# Edit .env with your values
```

## 🐛 Troubleshooting

**Port already in use?**
- Change ports in `docker-compose.yml`

**Application won't start?**
- Check logs: `docker-compose logs app`
- Check database: `docker-compose logs postgres`

**Need to reset everything?**
- `./deploy.sh clean` (⚠️ deletes all data)

## 📚 Full Documentation

See [DOCKER_DEPLOYMENT.md](DOCKER_DEPLOYMENT.md) for detailed documentation.


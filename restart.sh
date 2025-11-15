#!/bin/bash

PORT=8080
JAR="bookstore.jar"

PID=$(sudo netstat -tulnp 2>/dev/null | grep ":$PORT" | awk '{print $7}' | cut -d'/' -f1)
echo "Found PID: $PID"

if [ -n "$PID" ]; then
  echo "Killing process on port $PORT (PID: $PID)..."
  sudo kill -9 $PID
else
  echo "No process running on port $PORT."
fi

echo "Starting $JAR on port $PORT..."
java -jar $JAR --server.port=$PORT -d &
echo "Application started."
#!/bin/bash
echo "=== Redis Container Status ==="
docker ps | grep redis

echo -e "\n=== Redis Logs (last 20 lines) ==="
docker logs --tail 20 redis_courseonlineapp

echo -e "\n=== Spring Boot Environment ==="
docker exec springboot-app env | grep REDIS

echo -e "\n=== Network Connectivity ==="
docker exec springboot-app ping redis -c 2

echo -e "\n=== Redis Ping Test ==="
docker exec redis_courseonlineapp redis-cli ping

echo -e "\n=== Spring Boot Logs (last 20 lines) ==="
docker logs --tail 20 springboot-app

#!/bin/bash
# Create and run "mysql-backend" container in the background
cd docker-sql
docker-compose up -d    
cd ..

if [ "$1" = "--db-only" ]; then
    read -r -d '' _ </dev/tty       # wait until ctrl+C is pressed
    docker stop mysql-backend
    docker rm  mysql-backend
    exit
fi

# create a network named "spring-net"
docker network create spring-net              

# connect mysql-backend with the spring-net network
docker network connect spring-net mysql-backend   

# create a custom image for our spring boot application
docker build -f Dockerfile -t docker-spring-boot .          


# delete the container if already exists
docker rm spring-app

sleep 1

docker exec mysql-backend /root/initialize.sh


# create the container and run (in the background)passing mysql host, password, port, the network
docker run --name spring-app  -p 8080:8080 --net spring-net -e MYSQL_HOST=mysql-backend -e MYSQL_PASSWORD=root -e MYSQL_PORT=3306  docker-spring-boot


echo "Stopping and removing containers..."



docker stop spring-app  # stop container
docker rm spring-app  # remove container

docker stop mysql-backend
docker rm  mysql-backend


docker network rm spring-net
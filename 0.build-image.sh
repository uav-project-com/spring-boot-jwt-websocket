#!/bin/bash

mvn clean package

# build image
docker build -t nobjta9xtq/uav-controler-server:1.0 .

# push to dockerhub.com
docker push nobjta9xtq/uav-controler-server:1.0

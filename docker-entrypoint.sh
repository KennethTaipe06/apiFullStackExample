#!/bin/sh

# Script para configurar variables de entorno antes de ejecutar la aplicación

java \
  -Dspring.datasource.url=${SPRING_DATASOURCE_URL:-jdbc:postgresql://127.0.0.1:5432/postgres} \
  -Dspring.datasource.username=${SPRING_DATASOURCE_USERNAME:-postgres} \
  -Dspring.datasource.password=${SPRING_DATASOURCE_PASSWORD:-root} \
  -jar app.jar

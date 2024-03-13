#!/bin/bash

docker-compose up -d zookeeper broker

# Attend que Kafka soit prêt
echo "Attente de Kafka pour être prêt..."
while ! nc -z localhost 9092; do   
  sleep 1 # attendre 1 seconde avant de vérifier à nouveau
done

echo "Kafka est prêt."

# Création des topics
docker exec broker kafka-topics --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 1 --topic topic1
docker exec broker kafka-topics --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 1 --topic topic2
docker exec broker kafka-topics --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 1 --topic topic3

echo "Topics créés."

# Affichage des topics
docker exec broker kafka-topics --bootstrap-server localhost:9092 --list

# Démarrage docker Postgres
docker-compose up -d postgresdb

# Attend que Postgres soit prêt
echo "Attente de Postgres pour être prêt..."
while ! nc -z localhost 5432; do   
  sleep 1 # attendre 1 seconde avant de vérifier à nouveau
done

echo "Postgres est prêt."



# Démarrage docker mirthconnect
docker-compose up -d mirthconnect

# Attend que mirthconnect soit prêt
echo "Attente de mirthconnect pour être prêt..."
while ! nc -z localhost 8080; do   
  sleep 1 # attendre 1 seconde avant de vérifier à nouveau
done

echo "mirthconnect est prêt."
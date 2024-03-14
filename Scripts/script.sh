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

# Attendre un peu pour s'assurer que la base de données est prête avec un compte à rebours
echo "Attente de la base de données pour être prête..."
temps_restant=15
while [ $temps_restant -gt 0 ]; do
  echo -ne "Temps restant : $temps_restant\033[0K\r"
  sleep 1
  ((temps_restant--))
done
echo "La base de données est prête maintenant."


# Création des tables
# Remplacer "postgresdb" par le nom de votre conteneur PostgreSQL si différent
docker exec postgresdb psql -U postgres -d HOPSIIA -c "
CREATE TABLE Patient (
    id_patient SERIAL PRIMARY KEY,
    birthday DATE,
    sex VARCHAR(10),
    nationality VARCHAR(255)
);
CREATE TABLE Name (
    id_name SERIAL PRIMARY KEY,
    id_patient INTEGER REFERENCES Patient(id_patient),
    last_name VARCHAR(255),
    first_name VARCHAR(255),
    name_type VARCHAR(50)
);
CREATE TABLE Address (
    id_address SERIAL PRIMARY KEY,
    id_patient INTEGER REFERENCES Patient(id_patient),
    street VARCHAR(255),
    city VARCHAR(255),
    zip VARCHAR(50)
);
CREATE TABLE Movements (
    id_movement SERIAL PRIMARY KEY,
    id_patient INTEGER REFERENCES Patient(id_patient),
    visit_number VARCHAR(255) UNIQUE,
    service VARCHAR(255),
    bed VARCHAR(50),
    room VARCHAR(50),
    admit_time TIMESTAMP
);
CREATE TABLE Stay (
    id_stay SERIAL PRIMARY KEY,
    visit_number VARCHAR(255) REFERENCES Movements(visit_number),
    start_time TIMESTAMP,
    end_time TIMESTAMP
);
"

# Affichage des tables
docker exec postgresdb psql -U postgres -d HOPSIIA -c "\dt"

# Démarrage docker mirthconnect
docker-compose up -d mirthconnect

# Attend que mirthconnect soit prêt
echo "Attente de mirthconnect pour être prêt..."
while ! nc -z localhost 8080; do   
  sleep 1 # attendre 1 seconde avant de vérifier à nouveau
done

echo "mirthconnect est prêt."

docker exec -u root mirthconnect chmod -R 777 /var/echantillon_hl7
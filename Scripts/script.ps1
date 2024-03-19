# Démarrage des services avec Docker Compose
docker-compose up -d zookeeper broker

# Attend que Kafka soit prêt
Write-Host "Attente de Kafka pour être prêt..."
do {
    Start-Sleep -Seconds 1
} while (-not(Test-Connection localhost -Port 9092 -Quiet))

Write-Host "Kafka est prêt."

# Création des topics
docker exec broker kafka-topics --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 1 --topic topic1
docker exec broker kafka-topics --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 1 --topic topic2
docker exec broker kafka-topics --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 1 --topic topic3

Write-Host "Topics créés."

# Affichage des topics
docker exec broker kafka-topics --bootstrap-server localhost:9092 --list

# Démarrage de Docker Postgres
docker-compose up -d postgresdb

# Attend que Postgres soit prêt
Write-Host "Attente de Postgres pour être prêt..."
do {
    Start-Sleep -Seconds 1
} while (-not(Test-Connection localhost -Port 5432 -Quiet))

# Création des tables
# Remplacer "postgresdb" par le nom de votre conteneur PostgreSQL si différent
docker exec postgresdb psql -U postgres -d HOPSIIA -c "
CREATE TABLE Patient (
    id_patient VARCHAR(255) PRIMARY KEY,
    birthday DATE,
    sex VARCHAR(10),
    nationality VARCHAR(255)
);
CREATE TABLE Name (
    id_name SERIAL PRIMARY KEY,
    id_patient VARCHAR(255) REFERENCES Patient(id_patient),
    last_name VARCHAR(255),
    first_name VARCHAR(255),
    name_type VARCHAR(50)
);
CREATE TABLE Address (
    id_address SERIAL PRIMARY KEY,
    id_patient VARCHAR(255) REFERENCES Patient(id_patient),
    street VARCHAR(255),
    city VARCHAR(255),
    zip VARCHAR(50)
);
CREATE TABLE Movements (
    id_movement SERIAL PRIMARY KEY,
    id_patient VARCHAR(255) REFERENCES Patient(id_patient),
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

Write-Host "Postgres est prêt."

# Démarrage de Docker Mirth Connect
docker-compose up -d mirthconnect

# Attend que Mirth Connect soit prêt
Write-Host "Attente de Mirth Connect pour être prêt..."
do {
    Start-Sleep -Seconds 1
} while (-not(Test-Connection localhost -Port 8080 -Quiet))

Write-Host "Mirth Connect est prêt."

docker exec -u root mirthconnect chmod -R 777 /var/echantillon_hl7

docker-compose up -d pr1
docker-compose up -d cr1
docker-compose up -d pr2cs3
docker-compose up -d cs2pr3

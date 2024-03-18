# Démarrage de Zookeeper et Broker avec Docker Compose
Invoke-Expression "docker-compose up -d zookeeper broker"

# Attend que Kafka soit prêt
Write-Host "Attente de Kafka pour être prêt..."
do {
    Start-Sleep -Seconds 1
} while (-not (Test-Connection localhost -Port 9092 -Quiet))

Write-Host "Kafka est prêt."

# Création des topics
Invoke-Expression "docker exec broker kafka-topics --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 1 --topic topic1"
Invoke-Expression "docker exec broker kafka-topics --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 1 --topic topic2"
Invoke-Expression "docker exec broker kafka-topics --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 1 --topic topic3"

Write-Host "Topics créés."

# Affichage des topics
Invoke-Expression "docker exec broker kafka-topics --bootstrap-server localhost:9092 --list"

# Démarrage docker Postgres
Invoke-Expression "docker-compose up -d postgresdb"

# Attend que Postgres soit prêt
Write-Host "Attente de Postgres pour être prêt..."
do {
    Start-Sleep -Seconds 1
} while (-not (Test-Connection localhost -Port 5432 -Quiet))

Write-Host "Postgres est prêt."

# Attendre un peu pour s'assurer que la base de données est prête avec un compte à rebours
Write-Host "Attente de la base de données pour être prête..."
$temps_restant = 15
do {
    Write-Host "Temps restant : $temps_restant" -NoNewline
    Start-Sleep -Seconds 1
    $temps_restant--
} while ($temps_restant -gt 0)
Write-Host "La base de données est prête maintenant."

# Création des tables
$cmdSql = @"
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
"@
Invoke-Expression "docker exec postgresdb psql -U postgres -d HOPSIIA -c `"$cmdSql`""

# Affichage des tables
Invoke-Expression "docker exec postgresdb psql -U postgres -d HOPSIIA -c '\dt'"

# Démarrage docker mirthconnect
Invoke-Expression "docker-compose up -d mirthconnect"

# Attend que mirthconnect soit prêt
Write-Host "Attente de mirthconnect pour être prêt..."
do {
    Start-Sleep -Seconds 1
} while (-not (Test-Connection localhost -Port 8080 -Quiet))

Write-Host "mirthconnect est prêt."

Invoke-Expression "docker exec -u root mirthconnect chmod -R 777 /var/echantillon_hl7"

docker-compose up -d pr1
docker-compose up -d cr1

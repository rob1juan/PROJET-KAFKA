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

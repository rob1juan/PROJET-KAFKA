# Projet Kafka avec Spring Boot, PostgreSQL, et Docker

## Contributeurs 
Auteurs : ***[Robin JUAN](https://github.com/rob1juan)*** & ***[Maxime GUIOT](https://github.com/grand0x)***

École : **[INSA Hauts-de-France](https://www.insa-hautsdefrance.fr/)**

---
## Sommaire

- [Sommaire](#sommaire)
- [Introduction](#introduction)
- [Architecture](#architecture)
- [Prérequis](#prérequis)
- [Installation et Démarrage](#installation-et-démarrage)
- [Commandes](#commandes)
- [Questions](#questions)
    - [Question N° 1 : Architecture Alternative sans Kafka](#question-n°-1--architecture-alternative-sans-kafka)
    - [Question N°2 : Avis sur les Deux Architectures](#question-n°2--avis-sur-les-deux-architectures)
    - [Question N°3 : Sécurisation des Échanges dans Kafka](#question-n°3--sécurisation-des-échanges-dans-kafka)

    

## Introduction

Ce projet illustre l'implémentation d'une architecture basée sur Apache Kafka pour le traitement et l'échange de messages dans un système de gestion de données patient. Le système traite des fichiers HL7, les transforme en JSON, les envoie à travers Kafka pour insertion dans PostgreSQL, et permet des interactions via une application console.

## Architecture

![Architecture projet](/Images/Architecture.png)

L'architecture globale se compose des composants suivants :

- **Pr1 (Producteur Kafka 1)** : Application Spring Boot lisant et transformant des fichiers HL7 en JSON pour publication sur un topic Kafka.
- **Cr1 (Consommateur Kafka 1)** : Application consommant les messages JSON du topic Kafka pour insertion dans PostgreSQL.
- **Pr2/Cr3 (Producteur/Consommateur Kafka 2/3)** : Application console permettant d'envoyer des commandes via Kafka et de recevoir des réponses à travers un autre topic.
- **Cr2Pr3 (Consommateur Kafka 2/Producteur Kafka 3)** : Application récupérant les données de PostgreSQL et les renvoyant à Pr2/Cr3 via Kafka.
- **Services Kafka et PostgreSQL** : Déployés dans Docker pour le traitement des messages et la persistance des données.

## Prérequis

- JDK 17+
- Maven
- Docker et Docker Compose

## Installation et Démarrage

Un script PowerShell `script.ps1` est fourni dans Script pour automatiser l'installation et le démarrage de l'environnement Kafka et PostgreSQL, ainsi que pour compiler les applications.

```powershell
./script.ps1
```

Une fois démarré, il sera nécéssaire de charger le channel Mirth Connect.
Pour cela, il faut se rendre sur l'interface web de Mirth Connect (http://localhost:8080) et importer le fichier `HL7 to JSON API.xml` situé dans le dossier `export config`.

Après chargement et déploiement du canal Mirth Connect, il suffit de transférer les fichiers HL7 dans le dossier `/var/echantillon_hl7` du container Mirth.

Dans un terminal sous Linux, se rendre dans le dossier `ECHANTILLONS` et exécuter la commande suivante :
```bash
docker cp ./ mirthconnect:/var/echantillon_hl7
```

Le transfert s'effectuera et Mirth traitera les fichiers.

## Commandes

Voici les commandes supportées par l'application console :

- **help** : Affiche la liste des commandes supportées.
- **exit** : Quitte l'application.
- **get_all_patients** : Récupère tous les patients de la base de données.
- **get_patient_by_id** : Récupère un patient par son identifiant.
- **get_patient_by_name** : Récupère un patient par son nom.
- **get_patient_stay_by_id** : Récupère le séjour d'un patient par son identifiant.
- **get_patient_movements_by_sid** : Récupère les mouvements d'un patient par son identifiant de séjour.
- **export_patients** : Exporte les patients dans un fichier CSV.



## Questions 
### Question N° 1 : Architecture Alternative sans Kafka

Une architecture alternative pour répondre au même besoin sans utiliser Kafka ou tout autre bus de messages pourrait reposer sur une approche de traitement batch ou sur demande via des API REST.

**Architecture REST API et Base de Données :**

- **API de Traitement (Pr1 équivalent)** : Une application Spring Boot exposant des API REST pour la réception des fichiers HL7. Cette application serait responsable de parser les fichiers, de les convertir en JSON, et de les insérer directement dans la base de données PostgreSQL.
- **Base de Données PostgreSQL** : Stockage des données patient, identique à l'architecture actuelle.
- **Application Console (Pr2/Cr3 équivalent)** : Une application console qui communique avec une API REST pour envoyer des commandes ou requêtes et recevoir des réponses. Cette API serait exposée par une application serveur distincte ou pourrait être une partie de l'API de Traitement.
- **Serveur d'Application/API pour les Requêtes (Cr2Pr3 équivalent)** : Une application Spring Boot exposant des API REST pour effectuer des opérations de lecture sur la base de données et renvoyer les résultats à l'application console ou à tout autre consommateur.

### Question N°2 : Avis sur les Deux Architectures

**Architecture avec Kafka** :
- Avantages : Découplage fort entre les producteurs et les consommateurs, élasticité et résilience accrues, et capacité à gérer de hauts volumes de messages de manière asynchrone.
- Inconvénients : Complexité accrue due à la gestion d'un système Kafka, nécessité de sécuriser et de surveiller le bus de messages.

**Architecture REST API** :
- Avantages : Simplicité de mise en place et familiarité pour de nombreux développeurs. Plus facile à sécuriser avec des mécanismes standards comme HTTPS et OAuth/JWT pour les API REST.
- Inconvénients : Moins adaptée pour traiter de hauts volumes de messages en temps réel. Couplage plus fort entre les composants, et dépendance sur la disponibilité du serveur d'API.

**Comparaison** : L'architecture REST API est généralement plus simple à mettre en place et à maintenir pour des applications de petite à moyenne envergure, principalement en raison de la familiarité des développeurs avec les API REST et de la moindre infrastructure requise. Cependant, pour des applications nécessitant une haute disponibilité, une forte découplabilité, et un traitement en temps réel de grandes quantités de messages, l'architecture basée sur Kafka est préférable malgré sa complexité accrue.

### Question N°3 : Sécurisation des Échanges dans Kafka

**Possibilités de Sécurisation dans Kafka** :
1. **Authentification** : SASL/PLAIN, SASL/SCRAM, ou SASL/Kerberos pour contrôler l'accès au bus Kafka.
2. **Autorisation** : Utilisation des ACLs pour définir qui peut lire ou écrire dans les topics.
3. **Chiffrement** : SSL/TLS pour le chiffrement des données en transit entre les producteurs, le bus Kafka et les consommateurs.
4. **Chiffrement des données** : Bien que Kafka lui-même ne fournisse pas cette fonctionnalité directement, le stockage utilisé par Kafka peut être chiffré pour sécuriser les données.

**Choix et Mise en Place** :
Le choix d'un procédé de sécurisation dépend des besoins spécifiques en matière de sécurité, de performance et de complexité.

Pour une mise en place basique mais efficace, **la combinaison d'authentification SASL/SCRAM avec le chiffrement SSL/TLS** pour les données en transit est recommandée. Cette approche offre un bon équilibre entre sécurité et complexité.

- **SASL/SCRAM** fournit une méthode d'authentification sécurisée qui supporte le hachage et le salage des mots de passe.
- **SSL/TLS** assure que les données sont chiffrées durant leur transfert, protégeant ainsi contre l'écoute clandestine.

Pour mettre en place SSL/TLS, vous devez :
1. Générer des certificats pour chaque broker Kafka et pour les clients.
2. Configurer les brokers Kafka pour utiliser SSL en définissant les propriétés `ssl.keystore.location`, `ssl.keystore.password`, `ssl.key.password`, `ssl.truststore.location`, et `ssl.truststore.password`.
3. Configurer les clients producteurs et consommateurs pour utiliser SSL en définissant les propriétés similaires.

La documentation officielle de Kafka fournit des guides détaillés pour la configuration de ces mécanismes de sécurité.

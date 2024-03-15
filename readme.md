1er étape : Exécuter le script sh ou ps1 pour initialiser les containers docker (bien supp les containers, les volumes et les images avant d'éxécuter le script)

2eme étape : allez sur le navigateur et tapez l'url : http://localhost:8080/ pour accéder à l'application web

3e étape : Dowload Mirth et ensuite cliquer sur "Lauch Mirth Connect Administrator" pour accéder à l'interface de Mirth

4e étape : Importer le channel "HL7 to JSON API" dans Mirth, et le déployer

5e étape : Dans un terminal, se rendre dans le repo "PROJET KAFKA/test" et éxécuter la commande 
"docker cp ./ mirthconnect:/var/echantillon_hl7"

Les fichiers du dossier /test seront copié dans le container mirthconnect, dans le repo /var/echantillon_hl7, et seront traité par le channel "HL7 to JSON" de Mirth pour être envoyé au producteur 1
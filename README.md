# Projet Bataille Navale

## exécution du projet
C'est possible de compiler et exécuter le projet en utilisant maven, cependant si vous avez seule le .jar, l'exécution peut-être fait comme

`java -cp bataille_navale-1.0-SNAPSHOT.jar ensta.Main`

## Jeu multijoeur en ligne
Pour jouer en ligne, il faut avoir un joeur server et un client, le joeur server
doit mettre 'y' dans la option présenté et l'autre 'n'. 

Le adresse IP du server et la port TCP seront affichés,
ces donnés doivent être mis dans les options du joeur client et la connection sera essayé.

**Attention**: L'utilisation de NAT empêche que deux joeurs hors de la même réseaux puissent joeur entrê eux.
Vérifié avant d'essayer joeur en réseaux différents si le IP du serveur affiche n'est pas un IP privée.

**Solution**: Une possible solution c'est d'utiliser des services de tunnel comme Ngrok, mais c'est n'était pas testé

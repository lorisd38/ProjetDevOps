[![workflow](https://github.com/pixel38320/ProjetDevOps/actions/workflows/workflow.yml/badge.svg)](https://github.com/pixel38320/ProjetDevOps/actions/workflows/workflow.yml)
[![codecov](https://codecov.io/gh/pixel38320/ProjetDevOps/branch/master/graph/badge.svg?token=DMHKXM5VIC)](https://codecov.io/gh/pixel38320/ProjetDevOps)
[![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/pixel38320/ProjetDevOps?include_prereleases)](https://github.com/pixel38320/ProjetDevOps/releases/tag/1.0)

# ProjetDevOps
Les fonctionnalités ( tout est faisable pour un CSV ou un tableau d'objet ) :
 - Parser
 - Afficher le dataframe
 - Afficher les n premières/dernières lignes
 - Selectionner un sous ensemble de lignes et colonnes
 - Selectionner les lignes avec le nom de la colonne et son contenu voulu
 - Statistique pour le dataframe 
 - Group by sur une colonne

Outil :<br>
 Plateforme de collaboration : ![GIT](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)<br>
 Plateforme de communication : ![SCORD](https://img.shields.io/badge/Discord-7289DA?style=for-the-badge&logo=discord&logoColor=white)<br>
 Plateforme de deploiement : [![DOCKER](https://img.shields.io/badge/Docker-blue?style=for-the-badge&logo=docker&logoColor=white)](https://hub.docker.com/repository/docker/loris38/devops)<br>
 Language utilisé : [![JAVA](https://img.shields.io/badge/Java%20SE%2014-orange?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/technologies/javase/jdk14-archive-downloads.html)<br>
 Javadoc : [![JAVADOC](https://img.shields.io/badge/Java%20doc-grey?style=for-the-badge&logo=java&logoColor=white)](https://pixel38320.github.io/ProjetDevOps/doc/)

Workflow :
 - 5 branches pour les 5 fonctionnalités (feature-<fonctionalité>)
 - Division du travail en deux parties
 - Chacune des parties gérée par un binôme
 - Chaque membre du binôme développe une fonctionnalité et fait le test pour une autre
 - 2 autres branches Hotfix pour le debug lors de la fusion et Develop pour la version la plus avancée, vérifie avant de push sur master, la branche publique
 - Le jeu de test a été lancé à chaque fois qu'une modification a été faite sur Hotfix et Develop grâce au pipeline CI.
 - Le pipeline CI a lancé une exécution de test via maven 
 - Les tests ont été réalisés avec JUnit

Fichier image du Docker :
 - Se base sur l'image ubuntu:20.04
 - Installe git, maven et java SE 14
 - Clone ce git
 - Exécute la commande de clean, test, compile et package de maven
 - Prépare la commande d'exécution (du .jar) du scénario dans notre class App.java
 
FeedBack :
 - Projet très interessant

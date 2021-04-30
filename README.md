Workflow : ![workflow](https://github.com/pixel38320/ProjetDevOps/actions/workflows/workflow.yml/badge.svg)


# ProjetDevOps
Les fonctionnalités ( tout est faisable pour un CSV et un tableau d'objet) :
 - Parser
 - Afficher le dataframe
 - Afficher les n premieres/dernieres lignes
 - Selectionner un sous ensemble de lignes et colonnes
 - Selectionner les lignes avec le nom de la colonne et son contenu voulu
 - Statistique pour le dataframe 
 - Group by sur une colonne

Outil :
 Plateforme de collaboration : ![GIT](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)
 Plateforme de communication : ![SCORD](https://img.shields.io/badge/Discord-7289DA?style=for-the-badge&logo=discord&logoColor=white)

Workflow :
 - 5 branches pour les 5 fonctionnalités (feature-<fonctionalité>)
 - Division du travail en deux parties
 - Chacunes des parties gérée par un binome
 - Chaque membre du binome developpe une fonctionnalité et fait le test pour une autre
 - 2 autres branche Hotfix pour le debug lors de la fusion et Develop pour une version la plus avancée, verifie avant de push sur master, la branche publique
 - Le jeu de test a été lancé a chaque fois qu'une modification a été faite.
 - Pour hotfix et develop il a été automatiquement fait par le CI.
 - Le CI a pu lancer les tests grace au maven. 
 - Les tests ont été fait avec JUnint

Docker :

FeedBack :
 - Projet très interessant

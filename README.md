# 🏆 Gestion de Tournois E-sport

### Introduction
Une application de gestion de tournois d'e-sport développée pour organiser et suivre les compétitions de jeux vidéo. Elle permet la gestion des joueurs, des équipes et des tournois, tout en offrant une expérience complète pour le suivi et la configuration de chaque événement.

## 📋 Table des Matières
- [Contexte du Projet](#contexte-du-projet)
- [Fonctionnalités](#fonctionnalités)
- [Architecture et Structure des Classes](#architecture-et-structure-des-classes)
- [Configuration](#configuration)
- [Installation](#installation)
- [Utilisation](#utilisation)
- [Tests et Couverture de Code](#tests-et-couverture-de-code)
- [Outils et Technologies](#outils-et-technologies)
- [Contributions](#contributions)
- [Licence](#licence)

## 🎯 Contexte du Projet
L'objectif est de fournir une application capable de gérer des tournois d'e-sport en facilitant l'organisation des joueurs, des équipes et des événements. Ce projet est développé en Java, en utilisant des technologies modernes et des pratiques de programmation avancées.

## 🌟 Fonctionnalités

### 🧑‍🤝‍🧑 Gestion des Joueurs
- **Inscription** d'un nouveau joueur
- **Modification** et **suppression** des joueurs existants
- **Affichage** d'un ou plusieurs joueurs

### 👥 Gestion des Équipes
- **Création** d'une équipe
- **Ajout** ou **retrait** de joueurs dans une équipe
- **Affichage** des équipes avec détails

### 🏅 Gestion des Tournois
- **Création** d'un tournoi avec paramètres de base (jeu, équipes, dates, etc.)
- **Modification** des informations de tournoi
- **Affichage** d'un ou plusieurs tournois avec détails et état actuel

## 🏗️ Architecture et Structure des Classes

### Principales Entités
- **Joueur**: `pseudo`, `âge`, `équipe`
- **Équipe**: `nom`, `joueurs`, `tournois`, `classement`
- **Tournoi**: `titre`, `jeu`, `dates`, `spectateurs`, `équipes`, `duréeEstimée`, `tempsDePause`, `tempsDeCérémonie`, `statut`
- **Jeu**: `nom`, `difficulté`, `duréeMoyenne`

### Interfaces Clés
- **TournoiDao**: Calcul de la durée estimée d'un tournoi
- **TournoiMetier**: Gestion et calculs métier pour les tournois

### Implémentations
- **TournoiDaoImpl**: Calcul de la durée estimée de base
- **TournoiDaoExtension**: Calcul avancé (Open/Closed Principle)
- **TournoiMetierImpl**: Logique métier pour la gestion des tournois

### Calcul de la Durée Estimée du Tournoi
1. **Calcul de base**: `(Nombre d'équipes × Durée moyenne d'un match) + Temps de pause`
2. **Calcul avancé**: `(Nombre d'équipes × Durée moyenne × difficulté du jeu) + Temps de pause + Temps de cérémonie`

## ⚙️ Configuration

### Fichiers de Configuration
- `applicationContext.xml`: Configuration Spring pour l'IoC et DI
- `pom.xml`: Configuration des dépendances Maven
- `persistence.xml`: Configuration JPA pour la connexion H2
- **Autres**: Fichiers de configuration additionnels si nécessaire

### Structure du Projet
- **Couche Modèle**: Entités JPA
- **Couche Repository**: Utilisant JPA/Hibernate
- **Couche Service**: Logique métier
- **Couche Utilitaire**: Utilitaires communs
- **Classe Présentation**: Menu console pour interaction utilisateur

## 🛠️ Installation

1. **Cloner le projet** :

   ```bash
   git clone <https://github.com/asmaabarj/gestion_tournois.git>
   cd <gestion_tournois> 


2. **Configuration des dépendances Maven** :

```bash
   mvn install
```

3. **Configurer la base de données** :

- Le projet utilise H2 en tant que SGBD embarqué.
- Assurez-vous que persistence.xml est correctement configuré pour H2.

4. **Exécuter le projet** :

- Lancer la méthode main() de la classe de présentation pour démarrer l'application console.


# 🚀 Utilisation

### Menu Console
Une interface de console propose plusieurs options pour interagir avec les joueurs, équipes, et tournois.

### Gestion des Tournois
Choisissez les options pour organiser, modifier et consulter les détails des tournois.

### Gestion des Équipes et Joueurs
Ajoutez ou modifiez des équipes et joueurs en sélectionnant les options correspondantes.

# ✅ Tests et Couverture de Code

## 📑 Tests Unitaires
- Implémentés avec JUnit et Mockito.
- Vérification de chaque couche de l'application (DAO, Service, etc.).

## 📊 Mesure de Couverture
- Utilisation de JaCoCo pour la couverture de code.
- Inclut des tests d'intégration couvrant les interactions entre DAO et Service.

Exécutez les tests :
```bash
mvn test
```
# 🛠️ Outils et Technologies

- **Java 8** : Utilisé pour les fonctionnalités de base et avancées (Stream API, Lambda, etc.)
- **Maven** : Gestion des dépendances et du cycle de vie du projet
- **Spring Core** : Inversion of Control (IoC) et Dependency Injection (DI)
- **JPA/Hibernate** : Accès aux données pour la gestion des entités et la persistance
- **H2 Database** : SGBD embarqué pour les tests et le développement local
- **JaCoCo** : Mesure de la couverture de code
- **SLF4J** : Système de logging pour une gestion centralisée des logs

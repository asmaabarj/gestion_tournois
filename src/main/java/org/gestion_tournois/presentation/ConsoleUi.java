package org.gestion_tournois.presentation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.gestion_tournois.controller.EquipeController;
import org.gestion_tournois.controller.JeuController;
import org.gestion_tournois.controller.JoueurController;
import org.gestion_tournois.controller.TournoiController;
import org.gestion_tournois.model.Equipe;
import org.gestion_tournois.model.Jeu;
import org.gestion_tournois.model.Joueur;
import org.gestion_tournois.model.Tournoi;
import org.gestion_tournois.model.enums.TournoiStatus;
import org.gestion_tournois.util.ConsoleLogger;
import org.gestion_tournois.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ConsoleUi {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleUi.class);
    private final JoueurController joueurController;
    private final EquipeController equipeController;
    private final TournoiController tournoiController;
    private final JeuController jeuController;
    private final ConsoleLogger consoleLogger;
    private final Scanner scanner;

    public ConsoleUi(JoueurController joueurController, EquipeController equipeController,
                     TournoiController tournoiController, JeuController jeuController,
                     ConsoleLogger consoleLogger) {
        this.joueurController = joueurController;
        this.equipeController = equipeController;
        this.tournoiController = tournoiController;
        this.jeuController = jeuController;
        this.consoleLogger = consoleLogger;
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        LOGGER.info("Démarrage de l'application");
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        org.h2.tools.Server h2WebServer = context.getBean("h2WebServer", org.h2.tools.Server.class);
        String h2ConsoleUrl = h2WebServer.getURL();
        LOGGER.info("H2 Console URL: {}", h2ConsoleUrl);

        ConsoleUi console = context.getBean(ConsoleUi.class);
        console.afficherMenuPrincipal();
        LOGGER.info("Fermeture de l'application");
    }

    public void afficherMenuPrincipal() {
        boolean continuer = true;
        while (continuer) {
            consoleLogger.afficherMessage("Menu principal:");
            consoleLogger.afficherMessage("1. Gestion des joueurs");
            consoleLogger.afficherMessage("2. Gestion des équipes");
            consoleLogger.afficherMessage("3. Gestion des tournois");
            consoleLogger.afficherMessage("4. Gestion des jeux");
            consoleLogger.afficherMessage("0. Quitter");
            consoleLogger.afficherMessage("Choisissez une option:");

            int choix = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne

            switch (choix) {
                case 1:
                    afficherMenuJoueur();
                    break;
                case 2:
                    afficherMenuEquipe();
                    break;
                case 3:
                    afficherMenuTournoi();
                    break;
                case 4:
                    afficherMenuJeu();
                    break;
                case 0:
                    continuer = false;
                    break;
                default:
                    consoleLogger.afficherErreur("Option invalide. Veuillez réessayer.");
            }
        }
        scanner.close();
    }

    private void afficherMenuJoueur() {
        boolean continuer = true;
        while (continuer) {
            consoleLogger.afficherMessage("Menu Joueur:");
            consoleLogger.afficherMessage("1. Inscrire un joueur");
            consoleLogger.afficherMessage("2. Modifier un joueur");
            consoleLogger.afficherMessage("3. Supprimer un joueur");
            consoleLogger.afficherMessage("4. Afficher un joueur");
            consoleLogger.afficherMessage("5. Afficher tous les joueurs");
            consoleLogger.afficherMessage("6. Afficher les joueurs d'une équipe");
            consoleLogger.afficherMessage("0. Retour au menu principal");
            consoleLogger.afficherMessage("Choisissez une option:");

            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    inscrireJoueur();
                    break;
                case 2:
                    modifierJoueur();
                    break;
                case 3:
                    supprimerJoueur();
                    break;
                case 4:
                    afficherJoueur();
                    break;
                case 5:
                    afficherTousJoueurs();
                    break;
                case 6:
                    afficherJoueursParEquipe();
                    break;
                case 0:
                    continuer = false;
                    break;
                default:
                    consoleLogger.afficherErreur("Option invalide. Veuillez réessayer.");
            }
        }
    }

    private void inscrireJoueur() {
        String pseudo;
        int age;
        
        do {
            consoleLogger.afficherMessage("Entrez le pseudo du joueur (3-20 caractères, lettres, chiffres, - et _ uniquement):");
            pseudo = scanner.nextLine();
            if (!ValidationUtil.validerPseudo(pseudo)) {
                consoleLogger.afficherErreur("Pseudo invalide. Veuillez réessayer.");
            }
        } while (!ValidationUtil.validerPseudo(pseudo));

        do {
            consoleLogger.afficherMessage("Entrez l'âge du joueur (12-99 ans):");
            while (!scanner.hasNextInt()) {
                consoleLogger.afficherErreur("Veuillez entrer un nombre valide.");
                scanner.next();
            }
            age = scanner.nextInt();
            scanner.nextLine();
            if (!ValidationUtil.validerAge(age)) {
                consoleLogger.afficherErreur("Âge invalide. Veuillez réessayer.");
            }
        } while (!ValidationUtil.validerAge(age));

        Joueur nouveauJoueur = joueurController.inscrireJoueur(pseudo, age);
        if (nouveauJoueur != null) {
            consoleLogger.afficherMessage("Joueur inscrit avec succès. ID: " + nouveauJoueur.getId());
        } else {
            consoleLogger.afficherErreur("Échec de l'inscription du joueur.");
        }
    }

    private void modifierJoueur() {
        Long id;
        String nouveauPseudo;
        int nouvelAge;

        do {
            consoleLogger.afficherMessage("Entrez l'ID du joueur à modifier:");
            while (!scanner.hasNextLong()) {
                consoleLogger.afficherErreur("Veuillez entrer un nombre valide.");
                scanner.next();
            }
            id = scanner.nextLong();
            scanner.nextLine();
            if (!ValidationUtil.validerId(id)) {
                consoleLogger.afficherErreur("ID invalide. Veuillez réessayer.");
            }
        } while (!ValidationUtil.validerId(id));

        do {
            consoleLogger.afficherMessage("Entrez le nouveau pseudo du joueur (3-20 caractères):");
            nouveauPseudo = scanner.nextLine();
            if (!ValidationUtil.validerPseudo(nouveauPseudo)) {
                consoleLogger.afficherErreur("Pseudo invalide. Veuillez réessayer.");
            }
        } while (!ValidationUtil.validerPseudo(nouveauPseudo));

        do {
            consoleLogger.afficherMessage("Entrez le nouvel âge du joueur (12-99 ans):");
            while (!scanner.hasNextInt()) {
                consoleLogger.afficherErreur("Veuillez entrer un nombre valide.");
                scanner.next();
            }
            nouvelAge = scanner.nextInt();
            scanner.nextLine();
            if (!ValidationUtil.validerAge(nouvelAge)) {
                consoleLogger.afficherErreur("Âge invalide. Veuillez réessayer.");
            }
        } while (!ValidationUtil.validerAge(nouvelAge));

        Joueur joueurModifie = joueurController.modifierJoueur(id, nouveauPseudo, nouvelAge);
        if (joueurModifie != null) {
            consoleLogger.afficherMessage("Joueur modifié avec succès.");
        } else {
            consoleLogger.afficherErreur("Échec de la modification du joueur.");
        }
    }

    private void supprimerJoueur() {
        Long id;
        do {
            consoleLogger.afficherMessage("Entrez l'ID du joueur à supprimer:");
            while (!scanner.hasNextLong()) {
                consoleLogger.afficherErreur("Veuillez entrer un nombre valide.");
                scanner.next();
            }
            id = scanner.nextLong();
            scanner.nextLine();
            if (!ValidationUtil.validerId(id)) {
                consoleLogger.afficherErreur("ID invalide. Veuillez réessayer.");
            }
        } while (!ValidationUtil.validerId(id));

        Optional<Joueur> joueurOptional = joueurController.obtenirJoueur(id);
        if (joueurOptional.isPresent()) {
            consoleLogger.afficherMessage("Êtes-vous sûr de vouloir supprimer le joueur " + 
                joueurOptional.get().getPseudo() + "? (O/N)");
            String confirmation = scanner.nextLine();
            if (confirmation.equalsIgnoreCase("O")) {
                joueurController.supprimerJoueur(id);
                consoleLogger.afficherMessage("Joueur supprimé avec succès.");
            } else {
                consoleLogger.afficherMessage("Suppression annulée.");
            }
        } else {
            consoleLogger.afficherErreur("Joueur non trouvé.");
        }
    }

    private void afficherJoueur() {
        Long id;
        do {
            consoleLogger.afficherMessage("Entrez l'ID du joueur à afficher:");
            while (!scanner.hasNextLong()) {
                consoleLogger.afficherErreur("Veuillez entrer un nombre valide.");
                scanner.next();
            }
            id = scanner.nextLong();
            scanner.nextLine();
            if (!ValidationUtil.validerId(id)) {
                consoleLogger.afficherErreur("ID invalide. Veuillez réessayer.");
            }
        } while (!ValidationUtil.validerId(id));

        Optional<Joueur> joueurOptional = joueurController.obtenirJoueur(id);
        if (joueurOptional.isPresent()) {
            Joueur joueur = joueurOptional.get();
            consoleLogger.afficherMessage("Détails du joueur:");
            consoleLogger.afficherMessage("ID: " + joueur.getId());
            consoleLogger.afficherMessage("Pseudo: " + joueur.getPseudo());
            consoleLogger.afficherMessage("Âge: " + joueur.getAge());
        } else {
            consoleLogger.afficherErreur("Joueur non trouvé.");
        }
    }

    private void afficherTousJoueurs() {
        List<Joueur> joueurs = joueurController.obtenirTousJoueurs();
        if (!joueurs.isEmpty()) {
            consoleLogger.afficherMessage("Liste de tous les joueurs:");
            for (Joueur joueur : joueurs) {
                consoleLogger.afficherMessage("ID: " + joueur.getId() + ", Pseudo: " + joueur.getPseudo() + ", Âge: " + joueur.getAge());
            }
        } else {
            consoleLogger.afficherMessage("Aucun joueur trouvé.");
        }
    }

    private void afficherJoueursParEquipe() {
        Long equipeId;
        do {
            consoleLogger.afficherMessage("Entrez l'ID de l'équipe:");
            while (!scanner.hasNextLong()) {
                consoleLogger.afficherErreur("Veuillez entrer un nombre valide.");
                scanner.next();
            }
            equipeId = scanner.nextLong();
            scanner.nextLine();
            if (!ValidationUtil.validerId(equipeId)) {
                consoleLogger.afficherErreur("ID invalide. Veuillez réessayer.");
            }
        } while (!ValidationUtil.validerId(equipeId));

        List<Joueur> joueurs = joueurController.obtenirJoueursParEquipe(equipeId);
        if (!joueurs.isEmpty()) {
            consoleLogger.afficherMessage("Joueurs de l'équipe (ID: " + equipeId + "):");
            for (Joueur joueur : joueurs) {
                consoleLogger.afficherMessage("ID: " + joueur.getId() + ", Pseudo: " + joueur.getPseudo() + ", Âge: " + joueur.getAge());
            }
        } else {
            consoleLogger.afficherMessage("Aucun joueur trouvé pour cette équipe.");
        }
    }

    private void afficherMenuEquipe() {
        boolean continuer = true;
        while (continuer) {
            consoleLogger.afficherMessage("Menu Équipe:");
            consoleLogger.afficherMessage("1. Créer une équipe");
            consoleLogger.afficherMessage("2. Modifier une équipe");
            consoleLogger.afficherMessage("3. Supprimer une équipe");
            consoleLogger.afficherMessage("4. Afficher une équipe");
            consoleLogger.afficherMessage("5. Afficher toutes les équipes");
            consoleLogger.afficherMessage("6. Ajouter un joueur à une équipe");
            consoleLogger.afficherMessage("7. Retirer un joueur d'une équipe");
            consoleLogger.afficherMessage("0. Retour au menu principal");
            consoleLogger.afficherMessage("Choisissez une option:");

            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    creerEquipe();
                    break;
                case 2:
                    modifierEquipe();
                    break;
                case 3:
                    supprimerEquipe();
                    break;
                case 4:
                    afficherEquipe();
                    break;
                case 5:
                    afficherToutesEquipes();
                    break;
                case 6:
                    ajouterJoueurAEquipe();
                    break;
                case 7:
                    retirerJoueurDeEquipe();
                    break;
                case 0:
                    continuer = false;
                    break;
                default:
                    consoleLogger.afficherErreur("Option invalide. Veuillez réessayer.");
            }
        }
    }

    private void creerEquipe() {
        String nom;
        do {
            consoleLogger.afficherMessage("Entrez le nom de l'équipe (2-30 caractères):");
            nom = scanner.nextLine();
            if (!ValidationUtil.validerNomEquipe(nom)) {
                consoleLogger.afficherErreur("Nom d'équipe invalide. Veuillez réessayer.");
            }
        } while (!ValidationUtil.validerNomEquipe(nom));

        Equipe equipeCreee = equipeController.creerEquipe(nom);
        if (equipeCreee != null) {
            consoleLogger.afficherMessage("Équipe créée avec succès. ID: " + equipeCreee.getId());
        } else {
            consoleLogger.afficherErreur("Erreur lors de la création de l'équipe.");
        }
    }

    private void modifierEquipe() {
        Long id;
        String nouveauNom;

        do {
            consoleLogger.afficherMessage("Entrez l'ID de l'équipe à modifier:");
            while (!scanner.hasNextLong()) {
                consoleLogger.afficherErreur("Veuillez entrer un nombre valide.");
                scanner.next();
            }
            id = scanner.nextLong();
            scanner.nextLine();
            if (!ValidationUtil.validerId(id)) {
                consoleLogger.afficherErreur("ID invalide. Veuillez réessayer.");
            }
        } while (!ValidationUtil.validerId(id));

        Optional<Equipe> equipeOptional = equipeController.obtenirEquipe(id);
        if (equipeOptional.isPresent()) {
            do {
                consoleLogger.afficherMessage("Entrez le nouveau nom de l'équipe (2-30 caractères):");
                nouveauNom = scanner.nextLine();
                if (!ValidationUtil.validerNomEquipe(nouveauNom)) {
                    consoleLogger.afficherErreur("Nom d'équipe invalide. Veuillez réessayer.");
                }
            } while (!ValidationUtil.validerNomEquipe(nouveauNom));

            Equipe equipeModifiee = equipeController.modifierEquipe(id, nouveauNom);
            if (equipeModifiee != null) {
                consoleLogger.afficherMessage("Équipe modifiée avec succès.");
            } else {
                consoleLogger.afficherErreur("Erreur lors de la modification de l'équipe.");
            }
        } else {
            consoleLogger.afficherErreur("Équipe non trouvée.");
        }
    }

    private void supprimerEquipe() {
        Long id;
        do {
            consoleLogger.afficherMessage("Entrez l'ID de l'équipe à supprimer:");
            while (!scanner.hasNextLong()) {
                consoleLogger.afficherErreur("Veuillez entrer un nombre valide.");
                scanner.next();
            }
            id = scanner.nextLong();
            scanner.nextLine();
            if (!ValidationUtil.validerId(id)) {
                consoleLogger.afficherErreur("ID invalide. Veuillez réessayer.");
            }
        } while (!ValidationUtil.validerId(id));

        Optional<Equipe> equipeOptional = equipeController.obtenirEquipe(id);
        if (equipeOptional.isPresent()) {
            consoleLogger.afficherMessage("Êtes-vous sûr de vouloir supprimer l'équipe " + 
                equipeOptional.get().getNom() + "? (O/N)");
            String confirmation = scanner.nextLine();
            if (confirmation.equalsIgnoreCase("O")) {
                equipeController.supprimerEquipe(id);
                consoleLogger.afficherMessage("Équipe supprimée avec succès.");
            } else {
                consoleLogger.afficherMessage("Suppression annulée.");
            }
        } else {
            consoleLogger.afficherErreur("Équipe non trouvée.");
        }
    }

    private void afficherEquipe() {
        consoleLogger.afficherMessage("Affichage d'une équipe");
        consoleLogger.afficherMessage("Entrez l'ID de l'équipe à afficher:");
        Long id = scanner.nextLong();
        scanner.nextLine();

        Optional<Equipe> equipeOptionnelle = equipeController.obtenirEquipe(id);
        if (equipeOptionnelle.isPresent()) {
            Equipe equipe = equipeOptionnelle.get();
            consoleLogger.afficherMessage("ID: " + equipe.getId());
            consoleLogger.afficherMessage("Nom: " + equipe.getNom());
            consoleLogger.afficherMessage("Classement: " + equipe.getClassement());
            consoleLogger.afficherMessage("Joueurs:");
            for (Joueur joueur : equipe.getJoueurs()) {
                consoleLogger.afficherMessage("  - " + joueur.getPseudo());
            }
        } else {
            consoleLogger.afficherErreur("Équipe non trouvée.");
        }
    }

    private void afficherToutesEquipes() {
        consoleLogger.afficherMessage("Liste de toutes les équipes:");
        List<Equipe> equipes = equipeController.obtenirToutesEquipes();
        if (!equipes.isEmpty()) {
            for (Equipe equipe : equipes) {
                consoleLogger.afficherMessage("ID: " + equipe.getId() + ", Nom: " + equipe.getNom() + ", Classement: "
                        + equipe.getClassement());
            }
        } else {
            consoleLogger.afficherMessage("Aucune équipe trouvée.");
        }
    }

    private void ajouterJoueurAEquipe() {
        consoleLogger.afficherMessage("Ajout d'un joueur à une équipe");
        consoleLogger.afficherMessage("Entrez l'ID de l'équipe:");
        Long equipeId = scanner.nextLong();
        consoleLogger.afficherMessage("Entrez l'ID du joueur à ajouter:");
        Long joueurId = scanner.nextLong();
        scanner.nextLine();

        equipeController.ajouterJoueurAEquipe(equipeId, joueurId);
        consoleLogger.afficherMessage("Joueur ajouté à l'équipe avec succès (si les deux existent).");
    }

    private void retirerJoueurDeEquipe() {
        consoleLogger.afficherMessage("Retrait d'un joueur d'une équipe");
        consoleLogger.afficherMessage("Entrez l'ID de l'équipe:");
        Long equipeId = scanner.nextLong();
        consoleLogger.afficherMessage("Entrez l'ID du joueur à retirer:");
        Long joueurId = scanner.nextLong();
        scanner.nextLine();

        equipeController.retirerJoueurDeEquipe(equipeId, joueurId);
        consoleLogger.afficherMessage("Joueur retiré de l'équipe avec succès (si les deux existent).");
    }

    private void afficherMenuJeu() {
        boolean continuer = true;
        while (continuer) {
            consoleLogger.afficherMessage("Menu Jeu:");
            consoleLogger.afficherMessage("1. Créer un jeu");
            consoleLogger.afficherMessage("2. Modifier un jeu");
            consoleLogger.afficherMessage("3. Supprimer un jeu");
            consoleLogger.afficherMessage("4. Afficher un jeu");
            consoleLogger.afficherMessage("5. Afficher tous les jeux");
            consoleLogger.afficherMessage("0. Retour au menu principal");
            consoleLogger.afficherMessage("Choisissez une option:");

            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    creerJeu();
                    break;
                case 2:
                    modifierJeu();
                    break;
                case 3:
                    supprimerJeu();
                    break;
                case 4:
                    afficherJeu();
                    break;
                case 5:
                    afficherTousJeux();
                    break;
                case 0:
                    continuer = false;
                    break;
                default:
                    consoleLogger.afficherErreur("Option invalide. Veuillez réessayer.");
            }
        }
    }

    private void creerJeu() {
        String nom;
        int difficulte;
        int dureeMoyenneMatch;

        do {
            consoleLogger.afficherMessage("Entrez le nom du jeu (2-50 caractères):");
            nom = scanner.nextLine();
            if (!ValidationUtil.validerNomJeu(nom)) {
                consoleLogger.afficherErreur("Nom du jeu invalide. Veuillez réessayer.");
            }
        } while (!ValidationUtil.validerNomJeu(nom));

        do {
            consoleLogger.afficherMessage("Entrez la difficulté du jeu (1-10):");
            while (!scanner.hasNextInt()) {
                consoleLogger.afficherErreur("Veuillez entrer un nombre valide.");
                scanner.next();
            }
            difficulte = scanner.nextInt();
            if (!ValidationUtil.validerDifficulte(difficulte)) {
                consoleLogger.afficherErreur("Difficulté invalide. Veuillez réessayer.");
            }
        } while (!ValidationUtil.validerDifficulte(difficulte));

        do {
            consoleLogger.afficherMessage("Entrez la durée moyenne d'un match (en minutes, max 360):");
            while (!scanner.hasNextInt()) {
                consoleLogger.afficherErreur("Veuillez entrer un nombre valide.");
                scanner.next();
            }
            dureeMoyenneMatch = scanner.nextInt();
            scanner.nextLine();
            if (!ValidationUtil.validerDuree(dureeMoyenneMatch)) {
                consoleLogger.afficherErreur("Durée moyenne invalide. Veuillez réessayer.");
            }
        } while (!ValidationUtil.validerDuree(dureeMoyenneMatch));

        Jeu nouveauJeu = jeuController.creerJeu(nom, difficulte, dureeMoyenneMatch);
        if (nouveauJeu != null) {
            consoleLogger.afficherMessage("Jeu créé avec succès. ID: " + nouveauJeu.getId());
        } else {
            consoleLogger.afficherErreur("Erreur lors de la création du jeu.");
        }
    }

    private void modifierJeu() {
        Long id;
        String nouveauNom;
        int nouvelleDifficulte;
        int nouvelleDureeMoyenneMatch;

        do {
            consoleLogger.afficherMessage("Entrez l'ID du jeu à modifier:");
            while (!scanner.hasNextLong()) {
                consoleLogger.afficherErreur("Veuillez entrer un nombre valide.");
                scanner.next();
            }
            id = scanner.nextLong();
            scanner.nextLine();
            if (!ValidationUtil.validerId(id)) {
                consoleLogger.afficherErreur("ID invalide. Veuillez réessayer.");
            }
        } while (!ValidationUtil.validerId(id));

        Optional<Jeu> jeuOptional = jeuController.obtenirJeu(id);
        if (jeuOptional.isPresent()) {
            do {
                consoleLogger.afficherMessage("Entrez le nouveau nom du jeu (ou appuyez sur Entrée pour garder l'ancien):");
                nouveauNom = scanner.nextLine();
                if (!nouveauNom.isEmpty() && !ValidationUtil.validerNomJeu(nouveauNom)) {
                    consoleLogger.afficherErreur("Nom du jeu invalide. Veuillez réessayer.");
                    continue;
                }
                if (nouveauNom.isEmpty()) {
                    nouveauNom = jeuOptional.get().getNom();
                }
                break;
            } while (true);

            do {
                consoleLogger.afficherMessage("Entrez la nouvelle difficulté (1-10) ou -1 pour garder l'ancienne:");
                while (!scanner.hasNextInt()) {
                    consoleLogger.afficherErreur("Veuillez entrer un nombre valide.");
                    scanner.next();
                }
                nouvelleDifficulte = scanner.nextInt();
                if (nouvelleDifficulte == -1) {
                    nouvelleDifficulte = jeuOptional.get().getDifficulte();
                    break;
                }
                if (!ValidationUtil.validerDifficulte(nouvelleDifficulte)) {
                    consoleLogger.afficherErreur("Difficulté invalide. Veuillez réessayer.");
                    continue;
                }
                break;
            } while (true);

            do {
                consoleLogger.afficherMessage("Entrez la nouvelle durée moyenne (en minutes, max 360) ou -1 pour garder l'ancienne:");
                while (!scanner.hasNextInt()) {
                    consoleLogger.afficherErreur("Veuillez entrer un nombre valide.");
                    scanner.next();
                }
                nouvelleDureeMoyenneMatch = scanner.nextInt();
                scanner.nextLine();
                if (nouvelleDureeMoyenneMatch == -1) {
                    nouvelleDureeMoyenneMatch = jeuOptional.get().getDureeMoyenneMatch();
                    break;
                }
                if (!ValidationUtil.validerDuree(nouvelleDureeMoyenneMatch)) {
                    consoleLogger.afficherErreur("Durée moyenne invalide. Veuillez réessayer.");
                    continue;
                }
                break;
            } while (true);

            Jeu jeuModifie = jeuController.modifierJeu(id, nouveauNom, nouvelleDifficulte, nouvelleDureeMoyenneMatch);
            if (jeuModifie != null) {
                consoleLogger.afficherMessage("Jeu modifié avec succès.");
            } else {
                consoleLogger.afficherErreur("Erreur lors de la modification du jeu.");
            }
        } else {
            consoleLogger.afficherErreur("Jeu non trouvé.");
        }
    }

    private void supprimerJeu() {
        consoleLogger.afficherMessage("Suppression d'un jeu");
        consoleLogger.afficherMessage("Entrez l'ID du jeu à supprimer:");
        Long id = scanner.nextLong();
        scanner.nextLine();

        Optional<Jeu> jeuOptional = jeuController.obtenirJeu(id);
        if (jeuOptional.isPresent()) {
            consoleLogger.afficherMessage("Êtes-vous sûr de vouloir supprimer le jeu " + jeuOptional.get().getNom() + "? (O/N)");
            String confirmation = scanner.nextLine();
            if (confirmation.equalsIgnoreCase("O")) {
                jeuController.supprimerJeu(id);
                consoleLogger.afficherMessage("Jeu supprimé avec succès.");
            } else {
                consoleLogger.afficherMessage("Suppression annulée.");
            }
        } else {
            consoleLogger.afficherErreur("Jeu non trouvé.");
        }
    }

    private void afficherJeu() {
        consoleLogger.afficherMessage("Affichage d'un jeu");
        consoleLogger.afficherMessage("Entrez l'ID du jeu à afficher:");
        Long id = scanner.nextLong();
        scanner.nextLine();

        Optional<Jeu> jeuOptional = jeuController.obtenirJeu(id);
        if (jeuOptional.isPresent()) {
            Jeu jeu = jeuOptional.get();
            consoleLogger.afficherMessage("Détails du jeu:");
            consoleLogger.afficherMessage("ID: " + jeu.getId());
            consoleLogger.afficherMessage("Nom: " + jeu.getNom());
            consoleLogger.afficherMessage("Difficulté: " + jeu.getDifficulte());
            consoleLogger.afficherMessage("Durée moyenne d'un match: " + jeu.getDureeMoyenneMatch() + " minutes");
        } else {
            consoleLogger.afficherErreur("Jeu non trouvé.");
        }
    }

    private void afficherTousJeux() {
        consoleLogger.afficherMessage("Liste de tous les jeux:");
        List<Jeu> jeux = jeuController.obtenirTousJeux();
        if (!jeux.isEmpty()) {
            for (Jeu jeu : jeux) {
                consoleLogger.afficherMessage("ID: " + jeu.getId() + ", Nom: " + jeu.getNom() + ", Difficulté: "
                        + jeu.getDifficulte() + ", Durée moyenne: " + jeu.getDureeMoyenneMatch() + " minutes");
            }
        } else {
            consoleLogger.afficherMessage("Aucun jeu trouvé.");
        }
    }

    private void afficherMenuTournoi() {
        boolean continuer = true;
        while (continuer) {
            consoleLogger.afficherMessage("\n--- Menu Tournoi ---");
            consoleLogger.afficherMessage("1. Créer un tournoi");
            consoleLogger.afficherMessage("2. Modifier un tournoi");
            consoleLogger.afficherMessage("3. Supprimer un tournoi");
            consoleLogger.afficherMessage("4. Afficher un tournoi");
            consoleLogger.afficherMessage("5. Afficher tous les tournois");
            consoleLogger.afficherMessage("6. Ajouter une équipe à un tournoi");
            consoleLogger.afficherMessage("7. Retirer une équipe d'un tournoi");
            consoleLogger.afficherMessage("8. Calculer la durée estimée d'un tournoi");
            consoleLogger.afficherMessage("9. Modifier le statut d'un tournoi");
            consoleLogger.afficherMessage("0. Retour au menu principal");
            consoleLogger.afficherMessage("Choisissez une option:");

            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    creerTournoi();
                    break;
                case 2:
                    modifierTournoi();
                    break;
                case 3:
                    supprimerTournoi();
                    break;
                case 4:
                    afficherTournoi();
                    break;
                case 5:
                    afficherTousTournois();
                    break;
                case 6:
                    ajouterEquipeATournoi();
                    break;
                case 7:
                    retirerEquipeDeTournoi();
                    break;
                case 8:
                    calculerDureeEstimeeTournoi();
                    break;
                case 9:
                    modifierStatutTournoi();
                    break;
                case 0:
                    continuer = false;
                    break;
                default:
                    consoleLogger.afficherErreur("Option invalide. Veuillez réessayer.");
            }
        }
    }

    private void creerTournoi() {
        String titre;
        Long jeuId;
        LocalDate dateDebut, dateFin;
        int nombreSpectateurs, dureeMoyenneMatch, tempsCeremonie, tempsPauseEntreMatchs;

        do {
            consoleLogger.afficherMessage("Entrez le titre du tournoi (3-100 caractères):");
            titre = scanner.nextLine();
            if (!ValidationUtil.validerTitreTournoi(titre)) {
                consoleLogger.afficherErreur("Titre invalide. Veuillez réessayer.");
            }
        } while (!ValidationUtil.validerTitreTournoi(titre));

        do {
            consoleLogger.afficherMessage("Entrez l'ID du jeu pour ce tournoi:");
            while (!scanner.hasNextLong()) {
                consoleLogger.afficherErreur("Veuillez entrer un nombre valide.");
                scanner.next();
            }
            jeuId = scanner.nextLong();
            scanner.nextLine();
            if (!ValidationUtil.validerId(jeuId)) {
                consoleLogger.afficherErreur("ID invalide. Veuillez réessayer.");
            }
        } while (!ValidationUtil.validerId(jeuId));

        do {
            consoleLogger.afficherMessage("Entrez la date de début (format: dd/MM/yyyy):");
            dateDebut = ValidationUtil.validerFormatDate(scanner.nextLine());
            if (dateDebut == null) {
                consoleLogger.afficherErreur("Date invalide. Veuillez réessayer.");
            }
        } while (dateDebut == null);

        do {
            consoleLogger.afficherMessage("Entrez la date de fin (format: dd/MM/yyyy):");
            dateFin = ValidationUtil.validerFormatDate(scanner.nextLine());
            if (dateFin == null || !ValidationUtil.validerDates(dateDebut, dateFin)) {
                consoleLogger.afficherErreur("Date invalide ou antérieure à la date de début. Veuillez réessayer.");
            }
        } while (dateFin == null || !ValidationUtil.validerDates(dateDebut, dateFin));

        do {
            consoleLogger.afficherMessage("Entrez le nombre de spectateurs attendus (0-100000):");
            while (!scanner.hasNextInt()) {
                consoleLogger.afficherErreur("Veuillez entrer un nombre valide.");
                scanner.next();
            }
            nombreSpectateurs = scanner.nextInt();
            scanner.nextLine();
            if (!ValidationUtil.validerNombreSpectateurs(nombreSpectateurs)) {
                consoleLogger.afficherErreur("Nombre de spectateurs invalide. Veuillez réessayer.");
            }
        } while (!ValidationUtil.validerNombreSpectateurs(nombreSpectateurs));

        do {
            consoleLogger.afficherMessage("Entrez la durée moyenne d'un match (en minutes, max 360):");
            while (!scanner.hasNextInt()) {
                consoleLogger.afficherErreur("Veuillez entrer un nombre valide.");
                scanner.next();
            }
            dureeMoyenneMatch = scanner.nextInt();
            scanner.nextLine();
            if (!ValidationUtil.validerDuree(dureeMoyenneMatch)) {
                consoleLogger.afficherErreur("Durée moyenne invalide. Veuillez réessayer.");
            }
        } while (!ValidationUtil.validerDuree(dureeMoyenneMatch));

        do {
            consoleLogger.afficherMessage("Entrez le temps de cérémonie (en minutes, max 360):");
            while (!scanner.hasNextInt()) {
                consoleLogger.afficherErreur("Veuillez entrer un nombre valide.");
                scanner.next();
            }
            tempsCeremonie = scanner.nextInt();
            scanner.nextLine();
            if (!ValidationUtil.validerDuree(tempsCeremonie)) {
                consoleLogger.afficherErreur("Temps de cérémonie invalide. Veuillez réessayer.");
            }
        } while (!ValidationUtil.validerDuree(tempsCeremonie));

        do {
            consoleLogger.afficherMessage("Entrez le temps de pause entre les matchs (en minutes, max 360):");
            while (!scanner.hasNextInt()) {
                consoleLogger.afficherErreur("Veuillez entrer un nombre valide.");
                scanner.next();
            }
            tempsPauseEntreMatchs = scanner.nextInt();
            scanner.nextLine();
            if (!ValidationUtil.validerDuree(tempsPauseEntreMatchs)) {
                consoleLogger.afficherErreur("Temps de pause invalide. Veuillez réessayer.");
            }
        } while (!ValidationUtil.validerDuree(tempsPauseEntreMatchs));

        Tournoi nouveauTournoi = tournoiController.creerTournoi(titre, jeuId, dateDebut, dateFin, nombreSpectateurs,
                dureeMoyenneMatch, tempsCeremonie, tempsPauseEntreMatchs);
        if (nouveauTournoi != null) {
            consoleLogger.afficherMessage("Tournoi créé avec succès. ID: " + nouveauTournoi.getId());
        } else {
            consoleLogger.afficherErreur("Erreur lors de la création du tournoi.");
        }
    }

    private void modifierTournoi() {
        Long id;
        String nouveauTitre;
        LocalDate nouvelleDateDebut, nouvelleDateFin;
        int nouveauNombreSpectateurs;

        do {
            consoleLogger.afficherMessage("Entrez l'ID du tournoi à modifier:");
            while (!scanner.hasNextLong()) {
                consoleLogger.afficherErreur("Veuillez entrer un nombre valide.");
                scanner.next();
            }
            id = scanner.nextLong();
            scanner.nextLine();
            if (!ValidationUtil.validerId(id)) {
                consoleLogger.afficherErreur("ID invalide. Veuillez réessayer.");
            }
        } while (!ValidationUtil.validerId(id));

        Optional<Tournoi> tournoiOptional = tournoiController.obtenirTournoi(id);
        if (tournoiOptional.isPresent()) {
            Tournoi tournoi = tournoiOptional.get();

            do {
                consoleLogger.afficherMessage("Entrez le nouveau titre (ou appuyez sur Entrée pour garder l'ancien):");
                nouveauTitre = scanner.nextLine();
                if (!nouveauTitre.isEmpty() && !ValidationUtil.validerTitreTournoi(nouveauTitre)) {
                    consoleLogger.afficherErreur("Titre invalide. Veuillez réessayer.");
                    continue;
                }
                if (nouveauTitre.isEmpty()) {
                    nouveauTitre = tournoi.getTitre();
                }
                break;
            } while (true);

            do {
                consoleLogger.afficherMessage("Entrez la nouvelle date de début (format: dd/MM/yyyy) ou appuyez sur Entrée pour garder l'ancienne:");
                String dateDebutStr = scanner.nextLine();
                if (dateDebutStr.isEmpty()) {
                    nouvelleDateDebut = tournoi.getDateDebut();
                    break;
                }
                nouvelleDateDebut = ValidationUtil.validerFormatDate(dateDebutStr);
                if (nouvelleDateDebut == null) {
                    consoleLogger.afficherErreur("Date invalide. Veuillez réessayer.");
                    continue;
                }
                break;
            } while (true);

            do {
                consoleLogger.afficherMessage("Entrez la nouvelle date de fin (format: dd/MM/yyyy) ou appuyez sur Entrée pour garder l'ancienne:");
                String dateFinStr = scanner.nextLine();
                if (dateFinStr.isEmpty()) {
                    nouvelleDateFin = tournoi.getDateFin();
                    break;
                }
                nouvelleDateFin = ValidationUtil.validerFormatDate(dateFinStr);
                if (nouvelleDateFin == null || !ValidationUtil.validerDates(nouvelleDateDebut, nouvelleDateFin)) {
                    consoleLogger.afficherErreur("Date invalide ou antérieure à la date de début. Veuillez réessayer.");
                    continue;
                }
                break;
            } while (true);

            do {
                consoleLogger.afficherMessage("Entrez le nouveau nombre de spectateurs (0-100000) ou -1 pour garder l'ancien:");
                while (!scanner.hasNextInt()) {
                    consoleLogger.afficherErreur("Veuillez entrer un nombre valide.");
                    scanner.next();
                }
                nouveauNombreSpectateurs = scanner.nextInt();
                scanner.nextLine();
                if (nouveauNombreSpectateurs == -1) {
                    nouveauNombreSpectateurs = tournoi.getNombreSpectateurs();
                    break;
                }
                if (!ValidationUtil.validerNombreSpectateurs(nouveauNombreSpectateurs)) {
                    consoleLogger.afficherErreur("Nombre de spectateurs invalide. Veuillez réessayer.");
                    continue;
                }
                break;
            } while (true);

            Tournoi tournoiModifie = tournoiController.modifierTournoi(id, nouveauTitre, nouvelleDateDebut,
                    nouvelleDateFin, nouveauNombreSpectateurs);
            if (tournoiModifie != null) {
                consoleLogger.afficherMessage("Tournoi modifié avec succès.");
            } else {
                consoleLogger.afficherErreur("Erreur lors de la modification du tournoi.");
            }
        } else {
            consoleLogger.afficherErreur("Tournoi non trouvé.");
        }
    }

    private void supprimerTournoi() {
        consoleLogger.afficherMessage("Suppression d'un tournoi");
        consoleLogger.afficherMessage("Entrez l'ID du tournoi à supprimer:");
        Long id = scanner.nextLong();
        scanner.nextLine();

        Optional<Tournoi> tournoiOptional = tournoiController.obtenirTournoi(id);
        if (tournoiOptional.isPresent()) {
            consoleLogger.afficherMessage("Êtes-vous sûr de vouloir supprimer le tournoi " + tournoiOptional.get().getTitre() + "? (O/N)");
            String confirmation = scanner.nextLine();
            if (confirmation.equalsIgnoreCase("O")) {
                tournoiController.supprimerTournoi(id);
                consoleLogger.afficherMessage("Tournoi supprimé avec succès.");
            } else {
                consoleLogger.afficherMessage("Suppression annulée.");
            }
        } else {
            consoleLogger.afficherErreur("Tournoi non trouvé.");
        }
    }

    private void afficherTournoi() {
        consoleLogger.afficherMessage("Affichage d'un tournoi");
        consoleLogger.afficherMessage("Entrez l'ID du tournoi à afficher:");
        Long id = scanner.nextLong();
        scanner.nextLine();

        Optional<Tournoi> tournoiOptional = tournoiController.obtenirTournoi(id);
        if (tournoiOptional.isPresent()) {
            Tournoi tournoi = tournoiOptional.get();
            consoleLogger.afficherMessage("Détails du tournoi:");
            consoleLogger.afficherMessage("ID: " + tournoi.getId());
            consoleLogger.afficherMessage("Titre: " + tournoi.getTitre());
            consoleLogger.afficherMessage("Jeu: " + tournoi.getJeu().getNom());
            consoleLogger.afficherMessage("Date de début: " + tournoi.getDateDebut());
            consoleLogger.afficherMessage("Date de fin: " + tournoi.getDateFin());
            consoleLogger.afficherMessage("Nombre de spectateurs: " + tournoi.getNombreSpectateurs());
            consoleLogger.afficherMessage("Statut: " + tournoi.getStatut());
            consoleLogger.afficherMessage("Équipes participantes:");
            for (Equipe equipe : tournoi.getEquipes()) {
                consoleLogger.afficherMessage("- " + equipe.getNom());
            }
        } else {
            consoleLogger.afficherErreur("Tournoi non trouvé.");
        }
    }

    private void afficherTousTournois() {
        consoleLogger.afficherMessage("Liste de tous les tournois:");
        List<Tournoi> tournois = tournoiController.obtenirTousTournois();
        if (!tournois.isEmpty()) {
            for (Tournoi tournoi : tournois) {
                String jeuNom = tournoi.getJeu() != null ? tournoi.getJeu().getNom() : "N/A";
                consoleLogger.afficherMessage("ID: " + tournoi.getId() + ", Titre: " + tournoi.getTitre() + ", Jeu: "
                        + jeuNom + ", Statut: " + tournoi.getStatut());
            }
        } else {
            consoleLogger.afficherMessage("Aucun tournoi trouvé.");
        }
    }

    private void ajouterEquipeATournoi() {
        Long tournoiId;
        Long equipeId;

        do {
            consoleLogger.afficherMessage("Entrez l'ID du tournoi:");
            while (!scanner.hasNextLong()) {
                consoleLogger.afficherErreur("Veuillez entrer un nombre valide.");
                scanner.next();
            }
            tournoiId = scanner.nextLong();
            if (!ValidationUtil.validerId(tournoiId)) {
                consoleLogger.afficherErreur("ID du tournoi invalide. Veuillez réessayer.");
            }
        } while (!ValidationUtil.validerId(tournoiId));

        do {
            consoleLogger.afficherMessage("Entrez l'ID de l'équipe à ajouter:");
            while (!scanner.hasNextLong()) {
                consoleLogger.afficherErreur("Veuillez entrer un nombre valide.");
                scanner.next();
            }
            equipeId = scanner.nextLong();
            scanner.nextLine();
            if (!ValidationUtil.validerId(equipeId)) {
                consoleLogger.afficherErreur("ID de l'équipe invalide. Veuillez réessayer.");
            }
        } while (!ValidationUtil.validerId(equipeId));

        try {
            tournoiController.ajouterEquipeATournoi(tournoiId, equipeId);
            consoleLogger.afficherMessage("Équipe ajoutée au tournoi avec succès.");
        } catch (Exception e) {
            consoleLogger.afficherErreur("Erreur lors de l'ajout de l'équipe au tournoi: " + e.getMessage());
        }
    }

    private void retirerEquipeDeTournoi() {
        Long tournoiId;
        Long equipeId;

        do {
            consoleLogger.afficherMessage("Entrez l'ID du tournoi:");
            while (!scanner.hasNextLong()) {
                consoleLogger.afficherErreur("Veuillez entrer un nombre valide.");
                scanner.next();
            }
            tournoiId = scanner.nextLong();
            if (!ValidationUtil.validerId(tournoiId)) {
                consoleLogger.afficherErreur("ID du tournoi invalide. Veuillez réessayer.");
            }
        } while (!ValidationUtil.validerId(tournoiId));

        do {
            consoleLogger.afficherMessage("Entrez l'ID de l'équipe à retirer:");
            while (!scanner.hasNextLong()) {
                consoleLogger.afficherErreur("Veuillez entrer un nombre valide.");
                scanner.next();
            }
            equipeId = scanner.nextLong();
            scanner.nextLine();
            if (!ValidationUtil.validerId(equipeId)) {
                consoleLogger.afficherErreur("ID de l'équipe invalide. Veuillez réessayer.");
            }
        } while (!ValidationUtil.validerId(equipeId));

        try {
            tournoiController.retirerEquipeDeTournoi(tournoiId, equipeId);
            consoleLogger.afficherMessage("Équipe retirée du tournoi avec succès.");
        } catch (Exception e) {
            consoleLogger.afficherErreur("Erreur lors du retrait de l'équipe du tournoi: " + e.getMessage());
        }
    }

    private void calculerDureeEstimeeTournoi() {
        Long tournoiId;
        do {
            consoleLogger.afficherMessage("Entrez l'ID du tournoi:");
            while (!scanner.hasNextLong()) {
                consoleLogger.afficherErreur("Veuillez entrer un nombre valide.");
                scanner.next();
            }
            tournoiId = scanner.nextLong();
            scanner.nextLine();
            if (!ValidationUtil.validerId(tournoiId)) {
                consoleLogger.afficherErreur("ID du tournoi invalide. Veuillez réessayer.");
            }
        } while (!ValidationUtil.validerId(tournoiId));

        int dureeEstimee = tournoiController.obtenirDureeEstimeeTournoi(tournoiId);
        if (dureeEstimee > 0) {
            Optional<Tournoi> tournoiOptional = tournoiController.obtenirTournoi(tournoiId);
            if (tournoiOptional.isPresent()) {
                Tournoi tournoi = tournoiOptional.get();
                consoleLogger.afficherMessage("La durée estimée du tournoi " + tournoi.getTitre() + 
                    " est de " + tournoi.getDureeEstimee() + " minutes.");
            } else {
                consoleLogger.afficherErreur("Tournoi non trouvé après le calcul.");
            }
        } else {
            consoleLogger.afficherErreur("Impossible de calculer la durée estimée du tournoi.");
        }
    }

    private void modifierStatutTournoi() {
        Long tournoiId;

        do {
            consoleLogger.afficherMessage("Entrez l'ID du tournoi:");
            while (!scanner.hasNextLong()) {
                consoleLogger.afficherErreur("Veuillez entrer un nombre valide.");
                scanner.next();
            }
            tournoiId = scanner.nextLong();
            scanner.nextLine();
            if (!ValidationUtil.validerId(tournoiId)) {
                consoleLogger.afficherErreur("ID du tournoi invalide. Veuillez réessayer.");
            }
        } while (!ValidationUtil.validerId(tournoiId));

        Optional<Tournoi> tournoiOptional = tournoiController.obtenirTournoi(tournoiId);
        if (tournoiOptional.isPresent()) {
            consoleLogger.afficherMessage("Choisissez le nouveau statut:");
            consoleLogger.afficherMessage("1. PLANIFIE");
            consoleLogger.afficherMessage("2. EN_COURS");
            consoleLogger.afficherMessage("3. TERMINE");
            consoleLogger.afficherMessage("4. ANNULE");
            
            int choix;
            do {
                while (!scanner.hasNextInt()) {
                    consoleLogger.afficherErreur("Veuillez entrer un nombre valide.");
                    scanner.next();
                }
                choix = scanner.nextInt();
                scanner.nextLine();
                if (choix < 1 || choix > 4) {
                    consoleLogger.afficherErreur("Choix invalide. Veuillez entrer un nombre entre 1 et 4.");
                }
            } while (choix < 1 || choix > 4);

            TournoiStatus nouveauStatut;
            switch (choix) {
                case 1:
                    nouveauStatut = TournoiStatus.PLANIFIE;
                    break;
                case 2:
                    nouveauStatut = TournoiStatus.EN_COURS;
                    break;
                case 3:
                    nouveauStatut = TournoiStatus.TERMINE;
                    break;
                case 4:
                    nouveauStatut = TournoiStatus.ANNULE;
                    break;
                default:
                    throw new IllegalStateException("Valeur inattendue: " + choix);
            }

            tournoiController.modifierStatutTournoi(tournoiId, nouveauStatut);
            consoleLogger.afficherMessage("Statut du tournoi modifié avec succès.");
        } else {
            consoleLogger.afficherErreur("Tournoi non trouvé.");
        }
    }
}

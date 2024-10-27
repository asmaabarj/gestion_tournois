package org.gestion_tournois.presentation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.gestion_tournois.controllers.EquipeController;
import org.gestion_tournois.controllers.JeuController;
import org.gestion_tournois.controllers.JoueurController;
import org.gestion_tournois.controllers.TournoiController;
import org.gestion_tournois.models.Equipe;
import org.gestion_tournois.models.Jeu;
import org.gestion_tournois.models.Joueur;
import org.gestion_tournois.models.Tournoi;
import org.gestion_tournois.models.enums.Status;
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
    private final Scanner scanner;

    public ConsoleUi(JoueurController joueurController, EquipeController equipeController,
                    TournoiController tournoiController, JeuController jeuController) {
        this.joueurController = joueurController;
        this.equipeController = equipeController;
        this.tournoiController = tournoiController;
        this.jeuController = jeuController;
        this.scanner = new Scanner(System.in);
        LOGGER.info("ConsoleUi initialisée");
    }

    // Menu Principal
    public void afficherMenuPrincipal() {
        boolean continuer = true;
        while (continuer) {
            LOGGER.info("Affichage du menu principal");
            System.out.println("\n=== Menu Principal ===");
            System.out.println("1. Gestion des joueurs");
            System.out.println("2. Gestion des équipes");
            System.out.println("3. Gestion des tournois");
            System.out.println("4. Gestion des jeux");
            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");

            try {
                int choix = scanner.nextInt();
                scanner.nextLine();

                switch (choix) {
                    case 1:
                        gererMenuJoueurs();
                        break;
                    case 2:
                        gererMenuEquipes();
                        break;
                    case 3:
                        gererMenuTournois();
                        break;
                    case 4:
                        gererMenuJeux();
                        break;
                    case 0:
                        continuer = false;
                        LOGGER.info("Fermeture de l'application");
                        break;
                    default:
                        System.out.println("Option invalide");
                }
            } catch (Exception e) {
                LOGGER.error("Erreur dans le menu principal", e);
                scanner.nextLine();
                System.out.println("Erreur de saisie. Veuillez réessayer.");
            }
        }
    }

    // Gestion des Joueurs
    private void gererMenuJoueurs() {
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n=== Gestion des Joueurs ===");
            System.out.println("1. Inscrire un joueur");
            System.out.println("2. Modifier un joueur");
            System.out.println("3. Supprimer un joueur");
            System.out.println("4. Afficher un joueur");
            System.out.println("5. Afficher tous les joueurs");
            System.out.println("6. Afficher les joueurs d'une équipe");
            System.out.println("0. Retour");

            try {
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
                        System.out.println("Option invalide");
                }
            } catch (Exception e) {
                LOGGER.error("Erreur dans le menu joueurs", e);
                scanner.nextLine();
                System.out.println("Erreur de saisie. Veuillez réessayer.");
            }
        }
    }

    private void inscrireJoueur() {
        try {
            System.out.println("Entrez le pseudo du joueur:");
            String pseudo = scanner.nextLine();
            System.out.println("Entrez l'âge du joueur:");
            int age = scanner.nextInt();
            scanner.nextLine();

            Joueur nouveauJoueur = joueurController.inscrireJoueur(pseudo, age);
            if (nouveauJoueur != null) {
                System.out.println("Joueur inscrit avec succès. ID: " + nouveauJoueur.getId());
            } else {
                System.out.println("Échec de l'inscription du joueur.");
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors de l'inscription du joueur", e);
            System.out.println("Erreur lors de l'inscription du joueur.");
        }
    }

    private void modifierJoueur() {
        try {
            System.out.println("Entrez l'ID du joueur à modifier:");
            Long id = scanner.nextLong();
            scanner.nextLine();

            System.out.println("Entrez le nouveau pseudo:");
            String nouveauPseudo = scanner.nextLine();
            System.out.println("Entrez le nouvel âge:");
            int nouvelAge = scanner.nextInt();
            scanner.nextLine();

            Joueur joueurModifie = joueurController.modifierJoueur(id, nouveauPseudo, nouvelAge);
            if (joueurModifie != null) {
                System.out.println("Joueur modifié avec succès.");
            } else {
                System.out.println("Joueur non trouvé.");
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors de la modification du joueur", e);
            System.out.println("Erreur lors de la modification du joueur.");
        }
    }

    private void supprimerJoueur() {
        try {
            System.out.println("Entrez l'ID du joueur à supprimer:");
            Long id = scanner.nextLong();
            scanner.nextLine();

            joueurController.supprimerJoueur(id);
            System.out.println("Joueur supprimé avec succès.");
        } catch (Exception e) {
            LOGGER.error("Erreur lors de la suppression du joueur", e);
            System.out.println("Erreur lors de la suppression du joueur.");
        }
    }

    private void afficherJoueur() {
        try {
            System.out.println("Entrez l'ID du joueur à afficher:");
            Long id = scanner.nextLong();
            scanner.nextLine();

            Optional<Joueur> joueurOpt = joueurController.obtenirJoueur(id);
            if (joueurOpt.isPresent()) {
                Joueur joueur = joueurOpt.get();
                System.out.println("ID: " + joueur.getId());
                System.out.println("Pseudo: " + joueur.getPseudo());
                System.out.println("Âge: " + joueur.getAge());
            } else {
                System.out.println("Joueur non trouvé.");
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors de l'affichage du joueur", e);
            System.out.println("Erreur lors de l'affichage du joueur.");
        }
    }

    private void afficherTousJoueurs() {
        try {
            List<Joueur> joueurs = joueurController.obtenirTousJoueurs();
            if (!joueurs.isEmpty()) {
                System.out.println("Liste des joueurs:");
                for (Joueur joueur : joueurs) {
                    System.out.println("ID: " + joueur.getId() + ", Pseudo: " + joueur.getPseudo() + ", Âge: " + joueur.getAge());
                }
            } else {
                System.out.println("Aucun joueur trouvé.");
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors de l'affichage des joueurs", e);
            System.out.println("Erreur lors de l'affichage des joueurs.");
        }
    }

    private void afficherJoueursParEquipe() {
        try {
            System.out.println("Entrez l'ID de l'équipe:");
            Long equipeId = scanner.nextLong();
            scanner.nextLine();

            List<Joueur> joueurs = joueurController.obtenirJoueursParEquipe(equipeId);
            if (!joueurs.isEmpty()) {
                System.out.println("Joueurs de l'équipe " + equipeId + ":");
                for (Joueur joueur : joueurs) {
                    System.out.println("ID: " + joueur.getId() + ", Pseudo: " + joueur.getPseudo() + ", Âge: " + joueur.getAge());
                }
            } else {
                System.out.println("Aucun joueur trouvé pour cette équipe.");
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors de l'affichage des joueurs par équipe", e);
            System.out.println("Erreur lors de l'affichage des joueurs par équipe.");
        }
    }

    private void gererMenuEquipes() {
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n=== Gestion des Équipes ===");
            System.out.println("1. Créer une équipe");
            System.out.println("2. Modifier une équipe");
            System.out.println("3. Supprimer une équipe");
            System.out.println("4. Afficher une équipe");
            System.out.println("5. Afficher toutes les équipes");
            System.out.println("6. Ajouter un joueur à une équipe");
            System.out.println("7. Retirer un joueur d'une équipe");
            System.out.println("0. Retour");

            try {
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
                        System.out.println("Option invalide");
                }
            } catch (Exception e) {
                LOGGER.error("Erreur dans le menu équipes", e);
                scanner.nextLine();
                System.out.println("Erreur de saisie. Veuillez réessayer.");
            }
        }
    }

    private void creerEquipe() {
        try {
            System.out.println("Entrez le nom de l'équipe:");
            String nom = scanner.nextLine();

            Equipe nouvelleEquipe = equipeController.creerEquipe(nom);
            if (nouvelleEquipe != null) {
                System.out.println("Équipe créée avec succès. ID: " + nouvelleEquipe.getId());
            } else {
                System.out.println("Échec de la création de l'équipe.");
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors de la création de l'équipe", e);
            System.out.println("Erreur lors de la création de l'équipe.");
        }
    }

    // Suite des méthodes de gestion des équipes
    private void modifierEquipe() {
        try {
            System.out.println("Entrez l'ID de l'équipe à modifier:");
            Long id = scanner.nextLong();
            scanner.nextLine();

            System.out.println("Entrez le nouveau nom:");
            String nouveauNom = scanner.nextLine();

            Equipe equipeModifiee = equipeController.modifierEquipe(id, nouveauNom);
            if (equipeModifiee != null) {
                System.out.println("Équipe modifiée avec succès.");
            } else {
                System.out.println("Équipe non trouvée.");
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors de la modification de l'équipe", e);
            System.out.println("Erreur lors de la modification de l'équipe.");
        }
    }

    private void supprimerEquipe() {
        try {
            System.out.println("Entrez l'ID de l'équipe à supprimer:");
            Long id = scanner.nextLong();
            scanner.nextLine();

            equipeController.supprimerEquipe(id);
            System.out.println("Équipe supprimée avec succès.");
        } catch (Exception e) {
            LOGGER.error("Erreur lors de la suppression de l'équipe", e);
            System.out.println("Erreur lors de la suppression de l'équipe.");
        }
    }

    private void afficherEquipe() {
        try {
            System.out.println("Entrez l'ID de l'équipe à afficher:");
            Long id = scanner.nextLong();
            scanner.nextLine();

            Optional<Equipe> equipeOpt = equipeController.obtenirEquipe(id);
            if (equipeOpt.isPresent()) {
                Equipe equipe = equipeOpt.get();
                System.out.println("ID: " + equipe.getId());
                System.out.println("Nom: " + equipe.getNom());
                System.out.println("Joueurs:");
                for (Joueur joueur : equipe.getJoueurs()) {
                    System.out.println("- " + joueur.getPseudo());
                }
            } else {
                System.out.println("Équipe non trouvée.");
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors de l'affichage de l'équipe", e);
            System.out.println("Erreur lors de l'affichage de l'équipe.");
        }
    }

    private void afficherToutesEquipes() {
        try {
            List<Equipe> equipes = equipeController.obtenirToutesEquipes();
            if (!equipes.isEmpty()) {
                System.out.println("Liste des équipes:");
                for (Equipe equipe : equipes) {
                    System.out.println("ID: " + equipe.getId() + ", Nom: " + equipe.getNom());
                }
            } else {
                System.out.println("Aucune équipe trouvée.");
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors de l'affichage des équipes", e);
            System.out.println("Erreur lors de l'affichage des équipes.");
        }
    }

    private void ajouterJoueurAEquipe() {
        try {
            System.out.println("Entrez l'ID de l'équipe:");
            Long equipeId = scanner.nextLong();
            System.out.println("Entrez l'ID du joueur à ajouter:");
            Long joueurId = scanner.nextLong();
            scanner.nextLine();

            Optional<Joueur> joueurOpt = joueurController.obtenirJoueur(joueurId);
            if (joueurOpt.isPresent()) {
                Joueur joueur = joueurOpt.get();
                equipeController.ajouterJoueurAEquipe(equipeId, joueur);
                System.out.println("Joueur ajouté à l'équipe avec succès.");
            } else {
                System.out.println("Joueur non trouvé.");
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors de l'ajout du joueur à l'équipe", e);
            System.out.println("Erreur lors de l'ajout du joueur à l'équipe.");
        }
    }

    private void retirerJoueurDeEquipe() {
        try {
            System.out.println("Entrez l'ID de l'équipe:");
            Long equipeId = scanner.nextLong();
            System.out.println("Entrez l'ID du joueur à retirer:");
            Long joueurId = scanner.nextLong();
            scanner.nextLine();

            // Récupérer d'abord le joueur
            Optional<Joueur> joueurOpt = joueurController.obtenirJoueur(joueurId);
            if (joueurOpt.isPresent()) {
                Joueur joueur = joueurOpt.get();
                equipeController.retirerJoueurDeEquipe(equipeId, joueur);
                System.out.println("Joueur retiré de l'équipe avec succès.");
            } else {
                System.out.println("Joueur non trouvé.");
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors du retrait du joueur de l'équipe", e);
            System.out.println("Erreur lors du retrait du joueur de l'équipe.");
        }
    }

    // Gestion des Tournois
    private void gererMenuTournois() {
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n=== Gestion des Tournois ===");
            System.out.println("1. Créer un tournoi");
            System.out.println("2. Modifier un tournoi");
            System.out.println("3. Supprimer un tournoi");
            System.out.println("4. Afficher un tournoi");
            System.out.println("5. Afficher tous les tournois");
            System.out.println("6. Ajouter une équipe à un tournoi");
            System.out.println("7. Retirer une équipe d'un tournoi");
            System.out.println("8. Calculer la durée estimée d'un tournoi");
            System.out.println("9. Modifier le statut d'un tournoi");
            System.out.println("0. Retour");

            try {
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
                        System.out.println("Option invalide");
                }
            } catch (Exception e) {
                LOGGER.error("Erreur dans le menu tournois", e);
                scanner.nextLine();
                System.out.println("Erreur de saisie. Veuillez réessayer.");
            }
        }
    }

    private void creerTournoi() {
        try {
            System.out.println("Entrez le titre du tournoi:");
            String titre = scanner.nextLine();

            System.out.println("Entrez l'ID du jeu:");
            Long jeuId = scanner.nextLong();
            scanner.nextLine();

            System.out.println("Entrez la date de début (format: dd/MM/yyyy):");
            String dateDebutStr = scanner.nextLine();
            LocalDate dateDebut = LocalDate.parse(dateDebutStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            System.out.println("Entrez la date de fin (format: dd/MM/yyyy):");
            String dateFinStr = scanner.nextLine();
            LocalDate dateFin = LocalDate.parse(dateFinStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            System.out.println("Entrez le nombre de spectateurs attendus:");
            int nombreSpectateurs = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Entrez la durée moyenne d'un match (en minutes):");
            int dureeMoyenneMatch = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Entrez le temps de pause entre les matchs (en minutes):");
            int tempsPauseEntreMatchs = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Entrez le nombre maximum d'équipes:");
            int nombreMaxEquipes = scanner.nextInt();
            scanner.nextLine();

            Tournoi nouveauTournoi = tournoiController.create(titre, jeuId, dateDebut, dateFin,
                nombreSpectateurs, dureeMoyenneMatch, tempsPauseEntreMatchs, nombreMaxEquipes);
            
            if (nouveauTournoi != null) {
                System.out.println("Tournoi créé avec succès. ID: " + nouveauTournoi.getId());
            } else {
                System.out.println("Échec de la création du tournoi.");
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors de la création du tournoi", e);
            System.out.println("Erreur lors de la création du tournoi.");
        }
    }

    private void modifierTournoi() {
        try {
            System.out.println("Entrez l'ID du tournoi à modifier:");
            Long id = scanner.nextLong();
            scanner.nextLine();

            System.out.println("Entrez le nouveau titre:");
            String nouveauTitre = scanner.nextLine();

            System.out.println("Entrez la nouvelle date de début (format: dd/MM/yyyy):");
            String dateDebutStr = scanner.nextLine();
            LocalDate nouvelleDateDebut = LocalDate.parse(dateDebutStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            System.out.println("Entrez la nouvelle date de fin (format: dd/MM/yyyy):");
            String dateFinStr = scanner.nextLine();
            LocalDate nouvelleDateFin = LocalDate.parse(dateFinStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            System.out.println("Entrez le nouveau nombre de spectateurs:");
            int nouveauNombreSpectateurs = scanner.nextInt();
            scanner.nextLine();

            Tournoi tournoiModifie = tournoiController.modifierTournoi(id, nouveauTitre, nouvelleDateDebut, 
                nouvelleDateFin, nouveauNombreSpectateurs);
            
            if (tournoiModifie != null) {
                System.out.println("Tournoi modifié avec succès.");
            } else {
                System.out.println("Tournoi non trouvé.");
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors de la modification du tournoi", e);
            System.out.println("Erreur lors de la modification du tournoi.");
        }
    }

    private void supprimerTournoi() {
        try {
            System.out.println("Entrez l'ID du tournoi à supprimer:");
            Long id = scanner.nextLong();
            scanner.nextLine();

            tournoiController.supprimerTournoi(id);
            System.out.println("Tournoi supprimé avec succès.");
        } catch (Exception e) {
            LOGGER.error("Erreur lors de la suppression du tournoi", e);
            System.out.println("Erreur lors de la suppression du tournoi.");
        }
    }

    private void afficherTournoi() {
        try {
            System.out.println("Entrez l'ID du tournoi à afficher:");
            Long id = scanner.nextLong();
            scanner.nextLine();

            Optional<Tournoi> tournoiOpt = tournoiController.obtenirTournoi(id);
            if (tournoiOpt.isPresent()) {
                Tournoi tournoi = tournoiOpt.get();
                System.out.println("ID: " + tournoi.getId());
                System.out.println("Titre: " + tournoi.getTitre());
                System.out.println("Jeu: " + tournoi.getJeu().getNom());
                System.out.println("Date de début: " + tournoi.getDateDebut());
                System.out.println("Date de fin: " + tournoi.getDateFin());
                System.out.println("Nombre de spectateurs: " + tournoi.getNombreSpectateurs());
                System.out.println("Statut: " + tournoi.getStatut());
                System.out.println("Équipes participantes:");
                for (Equipe equipe : tournoi.getEquipes()) {
                    System.out.println("- " + equipe.getNom());
                }
            } else {
                System.out.println("Tournoi non trouvé.");
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors de l'affichage du tournoi", e);
            System.out.println("Erreur lors de l'affichage du tournoi.");
        }
    }

    private void afficherTousTournois() {
        try {
            List<Tournoi> tournois = tournoiController.obtenirTousTournois();
            if (!tournois.isEmpty()) {
                System.out.println("Liste des tournois:");
                for (Tournoi tournoi : tournois) {
                    System.out.println("ID: " + tournoi.getId() + 
                        ", Titre: " + tournoi.getTitre() + 
                        ", Statut: " + tournoi.getStatut());
                }
            } else {
                System.out.println("Aucun tournoi trouvé.");
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors de l'affichage des tournois", e);
            System.out.println("Erreur lors de l'affichage des tournois.");
        }
    }

    private void ajouterEquipeATournoi() {
        try {
            System.out.println("Entrez l'ID du tournoi:");
            Long tournoiId = scanner.nextLong();
            System.out.println("Entrez l'ID de l'équipe à ajouter:");
            Long equipeId = scanner.nextLong();
            scanner.nextLine();

            tournoiController.ajouterEquipeATournoi(tournoiId, equipeId);
            System.out.println("Équipe ajoutée au tournoi avec succès.");
        } catch (Exception e) {
            LOGGER.error("Erreur lors de l'ajout de l'équipe au tournoi", e);
            System.out.println("Erreur lors de l'ajout de l'équipe au tournoi.");
        }
    }

    private void retirerEquipeDeTournoi() {
        try {
            System.out.println("Entrez l'ID du tournoi:");
            Long tournoiId = scanner.nextLong();
            System.out.println("Entrez l'ID de l'équipe à retirer:");
            Long equipeId = scanner.nextLong();
            scanner.nextLine();

            tournoiController.retirerEquipeDeTournoi(tournoiId, equipeId);
            System.out.println("Équipe retirée du tournoi avec succès.");
        } catch (Exception e) {
            LOGGER.error("Erreur lors du retrait de l'équipe du tournoi", e);
            System.out.println("Erreur lors du retrait de l'équipe du tournoi.");
        }
    }

    private void calculerDureeEstimeeTournoi() {
        try {
            System.out.println("Entrez l'ID du tournoi:");
            Long tournoiId = scanner.nextLong();
            scanner.nextLine();

            int dureeEstimee = tournoiController.calculerDureeEstimee(tournoiId);
            System.out.println("Durée estimée du tournoi: " + dureeEstimee + " minutes");
        } catch (Exception e) {
            LOGGER.error("Erreur lors du calcul de la durée estimée", e);
            System.out.println("Erreur lors du calcul de la durée estimée.");
        }
    }

    private void modifierStatutTournoi() {
        try {
            System.out.println("Entrez l'ID du tournoi:");
            Long tournoiId = scanner.nextLong();
            scanner.nextLine();

            System.out.println("Choisissez le nouveau statut:");
            System.out.println("1. PLANIFIE");
            System.out.println("2. EN_COURS");
            System.out.println("3. TERMINE");
            System.out.println("4. ANNULE");
            
            int choix = scanner.nextInt();
            scanner.nextLine();

            Status nouveauStatut; // Changé de TournoiStatus à Status
            switch (choix) {
                case 1:
                    nouveauStatut = Status.PLANIFIE;
                    break;
                case 2:
                    nouveauStatut = Status.EN_COURS;
                    break;
                case 3:
                    nouveauStatut = Status.TERMINE;
                    break;
                case 4:
                    nouveauStatut = Status.ANNULE;
                    break;
                default:
                    System.out.println("Choix invalide");
                    return;
            }

            tournoiController.modifierStatutTournoi(tournoiId, nouveauStatut);
            System.out.println("Statut du tournoi modifié avec succès.");
        } catch (Exception e) {
            LOGGER.error("Erreur lors de la modification du statut", e);
            System.out.println("Erreur lors de la modification du statut.");
        }
    }

    private void gererMenuJeux() {
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n=== Gestion des Jeux ===");
            System.out.println("1. Ajouter un jeu");
            System.out.println("2. Modifier un jeu");
            System.out.println("3. Supprimer un jeu");
            System.out.println("4. Afficher un jeu");
            System.out.println("5. Afficher tous les jeux");
            System.out.println("0. Retour");

            try {
                int choix = scanner.nextInt();
                scanner.nextLine();

                switch (choix) {
                    case 1:
                        ajouterJeu();
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
                        System.out.println("Option invalide");
                }
            } catch (Exception e) {
                LOGGER.error("Erreur dans le menu jeux", e);
                scanner.nextLine();
                System.out.println("Erreur de saisie. Veuillez réessayer.");
            }
        }
    }

    private void ajouterJeu() {
        try {
            System.out.println("Entrez le nom du jeu:");
            String nom = scanner.nextLine();
            
            System.out.println("Entrez le nombre minimum de joueurs:");
            int minJoueurs = scanner.nextInt();
            
            System.out.println("Entrez le nombre maximum de joueurs:");
            int maxJoueurs = scanner.nextInt();
            scanner.nextLine();

            Jeu nouveauJeu = jeuController.creerJeu(nom, minJoueurs, maxJoueurs);
            if (nouveauJeu != null) {
                System.out.println("Jeu ajouté avec succès. ID: " + nouveauJeu.getId());
            } else {
                System.out.println("Échec de l'ajout du jeu.");
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors de l'ajout du jeu", e);
            System.out.println("Erreur lors de l'ajout du jeu.");
        }
    }

    private void modifierJeu() {
        try {
            System.out.println("Entrez l'ID du jeu à modifier:");
            Long id = scanner.nextLong();
            scanner.nextLine();

            System.out.println("Entrez le nouveau nom:");
            String nouveauNom = scanner.nextLine();

            System.out.println("Entrez le nouveau nombre minimum de joueurs:");
            int minJoueurs = scanner.nextInt();

            System.out.println("Entrez le nouveau nombre maximum de joueurs:");
            int maxJoueurs = scanner.nextInt();
            scanner.nextLine();

            Jeu jeuModifie = jeuController.modifierJeu(id, nouveauNom, minJoueurs, maxJoueurs);
            if (jeuModifie != null) {
                System.out.println("Jeu modifié avec succès.");
            } else {
                System.out.println("Jeu non trouvé.");
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors de la modification du jeu", e);
            System.out.println("Erreur lors de la modification du jeu.");
        }
    }

    private void supprimerJeu() {
        try {
            System.out.println("Entrez l'ID du jeu à supprimer:");
            Long id = scanner.nextLong();
            scanner.nextLine();

            jeuController.supprimerJeu(id);
            System.out.println("Jeu supprimé avec succès.");
        } catch (Exception e) {
            LOGGER.error("Erreur lors de la suppression du jeu", e);
            System.out.println("Erreur lors de la suppression du jeu.");
        }
    }

    private void afficherJeu() {
        try {
            System.out.println("Entrez l'ID du jeu à afficher:");
            Long id = scanner.nextLong();
            scanner.nextLine();

            Optional<Jeu> jeuOpt = jeuController.obtenirJeu(id);
            if (jeuOpt.isPresent()) {
                Jeu jeu = jeuOpt.get();
                System.out.println("ID: " + jeu.getId());
                System.out.println("Nom: " + jeu.getNom());
            } else {
                System.out.println("Jeu non trouvé.");
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors de l'affichage du jeu", e);
            System.out.println("Erreur lors de l'affichage du jeu.");
        }
    }

    private void afficherTousJeux() {
        try {
            List<Jeu> jeux = jeuController.obtenirTousJeux();
            if (!jeux.isEmpty()) {
                System.out.println("Liste des jeux:");
                for (Jeu jeu : jeux) {
                    System.out.println("ID: " + jeu.getId() + ", Nom: " + jeu.getNom());
                }
            } else {
                System.out.println("Aucun jeu trouvé.");
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors de l'affichage des jeux", e);
            System.out.println("Erreur lors de l'affichage des jeux.");
        }
    }

    public static void main(String[] args) {
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

            JoueurController joueurController = context.getBean(JoueurController.class);
            EquipeController equipeController = context.getBean(EquipeController.class);
            TournoiController tournoiController = context.getBean(TournoiController.class);
            JeuController jeuController = context.getBean(JeuController.class);

            ConsoleUi consoleUi = new ConsoleUi(
                joueurController,
                equipeController,
                tournoiController,
                jeuController
            );

            consoleUi.afficherMenuPrincipal();

        } catch (Exception e) {
            LOGGER.error("Erreur lors du démarrage de l'application", e);
            System.out.println("Une erreur est survenue lors du démarrage de l'application.");
        }
    }
}

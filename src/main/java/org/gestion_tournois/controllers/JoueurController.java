package org.gestion_tournois.controllers;

import org.gestion_tournois.models.Joueur;
import org.gestion_tournois.services.JoueurService;
import org.gestion_tournois.utils.MessageLogger;

import java.util.List;
import java.util.Optional;

public class JoueurController {

    private final JoueurService joueurService;

    public JoueurController(JoueurService joueurService) {
        this.joueurService = joueurService;
    }

    public Joueur inscrireJoueur(String pseudo, int age) {
        MessageLogger.info("Tentative d'inscription d'un nouveau joueur: " + pseudo);
        Joueur joueur = new Joueur();
        joueur.setPseudo(pseudo);
        joueur.setAge(age);
        return joueurService.create(joueur);
    }

    public Joueur modifierJoueur(Long id, String nouveauPseudo, int nouvelAge) {
        MessageLogger.info("Tentative de modification du joueur avec l'ID: " + id);
        Optional<Joueur> joueurOptional = joueurService.searchById(id);
        if (joueurOptional.isPresent()) {
            Joueur joueur = joueurOptional.get();
            joueur.setPseudo(nouveauPseudo);
            joueur.setAge(nouvelAge);
            return joueurService.update(joueur);
        } else {
            MessageLogger.warn("Joueur avec l'ID " + id + " non trouvé");
            return null;
        }
    }

    public void supprimerJoueur(Long id) {
        MessageLogger.info("Tentative de suppression du joueur avec l'ID: " + id);
        joueurService.delete(id);
    }

    public Optional<Joueur> obtenirJoueur(Long id) {
        MessageLogger.info("Tentative d'obtention du joueur avec l'ID: " + id);
        return joueurService.searchById(id);
    }

    public List<Joueur> obtenirTousJoueurs() {
        MessageLogger.info("Tentative d'obtention de tous les joueurs");
        return joueurService.getAll();
    }

    public List<Joueur> obtenirJoueursParEquipe(Long equipeId) {
        MessageLogger.info("Tentative d'obtention des joueurs pour l'équipe avec l'ID: " + equipeId);
        return joueurService.searchByEquipe(equipeId);
    }

    public boolean existeJoueurParPseudo(String pseudo) {
        MessageLogger.info("Vérification de l'existence d'un joueur avec le pseudo: " + pseudo);
        return joueurService.existeByPseudo(pseudo);
    }

    public Optional<Joueur> obtenirJoueurParPseudo(String pseudo) {
        MessageLogger.info("Tentative d'obtention du joueur avec le pseudo: " + pseudo);
        return joueurService.searchByPseudo(pseudo);
    }
}

package org.gestion_tournois.controller;

import org.gestion_tournois.model.Joueur;
import org.gestion_tournois.service.interfaces.JoueurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class JoueurController {
    private static final Logger LOGGER = LoggerFactory.getLogger(JoueurController.class);
    private final JoueurService joueurService;

    public JoueurController(JoueurService joueurService) {
        this.joueurService = joueurService;
    }

    public Joueur inscrireJoueur(String pseudo, int age) {
        LOGGER.info("Tentative d'inscription d'un nouveau joueur: {}", pseudo);
        Joueur joueur = new Joueur();
        joueur.setPseudo(pseudo);
        joueur.setAge(age);
        return joueurService.inscrireJoueur(joueur);
    }

    public Joueur modifierJoueur(Long id, String nouveauPseudo, int nouvelAge) {
        LOGGER.info("Tentative de modification du joueur avec l'ID: {}", id);
        Optional<Joueur> joueurOptional = joueurService.obtenirJoueur(id);
        if (joueurOptional.isPresent()) {
            Joueur joueur = joueurOptional.get();
            joueur.setPseudo(nouveauPseudo);
            joueur.setAge(nouvelAge);
            return joueurService.modifierJoueur(joueur);
        } else {
            LOGGER.warn("Joueur avec l'ID {} non trouvé", id);
            return null;
        }
    }

    public void supprimerJoueur(Long id) {
        LOGGER.info("Tentative de suppression du joueur avec l'ID: {}", id);
        joueurService.supprimerJoueur(id);
    }

    public Optional<Joueur> obtenirJoueur(Long id) {
        LOGGER.info("Tentative d'obtention du joueur avec l'ID: {}", id);
        return joueurService.obtenirJoueur(id);
    }

    public List<Joueur> obtenirTousJoueurs() {
        LOGGER.info("Tentative d'obtention de tous les joueurs");
        return joueurService.obtenirTousJoueurs();
    }

    public List<Joueur> obtenirJoueursParEquipe(Long equipeId) {
        LOGGER.info("Tentative d'obtention des joueurs pour l'équipe avec l'ID: {}", equipeId);
        return joueurService.obtenirJoueursParEquipe(equipeId);
    }

    public boolean existeJoueurParPseudo(String pseudo) {
        LOGGER.info("Vérification de l'existence d'un joueur avec le pseudo: {}", pseudo);
        return joueurService.existeParPseudo(pseudo);
    }

    public Optional<Joueur> obtenirJoueurParPseudo(String pseudo) {
        LOGGER.info("Tentative d'obtention du joueur avec le pseudo: {}", pseudo);
        return joueurService.trouverParPseudo(pseudo);
    }
}

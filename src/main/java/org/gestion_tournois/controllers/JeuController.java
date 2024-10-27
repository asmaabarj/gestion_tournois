package org.gestion_tournois.controllers;

import org.gestion_tournois.models.Jeu;
import org.gestion_tournois.services.JeuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class JeuController {
    private static final Logger LOGGER = LoggerFactory.getLogger(JeuController.class);
    private final JeuService jeuService;

    public JeuController(JeuService jeuService) {
        this.jeuService = jeuService;
    }

    public Jeu creerJeu(String nom, int difficulte, int dureeMoyenneMatch) {
        LOGGER.info("Tentative de création d'un nouveau jeu: {}", nom);
        Jeu jeu = new Jeu();
        jeu.setNom(nom);
        jeu.setDifficulte(difficulte);
        jeu.setDureeMoyenneMatch(dureeMoyenneMatch);
        return jeuService.create(jeu);
    }

    public Jeu modifierJeu(Long id, String nouveauNom, int nouvelleDifficulte, int nouvelleDureeMoyenneMatch) {
        LOGGER.info("Tentative de modification du jeu avec l'ID: {}", id);
        Optional<Jeu> jeuOptional = jeuService.searchById(id);
        if (jeuOptional.isPresent()) {
            Jeu jeu = jeuOptional.get();
            jeu.setNom(nouveauNom);
            jeu.setDifficulte(nouvelleDifficulte);
            jeu.setDureeMoyenneMatch(nouvelleDureeMoyenneMatch);
            return jeuService.update(jeu);
        } else {
            LOGGER.warn("Jeu avec l'ID {} non trouvé", id);
            return null;
        }
    }

    public void supprimerJeu(Long id) {
        LOGGER.info("Tentative de suppression du jeu avec l'ID: {}", id);
        jeuService.delete(id);
    }

    public Optional<Jeu> obtenirJeu(Long id) {
        LOGGER.info("Tentative d'obtention du jeu avec l'ID: {}", id);
        return jeuService.searchById(id);
    }

    public List<Jeu> obtenirTousJeux() {
        LOGGER.info("Tentative d'obtention de tous les jeux");
        return jeuService.getAll();
    }
}

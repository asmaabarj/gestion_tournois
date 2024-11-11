package org.gestion_tournois.controller;

import org.gestion_tournois.model.Equipe;
import org.gestion_tournois.service.interfaces.EquipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class EquipeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EquipeController.class);
    private final EquipeService equipeService;

    public EquipeController(EquipeService equipeService) {
        this.equipeService = equipeService;
    }

    public Equipe creerEquipe(String nom) {
        LOGGER.info("Tentative de création d'une nouvelle équipe: {}", nom);
        Equipe equipe = new Equipe();
        equipe.setNom(nom);
        return equipeService.creerEquipe(equipe);
    }

    public Equipe modifierEquipe(Long id, String nouveauNom) {
        LOGGER.info("Tentative de modification de l'équipe avec l'ID: {}", id);
        Optional<Equipe> equipeOptional = equipeService.obtenirEquipe(id);
        if (equipeOptional.isPresent()) {
            Equipe equipe = equipeOptional.get();
            equipe.setNom(nouveauNom);
            return equipeService.modifierEquipe(equipe);
        } else {
            LOGGER.warn("Équipe avec l'ID {} non trouvée", id);
            return null;
        }
    }

    public void supprimerEquipe(Long id) {
        LOGGER.info("Tentative de suppression de l'équipe avec l'ID: {}", id);
        equipeService.supprimerEquipe(id);
    }

    public Optional<Equipe> obtenirEquipe(Long id) {
        LOGGER.info("Tentative d'obtention de l'équipe avec l'ID: {}", id);
        return equipeService.obtenirEquipe(id);
    }

    public List<Equipe> obtenirToutesEquipes() {
        LOGGER.info("Tentative d'obtention de toutes les équipes");
        return equipeService.obtenirToutesEquipes();
    }

    public void ajouterJoueurAEquipe(Long equipeId, Long joueurId) {
        LOGGER.info("Tentative d'ajout du joueur {} à l'équipe {}", joueurId, equipeId);
        equipeService.ajouterJoueur(equipeId, joueurId);
    }

    public void retirerJoueurDeEquipe(Long equipeId, Long joueurId) {
        LOGGER.info("Tentative de retrait du joueur {} de l'équipe {}", joueurId, equipeId);
        equipeService.retirerJoueur(equipeId, joueurId);
    }

    public List<Equipe> obtenirEquipesParTournoi(Long tournoiId) {
        LOGGER.info("Tentative d'obtention des équipes pour le tournoi avec l'ID: {}", tournoiId);
        return equipeService.obtenirEquipesParTournoi(tournoiId);
    }
}

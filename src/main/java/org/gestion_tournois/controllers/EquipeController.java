package org.gestion_tournois.controllers;

import org.gestion_tournois.models.Equipe;
import org.gestion_tournois.models.Joueur;
import org.gestion_tournois.services.EquipeService;
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
        return equipeService.create(equipe);
    }

    public Equipe modifierEquipe(Long id, String nouveauNom) {
        LOGGER.info("Tentative de modification de l'équipe avec l'ID: {}", id);
        Optional<Equipe> equipeOptional = equipeService.searchById(id);
        if (equipeOptional.isPresent()) {
            Equipe equipe = equipeOptional.get();
            equipe.setNom(nouveauNom);
            return equipeService.update(equipe);
        } else {
            LOGGER.warn("Équipe avec l'ID {} non trouvée", id);
            return null;
        }
    }

    public void supprimerEquipe(Long id) {
        LOGGER.info("Tentative de suppression de l'équipe avec l'ID: {}", id);
        equipeService.delete(id);
    }

    public Optional<Equipe> obtenirEquipe(Long id) {
        LOGGER.info("Tentative d'obtention de l'équipe avec l'ID: {}", id);
        return equipeService.searchById(id);
    }

    public List<Equipe> obtenirToutesEquipes() {
        LOGGER.info("Tentative d'obtention de toutes les équipes");
        return equipeService.getAll();
    }

    public void ajouterJoueurAEquipe(Long equipeId, Joueur joueur) {
        LOGGER.info("Tentative d'ajout du joueur {} à l'équipe {}", joueur.getPseudo(), equipeId);
        equipeService.addJoueur(equipeId, joueur);
    }

    public void retirerJoueurDeEquipe(Long equipeId, Joueur joueur) {
        LOGGER.info("Tentative de retrait du joueur {} de l'équipe {}", joueur.getPseudo(), equipeId);
        equipeService.deleteJoueur(equipeId, joueur);
    }

    public List<Equipe> obtenirEquipesParTournoi(Long tournoiId) {
        LOGGER.info("Tentative d'obtention des équipes pour le tournoi avec l'ID: {}", tournoiId);
        return equipeService.searchByTournoi(tournoiId);
    }
}

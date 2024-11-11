package org.gestion_tournois.service.impl;

import org.gestion_tournois.repositories.interfaces.EquipeDao;
import org.gestion_tournois.repositories.interfaces.JoueurDao;
import org.gestion_tournois.model.Equipe;
import org.gestion_tournois.model.Joueur;
import org.gestion_tournois.service.interfaces.EquipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class EquipeServiceImpl implements EquipeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EquipeServiceImpl.class);

    private final EquipeDao equipeDao;
    private final JoueurDao joueurDao;

    public EquipeServiceImpl(EquipeDao equipeDao, JoueurDao joueurDao) {
        this.equipeDao = equipeDao;
        this.joueurDao = joueurDao;
    }

    @Override
    @Transactional
    public Equipe creerEquipe(Equipe equipe) {
        LOGGER.info("Création d'une nouvelle équipe");
        return equipeDao.creer(equipe);
    }

    @Override
    @Transactional
    public Equipe modifierEquipe(Equipe equipe) {
        LOGGER.info("Modification de l'équipe avec l'ID: {}", equipe.getId());
        return equipeDao.modifier(equipe);
    }

    @Override
    @Transactional
    public void supprimerEquipe(Long id) {
        LOGGER.info("Suppression de l'équipe avec l'ID: {}", id);
        equipeDao.supprimer(id);
    }

    @Override
    public Optional<Equipe> obtenirEquipe(Long id) {
        LOGGER.info("Recherche de l'équipe avec l'ID: {}", id);
        return equipeDao.trouverParId(id);
    }

    @Override
    public List<Equipe> obtenirToutesEquipes() {
        LOGGER.info("Récupération de toutes les équipes");
        return equipeDao.trouverTous();
    }

    @Override
    @Transactional
    public void ajouterJoueur(Long equipeId, Long joueurId) {
        LOGGER.info("Ajout du joueur {} à l'équipe {}", joueurId, equipeId);
        Optional<Equipe> equipeOptional = equipeDao.trouverParId(equipeId);
        Optional<Joueur> joueurOptional = joueurDao.trouverParId(joueurId);
        if (equipeOptional.isPresent() && joueurOptional.isPresent()) {
            Equipe equipe = equipeOptional.get();
            Joueur joueur = joueurOptional.get();
            equipe.getJoueurs().add(joueur);
            joueur.setEquipe(equipe);
            equipeDao.modifier(equipe);
        } else {
            LOGGER.warn("Équipe avec l'ID {} ou Joueur avec l'ID {} non trouvé", equipeId, joueurId);
        }
    }

    @Override
    @Transactional
    public void retirerJoueur(Long equipeId, Long joueurId) {
        LOGGER.info("Retrait du joueur {} de l'équipe {}", joueurId, equipeId);
        Optional<Joueur> joueur = joueurDao.trouverParId(joueurId);
        if (joueur.isPresent()) {
            equipeDao.retirerJoueur(equipeId, joueur.get());
        } else {
            LOGGER.warn("Joueur avec l'ID {} non trouvé", joueurId);
        }
    }

    @Override
    public List<Equipe> obtenirEquipesParTournoi(Long tournoiId) {
        LOGGER.info("Récupération des équipes pour le tournoi avec l'ID: {}", tournoiId);
        return equipeDao.trouverParTournoi(tournoiId);
    }
}

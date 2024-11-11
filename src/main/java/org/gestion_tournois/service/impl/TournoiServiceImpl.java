package org.gestion_tournois.service.impl;

import org.gestion_tournois.repositories.interfaces.TournoiDao;
import org.gestion_tournois.repositories.interfaces.EquipeDao;
import org.gestion_tournois.model.Tournoi;
import org.gestion_tournois.model.Equipe;
import org.gestion_tournois.service.interfaces.TournoiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.gestion_tournois.model.enums.TournoiStatus;

import java.util.List;
import java.util.Optional;

@Transactional
public class TournoiServiceImpl implements TournoiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TournoiServiceImpl.class);

    private final TournoiDao tournoiDao;
    private final EquipeDao equipeDao;

    public TournoiServiceImpl(TournoiDao tournoiDao, EquipeDao equipeDao) {
        this.tournoiDao = tournoiDao;
        this.equipeDao = equipeDao;
    }

    @Override
    public Tournoi creerTournoi(Tournoi tournoi) {
        LOGGER.info("Création d'un nouveau tournoi");
        return tournoiDao.creer(tournoi);
    }

    @Override
    public Tournoi modifierTournoi(Tournoi tournoi) {
        LOGGER.info("Modification du tournoi avec l'ID: {}", tournoi.getId());
        return tournoiDao.modifier(tournoi);
    }

    @Override
    public void supprimerTournoi(Long id) {
        LOGGER.info("Suppression du tournoi avec l'ID: {}", id);
        tournoiDao.supprimer(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tournoi> obtenirTournoi(Long id) {
        LOGGER.info("Recherche du tournoi avec l'ID: {}", id);
        return tournoiDao.trouverParIdAvecEquipes(id);
    }

    @Override
    public List<Tournoi> obtenirTousTournois() {
        LOGGER.info("Récupération de tous les tournois");
        return tournoiDao.trouverTous();
    }

    @Override
    public void ajouterEquipe(Long tournoiId, Long equipeId) {
        LOGGER.info("Ajout de l'équipe {} au tournoi {}", equipeId, tournoiId);
        Optional<Equipe> equipe = equipeDao.trouverParId(equipeId);
        if (equipe.isPresent()) {
            tournoiDao.ajouterEquipe(tournoiId, equipe.get());
        } else {
            LOGGER.warn("Équipe avec l'ID {} non trouvée", equipeId);
        }
    }

    @Override
    public void retirerEquipe(Long tournoiId, Long equipeId) {
        LOGGER.info("Retrait de l'équipe {} du tournoi {}", equipeId, tournoiId);
        Optional<Equipe> equipe = equipeDao.trouverParId(equipeId);
        if (equipe.isPresent()) {
            tournoiDao.retirerEquipe(tournoiId, equipe.get());
        } else {
            LOGGER.warn("Équipe avec l'ID {} non trouvée", equipeId);
        }
    }

    @Override
    public int calculerdureeEstimeeTournoi(Long tournoiId) {
        LOGGER.info("Calcul de la durée estimée pour le tournoi avec l'ID: {}", tournoiId);
        return tournoiDao.calculerdureeEstimeeTournoi(tournoiId);
    }

    @Override
    @Transactional
    public void modifierStatutTournoi(Long tournoiId, TournoiStatus nouveauStatut) {
        LOGGER.info("Modification du statut du tournoi {} à {}", tournoiId, nouveauStatut);
        tournoiDao.modifierStatut(tournoiId, nouveauStatut);
    }
}

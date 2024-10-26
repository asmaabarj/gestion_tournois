package org.gestion_tournois.repositories.impl;

import org.gestion_tournois.repositories.interfaces.TournoiDao;
import org.gestion_tournois.models.Tournoi;
import org.gestion_tournois.models.Equipe;
import org.gestion_tournois.models.Jeu;
import org.gestion_tournois.models.enums.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

public class TournoiDaoExtension implements TournoiDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(TournoiDaoExtension.class);

    @PersistenceContext
    private EntityManager entityManager;

    private final TournoiDaoImpl tournoiDaoImpl;

    public TournoiDaoExtension(TournoiDaoImpl tournoiDaoImpl) {
        this.tournoiDaoImpl = tournoiDaoImpl;
    }

    @Override
    public Tournoi create(Tournoi tournoi) {
        return tournoiDaoImpl.create(tournoi);
    }

    @Override
    public Tournoi update(Tournoi tournoi) {
        return tournoiDaoImpl.update(tournoi);
    }

    @Override
    public void delete(Long id) {
        tournoiDaoImpl.delete(id);
    }

    @Override
    public Optional<Tournoi> searchById(Long id) {
        return tournoiDaoImpl.searchById(id);
    }

    @Override
    public List<Tournoi> getAll() {
        return tournoiDaoImpl.getAll();
    }

    @Override
    public void addEquipe(Long tournoiId, Equipe equipe) {
        tournoiDaoImpl.addEquipe(tournoiId, equipe);
    }

    @Override
    public void deleteEquipe(Long tournoiId, Equipe equipe) {
        tournoiDaoImpl.deleteEquipe(tournoiId, equipe);
    }

    @Override
    public Optional<Tournoi> searchByIdWithEquipes(Long id) {
        return tournoiDaoImpl.searchByIdWithEquipes(id);
    }

    @Override
    public int calculatedureeEstimeeTournoi(Long tournoiId) {
        try {
            Tournoi tournoi = entityManager.find(Tournoi.class, tournoiId);
            if (tournoi != null) {
                int nombreEquipes = tournoi.getEquipes().size();
                Jeu jeu = tournoi.getJeu();
                int dureeMoyenneMatch = jeu.getDureeMoyenneMatch();
                int difficulteJeu = jeu.getDifficulte();

                int tempsPauseEntreMatchs = tournoi.getTempsPauseEntreMatchs();
                int dureeEstimee = (nombreEquipes * dureeMoyenneMatch * difficulteJeu) + tempsPauseEntreMatchs; // Ajustement avec la difficulté
                tournoi.setDureeEstimee(dureeEstimee);
                entityManager.merge(tournoi);
                return dureeEstimee;
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors du calcul de la durée estimée du tournoi: ", e);
        }
        return 0;
    }

    @Override
    public void updateStatut(Long tournoiId, Status nouveauStatut) {
        tournoiDaoImpl.updateStatut(tournoiId, nouveauStatut);
    }
}

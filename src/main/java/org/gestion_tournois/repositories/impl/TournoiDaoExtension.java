package org.gestion_tournois.repositories.impl;

import org.gestion_tournois.repositories.interfaces.TournoiDao;
import org.gestion_tournois.model.Tournoi;
import org.gestion_tournois.model.Equipe;
import org.gestion_tournois.model.Jeu;
import org.gestion_tournois.model.enums.TournoiStatus;
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
    public Tournoi creer(Tournoi tournoi) {
        return tournoiDaoImpl.creer(tournoi);
    }

    @Override
    public Tournoi modifier(Tournoi tournoi) {
        return tournoiDaoImpl.modifier(tournoi);
    }

    @Override
    public void supprimer(Long id) {
        tournoiDaoImpl.supprimer(id);
    }

    @Override
    public Optional<Tournoi> trouverParId(Long id) {
        return tournoiDaoImpl.trouverParId(id);
    }

    @Override
    public List<Tournoi> trouverTous() {
        return tournoiDaoImpl.trouverTous();
    }

    @Override
    public void ajouterEquipe(Long tournoiId, Equipe equipe) {
        tournoiDaoImpl.ajouterEquipe(tournoiId, equipe);
    }

    @Override
    public void retirerEquipe(Long tournoiId, Equipe equipe) {
        tournoiDaoImpl.retirerEquipe(tournoiId, equipe);
    }

    @Override
    public Optional<Tournoi> trouverParIdAvecEquipes(Long id) {
        return tournoiDaoImpl.trouverParIdAvecEquipes(id);
    }

    @Override
    public int calculerdureeEstimeeTournoi(Long tournoiId) {
        Tournoi tournoi = entityManager.find(Tournoi.class, tournoiId);
        if (tournoi != null) {
            int nombreEquipes = tournoi.getEquipes().size();
            Jeu jeu = tournoi.getJeu();
            int dureeMoyenneMatch = jeu.getDureeMoyenneMatch();
            int difficulteJeu = jeu.getDifficulte();
            int tempsPauseEntreMatchs = tournoi.getTempsPauseEntreMatchs();
            int tempsCeremonie = tournoi.getTempsCeremonie();

            int dureeEstimee = (nombreEquipes * dureeMoyenneMatch * difficulteJeu) + tempsPauseEntreMatchs
                    + tempsCeremonie;
            tournoi.setDureeEstimee(dureeEstimee);
            entityManager.merge(tournoi);
            return dureeEstimee;
        }
        return 0;
    }

    @Override
    public void modifierStatut(Long tournoiId, TournoiStatus nouveauStatut) {
        tournoiDaoImpl.modifierStatut(tournoiId, nouveauStatut);
    }
}

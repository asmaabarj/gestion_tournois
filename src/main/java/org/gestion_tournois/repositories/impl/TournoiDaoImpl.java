package org.gestion_tournois.repositories.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.gestion_tournois.models.Equipe;
import org.gestion_tournois.models.Jeu;
import org.gestion_tournois.models.Tournoi;
import org.gestion_tournois.models.enums.Status;
import org.gestion_tournois.repositories.interfaces.TournoiDao;
import org.gestion_tournois.utils.MessageLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TournoiDaoImpl implements TournoiDao {

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger LOGGER = LoggerFactory.getLogger(TournoiDaoImpl.class);

    @Override
    public Tournoi create(Tournoi tournoi) {
        try {
            entityManager.persist(tournoi);
            LOGGER.info("Création du tournoi - ID: {}", tournoi.getId());
            return tournoi;
        } catch (Exception e) {
            LOGGER.error("Échec de la création du tournoi", e);
            return null;
        }
    }

    @Override
    public Tournoi update(Tournoi tournoi) {
        try {
            Tournoi updatedTournoi = entityManager.merge(tournoi);
            LOGGER.info("Mise à jour du tournoi - ID: {}", updatedTournoi.getId());
            return updatedTournoi;
        } catch (Exception e) {
            LOGGER.error("Échec de la mise à jour du tournoi", e);
            return null;
        }
    }

    @Override
    public void delete(Long id) {
        try {
            Tournoi tournoi = entityManager.find(Tournoi.class, id);
            if (tournoi != null) {
                entityManager.remove(tournoi);
                MessageLogger.info("Tournoi supprimé avec l'ID: " + id);
            } else {
                MessageLogger.warn("Tentative de suppression d'un tournoi inexistant avec l'ID: " + id);
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors de la suppression du tournoi: ", e);
        }
    }

    @Override
    public Optional<Tournoi> searchById(Long id) {
        try {
            return Optional.ofNullable(entityManager.find(Tournoi.class, id));
        } catch (Exception e) {
            LOGGER.error("Erreur lors de la recherche du tournoi avec l'ID " + id + ": ", e);
            return Optional.empty();
        }
    }

    @Override
    public List<Tournoi> getAll() {
        try {
            Query query = entityManager.createQuery("SELECT t FROM Tournoi t");
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.error("Échec de la récupération des tournois", e);
            return Collections.emptyList();
        }
    }

    @Override
    public void addEquipe(Long tournoiId, Long equipeId) {
        try {
            Tournoi tournoi = entityManager.find(Tournoi.class, tournoiId);
            Equipe equipe = entityManager.find(Equipe.class, equipeId);
            if (tournoi != null && equipe != null) {
                tournoi.getEquipes().add(equipe);
                entityManager.merge(tournoi);
                MessageLogger.info("Équipe ajoutée au tournoi avec l'ID: " + tournoiId);
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors de l'ajout de l'équipe au tournoi: ", e);
        }
    }

    @Override
    public void deleteEquipe(Long tournoiId, Long equipeId) {
        try {
            Tournoi tournoi = entityManager.find(Tournoi.class, tournoiId);
            Equipe equipe = entityManager.find(Equipe.class, equipeId);
            if (tournoi != null && equipe != null) {
                tournoi.getEquipes().remove(equipe);
                entityManager.merge(tournoi);
                MessageLogger.info("Équipe retirée du tournoi avec l'ID: " + tournoiId);
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors du retrait de l'équipe du tournoi: ", e);
        }
    }

    @Override
    public int calculatedureeEstimeeTournoi(Long tournoiId) {
        try {
            Tournoi tournoi = entityManager.find(Tournoi.class, tournoiId);
            if (tournoi != null) {
                int nombreEquipes = tournoi.getEquipes().size();
                Jeu jeu = tournoi.getJeu();
                int dureeMoyenneMatch = jeu.getDureeMoyenneMatch();
                int tempsPauseEntreMatchs = tournoi.getTempsPauseEntreMatchs();

                int dureeEstimee = (nombreEquipes * dureeMoyenneMatch) + tempsPauseEntreMatchs;
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
    public Optional<Tournoi> searchByIdWithEquipes(Long id) {
        try {
            Query query = entityManager.createQuery(
                    "SELECT DISTINCT t FROM Tournoi t LEFT JOIN FETCH t.equipes WHERE t.id = :id");
            query.setParameter("id", id);
            List<Tournoi> results = query.getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } catch (Exception e) {
            LOGGER.error("Échec de la recherche du tournoi (ID: {}) avec ses équipes", id, e);
            return Optional.empty();
        }
    }

    @Override
    public void updateStatut(Long tournoiId, Status nouveauStatut) {
        try {
            Tournoi tournoi = entityManager.find(Tournoi.class, tournoiId);
            if (tournoi != null) {
                tournoi.setStatut(nouveauStatut);
                entityManager.merge(tournoi);
                LOGGER.info("Statut du tournoi avec l'ID {} modifié à {}", tournoiId, nouveauStatut);
            } else {
                LOGGER.warn("Tentative de modification du statut d'un tournoi inexistant avec l'ID: {}", tournoiId);
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors de la mise à jour du statut du tournoi: ", e);
        }
    }
}

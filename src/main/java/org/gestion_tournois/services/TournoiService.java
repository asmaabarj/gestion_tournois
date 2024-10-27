package org.gestion_tournois.services;

import java.util.List;
import java.util.Optional;

import org.gestion_tournois.models.Tournoi;
import org.gestion_tournois.models.enums.Status;
import org.gestion_tournois.repositories.interfaces.TournoiDao;

public class TournoiService {

    private final TournoiDao tournoiDao;

    public TournoiService(TournoiDao tournoiDao) {
        this.tournoiDao = tournoiDao;
    }

    public Tournoi create(Tournoi tournoi) {
        return tournoiDao.create(tournoi);
    }

    public Tournoi update(Tournoi tournoi) {
        return tournoiDao.update(tournoi);
    }

    public void delete(Long id) {
        tournoiDao.delete(id);
    }

    public Optional<Tournoi> searchById(Long id) {
        return tournoiDao.searchById(id);
    }

    public List<Tournoi> getAll() {
        return tournoiDao.getAll();
    }

    public void addEquipe(Long tournoiId, Long equipeId) {
        tournoiDao.addEquipe(tournoiId, equipeId);
    }

    public void deleteEquipe(Long tournoiId, Long equipeId) {
        tournoiDao.deleteEquipe(tournoiId, equipeId);
    }

    public int calculatedureeEstimeeTournoi(Long tournoiId) {
        return tournoiDao.calculatedureeEstimeeTournoi(tournoiId);
    }

    public Optional<Tournoi> searchByIdWithEquipes(Long id) {
        return tournoiDao.searchByIdWithEquipes(id);
    }

    public void updateStatut(Long tournoiId, Status nouveauStatut) {
        tournoiDao.updateStatut(tournoiId, nouveauStatut);
    }
}

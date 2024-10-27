package org.gestion_tournois.repositories.interfaces;

import java.util.List;
import java.util.Optional;

import org.gestion_tournois.models.Tournoi;
import org.gestion_tournois.models.enums.Status;

public interface TournoiDao {
    Tournoi create(Tournoi tournoi);
    Tournoi update(Tournoi tournoi);
    void delete(Long id);
    Optional<Tournoi> searchById(Long id);
    List<Tournoi> getAll();
    void addEquipe(Long tournoiId, Long equipeId);
    void deleteEquipe(Long tournoiId, Long equipeId);

    public int calculatedureeEstimeeTournoi(Long tournoiId);
    public Optional<Tournoi> searchByIdWithEquipes(Long id);
    public void updateStatut(Long tournoiId, Status nouveauStatut);

    }

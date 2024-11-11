package org.gestion_tournois.service.interfaces;

import org.gestion_tournois.model.Tournoi;

import java.util.List;
import java.util.Optional;
import org.gestion_tournois.model.enums.TournoiStatus;

public interface TournoiService {
    Tournoi creerTournoi(Tournoi tournoi);

    Tournoi modifierTournoi(Tournoi tournoi);

    void supprimerTournoi(Long id);

    Optional<Tournoi> obtenirTournoi(Long id);

    List<Tournoi> obtenirTousTournois();

    void ajouterEquipe(Long tournoiId, Long equipeId);

    void retirerEquipe(Long tournoiId, Long equipeId);

    int calculerdureeEstimeeTournoi(Long tournoiId);

    void modifierStatutTournoi(Long tournoiId, TournoiStatus nouveauStatut);
}

package org.gestion_tournois.repositories.interfaces;

import org.gestion_tournois.models.Equipe;
import org.gestion_tournois.models.Joueur;

import java.util.List;
import java.util.Optional;

public interface EquipeDao {

    Equipe create(Equipe equipe);

    Equipe update(Equipe equipe);

    void delete(Long id);

    Optional<Equipe> searchById(Long id);

    List<Equipe> getAll();

    void addJoueur(Long equipeId, Joueur joueur);

    void deleteJoueur(Long equipeId, Joueur joueur);

    List<Equipe> searchByTournoi(Long tournoiId);
}

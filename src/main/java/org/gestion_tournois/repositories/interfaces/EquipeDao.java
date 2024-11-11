package org.gestion_tournois.repositories.interfaces;

import org.gestion_tournois.model.Equipe;
import org.gestion_tournois.model.Joueur;
import java.util.List;
import java.util.Optional;

public interface EquipeDao {
    Equipe creer(Equipe equipe);

    Equipe modifier(Equipe equipe);

    void supprimer(Long id);

    Optional<Equipe> trouverParId(Long id);

    List<Equipe> trouverTous();

    void ajouterJoueur(Long equipeId, Joueur joueur);

    void retirerJoueur(Long equipeId, Joueur joueur);

    List<Equipe> trouverParTournoi(Long tournoiId);
}

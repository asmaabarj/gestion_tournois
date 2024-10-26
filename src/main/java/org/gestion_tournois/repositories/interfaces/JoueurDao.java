package org.gestion_tournois.repositories.interfaces;

import org.gestion_tournois.models.Joueur;

import java.util.List;
import java.util.Optional;

public interface JoueurDao {
    Joueur create(Joueur joueur);
    Joueur update(Joueur joueur);
    void delete(Long id);
    Optional<Joueur> searchById(Long id);
    List<Joueur> getAll();
    List<Joueur> searchByEquipe(Long equipeId);
    boolean existeByPseudo(String pseudo);
    Optional<Joueur> searchByPseudo(String pseudo);
}

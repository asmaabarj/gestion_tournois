package org.gestion_tournois.repositories.interfaces;

import org.gestion_tournois.models.Jeu;

import java.util.List;
import java.util.Optional;

public interface JeuDao {
    Jeu create(Jeu jeu);
    Jeu update(Jeu jeu);
    void delete(Long id);
    Optional<Jeu> searchById(Long id);
    List<Jeu> getAll();
}

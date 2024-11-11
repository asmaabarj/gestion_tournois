package org.gestion_tournois.service.interfaces;

import org.gestion_tournois.model.Joueur;
import java.util.List;
import java.util.Optional;

public interface JoueurService {
    Joueur inscrireJoueur(Joueur joueur);

    Joueur modifierJoueur(Joueur joueur);

    void supprimerJoueur(Long id);

    Optional<Joueur> obtenirJoueur(Long id);

    List<Joueur> obtenirTousJoueurs();

    List<Joueur> obtenirJoueursParEquipe(Long equipeId);

    boolean existeParPseudo(String pseudo);

    Optional<Joueur> trouverParPseudo(String pseudo);
}

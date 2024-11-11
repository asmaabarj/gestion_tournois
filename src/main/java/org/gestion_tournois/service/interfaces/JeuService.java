package org.gestion_tournois.service.interfaces;

import org.gestion_tournois.model.Jeu;
import java.util.List;
import java.util.Optional;

public interface JeuService {
    Jeu creerJeu(Jeu jeu);

    Jeu modifierJeu(Jeu jeu);

    void supprimerJeu(Long id);

    Optional<Jeu> obtenirJeu(Long id);

    List<Jeu> obtenirTousJeux();
}

package org.gestion_tournois.service.impl;

import org.gestion_tournois.repositories.interfaces.JoueurDao;
import org.gestion_tournois.model.Joueur;
import org.gestion_tournois.service.interfaces.JoueurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class JoueurServiceImpl implements JoueurService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JoueurServiceImpl.class);

    private final JoueurDao joueurDao;

    public JoueurServiceImpl(JoueurDao joueurDao) {
        this.joueurDao = joueurDao;
    }

    @Override
    public Joueur inscrireJoueur(Joueur joueur) {
        LOGGER.info("Inscription d'un nouveau joueur");
        return joueurDao.inscrire(joueur);
    }

    @Override
    public Joueur modifierJoueur(Joueur joueur) {
        LOGGER.info("Modification du joueur avec l'ID: {}", joueur.getId());
        return joueurDao.modifier(joueur);
    }

    @Override
    public void supprimerJoueur(Long id) {
        LOGGER.info("Suppression du joueur avec l'ID: {}", id);
        joueurDao.supprimer(id);
    }

    @Override
    public Optional<Joueur> obtenirJoueur(Long id) {
        LOGGER.info("Recherche du joueur avec l'ID: {}", id);
        return joueurDao.trouverParId(id);
    }

    @Override
    public List<Joueur> obtenirTousJoueurs() {
        LOGGER.info("Récupération de tous les joueurs");
        return joueurDao.trouverTous();
    }

    @Override
    public List<Joueur> obtenirJoueursParEquipe(Long equipeId) {
        LOGGER.info("Récupération des joueurs pour l'équipe avec l'ID: {}", equipeId);
        return joueurDao.trouverParEquipe(equipeId);
    }

    @Override
    public boolean existeParPseudo(String pseudo) {
        LOGGER.info("Vérification de l'existence d'un joueur avec le pseudo: {}", pseudo);
        return joueurDao.existeParPseudo(pseudo);
    }

    @Override
    public Optional<Joueur> trouverParPseudo(String pseudo) {
        LOGGER.info("Recherche d'un joueur avec le pseudo: {}", pseudo);
        return joueurDao.trouverParPseudo(pseudo);
    }
}

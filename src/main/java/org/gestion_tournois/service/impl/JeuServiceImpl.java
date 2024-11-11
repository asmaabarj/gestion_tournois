package org.gestion_tournois.service.impl;

import org.gestion_tournois.repositories.interfaces.JeuDao;
import org.gestion_tournois.model.Jeu;
import org.gestion_tournois.service.interfaces.JeuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class JeuServiceImpl implements JeuService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JeuServiceImpl.class);

    private final JeuDao jeuDao;

    public JeuServiceImpl(JeuDao jeuDao) {
        this.jeuDao = jeuDao;
    }

    @Override
    public Jeu creerJeu(Jeu jeu) {
        LOGGER.info("Création d'un nouveau jeu");
        return jeuDao.creer(jeu);
    }

    @Override
    public Jeu modifierJeu(Jeu jeu) {
        LOGGER.info("Modification du jeu avec l'ID: {}", jeu.getId());
        return jeuDao.modifier(jeu);
    }

    @Override
    public void supprimerJeu(Long id) {
        LOGGER.info("Suppression du jeu avec l'ID: {}", id);
        jeuDao.supprimer(id);
    }

    @Override
    public Optional<Jeu> obtenirJeu(Long id) {
        LOGGER.info("Recherche du jeu avec l'ID: {}", id);
        return jeuDao.trouverParId(id);
    }

    @Override
    public List<Jeu> obtenirTousJeux() {
        LOGGER.info("Récupération de tous les jeux");
        return jeuDao.trouverTous();
    }
}

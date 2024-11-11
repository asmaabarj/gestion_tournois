package org.gestion_tournois.repositories.impl;

import org.gestion_tournois.repositories.interfaces.JeuDao;
import org.gestion_tournois.model.Jeu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class JeuDaoImpl implements JeuDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(JeuDaoImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Jeu creer(Jeu jeu) {
        entityManager.persist(jeu);
        LOGGER.info("Jeu créé avec l'ID: {}", jeu.getId());
        return jeu;
    }

    @Override
    public Jeu modifier(Jeu jeu) {
        Jeu jeuModifie = entityManager.merge(jeu);
        LOGGER.info("Jeu modifié avec l'ID: {}", jeuModifie.getId());
        return jeuModifie;
    }

    @Override
    public void supprimer(Long id) {
        Jeu jeu = entityManager.find(Jeu.class, id);
        if (jeu != null) {
            entityManager.remove(jeu);
            LOGGER.info("Jeu supprimé avec l'ID: {}", id);
        } else {
            LOGGER.warn("Tentative de suppression d'un jeu inexistant avec l'ID: {}", id);
        }
    }

    @Override
    public Optional<Jeu> trouverParId(Long id) {
        Jeu jeu = entityManager.find(Jeu.class, id);
        return Optional.ofNullable(jeu);
    }

    @Override
    public List<Jeu> trouverTous() {
        TypedQuery<Jeu> query = entityManager.createQuery("SELECT j FROM Jeu j", Jeu.class);
        return query.getResultList();
    }
}

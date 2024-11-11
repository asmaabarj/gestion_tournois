package org.gestion_tournois.repositories.impl;

import org.gestion_tournois.repositories.interfaces.JoueurDao;
import org.gestion_tournois.model.Joueur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class JoueurDaoImpl implements JoueurDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(JoueurDaoImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Joueur inscrire(Joueur joueur) {
        if (entityManager == null) {
            LOGGER.error("EntityManager is null");
            throw new IllegalStateException("EntityManager is not initialized");
        }
        entityManager.persist(joueur);
        LOGGER.info("Joueur inscrit avec l'ID: {}", joueur.getId());
        return joueur;
    }

    @Override
    public Joueur modifier(Joueur joueur) {
        Joueur joueurModifie = entityManager.merge(joueur);
        LOGGER.info("Joueur modifié avec l'ID: {}", joueurModifie.getId());
        return joueurModifie;
    }

    @Override
    public void supprimer(Long id) {
        Joueur joueur = entityManager.find(Joueur.class, id);
        if (joueur != null) {
            entityManager.remove(joueur);
            LOGGER.info("Joueur supprimé avec l'ID: {}", id);
        } else {
            LOGGER.warn("Tentative de suppression d'un joueur inexistant avec l'ID: {}", id);
        }
    }

    @Override
    public Optional<Joueur> trouverParId(Long id) {
        Joueur joueur = entityManager.find(Joueur.class, id);
        return Optional.ofNullable(joueur);
    }

    @Override
    public List<Joueur> trouverTous() {
        TypedQuery<Joueur> query = entityManager.createQuery("SELECT j FROM Joueur j", Joueur.class);
        return query.getResultList();
    }

    @Override
    public List<Joueur> trouverParEquipe(Long equipeId) {
        TypedQuery<Joueur> query = entityManager.createQuery(
                "SELECT j FROM Joueur j WHERE j.equipe.id = :equipeId", Joueur.class);
        query.setParameter("equipeId", equipeId);
        return query.getResultList();
    }

    @Override
    public boolean existeParPseudo(String pseudo) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(j) FROM Joueur j WHERE j.pseudo = :pseudo", Long.class);
        query.setParameter("pseudo", pseudo);
        return query.getSingleResult() > 0;
    }

    @Override
    public Optional<Joueur> trouverParPseudo(String pseudo) {
        TypedQuery<Joueur> query = entityManager.createQuery(
                "SELECT j FROM Joueur j WHERE j.pseudo = :pseudo", Joueur.class);
        query.setParameter("pseudo", pseudo);
        List<Joueur> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}

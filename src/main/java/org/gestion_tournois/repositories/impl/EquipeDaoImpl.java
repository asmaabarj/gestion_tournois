package org.gestion_tournois.repositories.impl;

import org.gestion_tournois.repositories.interfaces.EquipeDao;
import org.gestion_tournois.model.Equipe;
import org.gestion_tournois.model.Joueur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class EquipeDaoImpl implements EquipeDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(EquipeDaoImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Equipe creer(Equipe equipe) {
        entityManager.persist(equipe);
        LOGGER.info("Equipe créée avec l'ID: {}", equipe.getId());
        return equipe;
    }

    @Override
    public Equipe modifier(Equipe equipe) {
        Equipe equipeModifiee = entityManager.merge(equipe);
        LOGGER.info("Equipe modifiée avec l'ID: {}", equipeModifiee.getId());
        return equipeModifiee;
    }

    @Override
    public void supprimer(Long id) {
        Equipe equipe = entityManager.find(Equipe.class, id);
        if (equipe != null) {
            entityManager.remove(equipe);
            LOGGER.info("Equipe supprimée avec l'ID: {}", id);
        } else {
            LOGGER.warn("Tentative de suppression d'une équipe inexistante avec l'ID: {}", id);
        }
    }

    @Override
    public Optional<Equipe> trouverParId(Long id) {
        Equipe equipe = entityManager.find(Equipe.class, id);
        return Optional.ofNullable(equipe);
    }

    @Override
    public List<Equipe> trouverTous() {
        TypedQuery<Equipe> query = entityManager.createQuery("SELECT e FROM Equipe e", Equipe.class);
        return query.getResultList();
    }

    @Override
    public void ajouterJoueur(Long equipeId, Joueur joueur) {
        Equipe equipe = entityManager.find(Equipe.class, equipeId);
        if (equipe != null) {
            equipe.getJoueurs().add(joueur);
            joueur.setEquipe(equipe);
            entityManager.merge(equipe);
            LOGGER.info("Joueur ajouté à l'équipe avec l'ID: {}", equipeId);
        } else {
            LOGGER.warn("Tentative d'ajout d'un joueur à une équipe inexistante avec l'ID: {}", equipeId);
        }
    }

    @Override
    public void retirerJoueur(Long equipeId, Joueur joueur) {
        Equipe equipe = entityManager.find(Equipe.class, equipeId);
        if (equipe != null) {
            equipe.getJoueurs().remove(joueur);
            joueur.setEquipe(null);
            entityManager.merge(equipe);
            LOGGER.info("Joueur retiré de l'équipe avec l'ID: {}", equipeId);
        } else {
            LOGGER.warn("Tentative de retrait d'un joueur d'une équipe inexistante avec l'ID: {}", equipeId);
        }
    }

    @Override
    public List<Equipe> trouverParTournoi(Long tournoiId) {
        TypedQuery<Equipe> query = entityManager.createQuery(
                "SELECT e FROM Equipe e JOIN e.tournois t WHERE t.id = :tournoiId", Equipe.class);
        query.setParameter("tournoiId", tournoiId);
        return query.getResultList();
    }
}

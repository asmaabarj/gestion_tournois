package org.gestion_tournois.repositories.impl;

import org.gestion_tournois.repositories.interfaces.JeuDao;
import org.gestion_tournois.models.Jeu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

public class JeuDaoImpl implements JeuDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(JeuDaoImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Jeu create(Jeu jeu) {
        entityManager.persist(jeu);
        LOGGER.info("Jeu créé avec l'ID: {}", jeu.getId());
        return jeu;
    }

    @Override
    public Jeu update(Jeu jeu) {
        Jeu updatedJeu = entityManager.merge(jeu);
        LOGGER.info("Jeu mis à jour avec l'ID: {}", updatedJeu.getId());
        return updatedJeu;
    }

    @Override
    public void delete(Long id) {
        Jeu jeu = entityManager.find(Jeu.class, id);
        if (jeu != null) {
            entityManager.remove(jeu);
            LOGGER.info("Jeu supprimé avec l'ID: {}", id);
        } else {
            LOGGER.warn("Aucun jeu trouvé pour suppression avec l'ID: {}", id);
        }
    }

    @Override
    public Optional<Jeu> searchById(Long id) {
        Jeu jeu = entityManager.find(Jeu.class, id);
        LOGGER.info(jeu != null ? "Jeu trouvé avec l'ID: {}" : "Aucun jeu trouvé avec l'ID: {}", id);
        return Optional.ofNullable(jeu);
    }

    @Override
    public List<Jeu> getAll() {
        Query query = entityManager.createQuery("SELECT j FROM Jeu j");
        List<Jeu> jeux = query.getResultList();
        LOGGER.info("Nombre de jeux récupérés: {}", jeux.size());
        return jeux;
    }
}

package org.gestion_tournois.repositories.impl;

import org.gestion_tournois.repositories.interfaces.JoueurDao;
import org.gestion_tournois.models.Joueur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

public class JoueurDaoImpl implements JoueurDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(JoueurDaoImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Joueur create(Joueur joueur) {
        entityManager.persist(joueur);
        LOGGER.info("Création du joueur avec le pseudo: {} et l'ID: {}", joueur.getPseudo(), joueur.getId());
        return joueur;
    }

    @Override
    public Joueur update(Joueur joueur) {
        Joueur joueurModifie = entityManager.merge(joueur);
        LOGGER.info("Mise à jour du joueur avec l'ID: {} et le pseudo: {}", joueurModifie.getId(), joueurModifie.getPseudo());
        return joueurModifie;
    }

    @Override
    public void delete(Long id) {
        Joueur joueur = entityManager.find(Joueur.class, id);
        if (joueur != null) {
            entityManager.remove(joueur);
            LOGGER.info("Suppression du joueur avec l'ID: {}", id);
        } else {
            LOGGER.warn("Tentative de suppression d'un joueur inexistant avec l'ID: {}", id);
        }
    }

    @Override
    public Optional<Joueur> searchById(Long id) {
        Joueur joueur = entityManager.find(Joueur.class, id);
        if (joueur != null) {
            LOGGER.info("Joueur trouvé avec l'ID: {}", id);
        } else {
            LOGGER.warn("Aucun joueur trouvé avec l'ID: {}", id);
        }
        return Optional.ofNullable(joueur);
    }

    @Override
    public List<Joueur> getAll() {
        LOGGER.info("Récupération de tous les joueurs.");
        Query query = entityManager.createQuery("SELECT j FROM Joueur j");
        return query.getResultList();
    }

    @Override
    public List<Joueur> searchByEquipe(Long equipeId) {
        LOGGER.info("Recherche de joueurs pour l'équipe avec l'ID: {}", equipeId);
        Query query = entityManager.createQuery(
                "SELECT j FROM Joueur j WHERE j.equipe.id = :equipeId");
        query.setParameter("equipeId", equipeId);
        return query.getResultList();
    }

    @Override
    public boolean existeByPseudo(String pseudo) {
        Query query = entityManager.createQuery(
                "SELECT COUNT(j) FROM Joueur j WHERE j.pseudo = :pseudo");
        query.setParameter("pseudo", pseudo);
        Long count = (Long) query.getSingleResult();  // Cast to Long
        LOGGER.info("Vérification de l'existence du pseudo: {}. Existe-t-il? {}", pseudo, count > 0);
        return count > 0;
    }

    @Override
    public Optional<Joueur> searchByPseudo(String pseudo) {
        Query query = entityManager.createQuery(
                "SELECT j FROM Joueur j WHERE j.pseudo = :pseudo");
        query.setParameter("pseudo", pseudo);
        List<Joueur> results = query.getResultList();
        if (results.isEmpty()) {
            LOGGER.warn("Aucun joueur trouvé avec le pseudo: {}", pseudo);
            return Optional.empty();
        } else {
            LOGGER.info("Joueur trouvé avec le pseudo: {}", pseudo);
            return Optional.of(results.get(0));
        }
    }
}

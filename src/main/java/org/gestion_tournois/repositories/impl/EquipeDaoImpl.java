package org.gestion_tournois.repositories.impl;

import org.gestion_tournois.repositories.interfaces.EquipeDao;
import org.gestion_tournois.models.Equipe;
import org.gestion_tournois.models.Joueur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

public class EquipeDaoImpl implements EquipeDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(EquipeDaoImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Equipe create(Equipe equipe) {
        entityManager.persist(equipe);
        LOGGER.info("Création de l'équipe avec l'ID: {} et le nom: {}", equipe.getId(), equipe.getNom());
        return equipe;
    }

    @Override
    public Equipe update(Equipe equipe) {
        Equipe equipeModifiee = entityManager.merge(equipe);
        LOGGER.info("Mise à jour de l'équipe avec l'ID: {} et le nom: {}", equipeModifiee.getId(), equipeModifiee.getNom());
        return equipeModifiee;
    }

    @Override
    public void delete(Long id) {
        Equipe equipe = entityManager.find(Equipe.class, id);
        if (equipe != null) {
            entityManager.remove(equipe);
            LOGGER.info("Suppression de l'équipe avec l'ID: {}", id);
        } else {
            LOGGER.warn("Tentative de suppression d'une équipe inexistante avec l'ID: {}", id);
        }
    }

    @Override
    public Optional<Equipe> searchById(Long id) {
        Equipe equipe = entityManager.find(Equipe.class, id);
        if (equipe != null) {
            LOGGER.info("Équipe trouvée avec l'ID: {}", id);
        } else {
            LOGGER.warn("Aucune équipe trouvée avec l'ID: {}", id);
        }
        return Optional.ofNullable(equipe);
    }

    @Override
    public List<Equipe> getAll() {
        LOGGER.info("Récupération de toutes les équipes.");
        Query query = entityManager.createQuery("SELECT e FROM Equipe e");
        List<Equipe> equipes = query.getResultList();
        LOGGER.info("Liste de toutes les équipes récupérée avec succès. Nombre total d'équipes: {}", equipes.size());
        return equipes;
    }

    @Override
    public void addJoueur(Long equipeId, Joueur joueur) {
        Equipe equipe = entityManager.find(Equipe.class, equipeId);
        if (equipe != null) {
            equipe.getJoueurs().add(joueur);
            joueur.setEquipe(equipe);
            entityManager.merge(equipe);
            LOGGER.info("Le joueur avec le pseudo: {} a été ajouté à l'équipe avec l'ID: {}", joueur.getPseudo(), equipeId);
        } else {
            LOGGER.warn("Tentative d'ajout d'un joueur à une équipe inexistante avec l'ID: {}", equipeId);
        }
    }

    @Override
    public void deleteJoueur(Long equipeId, Joueur joueur) {
        Equipe equipe = entityManager.find(Equipe.class, equipeId);
        if (equipe != null) {
            equipe.getJoueurs().remove(joueur);
            joueur.setEquipe(null);
            entityManager.merge(equipe);
            LOGGER.info("Le joueur avec le pseudo: {} a été retiré de l'équipe avec l'ID: {}", joueur.getPseudo(), equipeId);
        } else {
            LOGGER.warn("Tentative de retrait d'un joueur d'une équipe inexistante avec l'ID: {}", equipeId);
        }
    }

    @Override
    public List<Equipe> searchByTournoi(Long tournoiId) {
        LOGGER.info("Recherche des équipes pour le tournoi avec l'ID: {}", tournoiId);
        Query query = entityManager.createQuery(
                "SELECT e FROM Equipe e JOIN e.tournois t WHERE t.id = :tournoiId");
        query.setParameter("tournoiId", tournoiId);
        List<Equipe> equipes = query.getResultList();
        LOGGER.info("Liste des équipes récupérée pour le tournoi ID: {}, Nombre d'équipes: {}", tournoiId, equipes.size());
        return equipes;
    }
}

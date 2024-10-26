package org.gestion_tournois.services;

import org.gestion_tournois.models.Equipe;
import org.gestion_tournois.models.Joueur;
import org.gestion_tournois.repositories.interfaces.EquipeDao;

import java.util.List;
import java.util.Optional;

public class EquipeService {

    private final EquipeDao equipeDao;

    public EquipeService(EquipeDao equipeDao) {
        this.equipeDao = equipeDao;
    }

    public Equipe create(Equipe equipe) {
        return equipeDao.create(equipe);
    }

    public Equipe update(Equipe equipe) {
        return equipeDao.update(equipe);
    }

    public void delete(Long id) {
        equipeDao.delete(id);
    }

    public Optional<Equipe> searchById(Long id) {
        return equipeDao.searchById(id);
    }

    public List<Equipe> getAll() {
        return equipeDao.getAll();
    }

    public void addJoueur(Long equipeId, Joueur joueur) {
        equipeDao.addJoueur(equipeId, joueur);
    }

    public void deleteJoueur(Long equipeId, Joueur joueur) {
        equipeDao.deleteJoueur(equipeId, joueur);
    }

    public List<Equipe> searchByTournoi(Long tournoiId) {
        return equipeDao.searchByTournoi(tournoiId);
    }
}

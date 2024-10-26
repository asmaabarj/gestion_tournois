package org.gestion_tournois.services;

import org.gestion_tournois.repositories.interfaces.JoueurDao;
import org.gestion_tournois.models.Joueur;

import java.util.List;
import java.util.Optional;

public class JoueurService {

    private final JoueurDao joueurDao;

    public JoueurService(JoueurDao joueurDao) {
        this.joueurDao = joueurDao;
    }

    public Joueur create(Joueur joueur) {
        return joueurDao.create(joueur);
    }

    public Joueur update(Joueur joueur) {
        return joueurDao.update(joueur);
    }

    public void delete(Long id) {
        joueurDao.delete(id);
    }

    public Optional<Joueur> searchById(Long id) {
        return joueurDao.searchById(id);
    }

    public List<Joueur> getAll() {
        return joueurDao.getAll();
    }

    public boolean existeByPseudo(String pseudo) {
        return joueurDao.existeByPseudo(pseudo);
    }

    public Optional<Joueur> searchByPseudo(String pseudo) {
        return joueurDao.searchByPseudo(pseudo);
    }

    public List<Joueur> searchByEquipe(Long equipeId) {
        return joueurDao.searchByEquipe(equipeId);
    }
}


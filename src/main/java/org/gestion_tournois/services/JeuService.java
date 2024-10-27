package org.gestion_tournois.services;

import org.gestion_tournois.models.Jeu;
import org.gestion_tournois.repositories.interfaces.JeuDao;

import java.util.List;
import java.util.Optional;

public class JeuService {

    private final JeuDao jeuDao;

    public JeuService(JeuDao jeuDao) {
        this.jeuDao = jeuDao;
    }

    public Jeu create(Jeu jeu) {
        return jeuDao.create(jeu);
    }

    public Jeu update(Jeu jeu) {
        return jeuDao.update(jeu);
    }

    public void delete(Long id) {
        jeuDao.delete(id);
    }

    public Optional<Jeu> searchById(Long id) {
        return jeuDao.searchById(id);
    }

    public List<Jeu> getAll() {
        return jeuDao.getAll();
    }
}

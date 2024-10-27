package org.gestion_tournois.services;

import org.gestion_tournois.models.Joueur;
import org.gestion_tournois.repositories.interfaces.JoueurDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JoueurServiceTest {

    @Mock
    private JoueurDao joueurDao;

    @InjectMocks
    private JoueurService joueurService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreate() {
        Joueur joueur = new Joueur("PseudoTest", 20);
        when(joueurDao.create(joueur)).thenReturn(joueur);

        Joueur createdJoueur = joueurService.create(joueur);
        assertEquals(joueur, createdJoueur);
        verify(joueurDao, times(1)).create(joueur);
    }

    @Test
    public void testUpdate() {
        Joueur joueur = new Joueur("PseudoUpdate", 25);
        when(joueurDao.update(joueur)).thenReturn(joueur);

        Joueur updatedJoueur = joueurService.update(joueur);
        assertEquals(joueur, updatedJoueur);
        verify(joueurDao, times(1)).update(joueur);
    }

    @Test
    public void testDelete() {
        Long joueurId = 1L;
        doNothing().when(joueurDao).delete(joueurId);

        joueurService.delete(joueurId);
        verify(joueurDao, times(1)).delete(joueurId);
    }

    @Test
    public void testSearchByIdFound() {
        Long joueurId = 1L;
        Joueur joueur = new Joueur("PseudoRecherche", 18);
        when(joueurDao.searchById(joueurId)).thenReturn(Optional.of(joueur));

        Optional<Joueur> foundJoueur = joueurService.searchById(joueurId);
        assertTrue(foundJoueur.isPresent());
        assertEquals(joueur, foundJoueur.get());
        verify(joueurDao, times(1)).searchById(joueurId);
    }

    @Test
    public void testSearchByIdNotFound() {
        Long joueurId = 1L;
        when(joueurDao.searchById(joueurId)).thenReturn(Optional.empty());

        Optional<Joueur> foundJoueur = joueurService.searchById(joueurId);
        assertFalse(foundJoueur.isPresent());
        verify(joueurDao, times(1)).searchById(joueurId);
    }

    @Test
    public void testGetAll() {
        Joueur joueur1 = new Joueur("Pseudo1", 21);
        Joueur joueur2 = new Joueur("Pseudo2", 22);
        List<Joueur> joueurs = Arrays.asList(joueur1, joueur2);
        when(joueurDao.getAll()).thenReturn(joueurs);

        List<Joueur> allJoueurs = joueurService.getAll();
        assertEquals(2, allJoueurs.size());
        assertEquals(joueur1, allJoueurs.get(0));
        assertEquals(joueur2, allJoueurs.get(1));
        verify(joueurDao, times(1)).getAll();
    }

    @Test
    public void testExisteByPseudoTrue() {
        String pseudo = "ExistingPseudo";
        when(joueurDao.existeByPseudo(pseudo)).thenReturn(true);

        boolean exists = joueurService.existeByPseudo(pseudo);
        assertTrue(exists);
        verify(joueurDao, times(1)).existeByPseudo(pseudo);
    }

    @Test
    public void testExisteByPseudoFalse() {
        String pseudo = "NonExistingPseudo";
        when(joueurDao.existeByPseudo(pseudo)).thenReturn(false);

        boolean exists = joueurService.existeByPseudo(pseudo);
        assertFalse(exists);
        verify(joueurDao, times(1)).existeByPseudo(pseudo);
    }

    @Test
    public void testSearchByPseudoFound() {
        String pseudo = "FoundPseudo";
        Joueur joueur = new Joueur(pseudo, 19);
        when(joueurDao.searchByPseudo(pseudo)).thenReturn(Optional.of(joueur));

        Optional<Joueur> foundJoueur = joueurService.searchByPseudo(pseudo);
        assertTrue(foundJoueur.isPresent());
        assertEquals(joueur, foundJoueur.get());
        verify(joueurDao, times(1)).searchByPseudo(pseudo);
    }

    @Test
    public void testSearchByPseudoNotFound() {
        String pseudo = "NotFoundPseudo";
        when(joueurDao.searchByPseudo(pseudo)).thenReturn(Optional.empty());

        Optional<Joueur> foundJoueur = joueurService.searchByPseudo(pseudo);
        assertFalse(foundJoueur.isPresent());
        verify(joueurDao, times(1)).searchByPseudo(pseudo);
    }

    @Test
    public void testSearchByEquipe() {
        Long equipeId = 1L;
        Joueur joueur1 = new Joueur("EquipeJoueur1", 23);
        Joueur joueur2 = new Joueur("EquipeJoueur2", 24);
        List<Joueur> joueurs = Arrays.asList(joueur1, joueur2);
        when(joueurDao.searchByEquipe(equipeId)).thenReturn(joueurs);

        List<Joueur> joueursByEquipe = joueurService.searchByEquipe(equipeId);
        assertEquals(2, joueursByEquipe.size());
        assertEquals(joueur1, joueursByEquipe.get(0));
        assertEquals(joueur2, joueursByEquipe.get(1));
        verify(joueurDao, times(1)).searchByEquipe(equipeId);
    }
}

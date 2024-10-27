package org.gestion_tournois.services;

import org.gestion_tournois.models.Equipe;
import org.gestion_tournois.models.Joueur;
import org.gestion_tournois.repositories.interfaces.EquipeDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EquipeServiceTest {

    @Mock
    private EquipeDao equipeDao;

    @InjectMocks
    private EquipeService equipeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEquipe() {
        Equipe equipe = new Equipe("Equipe Test");
        when(equipeDao.create(equipe)).thenReturn(equipe);

        Equipe createdEquipe = equipeService.create(equipe);

        assertNotNull(createdEquipe);
        assertEquals("Equipe Test", createdEquipe.getNom());
        verify(equipeDao, times(1)).create(equipe);
    }

    @Test
    void testUpdateEquipe() {
        Equipe equipe = new Equipe("Equipe Update");
        equipe.setId(1L);
        when(equipeDao.update(equipe)).thenReturn(equipe);

        Equipe updatedEquipe = equipeService.update(equipe);

        assertNotNull(updatedEquipe);
        assertEquals("Equipe Update", updatedEquipe.getNom());
        verify(equipeDao, times(1)).update(equipe);
    }

    @Test
    void testDeleteEquipe() {
        Long equipeId = 1L;
        doNothing().when(equipeDao).delete(equipeId);

        equipeService.delete(equipeId);

        verify(equipeDao, times(1)).delete(equipeId);
    }

    @Test
    void testSearchByIdEquipeFound() {
        Equipe equipe = new Equipe("Equipe Search");
        equipe.setId(1L);
        when(equipeDao.searchById(1L)).thenReturn(Optional.of(equipe));

        Optional<Equipe> foundEquipe = equipeService.searchById(1L);

        assertTrue(foundEquipe.isPresent());
        assertEquals("Equipe Search", foundEquipe.get().getNom());
        verify(equipeDao, times(1)).searchById(1L);
    }

    @Test
    void testSearchByIdEquipeNotFound() {
        when(equipeDao.searchById(1L)).thenReturn(Optional.empty());

        Optional<Equipe> foundEquipe = equipeService.searchById(1L);

        assertFalse(foundEquipe.isPresent());
        verify(equipeDao, times(1)).searchById(1L);
    }

    @Test
    void testGetAllEquipes() {
        List<Equipe> equipeList = new ArrayList<>();
        equipeList.add(new Equipe("Equipe 1"));
        equipeList.add(new Equipe("Equipe 2"));
        when(equipeDao.getAll()).thenReturn(equipeList);

        List<Equipe> equipes = equipeService.getAll();

        assertNotNull(equipes);
        assertEquals(2, equipes.size());
        verify(equipeDao, times(1)).getAll();
    }

    @Test
    void testAddJoueurToEquipe() {
        Long equipeId = 1L;
        Joueur joueur = new Joueur();
        joueur.setPseudo("Joueur Test");

        doNothing().when(equipeDao).addJoueur(equipeId, joueur);

        equipeService.addJoueur(equipeId, joueur);

        verify(equipeDao, times(1)).addJoueur(equipeId, joueur);
    }

    @Test
    void testDeleteJoueurFromEquipe() {
        Long equipeId = 1L;
        Joueur joueur = new Joueur();
        joueur.setPseudo("Joueur Test");

        doNothing().when(equipeDao).deleteJoueur(equipeId, joueur);

        equipeService.deleteJoueur(equipeId, joueur);

        verify(equipeDao, times(1)).deleteJoueur(equipeId, joueur);
    }

    @Test
    void testSearchByTournoi() {
        Long tournoiId = 1L;
        List<Equipe> equipeList = new ArrayList<>();
        equipeList.add(new Equipe("Equipe 1"));
        when(equipeDao.searchByTournoi(tournoiId)).thenReturn(equipeList);

        List<Equipe> equipes = equipeService.searchByTournoi(tournoiId);

        assertNotNull(equipes);
        assertEquals(1, equipes.size());
        verify(equipeDao, times(1)).searchByTournoi(tournoiId);
    }
}

package org.gestion_tournois.service;

import org.gestion_tournois.model.Equipe;
import org.gestion_tournois.model.Joueur;
import org.gestion_tournois.repositories.interfaces.EquipeDao;
import org.gestion_tournois.repositories.interfaces.JoueurDao;
import org.gestion_tournois.service.impl.EquipeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class EquipeServiceTest {

    @Mock
    private EquipeDao equipeDao;

    @Mock
    private JoueurDao joueurDao;

    private EquipeServiceImpl equipeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        equipeService = new EquipeServiceImpl(equipeDao, joueurDao);
    }

    @Test
    void testCreerEquipe() {
        // Arrange
        Equipe equipe = new Equipe();
        equipe.setNom("Test Team");
        when(equipeDao.creer(any(Equipe.class))).thenReturn(equipe);

        Equipe result = equipeService.creerEquipe(equipe);

        assertNotNull(result);
        assertEquals("Test Team", result.getNom());
        verify(equipeDao).creer(equipe);
    }

    @Test
    void testModifierEquipe() {
        // Arrange
        Equipe equipe = new Equipe();
        equipe.setId(1L);
        equipe.setNom("Updated Team");
        when(equipeDao.modifier(any(Equipe.class))).thenReturn(equipe);

        // Act
        Equipe result = equipeService.modifierEquipe(equipe);

        assertNotNull(result);
        assertEquals("Updated Team", result.getNom());
        verify(equipeDao).modifier(equipe);
    }

    @Test
    void testAjouterJoueur() {
        Long equipeId = 1L;
        Long joueurId = 1L;
        Equipe equipe = new Equipe();
        Joueur joueur = new Joueur();
        when(equipeDao.trouverParId(equipeId)).thenReturn(Optional.of(equipe));
        when(joueurDao.trouverParId(joueurId)).thenReturn(Optional.of(joueur));
        when(equipeDao.modifier(any(Equipe.class))).thenReturn(equipe);

        // Act
        equipeService.ajouterJoueur(equipeId, joueurId);

        // Assert
        verify(equipeDao).modifier(equipe);
        assertEquals(equipe, joueur.getEquipe());
    }

    @Test
    void testAjouterJoueur_EquipeNonTrouvee() {
        Long equipeId = 1L;
        Long joueurId = 1L;
        when(equipeDao.trouverParId(equipeId)).thenReturn(Optional.empty());

        equipeService.ajouterJoueur(equipeId, joueurId);

        verify(equipeDao, never()).modifier(any(Equipe.class));
    }

    @Test
    void testRetirerJoueur() {
        Long equipeId = 1L;
        Long joueurId = 1L;
        Joueur joueur = new Joueur();
        when(joueurDao.trouverParId(joueurId)).thenReturn(Optional.of(joueur));

        equipeService.retirerJoueur(equipeId, joueurId);

        verify(equipeDao).retirerJoueur(equipeId, joueur);
    }

    @Test
    void testObtenirEquipesParTournoi() {
        Long tournoiId = 1L;
        List<Equipe> equipes = Arrays.asList(new Equipe(), new Equipe());
        when(equipeDao.trouverParTournoi(tournoiId)).thenReturn(equipes);

        List<Equipe> result = equipeService.obtenirEquipesParTournoi(tournoiId);

        assertEquals(2, result.size());
        verify(equipeDao).trouverParTournoi(tournoiId);
    }
}

package org.gestion_tournois.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.gestion_tournois.model.Joueur;
import org.gestion_tournois.repositories.interfaces.JoueurDao;
import org.gestion_tournois.service.impl.JoueurServiceImpl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class JoueurServiceTest {

    @Mock
    private JoueurDao joueurDao;

    private JoueurServiceImpl joueurService;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        joueurService = new JoueurServiceImpl(joueurDao);
    }

    @Test
    void testInscrireJoueur() {
        // Arrange
        Joueur joueur = new Joueur();
        joueur.setPseudo("TestPlayer");
        joueur.setAge(20);
        when(joueurDao.inscrire(any(Joueur.class))).thenReturn(joueur);

        // Act
        Joueur result = joueurService.inscrireJoueur(joueur);

        // Assert
        assertNotNull(result);
        assertEquals("TestPlayer", result.getPseudo());
        assertEquals(20, result.getAge());
        verify(joueurDao).inscrire(joueur);
    }

    @Test
    void testModifierJoueur() {
        // Arrange
        Joueur joueur = new Joueur();
        joueur.setId(1L);
        joueur.setPseudo("UpdatedPlayer");
        joueur.setAge(21);
        when(joueurDao.modifier(any(Joueur.class))).thenReturn(joueur);

        // Act
        Joueur result = joueurService.modifierJoueur(joueur);

        // Assert
        assertNotNull(result);
        assertEquals("UpdatedPlayer", result.getPseudo());
        assertEquals(21, result.getAge());
        verify(joueurDao).modifier(joueur);
    }

    @Test
    void testObtenirJoueur() {
        // Arrange
        Long id = 1L;
        Joueur joueur = new Joueur();
        joueur.setId(id);
        joueur.setPseudo("TestPlayer");
        when(joueurDao.trouverParId(id)).thenReturn(Optional.of(joueur));

        // Act
        Optional<Joueur> result = joueurService.obtenirJoueur(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        assertEquals("TestPlayer", result.get().getPseudo());
        verify(joueurDao).trouverParId(id);
    }

    @Test
    void testObtenirJoueurNonExistant() {
        // Arrange
        Long id = 999L;
        when(joueurDao.trouverParId(id)).thenReturn(Optional.empty());

        // Act
        Optional<Joueur> result = joueurService.obtenirJoueur(id);

        // Assert
        assertFalse(result.isPresent());
        verify(joueurDao).trouverParId(id);
    }

    @Test
    void testObtenirTousJoueurs() {
        // Arrange
        List<Joueur> joueurs = Arrays.asList(
            new Joueur("Player1", 20),
            new Joueur("Player2", 22)
        );
        when(joueurDao.trouverTous()).thenReturn(joueurs);

        // Act
        List<Joueur> result = joueurService.obtenirTousJoueurs();

        // Assert
        assertEquals(2, result.size());
        verify(joueurDao).trouverTous();
    }

    @Test
    void testObtenirJoueursParEquipe() {
        // Arrange
        Long equipeId = 1L;
        List<Joueur> joueurs = Arrays.asList(
            new Joueur("Player1", 20),
            new Joueur("Player2", 22)
        );
        when(joueurDao.trouverParEquipe(equipeId)).thenReturn(joueurs);

        // Act
        List<Joueur> result = joueurService.obtenirJoueursParEquipe(equipeId);

        // Assert
        assertEquals(2, result.size());
        verify(joueurDao).trouverParEquipe(equipeId);
    }

    @Test
    void testExisteParPseudo() {
        // Arrange
        String pseudo = "TestPlayer";
        when(joueurDao.existeParPseudo(pseudo)).thenReturn(true);

        // Act
        boolean result = joueurService.existeParPseudo(pseudo);

        // Assert
        assertTrue(result);
        verify(joueurDao).existeParPseudo(pseudo);
    }

    @Test
    void testTrouverParPseudo() {
        // Arrange
        String pseudo = "TestPlayer";
        Joueur joueur = new Joueur(pseudo, 20);
        when(joueurDao.trouverParPseudo(pseudo)).thenReturn(Optional.of(joueur));

        // Act
        Optional<Joueur> result = joueurService.trouverParPseudo(pseudo);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(pseudo, result.get().getPseudo());
        verify(joueurDao).trouverParPseudo(pseudo);
    }

    @Test
    void testSupprimerJoueur() {
        // Arrange
        Long id = 1L;

        // Act
        joueurService.supprimerJoueur(id);

        // Assert
        verify(joueurDao).supprimer(id);
    }

    @Test
    void testTrouverParPseudoNonExistant() {
        // Arrange
        String pseudo = "NonExistantPlayer";
        when(joueurDao.trouverParPseudo(pseudo)).thenReturn(Optional.empty());

        // Act
        Optional<Joueur> result = joueurService.trouverParPseudo(pseudo);

        // Assert
        assertFalse(result.isPresent());
        verify(joueurDao).trouverParPseudo(pseudo);
    }
}

package org.gestion_tournois.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.gestion_tournois.model.Jeu;
import org.gestion_tournois.repositories.interfaces.JeuDao;
import org.gestion_tournois.service.impl.JeuServiceImpl;
import org.junit.jupiter.api.AfterEach;
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

public class JeuServiceTest {

    @Mock
    private JeuDao jeuDao;

    private JeuServiceImpl jeuService;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        jeuService = new JeuServiceImpl(jeuDao);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testCreerJeu() {
        // Arrange
        Jeu jeu = new Jeu();
        jeu.setNom("League of Legends");
        jeu.setDifficulte(8);
        jeu.setDureeMoyenneMatch(45);
        when(jeuDao.creer(any(Jeu.class))).thenReturn(jeu);

        // Act
        Jeu result = jeuService.creerJeu(jeu);

        // Assert
        assertNotNull(result);
        assertEquals("League of Legends", result.getNom());
        assertEquals(8, result.getDifficulte());
        assertEquals(45, result.getDureeMoyenneMatch());
        verify(jeuDao).creer(jeu);
    }

    @Test
    void testModifierJeu() {
        // Arrange
        Jeu jeu = new Jeu();
        jeu.setId(1L);
        jeu.setNom("Dota 2");
        jeu.setDifficulte(9);
        jeu.setDureeMoyenneMatch(50);
        when(jeuDao.modifier(any(Jeu.class))).thenReturn(jeu);

        // Act
        Jeu result = jeuService.modifierJeu(jeu);

        // Assert
        assertNotNull(result);
        assertEquals("Dota 2", result.getNom());
        assertEquals(9, result.getDifficulte());
        assertEquals(50, result.getDureeMoyenneMatch());
        verify(jeuDao).modifier(jeu);
    }

    @Test
    void testObtenirJeu() {
        // Arrange
        Long id = 1L;
        Jeu jeu = new Jeu();
        jeu.setId(id);
        jeu.setNom("Counter-Strike");
        when(jeuDao.trouverParId(id)).thenReturn(Optional.of(jeu));

        // Act
        Optional<Jeu> result = jeuService.obtenirJeu(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        assertEquals("Counter-Strike", result.get().getNom());
        verify(jeuDao).trouverParId(id);
    }

    @Test
    void testObtenirJeuNonExistant() {
        // Arrange
        Long id = 999L;
        when(jeuDao.trouverParId(id)).thenReturn(Optional.empty());

        // Act
        Optional<Jeu> result = jeuService.obtenirJeu(id);

        // Assert
        assertFalse(result.isPresent());
        verify(jeuDao).trouverParId(id);
    }

    @Test
    void testObtenirTousJeux() {
        // Arrange
        List<Jeu> jeux = Arrays.asList(
            new Jeu("Valorant", 7, 40),
            new Jeu("Rocket League", 6, 7)
        );
        when(jeuDao.trouverTous()).thenReturn(jeux);

        // Act
        List<Jeu> result = jeuService.obtenirTousJeux();

        // Assert
        assertEquals(2, result.size());
        verify(jeuDao).trouverTous();
    }

    @Test
    void testSupprimerJeu() {
        // Arrange
        Long id = 1L;

        // Act
        jeuService.supprimerJeu(id);

        // Assert
        verify(jeuDao).supprimer(id);
    }

    @Test
    void testCreerJeuAvecValeursLimites() {
        // Arrange
        Jeu jeu = new Jeu();
        // Création d'une chaîne de 100 caractères pour le nom
        StringBuilder nomBuilder = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            nomBuilder.append("A");
        }
        jeu.setNom(nomBuilder.toString());
        jeu.setDifficulte(10); // Test avec la difficulté maximale
        jeu.setDureeMoyenneMatch(1); // Test avec la durée minimale
        when(jeuDao.creer(any(Jeu.class))).thenReturn(jeu);

        // Act
        Jeu result = jeuService.creerJeu(jeu);

        // Assert
        assertNotNull(result);
        assertEquals(100, result.getNom().length());
        assertEquals(10, result.getDifficulte());
        assertEquals(1, result.getDureeMoyenneMatch());
        verify(jeuDao).creer(jeu);
    }

    @Test
    void testModifierJeuAvecValeursLimites() {
        // Arrange
        Jeu jeu = new Jeu();
        jeu.setId(1L);
        jeu.setNom("AB"); // Test avec la longueur minimale (2 caractères)
        jeu.setDifficulte(1); // Test avec la difficulté minimale
        jeu.setDureeMoyenneMatch(Integer.MAX_VALUE); // Test avec une durée très longue
        when(jeuDao.modifier(any(Jeu.class))).thenReturn(jeu);

        // Act
        Jeu result = jeuService.modifierJeu(jeu);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getNom().length());
        assertEquals(1, result.getDifficulte());
        assertEquals(Integer.MAX_VALUE, result.getDureeMoyenneMatch());
        verify(jeuDao).modifier(jeu);
    }
}

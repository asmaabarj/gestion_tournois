package org.gestion_tournois.service;

import org.gestion_tournois.model.Tournoi;
import org.gestion_tournois.model.Equipe;
import org.gestion_tournois.model.Jeu;
import org.gestion_tournois.model.enums.TournoiStatus;
import org.gestion_tournois.repositories.interfaces.TournoiDao;
import org.gestion_tournois.repositories.interfaces.EquipeDao;
import org.gestion_tournois.service.impl.TournoiServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TournoiServiceTest {

    @Mock
    private TournoiDao tournoiDao;

    @Mock
    private EquipeDao equipeDao;

    private TournoiServiceImpl tournoiService;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        tournoiService = new TournoiServiceImpl(tournoiDao, equipeDao);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testCreerTournoi() {
        // Arrange
        Tournoi tournoi = new Tournoi();
        tournoi.setTitre("Tournoi Test");
        tournoi.setDateDebut(LocalDate.now());
        tournoi.setDateFin(LocalDate.now().plusDays(2));
        tournoi.setNombreSpectateurs(1000);
        when(tournoiDao.creer(any(Tournoi.class))).thenReturn(tournoi);

        // Act
        Tournoi result = tournoiService.creerTournoi(tournoi);

        // Assert
        assertNotNull(result);
        assertEquals("Tournoi Test", result.getTitre());
        verify(tournoiDao).creer(tournoi);
    }

    @Test
    void testModifierTournoi() {
        // Arrange
        Tournoi tournoi = new Tournoi();
        tournoi.setId(1L);
        tournoi.setTitre("Tournoi Modifié");
        when(tournoiDao.modifier(any(Tournoi.class))).thenReturn(tournoi);

        // Act
        Tournoi result = tournoiService.modifierTournoi(tournoi);

        // Assert
        assertNotNull(result);
        assertEquals("Tournoi Modifié", result.getTitre());
        verify(tournoiDao).modifier(tournoi);
    }

    @Test
    void testObtenirTournoi() {
        // Arrange
        Long id = 1L;
        Tournoi tournoi = new Tournoi();
        tournoi.setId(id);
        when(tournoiDao.trouverParIdAvecEquipes(id)).thenReturn(Optional.of(tournoi));

        // Act
        Optional<Tournoi> result = tournoiService.obtenirTournoi(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(tournoiDao).trouverParIdAvecEquipes(id);
    }

    @Test
    void testObtenirTournoiNonExistant() {
        // Arrange
        Long id = 999L;
        when(tournoiDao.trouverParIdAvecEquipes(id)).thenReturn(Optional.empty());

        // Act
        Optional<Tournoi> result = tournoiService.obtenirTournoi(id);

        // Assert
        assertFalse(result.isPresent());
        verify(tournoiDao).trouverParIdAvecEquipes(id);
    }

    @Test
    void testObtenirTousTournois() {
        // Arrange
        List<Tournoi> tournois = Arrays.asList(
            new Tournoi(),
            new Tournoi()
        );
        when(tournoiDao.trouverTous()).thenReturn(tournois);

        // Act
        List<Tournoi> result = tournoiService.obtenirTousTournois();

        // Assert
        assertEquals(2, result.size());
        verify(tournoiDao).trouverTous();
    }

    @Test
    void testAjouterEquipe() {
        // Arrange
        Long tournoiId = 1L;
        Long equipeId = 1L;
        Equipe equipe = new Equipe();
        equipe.setId(equipeId);
        when(equipeDao.trouverParId(equipeId)).thenReturn(Optional.of(equipe));

        // Act
        tournoiService.ajouterEquipe(tournoiId, equipeId);

        // Assert
        verify(equipeDao).trouverParId(equipeId);
        verify(tournoiDao).ajouterEquipe(tournoiId, equipe);
    }

    @Test
    void testRetirerEquipe() {
        // Arrange
        Long tournoiId = 1L;
        Long equipeId = 1L;
        Equipe equipe = new Equipe();
        equipe.setId(equipeId);
        when(equipeDao.trouverParId(equipeId)).thenReturn(Optional.of(equipe));

        // Act
        tournoiService.retirerEquipe(tournoiId, equipeId);

        // Assert
        verify(equipeDao).trouverParId(equipeId);
        verify(tournoiDao).retirerEquipe(tournoiId, equipe);
    }

    @Test
    void testCalculerDureeEstimeeTournoi() {
        // Arrange
        Long tournoiId = 1L;
        int dureeEstimee = 480; // 8 heures en minutes
        when(tournoiDao.calculerdureeEstimeeTournoi(tournoiId)).thenReturn(dureeEstimee);

        // Act
        int result = tournoiService.calculerdureeEstimeeTournoi(tournoiId);

        // Assert
        assertEquals(dureeEstimee, result);
        verify(tournoiDao).calculerdureeEstimeeTournoi(tournoiId);
    }

    @Test
    void testModifierStatutTournoi() {
        // Arrange
        Long tournoiId = 1L;
        TournoiStatus nouveauStatut = TournoiStatus.EN_COURS;

        // Act
        tournoiService.modifierStatutTournoi(tournoiId, nouveauStatut);

        // Assert
        verify(tournoiDao).modifierStatut(tournoiId, nouveauStatut);
    }

    @Test
    void testAjouterEquipeAvecEquipeInexistante() {
        // Arrange
        Long tournoiId = 1L;
        Long equipeId = 999L;
        when(equipeDao.trouverParId(equipeId)).thenReturn(Optional.empty());

        // Act
        tournoiService.ajouterEquipe(tournoiId, equipeId);

        // Assert
        verify(equipeDao).trouverParId(equipeId);
        verify(tournoiDao, never()).ajouterEquipe(anyLong(), any(Equipe.class));
    }

    @Test
    void testRetirerEquipeAvecEquipeInexistante() {
        // Arrange
        Long tournoiId = 1L;
        Long equipeId = 999L;
        when(equipeDao.trouverParId(equipeId)).thenReturn(Optional.empty());

        // Act
        tournoiService.retirerEquipe(tournoiId, equipeId);

        // Assert
        verify(equipeDao).trouverParId(equipeId);
        verify(tournoiDao, never()).retirerEquipe(anyLong(), any(Equipe.class));
    }
}

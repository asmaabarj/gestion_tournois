package org.gestion_tournois.integration;

import java.lang.reflect.Field;
import java.time.LocalDate;

import javax.persistence.EntityManager;

import org.gestion_tournois.model.Equipe;
import org.gestion_tournois.model.Tournoi;
import org.gestion_tournois.repositories.impl.EquipeDaoImpl;
import org.gestion_tournois.repositories.impl.TournoiDaoImpl;
import org.gestion_tournois.service.impl.TournoiServiceImpl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class TournoiServiceIntegrationTest {

    @Mock
    private EntityManager entityManager;

    private TournoiDaoImpl tournoiDao;
    private EquipeDaoImpl equipeDao;
    private TournoiServiceImpl tournoiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        tournoiDao = new TournoiDaoImpl();
        equipeDao = new EquipeDaoImpl();
        
        try {
            Field tournoiDaoField = TournoiDaoImpl.class.getDeclaredField("entityManager");
            tournoiDaoField.setAccessible(true);
            tournoiDaoField.set(tournoiDao, entityManager);

            Field equipeDaoField = EquipeDaoImpl.class.getDeclaredField("entityManager");
            equipeDaoField.setAccessible(true);
            equipeDaoField.set(equipeDao, entityManager);
        } catch (Exception e) {
            fail("Erreur lors de l'injection de l'EntityManager: " + e.getMessage());
        }

        tournoiService = new TournoiServiceImpl(tournoiDao, equipeDao);
    }

    @Test
    void testCreerTournoiEtAjouterEquipe() {
        Tournoi tournoi = new Tournoi();
        tournoi.setId(1L);
        tournoi.setTitre("Tournoi Test");
        tournoi.setDateDebut(LocalDate.now().plusDays(1));
        tournoi.setDateFin(LocalDate.now().plusDays(2));
        tournoi.setNombreSpectateurs(100);

        Equipe equipe = new Equipe();
        equipe.setId(1L);
        equipe.setNom("Équipe Test");

        when(entityManager.merge(any(Tournoi.class))).thenReturn(tournoi);
        when(entityManager.find(Tournoi.class, 1L)).thenReturn(tournoi);
        when(entityManager.find(Equipe.class, 1L)).thenReturn(equipe);

        when(entityManager.merge(tournoi)).thenReturn(tournoi);
        Tournoi tournoiCree = tournoiService.creerTournoi(tournoi);
        assertNotNull(tournoiCree);
        assertEquals("Tournoi Test", tournoiCree.getTitre());

        tournoiService.ajouterEquipe(tournoiCree.getId(), equipe.getId());

        verify(entityManager, times(1)).merge(tournoi);
        verify(entityManager, times(1)).find(eq(Tournoi.class), eq(1L));
        verify(entityManager, times(1)).find(eq(Equipe.class), eq(1L));
    }
}

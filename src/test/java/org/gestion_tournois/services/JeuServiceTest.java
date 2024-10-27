package org.gestion_tournois.services;

import org.gestion_tournois.models.Jeu;
import org.gestion_tournois.repositories.interfaces.JeuDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class JeuServiceTest {
    
    @Mock
    private JeuDao jeuDao;
    
    private JeuService jeuService;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        jeuService = new JeuService(jeuDao);
    }
    
    @Test
    public void testCreerJeu_Success() {
        // Arrange
        String nom = "League of Legends";
        int minJoueurs = 5;
        int maxJoueurs = 5;
        Jeu jeu = new Jeu(nom, minJoueurs, maxJoueurs);
        when(jeuDao.save(any(Jeu.class))).thenReturn(jeu);
        
        // Act
        Jeu result = jeuService.creerJeu(nom, minJoueurs, maxJoueurs);
        
        // Assert
        assertNotNull(result);
        assertEquals(nom, result.getNom());
        assertEquals(minJoueurs, result.getMinJoueurs());
        assertEquals(maxJoueurs, result.getMaxJoueurs());
        verify(jeuDao).save(any(Jeu.class));
    }
    
    @Test
    public void testModifierJeu_Success() {
        // Arrange
        Long id = 1L;
        String nouveauNom = "DOTA 2";
        int minJoueurs = 5;
        int maxJoueurs = 5;
        Jeu jeuExistant = new Jeu("League of Legends", 5, 5);
        jeuExistant.setId(id);
        
        when(jeuDao.findById(id)).thenReturn(Optional.of(jeuExistant));
        when(jeuDao.save(any(Jeu.class))).thenReturn(jeuExistant);
        
        // Act
        Jeu result = jeuService.modifierJeu(id, nouveauNom, minJoueurs, maxJoueurs);
        
        // Assert
        assertNotNull(result);
        assertEquals(nouveauNom, result.getNom());
        verify(jeuDao).findById(id);
        verify(jeuDao).save(any(Jeu.class));
    }
    
    @Test
    public void testSupprimerJeu_Success() {
        // Arrange
        Long id = 1L;
        Jeu jeu = new Jeu("League of Legends", 5, 5);
        jeu.setId(id);
        when(jeuDao.findById(id)).thenReturn(Optional.of(jeu));
        doNothing().when(jeuDao).delete(jeu);
        
        // Act
        boolean result = jeuService.supprimerJeu(id);
        
        // Assert
        assertTrue(result);
        verify(jeuDao).findById(id);
        verify(jeuDao).delete(jeu);
    }
    
    @Test
    public void testObtenirJeu_Success() {
        // Arrange
        Long id = 1L;
        Jeu jeu = new Jeu("League of Legends", 5, 5);
        jeu.setId(id);
        when(jeuDao.findById(id)).thenReturn(Optional.of(jeu));
        
        // Act
        Optional<Jeu> result = jeuService.obtenirJeu(id);
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals(jeu.getNom(), result.get().getNom());
        verify(jeuDao).findById(id);
    }
    
    @Test
    public void testListerJeux_Success() {
        // Arrange
        List<Jeu> jeux = Arrays.asList(
            new Jeu("League of Legends", 5, 5),
            new Jeu("DOTA 2", 5, 5)
        );
        when(jeuDao.findAll()).thenReturn(jeux);
        
        // Act
        List<Jeu> result = jeuService.listerJeux();
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(jeuDao).findAll();
    }
}

package org.gestion_tournois.controllers;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.gestion_tournois.models.Jeu;
import org.gestion_tournois.models.Tournoi;
import org.gestion_tournois.models.enums.Status;
import org.gestion_tournois.services.JeuService;
import org.gestion_tournois.services.TournoiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TournoiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TournoiController.class);
    private final TournoiService tournoiService;
    private final JeuService jeuService;

    public TournoiController(TournoiService tournoiService, JeuService jeuService) {
        this.tournoiService = tournoiService;
        this.jeuService = jeuService;
    }

    public Tournoi create(String titre, Long jeuId, LocalDate dateDebut, LocalDate dateFin,
                                int nombreSpectateurs, int dureeMoyenneMatch, int tempsCeremonie, int tempsPauseEntreMatchs) {
        LOGGER.info("Tentative de création d'un nouveau tournoi: {}", titre);

        if (dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("La date de début ne peut pas être après la date de fin.");
        }

        Jeu jeu = jeuService.searchById(jeuId)
                .orElseThrow(() -> new IllegalArgumentException("Jeu non trouvé pour l'ID: " + jeuId));

        Tournoi tournoi = new Tournoi(titre, jeu, dateDebut, dateFin, dureeMoyenneMatch);
        tournoi.setNombreSpectateurs(nombreSpectateurs);
        tournoi.setTempsCeremonie(tempsCeremonie);
        tournoi.setTempsPauseEntreMatchs(tempsPauseEntreMatchs);

        Tournoi savedTournoi = tournoiService.create(tournoi);

        int dureeEstimee = calculerDureeEstimee(savedTournoi);
        savedTournoi.setDureeEstimee(dureeEstimee);

        return tournoiService.update(savedTournoi);
    }

    public Tournoi modifierTournoi(Long id, String nouveauTitre, LocalDate nouvelleDateDebut,
                                   LocalDate nouvelleDateFin, int nouveauNombreSpectateurs) {
        LOGGER.info("Tentative de modification du tournoi avec l'ID: {}", id);
        return tournoiService.searchById(id)
                .map(tournoi -> {
                    tournoi.setTitre(nouveauTitre);
                    tournoi.setDateDebut(nouvelleDateDebut);
                    tournoi.setDateFin(nouvelleDateFin);
                    tournoi.setNombreSpectateurs(nouveauNombreSpectateurs);
                    return tournoiService.update(tournoi);
                })
                .orElseGet(() -> {
                    LOGGER.warn("Tournoi avec l'ID {} non trouvé", id);
                    return null;
                });
    }

    public void supprimerTournoi(Long id) {
        LOGGER.info("Tentative de suppression du tournoi avec l'ID: {}", id);
        tournoiService.delete(id);
    }

    public Optional<Tournoi> obtenirTournoi(Long id) {
        LOGGER.info("Tentative d'obtention du tournoi avec l'ID: {}", id);
        return tournoiService.searchById(id);
    }

    public List<Tournoi> obtenirTousTournois() {
        LOGGER.info("Tentative d'obtention de tous les tournois");
        return tournoiService.getAll();
    }

    public void ajouterEquipeATournoi(Long tournoiId, Long equipeId) {
        LOGGER.info("Tentative d'ajout de l'équipe {} au tournoi {}", equipeId, tournoiId);
        tournoiService.addEquipe(tournoiId, equipeId);
    }

    public void retirerEquipeDeTournoi(Long tournoiId, Long equipeId) {
        LOGGER.info("Tentative de retrait de l'équipe {} du tournoi {}", equipeId, tournoiId);
        tournoiService.deleteEquipe(tournoiId, equipeId);
    }

    public int obtenirDureeEstimeeTournoi(Long tournoiId) {
        LOGGER.info("Tentative d'obtention de la durée estimée du tournoi avec l'ID: {}", tournoiId);
        return tournoiService.calculatedureeEstimeeTournoi(tournoiId);
    }

    public int calculerDureeEstimee(Long tournoiId) {
        Optional<Tournoi> tournoi = obtenirTournoi(tournoiId);
        if (tournoi.isPresent()) {
            return calculerDureeEstimee(tournoi.get());
        }
        throw new IllegalArgumentException("Tournoi non trouvé");
    }

    private int calculerDureeEstimee(Tournoi tournoi) {
        int nombreJours = (int) ChronoUnit.DAYS.between(tournoi.getDateDebut(), tournoi.getDateFin()) + 1;
        int nombreMatchsParJour = 8;
        int nombreTotalMatchs = nombreJours * nombreMatchsParJour;

        return (tournoi.getDureeMoyenneMatch() * nombreTotalMatchs) +
                (tournoi.getTempsPauseEntreMatchs() * (nombreTotalMatchs - 1)) +
                tournoi.getTempsCeremonie();
    }

    public void modifierStatutTournoi(Long tournoiId, Status nouveauStatut) {
        LOGGER.info("Tentative de modification du statut du tournoi {} à {}", tournoiId, nouveauStatut);
        tournoiService.updateStatut(tournoiId, nouveauStatut);
    }
}

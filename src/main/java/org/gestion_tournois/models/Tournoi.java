package org.gestion_tournois.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.gestion_tournois.models.enums.Status;

@Entity
@Table(name = "tournois")
public class Tournoi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    private String titre;

    @ManyToOne(optional = false)
    @JoinColumn(name = "jeu_id", nullable = false)
    private Jeu jeu;

    @NotNull
    private LocalDate dateDebut;

    @NotNull
    private LocalDate dateFin;

    @Min(value = 0)
    private int nombreSpectateurs;

    @ManyToMany
    @JoinTable(name = "tournoi_equipe", joinColumns = @JoinColumn(name = "tournoi_id"), inverseJoinColumns = @JoinColumn(name = "equipe_id"))
    private List<Equipe> equipes = new ArrayList<>();

    @Column(nullable = false)
    private int dureeEstimee = 0;

    @Column(nullable = false)
    private int dureeMoyenneMatch = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status statut = Status.PLANIFIE;

    @Column(nullable = false)
    private int tempsCeremonie = 0;

    @Column(nullable = false)
    private int tempsPauseEntreMatchs = 0;

    public Tournoi() {
    }

    public Tournoi(String titre, Jeu jeu, LocalDate dateDebut, LocalDate dateFin, int dureeMoyenneMatch) {
        this.titre = titre;
        this.jeu = jeu;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.dureeMoyenneMatch = dureeMoyenneMatch;
        this.statut = Status.PLANIFIE;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Jeu getJeu() {
        return jeu;
    }

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public int getNombreSpectateurs() {
        return nombreSpectateurs;
    }

    public void setNombreSpectateurs(int nombreSpectateurs) {
        this.nombreSpectateurs = nombreSpectateurs;
    }

    public List<Equipe> getEquipes() {
        return equipes;
    }

    public void setEquipes(List<Equipe> equipes) {
        this.equipes = equipes;
    }

    public int getDureeEstimee() {
        return dureeEstimee;
    }

    public void setDureeEstimee(int dureeEstimee) {
        this.dureeEstimee = dureeEstimee;
    }

    public int getTempsPauseEntreMatchs() {
        return tempsPauseEntreMatchs;
    }

    public void setTempsPauseEntreMatchs(int tempsPauseEntreMatchs) {
        this.tempsPauseEntreMatchs = tempsPauseEntreMatchs;
    }

    public int getTempsCeremonie() {
        return tempsCeremonie;
    }

    public void setTempsCeremonie(int tempsCeremonie) {
        this.tempsCeremonie = tempsCeremonie;
    }

    public Status getStatut() {
        return statut;
    }

    public void setStatut(Status statut) {
        this.statut = statut;
    }

    public int calculateEstimatedDuration() {
        int nombreEquipes = this.equipes.size();
        int difficulteJeu = this.jeu.getDifficulte();
        int baseEstimation = (nombreEquipes * this.dureeMoyenneMatch * difficulteJeu) + this.tempsPauseEntreMatchs;
        return baseEstimation + this.tempsCeremonie;
    }

    public int getDureeMoyenneMatch() {
        return dureeMoyenneMatch;
    }

    public void setDureeMoyenneMatch(int dureeMoyenneMatch) {
        this.dureeMoyenneMatch = dureeMoyenneMatch;
    }

}

package org.gestion_tournois.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;

@Entity
@Table(name = "jeux")
public class Jeu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(unique = true)
    private String nom;

    @Min(value = 1)
    @Max(value = 10)
    private int difficulte;

    @Min(value = 1)
    private int dureeMoyenneMatch;

    // Constructors
    public Jeu() {
    }

    public Jeu(String nom, int difficulte, int dureeMoyenneMatch) {
        this.nom = nom;
        this.difficulte = difficulte;
        this.dureeMoyenneMatch = dureeMoyenneMatch;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getDifficulte() {
        return difficulte;
    }

    public void setDifficulte(int difficulte) {
        this.difficulte = difficulte;
    }

    public int getDureeMoyenneMatch() {
        return dureeMoyenneMatch;
    }

    public void setDureeMoyenneMatch(int dureeMoyenneMatch) {
        this.dureeMoyenneMatch = dureeMoyenneMatch;
    }

    // toString method
    @Override
    public String toString() {
        return "Jeu{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", difficulte=" + difficulte +
                ", dureeMoyenneMatch=" + dureeMoyenneMatch +
                '}';
    }
}

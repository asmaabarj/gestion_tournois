package org.gestion_tournois.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Min;

@Entity
@Table(name = "joueurs")
public class Joueur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(unique = true)
    private String pseudo;

    @NotNull
    @Min(value = 13)
    private int age;

    @ManyToOne
    @JoinColumn(name = "equipe_id")
    private Equipe equipe;

    // Constructors
    public Joueur() {
    }

    public Joueur(String pseudo, int age) {
        this.pseudo = pseudo;
        this.age = age;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    // toString method
    @Override
    public String toString() {
        return "Joueur{" +
                "id=" + id +
                ", pseudo='" + pseudo + '\'' +
                ", age=" + age +
                ", equipe=" + (equipe != null ? equipe.getNom() : "Aucune") +
                '}';
    }
}

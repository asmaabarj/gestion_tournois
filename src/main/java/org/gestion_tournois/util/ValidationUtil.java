package org.gestion_tournois.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidationUtil {
    
    public static boolean validerPseudo(String pseudo) {
        return pseudo != null && pseudo.length() >= 3 && pseudo.length() <= 20 
            && pseudo.matches("^[a-zA-Z0-9_-]+$");
    }

    public static boolean validerAge(int age) {
        return age >= 12 && age <= 99;
    }

    public static boolean validerNomEquipe(String nom) {
        return nom != null && nom.length() >= 2 && nom.length() <= 30 
            && nom.matches("^[a-zA-Z0-9\\s_-]+$");
    }

    public static boolean validerNomJeu(String nom) {
        return nom != null && nom.length() >= 2 && nom.length() <= 50 
            && nom.matches("^[a-zA-Z0-9\\s_-]+$");
    }

    public static boolean validerDifficulte(int difficulte) {
        return difficulte >= 1 && difficulte <= 10;
    }

    public static boolean validerDuree(int duree) {
        return duree > 0 && duree <= 360;
    }

    public static boolean validerTitreTournoi(String titre) {
        return titre != null && titre.length() >= 3 && titre.length() <= 100 
            && titre.matches("^[a-zA-Z0-9\\s_-]+$");
    }

    public static boolean validerNombreSpectateurs(int nombreSpectateurs) {
        return nombreSpectateurs >= 0 && nombreSpectateurs <= 100000;
    }

    public static boolean validerDates(LocalDate dateDebut, LocalDate dateFin) {
        if (dateDebut == null || dateFin == null) {
            return false;
        }
        return !dateDebut.isAfter(dateFin) && !dateDebut.isBefore(LocalDate.now());
    }

    public static LocalDate validerFormatDate(String dateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate date = LocalDate.parse(dateStr, formatter);
            if (date.isBefore(LocalDate.now())) {
                return null;
            }
            return date;
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static boolean validerId(Long id) {
        return id != null && id > 0;
    }
}

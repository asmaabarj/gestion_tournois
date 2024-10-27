package org.gestion_tournois.utils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidationUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationUtil.class);
    private static final Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public static <T> boolean validerObject(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<T> violation : violations) {
                LOGGER.error("Erreur de validation: {} - {}", 
                    violation.getPropertyPath(), 
                    violation.getMessage());
            }
            return false;
        }
        return true;
    }

    public static boolean validerNom(String nom) {
        return nom != null && !nom.trim().isEmpty();
    }

    public static boolean validerDates(LocalDate dateDebut, LocalDate dateFin) {
        if (dateDebut == null || dateFin == null) {
            return false;
        }
        return !dateDebut.isAfter(dateFin);
    }

    public static LocalDate validerDateFormat(String input) {
        try {
            return LocalDate.parse(input);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("La date doit être au format YYYY-MM-DD");
        }
    }

    public static int validerNombrePositif(String input) {
        try {
            int nombre = Integer.parseInt(input);
            if (nombre <= 0) {
                throw new IllegalArgumentException("Le nombre doit être positif");
            }
            return nombre;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Veuillez entrer un nombre valide");
        }
    }

    public static Long validerLong(String input) {
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Veuillez entrer un nombre valide");
        }
    }
}

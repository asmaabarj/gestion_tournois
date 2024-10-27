package org.gestion_tournois;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.gestion_tournois.presentation.ConsoleUi;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOGGER.info("Démarrage de l'application");

        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

            Server h2WebServer = context.getBean("h2WebServer", Server.class);
            String h2ConsoleUrl = h2WebServer.getURL();
            LOGGER.info("H2 Console URL: {}", h2ConsoleUrl);

            ConsoleUi ui = context.getBean(ConsoleUi.class);
            ui.afficherMenuPrincipal();

        } catch (Exception e) {
            LOGGER.error("Erreur lors du démarrage de l'application", e);
        } finally {
            LOGGER.info("Fermeture de l'application");
        }
    }
}
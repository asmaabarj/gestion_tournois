package org.gestion_tournois;

import org.gestion_tournois.presentation.ConsoleUi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        LOGGER.info("Démarrage de l'application");
        
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        
        org.h2.tools.Server h2WebServer = context.getBean("h2WebServer", org.h2.tools.Server.class);
        String h2ConsoleUrl = h2WebServer.getURL();
        LOGGER.info("H2 Console URL: {}", h2ConsoleUrl);

        ConsoleUi console = context.getBean(ConsoleUi.class);
        console.afficherMenuPrincipal();

        LOGGER.info("Fermeture de l'application");
    }
}

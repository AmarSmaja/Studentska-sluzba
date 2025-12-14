package ui.console;

import config.AppConfig;

/**
 * Ulazna tacka za konzolnu varijantu aplikacije "Studentska sluzba".
 */
public class ConsoleMain {
    /**
     * Glavna metoda za pokretanje konzolne aplikacije.
     * @param args defaultni argumenti.
     */
    static void main(String[] args) {
        AppConfig config = new AppConfig();
        ConsoleApp app = new ConsoleApp(config);
        app.start();
    }
}

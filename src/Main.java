import config.AppConfig;
import ui.swing.LoginFrame;

import javax.swing.*;

/**
 * Ulazna tacka Java Desktop aplikacije "Studentska sluzba".
 * <ol>
 *     <li>Kreira se centralna konfiguracija aplikacije {@link AppConfig}.</li>
 *     <li>Pokrece se Swing GUI tako sto se otvara pocetni {@link LoginFrame}.</li>
 * </ol>
 */
public class Main {
    /**
     * Glavna metoda aplikacije.
     * @param args defaultni argumenti.
     */
    public static void main(String[] args) {
        AppConfig config = new AppConfig();

        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame(config);
            frame.setVisible(true);
        });
    }
}

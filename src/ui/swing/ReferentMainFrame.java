package ui.swing;

import config.AppConfig;

import ui.swing.predmet.PredmetPanel;
import ui.swing.student.StudentPanel;
import ui.swing.upis.UpisPanel;
import ui.swing.ocjena.OcjenePanel;
import ui.swing.karton.KartonPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Glavni prozor za rad referenta u Swing aplikaciji.
 * <p>Sadrzi tabove za sve funkcionalnosti koje referent treba:
 * <ul>
 *     <li><b>Studenti</b> - upravljanje studentima ({@link StudentPanel})</li>
 *     <li><b>Predmeti</b> - upravljanje predmetima ({@link PredmetPanel})</li>
 *     <li><b>Upisi i ocjene</b> - Upis studenata na predmete i rad sa ocjenama ({@link UpisPanel})</li>
 *     <li><b>Ocjene</b> - Pregled i izmjena ocjena po upisima ({@link OcjenePanel})</li>
 *     <li><b>Karton</b> - pregled studentskog kratona i polozenih ECTS bodova ({@link KartonPanel})</li>
 * </ul>
 * </p>
 */
public class ReferentMainFrame extends JFrame {
    /**
     * Kreira glavni prozor za referenta i dodaje tabove za sve funkcionalnosti.
     * @param config Konfiguracija koja se prosljedjuje nizim panelima.
     */
    public ReferentMainFrame(AppConfig config) {
        setTitle("Studentska služba - Referent");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Studenti", new StudentPanel(config));
        tabbedPane.addTab("Predmeti", new PredmetPanel(config));
        tabbedPane.addTab("Upisi i ocjene", new UpisPanel(config));
        tabbedPane.addTab("Ocjene", new OcjenePanel(config));
        tabbedPane.addTab("Karton", new KartonPanel(config));

        add(tabbedPane, BorderLayout.CENTER);
    }
}
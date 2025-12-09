package ui.swing;

import config.AppConfig;

import ui.swing.predmet.PredmetPanel;
import ui.swing.student.StudentPanel;
import ui.swing.upis.UpisPanel;
import ui.swing.ocjena.OcjenePanel;
import ui.swing.karton.KartonPanel;

import javax.swing.*;
import java.awt.*;

public class ReferentMainFrame extends JFrame {
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
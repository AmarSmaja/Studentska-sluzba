package ui.swing.predmet;

import config.AppConfig;
import domain.Predmet;
import service.PredmetService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PredmetPanel extends JPanel {
    private final PredmetService predmetService;

    // UI komponente
    private JTextArea taLista;
    private JTextField tfSifra;
    private JTextField tfNaziv;
    private JTextField tfEcts;
    private JTextField tfSemestar;
    private JButton btnOsvjezi;
    private JButton btnDodaj;

    public PredmetPanel(AppConfig config) {
        this.predmetService = config.getPredmetService();
        initGui();
        osvjeziListu();
    }

    private void initGui() {
        setLayout(new BorderLayout());

        // --- Lijevo: lista predmeta ---
        taLista = new JTextArea();
        taLista.setEditable(false);
        JScrollPane scroll = new JScrollPane(taLista);
        add(scroll, BorderLayout.CENTER);

        // --- Desno: forma za unos ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tfSifra = new JTextField(10);
        tfNaziv = new JTextField(10);
        tfEcts = new JTextField(10);
        tfSemestar = new JTextField(10);

        int y = 0;

        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Šifra predmeta:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfSifra, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Naziv:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfNaziv, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("ECTS (1–15):"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfEcts, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Semestar (1–10):"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfSemestar, gbc);

        // Dugmad
        y++;
        btnOsvjezi = new JButton("Osvježi listu");
        btnDodaj = new JButton("Dodaj predmet");

        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(btnOsvjezi, gbc);
        gbc.gridx = 1;
        formPanel.add(btnDodaj, gbc);

        add(formPanel, BorderLayout.EAST);

        // Listeneri
        btnOsvjezi.addActionListener(e -> osvjeziListu());
        btnDodaj.addActionListener(e -> dodajPredmet());
    }

    private void osvjeziListu() {
        try {
            List<Predmet> predmeti = predmetService.sviPredmeti();
            if (predmeti.isEmpty()) {
                taLista.setText("Nema predmeta.");
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (Predmet p : predmeti) {
                sb.append(p).append(System.lineSeparator());
            }
            taLista.setText(sb.toString());
        } catch (Exception e) {
            taLista.setText("Greška pri učitavanju predmeta: " + e.getMessage());
        }
    }

    private void dodajPredmet() {
        try {
            String sifra = tfSifra.getText().trim();
            String naziv = tfNaziv.getText().trim();
            int ects = Integer.parseInt(tfEcts.getText().trim());
            int semestar = Integer.parseInt(tfSemestar.getText().trim());

            Predmet p = new Predmet(sifra, naziv, ects, semestar);
            predmetService.kreirajPredmet(p);

            JOptionPane.showMessageDialog(this,
                    "Predmet dodan.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);

            // očisti polja
            tfSifra.setText("");
            tfNaziv.setText("");
            tfEcts.setText("");
            tfSemestar.setText("");

            osvjeziListu();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Greška: " + e.getMessage(),
                    "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }
}

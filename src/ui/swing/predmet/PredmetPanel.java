package ui.swing.predmet;

import config.AppConfig;
import domain.Predmet;
import service.PredmetService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Swing GUI panel za rad sa predmetima.
 * <p>Omogucava:
 * <ul>
 *     <li>Prikaz liste svih predmeta.</li>
 *     <li>Dodavanje novih predmeta.</li>
 * </ul>
 * Za poslovnu logiku koristi se {@link PredmetService}.
 * </p>
 */
public class PredmetPanel extends JPanel {
    private final PredmetService predmetService;

    private JTextArea Lista;
    private JTextField tfSifra;
    private JTextField tfNaziv;
    private JTextField tfEcts;
    private JTextField tfSemestar;
    private JButton btnOsvjezi;
    private JButton btnDodaj;

    /**
     * Kreira panel za rad sa predmetima i inicijalizuje GUI komponente.
     * @param config Konfiguracija iz koje se dobija {@link PredmetService}.
     */
    public PredmetPanel(AppConfig config) {
        this.predmetService = config.getPredmetService();
        initGui();
        osvjeziListu();
    }

    /**
     * Inicijalizuje i rasporedjuje sve Swing komponente.
     */
    private void initGui() {
        setLayout(new BorderLayout());

        Lista = new JTextArea();
        Lista.setEditable(false);
        JScrollPane scroll = new JScrollPane(Lista);
        add(scroll, BorderLayout.CENTER);

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

        y++;
        btnOsvjezi = new JButton("Osvježi listu");
        btnDodaj = new JButton("Dodaj predmet");

        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(btnOsvjezi, gbc);
        gbc.gridx = 1;
        formPanel.add(btnDodaj, gbc);

        add(formPanel, BorderLayout.EAST);

        btnOsvjezi.addActionListener(e -> osvjeziListu());
        btnDodaj.addActionListener(e -> dodajPredmet());
    }

    /**
     * Ucitava sve predmete iz {@link PredmetService#sviPredmeti()}, i
     * prikazuje ih u polju {@link #Lista}.
     */
    private void osvjeziListu() {
        try {
            List<Predmet> predmeti = predmetService.sviPredmeti();
            if (predmeti.isEmpty()) {
                Lista.setText("Nema predmeta.");
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (Predmet p : predmeti) {
                sb.append(p).append(System.lineSeparator());
            }
            Lista.setText(sb.toString());
        } catch (Exception e) {
            Lista.setText("Greška pri učitavanju predmeta: " + e.getMessage());
        }
    }

    /**
     * Dodaje novi predmet koristeci informacije iz korisnickog inputa.
     */
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

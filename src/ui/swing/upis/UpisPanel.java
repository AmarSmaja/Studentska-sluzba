package ui.swing.upis;

import config.AppConfig;
import domain.Upis;
import service.UpisService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UpisPanel extends JPanel {
    private final UpisService upisService;

    // UI komponente
    private JTextArea taLista;

    private JTextField tfIndeks;
    private JTextField tfSifraPredmeta;
    private JTextField tfGodina;

    private JTextField tfUpisId;
    private JTextField tfOcjena;
    private JTextField tfRazlog;

    private JButton btnUpisi;
    private JButton btnPrikaziUpise;
    private JButton btnPonistiUpis;
    private JButton btnUnesiOcjenu;
    private JButton btnPromijeniOcjenu;

    public UpisPanel(AppConfig config) {
        this.upisService = config.getUpisService();
        initGui();
    }

    private void initGui() {
        setLayout(new BorderLayout());

        // --- Lijevo: tekstualna lista upisa ---
        taLista = new JTextArea();
        taLista.setEditable(false);
        JScrollPane scroll = new JScrollPane(taLista);
        add(scroll, BorderLayout.CENTER);

        // --- Desno: forma za rad sa upisima ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tfIndeks = new JTextField(10);
        tfSifraPredmeta = new JTextField(10);
        tfGodina = new JTextField(10);

        tfUpisId = new JTextField(10);
        tfOcjena = new JTextField(10);
        tfRazlog = new JTextField(10);

        int y = 0;

        // Indeks
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Broj indeksa:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfIndeks, gbc);

        // Šifra predmeta
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Šifra predmeta:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfSifraPredmeta, gbc);

        // Godina
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Akad. godina (npr. 2024/25):"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfGodina, gbc);

        // Dugmad: upis + prikaz
        y++;
        btnUpisi = new JButton("Upiši predmet");
        btnPrikaziUpise = new JButton("Prikaži upise studenta");

        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(btnUpisi, gbc);
        gbc.gridx = 1;
        formPanel.add(btnPrikaziUpise, gbc);

        // ID upisa
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("ID upisa:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfUpisId, gbc);

        // Ocjena
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Ocjena (5–10):"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfOcjena, gbc);

        // Razlog
        y++;
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Razlog izmjene:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfRazlog, gbc);

        // Dugmad: ocjene + poništavanje
        y++;
        btnUnesiOcjenu = new JButton("Unesi ocjenu");
        btnPromijeniOcjenu = new JButton("Promijeni ocjenu");

        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(btnUnesiOcjenu, gbc);
        gbc.gridx = 1;
        formPanel.add(btnPromijeniOcjenu, gbc);

        y++;
        btnPonistiUpis = new JButton("Poništi upis");
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        formPanel.add(btnPonistiUpis, gbc);

        add(formPanel, BorderLayout.EAST);

        // Listeneri
        btnUpisi.addActionListener(e -> upisiPredmet());
        btnPrikaziUpise.addActionListener(e -> prikaziUpise());
        btnUnesiOcjenu.addActionListener(e -> unesiOcjenu());
        btnPromijeniOcjenu.addActionListener(e -> promijeniOcjenu());
        btnPonistiUpis.addActionListener(e -> ponistiUpis());
    }

    // ====== Akcije ======

    private void upisiPredmet() {
        try {
            String indeks = tfIndeks.getText().trim();
            String sifra = tfSifraPredmeta.getText().trim();
            String godina = tfGodina.getText().trim();

            Upis u = upisService.upisiPredmet(indeks, sifra, godina);

            JOptionPane.showMessageDialog(this,
                    "Upis kreiran. ID = " + u.getId(),
                    "Info", JOptionPane.INFORMATION_MESSAGE);

            prikaziUpise();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Greška: " + e.getMessage(),
                    "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void prikaziUpise() {
        try {
            String indeks = tfIndeks.getText().trim();
            if (indeks.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Unesite broj indeksa.",
                        "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<Upis> upisi = upisService.upisiStudenta(indeks);
            if (upisi.isEmpty()) {
                taLista.setText("Nema upisa za ovog studenta.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            for (Upis u : upisi) {
                sb.append(u).append(System.lineSeparator());
            }
            taLista.setText(sb.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Greška: " + e.getMessage(),
                    "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void unesiOcjenu() {
        try {
            long id = Long.parseLong(tfUpisId.getText().trim());
            int ocjena = Integer.parseInt(tfOcjena.getText().trim());

            upisService.unesiOcjenu(id, ocjena);

            JOptionPane.showMessageDialog(this,
                    "Ocjena unesena.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);

            prikaziUpise();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Greška: " + e.getMessage(),
                    "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void promijeniOcjenu() {
        try {
            long id = Long.parseLong(tfUpisId.getText().trim());
            int ocjena = Integer.parseInt(tfOcjena.getText().trim());
            String razlog = tfRazlog.getText().trim();

            upisService.promijeniOcjenu(id, ocjena, razlog);

            JOptionPane.showMessageDialog(this,
                    "Ocjena promijenjena.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);

            prikaziUpise();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Greška: " + e.getMessage(),
                    "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ponistiUpis() {
        try {
            long id = Long.parseLong(tfUpisId.getText().trim());
            upisService.ponistiUpis(id);

            JOptionPane.showMessageDialog(this,
                    "Upis poništen.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);

            prikaziUpise();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Greška: " + e.getMessage(),
                    "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }
}

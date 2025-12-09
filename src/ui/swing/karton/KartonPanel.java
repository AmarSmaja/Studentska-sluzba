package ui.swing.karton;

import config.AppConfig;
import service.UpisService;

import javax.swing.*;
import java.awt.*;

public class KartonPanel extends JPanel {
    private final UpisService upisService;

    private JTextArea taKarton;
    private JTextField tfIndeks;
    private JButton btnPrikazi;
    private JLabel lblUkupnoEctsValue;

    public KartonPanel(AppConfig config) {
        this.upisService = config.getUpisService();
        initGui();
    }

    private void initGui() {
        setLayout(new BorderLayout());

        taKarton = new JTextArea();
        taKarton.setEditable(false);
        taKarton.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(taKarton);
        add(scrollPane, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.weightx = 1.0;

        tfIndeks = new JTextField(12);
        btnPrikazi = new JButton("Prikaži karton");
        JLabel lblUkupnoEcts = new JLabel("Ukupno položenih ECTS:");
        lblUkupnoEctsValue = new JLabel("0");

        int y = 0;

        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Broj indeksa:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfIndeks, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        formPanel.add(btnPrikazi, gbc);

        y++;
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(lblUkupnoEcts, gbc);
        gbc.gridx = 1;
        formPanel.add(lblUkupnoEctsValue, gbc);

        add(formPanel, BorderLayout.EAST);

        btnPrikazi.addActionListener(e -> prikaziKarton());
    }

    private void prikaziKarton() {
        try {
            String indeks = tfIndeks.getText().trim();
            if (indeks.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Unesite broj indeksa.",
                        "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // poziv servisa
            UpisService.StudentKarton karton = upisService.kreirajKarton(indeks);
            String tekst = upisService.formatirajKarton(karton);

            taKarton.setText(tekst);
            lblUkupnoEctsValue.setText(
                    String.valueOf(karton.getUkupnoPolozenihEcts())
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Greška: " + ex.getMessage(),
                    "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }
}

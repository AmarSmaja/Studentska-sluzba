package ui.swing.ocjena;

import config.AppConfig;
import domain.Upis;
import service.UpisService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Swing GUI panel za rad sa ocjenama studenata.
 * <p>Omogucava:
 * <ul>
 *     <li>Prikaz svih upisa studenta.</li>
 *     <li>Unos ocjene za upis.</li>
 *     <li>Promjena postojece ocjene.</li>
 * </ul>
 * Za poslovnu logiku koristi se {@link UpisService}.
 * </p>
 */
public class OcjenePanel extends JPanel {
    private final UpisService upisService;

    private JTextArea taLista;

    private JTextField tfIndeks;
    private JTextField tfUpisId;
    private JTextField tfOcjena;
    private JTextField tfRazlog;

    private JButton btnUcitajUpise;
    private JButton btnUnesiOcjenu;
    private JButton btnPromijeniOcjenu;

    /**
     * Kreira panel za upravljanje ocjenama i incijalizuje GUI komponente.
     * @param config Konfiguracija iz koje se dobija {@link UpisService}.
     */
    public OcjenePanel(AppConfig config) {
        this.upisService = config.getUpisService();
        initGui();
    }

    /**
     * Inicijalizuje i rasporedjuje sve Swing komponente.
     */
    private void initGui() {
        setLayout(new BorderLayout());

        taLista = new JTextArea();
        taLista.setEditable(false);
        JScrollPane scroll = new JScrollPane(taLista);
        add(scroll, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.weightx = 1.0;

        tfIndeks = new JTextField(12);
        tfUpisId = new JTextField(12);
        tfOcjena = new JTextField(5);
        tfRazlog = new JTextField(12);

        btnUcitajUpise = new JButton("Prikaži upise studenta");
        btnUnesiOcjenu = new JButton("Unesi ocjenu");
        btnPromijeniOcjenu = new JButton("Promijeni ocjenu");

        int y = 0;

        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Broj indeksa:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfIndeks, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        formPanel.add(btnUcitajUpise, gbc);

        y++;
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("ID upisa:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfUpisId, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Ocjena (5–10):"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfOcjena, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Razlog promjene:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfRazlog, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        formPanel.add(btnUnesiOcjenu, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        formPanel.add(btnPromijeniOcjenu, gbc);

        add(formPanel, BorderLayout.EAST);

        btnUcitajUpise.addActionListener(e -> ucitajUpiseStudenta());
        btnUnesiOcjenu.addActionListener(e -> unesiOcjenu());
        btnPromijeniOcjenu.addActionListener(e -> promijeniOcjenu());
    }

    /**
     * Ucitava i prikazuje sve upise za unesen broj indeksa.
     */
    private void ucitajUpiseStudenta() {
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
                taLista.setText("Student nema upisa.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            for (Upis u : upisi) {
                sb.append(u).append(System.lineSeparator());
            }
            taLista.setText(sb.toString());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Greška: " + ex.getMessage(),
                    "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Vrsi unos ocjene za upis ciji je ID unesen.
     */
    private void unesiOcjenu() {
        try {
            long upisId = Long.parseLong(tfUpisId.getText().trim());
            int ocjena = Integer.parseInt(tfOcjena.getText().trim());

            upisService.unesiOcjenu(upisId, ocjena);

            JOptionPane.showMessageDialog(this,
                    "Ocjena je unesena.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);

            ucitajUpiseStudenta();

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this,
                    "ID upisa i ocjena moraju biti brojevi.",
                    "Greška", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Greška: " + ex.getMessage(),
                    "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Vrsi promjenu ocjene za upis i upisuje razlog promjene.
     */
    private void promijeniOcjenu() {
        try {
            long upisId = Long.parseLong(tfUpisId.getText().trim());
            int ocjena = Integer.parseInt(tfOcjena.getText().trim());
            String razlog = tfRazlog.getText().trim();

            if (razlog.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Razlog promjene ocjene je obavezan.",
                        "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }

            upisService.promijeniOcjenu(upisId, ocjena, razlog);

            JOptionPane.showMessageDialog(this,
                    "Ocjena je promijenjena.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);

            ucitajUpiseStudenta();

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this,
                    "ID upisa i ocjena moraju biti brojevi.",
                    "Greška", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Greška: " + ex.getMessage(),
                    "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }
}

package ui.swing;

import config.AppConfig;
import domain.Upis;
import service.UpisService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Glavni prozor za studenta u Swing aplikaciji.
 */
public class StudentMainFrame extends JFrame {
    private final UpisService upisService;
    private final String brojIndeksa;
    private JTextArea taUpisi;

    /**
     * Kreira prozor za studenta i ucitava njegove upise.
     * @param config Konfiguracija iz koje se dobija {@link UpisService}.
     * @param brojIndeksa Broj indeksa studenta, npr. 100/IT-20.
     */
    public StudentMainFrame(AppConfig config, String brojIndeksa) {
        this.upisService = config.getUpisService();
        this.brojIndeksa = brojIndeksa;

        initGui();
        ucitajUpise();
    }

    /**
     * Inicijalizuje sve GUI kompontente.
     */
    private void initGui() {
        setTitle("Studentska služba - Student (" + brojIndeksa + ")");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblNaslov = new JLabel("Moji upisi i ocjene", SwingConstants.CENTER);
        add(lblNaslov, BorderLayout.NORTH);

        taUpisi = new JTextArea();
        taUpisi.setEditable(false);
        add(new JScrollPane(taUpisi), BorderLayout.CENTER);
    }

    /**
     * Ucitava sve upise studenata.
     */
    private void ucitajUpise() {
        try {
            List<Upis> upisi = upisService.upisiStudenta(brojIndeksa);
            if (upisi.isEmpty()) {
                taUpisi.setText("Nema upisa.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (Upis u : upisi) {
                    sb.append(u).append(System.lineSeparator());
                }
                taUpisi.setText(sb.toString());
            }
        } catch (Exception e) {
            taUpisi.setText("Greška: " + e.getMessage());
        }
    }
}

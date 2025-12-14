package ui.swing;

import config.AppConfig;
import service.StudentService;

import javax.swing.*;
import java.awt.*;

/**
 * Glavni login prozor Swing aplikacije.
 * <p>Omogucava:
 * <ul>
 *     <li><b>Referent</b> - otvara {@link ReferentMainFrame}</li>
 *     <li><b>Student</b> - uz provjeru broja indeksa otvara {@link StudentMainFrame}</li>
 * </ul>
 * </p>
 */
public class LoginFrame extends JFrame {
    private final AppConfig config;
    private final StudentService studentService;

    private JRadioButton rbReferent;
    private JRadioButton rbStudent;
    private JTextField tfIndeks;
    private JButton btnLogin;
    private JButton btnExit;

    /**
     * Kreira login prozor i incijalizuje GUI komponente.
     * @param config Konfiguracija iz koje se dobija {@link StudentService}.
     */
    public LoginFrame(AppConfig config) {
        this.config = config;
        this.studentService = config.getStudentService();

        initGui();
    }

    /**
     * Inicijalizuje i rasporedjuje sve Swing komponente.
     */
    private void initGui() {
        setTitle("Studentska služba - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        rbReferent = new JRadioButton("Referent", true);
        rbStudent = new JRadioButton("Student");

        ButtonGroup group = new ButtonGroup();
        group.add(rbReferent);
        group.add(rbStudent);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Odaberite ulogu:"), gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(rbReferent, gbc);

        gbc.gridx = 1;
        panel.add(rbStudent, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Broj indeksa (za studenta):"), gbc);

        tfIndeks = new JTextField();
        gbc.gridx = 1;
        panel.add(tfIndeks, gbc);

        btnLogin = new JButton("Login");
        btnExit = new JButton("Izlaz");

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(btnLogin, gbc);

        gbc.gridx = 1;
        panel.add(btnExit, gbc);

        add(panel);

        btnLogin.addActionListener(e -> onLogin());
        btnExit.addActionListener(e -> System.exit(0));
    }

    /**
     * Obrada logina na osnovu izabrane uloge.
     * <p>Ako je odabran referent:
     * <ul>
     *     <li>Kreira se i prikazuje {@link ReferentMainFrame}</li>
     *     <li>Trenutni login prozor se zatvara</li>
     * </ul>
     * </p>
     * <p>Ako je odabran student:
     * <ul>
     *     <li>Validira se broj indeksa</li>
     *     <li>Provjerava postojanje studenta preko {@link StudentService#pronadjiPoIndeksu(String)}</li>
     *     <li>U slucaju uspjeha, otvara se {@link StudentMainFrame}</li>
     * </ul>
     * </p>
     */
    private void onLogin() {
        if (rbReferent.isSelected()) {
            ReferentMainFrame refFrame = new ReferentMainFrame(config);
            refFrame.setVisible(true);
            dispose();
        } else if (rbStudent.isSelected()) {
            String indeks = tfIndeks.getText().trim();
            if (indeks.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Unesite broj indeksa.",
                        "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                studentService.pronadjiPoIndeksu(indeks);

                StudentMainFrame sFrame = new StudentMainFrame(config, indeks);
                sFrame.setVisible(true);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Greška: " + ex.getMessage(),
                        "Login neuspješan", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

package ui.swing;

import config.AppConfig;
import service.StudentService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final AppConfig config;
    private final StudentService studentService;

    private JRadioButton rbReferent;
    private JRadioButton rbStudent;
    private JTextField tfIndeks;
    private JButton btnLogin;
    private JButton btnExit;

    public LoginFrame(AppConfig config) {
        this.config = config;
        this.studentService = config.getStudentService();

        initGui();
    }

    private void initGui() {
        setTitle("Studentska služba - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null); // centriraj

        // Glavni panel
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Uloga
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

        // Broj indeksa (koristi se samo za studenta)
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Broj indeksa (za studenta):"), gbc);

        tfIndeks = new JTextField();
        gbc.gridx = 1;
        panel.add(tfIndeks, gbc);

        // Dugmad
        btnLogin = new JButton("Login");
        btnExit = new JButton("Izlaz");

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(btnLogin, gbc);

        gbc.gridx = 1;
        panel.add(btnExit, gbc);

        add(panel);

        // Listeneri
        btnLogin.addActionListener(e -> onLogin());
        btnExit.addActionListener(e -> System.exit(0));
    }

    private void onLogin() {
        if (rbReferent.isSelected()) {
            // za referenta trenutno ne tražimo user/pass (možeš kasnije dodati)
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
                // provjera da student postoji
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

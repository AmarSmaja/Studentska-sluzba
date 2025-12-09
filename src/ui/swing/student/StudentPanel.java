package ui.swing.student;

import config.AppConfig;
import domain.Student;
import service.StudentService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StudentPanel extends JPanel {
    private final StudentService studentService;

    // UI komponentе
    private JTextArea taLista;
    private JTextField tfIndeks;
    private JTextField tfIme;
    private JTextField tfPrezime;
    private JTextField tfStudijskiProgram;  // NOVO
    private JTextField tfGodinaUpisa;
    private JButton btnOsvjezi;
    private JButton btnDodaj;
    private JButton btnObrisi;
    private JButton btnPretragaPrezime;

    public StudentPanel(AppConfig config) {
        this.studentService = config.getStudentService();
        initGui();
        osvjeziListu();
    }

    private void initGui() {
        setLayout(new BorderLayout());

        // lijevo: lista studenata
        taLista = new JTextArea();
        taLista.setEditable(false);
        JScrollPane scroll = new JScrollPane(taLista);
        add(scroll, BorderLayout.CENTER);

        // desno: forma
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.weightx = 1.0;

        tfIndeks = new JTextField(10);
        tfIme = new JTextField(10);
        tfPrezime = new JTextField(10);
        tfStudijskiProgram = new JTextField(10);
        tfGodinaUpisa = new JTextField(10);

        btnOsvjezi = new JButton("Osvježi listu");
        btnDodaj = new JButton("Dodaj studenta");
        btnObrisi = new JButton("Obriši studenta");
        btnPretragaPrezime = new JButton("Pretraži po prezimenu");

        int y = 0;

        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Broj indeksa:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfIndeks, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Ime:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfIme, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Prezime:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfPrezime, gbc);

        y++;
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Studijski program:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfStudijskiProgram, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Godina upisa:"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfGodinaUpisa, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1;
        formPanel.add(btnOsvjezi, gbc);
        gbc.gridx = 1;
        formPanel.add(btnDodaj, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        formPanel.add(btnObrisi, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        formPanel.add(btnPretragaPrezime, gbc);

        add(formPanel, BorderLayout.EAST);

        btnOsvjezi.addActionListener(e -> osvjeziListu());
        btnDodaj.addActionListener(e -> dodajStudenta());
        btnObrisi.addActionListener(e -> obrisiStudenta());
        btnPretragaPrezime.addActionListener(e -> pretraziPoPrezimenu());
    }

    private void osvjeziListu() {
        try {
            List<Student> studenti = studentService.sviStudenti();
            if (studenti.isEmpty()) {
                taLista.setText("Nema studenata.");
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (Student s : studenti) {
                sb.append(s).append(System.lineSeparator());
            }
            taLista.setText(sb.toString());
        } catch (Exception e) {
            taLista.setText("Greška pri učitavanju studenata: " + e.getMessage());
        }
    }

    private void dodajStudenta() {
        try {
            String indeks = tfIndeks.getText().trim();
            String ime = tfIme.getText().trim();
            String prezime = tfPrezime.getText().trim();
            String program = tfStudijskiProgram.getText().trim();
            int godina = Integer.parseInt(tfGodinaUpisa.getText().trim());

            Student s = new Student(indeks, ime, prezime, program, godina);
            studentService.kreirajStudenta(s);

            JOptionPane.showMessageDialog(this,
                    "Student dodan.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);

            tfIndeks.setText("");
            tfIme.setText("");
            tfPrezime.setText("");
            tfStudijskiProgram.setText("");
            tfGodinaUpisa.setText("");

            osvjeziListu();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Greška: " + e.getMessage(),
                    "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void obrisiStudenta() {
        try {
            String indeks = tfIndeks.getText().trim();
            if (indeks.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Unesite broj indeksa studenta kojeg želite obrisati.",
                        "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Da li sigurno želite obrisati studenta " + indeks + "?",
                    "Potvrda brisanja",
                    JOptionPane.YES_NO_OPTION
            );
            if (choice != JOptionPane.YES_OPTION) {
                return;
            }

            studentService.obrisiStudenta(indeks);

            JOptionPane.showMessageDialog(this,
                    "Student je obrisan.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);

            osvjeziListu();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Greška: " + e.getMessage(),
                    "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pretraziPoPrezimenu() {
        try {
            String prefix = tfPrezime.getText().trim();
            if (prefix.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Unesite prezime ili prefix prezimena.",
                        "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }

            java.util.List<Student> lista = studentService.pretraziPoPrezimenuPrefix(prefix);
            if (lista.isEmpty()) {
                taLista.setText("Nema studenata sa zadanim prezimenom/prefixom.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (Student s : lista) {
                    sb.append(s).append(System.lineSeparator());
                }
                taLista.setText(sb.toString());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Greška: " + e.getMessage(),
                    "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }
}

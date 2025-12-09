package ui.console;

import config.AppConfig;
import domain.Predmet;
import domain.Student;
import domain.Upis;
import service.PredmetService;
import service.StudentService;
import service.UpisService;

import java.util.List;
import java.util.Scanner;

public class ConsoleApp {
    private final StudentService studentService;
    private final PredmetService predmetService;
    private final UpisService upisService;
    private final Scanner scanner;

    public ConsoleApp(AppConfig config) {
        this.studentService = config.getStudentService();
        this.predmetService = config.getPredmetService();
        this.upisService = config.getUpisService();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== Studentska služba (konzola) ===");

        while (true) {
            System.out.println();
            System.out.println("Odaberite ulogu:");
            System.out.println("1) Referent");
            System.out.println("2) Student");
            System.out.println("0) Izlaz");
            System.out.print(">> ");

            String izbor = scanner.nextLine().trim();
            switch (izbor) {
                case "1" -> referentMenu();
                case "2" -> studentMenu();
                case "0" -> {
                    System.out.println("Doviđenja!");
                    return;
                }
                default -> System.out.println("Nepoznata opcija.");
            }
        }
    }

    // ==================== REFERENT MENI ====================

    private void referentMenu() {
        // ovdje možeš kasnije dodati login (user/pass), za sada direktno meni
        while (true) {
            System.out.println();
            System.out.println("=== Referent meni ===");
            System.out.println("1) Studenti");
            System.out.println("2) Predmeti");
            System.out.println("3) Upisi i ocjene");
            System.out.println("0) Nazad");
            System.out.print(">> ");

            String izbor = scanner.nextLine().trim();
            switch (izbor) {
                case "1" -> studentSubMenu();
                case "2" -> predmetSubMenu();
                case "3" -> upisSubMenu();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Nepoznata opcija.");
            }
        }
    }

    // --- Studenti ---

    private void studentSubMenu() {
        while (true) {
            System.out.println();
            System.out.println("=== Studenti ===");
            System.out.println("1) Prikaži sve");
            System.out.println("2) Dodaj studenta");
            System.out.println("3) Ažuriraj studenta");
            System.out.println("4) Obriši studenta");
            System.out.println("5) Pretraga po prezimenu (prefix)");
            System.out.println("0) Nazad");
            System.out.print(">> ");

            String izbor = scanner.nextLine().trim();
            try {
                switch (izbor) {
                    case "1" -> prikaziSveStudente();
                    case "2" -> dodajStudenta();
                    case "3" -> azurirajStudenta();
                    case "4" -> obrisiStudenta();
                    case "5" -> pretragaStudenataPoPrezimenu();
                    case "0" -> {
                        return;
                    }
                    default -> System.out.println("Nepoznata opcija.");
                }
            } catch (Exception e) {
                System.out.println("Greška: " + e.getMessage());
            }
        }
    }

    private void prikaziSveStudente() {
        List<Student> studenti = studentService.sviStudenti();
        if (studenti.isEmpty()) {
            System.out.println("Nema studenata.");
            return;
        }
        studenti.forEach(System.out::println);
    }

    private void dodajStudenta() {
        System.out.print("Broj indeksa: ");
        String indeks = scanner.nextLine().trim();

        System.out.print("Ime: ");
        String ime = scanner.nextLine().trim();

        System.out.print("Prezime: ");
        String prezime = scanner.nextLine().trim();

        System.out.print("Studijski program: ");
        String program = scanner.nextLine().trim();

        System.out.print("Godina upisa (2020-2050): ");
        int godina = Integer.parseInt(scanner.nextLine().trim());

        Student s = new Student(indeks, ime, prezime, program, godina);
        studentService.kreirajStudenta(s);
        System.out.println("Student kreiran.");
    }

    private void azurirajStudenta() {
        System.out.print("Unesite broj indeksa studenta za ažuriranje: ");
        String indeks = scanner.nextLine().trim();

        Student s = studentService.sviStudenti().stream()
                .filter(st -> st.getBrojIndeksa().equals(indeks))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Student ne postoji."));

        System.out.print("Novo ime (" + s.getIme() + "): ");
        String ime = scanner.nextLine().trim();
        if (!ime.isBlank()) s.setIme(ime);

        System.out.print("Novo prezime (" + s.getPrezime() + "): ");
        String prezime = scanner.nextLine().trim();
        if (!prezime.isBlank()) s.setPrezime(prezime);

        System.out.print("Novi studijski program (" + s.getStudijskiProgram() + "): ");
        String program = scanner.nextLine().trim();
        if (!program.isBlank()) s.setStudijskiProgram(program);

        System.out.print("Nova godina upisa (" + s.getGodinaUpisa() + "): ");
        String godStr = scanner.nextLine().trim();
        if (!godStr.isBlank()) {
            s.setGodinaUpisa(Integer.parseInt(godStr));
        }

        studentService.azurirajStudenta(s);
        System.out.println("Student ažuriran.");
    }

    private void obrisiStudenta() {
        System.out.print("Unesite broj indeksa studenta za brisanje: ");
        String indeks = scanner.nextLine().trim();
        studentService.obrisiStudenta(indeks);
        System.out.println("Student obrisan (ako nije imao upise).");
    }

    private void pretragaStudenataPoPrezimenu() {
        System.out.print("Unesite prezime ili prefix prezimena: ");
        String prefix = scanner.nextLine().trim();

        try {
            List<Student> lista = studentService.pretraziPoPrezimenuPrefix(prefix);
            if (lista.isEmpty()) {
                System.out.println("Nema studenata sa zadanim prezimenom/prefixom.");
            } else {
                lista.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Greška: " + e.getMessage());
        }
    }

    // --- Predmeti ---

    private void predmetSubMenu() {
        while (true) {
            System.out.println();
            System.out.println("=== Predmeti ===");
            System.out.println("1) Prikaži sve");
            System.out.println("2) Dodaj predmet");
            System.out.println("3) Ažuriraj predmet");
            System.out.println("4) Obriši predmet");
            System.out.println("0) Nazad");
            System.out.print(">> ");

            String izbor = scanner.nextLine().trim();
            try {
                switch (izbor) {
                    case "1" -> prikaziSvePredmete();
                    case "2" -> dodajPredmet();
                    case "3" -> azurirajPredmet();
                    case "4" -> obrisiPredmet();
                    case "0" -> {
                        return;
                    }
                    default -> System.out.println("Nepoznata opcija.");
                }
            } catch (Exception e) {
                System.out.println("Greška: " + e.getMessage());
            }
        }
    }

    private void prikaziSvePredmete() {
        List<Predmet> predmeti = predmetService.sviPredmeti();
        if (predmeti.isEmpty()) {
            System.out.println("Nema predmeta.");
            return;
        }
        predmeti.forEach(System.out::println);
    }

    private void dodajPredmet() {
        System.out.print("Šifra predmeta: ");
        String sifra = scanner.nextLine().trim();

        System.out.print("Naziv: ");
        String naziv = scanner.nextLine().trim();

        System.out.print("ECTS (1-15): ");
        int ects = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Semestar (1-10): ");
        int semestar = Integer.parseInt(scanner.nextLine().trim());

        Predmet p = new Predmet(sifra, naziv, ects, semestar);
        predmetService.kreirajPredmet(p);
        System.out.println("Predmet kreiran.");
    }

    private void azurirajPredmet() {
        System.out.print("Unesite šifru predmeta za ažuriranje: ");
        String sifra = scanner.nextLine().trim();

        Predmet p = predmetService.pronadjiPoSifri(sifra);

        System.out.print("Novi naziv (" + p.getNaziv() + "): ");
        String naziv = scanner.nextLine().trim();
        if (!naziv.isBlank()) p.setNaziv(naziv);

        System.out.print("Novi ECTS (" + p.getEcts() + "): ");
        String ectsStr = scanner.nextLine().trim();
        if (!ectsStr.isBlank()) p.setEcts(Integer.parseInt(ectsStr));

        System.out.print("Novi semestar (" + p.getSemestar() + "): ");
        String semStr = scanner.nextLine().trim();
        if (!semStr.isBlank()) p.setSemestar(Integer.parseInt(semStr));

        predmetService.azurirajPredmet(p);
        System.out.println("Predmet ažuriran.");
    }

    private void obrisiPredmet() {
        System.out.print("Unesite šifru predmeta za brisanje: ");
        String sifra = scanner.nextLine().trim();
        predmetService.obrisiPredmet(sifra);
        System.out.println("Predmet obrisan (ako nema upisa).");
    }

    // --- Upisi i ocjene ---

    private void upisSubMenu() {
        while (true) {
            System.out.println();
            System.out.println("=== Upisi i ocjene ===");
            System.out.println("1) Upisi studenta na predmet");
            System.out.println("2) Poništi upis");
            System.out.println("3) Unesi ocjenu");
            System.out.println("4) Promijeni ocjenu");
            System.out.println("5) Prikaži sve upise studenta");
            System.out.println("0) Nazad");
            System.out.print(">> ");

            String izbor = scanner.nextLine().trim();
            try {
                switch (izbor) {
                    case "1" -> upisiPredmetStudentu();
                    case "2" -> ponistiUpis();
                    case "3" -> unesiOcjenu();
                    case "4" -> promijeniOcjenu();
                    case "5" -> prikaziUpiseStudenta();
                    case "0" -> {
                        return;
                    }
                    default -> System.out.println("Nepoznata opcija.");
                }
            } catch (Exception e) {
                System.out.println("Greška: " + e.getMessage());
            }
        }
    }

    private void upisiPredmetStudentu() {
        System.out.print("Broj indeksa: ");
        String indeks = scanner.nextLine().trim();

        System.out.print("Šifra predmeta: ");
        String sifra = scanner.nextLine().trim();

        System.out.print("Akademska godina (npr. 2024/25): ");
        String godina = scanner.nextLine().trim();

        Upis u = upisService.upisiPredmet(indeks, sifra, godina);
        System.out.println("Upis kreiran. ID upisa = " + u.getId());
    }

    private void ponistiUpis() {
        System.out.print("ID upisa za poništavanje: ");
        long id = Long.parseLong(scanner.nextLine().trim());
        upisService.ponistiUpis(id);
        System.out.println("Upis poništen.");
    }

    private void unesiOcjenu() {
        System.out.print("ID upisa: ");
        long id = Long.parseLong(scanner.nextLine().trim());

        System.out.print("Ocjena (5-10): ");
        int ocjena = Integer.parseInt(scanner.nextLine().trim());

        upisService.unesiOcjenu(id, ocjena);
        System.out.println("Ocjena unesena.");
    }

    private void promijeniOcjenu() {
        System.out.print("ID upisa: ");
        long id = Long.parseLong(scanner.nextLine().trim());

        System.out.print("Nova ocjena (5-10): ");
        int ocjena = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Razlog izmjene: ");
        String razlog = scanner.nextLine().trim();

        upisService.promijeniOcjenu(id, ocjena, razlog);
        System.out.println("Ocjena promijenjena.");
    }

    private void prikaziUpiseStudenta() {
        System.out.print("Broj indeksa: ");
        String indeks = scanner.nextLine().trim();

        List<Upis> upisi = upisService.upisiStudenta(indeks);
        if (upisi.isEmpty()) {
            System.out.println("Nema upisa za ovog studenta.");
            return;
        }
        upisi.forEach(System.out::println);
    }

    // ==================== STUDENT MENI ====================

    private void studentMenu() {
        System.out.print("Unesite svoj broj indeksa: ");
        String indeks = scanner.nextLine().trim();

        System.out.println("=== Student meni (" + indeks + ") ===");
        while (true) {
            System.out.println();
            System.out.println("1) Prikaži sve svoje upise");
            System.out.println("0) Nazad");
            System.out.print(">> ");

            String izbor = scanner.nextLine().trim();
            switch (izbor) {
                case "1" -> {
                    try {
                        List<Upis> upisi = upisService.upisiStudenta(indeks);
                        if (upisi.isEmpty()) {
                            System.out.println("Nema upisa.");
                        } else {
                            upisi.forEach(System.out::println);
                        }
                    } catch (Exception e) {
                        System.out.println("Greška: " + e.getMessage());
                    }
                }
                case "0" -> {
                    return;
                }
                default -> System.out.println("Nepoznata opcija.");
            }
        }
    }
}

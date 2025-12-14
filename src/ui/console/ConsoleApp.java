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

/**
 * Konzolna aplikacija za rad sa sistemom studentske sluzbe.
 * Omogucava rad u dvije uloge:
 * <ul>
 *     <li><b>Referent</b> - upravljanje studentima, predmetima, upisima i ocjenama.</li>
 *     <li><b>Student</b> - pregled vlastitih upisa.</li>
 * </ul>
 * Klasa koristi servise {@link StudentService}, {@link PredmetService} i {@link UpisService} za
 * poslovnu logiku.
 */
public class ConsoleApp {
    private final StudentService studentService;
    private final PredmetService predmetService;
    private final UpisService upisService;
    private final Scanner scanner;

    /**
     * Inicijalizuje konzolnu aplikaciju koristeci konfiguraciju.
     * @param config Centralna konfiguracija iz koje se dobijaju servisi.
     */
    public ConsoleApp(AppConfig config) {
        this.studentService = config.getStudentService();
        this.predmetService = config.getPredmetService();
        this.upisService = config.getUpisService();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Startna tacka aplikacije. Prikazuje glavni meni za izbor uloge i obradjuje korisnicki unos.
     */
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

    /**
     * Glavni meni za referenta. Omogucava pristup pod-menijima za rad sa studentima,
     * predmetima i rad sa ocjenama/upisima.
     */
    private void referentMenu() {
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

    /**
     * Pod-meni za rad sa studentima u referentskoj ulozi.
     * <p>Omogucava:
     * <ul>
     *     <li>Prikaz svih studenata.</li>
     *     <li>Dodavanja novih studenata.</li>
     *     <li>Azuriranje postojecih studenata.</li>
     *     <li>Brisanje studenata.</li>
     *     <li>Pretragu studenata po prefiksu prezimena,</li>
     * </ul>
     * </p>
     */
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

    /**
     * Ispisuje listu svih studenata
     */
    private void prikaziSveStudente() {
        List<Student> studenti = studentService.sviStudenti();
        if (studenti.isEmpty()) {
            System.out.println("Nema studenata.");
            return;
        }
        studenti.forEach(System.out::println);
    }

    /**
     * Dodaje novog studenta sa informacijama iz korisnickog inputa, i
     * prosljedjuje ih {@link StudentService}
     */
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

    /**
     * Azurira postojeceg studenta sa informacijama iz korisnickog inputa,
     * i prosljedjuje ih {@link StudentService}
     */
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

    /**
     * Vrsi brisanje studenta po broj indeksa kojeg korisnik unese preko input polja,
     * i prosljedjuje ih {@link StudentService}.
     */
    private void obrisiStudenta() {
        System.out.print("Unesite broj indeksa studenta za brisanje: ");
        String indeks = scanner.nextLine().trim();
        studentService.obrisiStudenta(indeks);
        System.out.println("Student obrisan (ako nije imao upise).");
    }

    /**
     * Vrsi pretragu studenta po prezimenu ili po prefiksu prezimena.
     */
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

    /**
     * Pod-meni za rad sa predmetima u referentskoj ulozi.
     * <p>Omogucava:
     * <ul>
     *     <li>Prikaz svih predmeta.</li>
     *     <li>Dodavanje novog predmeta.</li>
     *     <li>Azuriranje postojeceg predmeta.</li>
     *     <li>Brisanje predmeta.</li>
     * </ul>
     * </p>
     */
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

    /**
     * Ispisuje sve predmete
     */
    private void prikaziSvePredmete() {
        List<Predmet> predmeti = predmetService.sviPredmeti();
        if (predmeti.isEmpty()) {
            System.out.println("Nema predmeta.");
            return;
        }
        predmeti.forEach(System.out::println);
    }

    /**
     * Dodaje novi predmet koristeci informacije iz korisnickog inputa,
     */
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

    /**
     * Azurira postojeci predmet koristeci informacije iz korisnickog inputa.
     */
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

    /**
     * Vrsi brisanje predmeta po sifri koristeci informacije iz korisnickog inputa.
     */
    private void obrisiPredmet() {
        System.out.print("Unesite šifru predmeta za brisanje: ");
        String sifra = scanner.nextLine().trim();
        predmetService.obrisiPredmet(sifra);
        System.out.println("Predmet obrisan (ako nema upisa).");
    }

    /**
     * Pod-meni za rad sa upisima i ocjenama u referentskoj ulozi.
     * <p>Omogucava:
     * <ul>
     *     <li>Upis studenata na predmet.</li>
     *     <li>Ponistavanje upisa.</li>
     *     <li>Unosenje ocjene.</li>
     *     <li>Promjenu ocjene.</li>
     *     <li>Prikaz upisa studenata.</li>
     * </ul>
     * </p>
     */
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

    /**
     * Vrsi upis studenta na predmet koristeci informacije iz korisnickog inputa.
     */
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

    /**
     * Vrsi ponistavanje upisa koristeci ID upisa iz korisnickog inputa.
     */
    private void ponistiUpis() {
        System.out.print("ID upisa za poništavanje: ");
        long id = Long.parseLong(scanner.nextLine().trim());
        upisService.ponistiUpis(id);
        System.out.println("Upis poništen.");
    }

    /**
     * Vrsi unos ocjene koristeci ID upisa i Ocjenu iz korisnickog inputa
     */
    private void unesiOcjenu() {
        System.out.print("ID upisa: ");
        long id = Long.parseLong(scanner.nextLine().trim());

        System.out.print("Ocjena (5-10): ");
        int ocjena = Integer.parseInt(scanner.nextLine().trim());

        upisService.unesiOcjenu(id, ocjena);
        System.out.println("Ocjena unesena.");
    }

    /**
     * Mijenja ocjenu koristeci ID upisa, novu ocjenu i razlog izmeje iz korisnickog inputa.
     */
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

    /**
     * Ispisuje sve upise jednog studenta po broju indeksa iz korisnickog inputa.
     */
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

    /**
     * Meni za studenta. Student unosi broj indeksa i dobija pregled vlastitih upisa.
     */
    private void studentMenu() {
        System.out.print("Unesite svoj broj indeksa: ");
        String indeks = scanner.nextLine().trim();

        try {
            studentService.pronadjiPoIndeksu(indeks);
        } catch (Exception e) {
            System.out.println("Greška: " + e.getMessage());
            return;
        }

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

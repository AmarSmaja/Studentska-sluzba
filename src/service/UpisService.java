package service;

import domain.Upis;
import repo.PredmetRepository;
import repo.StudentRepository;
import repo.UpisRepository;

import java.util.List;

/**
 * Servisni sloj za rad sa upisima studenata na predmete, te kreiranje njihovog kartona.
 * <p>Sadrzi poslovnu logiku za :
 * <ul>Upis studenata na predmet</ul>
 * <ul>Ponistavanje upisa</ul>
 * <ul>Unos i izmjenu ocjena</ul>
 * <ul>Generisanje kartona studenata</ul>
 * </p>
 */
public class UpisService {
    private final UpisRepository upisRepo;
    private final StudentRepository studentRepo;
    private final PredmetRepository predmetRepo;

    /**
     * Predstavlja jednu stavku u kartonu studenata.
     */
    public static class StavkaKartona {
        private final String sifraPredmeta;
        private final String nazivPredmeta;
        private final String akademskaGodina;
        private final Integer ocjena; // može biti null
        private final int ects;

        /**
         * Kreira novu stavku kartona.
         * @param sifraPredmeta Sfira predmeta, npr. MAT1.
         * @param nazivPredmeta Naziv predmeta, npr. Matematika 1.
         * @param akademskaGodina Akademska godina, npr. 2020./21.
         * @param ocjena Ocjena, npr. 7.
         * @param ects Broj ECTS bodova, npr. 6.
         */
        public StavkaKartona(String sifraPredmeta, String nazivPredmeta,
                             String akademskaGodina, Integer ocjena, int ects) {
            this.sifraPredmeta = sifraPredmeta;
            this.nazivPredmeta = nazivPredmeta;
            this.akademskaGodina = akademskaGodina;
            this.ocjena = ocjena;
            this.ects = ects;
        }

        @Override
        public String toString() {
            return String.format("%s - %s (%s), ocjena: %s, ECTS: %d",
                    sifraPredmeta, nazivPredmeta, akademskaGodina,
                    ocjena == null ? "/" : ocjena.toString(),
                    ects);
        }

        public Integer getOcjena() {
            return ocjena;
        }

        public int getEcts() {
            return ects;
        }
    }

    /**
     * Klasa koja predstavlja kompletan karton studenta:
     * osnovne podatke o njemu, listu stavki i ukupan broj ECTS bodova.
     */
    public static class StudentKarton {
        private final String brojIndeksa;
        private final String imePrezime;
        private final java.util.List<StavkaKartona> stavke;
        private final int ukupnoPolozenihEcts;

        /**
         * Kreira novi karton studenta.
         * @param brojIndeksa Broj indeksa studenta, npr. 100/IT-20.
         * @param imePrezime Ime i prezime studenta.
         * @param stavke Lista stavki kartona, predmeti i ocjene.
         * @param ukupnoPolozenihEcts Ukupan broj polozenih ECTS bodova.
         */
        public StudentKarton(String brojIndeksa, String imePrezime, java.util.List<StavkaKartona> stavke, int ukupnoPolozenihEcts) {
            this.brojIndeksa = brojIndeksa;
            this.imePrezime = imePrezime;
            this.stavke = stavke;
            this.ukupnoPolozenihEcts = ukupnoPolozenihEcts;
        }

        public String getBrojIndeksa() {
            return brojIndeksa;
        }

        public String getImePrezime() {
            return imePrezime;
        }

        public java.util.List<StavkaKartona> getStavke() {
            return stavke;
        }

        public int getUkupnoPolozenihEcts() {
            return ukupnoPolozenihEcts;
        }
    }

    /**
     * Inicijalizuje servis za upise.
     * @param upisRepo Repozitorij za pristup podacima o upisima.
     * @param studentRepo Repozitorij za pristup podacima o studentima.
     * @param predmetRepo Repozitorij za pristup podacima o predmetima.
     */
    public UpisService(UpisRepository upisRepo, StudentRepository studentRepo, PredmetRepository predmetRepo) {
        this.upisRepo = upisRepo;
        this.studentRepo = studentRepo;
        this.predmetRepo = predmetRepo;
    }

    /**
     * Vrsi upis studenta na odredjeni predmet u akademskoj godini.
     * @param brojIndeksa Broj indeksa studenta, npr. 100/IT-20.
     * @param sifraPredmeta Sifra predmeta, npr. MAT1.
     * @param akademskaGodina Akademska godina, npr. 2020./21.
     * @return Kreirani upis
     */
    public Upis upisiPredmet(String brojIndeksa, String sifraPredmeta, String akademskaGodina) {
        studentRepo.findById(brojIndeksa).orElseThrow(() -> new IllegalArgumentException("Student ne postoji."));

        predmetRepo.findById(sifraPredmeta).orElseThrow(() -> new IllegalArgumentException("Predmet ne postoji."));

        if (upisRepo.exists(brojIndeksa, sifraPredmeta, akademskaGodina)) {
            throw new IllegalArgumentException("Student je već upisan na ovaj predmet u toj akademskoj godini.");
        }

        Upis u = new Upis(brojIndeksa, sifraPredmeta, akademskaGodina);
        upisRepo.save(u);
        return u;
    }

    /**
     * Ponistava upis na osnovu ID-a.
     * @param upisId ID Upisa, npr. 5.
     */
    public void ponistiUpis(long upisId) {
        upisRepo.delete(upisId);
    }

    /**
     * Unesi ocjenu za postojeci upis.
     * @param upisId ID upisa, npr. 5.
     * @param ocjena Ocjena koja se unosi, npr. 7.
     */
    public void unesiOcjenu(long upisId, int ocjena) {
        if (ocjena < 5 || ocjena > 10) {
            throw new IllegalArgumentException("Ocjena mora biti između 5 i 10.");
        }

        Upis u = upisRepo.findById(upisId).orElseThrow(() -> new IllegalArgumentException("Upis ne postoji."));

        if (u.getOcjena() != null) {
            throw new IllegalArgumentException("Ocjena već postoji. Koristi promjenu ocjene.");
        }

        u.setOcjena(ocjena);
        upisRepo.update(u);
    }

    /**
     * Mijenja postojecu ocjenu za upis i biljezi razlog izmjene.
     * <ul>
     *     <li>Nova ocjena mora biti u opsegu [5 - 10]</li>
     *     <li>Razlog izmjene je obavezan.</li>
     *     <li>Upis mora postojati.</li>
     * </ul>
     * @param upisId ID upisa, npr. 5.
     * @param novaOcjena Nova ocjena, npr. 9.
     * @param razlogIzmjene Opis razloga izmjene.
     */
    public void promijeniOcjenu(long upisId, int novaOcjena, String razlogIzmjene) {
        if (novaOcjena < 5 || novaOcjena > 10) {
            throw new IllegalArgumentException("Ocjena mora biti između 5 i 10.");
        }
        if (razlogIzmjene == null || razlogIzmjene.isBlank()) {
            throw new IllegalArgumentException("Razlog izmjene ocjene je obavezan.");
        }

        Upis u = upisRepo.findById(upisId).orElseThrow(() -> new IllegalArgumentException("Upis ne postoji."));

        u.setOcjena(novaOcjena);
        u.setRazlogIzmjene(razlogIzmjene);
        upisRepo.update(u);
    }

    /**
     * Kreira karton studenta za sve njegove upise.
     * @param brojIndeksa Broj indeksa studenta, npr. 100/IT-20.
     * @return Kreirani StudentKarton
     */
    public StudentKarton kreirajKarton(String brojIndeksa) {
        var studentOpt = studentRepo.findById(brojIndeksa);
        if (studentOpt.isEmpty()) {
            throw new IllegalArgumentException("Student sa indeksom " + brojIndeksa + " ne postoji.");
        }
        var student = studentOpt.get();

        java.util.List<Upis> upisi = upisRepo.findByStudent(brojIndeksa);
        java.util.List<StavkaKartona> stavke = new java.util.ArrayList<>();

        int polozeniEcts = 0;

        for (Upis u : upisi) {
            var predmetOpt = predmetRepo.findBySifra(u.getSifraPredmeta());
            if (predmetOpt.isEmpty()) {
                continue;
            }
            var p = predmetOpt.get();

            Integer ocjena = u.getOcjena();
            int ects = p.getEcts();

            if (ocjena != null && ocjena >= 6) {
                polozeniEcts += ects;
            }

            StavkaKartona sk = new StavkaKartona(
                    p.getSifraPredmeta(),
                    p.getNaziv(),
                    u.getAkademskaGodina(),
                    ocjena,
                    ects
            );
            stavke.add(sk);
        }

        String imePrezime = student.getIme() + " " + student.getPrezime();

        return new StudentKarton(brojIndeksa, imePrezime, stavke, polozeniEcts);
    }

    /**
     * Pomocna metoda koja formatira karton.
     * @param k Karton studenta.
     * @return Formatirani karton.
     */
    public String formatirajKarton(StudentKarton k) {
        StringBuilder sb = new StringBuilder();

        sb.append("Karton studenta ")
                .append(k.getImePrezime())
                .append(" (")
                .append(k.getBrojIndeksa())
                .append(")")
                .append(System.lineSeparator())
                .append("--------------------------------------------------")
                .append(System.lineSeparator());

        for (StavkaKartona s : k.getStavke()) {
            sb.append(s.toString())
                    .append(System.lineSeparator());
        }

        sb.append("--------------------------------------------------")
                .append(System.lineSeparator())
                .append("Ukupno položenih ECTS: ")
                .append(k.getUkupnoPolozenihEcts());

        return sb.toString();
    }

    /**
     * Vrsi upis studenta po broju indeksa.
     * @param brojIndeksa Broj indeksa studenta, npr. 100/IT-20.
     * @return Lista upisa.
     */
    public List<Upis> upisiStudenta(String brojIndeksa) {
        studentRepo.findById(brojIndeksa).orElseThrow(() -> new IllegalArgumentException("Student ne postoji."));

        return upisRepo.findByStudent(brojIndeksa);
    }

    /*
    public List<Upis> upisiStudentaZaGodinu(String brojIndeksa, String akademskaGodina) {
        studentRepo.findById(brojIndeksa).orElseThrow(() -> new IllegalArgumentException("Student ne postoji."));

        return upisRepo.findByStudentAndGodina(brojIndeksa, akademskaGodina);
    }
    */

    /**
     * Vraca sve upise za dati predmet.
     * @param sifraPredmeta Sifra predmeta, npr. MAT1.
     * @return Lista upisa.
     */
    public List<Upis> upisiZaPredmet(String sifraPredmeta) {
        return upisRepo.findByPredmet(sifraPredmeta);
    }
}

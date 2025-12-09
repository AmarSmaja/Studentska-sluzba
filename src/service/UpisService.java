package service;

import domain.Upis;
import repo.PredmetRepository;
import repo.StudentRepository;
import repo.UpisRepository;

import java.util.List;

public class UpisService {
    private final UpisRepository upisRepo;
    private final StudentRepository studentRepo;
    private final PredmetRepository predmetRepo;

    public static class StavkaKartona {
        private final String sifraPredmeta;
        private final String nazivPredmeta;
        private final String akademskaGodina;
        private final Integer ocjena; // može biti null
        private final int ects;

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

    public static class StudentKarton {
        private final String brojIndeksa;
        private final String imePrezime;
        private final java.util.List<StavkaKartona> stavke;
        private final int ukupnoPolozenihEcts;

        public StudentKarton(String brojIndeksa, String imePrezime,
                             java.util.List<StavkaKartona> stavke,
                             int ukupnoPolozenihEcts) {
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

    public UpisService(UpisRepository upisRepo, StudentRepository studentRepo, PredmetRepository predmetRepo) {
        this.upisRepo = upisRepo;
        this.studentRepo = studentRepo;
        this.predmetRepo = predmetRepo;
    }

    public Upis upisiPredmet(String brojIndeksa, String sifraPredmeta, String akademskaGodina) {
        studentRepo.findById(brojIndeksa)
                .orElseThrow(() -> new IllegalArgumentException("Student ne postoji."));

        predmetRepo.findById(sifraPredmeta)
                .orElseThrow(() -> new IllegalArgumentException("Predmet ne postoji."));

        if (upisRepo.exists(brojIndeksa, sifraPredmeta, akademskaGodina)) {
            throw new IllegalArgumentException("Student je već upisan na ovaj predmet u toj akademskoj godini.");
        }

        Upis u = new Upis(brojIndeksa, sifraPredmeta, akademskaGodina);
        upisRepo.save(u);
        return u;
    }

    public void ponistiUpis(long upisId) {
        upisRepo.delete(upisId);
    }

    public void unesiOcjenu(long upisId, int ocjena) {
        if (ocjena < 5 || ocjena > 10) {
            throw new IllegalArgumentException("Ocjena mora biti između 5 i 10.");
        }

        Upis u = upisRepo.findById(upisId)
                .orElseThrow(() -> new IllegalArgumentException("Upis ne postoji."));

        if (u.getOcjena() != null) {
            throw new IllegalArgumentException("Ocjena već postoji. Koristi promjenu ocjene.");
        }

        u.setOcjena(ocjena);
        upisRepo.update(u);
    }

    public void promijeniOcjenu(long upisId, int novaOcjena, String razlogIzmjene) {
        if (novaOcjena < 5 || novaOcjena > 10) {
            throw new IllegalArgumentException("Ocjena mora biti između 5 i 10.");
        }
        if (razlogIzmjene == null || razlogIzmjene.isBlank()) {
            throw new IllegalArgumentException("Razlog izmjene ocjene je obavezan.");
        }

        Upis u = upisRepo.findById(upisId)
                .orElseThrow(() -> new IllegalArgumentException("Upis ne postoji."));

        u.setOcjena(novaOcjena);
        u.setRazlogIzmjene(razlogIzmjene);
        upisRepo.update(u);
    }

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
                // ako nema predmeta, preskoči ovaj upis
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

    public List<Upis> upisiStudenta(String brojIndeksa) {
        return upisRepo.findByStudent(brojIndeksa);
    }

    public List<Upis> upisiStudentaZaGodinu(String brojIndeksa, String akademskaGodina) {
        return upisRepo.findByStudentAndGodina(brojIndeksa, akademskaGodina);
    }

    public List<Upis> upisiZaPredmet(String sifraPredmeta) {
        return upisRepo.findByPredmet(sifraPredmeta);
    }
}

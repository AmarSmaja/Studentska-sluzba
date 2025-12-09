package service;

import domain.Predmet;
import repo.PredmetRepository;
import repo.UpisRepository;

import java.util.List;

public class PredmetService {
    private final PredmetRepository predmetRepo;
    private final UpisRepository upisRepo;

    public PredmetService(PredmetRepository predmetRepo, UpisRepository upisRepo) {
        this.predmetRepo = predmetRepo;
        this.upisRepo = upisRepo;
    }

    public void kreirajPredmet(Predmet p) {
        if (p.getSifraPredmeta() == null || p.getSifraPredmeta().isBlank()) {
            throw new IllegalArgumentException("Šifra predmeta je obavezna.");
        }
        if (p.getNaziv() == null || p.getNaziv().isBlank()) {
            throw new IllegalArgumentException("Naziv predmeta je obavezan.");
        }
        if (p.getEcts() < 1 || p.getEcts() > 15) {
            throw new IllegalArgumentException("ECTS bodovi moraju biti između 1 i 15.");
        }
        if (p.getSemestar() < 1 || p.getSemestar() > 10) {
            throw new IllegalArgumentException("Semestar mora biti između 1 i 10.");
        }
        if (predmetRepo.findById(p.getSifraPredmeta()).isPresent()) {
            throw new IllegalArgumentException("Predmet sa ovom šifrom već postoji.");
        }

        predmetRepo.save(p);
    }

    public void azurirajPredmet(Predmet p) {
        if (predmetRepo.findById(p.getSifraPredmeta()).isEmpty()) {
            throw new IllegalArgumentException("Predmet ne postoji.");
        }
        predmetRepo.update(p);
    }

    public void obrisiPredmet(String sifra) {
        if (upisRepo.existsForPredmet(sifra)) {
            throw new IllegalStateException(
                    "Predmet ima upise/ocjene i ne može se obrisati."
            );
        }
        predmetRepo.delete(sifra);
    }

    public Predmet pronadjiPoSifri(String sifra) {
        return predmetRepo.findById(sifra)
                .orElseThrow(() -> new IllegalArgumentException("Predmet ne postoji."));
    }

    public List<Predmet> sviPredmeti() {
        return predmetRepo.findAll();
    }
}

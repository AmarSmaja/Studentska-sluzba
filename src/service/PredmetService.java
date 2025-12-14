package service;

import domain.Predmet;
import repo.PredmetRepository;
import repo.UpisRepository;

import java.util.List;

/**
 * Service sloj za rad sa predmetima.
 * <p>Sadrzi poslovnu logiku za CRUD operacije sa bazom, kao i validaciju unosa u bazu.</p>
 */
public class PredmetService {
    private final PredmetRepository predmetRepo;
    private final UpisRepository upisRepo;

    /**
     * Inicijalizuje servis za predmete.
     * @param predmetRepo Repozitorij za pristup podacima o predmetima.
     * @param upisRepo Repozitorij za pristup podacima o upisima
     */
    public PredmetService(PredmetRepository predmetRepo, UpisRepository upisRepo) {
        this.predmetRepo = predmetRepo;
        this.upisRepo = upisRepo;
    }

    /**
     * Kreira novi predmet.
     * <ul>
     *     <li>Sifra predmeta je obavezna i mora biti jedinstvena</li>
     *      <li>Naziv predmeta je obavezan</li>
     *      <li>ECTS bodovi moraju biti u opsegu [1 - 8]</li>
     *      <li>Semestar mora biti u opsegu [1 - 10]</li>
     * </ul>
     * @param p Predmet koji se kreira.
     */
    public void kreirajPredmet(Predmet p) {
        if (p.getSifraPredmeta() == null || p.getSifraPredmeta().isBlank()) {
            throw new IllegalArgumentException("Šifra predmeta je obavezna.");
        }
        if (p.getNaziv() == null || p.getNaziv().isBlank()) {
            throw new IllegalArgumentException("Naziv predmeta je obavezan.");
        }
        if (p.getEcts() < 1 || p.getEcts() > 8) {
            throw new IllegalArgumentException("ECTS bodovi moraju biti između 1 i 8.");
        }
        if (p.getSemestar() < 1 || p.getSemestar() > 10) {
            throw new IllegalArgumentException("Semestar mora biti između 1 i 10.");
        }
        if (predmetRepo.findById(p.getSifraPredmeta()).isPresent()) {
            throw new IllegalArgumentException("Predmet sa ovom šifrom već postoji.");
        }

        predmetRepo.save(p);
    }

    /**
     * Vrsi azuriranje vec postojeceg predmeta.
     * @param p Predmet sa novim podacima.
     */
    public void azurirajPredmet(Predmet p) {
        if (predmetRepo.findById(p.getSifraPredmeta()).isEmpty()) {
            throw new IllegalArgumentException("Predmet ne postoji.");
        }
        predmetRepo.update(p);
    }

    /**
     * Vrsi brisanje predmeta na osnovu njegove sifre.
     * @param sifra Sifra predmeta koji se brise.
     */
    public void obrisiPredmet(String sifra) {
        if (upisRepo.existsForPredmet(sifra)) {
            throw new IllegalStateException(
                    "Predmet ima upise/ocjene i ne može se obrisati."
            );
        }
        predmetRepo.delete(sifra);
    }

    /**
     * Vrsi pretragu predmeta po njegovoj sifri.
     * @param sifra Sifra predmeta, npr. MAT1.
     * @return Predmet, ako postoji.
     */
    public Predmet pronadjiPoSifri(String sifra) {
        return predmetRepo.findById(sifra)
                .orElseThrow(() -> new IllegalArgumentException("Predmet ne postoji."));
    }

    /**
     * Vraca listu svih predmeta u bazi podataka.
     * @return Lista predmeta.
     */
    public List<Predmet> sviPredmeti() {
        return predmetRepo.findAll();
    }
}

package repo;

import domain.Predmet;

import java.util.List;
import java.util.Optional;

/**
 * Repozitorij interfejs za pristup podacima o predmetima.
 * <p>Definise CRUD operacije nad bazom.</p>
 */
public interface PredmetRepository {
    /**
     * Spasava novi predmet u bazu podataka.
     * @param predmet Predmet koji se dodaje.
     */
    void save(Predmet predmet);

    /**
     * Azurira postojeci predmet u bazi podataka.
     * @param predmet Predmet sa azuriranim podacima.
     */
    void update(Predmet predmet);

    /**
     * Brise predmet pomocu njegove sifre.
     * @param sifraPredmeta Sifra predmeta, npr. MAT1
     */
    void delete(String sifraPredmeta);

    /**
     * Vrsi pretragu predmeta po njihovoj sifri.
     * @param sifraPredmeta Sifra predmeta, npr. MAT1
     * @return Predmet, ako postoji
     */
    Optional<Predmet> findById(String sifraPredmeta);

    /**
     * Vrsi pretragu predmeta po sifri
     * @param sifra Sifra predmeta, npr. MAT1
     * @return Predmet, ako postoji
     */
    Optional<Predmet> findBySifra(String sifra);

    /**
     * Vraca listu svih predmeta koji se nalaze u bazi podataka.
     * @return Lista predmeta
     */
    List<Predmet> findAll();

    /**
     * Vrsi pretragu predmeta po prefiksu.
     * @param nazivPrefix Prefiks nekog predmeta, npr. S.
     * @return Lista predmeta sa tim prefiksom.
     */
    List<Predmet> findByNazivPrefix(String nazivPrefix);
}

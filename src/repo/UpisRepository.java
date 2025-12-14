package repo;

import domain.Upis;

import java.util.List;
import java.util.Optional;

/**
 * Repozitorij interfejs za pristup podacima o upisima studenata na predmete.
 * <p>Definise CRUD operacije nad bazom.</p>
 */
public interface UpisRepository {
    /**
     * Cuva novi upis u bazu podataka
     * @param upis ID upisa koji se dodaje
     */
    void save(Upis upis);

    /**
     * Azurira postojeci upis u bazi podataka
     * @param upis ID upisa koji se azurira, npr. 5
     */
    void update(Upis upis);

    /**
     * Brise upis na osnovu njegovog ID-a upisa.
     * @param id ID upisa koji se brise, npr. 5
     */
    void delete(long id);

    /**
     * Vrsi pretragu upisa po ID-u.
     * @param id ID upisa, npr. 5
     * @return Upis, ako postoji.
     */
    Optional<Upis> findById(long id);

    /**
     * Vrsi pretragu upisa po broju indeksa studenta.
     * @param brojIndeksa Broj indeksa studenta, npr. 100/IT-20.
     * @return Lista upisa.
     */
    List<Upis> findByStudent(String brojIndeksa);

    /**
     * Vrsi pretragu upisa studenta po indeksu i akademskoj godini.
     * @param brojIndeksa Broj indeksa studenta, npr. 100/IT-20.
     * @param akademskaGodina Akademska godina, npr. 2020./21.
     * @return Lista upisa
     */
    List<Upis> findByStudentAndGodina(String brojIndeksa, String akademskaGodina);

    /**
     * Vrsi pretragu upisa po odredjenom predmetu.
     * @param sifraPredmeta Sifra predmeta, npr. MAT1.
     * @return Lista upisa
     */
    List<Upis> findByPredmet(String sifraPredmeta);

    /**
     * Provjerava da li vec postoji upis istog studenta na isti predmet.
     * @param brojIndeksa Broj indeksa studenta, npr. 100/IT-20.
     * @param sifraPredmeta Sifra predmeta, npr. MAT1.
     * @param akademskaGodina Akademska godina, npr. 2020./21.
     * @return {@code true} ako postoji barem jedan takav upis, inace {@code false}.
     */
    boolean exists(String brojIndeksa, String sifraPredmeta, String akademskaGodina);

    /**
     * Provjerava da li student ima barem jedan upis.
     * @param brojIndeksa Broj indeksa studenta, npr. 100/IT-20.
     * @return {@code true} ako postoji barem jedan upis za studenta, inace {@code false}.
     */
    boolean existsForStudent(String brojIndeksa);

    /**
     * Provjerava da li predmet ima barem jedan upis.
     * @param sifraPredmeta Sifra predmeta, npr. MAT1.
     * @return {@code true} ako postoji barem jedan upis za predmet, inace {@code false}.
     */
    boolean existsForPredmet(String sifraPredmeta);
}

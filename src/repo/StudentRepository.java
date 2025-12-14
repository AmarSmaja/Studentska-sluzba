package repo;

import domain.Student;

import java.util.List;
import java.util.Optional;

/**
 * Repozitorij interfejs za pristup pdoacima o studentima.
 * <p>Definise CRUD operacije nad bazom.</p>
 */
public interface StudentRepository {
    /**
     * Spasava novog studenta u bazu podataka.
     * @param student Student koji se dodaje
     */
    void save(Student student);

    /**
     * Vrsi azuriranje podataka postojeceg studenta.
     * @param student Student sa azuriranim podacima.
     */
    void update(Student student);

    /**
     * Vrsi brisanje studenta na osnovu broja indeksa.
     * @param brojIndeksa Broj indeksa studenta, npr. 100/IT-20
     */
    void delete(String brojIndeksa);

    /**
     * Vrsi pretragu studenata po indeksu.
     * @param brojIndeksa Broj indeksa studenta, npr. 100/IT-20
     * @return Student, ako postoji
     */
    Optional<Student> findById(String brojIndeksa);

    /**
     * Vraca listu svih studenata u bazi podataka.
     * @return Lista studenata
     */
    List<Student> findAll();

    /**
     * Vrsi pretragu studenata po prefiksu.
     * @param prefix Prefiks prezimena, npr. S.
     * @return Lista studenata sa tim prefiksom.
     */
    List<Student> findByPrezimePrefix(String prefix);
}

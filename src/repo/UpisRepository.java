package repo;

import domain.Upis;

import java.util.List;
import java.util.Optional;

public interface UpisRepository {
    void save(Upis upis);

    void update(Upis upis);

    void delete(long id);

    Optional<Upis> findById(long id);

    List<Upis> findByStudent(String brojIndeksa);

    List<Upis> findByStudentAndGodina(String brojIndeksa, String akademskaGodina);

    List<Upis> findByPredmet(String sifraPredmeta);

    boolean exists(String brojIndeksa, String sifraPredmeta, String akademskaGodina);

    boolean existsForStudent(String brojIndeksa);

    boolean existsForPredmet(String sifraPredmeta);
}

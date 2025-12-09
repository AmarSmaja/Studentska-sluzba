package repo;

import domain.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {
    void save(Student student);

    void update(Student student);

    void delete(String brojIndeksa);

    Optional<Student> findById(String brojIndeksa);

    List<Student> findAll();

    List<Student> findByPrezimePrefix(String prefix);
}

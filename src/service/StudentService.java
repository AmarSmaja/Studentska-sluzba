package service;

import domain.Student;
import repo.StudentRepository;
import repo.UpisRepository;

import java.util.List;

public class StudentService {
    private final StudentRepository studentRepo;
    private final UpisRepository upisRepo;

    public StudentService(StudentRepository studentRepo, UpisRepository upisRepo) {
        this.studentRepo = studentRepo;
        this.upisRepo = upisRepo;
    }

    public void kreirajStudenta(Student s) {
        if (s.getBrojIndeksa() == null || s.getBrojIndeksa().isBlank()) {
            throw new IllegalArgumentException("Broj indeksa je obavezan.");
        }
        if (s.getGodinaUpisa() < 2020 || s.getGodinaUpisa() > 2050) {
            throw new IllegalArgumentException("Godina upisa mora biti između 2020 i 2050.");
        }
        if (studentRepo.findById(s.getBrojIndeksa()).isPresent()) {
            throw new IllegalArgumentException("Student sa ovim indeksom već postoji.");
        }

        studentRepo.save(s);
    }

    public void azurirajStudenta(Student s) {
        if (studentRepo.findById(s.getBrojIndeksa()).isEmpty()) {
            throw new IllegalArgumentException("Student ne postoji.");
        }
        studentRepo.update(s);
    }

    public Student pronadjiPoIndeksu(String indeks) {
        return studentRepo.findById(indeks)
                .orElseThrow(() -> new IllegalArgumentException("Student ne postoji."));
    }

    public void obrisiStudenta(String indeks) {
        if (upisRepo.existsForStudent(indeks)) {
            throw new IllegalStateException("Student ima evidentirane upise/ocjene i ne može se obrisati.");
        }
        studentRepo.delete(indeks);
    }

    public List<Student> pretraziPoPrezimenuPrefix(String prefix) {
        if (prefix == null || prefix.isBlank()) {
            throw new IllegalArgumentException("Prezime/prefix ne smije biti prazno.");
        }
        return studentRepo.findByPrezimePrefix(prefix.trim());
    }

    public List<Student> sviStudenti() {
        return studentRepo.findAll();
    }
}

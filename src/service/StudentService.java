package service;

import domain.Student;
import repo.StudentRepository;
import repo.UpisRepository;

import java.util.List;

/**
 * Servisni sloj za rad sa studentima.
 * <p>Sadrzi poslovnu logiku za CRUD operacije sa bazom, kao i validaciju unosa u bazu.</p>
 */
public class StudentService {
    private final StudentRepository studentRepo;
    private final UpisRepository upisRepo;

    /**
     * Inicijalizuje servis za studente.
     * @param studentRepo Repozitorij za pristup podacima o studentima.
     * @param upisRepo Repozitorij za pristup podacima o upisima.
     */
    public StudentService(StudentRepository studentRepo, UpisRepository upisRepo) {
        this.studentRepo = studentRepo;
        this.upisRepo = upisRepo;
    }

    /**
     * Kreira novog studenta.
     * @param s Student koji se kreira.
     */
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

    /**
     * Vrsi azuriranje vec postojeceg studenta.
     * @param s Student sa novim podacima.
     */
    public void azurirajStudenta(Student s) {
        if (studentRepo.findById(s.getBrojIndeksa()).isEmpty()) {
            throw new IllegalArgumentException("Student ne postoji.");
        }
        studentRepo.update(s);
    }

    /**
     * Vrsi pretragu studenata po broju indeksa.
     * @param indeks Broj indeksa studenta, npr. 100/IT-20.
     * @return Student, ako postoji.
     */
    public Student pronadjiPoIndeksu(String indeks) {
        return studentRepo.findById(indeks)
                .orElseThrow(() -> new IllegalArgumentException("Student ne postoji."));
    }

    /**
     * Vrsi brisanje studenta po broju indeksa.
     * @param indeks Broj indeksa studenta, npr. 100/IT-20.
     */
    public void obrisiStudenta(String indeks) {
        if (upisRepo.existsForStudent(indeks)) {
            throw new IllegalStateException("Student ima evidentirane upise/ocjene i ne može se obrisati.");
        }
        studentRepo.delete(indeks);
    }

    /**
     * Vrsi pretragu studenta po prefiksu
     * @param prefix Prefiks po kojem se vrsi pretraga, npr. S.
     * @return Lista studenata.
     */
    public List<Student> pretraziPoPrezimenuPrefix(String prefix) {
        if (prefix == null || prefix.isBlank()) {
            throw new IllegalArgumentException("Prezime/prefix ne smije biti prazno.");
        }
        return studentRepo.findByPrezimePrefix(prefix.trim());
    }

    /**
     * Vraca listu svih studenata u bazi podataka.
     * @return Lista studenata.
     */
    public List<Student> sviStudenti() {
        return studentRepo.findAll();
    }
}
